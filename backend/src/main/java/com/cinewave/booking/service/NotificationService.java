package com.cinewave.booking.service;

import com.cinewave.booking.model.BookingRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * US-008: Notify Booking Confirmation.
 * Stands in for a real Correspondence/Email rule. In production this would call
 * an email provider (JavaMailSender, SES, etc.) — here it renders the exact
 * template from the spec and logs it, which is enough to prove the trigger fires
 * at the right point in the lifecycle (case resolution).
 */
@Service
public class NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    public void sendBookingConfirmation(BookingRequest b) {
        String body = String.format(
            "Subject: Movie Ticket Booking Confirmed - Case #%d%n" +
            "Dear %s,%n" +
            "Your movie ticket booking has been successfully confirmed.%n" +
            "Case ID: %d%n" +
            "Movie Name: %s%n" +
            "Show Date & Time: %s %s%n" +
            "Number of Tickets: %d%n" +
            "Seat Numbers: %s%n" +
            "Total Cost: %s%n" +
            "Please arrive at the theatre before show time and present your booking details at entry.%n" +
            "Regards, CineWave Entertainment - Booking Support Team",
            b.getId(), b.getCustomerName(), b.getId(),
            b.getShow().getMovie().getMovieName(),
            b.getShow().getShowDate(), b.getShow().getShowTime(),
            b.getNumberOfTickets(), b.getSeatNumbers(), b.getTotalCost()
        );
        log.info("Sending booking confirmation email to {}\n{}", b.getCustomerEmail(), body);
    }
}
