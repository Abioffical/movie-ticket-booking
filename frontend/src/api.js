const BASE_URL = 'http://localhost:8080/api';

async function handle(res) {
  if (!res.ok) {
    const body = await res.json().catch(() => ({}));
    throw new Error(body.error || `Request failed (${res.status})`);
  }
  return res.json();
}

export const api = {
  getMovies: () => fetch(`${BASE_URL}/movies`).then(handle),
  getShowsByMovie: (movieId) => fetch(`${BASE_URL}/shows/movie/${movieId}`).then(handle),

  submitBooking: (payload) =>
    fetch(`${BASE_URL}/bookings`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(payload),
    }).then(handle),

  decide: (bookingId, decision) =>
    fetch(`${BASE_URL}/bookings/${bookingId}/decision`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ decision }),
    }).then(handle),

  getBooking: (id) => fetch(`${BASE_URL}/bookings/${id}`).then(handle),

  // Admin
  createMovie: (movie) =>
    fetch(`${BASE_URL}/movies`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(movie),
    }).then(handle),

  createShow: (show) =>
    fetch(`${BASE_URL}/shows`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(show),
    }).then(handle),
};
