package org.twelve.presentacion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.twelve.dominio.CategoriaService;
import org.twelve.dominio.MovieService;
import org.twelve.presentacion.dto.CategoriaDTO;
import org.twelve.presentacion.dto.MovieDTO;

import java.util.List;

@Controller
public class MovieController {

    private MovieService movieService;

    private CategoriaService categoriaService;

    @Autowired

    public MovieController(MovieService movieService, CategoriaService categoriaService) {
        this.movieService = movieService;
        this.categoriaService = categoriaService;
    }

    @RequestMapping(path = "/movies", method = RequestMethod.GET)
    public ModelAndView getAllMoviesView(
            @RequestParam(value = "idCategoria", required = false) Integer idCategoria,
            @RequestParam(value = "filter", required = false) String filter) {

        List<MovieDTO> movies;
        if ("topRated".equals(filter)) {
            movies = movieService.getMovieByValoracion();
        } else if ("newest".equals(filter)) {
            movies = movieService.getMovieByAnio();
        } else if (idCategoria != null) {
            movies = movieService.getMoviesByCategory(idCategoria);
        } else {
            movies = movieService.getAll();
        }

        List<CategoriaDTO> categorias = categoriaService.getAll();

        ModelMap modelo = new ModelMap();
        modelo.put("movies", movies);
        modelo.put("categorias", categorias);
        modelo.put("selectedFilter", filter);

        return new ModelAndView("movies", modelo);
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

    // GET /movies/category?idCategoria=ID_CATEGORIA
    @RequestMapping(path = "/movies/category", method = RequestMethod.GET)
    public ResponseEntity<List<MovieDTO>> getMoviesByCategory(@RequestParam Integer idCategoria) {
        List<MovieDTO> movies = movieService.getMoviesByCategory(idCategoria);
        if (movies != null && !movies.isEmpty()) {
            return ResponseEntity.ok(movies);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
