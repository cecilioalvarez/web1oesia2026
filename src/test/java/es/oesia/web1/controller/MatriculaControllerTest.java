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

import es.oesia.web1.negocio.Alumno;
import es.oesia.web1.negocio.Curso;
import es.oesia.web1.negocio.Imparticion;
import es.oesia.web1.negocio.Matricula;
import es.oesia.web1.repository.CursoRepository;
import es.oesia.web1.servicio.MatriculaServicio;

@WebMvcTest(MatriculaController.class)
class MatriculaControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private MatriculaServicio matriculaServicio;

	// Necesario porque Web1Application#cargarDatosIniciales requiere un CursoRepository
	// y esta clase de configuración se carga incluso en un slice @WebMvcTest.
	@MockitoBean
	private CursoRepository cursoRepository;

	private Alumno alumno() {
		Alumno alumno = new Alumno("12345678A", "Juan", "Pérez García");
		alumno.setId(1L);
		return alumno;
	}

	private Imparticion imparticion(LocalDate fechaInicio, LocalDate fechaFin) {
		Curso curso = new Curso("Java desde cero", 40, "Fundamentos del lenguaje Java");
		curso.setId(1L);
		Imparticion imparticion = new Imparticion(fechaInicio, fechaFin, curso);
		imparticion.setId(2L);
		return imparticion;
	}

	@Test
	void listarMatriculasMuestraElAlumnoYSusMatriculas() throws Exception {
		Alumno alumno = alumno();
		Imparticion imparticion = imparticion(LocalDate.of(2026, 1, 12), LocalDate.of(2026, 2, 12));
		List<Matricula> matriculas = List.of(new Matricula(LocalDate.of(2026, 1, 15), alumno, imparticion));
		when(matriculaServicio.obtenerAlumno(1L)).thenReturn(alumno);
		when(matriculaServicio.listarMatriculasDeAlumno(1L)).thenReturn(matriculas);

		mockMvc.perform(get("/alumnos/1/matriculas"))
				.andExpect(status().isOk())
				.andExpect(view().name("listamatriculas"))
				.andExpect(model().attribute("alumno", alumno))
				.andExpect(model().attribute("matriculas", matriculas));
	}

	@Test
	void formularioNuevaMatriculaMuestraElAlumnoYLasImparticionesDisponibles() throws Exception {
		Alumno alumno = alumno();
		Imparticion disponible = imparticion(LocalDate.of(2026, 3, 1), LocalDate.of(2026, 4, 1));
		when(matriculaServicio.obtenerAlumno(1L)).thenReturn(alumno);
		when(matriculaServicio.listarImparticionesDisponibles(1L)).thenReturn(List.of(disponible));

		mockMvc.perform(get("/alumnos/1/matriculas/nueva"))
				.andExpect(status().isOk())
				.andExpect(view().name("nuevamatricula"))
				.andExpect(model().attribute("alumno", alumno))
				.andExpect(model().attribute("imparticionesDisponibles", List.of(disponible)));
	}

	@Test
	void insertarMatriculaConFechaEImparticionMatriculaYRedirigeAlListadoDeAlumnos() throws Exception {
		mockMvc.perform(post("/alumnos/1/matriculas")
				.param("fecha", "2026-01-15")
				.param("imparticionId", "2"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/alumnos"));

		verify(matriculaServicio, times(1)).matricular(eq(1L), eq(2L), eq(LocalDate.of(2026, 1, 15)));
	}

	@Test
	void insertarMatriculaSinImparticionVuelveAlFormularioConErrorSinMatricular() throws Exception {
		Alumno alumno = alumno();
		when(matriculaServicio.obtenerAlumno(1L)).thenReturn(alumno);
		when(matriculaServicio.listarImparticionesDisponibles(1L)).thenReturn(List.of());

		mockMvc.perform(post("/alumnos/1/matriculas")
				.param("fecha", "2026-01-15"))
				.andExpect(status().isOk())
				.andExpect(view().name("nuevamatricula"))
				.andExpect(model().attributeExists("error"));

		verify(matriculaServicio, never()).matricular(eq(1L), any(), any());
	}

	@Test
	void insertarMatriculaSinFechaVuelveAlFormularioConErrorSinMatricular() throws Exception {
		Alumno alumno = alumno();
		when(matriculaServicio.obtenerAlumno(1L)).thenReturn(alumno);
		when(matriculaServicio.listarImparticionesDisponibles(1L)).thenReturn(List.of());

		mockMvc.perform(post("/alumnos/1/matriculas")
				.param("imparticionId", "2"))
				.andExpect(status().isOk())
				.andExpect(view().name("nuevamatricula"))
				.andExpect(model().attributeExists("error"));

		verify(matriculaServicio, never()).matricular(eq(1L), any(), any());
	}

}
