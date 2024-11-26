package org.twelve.punta_a_punta.vistas;

import com.microsoft.playwright.Page;

public class VistaFavoritos extends VistaWeb {

    public VistaFavoritos(Page page) {
        super(page);
        page.navigate("localhost:8081/spring/favoritos");
    }

    public boolean esMensajeNoFavoritosVisible() {
        return this.obtenerElemento(".no-favorites-message").isVisible();
    }

    public boolean esPeliculasGridVisible() {
        return this.obtenerElemento(".movies-grid").isVisible();
    }

    public int obtenerCantidadPeliculasFavoritas() {
        return this.obtenerElementos(".movies-grid .movie").size();
    }
}
