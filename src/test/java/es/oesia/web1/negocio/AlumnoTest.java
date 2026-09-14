package es.oesia.web1.negocio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

class AlumnoTest {

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

		assertEquals("12345678A", alumno.getDni());
		assertEquals("Juan", alumno.getNombre());
		assertEquals("Pérez García", alumno.getApellidos());
	}

	@Test
	void constructorVacioPermiteAsignarCamposConSetters() {
		Alumno alumno = new Alumno();

		alumno.setDni("87654321B");
		alumno.setNombre("María");
		alumno.setApellidos("López Sánchez");

		assertEquals("87654321B", alumno.getDni());
		assertEquals("María", alumno.getNombre());
		assertEquals("López Sánchez", alumno.getApellidos());
	}

	@Test
	void alumnoConTodosLosCamposEsValido() {
		Alumno alumno = new Alumno("12345678A", "Juan", "Pérez García");

		Set<ConstraintViolation<Alumno>> violaciones = validator.validate(alumno);

		assertTrue(violaciones.isEmpty());
	}

	@Test
	void dniEnBlancoNoEsValido() {
		Alumno alumno = new Alumno(" ", "Juan", "Pérez García");

		Set<ConstraintViolation<Alumno>> violaciones = validator.validate(alumno);

		assertFalse(violaciones.isEmpty());
		assertEquals("dni", violaciones.iterator().next().getPropertyPath().toString());
	}

	@Test
	void nombreEnBlancoNoEsValido() {
		Alumno alumno = new Alumno("12345678A", " ", "Pérez García");

		Set<ConstraintViolation<Alumno>> violaciones = validator.validate(alumno);

		assertFalse(violaciones.isEmpty());
		assertEquals("nombre", violaciones.iterator().next().getPropertyPath().toString());
	}

	@Test
	void apellidosEnBlancoNoEsValido() {
		Alumno alumno = new Alumno("12345678A", "Juan", " ");

		Set<ConstraintViolation<Alumno>> violaciones = validator.validate(alumno);

		assertFalse(violaciones.isEmpty());
		assertEquals("apellidos", violaciones.iterator().next().getPropertyPath().toString());
	}

}
