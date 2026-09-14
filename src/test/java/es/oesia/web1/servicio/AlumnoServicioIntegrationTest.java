package es.oesia.web1.servicio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;

import es.oesia.web1.negocio.Alumno;
import es.oesia.web1.repository.AlumnoRepository;

/**
 * Prueba de integración: verifica AlumnoServicio junto con AlumnoRepository
 * y una base de datos H2 embebida real, en lugar de mockear el repositorio.
 */
@DataJpaTest
@Import(AlumnoServicio.class)
class AlumnoServicioIntegrationTest {

	@Autowired
	private AlumnoServicio alumnoServicio;

	@Autowired
	private AlumnoRepository alumnoRepository;

	@BeforeEach
	void limpiarRepositorio() {
		alumnoRepository.deleteAll();
	}

	@Test
	void listarAlumnosDevuelveLosAlumnosPersistidosEnLaBaseDeDatos() {
		alumnoRepository.save(new Alumno("12345678A", "Juan", "Pérez García"));
		alumnoRepository.save(new Alumno("87654321B", "María", "López Sánchez"));

		List<Alumno> alumnos = alumnoServicio.listarAlumnos();

		assertEquals(2, alumnos.size());
	}

	@Test
	void obtenerAlumnoDevuelveElAlumnoPersistido() {
		Alumno alumno = alumnoRepository.save(new Alumno("12345678A", "Juan", "Pérez García"));

		Alumno encontrado = alumnoServicio.obtenerAlumno(alumno.getId());

		assertEquals("Juan", encontrado.getNombre());
		assertEquals("Pérez García", encontrado.getApellidos());
	}

	@Test
	void obtenerAlumnoConIdInexistenteLanzaExcepcion() {
		assertThrows(NoSuchElementException.class, () -> alumnoServicio.obtenerAlumno(999L));
	}

	@Test
	void guardarAlumnoLoPersisteEnLaBaseDeDatosConIdGenerado() {
		Alumno alumno = new Alumno("12345678A", "Juan", "Pérez García");

		Alumno guardado = alumnoServicio.guardarAlumno(alumno);

		assertNotNull(guardado.getId());
		assertTrue(alumnoRepository.findById(guardado.getId()).isPresent());
	}

	@Test
	void actualizarAlumnoModificaLosDatosPersistidosSinCrearUnRegistroNuevo() {
		Alumno alumno = alumnoRepository.save(new Alumno("12345678A", "Juan", "Pérez García"));
		Alumno datos = new Alumno("87654321B", "Juan Carlos", "Pérez Gómez");

		alumnoServicio.actualizarAlumno(alumno.getId(), datos);

		Alumno actualizado = alumnoRepository.findById(alumno.getId()).orElseThrow();
		assertEquals("87654321B", actualizado.getDni());
		assertEquals("Juan Carlos", actualizado.getNombre());
		assertEquals("Pérez Gómez", actualizado.getApellidos());
		assertEquals(1, alumnoRepository.findAll().size());
	}

	@Test
	void actualizarAlumnoConIdInexistenteLanzaExcepcionYNoModificaLaBaseDeDatos() {
		assertThrows(NoSuchElementException.class,
				() -> alumnoServicio.actualizarAlumno(999L, new Alumno("12345678A", "Juan", "Pérez García")));
		assertTrue(alumnoRepository.findAll().isEmpty());
	}

	@Test
	void eliminarAlumnoLoBorraDeLaBaseDeDatos() {
		Alumno alumno = alumnoRepository.save(new Alumno("12345678A", "Juan", "Pérez García"));

		alumnoServicio.eliminarAlumno(alumno.getId());

		assertFalse(alumnoRepository.findById(alumno.getId()).isPresent());
	}

}
