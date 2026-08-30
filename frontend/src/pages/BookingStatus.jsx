/** US-007/US-008/US-009/US-010 result screen. */
export default function BookingStatus({ booking, onStartOver }) {
  const cancelled = booking.bookingStatus === 'CANCELLED'

  return (
    <div className="panel">
      {cancelled ? (
        <>
          <h2>Booking Cancelled</h2>
          <p>No charge was made. You can start a new booking anytime.</p>
        </>
      ) : (
        <>
          <h2>🎟 Booking Confirmed</h2>
          <div className="ticket">
            <div><span className="muted">Ticket ID</span><strong>{booking.ticketId}</strong></div>
            <div><span className="muted">Movie</span><strong>{booking.show.movie.movieName}</strong></div>
            <div><span className="muted">Show</span><strong>{booking.show.showDate} · {booking.show.showTime.slice(0,5)}</strong></div>
            <div><span className="muted">Seats</span><strong>{booking.seatNumbers}</strong></div>
            <div><span className="muted">Total Paid</span><strong>₹{booking.totalCost}</strong></div>
            <div><span className="muted">Routed To</span><strong>{booking.assignedQueue}</strong></div>
            <div><span className="muted">SLA Status</span><strong>{booking.slaFlag}</strong></div>
          </div>
          <p className="muted">A confirmation email has been sent to {booking.customerEmail}.</p>
        </>
      )}
      <button className="primary-btn" onClick={onStartOver}>Book Another</button>
    </div>
  )
}
