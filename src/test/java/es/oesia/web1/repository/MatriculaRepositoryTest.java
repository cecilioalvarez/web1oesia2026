package es.oesia.web1.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import es.oesia.web1.negocio.Alumno;
import es.oesia.web1.negocio.Curso;
import es.oesia.web1.negocio.Imparticion;
import es.oesia.web1.negocio.Matricula;

@DataJpaTest
class MatriculaRepositoryTest {

	@Autowired
	private MatriculaRepository matriculaRepository;

	@Autowired
	private AlumnoRepository alumnoRepository;

	@Autowired
	private ImparticionRepository imparticionRepository;

	@Autowired
	private CursoRepository cursoRepository;

	private Alumno alumno;
	private Imparticion imparticion;

	@BeforeEach
	void limpiarRepositoriosYCrearDatosBase() {
		matriculaRepository.deleteAll();
		imparticionRepository.deleteAll();
		alumnoRepository.deleteAll();
		cursoRepository.deleteAll();

		alumno = alumnoRepository.save(new Alumno("12345678A", "Juan", "Pérez García"));
		Curso curso = cursoRepository.save(new Curso("Java desde cero", 40, "Fundamentos del lenguaje Java"));
		imparticion = imparticionRepository.save(new Imparticion(LocalDate.of(2026, 1, 12), LocalDate.of(2026, 2, 12), curso));
	}

	@Test
	void guardarMatriculaAsignaIdGenerado() {
		Matricula matricula = new Matricula(LocalDate.of(2026, 1, 15), alumno, imparticion);

		Matricula matriculaGuardada = matriculaRepository.save(matricula);

		assertNotNull(matriculaGuardada.getId());
	}

	@Test
	void findByIdEncuentraMatriculaGuardada() {
		Matricula matricula = matriculaRepository.save(new Matricula(LocalDate.of(2026, 1, 15), alumno, imparticion));

		Optional<Matricula> encontrada = matriculaRepository.findById(matricula.getId());

		assertTrue(encontrada.isPresent());
		assertEquals(LocalDate.of(2026, 1, 15), encontrada.get().getFecha());
	}

	@Test
	void findAllDevuelveTodasLasMatriculasGuardadas() {
		matriculaRepository.save(new Matricula(LocalDate.of(2026, 1, 15), alumno, imparticion));
		matriculaRepository.save(new Matricula(LocalDate.of(2026, 2, 1), alumno, imparticion));

		List<Matricula> matriculas = matriculaRepository.findAll();

		assertEquals(2, matriculas.size());
	}

	@Test
	void deleteByIdEliminaLaMatricula() {
		Matricula matricula = matriculaRepository.save(new Matricula(LocalDate.of(2026, 1, 15), alumno, imparticion));

		matriculaRepository.deleteById(matricula.getId());

		assertFalse(matriculaRepository.findById(matricula.getId()).isPresent());
	}

	@Test
	void findByIdConIdInexistenteDevuelveVacio() {
		Optional<Matricula> encontrada = matriculaRepository.findById(999L);

		assertTrue(encontrada.isEmpty());
	}

}
