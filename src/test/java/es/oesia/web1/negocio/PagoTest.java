package es.oesia.web1.negocio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

class PagoTest {

	private static ValidatorFactory validatorFactory;
	private static Validator validator;

	@BeforeAll
	static void crearValidator() {
		validatorFactory = Validation.buildDefaultValidatorFactory();
		validator = validatorFactory.getValidator();
	}

	@AfterAll
	static void cerrarValidator() {
		validatorFactory.close();
	}

	@Test
	void constructorConParametrosAsignaTodosLosCampos() {
		Matricula matricula = new Matricula(LocalDate.of(2026, 1, 15), null, null);
		Pago pago = new Pago(LocalDate.of(2026, 1, 20), new BigDecimal("150.00"), matricula);

		assertEquals(LocalDate.of(2026, 1, 20), pago.getFecha());
		assertEquals(new BigDecimal("150.00"), pago.getImporte());
		assertSame(matricula, pago.getMatricula());
	}

	@Test
	void constructorVacioPermiteAsignarCamposConSetters() {
		Matricula matricula = new Matricula(LocalDate.of(2026, 1, 15), null, null);
		Pago pago = new Pago();

		pago.setFecha(LocalDate.of(2026, 2, 1));
		pago.setImporte(new BigDecimal("200.00"));
		pago.setMatricula(matricula);

		assertEquals(LocalDate.of(2026, 2, 1), pago.getFecha());
		assertEquals(new BigDecimal("200.00"), pago.getImporte());
		assertSame(matricula, pago.getMatricula());
	}

	@Test
	void pagoConFechaEImporteValidosEsValido() {
		Pago pago = new Pago(LocalDate.of(2026, 1, 20), new BigDecimal("150.00"), null);

		Set<ConstraintViolation<Pago>> violaciones = validator.validate(pago);

		assertTrue(violaciones.isEmpty());
	}

	@Test
	void fechaNulaNoEsValida() {
		Pago pago = new Pago(null, new BigDecimal("150.00"), null);

		Set<ConstraintViolation<Pago>> violaciones = validator.validate(pago);

		assertFalse(violaciones.isEmpty());
		assertEquals("fecha", violaciones.iterator().next().getPropertyPath().toString());
	}

	@Test
	void importeNuloNoEsValido() {
		Pago pago = new Pago(LocalDate.of(2026, 1, 20), null, null);

		Set<ConstraintViolation<Pago>> violaciones = validator.validate(pago);

		assertFalse(violaciones.isEmpty());
		assertEquals("importe", violaciones.iterator().next().getPropertyPath().toString());
	}

	@Test
	void importeCeroNoEsValido() {
		Pago pago = new Pago(LocalDate.of(2026, 1, 20), BigDecimal.ZERO, null);

		Set<ConstraintViolation<Pago>> violaciones = validator.validate(pago);

		assertFalse(violaciones.isEmpty());
		assertEquals("importe", violaciones.iterator().next().getPropertyPath().toString());
	}

	@Test
	void importeNegativoNoEsValido() {
		Pago pago = new Pago(LocalDate.of(2026, 1, 20), new BigDecimal("-10.00"), null);

		Set<ConstraintViolation<Pago>> violaciones = validator.validate(pago);

		assertFalse(violaciones.isEmpty());
		assertEquals("importe", violaciones.iterator().next().getPropertyPath().toString());
	}

	@Test
	void matriculaSetPagoSincronizaElLadoPropietarioDeLaRelacion() {
		Matricula matricula = new Matricula(LocalDate.of(2026, 1, 15), null, null);
		Pago pago = new Pago(LocalDate.of(2026, 1, 20), new BigDecimal("150.00"), null);

		matricula.setPago(pago);

		assertSame(pago, matricula.getPago());
		assertSame(matricula, pago.getMatricula());
	}

}
