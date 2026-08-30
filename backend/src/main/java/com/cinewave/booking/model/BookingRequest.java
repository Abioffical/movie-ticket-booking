package com.cinewave.booking.model;

import com.cinewave.booking.model.enums.*;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * The case type: "Movie Ticket Request".
 * One row = one end-to-end booking, carrying it through every stage
 * (Booking Request -> Availability -> Approval -> Booking Execution -> Resolved).
 */
@Entity
@Table(name = "booking_request")
public class BookingRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_name", nullable = false)
    private String customerName;

    @Column(name = "customer_email", nullable = false)
    private String customerEmail;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "show_id", nullable = false)
    private Show show;

    @Column(name = "number_of_tickets", nullable = false)
    private Integer numberOfTickets;

    // --- Availability stage (US-002) ---
    @Enumerated(EnumType.STRING)
    @Column(name = "seat_availability_status")
    private AvailabilityStatus seatAvailabilityStatus;

    @Column(name = "available_seats_count")
    private Integer availableSeatsCount;

    // --- Cost calculation (US-003) ---
    @Column(name = "ticket_price")
    private BigDecimal ticketPrice;

    @Column(name = "total_cost")
    private BigDecimal totalCost;

    // --- Approval stage (US-004) ---
    @Enumerated(EnumType.STRING)
    @Column(name = "booking_status", nullable = false)
    private BookingStatus bookingStatus;

    // --- Booking Execution stage (US-007, US-010) ---
    @Column(name = "seat_numbers")
    private String seatNumbers;

    @Column(name = "ticket_id")
    private String ticketId;

    @Column(name = "assigned_queue")
    private String assignedQueue;

    // --- SLA (US-009) ---
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "sla_goal_at")
    private LocalDateTime slaGoalAt;

    @Column(name = "sla_deadline_at")
    private LocalDateTime slaDeadlineAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "sla_flag")
    private SlaFlag slaFlag;

    @Column(name = "priority")
    private String priority;

    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;

    // --- Notification (US-008) ---
    @Column(name = "notification_sent")
    private Boolean notificationSent = false;

    public BookingRequest() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public String getCustomerEmail() { return customerEmail; }
    public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }
    public Show getShow() { return show; }
    public void setShow(Show show) { this.show = show; }
    public Integer getNumberOfTickets() { return numberOfTickets; }
    public void setNumberOfTickets(Integer numberOfTickets) { this.numberOfTickets = numberOfTickets; }
    public AvailabilityStatus getSeatAvailabilityStatus() { return seatAvailabilityStatus; }
    public void setSeatAvailabilityStatus(AvailabilityStatus seatAvailabilityStatus) { this.seatAvailabilityStatus = seatAvailabilityStatus; }
    public Integer getAvailableSeatsCount() { return availableSeatsCount; }
    public void setAvailableSeatsCount(Integer availableSeatsCount) { this.availableSeatsCount = availableSeatsCount; }
    public BigDecimal getTicketPrice() { return ticketPrice; }
    public void setTicketPrice(BigDecimal ticketPrice) { this.ticketPrice = ticketPrice; }
    public BigDecimal getTotalCost() { return totalCost; }
    public void setTotalCost(BigDecimal totalCost) { this.totalCost = totalCost; }
    public BookingStatus getBookingStatus() { return bookingStatus; }
    public void setBookingStatus(BookingStatus bookingStatus) { this.bookingStatus = bookingStatus; }
    public String getSeatNumbers() { return seatNumbers; }
    public void setSeatNumbers(String seatNumbers) { this.seatNumbers = seatNumbers; }
    public String getTicketId() { return ticketId; }
    public void setTicketId(String ticketId) { this.ticketId = ticketId; }
    public String getAssignedQueue() { return assignedQueue; }
    public void setAssignedQueue(String assignedQueue) { this.assignedQueue = assignedQueue; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getSlaGoalAt() { return slaGoalAt; }
    public void setSlaGoalAt(LocalDateTime slaGoalAt) { this.slaGoalAt = slaGoalAt; }
    public LocalDateTime getSlaDeadlineAt() { return slaDeadlineAt; }
    public void setSlaDeadlineAt(LocalDateTime slaDeadlineAt) { this.slaDeadlineAt = slaDeadlineAt; }
    public SlaFlag getSlaFlag() { return slaFlag; }
    public void setSlaFlag(SlaFlag slaFlag) { this.slaFlag = slaFlag; }
    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }
    public LocalDateTime getResolvedAt() { return resolvedAt; }
    public void setResolvedAt(LocalDateTime resolvedAt) { this.resolvedAt = resolvedAt; }
    public Boolean getNotificationSent() { return notificationSent; }
    public void setNotificationSent(Boolean notificationSent) { this.notificationSent = notificationSent; }
}
