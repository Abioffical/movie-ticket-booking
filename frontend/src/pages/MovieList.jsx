import { useEffect, useState } from 'react'
import { api } from '../api'

export default function MovieList({ onSelectShow }) {
  const [movies, setMovies] = useState([])
  const [shows, setShows] = useState({})
  const [error, setError] = useState('')

  useEffect(() => {
    api.getMovies()
      .then(async (list) => {
        setMovies(list)
        const showMap = {}
        for (const movie of list) {
          showMap[movie.id] = await api.getShowsByMovie(movie.id)
        }
        setShows(showMap)
      })
      .catch((e) => setError(e.message))
  }, [])

  if (error) return <p className="error">Could not load movies: {error}</p>

  return (
    <div>
      <h2>Now Showing</h2>
      <div className="movie-grid">
        {movies.map((movie) => (
          <div className="movie-card" key={movie.id}>
            <h3>{movie.movieName}</h3>
            <p className="muted">{movie.genre} · {movie.language} · {movie.durationMinutes} min</p>
            <div className="show-list">
              {(shows[movie.id] || []).map((show) => (
                <button
                  key={show.id}
                  className={`show-chip ${show.showType === 'PREMIUM' ? 'premium' : ''}`}
                  disabled={show.availableSeats === 0}
                  onClick={() => onSelectShow(show)}
                >
                  {show.showDate} · {show.showTime.slice(0, 5)}
                  <span className="chip-sub">
                    {show.theatreName} · ₹{show.ticketPrice} · {show.availableSeats} seats left
                  </span>
                </button>
              ))}
            </div>
          </div>
        ))}
      </div>
    </div>
  )
}
