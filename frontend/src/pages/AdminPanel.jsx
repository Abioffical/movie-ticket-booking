import { useEffect, useState } from 'react'
import { api } from '../api'

/** Staff-facing screen: "Enable staff to manage show details and seating availability". */
export default function AdminPanel({ onBack }) {
  const [movies, setMovies] = useState([])
  const [movieForm, setMovieForm] = useState({ movieName: '', genre: '', language: '', durationMinutes: '' })
  const [showForm, setShowForm] = useState({
    movieId: '', showDate: '', showTime: '', theatreName: '', seatCapacity: '', showType: 'STANDARD', ticketPrice: '',
  })
  const [message, setMessage] = useState('')

  const loadMovies = () => api.getMovies().then(setMovies)
  useEffect(() => { loadMovies() }, [])

  const submitMovie = async (e) => {
    e.preventDefault()
    await api.createMovie({
      ...movieForm,
      durationMinutes: Number(movieForm.durationMinutes) || null,
    })
    setMovieForm({ movieName: '', genre: '', language: '', durationMinutes: '' })
    setMessage('Movie added.')
    loadMovies()
  }

  const submitShow = async (e) => {
    e.preventDefault()
    await api.createShow({
      movie: { id: Number(showForm.movieId) },
      showDate: showForm.showDate,
      showTime: showForm.showTime,
      theatreName: showForm.theatreName,
      seatCapacity: Number(showForm.seatCapacity),
      showType: showForm.showType,
      ticketPrice: Number(showForm.ticketPrice),
    })
    setMessage('Show added.')
  }

  return (
    <div className="panel">
      <button className="link-btn" onClick={onBack}>← Back to bookings</button>
      <h2>Staff: Manage Movies & Shows</h2>
      {message && <p className="success">{message}</p>}

      <h3>Add Movie</h3>
      <form onSubmit={submitMovie} className="form">
        <input placeholder="Movie name" value={movieForm.movieName}
          onChange={(e) => setMovieForm({ ...movieForm, movieName: e.target.value })} required />
        <input placeholder="Genre" value={movieForm.genre}
          onChange={(e) => setMovieForm({ ...movieForm, genre: e.target.value })} />
        <input placeholder="Language" value={movieForm.language}
          onChange={(e) => setMovieForm({ ...movieForm, language: e.target.value })} />
        <input type="number" placeholder="Duration (min)" value={movieForm.durationMinutes}
          onChange={(e) => setMovieForm({ ...movieForm, durationMinutes: e.target.value })} />
        <button type="submit" className="primary-btn">Add Movie</button>
      </form>

      <h3>Add Show</h3>
      <form onSubmit={submitShow} className="form">
        <select value={showForm.movieId} onChange={(e) => setShowForm({ ...showForm, movieId: e.target.value })} required>
          <option value="">Select movie…</option>
          {movies.map((m) => <option key={m.id} value={m.id}>{m.movieName}</option>)}
        </select>
        <input type="date" value={showForm.showDate}
          onChange={(e) => setShowForm({ ...showForm, showDate: e.target.value })} required />
        <input type="time" value={showForm.showTime}
          onChange={(e) => setShowForm({ ...showForm, showTime: e.target.value })} required />
        <input placeholder="Theatre name" value={showForm.theatreName}
          onChange={(e) => setShowForm({ ...showForm, theatreName: e.target.value })} required />
        <input type="number" placeholder="Seat capacity" value={showForm.seatCapacity}
          onChange={(e) => setShowForm({ ...showForm, seatCapacity: e.target.value })} required />
        <select value={showForm.showType} onChange={(e) => setShowForm({ ...showForm, showType: e.target.value })}>
          <option value="STANDARD">Standard</option>
          <option value="PREMIUM">Premium</option>
        </select>
        <input type="number" placeholder="Ticket price" value={showForm.ticketPrice}
          onChange={(e) => setShowForm({ ...showForm, ticketPrice: e.target.value })} required />
        <button type="submit" className="primary-btn">Add Show</button>
      </form>
    </div>
  )
}
