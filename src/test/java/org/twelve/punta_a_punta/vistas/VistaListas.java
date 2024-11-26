package org.twelve.punta_a_punta.vistas;

import com.microsoft.playwright.Page;

public class VistaListas extends VistaWeb{

    public VistaListas(Page page, int userId) {
        super(page);
        page.navigate("localhost:8081/spring/listas/" + userId);
    }

    public boolean esBotonCrearListaVisible() {
        return this.obtenerElemento("a.btn-success").isVisible();
    }

    public boolean esMensajeNoListasVisible() {
        return this.obtenerElemento(".text-muted").isVisible();
    }

    public boolean esSeccionListasVisible() {
        return this.obtenerElemento(".row").isVisible();
    }

    public int obtenerCantidadListas() {
        return this.obtenerElementos(".row .card").size();
    }
}
