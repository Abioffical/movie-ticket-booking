package com.cinewave.booking.controller;

import com.cinewave.booking.model.Movie;
import com.cinewave.booking.repository.MovieRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** Staff-facing "manage show details" surface from the scenario. */
@RestController
@RequestMapping("/api/movies")
public class MovieController {

    private final MovieRepository movieRepo;

    public MovieController(MovieRepository movieRepo) {
        this.movieRepo = movieRepo;
    }

    @GetMapping
    public List<Movie> getAll() {
        return movieRepo.findAll();
    }

    @PostMapping
    public Movie create(@RequestBody Movie movie) {
        return movieRepo.save(movie);
    }

    @PutMapping("/{id}")
    public Movie update(@PathVariable Long id, @RequestBody Movie updated) {
        Movie movie = movieRepo.findById(id).orElseThrow();
        movie.setMovieName(updated.getMovieName());
        movie.setGenre(updated.getGenre());
        movie.setLanguage(updated.getLanguage());
        movie.setDurationMinutes(updated.getDurationMinutes());
        return movieRepo.save(movie);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        movieRepo.deleteById(id);
    }
}
