INSERT IGNORE INTO movie (id, movie_name, genre, language, duration_minutes) VALUES
  (1, 'Galaxy Raiders', 'Sci-Fi', 'English', 142),
  (2, 'The Last Monsoon', 'Drama', 'Tamil', 128),
  (3, 'Circuit Breaker', 'Thriller', 'English', 118);

INSERT IGNORE INTO movie_shows (id, movie_id, show_date, show_time, theatre_name, seat_capacity, available_seats, show_type, ticket_price) VALUES
  (1, 1, CURRENT_DATE + INTERVAL 1 DAY, '18:30:00', 'CineWave IMAX - Screen 1', 120, 120, 'PREMIUM', 350.00),
  (2, 1, CURRENT_DATE + INTERVAL 1 DAY, '21:45:00', 'CineWave IMAX - Screen 1', 120, 6, 'PREMIUM', 350.00),
  (3, 2, CURRENT_DATE + INTERVAL 2 DAY, '16:00:00', 'CineWave Multiplex - Screen 3', 80, 80, 'STANDARD', 180.00),
  (4, 3, CURRENT_DATE + INTERVAL 1 DAY, '19:00:00', 'CineWave Multiplex - Screen 2', 100, 100, 'STANDARD', 200.00);
