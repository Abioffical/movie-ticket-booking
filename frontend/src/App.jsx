import { useState } from 'react'
import MovieList from './pages/MovieList.jsx'
import BookingForm from './pages/BookingForm.jsx'
import BookingSummary from './pages/BookingSummary.jsx'
import BookingStatus from './pages/BookingStatus.jsx'
import AdminPanel from './pages/AdminPanel.jsx'

// Simple state machine mirroring the case lifecycle:
// movies -> booking (US-001) -> summary (US-002/003/004/006) -> status (US-007/008/009/010)
export default function App() {
  const [view, setView] = useState('movies')
  const [selectedShow, setSelectedShow] = useState(null)
  const [booking, setBooking] = useState(null)

  const reset = () => {
    setView('movies')
    setSelectedShow(null)
    setBooking(null)
  }

  return (
    <div className="app-shell">
      <header className="topbar">
        <h1 onClick={reset}>🎬 CineWave</h1>
        {view !== 'admin' && <button className="link-btn" onClick={() => setView('admin')}>Staff Admin</button>}
      </header>

      <main className="content">
        {view === 'movies' && (
          <MovieList onSelectShow={(show) => { setSelectedShow(show); setView('booking') }} />
        )}

        {view === 'booking' && selectedShow && (
          <BookingForm
            show={selectedShow}
            onBack={() => setView('movies')}
            onSubmitted={(b) => { setBooking(b); setView('summary') }}
          />
        )}

        {view === 'summary' && booking && (
          <BookingSummary
            booking={booking}
            onBack={() => setView('movies')}
            onDecided={(b) => { setBooking(b); setView('status') }}
          />
        )}

        {view === 'status' && booking && (
          <BookingStatus booking={booking} onStartOver={reset} />
        )}

        {view === 'admin' && <AdminPanel onBack={reset} />}
      </main>
    </div>
  )
}
