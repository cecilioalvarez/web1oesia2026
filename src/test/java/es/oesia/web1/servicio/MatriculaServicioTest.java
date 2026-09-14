package es.oesia.web1.servicio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import es.oesia.web1.negocio.Alumno;
import es.oesia.web1.negocio.Imparticion;
import es.oesia.web1.negocio.Matricula;
import es.oesia.web1.repository.AlumnoRepository;
import es.oesia.web1.repository.ImparticionRepository;
import es.oesia.web1.repository.MatriculaRepository;

@ExtendWith(MockitoExtension.class)
class MatriculaServicioTest {

	@Mock
	private MatriculaRepository matriculaRepository;

	@Mock
	private AlumnoRepository alumnoRepository;

	@Mock
	private ImparticionRepository imparticionRepository;

	@InjectMocks
	private MatriculaServicio matriculaServicio;

	@Test
	void obtenerAlumnoDevuelveElAlumnoCuandoExiste() {
		Alumno alumno = new Alumno("12345678A", "Juan", "Pérez García");
		when(alumnoRepository.findById(1L)).thenReturn(Optional.of(alumno));

		Alumno resultado = matriculaServicio.obtenerAlumno(1L);

		assertSame(alumno, resultado);
	}

	@Test
	void obtenerAlumnoLanzaExcepcionCuandoNoExiste() {
		when(alumnoRepository.findById(999L)).thenReturn(Optional.empty());

		assertThrows(NoSuchElementException.class, () -> matriculaServicio.obtenerAlumno(999L));
	}

	@Test
	void listarMatriculasDeAlumnoDevuelveLasMatriculasDelAlumno() {
		Alumno alumno = new Alumno("12345678A", "Juan", "Pérez García");
		Matricula matricula = new Matricula(LocalDate.of(2026, 1, 15), alumno, null);
		alumno.addMatricula(matricula);
		when(alumnoRepository.findById(1L)).thenReturn(Optional.of(alumno));

		List<Matricula> resultado = matriculaServicio.listarMatriculasDeAlumno(1L);

		assertEquals(List.of(matricula), resultado);
	}

	@Test
	void listarImparticionesDisponiblesExcluyeLasYaMatriculadas() {
		Alumno alumno = new Alumno("12345678A", "Juan", "Pérez García");
		Imparticion matriculada = new Imparticion(LocalDate.of(2026, 1, 12), LocalDate.of(2026, 2, 12), null);
		matriculada.setId(1L);
		Imparticion disponible = new Imparticion(LocalDate.of(2026, 3, 1), LocalDate.of(2026, 4, 1), null);
		disponible.setId(2L);
		alumno.addMatricula(new Matricula(LocalDate.of(2026, 1, 15), alumno, matriculada));
		when(alumnoRepository.findById(1L)).thenReturn(Optional.of(alumno));
		when(imparticionRepository.findAll()).thenReturn(List.of(matriculada, disponible));

		List<Imparticion> resultado = matriculaServicio.listarImparticionesDisponibles(1L);

		assertEquals(List.of(disponible), resultado);
	}

	@Test
	void listarImparticionesDisponiblesSinMatriculasPreviasDevuelveTodas() {
		Alumno alumno = new Alumno("12345678A", "Juan", "Pérez García");
		Imparticion imparticion = new Imparticion(LocalDate.of(2026, 3, 1), LocalDate.of(2026, 4, 1), null);
		when(alumnoRepository.findById(1L)).thenReturn(Optional.of(alumno));
		when(imparticionRepository.findAll()).thenReturn(List.of(imparticion));

		List<Imparticion> resultado = matriculaServicio.listarImparticionesDisponibles(1L);

		assertEquals(List.of(imparticion), resultado);
	}

	@Test
	void matricularCreaLaMatriculaYSincronizaAmbosLadosDeLaRelacion() {
		Alumno alumno = new Alumno("12345678A", "Juan", "Pérez García");
		Imparticion imparticion = new Imparticion(LocalDate.of(2026, 1, 12), LocalDate.of(2026, 2, 12), null);
		when(alumnoRepository.findById(1L)).thenReturn(Optional.of(alumno));
		when(imparticionRepository.findById(2L)).thenReturn(Optional.of(imparticion));
		when(matriculaRepository.save(any(Matricula.class)))
				.thenAnswer(invocation -> invocation.getArgument(0));

		Matricula resultado = matriculaServicio.matricular(1L, 2L, LocalDate.of(2026, 1, 15));

		assertEquals(LocalDate.of(2026, 1, 15), resultado.getFecha());
		assertSame(alumno, resultado.getAlumno());
		assertSame(imparticion, resultado.getImparticion());
		assertTrue(alumno.getMatriculas().contains(resultado));
		assertTrue(imparticion.getMatriculas().contains(resultado));
		verify(matriculaRepository, times(1)).save(resultado);
	}

	@Test
	void matricularConAlumnoInexistenteLanzaExcepcionYNoGuarda() {
		when(alumnoRepository.findById(999L)).thenReturn(Optional.empty());

		assertThrows(NoSuchElementException.class,
				() -> matriculaServicio.matricular(999L, 2L, LocalDate.of(2026, 1, 15)));
		verify(matriculaRepository, never()).save(any());
	}

	@Test
	void matricularConImparticionInexistenteLanzaExcepcionYNoGuarda() {
		Alumno alumno = new Alumno("12345678A", "Juan", "Pérez García");
		when(alumnoRepository.findById(1L)).thenReturn(Optional.of(alumno));
		when(imparticionRepository.findById(999L)).thenReturn(Optional.empty());

		assertThrows(NoSuchElementException.class,
				() -> matriculaServicio.matricular(1L, 999L, LocalDate.of(2026, 1, 15)));
		assertTrue(alumno.getMatriculas().isEmpty());
		verify(matriculaRepository, never()).save(any());
	}

}
