package org.twelve.punta_a_punta;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;
import org.twelve.punta_a_punta.vistas.VistaLogin;
import org.twelve.punta_a_punta.vistas.VistaPerfil;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class VistaPerfilE2E {

    static Playwright playwright;
    static Browser browser;
    BrowserContext context;
    VistaPerfil vistaPerfil;

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

        // se inicia sesion antes de acceder a la vista de perfil
        VistaLogin vistaLogin = new VistaLogin(page);
        vistaLogin.iniciarSesion("morae2e@gmail.com", "12345");

        page.waitForNavigation(() -> {
            page.navigate("http://localhost:8081/spring/perfil");
        });

        // ahora si se accede a la vista perfil y le pasamos el id del usuario q inició sesion
        vistaPerfil = new VistaPerfil(page, 7);
    }

    @AfterEach
    void cerrarContexto() {
        context.close();
    }

    @Test
    void deberiaMostrarNombreDeUsuario() {
        String nombreUsuario = vistaPerfil.obtenerNombreUsuario();
        assertThat(nombreUsuario, not(isEmptyOrNullString()));
    }

    @Test
    void deberiaMostrarDescripcionDeUsuario() {
        String descripcionUsuario = vistaPerfil.obtenerDescripcionUsuario();
        assertThat(descripcionUsuario, not(isEmptyOrNullString()));
    }

    @Test
    void deberiaMostrarCantidadDePeliculasVistas() {
        String cantidadPeliculas = vistaPerfil.obtenerCantidadPeliculasVistas();
        assertThat(cantidadPeliculas, not(isEmptyOrNullString()));
    }

    @Test
    void deberiaMostrarCantidadDePeliculasVistasEsteAno() {
        String cantidadEsteAno = vistaPerfil.obtenerCantidadVistasEsteAno();
        assertThat(cantidadEsteAno, not(isEmptyOrNullString()));
    }

    @Test
    void deberiaMostrarCantidadDeSeguidores() {
        String cantidadSeguidores = vistaPerfil.obtenerCantidadSeguidores();
        assertThat(cantidadSeguidores, not(isEmptyOrNullString()));
    }

    @Test
    void deberiaMostrarCantidadDeSiguiendo() {
        String cantidadSiguiendo = vistaPerfil.obtenerCantidadSiguiendo();
        assertThat(cantidadSiguiendo, not(isEmptyOrNullString()));
    }

    @Test
    void deberiaMostrarSeccionFavoritos() {
        boolean favoritosVisible = vistaPerfil.esSeccionFavoritosVisible();
        assertThat(favoritosVisible, is(true));
    }

    @Test
    void deberiaMostrarSeccionWatchlist() {
        boolean watchlistVisible = vistaPerfil.esSeccionWatchlistVisible();
        assertThat(watchlistVisible, is(true));
    }

    @Test
    void deberiaMostrarSeccionActividadReciente() {
        boolean actividadVisible = vistaPerfil.esSeccionActividadVisible();
        assertThat(actividadVisible, is(true));
    }
}
