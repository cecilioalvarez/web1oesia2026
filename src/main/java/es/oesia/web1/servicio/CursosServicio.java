package es.oesia.web1.servicio;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.oesia.web1.negocio.Curso;
import es.oesia.web1.negocio.Imparticion;
import es.oesia.web1.repository.CursoRepository;

@Service
@Transactional
public class CursosServicio {

	private final CursoRepository cursoRepository;

	public CursosServicio(CursoRepository cursoRepository) {
		this.cursoRepository = cursoRepository;
	}

	@Transactional(readOnly = true)
	public List<Curso> listarCursos(String titulo) {
		if (titulo != null && !titulo.isBlank()) {
			return cursoRepository.findByTituloContainingIgnoreCase(titulo);
		}
		return cursoRepository.findAll();
	}

	@Transactional(readOnly = true)
	public Curso obtenerCurso(Long id) {
		return cursoRepository.findById(id).orElseThrow();
	}

	public Curso guardarCurso(Curso curso) {
		return cursoRepository.save(curso);
	}

	public void eliminarCurso(Long id) {
		cursoRepository.deleteById(id);
	}

	public void añadirImparticion(Long cursoId, Imparticion imparticion) {
		Curso curso = cursoRepository.findById(cursoId).orElseThrow();
		curso.addImparticion(imparticion);
		cursoRepository.save(curso);
	}

}
