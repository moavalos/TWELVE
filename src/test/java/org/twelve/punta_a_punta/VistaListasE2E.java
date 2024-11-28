package org.twelve.punta_a_punta;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;
import org.twelve.punta_a_punta.vistas.VistaListas;
import org.twelve.punta_a_punta.vistas.VistaLogin;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class VistaListasE2E {

    static Playwright playwright;
    static Browser browser;
    BrowserContext context;
    VistaListas vistaListas;

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
            page.navigate("http://localhost:8081/spring/listas");
        });

        vistaListas = new VistaListas(page, 6);
    }

    @AfterEach
    void cerrarContexto() {
        if (context != null) {
            context.close();
        }
    }

    @Test
    void deberiaMostrarTituloListas() {
        String titulo = vistaListas.obtenerTextoDelElemento("h2.text-center");
        assertThat(titulo, equalTo("Mis Listas Colaborativas"));
    }

    @Test
    void deberiaMostrarBotonCrearLista() {
        boolean botonVisible = vistaListas.esBotonCrearListaVisible();
        assertThat(botonVisible, is(true));
    }

    @Test
    void deberiaMostrarMensajeSiNoHayListas() {
        boolean mensajeVisible = vistaListas.esMensajeNoListasVisible();
        assertThat(mensajeVisible, is(false));
    }
}
