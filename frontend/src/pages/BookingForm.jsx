import { useState } from 'react'
import { api } from '../api'

/** US-001: Submit Movie Ticket Request. */
export default function BookingForm({ show, onSubmitted, onBack }) {
  const [customerName, setCustomerName] = useState('')
  const [customerEmail, setCustomerEmail] = useState('')
  const [numberOfTickets, setNumberOfTickets] = useState(1)
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)

  const handleSubmit = async (e) => {
    e.preventDefault()
    setError('')
    if (numberOfTickets < 1) {
      setError('Number of tickets must be at least 1.')
      return
    }
    setLoading(true)
    try {
      const booking = await api.submitBooking({
        customerName,
        customerEmail,
        showId: show.id,
        numberOfTickets: Number(numberOfTickets),
      })
      onSubmitted(booking)
    } catch (e) {
      setError(e.message)
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="panel">
      <button className="link-btn" onClick={onBack}>← Back to shows</button>
      <h2>Book Tickets</h2>
      <p className="muted">
        {show.movie.movieName} · {show.showDate} · {show.showTime.slice(0, 5)} · {show.theatreName}
      </p>

      <form onSubmit={handleSubmit} className="form">
        <label>
          Full Name
          <input value={customerName} onChange={(e) => setCustomerName(e.target.value)} required />
        </label>
        <label>
          Email
          <input type="email" value={customerEmail} onChange={(e) => setCustomerEmail(e.target.value)} required />
        </label>
        <label>
          Number of Tickets
          <input
            type="number"
            min="1"
            max={show.availableSeats}
            value={numberOfTickets}
            onChange={(e) => setNumberOfTickets(e.target.value)}
            required
          />
        </label>

        {error && <p className="error">{error}</p>}

        <button type="submit" className="primary-btn" disabled={loading}>
          {loading ? 'Checking availability…' : 'Check Availability & Cost'}
        </button>
      </form>
    </div>
  )
}
