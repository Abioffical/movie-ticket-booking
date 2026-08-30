package com.cinewave.booking.controller;

import com.cinewave.booking.dto.BookingDecisionRequest;
import com.cinewave.booking.dto.BookingSubmitRequest;
import com.cinewave.booking.model.BookingRequest;
import com.cinewave.booking.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    /** US-001 + US-002 + US-003: submit a request; response already carries availability + cost. */
    @PostMapping
    public BookingRequest submit(@Valid @RequestBody BookingSubmitRequest req) {
        return bookingService.submitRequest(req);
    }

    /** US-004: customer confirms or cancels; triggers US-007/008/010 on CONFIRM. */
    @PostMapping("/{id}/decision")
    public BookingRequest decide(@PathVariable Long id, @Valid @RequestBody BookingDecisionRequest req) {
        return bookingService.decide(id, req.getDecision());
    }

    /** US-006: full detail for the review/status screen, with a live SLA flag (US-009). */
    @GetMapping("/{id}")
    public BookingRequest getOne(@PathVariable Long id) {
        return bookingService.getById(id);
    }

    @GetMapping
    public List<BookingRequest> getAll() {
        return bookingService.getAll();
    }
}
