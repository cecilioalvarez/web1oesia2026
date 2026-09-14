package es.oesia.web1.negocio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.Set;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

class MatriculaTest {

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
		Alumno alumno = new Alumno("12345678A", "Juan", "Pérez García");
		Imparticion imparticion = new Imparticion(LocalDate.of(2026, 1, 12), LocalDate.of(2026, 2, 12), null);
		Matricula matricula = new Matricula(LocalDate.of(2026, 1, 15), alumno, imparticion);

		assertEquals(LocalDate.of(2026, 1, 15), matricula.getFecha());
		assertSame(alumno, matricula.getAlumno());
		assertSame(imparticion, matricula.getImparticion());
	}

	@Test
	void constructorVacioPermiteAsignarCamposConSetters() {
		Alumno alumno = new Alumno("12345678A", "Juan", "Pérez García");
		Imparticion imparticion = new Imparticion(LocalDate.of(2026, 1, 12), LocalDate.of(2026, 2, 12), null);
		Matricula matricula = new Matricula();

		matricula.setFecha(LocalDate.of(2026, 1, 15));
		matricula.setAlumno(alumno);
		matricula.setImparticion(imparticion);

		assertEquals(LocalDate.of(2026, 1, 15), matricula.getFecha());
		assertSame(alumno, matricula.getAlumno());
		assertSame(imparticion, matricula.getImparticion());
	}

	@Test
	void matriculaConFechaEsValida() {
		Matricula matricula = new Matricula(LocalDate.of(2026, 1, 15), null, null);

		Set<ConstraintViolation<Matricula>> violaciones = validator.validate(matricula);

		assertTrue(violaciones.isEmpty());
	}

	@Test
	void fechaNulaNoEsValida() {
		Matricula matricula = new Matricula(null, null, null);

		Set<ConstraintViolation<Matricula>> violaciones = validator.validate(matricula);

		assertFalse(violaciones.isEmpty());
		assertEquals("fecha", violaciones.iterator().next().getPropertyPath().toString());
	}

}
