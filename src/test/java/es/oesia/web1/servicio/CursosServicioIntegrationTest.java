package es.oesia.web1.servicio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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

import es.oesia.web1.negocio.Curso;
import es.oesia.web1.negocio.Imparticion;
import es.oesia.web1.repository.CursoRepository;
import es.oesia.web1.repository.ImparticionRepository;

/**
 * Prueba de integración: verifica CursosServicio junto con CursoRepository e
 * ImparticionRepository y una base de datos H2 embebida real, en lugar de
 * mockear los repositorios.
 */
@DataJpaTest
@Import(CursosServicio.class)
class CursosServicioIntegrationTest {

	@Autowired
	private CursosServicio cursosServicio;

	@Autowired
	private CursoRepository cursoRepository;

	@Autowired
	private ImparticionRepository imparticionRepository;

	@BeforeEach
	void limpiarRepositorios() {
		imparticionRepository.deleteAll();
		cursoRepository.deleteAll();
	}

	@Test
	void listarCursosDevuelveLosCursosPersistidosEnLaBaseDeDatos() {
		cursoRepository.save(new Curso("Java desde cero", 40, "Fundamentos del lenguaje Java"));
		cursoRepository.save(new Curso("Spring Boot avanzado", 30, "APIs REST con Spring Boot"));

		List<Curso> cursos = cursosServicio.listarCursos(null);

		assertEquals(2, cursos.size());
	}

	@Test
	void listarCursosConTituloFiltraLosCursosPersistidos() {
		cursoRepository.save(new Curso("Java desde cero", 40, "Fundamentos del lenguaje Java"));
		cursoRepository.save(new Curso("Spring Boot avanzado", 30, "APIs REST con Spring Boot"));

		List<Curso> cursos = cursosServicio.listarCursos("java");

		assertEquals(1, cursos.size());
		assertEquals("Java desde cero", cursos.get(0).getTitulo());
	}

	@Test
	void obtenerCursoConIdInexistenteLanzaExcepcion() {
		assertThrows(NoSuchElementException.class, () -> cursosServicio.obtenerCurso(999L));
	}

	@Test
	void guardarCursoLoPersisteEnLaBaseDeDatosConIdGenerado() {
		Curso curso = new Curso("Java desde cero", 40, "Fundamentos del lenguaje Java");

		Curso guardado = cursosServicio.guardarCurso(curso);

		assertNotNull(guardado.getId());
		assertTrue(cursoRepository.findById(guardado.getId()).isPresent());
	}

	@Test
	void eliminarCursoLoBorraDeLaBaseDeDatos() {
		Curso curso = cursoRepository.save(new Curso("Java desde cero", 40, "Fundamentos del lenguaje Java"));

		cursosServicio.eliminarCurso(curso.getId());

		assertFalse(cursoRepository.findById(curso.getId()).isPresent());
	}

	@Test
	void anadirImparticionLaPersisteVinculadaAlCurso() {
		Curso curso = cursoRepository.save(new Curso("Java desde cero", 40, "Fundamentos del lenguaje Java"));
		Imparticion imparticion = new Imparticion(LocalDate.of(2026, 1, 12), LocalDate.of(2026, 2, 12), null);

		cursosServicio.añadirImparticion(curso.getId(), imparticion);

		assertNotNull(imparticion.getId());
		Curso cursoActualizado = cursoRepository.findById(curso.getId()).orElseThrow();
		assertEquals(1, cursoActualizado.getImparticiones().size());
	}

	@Test
	void actualizarImparticionModificaLosDatosPersistidos() {
		Curso curso = cursoRepository.save(new Curso("Java desde cero", 40, "Fundamentos del lenguaje Java"));
		Imparticion imparticion = imparticionRepository
				.save(new Imparticion(LocalDate.of(2026, 1, 12), LocalDate.of(2026, 2, 12), curso));
		Imparticion datos = new Imparticion(LocalDate.of(2026, 3, 1), LocalDate.of(2026, 4, 1), null);

		cursosServicio.actualizarImparticion(imparticion.getId(), datos);

		Imparticion actualizada = imparticionRepository.findById(imparticion.getId()).orElseThrow();
		assertEquals(LocalDate.of(2026, 3, 1), actualizada.getFechaInicio());
		assertEquals(LocalDate.of(2026, 4, 1), actualizada.getFechaFin());
	}

	@Test
	void eliminarImparticionLaBorraDeLaBaseDeDatos() {
		Curso curso = cursoRepository.save(new Curso("Java desde cero", 40, "Fundamentos del lenguaje Java"));
		Imparticion imparticion = imparticionRepository
				.save(new Imparticion(LocalDate.of(2026, 1, 12), LocalDate.of(2026, 2, 12), curso));

		cursosServicio.eliminarImparticion(imparticion.getId());

		assertFalse(imparticionRepository.findById(imparticion.getId()).isPresent());
	}

}
