package org.twelve.punta_a_punta.vistas;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class VistaUsuarioDatos extends VistaWeb {

    public VistaUsuarioDatos(Page page) {
        super(page);
        page.navigate("http://localhost:8081/spring/nuevo-usuario");
    }

    public void escribirEmail(String email) {
        this.escribirEnElElemento("input#email", email);
    }

    public void escribirContrasena(String password) {
        this.escribirEnElElemento("input#password", password);
    }

    public void escribirConfirmacionContrasena(String confirmPassword) {
        this.escribirEnElElemento("input#confirmPassword", confirmPassword);
    }

    public void escribirNombreCompleto(String nombre) {
        this.escribirEnElElemento("input#nombre", nombre);
    }

    public void escribirUsername(String username) {
        this.escribirEnElElemento("input#username", username);
    }

    public void seleccionarPais(String pais) {
        this.obtenerElemento("select#pais").selectOption(new String[]{pais});
    }

    public void darClickEnRegistrar() {
        this.darClickEnElElemento("button[type='submit']");
    }

    public boolean esVisibleError() {
        return this.obtenerElemento("p.alert.alert-danger").isVisible();
    }

    public String obtenerTextoDelElemento(String selector) {
        return this.obtenerElemento(selector).textContent();
    }

    public String obtenerValorCampo(String selector) {
        return this.obtenerElemento(selector).inputValue();
    }

    public void escribirEnElElemento(String selector, String texto) {
        Locator elemento = this.obtenerElemento(selector);
        elemento.fill(texto);
    }

    public Locator obtenerElemento(String selector) {
        return this.page.locator(selector);
    }

    public void darClickEnElElemento(String selector) {
        Locator elemento = this.obtenerElemento(selector);
        elemento.click();
    }
}
