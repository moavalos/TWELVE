package org.twelve.servicesImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.twelve.dominio.PaisRepository;
import org.twelve.dominio.entities.Pais;
import org.twelve.dominio.serviceImpl.PaisServiceImpl;
import org.twelve.presentacion.dto.PaisDTO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PaisServiceImplTest {

    private Pais pais1;
    private Pais pais2;
    private HttpServletRequest requestMock;
    private HttpSession sessionMock;
    private PaisServiceImpl paisServiceImpl;
    private PaisRepository paisRepository;

    @BeforeEach
    public void init() {
        pais1 = mock(Pais.class);
        pais2 = mock(Pais.class);
        requestMock = mock(HttpServletRequest.class);
        sessionMock = mock(HttpSession.class);
        paisRepository = mock(PaisRepository.class);
        paisServiceImpl = new PaisServiceImpl(paisRepository);

        when(pais1.getNombre()).thenReturn("Argentina");
        when(pais2.getNombre()).thenReturn("Brasil");
    }

    @Test
    public void testFindAllCuandoHayPaisesDeberiaRetornarListaDePaisDTO() {
        when(paisRepository.findAll()).thenReturn(Arrays.asList(pais1, pais2));

        List<PaisDTO> result = paisServiceImpl.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Argentina", result.get(0).getNombre());
        assertEquals("Brasil", result.get(1).getNombre());

        verify(paisRepository, times(1)).findAll();
    }

    @Test
    public void testFindAllCuandoNoHayPaisesDeberiaRetornarListaVacia() {
        when(paisRepository.findAll()).thenReturn(List.of());

        List<PaisDTO> result = paisServiceImpl.findAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(paisRepository, times(1)).findAll();
    }

    @Test
    public void testConvertToDTODeberiaConvertirPaisCorrectamente() {
        PaisDTO result = paisServiceImpl.convertToDTO(pais1);

        assertNotNull(result);
        assertEquals(pais1.getId(), result.getId());
        assertEquals(pais1.getNombre(), result.getNombre());
    }
}
