package es.oesia.web1.servicio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import es.oesia.web1.negocio.Alumno;
import es.oesia.web1.repository.AlumnoRepository;

@ExtendWith(MockitoExtension.class)
class AlumnoServicioTest {

	@Mock
	private AlumnoRepository alumnoRepository;

	@InjectMocks
	private AlumnoServicio alumnoServicio;

	@Test
	void listarAlumnosDevuelveTodosLosAlumnosDelRepositorio() {
		List<Alumno> alumnos = List.of(new Alumno("12345678A", "Juan", "Pérez García"),
				new Alumno("87654321B", "María", "López Sánchez"));
		when(alumnoRepository.findAll()).thenReturn(alumnos);

		List<Alumno> resultado = alumnoServicio.listarAlumnos();

		assertEquals(alumnos, resultado);
	}

	@Test
	void obtenerAlumnoDevuelveElAlumnoCuandoExiste() {
		Alumno alumno = new Alumno("12345678A", "Juan", "Pérez García");
		when(alumnoRepository.findById(1L)).thenReturn(Optional.of(alumno));

		Alumno resultado = alumnoServicio.obtenerAlumno(1L);

		assertSame(alumno, resultado);
	}

	@Test
	void obtenerAlumnoLanzaExcepcionCuandoNoExiste() {
		when(alumnoRepository.findById(999L)).thenReturn(Optional.empty());

		assertThrows(NoSuchElementException.class, () -> alumnoServicio.obtenerAlumno(999L));
	}

	@Test
	void guardarAlumnoDelegaEnElRepositorio() {
		Alumno alumno = new Alumno("12345678A", "Juan", "Pérez García");
		Alumno alumnoGuardado = new Alumno("12345678A", "Juan", "Pérez García");
		alumnoGuardado.setId(1L);
		when(alumnoRepository.save(alumno)).thenReturn(alumnoGuardado);

		Alumno resultado = alumnoServicio.guardarAlumno(alumno);

		assertSame(alumnoGuardado, resultado);
	}

	@Test
	void actualizarAlumnoModificaLosCamposDelAlumnoExistente() {
		Alumno alumnoExistente = new Alumno("12345678A", "Juan", "Pérez García");
		alumnoExistente.setId(1L);
		Alumno datos = new Alumno("87654321B", "Juan Carlos", "Pérez Gómez");
		when(alumnoRepository.findById(1L)).thenReturn(Optional.of(alumnoExistente));

		alumnoServicio.actualizarAlumno(1L, datos);

		assertEquals("87654321B", alumnoExistente.getDni());
		assertEquals("Juan Carlos", alumnoExistente.getNombre());
		assertEquals("Pérez Gómez", alumnoExistente.getApellidos());
		verify(alumnoRepository, times(1)).save(alumnoExistente);
	}

	@Test
	void actualizarAlumnoLanzaExcepcionCuandoNoExiste() {
		when(alumnoRepository.findById(999L)).thenReturn(Optional.empty());

		assertThrows(NoSuchElementException.class,
				() -> alumnoServicio.actualizarAlumno(999L, new Alumno("12345678A", "Juan", "Pérez García")));
		verify(alumnoRepository, never()).save(any());
	}

	@Test
	void eliminarAlumnoDelegaEnElRepositorio() {
		alumnoServicio.eliminarAlumno(1L);

		verify(alumnoRepository, times(1)).deleteById(1L);
	}

}
