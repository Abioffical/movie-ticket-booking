package com.cinewave.booking.service;

import com.cinewave.booking.dto.BookingDecisionRequest;
import com.cinewave.booking.dto.BookingSubmitRequest;
import com.cinewave.booking.model.BookingRequest;
import com.cinewave.booking.model.Show;
import com.cinewave.booking.model.enums.*;
import com.cinewave.booking.repository.BookingRequestRepository;
import com.cinewave.booking.repository.ShowRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

@Service
public class BookingService {

    private final BookingRequestRepository bookingRepo;
    private final ShowRepository showRepo;
    private final NotificationService notificationService;
    private final Random random = new Random();

    public BookingService(BookingRequestRepository bookingRepo, ShowRepository showRepo,
                           NotificationService notificationService) {
        this.bookingRepo = bookingRepo;
        this.showRepo = showRepo;
        this.notificationService = notificationService;
    }

    /**
     * US-001 (submit) + US-002 (availability) + US-003 (cost) all happen here,
     * mirroring the "Booking Request" -> "Availability" stage transition in one call,
     * since there is no separate human step in between on the website.
     */
    @Transactional
    public BookingRequest submitRequest(BookingSubmitRequest req) {
        Show show = showRepo.findById(req.getShowId())
            .orElseThrow(() -> new NoSuchElementException("Show not found: " + req.getShowId()));

        BookingRequest booking = new BookingRequest();
        booking.setCustomerName(req.getCustomerName());
        booking.setCustomerEmail(req.getCustomerEmail());
        booking.setShow(show);
        booking.setNumberOfTickets(req.getNumberOfTickets());
        booking.setCreatedAt(LocalDateTime.now());

        // US-009: SLA goal (1 day) / deadline (2 days) from case creation
        booking.setSlaGoalAt(booking.getCreatedAt().plusDays(1));
        booking.setSlaDeadlineAt(booking.getCreatedAt().plusDays(2));
        booking.setSlaFlag(SlaFlag.ON_TRACK);
        booking.setPriority("NORMAL");

        // US-002: Check Show Availability
        booking.setAvailableSeatsCount(show.getAvailableSeats());
        if (show.getAvailableSeats() >= req.getNumberOfTickets()) {
            booking.setSeatAvailabilityStatus(AvailabilityStatus.AVAILABLE);

            // US-003: Calculate Booking Cost
            booking.setTicketPrice(show.getTicketPrice());
            BigDecimal totalCost = show.getTicketPrice().multiply(BigDecimal.valueOf(req.getNumberOfTickets()));
            booking.setTotalCost(totalCost);

            booking.setBookingStatus(BookingStatus.AWAITING_CONFIRMATION);
        } else {
            booking.setSeatAvailabilityStatus(AvailabilityStatus.UNAVAILABLE);
            booking.setBookingStatus(BookingStatus.REJECTED_NO_SEATS);
            booking.setResolvedAt(LocalDateTime.now());
        }

        return bookingRepo.save(booking);
    }

    /**
     * US-004: Confirm Booking Request (Approval stage).
     * CONFIRM -> US-007 (process booking) + US-010 (route by show type) + US-008 (notify).
     * CANCEL  -> resolved immediately, no further processing.
     */
    @Transactional
    public BookingRequest decide(Long bookingId, BookingDecisionRequest.Decision decision) {
        BookingRequest booking = bookingRepo.findById(bookingId)
            .orElseThrow(() -> new NoSuchElementException("Booking not found: " + bookingId));

        if (booking.getBookingStatus() != BookingStatus.AWAITING_CONFIRMATION) {
            throw new IllegalStateException("Booking is not awaiting a decision (current status: "
                + booking.getBookingStatus() + ")");
        }

        if (decision == BookingDecisionRequest.Decision.CANCEL) {
            booking.setBookingStatus(BookingStatus.CANCELLED);
            booking.setResolvedAt(LocalDateTime.now());
            return bookingRepo.save(booking);
        }

        // --- US-007: Process Ticket Booking ---
        Show show = booking.getShow();
        show.setAvailableSeats(show.getAvailableSeats() - booking.getNumberOfTickets());
        showRepo.save(show);

        booking.setSeatNumbers(allocateSeats(booking.getNumberOfTickets(), show));
        booking.setTicketId(generateTicketId(booking));
        booking.setBookingStatus(BookingStatus.CONFIRMED);

        // --- US-010: Route Booking Request by Show Type ---
        booking.setAssignedQueue(routeByShowType(show.getShowType()));

        booking.setResolvedAt(LocalDateTime.now());
        BookingRequest saved = bookingRepo.save(booking);

        // --- US-008: Notify Booking Confirmation ---
        notificationService.sendBookingConfirmation(saved);
        saved.setNotificationSent(true);
        return bookingRepo.save(saved);
    }

    /** US-010: Decision-table equivalent — Premium shows go to the premium queue, everything else to standard. */
    private String routeByShowType(ShowType showType) {
        return showType == ShowType.PREMIUM ? "PremiumShowQueue" : "StandardShowQueue";
    }

    private String allocateSeats(int count, Show show) {
        String rowLetters = "ABCDEFGHJKLM";
        char row = rowLetters.charAt(random.nextInt(rowLetters.length()));
        int startSeat = random.nextInt(Math.max(show.getSeatCapacity() - count, 1)) + 1;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            if (i > 0) sb.append(", ");
            sb.append(row).append(startSeat + i);
        }
        return sb.toString();
    }

    private String generateTicketId(BookingRequest booking) {
        return "TKT-" + booking.getShow().getId() + "-" + booking.getId() + "-"
            + (1000 + random.nextInt(9000));
    }

    /**
     * US-009: recompute the SLA flag/priority on read, so status is always accurate
     * even between scheduler runs. See SlaMonitorService for the background sweep
     * that persists the same logic for open cases.
     */
    public BookingRequest refreshSlaFlag(BookingRequest booking) {
        if (booking.getResolvedAt() != null) return booking;
        LocalDateTime now = LocalDateTime.now();
        if (now.isAfter(booking.getSlaDeadlineAt())) {
            booking.setSlaFlag(SlaFlag.BREACHED);
            booking.setPriority("HIGH");
        } else if (now.isAfter(booking.getSlaGoalAt())) {
            booking.setSlaFlag(SlaFlag.APPROACHING_DEADLINE);
        }
        return booking;
    }

    public BookingRequest getById(Long id) {
        BookingRequest booking = bookingRepo.findById(id)
            .orElseThrow(() -> new NoSuchElementException("Booking not found: " + id));
        return refreshSlaFlag(booking);
    }

    public List<BookingRequest> getAll() {
        return bookingRepo.findAll();
    }
}
