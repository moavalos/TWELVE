package org.twelve.presentacion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.twelve.dominio.MovieService;
import org.twelve.presentacion.dto.MovieDTO;

import java.util.List;

@Controller
public class MovieController {

    private MovieService movieService;

    @Autowired
    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @RequestMapping(path = "/movies", method = RequestMethod.GET)
    public ModelAndView getAllMoviesView() {
        List<MovieDTO> movies = movieService.getAll();

        ModelMap modelo = new ModelMap();
        modelo.put("movies", movies);

        return new ModelAndView("movies", modelo);
    }


    @RequestMapping(path = "/top-rated-movies", method = RequestMethod.GET)
    public ModelAndView getTopRatedMoviesView() {
        List<MovieDTO> topRated = movieService.getMovieByValoracion();

        ModelMap modelo = new ModelMap();
        modelo.put("topRated", topRated);

        return new ModelAndView("topRatedMovies", modelo);
    }

    @RequestMapping(path = "/newestMovies", method = RequestMethod.GET)
    public ModelAndView getNewestMoviesView() {
        List<MovieDTO> newestMovies = movieService.getMovieByAnio();

        ModelMap modelo = new ModelMap();
        modelo.put("newestMovies", newestMovies);

        return new ModelAndView("newestMovies", modelo);
    }


    // TODO modifidcar estos endpoints

    /*@RequestMapping(path = "/getAll", method = RequestMethod.GET)
    public ResponseEntity<List<Movie>> getAllMovies(@ModelAttribute("movie") MovieDTO datosMovie, HttpServletRequest request) {
        List<Movie> movies = movieService.getAll();
        return ResponseEntity.ok(movies);
        return null;
    }*/

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<MovieDTO> getMovieById(@PathVariable Integer id) {
        MovieDTO movieDTO = movieService.getById(id);
        if (movieDTO != null) {
            return ResponseEntity.ok(movieDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @RequestMapping(path = "/agregar]", method = RequestMethod.POST)
    public ResponseEntity<MovieDTO> addMovie(@RequestBody MovieDTO movie) {
        MovieDTO createdMovie = movieService.create(movie);
        return ResponseEntity.ok(createdMovie);
    }

    @RequestMapping(path = "/search", method = RequestMethod.GET)
    public ResponseEntity<MovieDTO> searchMovies(@RequestParam String title) {
        MovieDTO movie = movieService.searchByTitle(title);
        if (movie != null) {
            return ResponseEntity.ok(movie);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @RequestMapping(path = "/most-viewed", method = RequestMethod.GET)
    public ResponseEntity<List<MovieDTO>> getMostViewedMovies() {
        List<MovieDTO> movies = movieService.getMovieMasVista();
        return ResponseEntity.ok(movies);
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<MovieDTO> updateMovie(@PathVariable Integer id, @RequestBody MovieDTO movie) {
        MovieDTO existingMovie = movieService.getById(id);
        if (existingMovie != null) {
            existingMovie.setNombre(movie.getNombre());
            existingMovie.setDescripcion(movie.getDescripcion());
            existingMovie.setFrase(movie.getFrase());
            existingMovie.setDuracion(movie.getDuracion());
            existingMovie.setPais(movie.getPais());
            existingMovie.setCantVistas(movie.getCantVistas());
            existingMovie.setAnioLanzamiento(movie.getAnioLanzamiento());
            existingMovie.setImagen(movie.getImagen());
            existingMovie.setLikes(movie.getLikes());
            existingMovie.setValoracion(movie.getValoracion());
            MovieDTO updatedMovie = movieService.create(existingMovie);
            return ResponseEntity.ok(updatedMovie);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
