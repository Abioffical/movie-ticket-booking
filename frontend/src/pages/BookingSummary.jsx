import { useState } from 'react'
import { api } from '../api'

/** US-002/US-003 results review + US-004 confirm/cancel + US-006 review layout. */
export default function BookingSummary({ booking, onDecided, onBack }) {
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)

  if (booking.seatAvailabilityStatus === 'UNAVAILABLE') {
    return (
      <div className="panel">
        <button className="link-btn" onClick={onBack}>← Back to shows</button>
        <h2>Not Enough Seats</h2>
        <p>
          Only {booking.availableSeatsCount} seat(s) are available for this show, but you requested{' '}
          {booking.numberOfTickets}. Please choose a different show or reduce the ticket count.
        </p>
      </div>
    )
  }

  const decide = async (decision) => {
    setLoading(true)
    setError('')
    try {
      const updated = await api.decide(booking.id, decision)
      onDecided(updated)
    } catch (e) {
      setError(e.message)
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="panel">
      <h2>Review Your Booking</h2>
      <div className="summary-grid">
        <div><span className="muted">Movie</span><strong>{booking.show.movie.movieName}</strong></div>
        <div><span className="muted">Show</span><strong>{booking.show.showDate} · {booking.show.showTime.slice(0,5)}</strong></div>
        <div><span className="muted">Theatre</span><strong>{booking.show.theatreName}</strong></div>
        <div><span className="muted">Tickets</span><strong>{booking.numberOfTickets}</strong></div>
        <div><span className="muted">Price / Ticket</span><strong>₹{booking.ticketPrice}</strong></div>
        <div className="total"><span className="muted">Total Cost</span><strong>₹{booking.totalCost}</strong></div>
      </div>

      {error && <p className="error">{error}</p>}

      <div className="action-row">
        <button className="ghost-btn" disabled={loading} onClick={() => decide('CANCEL')}>Cancel</button>
        <button className="primary-btn" disabled={loading} onClick={() => decide('CONFIRM')}>
          {loading ? 'Processing…' : 'Confirm Booking'}
        </button>
      </div>
    </div>
  )
}
