package com.cinewave.booking.repository;

import com.cinewave.booking.model.BookingRequest;
import com.cinewave.booking.model.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRequestRepository extends JpaRepository<BookingRequest, Long> {
    List<BookingRequest> findByBookingStatus(BookingStatus status);
    List<BookingRequest> findByCustomerEmailIgnoreCase(String email);
}
