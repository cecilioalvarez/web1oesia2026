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

import es.oesia.web1.negocio.Alumno;

@DataJpaTest
class AlumnoRepositoryTest {

	@Autowired
	private AlumnoRepository alumnoRepository;

	@BeforeEach
	void limpiarRepositorio() {
		alumnoRepository.deleteAll();
	}

	@Test
	void guardarAlumnoAsignaIdGenerado() {
		Alumno alumno = new Alumno("12345678A", "Juan", "Pérez García");

		Alumno alumnoGuardado = alumnoRepository.save(alumno);

		assertNotNull(alumnoGuardado.getId());
	}

	@Test
	void findByIdEncuentraAlumnoGuardado() {
		Alumno alumno = alumnoRepository.save(new Alumno("87654321B", "María", "López Sánchez"));

		Optional<Alumno> encontrado = alumnoRepository.findById(alumno.getId());

		assertTrue(encontrado.isPresent());
		assertEquals("María", encontrado.get().getNombre());
	}

	@Test
	void findAllDevuelveTodosLosAlumnosGuardados() {
		alumnoRepository.save(new Alumno("12345678A", "Juan", "Pérez García"));
		alumnoRepository.save(new Alumno("87654321B", "María", "López Sánchez"));

		List<Alumno> alumnos = alumnoRepository.findAll();

		assertEquals(2, alumnos.size());
	}

	@Test
	void actualizarAlumnoExistenteModificaSusCampos() {
		Alumno alumno = alumnoRepository.save(new Alumno("12345678A", "Juan", "Pérez García"));

		alumno.setNombre("Juan Carlos");
		alumno.setApellidos("Pérez Gómez");
		alumnoRepository.save(alumno);

		Alumno actualizado = alumnoRepository.findById(alumno.getId()).orElseThrow();
		assertEquals("Juan Carlos", actualizado.getNombre());
		assertEquals("Pérez Gómez", actualizado.getApellidos());
	}

	@Test
	void deleteByIdEliminaElAlumno() {
		Alumno alumno = alumnoRepository.save(new Alumno("12345678A", "Juan", "Pérez García"));

		alumnoRepository.deleteById(alumno.getId());

		assertFalse(alumnoRepository.findById(alumno.getId()).isPresent());
	}

	@Test
	void findByIdConIdInexistenteDevuelveVacio() {
		Optional<Alumno> encontrado = alumnoRepository.findById(999L);

		assertTrue(encontrado.isEmpty());
	}

}
