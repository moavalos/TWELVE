package org.twelve.presentacion.dto;

import org.twelve.dominio.entities.Categoria;
import org.twelve.dominio.entities.Movie;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MovieDTO {

    private Integer id;
    private String nombre;
    private String descripcion;
    private String frase;
    private Double duracion;
    private PaisDTO pais;
    private Integer cantVistas;
    private List<CategoriaDTO> categorias;
    private String anioLanzamiento;
    private String imagen;
    private Integer likes;
    private Double valoracion;
    private String director;
    private String escritor;
    private String idioma;
    private String tambienConocidaComo;

    public MovieDTO() {
    }

    public MovieDTO(Integer id, String nombre, String descripcion, String frase, Double duracion, PaisDTO pais, Integer cantVistas, List<CategoriaDTO> categorias, String anioLanzamiento, String imagen, Integer likes, Double valoracion, String director, String escritor, String idioma, String tambienConocidaComo) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.frase = frase;
        this.duracion = duracion;
        this.pais = pais;
        this.cantVistas = cantVistas;
        this.categorias = categorias;
        this.anioLanzamiento = anioLanzamiento;
        this.imagen = imagen;
        this.likes = likes;
        this.valoracion = valoracion;
        this.director = director;
        this.escritor = escritor;
        this.idioma = idioma;
        this.tambienConocidaComo = tambienConocidaComo;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFrase() {
        return frase;
    }

    public void setFrase(String frase) {
        this.frase = frase;
    }

    public Double getDuracion() {
        return duracion;
    }

    public void setDuracion(Double duracion) {
        this.duracion = duracion;
    }

    public PaisDTO getPais() {
        return pais;
    }

    public void setPais(PaisDTO pais) {
        this.pais = pais;
    }

    public Integer getCantVistas() {
        return cantVistas;
    }

    public void setCantVistas(Integer cantVistas) {
        this.cantVistas = cantVistas;
    }


    public List<CategoriaDTO> getCategorias() {
        return categorias;
    }

    public void setCategorias(List<CategoriaDTO> categorias) {
        this.categorias = categorias;
    }

    public String getAnioLanzamiento() {
        return anioLanzamiento;
    }

    public void setAnioLanzamiento(String anioLanzamiento) {
        this.anioLanzamiento = anioLanzamiento;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public Double getValoracion() {
        return valoracion;
    }

    public void setValoracion(Double valoracion) {
        this.valoracion = valoracion;
    }

    public Integer getId() {
        return id;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getDirector() {
        return director;
    }

    public void setEscritor(String escritor) {
        this.escritor = escritor;
    }

    public String getEscritor() {
        return escritor;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setTambienConocidaComo(String tambienConocidaComo) {
        this.tambienConocidaComo = tambienConocidaComo;
    }

    public String getTambienConocidaComo() {
        return tambienConocidaComo;
    }

    public static MovieDTO convertToDTO(Movie movie) {
        List<CategoriaDTO> categoriasDTOs = new ArrayList<>();
        for (Categoria categoria : movie.getCategorias()) {
            CategoriaDTO categoriaDTO = new CategoriaDTO();
            categoriaDTO.setId(categoria.getId());
            categoriaDTO.setNombre(categoria.getNombre());
            categoriasDTOs.add(categoriaDTO);
        }

        PaisDTO paisDTO = null;
        if (movie.getPais() != null) {
            paisDTO = PaisDTO.convertToDTO(movie.getPais());
        }



        return new MovieDTO(
                movie.getId(),
                movie.getNombre(),
                movie.getDescripcion(),
                movie.getFrase(),
                movie.getDuracion(),
                paisDTO,
                movie.getCantVistas(),
                categoriasDTOs,
                movie.getAñoLanzamiento(),
                movie.getImagen(),
                movie.getLikes(),
                movie.getValoracion(),
                movie.getDirector(),
                movie.getEscritor(),
                movie.getIdioma(),
                movie.getTambienConocidaComo()
        );
    }

    // dto a entidad en
    public static Movie convertToEntity(MovieDTO movieDTO) {



        Movie movie = new Movie();
        movie.setId(movieDTO.getId());
        movie.setNombre(movieDTO.getNombre());
        movie.setDescripcion(movieDTO.getDescripcion());
        movie.setFrase(movieDTO.getFrase());
        movie.setDuracion(movieDTO.getDuracion());
        movie.setCantVistas(movieDTO.getCantVistas());
        movie.setAñoLanzamiento(movieDTO.getAnioLanzamiento());
        movie.setImagen(movieDTO.getImagen());
        movie.setLikes(movieDTO.getLikes());
        movie.setValoracion(movieDTO.getValoracion());
        movie.setDirector(movieDTO.getDirector());
        movie.setEscritor(movieDTO.getEscritor());
        movie.setIdioma(movieDTO.getIdioma());
        movie.setTambienConocidaComo(movieDTO.getTambienConocidaComo());


        if (movieDTO.getPais() != null) {
            movie.setPais(PaisDTO.convertToEntity(movieDTO.getPais()));
        } else {
            movie.setPais(null);
        }

        if (movieDTO.getCategorias() != null) {
            Set<Categoria> categorias = new HashSet<>();
            for (CategoriaDTO categoriaDTO : movieDTO.getCategorias()) {
                Categoria categoria = new Categoria();
                categoria.setId(categoriaDTO.getId());
                categoria.setNombre(categoriaDTO.getNombre());
                categorias.add(categoria);
            }
            movie.setCategorias(categorias);
        }

        return movie;
    }
}
