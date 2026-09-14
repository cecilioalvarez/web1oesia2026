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

import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import es.oesia.web1.negocio.Alumno;
import es.oesia.web1.repository.CursoRepository;
import es.oesia.web1.servicio.AlumnoServicio;

@WebMvcTest(AlumnoController.class)
class AlumnoControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private AlumnoServicio alumnoServicio;

	// Necesario porque Web1Application#cargarDatosIniciales requiere un CursoRepository
	// y esta clase de configuración se carga incluso en un slice @WebMvcTest.
	@MockitoBean
	private CursoRepository cursoRepository;

	@Test
	void listarAlumnosMuestraElListadoConLosAlumnosDelServicio() throws Exception {
		List<Alumno> alumnos = List.of(new Alumno("12345678A", "Juan", "Pérez García"));
		when(alumnoServicio.listarAlumnos()).thenReturn(alumnos);

		mockMvc.perform(get("/alumnos"))
				.andExpect(status().isOk())
				.andExpect(view().name("listaalumnos"))
				.andExpect(model().attribute("alumnos", alumnos));
	}

	@Test
	void formularioNuevoAlumnoMuestraElFormularioConUnAlumnoVacio() throws Exception {
		mockMvc.perform(get("/alumnos/nuevo"))
				.andExpect(status().isOk())
				.andExpect(view().name("nuevoalumno"))
				.andExpect(model().attributeExists("alumno"));
	}

	@Test
	void insertarAlumnoConDatosValidosMuestraLaVistaDeConfirmacionConElAlumnoGuardado() throws Exception {
		Alumno alumnoGuardado = new Alumno("12345678A", "Juan", "Pérez García");
		alumnoGuardado.setId(1L);
		when(alumnoServicio.guardarAlumno(any(Alumno.class))).thenReturn(alumnoGuardado);

		mockMvc.perform(post("/alumnos")
				.param("dni", "12345678A")
				.param("nombre", "Juan")
				.param("apellidos", "Pérez García"))
				.andExpect(status().isOk())
				.andExpect(view().name("alumnoguardado"))
				.andExpect(model().attribute("alumno", alumnoGuardado));

		verify(alumnoServicio, times(1)).guardarAlumno(any(Alumno.class));
	}

	@Test
	void insertarAlumnoConDniEnBlancoVuelveAlFormularioSinGuardar() throws Exception {
		mockMvc.perform(post("/alumnos")
				.param("dni", "")
				.param("nombre", "Juan")
				.param("apellidos", "Pérez García"))
				.andExpect(status().isOk())
				.andExpect(view().name("nuevoalumno"))
				.andExpect(model().attributeHasFieldErrors("alumno", "dni"));

		verify(alumnoServicio, never()).guardarAlumno(any());
	}

	@Test
	void formularioEditarAlumnoMuestraElAlumnoObtenidoDelServicio() throws Exception {
		Alumno alumno = new Alumno("12345678A", "Juan", "Pérez García");
		alumno.setId(1L);
		when(alumnoServicio.obtenerAlumno(1L)).thenReturn(alumno);

		mockMvc.perform(get("/alumnos/1/editar"))
				.andExpect(status().isOk())
				.andExpect(view().name("editaralumno"))
				.andExpect(model().attribute("alumno", alumno));
	}

	@Test
	void formularioEditarAlumnoConIdInexistenteDevuelveError() throws Exception {
		when(alumnoServicio.obtenerAlumno(999L)).thenThrow(new NoSuchElementException());

		mockMvc.perform(get("/alumnos/999/editar"))
				.andExpect(status().is3xxRedirection());
	}

	@Test
	void editarAlumnoConDatosValidosActualizaYRedirigeAlListado() throws Exception {
		mockMvc.perform(post("/alumnos/1/editar")
				.param("dni", "12345678A")
				.param("nombre", "Juan Carlos")
				.param("apellidos", "Pérez Gómez"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/alumnos"));

		verify(alumnoServicio, times(1)).actualizarAlumno(eq(1L), any(Alumno.class));
	}

	@Test
	void editarAlumnoConNombreEnBlancoVuelveAlFormularioSinActualizar() throws Exception {
		mockMvc.perform(post("/alumnos/1/editar")
				.param("dni", "12345678A")
				.param("nombre", "")
				.param("apellidos", "Pérez Gómez"))
				.andExpect(status().isOk())
				.andExpect(view().name("editaralumno"))
				.andExpect(model().attributeHasFieldErrors("alumno", "nombre"));

		verify(alumnoServicio, never()).actualizarAlumno(any(), any());
	}

	@Test
	void eliminarAlumnoDelegaEnElServicioYRedirigeAlListado() throws Exception {
		mockMvc.perform(post("/alumnos/1/eliminar"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/alumnos"));

		verify(alumnoServicio, times(1)).eliminarAlumno(1L);
	}

}
