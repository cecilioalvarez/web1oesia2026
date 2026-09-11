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

class CursoTest {

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

		assertEquals("Java desde cero", curso.getTitulo());
		assertEquals(40, curso.getDuracion());
		assertEquals("Fundamentos del lenguaje Java", curso.getDescripcion());
	}

	@Test
	void constructorVacioPermiteAsignarCamposConSetters() {
		Curso curso = new Curso();

		curso.setTitulo("Spring Boot avanzado");
		curso.setDuracion(30);
		curso.setDescripcion("Desarrollo de APIs REST con Spring Boot");

		assertEquals("Spring Boot avanzado", curso.getTitulo());
		assertEquals(30, curso.getDuracion());
		assertEquals("Desarrollo de APIs REST con Spring Boot", curso.getDescripcion());
	}

	@Test
	void duracionMenorOIgualA200EsValida() {
		Curso curso = new Curso("Java desde cero", 200, "Fundamentos del lenguaje Java");

		Set<ConstraintViolation<Curso>> violaciones = validator.validate(curso);

		assertTrue(violaciones.isEmpty());
	}

	@Test
	void duracionMayorA200NoEsValida() {
		Curso curso = new Curso("Java desde cero", 201, "Fundamentos del lenguaje Java");

		Set<ConstraintViolation<Curso>> violaciones = validator.validate(curso);

		assertFalse(violaciones.isEmpty());
		assertEquals("duracion", violaciones.iterator().next().getPropertyPath().toString());
	}

}
