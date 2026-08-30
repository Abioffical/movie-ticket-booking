package com.cinewave.booking.model.enums;

/**
 * Mirrors the case lifecycle from the spec:
 * Booking Request -> Availability -> Approval -> Booking Execution -> Resolved
 */
public enum BookingStatus {
    AWAITING_CONFIRMATION,   // Availability stage passed, waiting on the customer (US-004)
    REJECTED_NO_SEATS,       // Availability stage failed (US-002)
    CONFIRMED,                // Booking Execution complete (US-007)
    CANCELLED                 // Customer declined at Approval (US-004)
}
