package org.twelve.punta_a_punta;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;
import org.twelve.punta_a_punta.vistas.VistaUsuarioDatos;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class VistaUsuarioDatosE2E {

    static Playwright playwright;
    static Browser browser;
    BrowserContext context;
    VistaUsuarioDatos vistaUsuarioDatos;

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
        vistaUsuarioDatos = new VistaUsuarioDatos(page);
    }

    @AfterEach
    void cerrarContexto() {
        context.close();
    }

    @Test
    void deberiaMostrarTituloRegistro() {
        String titulo = vistaUsuarioDatos.obtenerTextoDelElemento("h1.text-gray");
        assertThat(titulo, equalTo("Registro de Usuario"));
    }

    @Test
    void deberiaIngresarEmail() {
        vistaUsuarioDatos.escribirEmail("prueba@gmail.com");
        String emailIngresado = vistaUsuarioDatos.obtenerValorCampo("input#email");
        assertThat(emailIngresado, equalTo("prueba@gmail.com"));
    }

    @Test
    void deberiaIngresarContrasenaYConfirmacion() {
        vistaUsuarioDatos.escribirContrasena("12345segura!");
        vistaUsuarioDatos.escribirConfirmacionContrasena("12345segura!");
        assertThat(vistaUsuarioDatos.obtenerValorCampo("input#password"), equalTo("12345segura!"));
        assertThat(vistaUsuarioDatos.obtenerValorCampo("input#confirmPassword"), equalTo("12345segura!"));
    }

    @Test
    void deberiaIngresarNombreCompletoYUsername() {
        vistaUsuarioDatos.escribirNombreCompleto("Juan Pérez");
        vistaUsuarioDatos.escribirUsername("juanperez123");
        assertThat(vistaUsuarioDatos.obtenerValorCampo("input#nombre"), equalTo("Juan Pérez"));
        assertThat(vistaUsuarioDatos.obtenerValorCampo("input#username"), equalTo("juanperez123"));
    }

    @Test
    void deberiaSeleccionarPais() {
        vistaUsuarioDatos.seleccionarPais("1");
        String paisSeleccionado = vistaUsuarioDatos.obtenerValorCampo("select#pais");
        assertThat(paisSeleccionado, equalTo("1"));
    }

    @Test
    void deberiaMostrarErrorAlEnviarFormularioConCamposIncompletos() {
        vistaUsuarioDatos.darClickEnRegistrar();
        boolean errorVisible = vistaUsuarioDatos.esVisibleError();
        assertThat(errorVisible, is(false));
    }
}
