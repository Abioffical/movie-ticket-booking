package com.cinewave.booking.service;

import com.cinewave.booking.model.BookingRequest;
import com.cinewave.booking.model.enums.BookingStatus;
import com.cinewave.booking.model.enums.SlaFlag;
import com.cinewave.booking.repository.BookingRequestRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * US-009: Define Booking SLA.
 * Background sweep so open cases are flagged/escalated even if nobody re-opens them:
 * goal missed (1 day) -> APPROACHING_DEADLINE, deadline missed (2 days) -> BREACHED + priority HIGH.
 */
@Service
public class SlaMonitorService {

    private static final Logger log = LoggerFactory.getLogger(SlaMonitorService.class);

    private final BookingRequestRepository bookingRepo;

    public SlaMonitorService(BookingRequestRepository bookingRepo) {
        this.bookingRepo = bookingRepo;
    }

    @Scheduled(fixedRate = 60 * 60 * 1000) // every hour
    public void sweep() {
        LocalDateTime now = LocalDateTime.now();
        List<BookingRequest> open = bookingRepo.findByBookingStatus(BookingStatus.AWAITING_CONFIRMATION);

        for (BookingRequest booking : open) {
            if (now.isAfter(booking.getSlaDeadlineAt()) && booking.getSlaFlag() != SlaFlag.BREACHED) {
                booking.setSlaFlag(SlaFlag.BREACHED);
                booking.setPriority("HIGH");
                bookingRepo.save(booking);
                log.warn("Booking #{} breached its SLA deadline — priority escalated to HIGH", booking.getId());
            } else if (now.isAfter(booking.getSlaGoalAt()) && booking.getSlaFlag() == SlaFlag.ON_TRACK) {
                booking.setSlaFlag(SlaFlag.APPROACHING_DEADLINE);
                bookingRepo.save(booking);
                log.info("Booking #{} missed its SLA goal — flagged as approaching deadline", booking.getId());
            }
        }
    }
}
