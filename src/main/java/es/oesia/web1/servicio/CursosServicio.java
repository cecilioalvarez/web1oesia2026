package es.oesia.web1.servicio;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.oesia.web1.negocio.Curso;
import es.oesia.web1.negocio.Imparticion;
import es.oesia.web1.repository.CursoRepository;
import es.oesia.web1.repository.ImparticionRepository;

@Service
@Transactional
public class CursosServicio {

	private final CursoRepository cursoRepository;
	private final ImparticionRepository imparticionRepository;

	public CursosServicio(CursoRepository cursoRepository, ImparticionRepository imparticionRepository) {
		this.cursoRepository = cursoRepository;
		this.imparticionRepository = imparticionRepository;
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
		imparticionRepository.save(imparticion);

	}

	@Transactional(readOnly = true)
	public Imparticion obtenerImparticion(Long id) {
		return imparticionRepository.findById(id).orElseThrow();
	}

	public void actualizarImparticion(Long id, Imparticion datos) {
		Imparticion imparticion = imparticionRepository.findById(id).orElseThrow();
		imparticion.setFechaInicio(datos.getFechaInicio());
		imparticion.setFechaFin(datos.getFechaFin());
		imparticionRepository.save(imparticion);
	}

	public void eliminarImparticion(Long id) {
		imparticionRepository.deleteById(id);
	}

}
