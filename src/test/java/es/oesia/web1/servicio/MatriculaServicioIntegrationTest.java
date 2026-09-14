package es.oesia.web1.servicio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;

import es.oesia.web1.negocio.Alumno;
import es.oesia.web1.negocio.Curso;
import es.oesia.web1.negocio.Imparticion;
import es.oesia.web1.negocio.Matricula;
import es.oesia.web1.repository.AlumnoRepository;
import es.oesia.web1.repository.CursoRepository;
import es.oesia.web1.repository.ImparticionRepository;

/**
 * Prueba de integración: verifica MatriculaServicio junto con sus
 * repositorios y una base de datos H2 embebida real, en lugar de mockear
 * los repositorios.
 */
@DataJpaTest
@Import(MatriculaServicio.class)
class MatriculaServicioIntegrationTest {

	@Autowired
	private MatriculaServicio matriculaServicio;

	@Autowired
	private AlumnoRepository alumnoRepository;

	@Autowired
	private CursoRepository cursoRepository;

	@Autowired
	private ImparticionRepository imparticionRepository;

	private Alumno alumno;
	private Imparticion imparticionA;
	private Imparticion imparticionB;

	@BeforeEach
	void crearDatosBase() {
		alumno = alumnoRepository.save(new Alumno("12345678A", "Juan", "Pérez García"));
		Curso curso = cursoRepository.save(new Curso("Java desde cero", 40, "Fundamentos del lenguaje Java"));
		imparticionA = imparticionRepository
				.save(new Imparticion(LocalDate.of(2026, 1, 12), LocalDate.of(2026, 2, 12), curso));
		imparticionB = imparticionRepository
				.save(new Imparticion(LocalDate.of(2026, 3, 1), LocalDate.of(2026, 4, 1), curso));
	}

	@Test
	void obtenerAlumnoConIdInexistenteLanzaExcepcion() {
		assertThrows(NoSuchElementException.class, () -> matriculaServicio.obtenerAlumno(999L));
	}

	@Test
	void listarImparticionesDisponiblesExcluyeLasYaMatriculadasEnBaseDeDatosReal() {
		matriculaServicio.matricular(alumno.getId(), imparticionA.getId(), LocalDate.of(2026, 1, 15));

		List<Imparticion> disponibles = matriculaServicio.listarImparticionesDisponibles(alumno.getId());

		assertEquals(1, disponibles.size());
		assertEquals(imparticionB.getId(), disponibles.get(0).getId());
	}

	@Test
	void matricularPersisteLaMatriculaYQuedaEnLaListaDelAlumno() {
		Matricula matricula = matriculaServicio.matricular(alumno.getId(), imparticionA.getId(),
				LocalDate.of(2026, 1, 15));

		assertNotNull(matricula.getId());
		List<Matricula> matriculas = matriculaServicio.listarMatriculasDeAlumno(alumno.getId());
		assertEquals(1, matriculas.size());
		assertTrue(matriculas.stream().anyMatch(m -> m.getId().equals(matricula.getId())));
	}

	@Test
	void matricularConImparticionInexistenteLanzaExcepcion() {
		assertThrows(NoSuchElementException.class,
				() -> matriculaServicio.matricular(alumno.getId(), 999L, LocalDate.of(2026, 1, 15)));
	}

}
