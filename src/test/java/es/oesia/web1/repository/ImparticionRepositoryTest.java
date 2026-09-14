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

import es.oesia.web1.negocio.Curso;
import es.oesia.web1.negocio.Imparticion;

@DataJpaTest
class ImparticionRepositoryTest {

	@Autowired
	private ImparticionRepository imparticionRepository;

	@Autowired
	private CursoRepository cursoRepository;

	private Curso curso;

	@BeforeEach
	void limpiarRepositorioYCrearCurso() {
		imparticionRepository.deleteAll();
		cursoRepository.deleteAll();
		curso = cursoRepository.save(new Curso("Java desde cero", 40, "Fundamentos del lenguaje Java"));
	}

	@Test
	void guardarImparticionAsignaIdGenerado() {
		Imparticion imparticion = new Imparticion(LocalDate.of(2026, 1, 12), LocalDate.of(2026, 2, 12), curso);

		Imparticion imparticionGuardada = imparticionRepository.save(imparticion);

		assertNotNull(imparticionGuardada.getId());
	}

	@Test
	void findByIdEncuentraImparticionGuardada() {
		Imparticion imparticion = imparticionRepository
				.save(new Imparticion(LocalDate.of(2026, 1, 12), LocalDate.of(2026, 2, 12), curso));

		Optional<Imparticion> encontrada = imparticionRepository.findById(imparticion.getId());

		assertTrue(encontrada.isPresent());
		assertEquals(LocalDate.of(2026, 1, 12), encontrada.get().getFechaInicio());
	}

	@Test
	void findAllDevuelveTodasLasImparticionesGuardadas() {
		imparticionRepository.save(new Imparticion(LocalDate.of(2026, 1, 12), LocalDate.of(2026, 2, 12), curso));
		imparticionRepository.save(new Imparticion(LocalDate.of(2026, 3, 1), LocalDate.of(2026, 4, 1), curso));

		List<Imparticion> imparticiones = imparticionRepository.findAll();

		assertEquals(2, imparticiones.size());
	}

	@Test
	void actualizarImparticionExistenteModificaSusCampos() {
		Imparticion imparticion = imparticionRepository
				.save(new Imparticion(LocalDate.of(2026, 1, 12), LocalDate.of(2026, 2, 12), curso));

		imparticion.setFechaFin(LocalDate.of(2026, 5, 1));
		imparticionRepository.save(imparticion);

		Imparticion actualizada = imparticionRepository.findById(imparticion.getId()).orElseThrow();
		assertEquals(LocalDate.of(2026, 5, 1), actualizada.getFechaFin());
	}

	@Test
	void deleteByIdEliminaLaImparticion() {
		Imparticion imparticion = imparticionRepository
				.save(new Imparticion(LocalDate.of(2026, 1, 12), LocalDate.of(2026, 2, 12), curso));

		imparticionRepository.deleteById(imparticion.getId());

		assertFalse(imparticionRepository.findById(imparticion.getId()).isPresent());
	}

	@Test
	void findByIdConIdInexistenteDevuelveVacio() {
		Optional<Imparticion> encontrada = imparticionRepository.findById(999L);

		assertTrue(encontrada.isEmpty());
	}

}
