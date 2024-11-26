package org.twelve.punta_a_punta;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;
import org.twelve.punta_a_punta.vistas.VistaLogin;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.Matchers.is;

public class VistaLoginE2E {

    static Playwright playwright;
    static Browser browser;
    BrowserContext context;
    VistaLogin vistaLogin;

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
        vistaLogin = new VistaLogin(page);
    }

    @AfterEach
    void cerrarContexto() {
        context.close();
    }

    @Test
    void deberiaMostrarTituloIniciarSesion() {
        String titulo = vistaLogin.obtenerTextoDelElemento("h1.text-center.mb-4.text-gray");
        assertThat(titulo, equalToIgnoringCase("Iniciar Sesión"));
    }

    @Test
    void deberiaMostrarLinkParaRegistrarse() {
        String linkRegistro = vistaLogin.obtenerTextoDelElemento("a.text-primary");
        assertThat(linkRegistro, equalToIgnoringCase("Registrarme"));
    }

    @Test
    void deberiaTenerBotonDeIniciarSesionVisible() {
        boolean botonVisible = vistaLogin.obtenerElemento("#btn-login").isVisible();
        assertThat(botonVisible, is(true));
    }
}
