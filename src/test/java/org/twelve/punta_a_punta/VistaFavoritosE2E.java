package org.twelve.punta_a_punta;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.twelve.punta_a_punta.vistas.VistaFavoritos;
import org.twelve.punta_a_punta.vistas.VistaLogin;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class VistaFavoritosE2E {

    static Playwright playwright;
    static Browser browser;
    BrowserContext context;
    VistaFavoritos vistaFavoritos;

    @BeforeAll
    static void abrirNavegador() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false).setSlowMo(100));
    }

    @AfterAll
    static void cerrarNavegador() {
        if (playwright != null) {
            playwright.close();
        }
    }

    @BeforeEach
    void crearContextoYPagina() {
        context = browser.newContext();
        Page page = context.newPage();

        VistaLogin vistaLogin = new VistaLogin(page);
        vistaLogin.iniciarSesion("morae2e@gmail.com", "12345");

        page.waitForNavigation(() -> {
            page.navigate("http://localhost:8081/spring/favoritos");
        });

        vistaFavoritos = new VistaFavoritos(page);
    }

    @AfterEach
    void cerrarContexto() {
        if (context != null) {
            context.close();
        }
    }

    @Test
    void deberiaMostrarTituloFavoritos() {
        String titulo = vistaFavoritos.obtenerTextoDelElemento("h2.text-center");
        assertThat(titulo, equalTo("Películas Favoritas"));
    }

    @Test
    void deberiaMostrarMensajeSiNoHayFavoritos() {
        boolean mensajeVisible = vistaFavoritos.esMensajeNoFavoritosVisible();
        assertThat(mensajeVisible, is(true));
    }
}