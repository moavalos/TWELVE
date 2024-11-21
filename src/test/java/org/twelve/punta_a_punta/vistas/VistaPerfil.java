package org.twelve.punta_a_punta.vistas;

import com.microsoft.playwright.Page;

public class VistaPerfil extends VistaWeb{

    public VistaPerfil(Page page, int userId) {
        super(page);
        page.navigate("http://localhost:8081/spring/perfil/" + userId);
    }

    public String obtenerNombreUsuario() {
        return this.obtenerTextoDelElemento("h1.text-primary");
    }

    public String obtenerDescripcionUsuario() {
        return this.obtenerTextoDelElemento("p.mt-3.text-muted");
    }

    public boolean esBotonSeguirVisible() {
        return this.obtenerElemento(".follow-button button").isVisible();
    }

    public String obtenerTextoBotonSeguir() {
        return this.obtenerTextoDelElemento(".follow-button button");
    }

    public void darClickEnBotonSeguir() {
        this.darClickEnElElemento(".follow-button button");
    }

    public String obtenerCantidadPeliculasVistas() {
        return this.obtenerTextoDelElemento(".stat-item:nth-of-type(1) .stat-number");
    }

    public String obtenerCantidadVistasEsteAno() {
        return this.obtenerTextoDelElemento(".stat-item:nth-of-type(2) .stat-number");
    }

    public String obtenerCantidadSeguidores() {
        return this.obtenerTextoDelElemento(".stat-item:nth-of-type(3) .stat-number");
    }

    public String obtenerCantidadSiguiendo() {
        return this.obtenerTextoDelElemento(".stat-item:nth-of-type(4) .stat-number");
    }

    public boolean esSeccionFavoritosVisible() {
//        return this.obtenerElemento(".favorite-movies").isVisible();
        return this.obtenerElemento("div.favorite-movies:first-of-type").isVisible();

    }

    public boolean esSeccionWatchlistVisible() {
        return this.obtenerElemento(".watchlist").isVisible();
    }

    public boolean esSeccionActividadVisible() {
        return this.obtenerElemento(".recent-activity").isVisible();
    }
}
