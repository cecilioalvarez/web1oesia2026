package es.oesia.web1.servicio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import es.oesia.web1.negocio.Curso;
import es.oesia.web1.negocio.Imparticion;
import es.oesia.web1.repository.CursoRepository;
import es.oesia.web1.repository.ImparticionRepository;

@ExtendWith(MockitoExtension.class)
class CursosServicioTest {

	@Mock
	private CursoRepository cursoRepository;

	@Mock
	private ImparticionRepository imparticionRepository;

	@InjectMocks
	private CursosServicio cursosServicio;

	@Test
	void listarCursosSinTituloDevuelveTodosLosCursosDelRepositorio() {
		List<Curso> cursos = List.of(new Curso("Java desde cero", 40, "Fundamentos del lenguaje Java"));
		when(cursoRepository.findAll()).thenReturn(cursos);

		List<Curso> resultado = cursosServicio.listarCursos(null);

		assertEquals(cursos, resultado);
	}

	@Test
	void listarCursosConTituloEnBlancoDevuelveTodosLosCursosDelRepositorio() {
		List<Curso> cursos = List.of(new Curso("Java desde cero", 40, "Fundamentos del lenguaje Java"));
		when(cursoRepository.findAll()).thenReturn(cursos);

		List<Curso> resultado = cursosServicio.listarCursos("  ");

		assertEquals(cursos, resultado);
	}

	@Test
	void listarCursosConTituloFiltraPorTituloEnElRepositorio() {
		List<Curso> cursos = List.of(new Curso("Java desde cero", 40, "Fundamentos del lenguaje Java"));
		when(cursoRepository.findByTituloContainingIgnoreCase("java")).thenReturn(cursos);

		List<Curso> resultado = cursosServicio.listarCursos("java");

		assertEquals(cursos, resultado);
		verify(cursoRepository, never()).findAll();
	}

	@Test
	void obtenerCursoDevuelveElCursoCuandoExiste() {
		Curso curso = new Curso("Java desde cero", 40, "Fundamentos del lenguaje Java");
		when(cursoRepository.findById(1L)).thenReturn(Optional.of(curso));

		Curso resultado = cursosServicio.obtenerCurso(1L);

		assertSame(curso, resultado);
	}

	@Test
	void obtenerCursoLanzaExcepcionCuandoNoExiste() {
		when(cursoRepository.findById(999L)).thenReturn(Optional.empty());

		assertThrows(NoSuchElementException.class, () -> cursosServicio.obtenerCurso(999L));
	}

	@Test
	void guardarCursoDelegaEnElRepositorio() {
		Curso curso = new Curso("Java desde cero", 40, "Fundamentos del lenguaje Java");
		Curso cursoGuardado = new Curso("Java desde cero", 40, "Fundamentos del lenguaje Java");
		cursoGuardado.setId(1L);
		when(cursoRepository.save(curso)).thenReturn(cursoGuardado);

		Curso resultado = cursosServicio.guardarCurso(curso);

		assertSame(cursoGuardado, resultado);
	}

	@Test
	void eliminarCursoDelegaEnElRepositorio() {
		cursosServicio.eliminarCurso(1L);

		verify(cursoRepository, times(1)).deleteById(1L);
	}

	@Test
	void anadirImparticionLaVinculaAlCursoYLaGuarda() {
		Curso curso = new Curso("Java desde cero", 40, "Fundamentos del lenguaje Java");
		Imparticion imparticion = new Imparticion(LocalDate.of(2026, 1, 12), LocalDate.of(2026, 2, 12), null);
		when(cursoRepository.findById(1L)).thenReturn(Optional.of(curso));

		cursosServicio.añadirImparticion(1L, imparticion);

		assertSame(curso, imparticion.getCurso());
		assertEquals(1, curso.getImparticiones().size());
		verify(imparticionRepository, times(1)).save(imparticion);
	}

	@Test
	void anadirImparticionConCursoInexistenteLanzaExcepcionYNoGuarda() {
		when(cursoRepository.findById(999L)).thenReturn(Optional.empty());
		Imparticion imparticion = new Imparticion(LocalDate.of(2026, 1, 12), LocalDate.of(2026, 2, 12), null);

		assertThrows(NoSuchElementException.class, () -> cursosServicio.añadirImparticion(999L, imparticion));
		verify(imparticionRepository, never()).save(any());
	}

	@Test
	void obtenerImparticionDevuelveLaImparticionCuandoExiste() {
		Imparticion imparticion = new Imparticion(LocalDate.of(2026, 1, 12), LocalDate.of(2026, 2, 12), null);
		when(imparticionRepository.findById(1L)).thenReturn(Optional.of(imparticion));

		Imparticion resultado = cursosServicio.obtenerImparticion(1L);

		assertSame(imparticion, resultado);
	}

	@Test
	void obtenerImparticionLanzaExcepcionCuandoNoExiste() {
		when(imparticionRepository.findById(999L)).thenReturn(Optional.empty());

		assertThrows(NoSuchElementException.class, () -> cursosServicio.obtenerImparticion(999L));
	}

	@Test
	void actualizarImparticionModificaLasFechasDeLaImparticionExistente() {
		Imparticion existente = new Imparticion(LocalDate.of(2026, 1, 12), LocalDate.of(2026, 2, 12), null);
		existente.setId(1L);
		Imparticion datos = new Imparticion(LocalDate.of(2026, 3, 1), LocalDate.of(2026, 4, 1), null);
		when(imparticionRepository.findById(1L)).thenReturn(Optional.of(existente));

		cursosServicio.actualizarImparticion(1L, datos);

		assertEquals(LocalDate.of(2026, 3, 1), existente.getFechaInicio());
		assertEquals(LocalDate.of(2026, 4, 1), existente.getFechaFin());
		verify(imparticionRepository, times(1)).save(existente);
	}

	@Test
	void actualizarImparticionLanzaExcepcionCuandoNoExiste() {
		when(imparticionRepository.findById(999L)).thenReturn(Optional.empty());
		Imparticion datos = new Imparticion(LocalDate.of(2026, 3, 1), LocalDate.of(2026, 4, 1), null);

		assertThrows(NoSuchElementException.class, () -> cursosServicio.actualizarImparticion(999L, datos));
		verify(imparticionRepository, never()).save(any());
	}

	@Test
	void eliminarImparticionDelegaEnElRepositorio() {
		cursosServicio.eliminarImparticion(1L);

		verify(imparticionRepository, times(1)).deleteById(1L);
	}

}
