package es.oesia.web1.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import es.oesia.web1.negocio.Curso;

@DataJpaTest
class CursoRepositoryTest {

	@Autowired
	private CursoRepository cursoRepository;

	@BeforeEach
	void limpiarRepositorio() {
		cursoRepository.deleteAll();
	}

	@Test
	void guardarCursoAsignaIdGenerado() {
		Curso curso = new Curso("Java desde cero", 40, "Fundamentos del lenguaje Java");

		Curso cursoGuardado = cursoRepository.save(curso);

		assertNotNull(cursoGuardado.getId());
	}

	@Test
	void findByIdEncuentraCursoGuardado() {
		Curso curso = cursoRepository.save(new Curso("Spring Boot avanzado", 30, "APIs REST con Spring Boot"));

		Optional<Curso> encontrado = cursoRepository.findById(curso.getId());

		assertTrue(encontrado.isPresent());
		assertEquals("Spring Boot avanzado", encontrado.get().getTitulo());
	}

	@Test
	void findAllDevuelveTodosLosCursosGuardados() {
		cursoRepository.save(new Curso("Java desde cero", 40, "Fundamentos del lenguaje Java"));
		cursoRepository.save(new Curso("Spring Boot avanzado", 30, "APIs REST con Spring Boot"));

		List<Curso> cursos = cursoRepository.findAll();

		assertEquals(2, cursos.size());
	}

	@Test
	void actualizarCursoExistenteModificaSusCampos() {
		Curso curso = cursoRepository.save(new Curso("Java desde cero", 40, "Fundamentos del lenguaje Java"));

		curso.setTitulo("Java avanzado");
		curso.setDuracion(60);
		cursoRepository.save(curso);

		Curso actualizado = cursoRepository.findById(curso.getId()).orElseThrow();
		assertEquals("Java avanzado", actualizado.getTitulo());
		assertEquals(60, actualizado.getDuracion());
	}

	@Test
	void deleteByIdEliminaElCurso() {
		Curso curso = cursoRepository.save(new Curso("Java desde cero", 40, "Fundamentos del lenguaje Java"));

		cursoRepository.deleteById(curso.getId());

		assertFalse(cursoRepository.findById(curso.getId()).isPresent());
	}

	@Test
	void findByIdConIdInexistenteDevuelveVacio() {
		Optional<Curso> encontrado = cursoRepository.findById(999L);

		assertTrue(encontrado.isEmpty());
	}

	@Test
	void findByTituloContainingIgnoreCaseEncuentraCoincidenciasParcialesSinDistinguirMayusculas() {
		cursoRepository.save(new Curso("Java desde cero", 40, "Fundamentos del lenguaje Java"));
		cursoRepository.save(new Curso("Spring Boot avanzado", 30, "APIs REST con Spring Boot"));

		List<Curso> encontrados = cursoRepository.findByTituloContainingIgnoreCase("java");

		assertEquals(1, encontrados.size());
		assertEquals("Java desde cero", encontrados.get(0).getTitulo());
	}

	@Test
	void findByTituloContainingIgnoreCaseSinCoincidenciasDevuelveListaVacia() {
		cursoRepository.save(new Curso("Java desde cero", 40, "Fundamentos del lenguaje Java"));

		List<Curso> encontrados = cursoRepository.findByTituloContainingIgnoreCase("python");

		assertTrue(encontrados.isEmpty());
	}

}
