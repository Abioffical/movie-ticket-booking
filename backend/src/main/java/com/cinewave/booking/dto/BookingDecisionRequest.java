package com.cinewave.booking.dto;

import jakarta.validation.constraints.NotNull;

/** Payload for US-004: Confirm or Cancel a booking request. */
public class BookingDecisionRequest {

    public enum Decision { CONFIRM, CANCEL }

    @NotNull
    private Decision decision;

    public Decision getDecision() { return decision; }
    public void setDecision(Decision decision) { this.decision = decision; }
}
