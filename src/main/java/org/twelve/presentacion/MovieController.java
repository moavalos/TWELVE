package org.twelve.presentacion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.twelve.dominio.*;
import org.twelve.presentacion.dto.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class MovieController {

    private final MovieService movieService;

    @Lazy
    private final CategoriaService categoriaService;
    private final ComentarioService comentarioService;
    private final UsuarioService usuarioService;
    private final PaisService paisService;
    private final ListaColaborativaService listaColaborativaService;

    @Autowired
    public MovieController(MovieService movieService, CategoriaService categoriaService, ComentarioService comentarioService, UsuarioService usuarioService, PaisService paisService, ListaColaborativaService listaColaborativaService) {
        this.movieService = movieService;
        this.categoriaService = categoriaService;
        this.comentarioService = comentarioService;
        this.usuarioService = usuarioService;
        this.paisService = paisService;
        this.listaColaborativaService = listaColaborativaService;
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
    public ModelAndView traerDetallePelicula(@PathVariable("id") Integer id, HttpServletRequest request) {
        Integer usuarioLogueadoId = (Integer) request.getSession().getAttribute("usuarioId");

        MovieDTO movie = movieService.getById(id);

        long likesActualizados = usuarioService.obtenerCantidadDeLikes(movie);
        movie.setLikes((int) likesActualizados);

        //lista de comentarios
        List<ComentarioDTO> comentarios = comentarioService.obtenerComentariosPorPelicula(id);
        List<MovieDTO> similarMovies = movieService.getSimilarMovies(id);
        PerfilDTO usuario = usuarioService.buscarPorId(usuarioLogueadoId);
        boolean haDadoLike = usuario != null && usuarioService.haDadoLike(usuario, movie);
        boolean enListaVerMasTarde = usuario != null && usuarioService.estaEnListaVerMasTarde(usuario, movie);
        List<ListaColaborativaDTO> listasColaborativas = listaColaborativaService.obtenerListasPorUsuario(usuario.getId());

        //modelo
        ModelMap modelo = new ModelMap();
        modelo.put("movie", movie);
        modelo.put("comentarios", comentarios);
        // modelo.put("usuario", new PerfilDTO());
        modelo.put("peliculasSimilares", similarMovies);
        modelo.put("usuario", usuario);
        modelo.put("haDadoLike", haDadoLike);
        modelo.put("enListaVerMasTarde", enListaVerMasTarde);
        modelo.put("listasColaborativas", listasColaborativas);

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
        modelo.addAttribute("categoryId", id);
        return new ModelAndView("movies-categoria", modelo);
    }

    @RequestMapping(path = "/movie/{id}/like", method = RequestMethod.POST)
    public String darMeGusta(@PathVariable("id") Integer movieId, HttpServletRequest request) {
        Integer usuarioLogueadoId = (Integer) request.getSession().getAttribute("usuarioId");

        if (usuarioLogueadoId == null) {
            return "redirect:/login";
        }

        PerfilDTO usuario = usuarioService.buscarPorId(usuarioLogueadoId);
        MovieDTO movie = movieService.getById(movieId);

        if (usuario != null && movie != null) {
            usuarioService.guardarMeGusta(usuario, movie);
        }

        return "redirect:/detalle-pelicula/" + movieId;
    }

    @RequestMapping(path = "/pais/{id}", method = RequestMethod.GET)
    public ModelAndView getMoviesByPaisPage(@PathVariable Integer id, @RequestParam(required = false) String filter) {
        ModelMap modelo = new ModelMap();
        List<MovieDTO> movies = movieService.getMoviesByPais(id, filter);
        String nombrePais = paisService.findById(id).getNombre();

        modelo.addAttribute("movies", movies);
        modelo.addAttribute("selectedFilter", filter);
        modelo.addAttribute("nombrePais", nombrePais);
        return new ModelAndView("movies-pais", modelo);
    }

    @RequestMapping(path = "/movie/{id}/verMasTarde", method = RequestMethod.POST)
    public String verMasTarde(@PathVariable("id") Integer id, HttpServletRequest request) {
        Integer usuarioLogueadoId = (Integer) request.getSession().getAttribute("usuarioId");

        if (usuarioLogueadoId == null) {
            return "redirect:/login";
        }

        MovieDTO movie = movieService.getById(id);
        PerfilDTO usuario = usuarioService.buscarPorId(usuarioLogueadoId);

        if (usuario != null && movie != null) {
            usuarioService.agregarEnVerMasTarde(usuario, movie);
        }

        return "redirect:/detalle-pelicula/" + id;
    }

    @RequestMapping(path = "/movie/{id}/agregar-a-lista", method = RequestMethod.POST)
    public ModelAndView agregarPeliculaALista(@PathVariable Integer id,
                                              @RequestParam Integer listaColaborativaId,
                                              HttpServletRequest request) {
        ModelMap model = new ModelMap();
        Integer usuarioLogueadoId = (Integer) request.getSession().getAttribute("usuarioId");

        try {
            listaColaborativaService.agregarPeliculaALista(listaColaborativaId, id, usuarioLogueadoId);
            model.put("success", "Película agregada a la lista con éxito.");
        } catch (RuntimeException e) {
            model.put("error", e.getMessage());
        }

        MovieDTO movie = movieService.getById(id);
        List<ListaColaborativaDTO> listasColaborativas = listaColaborativaService.obtenerListasPorUsuario(usuarioLogueadoId);
        List<ComentarioDTO> comentarios = comentarioService.obtenerComentariosPorPelicula(id);
        List<MovieDTO> similarMovies = movieService.getSimilarMovies(id);

        model.put("movie", movie);
        model.put("listasColaborativas", listasColaborativas);
        model.put("comentarios", comentarios);
        model.put("peliculasSimilares", similarMovies);

        return new ModelAndView("detalle-pelicula", model);
    }

}
