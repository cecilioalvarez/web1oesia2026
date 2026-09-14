package es.oesia.web1.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import es.oesia.web1.negocio.Curso;
import es.oesia.web1.negocio.Imparticion;
import es.oesia.web1.repository.CursoRepository;
import es.oesia.web1.servicio.CursosServicio;

@WebMvcTest(ImparticionController.class)
class ImparticionControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private CursosServicio cursosServicio;

	// Necesario porque Web1Application#cargarDatosIniciales requiere un CursoRepository
	// y esta clase de configuración se carga incluso en un slice @WebMvcTest.
	@MockitoBean
	private CursoRepository cursoRepository;

	private Curso curso() {
		Curso curso = new Curso("Java desde cero", 40, "Fundamentos del lenguaje Java");
		curso.setId(1L);
		return curso;
	}

	@Test
	void formularioNuevaImparticionMuestraElCursoYUnaImparticionVacia() throws Exception {
		when(cursosServicio.obtenerCurso(1L)).thenReturn(curso());

		mockMvc.perform(get("/cursos/1/imparticiones/nueva"))
				.andExpect(status().isOk())
				.andExpect(view().name("nuevaimparticion"))
				.andExpect(model().attributeExists("curso"))
				.andExpect(model().attributeExists("imparticion"));
	}

	@Test
	void listarImparticionesMuestraLasImparticionesDelCurso() throws Exception {
		Curso curso = curso();
		Imparticion imparticion = new Imparticion(LocalDate.of(2026, 1, 12), LocalDate.of(2026, 2, 12), curso);
		curso.addImparticion(imparticion);
		when(cursosServicio.obtenerCurso(1L)).thenReturn(curso);

		mockMvc.perform(get("/cursos/1/imparticiones"))
				.andExpect(status().isOk())
				.andExpect(view().name("listaimparticiones"))
				.andExpect(model().attribute("curso", curso))
				.andExpect(model().attribute("imparticiones", List.of(imparticion)));
	}

	@Test
	void insertarImparticionConDatosValidosLaAnadeYRedirigeAlListadoDeCursos() throws Exception {
		mockMvc.perform(post("/cursos/1/imparticiones")
				.param("fechaInicio", "2026-01-12")
				.param("fechaFin", "2026-02-12"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/cursos"));

		verify(cursosServicio, times(1)).añadirImparticion(eq(1L), any(Imparticion.class));
	}

	@Test
	void insertarImparticionSinFechaInicioVuelveAlFormularioSinGuardar() throws Exception {
		when(cursosServicio.obtenerCurso(1L)).thenReturn(curso());

		mockMvc.perform(post("/cursos/1/imparticiones")
				.param("fechaFin", "2026-02-12"))
				.andExpect(status().isOk())
				.andExpect(view().name("nuevaimparticion"))
				.andExpect(model().attributeHasFieldErrors("imparticion", "fechaInicio"));

		verify(cursosServicio, never()).añadirImparticion(any(), any());
	}

	@Test
	void formularioEditarImparticionMuestraElCursoYLaImparticionObtenidaDelServicio() throws Exception {
		Curso curso = curso();
		Imparticion imparticion = new Imparticion(LocalDate.of(2026, 1, 12), LocalDate.of(2026, 2, 12), curso);
		imparticion.setId(5L);
		when(cursosServicio.obtenerCurso(1L)).thenReturn(curso);
		when(cursosServicio.obtenerImparticion(5L)).thenReturn(imparticion);

		mockMvc.perform(get("/cursos/1/imparticiones/5/editar"))
				.andExpect(status().isOk())
				.andExpect(view().name("editarimparticion"))
				.andExpect(model().attribute("curso", curso))
				.andExpect(model().attribute("imparticion", imparticion));
	}

	@Test
	void editarImparticionConDatosValidosActualizaYRedirigeAlListadoDeImparticiones() throws Exception {
		mockMvc.perform(post("/cursos/1/imparticiones/5/editar")
				.param("fechaInicio", "2026-03-01")
				.param("fechaFin", "2026-04-01"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/cursos/1/imparticiones"));

		verify(cursosServicio, times(1)).actualizarImparticion(eq(5L), any(Imparticion.class));
	}

	@Test
	void editarImparticionSinFechaFinVuelveAlFormularioSinActualizar() throws Exception {
		when(cursosServicio.obtenerCurso(1L)).thenReturn(curso());

		mockMvc.perform(post("/cursos/1/imparticiones/5/editar")
				.param("fechaInicio", "2026-03-01"))
				.andExpect(status().isOk())
				.andExpect(view().name("editarimparticion"))
				.andExpect(model().attributeHasFieldErrors("imparticion", "fechaFin"));

		verify(cursosServicio, never()).actualizarImparticion(any(), any());
	}

	@Test
	void eliminarImparticionDelegaEnElServicioYRedirigeAlListadoDeImparticiones() throws Exception {
		mockMvc.perform(post("/cursos/1/imparticiones/5/eliminar"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/cursos/1/imparticiones"));

		verify(cursosServicio, times(1)).eliminarImparticion(5L);
	}

}
