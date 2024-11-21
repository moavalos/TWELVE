package org.twelve.dominio;

import org.twelve.dominio.entities.Movie;
import org.twelve.dominio.entities.Usuario;
import org.twelve.dominio.entities.UsuarioMovie;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface UsuarioMovieRepository {

    Integer obtenerCantidadPeliculasVistas(Integer usuarioId);

    Integer obtenerCantidadPeliculasVistasEsteAno(Integer usuarioId);

    List<Movie> obtenerPeliculasFavoritas(Integer usuarioId);

    long obtenerCantidadDeLikes(Movie movie);

    Optional<UsuarioMovie> buscarMeGustaPorUsuario(Usuario usuario, Movie movie);

    void guardar(UsuarioMovie usuarioMovie);

    void borrarMeGusta(UsuarioMovie usuarioMovie);

    List<Object[]> buscarPeliculasDondeElUsuarioTuvoInteraccion(Integer usuarioId);

    Optional<UsuarioMovie> buscarVerMasTardePorUsuario(Usuario usuario, Movie movie);

    void borrarVerMasTarde(UsuarioMovie usuarioMovie);

    List<UsuarioMovie> obtenerPeliculasVerMasTarde(Integer usuarioId);
}
