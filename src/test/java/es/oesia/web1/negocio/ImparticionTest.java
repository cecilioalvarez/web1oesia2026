package es.oesia.web1.negocio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
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

class ImparticionTest {

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
		Curso curso = new Curso("Java desde cero", 40, "Fundamentos del lenguaje Java");
		Imparticion imparticion = new Imparticion(LocalDate.of(2026, 1, 12), LocalDate.of(2026, 2, 12), curso);

		assertEquals(LocalDate.of(2026, 1, 12), imparticion.getFechaInicio());
		assertEquals(LocalDate.of(2026, 2, 12), imparticion.getFechaFin());
		assertSame(curso, imparticion.getCurso());
	}

	@Test
	void constructorVacioPermiteAsignarCamposConSetters() {
		Curso curso = new Curso("Java desde cero", 40, "Fundamentos del lenguaje Java");
		Imparticion imparticion = new Imparticion();

		imparticion.setFechaInicio(LocalDate.of(2026, 3, 1));
		imparticion.setFechaFin(LocalDate.of(2026, 4, 1));
		imparticion.setCurso(curso);

		assertEquals(LocalDate.of(2026, 3, 1), imparticion.getFechaInicio());
		assertEquals(LocalDate.of(2026, 4, 1), imparticion.getFechaFin());
		assertSame(curso, imparticion.getCurso());
	}

	@Test
	void imparticionConFechasEsValida() {
		Imparticion imparticion = new Imparticion(LocalDate.of(2026, 1, 12), LocalDate.of(2026, 2, 12), null);

		Set<ConstraintViolation<Imparticion>> violaciones = validator.validate(imparticion);

		assertTrue(violaciones.isEmpty());
	}

	@Test
	void fechaInicioNulaNoEsValida() {
		Imparticion imparticion = new Imparticion(null, LocalDate.of(2026, 2, 12), null);

		Set<ConstraintViolation<Imparticion>> violaciones = validator.validate(imparticion);

		assertFalse(violaciones.isEmpty());
		assertEquals("fechaInicio", violaciones.iterator().next().getPropertyPath().toString());
	}

	@Test
	void fechaFinNulaNoEsValida() {
		Imparticion imparticion = new Imparticion(LocalDate.of(2026, 1, 12), null, null);

		Set<ConstraintViolation<Imparticion>> violaciones = validator.validate(imparticion);

		assertFalse(violaciones.isEmpty());
		assertEquals("fechaFin", violaciones.iterator().next().getPropertyPath().toString());
	}

	@Test
	void addMatriculaLaAnadeALaListaYAsignaLaImparticionComoPropietaria() {
		Imparticion imparticion = new Imparticion(LocalDate.of(2026, 1, 12), LocalDate.of(2026, 2, 12), null);
		Matricula matricula = new Matricula(LocalDate.of(2026, 1, 15), null, null);

		imparticion.addMatricula(matricula);

		assertTrue(imparticion.getMatriculas().contains(matricula));
		assertSame(imparticion, matricula.getImparticion());
	}

	@Test
	void removeMatriculaLaQuitaDeLaListaYDesvinculaLaImparticion() {
		Imparticion imparticion = new Imparticion(LocalDate.of(2026, 1, 12), LocalDate.of(2026, 2, 12), null);
		Matricula matricula = new Matricula(LocalDate.of(2026, 1, 15), null, null);
		imparticion.addMatricula(matricula);

		imparticion.removeMatricula(matricula);

		assertFalse(imparticion.getMatriculas().contains(matricula));
		assertNull(matricula.getImparticion());
	}

}
