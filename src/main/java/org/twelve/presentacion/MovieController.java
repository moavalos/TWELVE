package org.twelve.presentacion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.twelve.dominio.MovieService;
import org.twelve.dominio.entities.Movie;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class MovieController {

    private MovieService movieService;

    @Autowired
    public MovieController(MovieService movieService){
        this.movieService = movieService;
    }

    @RequestMapping(path = "/movies", method = RequestMethod.GET)
    public ModelAndView getAllMoviesView() {
        List<Movie> movies = movieService.getAll();

        ModelMap modelo = new ModelMap();
        modelo.put("movies", movies);

        return new ModelAndView("movies", modelo);
    }

    // TODO modifidcar estos endpoints

    //@RequestMapping(path = "/getAll", method = RequestMethod.GET)
    public ResponseEntity<List<Movie>> getAllMovies(@ModelAttribute("movie") DatosMovie datosMovie, HttpServletRequest request) {
        List<Movie> movies = movieService.getAll();
        return ResponseEntity.ok(movies);
    }

    @RequestMapping(path = "/{id}]", method = RequestMethod.GET)
    public ResponseEntity<Movie> getMovieById(@PathVariable Integer id) {
        Movie movie = movieService.getById(id);
        if (movie != null) {
            return ResponseEntity.ok(movie);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @RequestMapping(path = "/agregar]", method = RequestMethod.POST)
    public ResponseEntity<Movie> addMovie(@RequestBody Movie movie) {
        Movie createdMovie = movieService.create(movie);
        return ResponseEntity.ok(createdMovie);
    }

    @RequestMapping(path = "/search", method = RequestMethod.GET)
    public ResponseEntity<Movie> searchMovies(@RequestParam String title) {
        Movie movie = movieService.searchByTitle(title);
        if (movie != null) {
            return ResponseEntity.ok(movie);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @RequestMapping(path = "/most-viewed", method = RequestMethod.GET)
    public ResponseEntity<List<Movie>> getMostViewedMovies() {
        List<Movie> movies = movieService.getMovieMasVista();
        return ResponseEntity.ok(movies);
    }

    @RequestMapping(path = "/top-rated", method = RequestMethod.GET)
    public ResponseEntity<List<Movie>> getTopRatedMovies() {
        List<Movie> movies = movieService.getMovieByValoracion();
        return ResponseEntity.ok(movies);
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Movie> updateMovie(@PathVariable Integer id, @RequestBody Movie movie) {
        Movie existingMovie = movieService.getById(id);
        if (existingMovie != null) {
            existingMovie.setNombre(movie.getNombre());
            existingMovie.setDescripcion(movie.getDescripcion());
            existingMovie.setFrase(movie.getFrase());
            existingMovie.setDuracion(movie.getDuracion());
            existingMovie.setPais(movie.getPais());
            existingMovie.setCantVistas(movie.getCantVistas());
            existingMovie.setAñoLanzamiento(movie.getAñoLanzamiento());
            existingMovie.setImagen(movie.getImagen());
            existingMovie.setLikes(movie.getLikes());
            existingMovie.setValoracion(movie.getValoracion());
            Movie updatedMovie = movieService.create(existingMovie);
            return ResponseEntity.ok(updatedMovie);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
