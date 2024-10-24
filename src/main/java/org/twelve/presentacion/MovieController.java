package org.twelve.presentacion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.twelve.dominio.CategoriaService;
import org.twelve.dominio.ComentarioService;
import org.twelve.dominio.MovieService;
import org.twelve.dominio.UsuarioService;
import org.twelve.presentacion.dto.CategoriaDTO;
import org.twelve.presentacion.dto.ComentarioDTO;
import org.twelve.presentacion.dto.MovieDTO;
import org.twelve.presentacion.dto.PerfilDTO;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class MovieController {

    private final MovieService movieService;

    @Lazy
    private final CategoriaService categoriaService;
    private final ComentarioService comentarioService;
    private final UsuarioService usuarioService;

    @Autowired
    public MovieController(MovieService movieService, CategoriaService categoriaService, ComentarioService comentarioService, UsuarioService usuarioService) {
        this.movieService = movieService;
        this.categoriaService = categoriaService;
        this.comentarioService = comentarioService;
        this.usuarioService = usuarioService;
    }

    @RequestMapping(path = "/home", method = RequestMethod.GET)
    public ModelAndView getTopRatedMovies() {
        List<MovieDTO> topMovies = movieService.getMovieByValoracion().stream()
                .limit(4) // limita 4 peli nomas
                .collect(Collectors.toList());

        List<PerfilDTO> perfiles = usuarioService.encontrarTodos();

        ModelMap modelo = new ModelMap();
        modelo.put("movies", topMovies);
        modelo.put("perfiles", perfiles);

        return new ModelAndView("home", modelo);
    }

    @RequestMapping(path = "/movies", method = RequestMethod.GET)
    public ModelAndView getAllMoviesView(
            @RequestParam(value = "idCategoria", required = false) Integer idCategoria,
            @RequestParam(value = "filter", required = false) String filter) {

        List<MovieDTO> movies;
        if ("topRated".equals(filter))
            movies = movieService.getMovieByValoracion();
        else if ("newest".equals(filter))
            movies = movieService.getMovieByAnio();
        else if (idCategoria != null)
            movies = movieService.getMoviesByCategory(idCategoria);
        else
            movies = movieService.getAll();

        List<CategoriaDTO> categorias = categoriaService.getAll();

        ModelMap modelo = new ModelMap();
        modelo.put("movies", movies);
        modelo.put("categorias", categorias);
        modelo.put("selectedFilter", filter);

        return new ModelAndView("movies", modelo);
    }

    @RequestMapping(path = "/detalle-pelicula/{id}", method = RequestMethod.GET)
    public ModelAndView getMovieDetails(@PathVariable("id") Integer id) {

        MovieDTO movie = movieService.getById(id);
        //lista de comentarios
        List<ComentarioDTO> comentarios = comentarioService.obtenerComentariosPorPelicula(id);

        //modelo
        ModelMap modelo = new ModelMap();
        modelo.put("movie", movie);
        modelo.put("comentarios", comentarios);
        modelo.put("usuario", new PerfilDTO());

        return new ModelAndView("detalle-pelicula", modelo);
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<MovieDTO> getMovieById(@PathVariable Integer id) {
        MovieDTO movieDTO = movieService.getById(id);
        if (movieDTO != null)
            return ResponseEntity.ok(movieDTO);
        else
            return ResponseEntity.notFound().build();
    }

    @RequestMapping(path = "/agregar]", method = RequestMethod.POST)
    public ResponseEntity<MovieDTO> addMovie(@RequestBody MovieDTO movie) {
        MovieDTO createdMovie = movieService.create(movie);
        return ResponseEntity.ok(createdMovie);
    }

    @RequestMapping(path = "/search", method = RequestMethod.GET)
    public ModelAndView searchMovies(@RequestParam("title") String title) {
        List<MovieDTO> movies = movieService.searchByTitle(title);
        ModelMap modelo = new ModelMap();

        if (!movies.isEmpty()) {
            modelo.addAttribute("movies", movies);
        } else {
            modelo.addAttribute("message", "No se encontraron películas con el título proporcionado.");
        }

        return new ModelAndView("search-results", modelo);
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
        } else
            return ResponseEntity.notFound().build();
    }

    // GET /movies/category?idCategoria=ID_CATEGORIA
    @RequestMapping(path = "/movies/category", method = RequestMethod.GET)
    public ResponseEntity<List<MovieDTO>> getMoviesByCategory(@RequestParam Integer idCategoria) {
        List<MovieDTO> movies = movieService.getMoviesByCategory(idCategoria);
        if (movies != null && !movies.isEmpty())
            return ResponseEntity.ok(movies);
        else
            return ResponseEntity.notFound().build();
    }

    @RequestMapping(path = "/categoria/{id}", method = RequestMethod.GET)
    public ModelAndView getMovieCategoryPage(@PathVariable Integer id, @RequestParam(required = false) String filter) {
        ModelMap modelo = new ModelMap();
        List<MovieDTO> movies = movieService.getMoviesByCategory(id, filter);

        modelo.addAttribute("movies", movies);
        modelo.addAttribute("selectedFilter", filter);
        return new ModelAndView("movies-categoria", modelo);
    }
}
