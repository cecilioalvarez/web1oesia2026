package es.oesia.web1.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
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

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import es.oesia.web1.negocio.Curso;
import es.oesia.web1.repository.CursoRepository;
import es.oesia.web1.servicio.CursosServicio;

@WebMvcTest(CursoController.class)
class CursoControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private CursosServicio cursosServicio;

	// Necesario porque Web1Application#cargarDatosIniciales requiere un CursoRepository
	// y esta clase de configuración se carga incluso en un slice @WebMvcTest.
	@MockitoBean
	private CursoRepository cursoRepository;

	@Test
	void listarCursosSinFiltroMuestraElListadoConLosCursosDelServicio() throws Exception {
		List<Curso> cursos = List.of(new Curso("Java desde cero", 40, "Fundamentos del lenguaje Java"));
		when(cursosServicio.listarCursos(isNull())).thenReturn(cursos);

		mockMvc.perform(get("/cursos"))
				.andExpect(status().isOk())
				.andExpect(view().name("listacursos"))
				.andExpect(model().attribute("cursos", cursos));
	}

	@Test
	void listarCursosConFiltroDeTituloLoPasaAlServicio() throws Exception {
		List<Curso> cursos = List.of(new Curso("Java desde cero", 40, "Fundamentos del lenguaje Java"));
		when(cursosServicio.listarCursos("java")).thenReturn(cursos);

		mockMvc.perform(get("/cursos").param("titulo", "java"))
				.andExpect(status().isOk())
				.andExpect(view().name("listacursos"))
				.andExpect(model().attribute("cursos", cursos))
				.andExpect(model().attribute("titulo", "java"));
	}

	@Test
	void formularioNuevoCursoMuestraElFormularioConUnCursoVacio() throws Exception {
		mockMvc.perform(get("/cursos/nuevo"))
				.andExpect(status().isOk())
				.andExpect(view().name("nuevocurso"))
				.andExpect(model().attributeExists("curso"));
	}

	@Test
	void insertarCursoConDatosValidosGuardaYRedirigeAlListado() throws Exception {
		mockMvc.perform(post("/cursos")
				.param("titulo", "Java desde cero")
				.param("duracion", "40")
				.param("descripcion", "Fundamentos del lenguaje Java"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/cursos"));

		verify(cursosServicio, times(1)).guardarCurso(any(Curso.class));
	}

	@Test
	void insertarCursoConDuracionFueraDeRangoVuelveAlFormularioSinGuardar() throws Exception {
		mockMvc.perform(post("/cursos")
				.param("titulo", "Java desde cero")
				.param("duracion", "201")
				.param("descripcion", "Fundamentos del lenguaje Java"))
				.andExpect(status().isOk())
				.andExpect(view().name("nuevocurso"))
				.andExpect(model().attributeHasFieldErrors("curso", "duracion"));

		verify(cursosServicio, never()).guardarCurso(any());
	}

	@Test
	void eliminarCursoDelegaEnElServicioYRedirigeAlListado() throws Exception {
		mockMvc.perform(post("/cursos/1/eliminar"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/cursos"));

		verify(cursosServicio, times(1)).eliminarCurso(eq(1L));
	}

}
