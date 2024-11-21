package org.twelve.punta_a_punta.vistas;

import com.microsoft.playwright.Page;

public class VistaHome extends VistaWeb {

    public VistaHome(Page page) {
        super(page);
        page.navigate("localhost:8081/spring/home");
    }

    public String obtenerTituloDeSeguimientoDePeliculas() {
        return this.obtenerTextoDelElemento("h1.display-4.mb-4");
    }

    public String obtenerTituloDePeliculasPopulares() {
//        return this.obtenerTextoDelElemento(".container .row.mt-4 h2.text-primary.mb-3");
        return this.obtenerTextoDelElemento(".container .row.mt-4 h2.text-primary.mb-3:has-text('Populares')");

    }

    public String obtenerTituloDePerfiles() {
        return this.obtenerTextoDelElemento(".container .container.mt-5 h2.text-primary.mb-3");
    }

    public void darClickEnVerDetallesDePrimeraPelicula() {
        this.obtenerElemento(".row.mt-4 .card .btn-outline-primary").first().click();
    }

    public void darClickEnVerPerfilDePrimerUsuario() {
        this.obtenerElemento(".container.mt-5 .card a.btn-outline-primary").first().click();
    }

    public boolean esVisibleBotonVerMasPeliculas() {
        return this.obtenerElemento("a.btn-primary.rounded-pill[href='movies.html']").isVisible();
    }

    public boolean esVisibleBotonMasResenas() {
        return this.obtenerElemento(".row.mt-5 a.btn-outline-primary.rounded-pill").isVisible();
    }
}
