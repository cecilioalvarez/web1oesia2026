package es.oesia.web1.servicio;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.oesia.web1.negocio.Alumno;
import es.oesia.web1.negocio.Imparticion;
import es.oesia.web1.negocio.Matricula;
import es.oesia.web1.repository.AlumnoRepository;
import es.oesia.web1.repository.ImparticionRepository;
import es.oesia.web1.repository.MatriculaRepository;

@Service
@Transactional
public class MatriculaServicio {

	private final MatriculaRepository matriculaRepository;
	private final AlumnoRepository alumnoRepository;
	private final ImparticionRepository imparticionRepository;

	public MatriculaServicio(MatriculaRepository matriculaRepository, AlumnoRepository alumnoRepository,
			ImparticionRepository imparticionRepository) {
		this.matriculaRepository = matriculaRepository;
		this.alumnoRepository = alumnoRepository;
		this.imparticionRepository = imparticionRepository;
	}

	@Transactional(readOnly = true)
	public Alumno obtenerAlumno(Long alumnoId) {
		return alumnoRepository.findById(alumnoId).orElseThrow();
	}

	@Transactional(readOnly = true)
	public List<Matricula> listarMatriculasDeAlumno(Long alumnoId) {
		return obtenerAlumno(alumnoId).getMatriculas();
	}

	@Transactional(readOnly = true)
	public List<Imparticion> listarImparticionesDisponibles(Long alumnoId) {
		Alumno alumno = obtenerAlumno(alumnoId);
		List<Long> imparticionesYaMatriculadas = alumno.getMatriculas().stream()
				.map(matricula -> matricula.getImparticion().getId())
				.toList();
		return imparticionRepository.findAll().stream()
				.filter(imparticion -> !imparticionesYaMatriculadas.contains(imparticion.getId()))
				.toList();
	}

	public Matricula matricular(Long alumnoId, Long imparticionId, LocalDate fecha) {
		Alumno alumno = alumnoRepository.findById(alumnoId).orElseThrow();
		Imparticion imparticion = imparticionRepository.findById(imparticionId).orElseThrow();
		Matricula matricula = new Matricula(fecha, alumno, imparticion);
		alumno.addMatricula(matricula);
		imparticion.addMatricula(matricula);
		return matriculaRepository.save(matricula);
	}

}
