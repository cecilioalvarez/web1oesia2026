package es.oesia.web1.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
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
import es.oesia.web1.negocio.Pago;

@DataJpaTest
class PagoRepositoryTest {

	@Autowired
	private PagoRepository pagoRepository;

	@Autowired
	private MatriculaRepository matriculaRepository;

	@Autowired
	private AlumnoRepository alumnoRepository;

	@Autowired
	private ImparticionRepository imparticionRepository;

	@Autowired
	private CursoRepository cursoRepository;

	private Matricula matricula;

	@BeforeEach
	void limpiarRepositoriosYCrearMatricula() {
		pagoRepository.deleteAll();
		matriculaRepository.deleteAll();
		imparticionRepository.deleteAll();
		alumnoRepository.deleteAll();
		cursoRepository.deleteAll();

		Alumno alumno = alumnoRepository.save(new Alumno("12345678A", "Juan", "Pérez García"));
		Curso curso = cursoRepository.save(new Curso("Java desde cero", 40, "Fundamentos del lenguaje Java"));
		Imparticion imparticion = imparticionRepository
				.save(new Imparticion(LocalDate.of(2026, 1, 12), LocalDate.of(2026, 2, 12), curso));
		matricula = matriculaRepository.save(new Matricula(LocalDate.of(2026, 1, 15), alumno, imparticion));
	}

	@Test
	void guardarPagoAsignaIdGenerado() {
		Pago pago = new Pago(LocalDate.of(2026, 1, 20), new BigDecimal("150.00"), matricula);

		Pago pagoGuardado = pagoRepository.save(pago);

		assertNotNull(pagoGuardado.getId());
	}

	@Test
	void findByIdEncuentraPagoGuardado() {
		Pago pago = pagoRepository.save(new Pago(LocalDate.of(2026, 1, 20), new BigDecimal("150.00"), matricula));

		Optional<Pago> encontrado = pagoRepository.findById(pago.getId());

		assertTrue(encontrado.isPresent());
		assertEquals(new BigDecimal("150.00"), encontrado.get().getImporte());
	}

	@Test
	void findAllDevuelveTodosLosPagosGuardados() {
		Alumno otroAlumno = alumnoRepository.save(new Alumno("87654321B", "María", "López Sánchez"));
		Matricula otraMatricula = matriculaRepository
				.save(new Matricula(LocalDate.of(2026, 1, 16), otroAlumno, matricula.getImparticion()));
		pagoRepository.save(new Pago(LocalDate.of(2026, 1, 20), new BigDecimal("150.00"), matricula));
		pagoRepository.save(new Pago(LocalDate.of(2026, 1, 21), new BigDecimal("200.00"), otraMatricula));

		List<Pago> pagos = pagoRepository.findAll();

		assertEquals(2, pagos.size());
	}

	@Test
	void actualizarPagoExistenteModificaSusCampos() {
		Pago pago = pagoRepository.save(new Pago(LocalDate.of(2026, 1, 20), new BigDecimal("150.00"), matricula));

		pago.setImporte(new BigDecimal("180.00"));
		pagoRepository.save(pago);

		Pago actualizado = pagoRepository.findById(pago.getId()).orElseThrow();
		assertEquals(new BigDecimal("180.00"), actualizado.getImporte());
	}

	@Test
	void deleteByIdEliminaElPago() {
		Pago pago = pagoRepository.save(new Pago(LocalDate.of(2026, 1, 20), new BigDecimal("150.00"), matricula));

		pagoRepository.deleteById(pago.getId());

		assertFalse(pagoRepository.findById(pago.getId()).isPresent());
	}

	@Test
	void findByIdConIdInexistenteDevuelveVacio() {
		Optional<Pago> encontrado = pagoRepository.findById(999L);

		assertTrue(encontrado.isEmpty());
	}

}
