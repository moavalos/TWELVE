package org.twelve.punta_a_punta;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;
import org.twelve.punta_a_punta.vistas.VistaHome;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class VistaHomeE2E {

    static Playwright playwright;
    static Browser browser;
    BrowserContext context;
    VistaHome vistaHome;

    @BeforeAll
    static void abrirNavegador() {
        playwright = Playwright.create();
        //browser = playwright.chromium().launch(); // crear una instancia de navegador por cada test
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false).setSlowMo(100));
    }

    @AfterAll
    static void cerrarNavegador() {
        playwright.close();
    }

    @BeforeEach
    void crearContextoYPagina() {
        context = browser.newContext();
        Page page = context.newPage();
        vistaHome = new VistaHome(page);
    }

    @AfterEach
    void cerrarContexto() {
        context.close();
    }

    @Test
    void deberiaMostrarTituloDeSeguimientoDePeliculas() {
        String titulo = vistaHome.obtenerTituloDeSeguimientoDePeliculas();
        assertThat(titulo, equalToIgnoringCase("Crea tu propio seguimiento de películas"));
    }

    @Test
    void deberiaMostrarSeccionDePeliculasPopulares() {
        String tituloSeccion = vistaHome.obtenerTituloDePeliculasPopulares();
        assertThat(tituloSeccion, equalToIgnoringCase("Populares"));
    }

    @Test
    void deberiaNavegarADetalleDePrimeraPelicula() {
        vistaHome.darClickEnVerDetallesDePrimeraPelicula();
        String urlActual = vistaHome.obtenerURLActual();
        assertThat(urlActual, containsString("/detalle-pelicula/"));
    }

    @Test
    void deberiaMostrarSeccionDePerfiles() {
        String tituloPerfiles = vistaHome.obtenerTituloDePerfiles();
        assertThat(tituloPerfiles, equalToIgnoringCase("Perfiles"));
    }

    @Test
    void deberiaTenerBotonVerMasPeliculasVisible() {
        boolean botonVisible = vistaHome.esVisibleBotonVerMasPeliculas();
        assertThat(botonVisible, is(true));
    }

    @Test
    void deberiaTenerBotonMasResenasVisible() {
        boolean botonVisible = vistaHome.esVisibleBotonMasResenas();
        assertThat(botonVisible, is(true));
    }
}
