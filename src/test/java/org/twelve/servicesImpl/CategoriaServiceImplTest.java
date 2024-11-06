package org.twelve.servicesImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.twelve.dominio.CategoriaRepository;
import org.twelve.dominio.entities.Categoria;
import org.twelve.dominio.serviceImpl.CategoriaServiceImpl;
import org.twelve.presentacion.dto.CategoriaDTO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CategoriaServiceImplTest {

    private Categoria categoriaMock;
    private Categoria categoriaMock2;
    private HttpServletRequest requestMock;
    private HttpSession sessionMock;
    private CategoriaServiceImpl categoriaServiceImpl;
    private CategoriaRepository categoriaRepository;

    @BeforeEach
    public void init() {
        categoriaMock = mock(Categoria.class);
        categoriaMock2 = mock(Categoria.class);
        requestMock = mock(HttpServletRequest.class);
        sessionMock = mock(HttpSession.class);
        categoriaRepository = mock(CategoriaRepository.class);
        categoriaServiceImpl = new CategoriaServiceImpl(categoriaRepository);
    }

    @Test
    public void testGetAllCuandoHayCategoriasDeberiaRetornarListaDeCategoriaDTO() {
        when(categoriaMock.getId()).thenReturn(1);
        when(categoriaMock.getNombre()).thenReturn("Tecnología");

        when(categoriaMock2.getId()).thenReturn(2);
        when(categoriaMock2.getNombre()).thenReturn("Ciencia");

        when(categoriaRepository.findAll()).thenReturn(Arrays.asList(categoriaMock, categoriaMock2));

        List<CategoriaDTO> result = categoriaServiceImpl.getAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Tecnología", result.get(0).getNombre());
        assertEquals("Ciencia", result.get(1).getNombre());

        verify(categoriaRepository, times(1)).findAll();
    }

    @Test
    public void testGetAllCuandoNoHayCategoriasDeberiaRetornarListaVacia() {
        when(categoriaRepository.findAll()).thenReturn(List.of());

        List<CategoriaDTO> result = categoriaServiceImpl.getAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(categoriaRepository, times(1)).findAll();
    }

    @Test
    public void testConvertToDTODeberiaConvertirCategoriaCorrectamente() {
        Categoria categoria = new Categoria();
        categoria.setId(1);
        categoria.setNombre("Arte");

        CategoriaDTO dto = CategoriaDTO.convertToDTO(categoria);

        assertNotNull(dto);
        assertEquals(categoria.getId(), dto.getId());
        assertEquals(categoria.getNombre(), dto.getNombre());
    }
}
