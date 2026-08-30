package com.cinewave.booking.controller;

import com.cinewave.booking.model.Show;
import com.cinewave.booking.repository.MovieRepository;
import com.cinewave.booking.repository.ShowRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shows")
public class ShowController {

    private final ShowRepository showRepo;
    private final MovieRepository movieRepo;

    public ShowController(ShowRepository showRepo, MovieRepository movieRepo) {
        this.showRepo = showRepo;
        this.movieRepo = movieRepo;
    }

    @GetMapping
    public List<Show> getAll() {
        return showRepo.findAll();
    }

    @GetMapping("/movie/{movieId}")
    public List<Show> getByMovie(@PathVariable Long movieId) {
        return showRepo.findByMovieId(movieId);
    }

    /** US-005: shows are created against an existing reusable Movie record. */
    @PostMapping
    public Show create(@RequestBody Show show) {
        show.setMovie(movieRepo.findById(show.getMovie().getId()).orElseThrow());
        if (show.getAvailableSeats() == null) {
            show.setAvailableSeats(show.getSeatCapacity());
        }
        return showRepo.save(show);
    }

    @PutMapping("/{id}")
    public Show update(@PathVariable Long id, @RequestBody Show updated) {
        Show show = showRepo.findById(id).orElseThrow();
        show.setShowDate(updated.getShowDate());
        show.setShowTime(updated.getShowTime());
        show.setTheatreName(updated.getTheatreName());
        show.setSeatCapacity(updated.getSeatCapacity());
        show.setAvailableSeats(updated.getAvailableSeats());
        show.setShowType(updated.getShowType());
        show.setTicketPrice(updated.getTicketPrice());
        return showRepo.save(show);
    }
}
