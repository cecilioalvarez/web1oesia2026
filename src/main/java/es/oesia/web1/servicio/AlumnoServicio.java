package es.oesia.web1.servicio;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.oesia.web1.negocio.Alumno;
import es.oesia.web1.repository.AlumnoRepository;

@Service
@Transactional
public class AlumnoServicio {

	private final AlumnoRepository alumnoRepository;

	public AlumnoServicio(AlumnoRepository alumnoRepository) {
		this.alumnoRepository = alumnoRepository;
	}

	@Transactional(readOnly = true)
	public List<Alumno> listarAlumnos() {
		return alumnoRepository.findAll();
	}

	@Transactional(readOnly = true)
	public Alumno obtenerAlumno(Long id) {
		return alumnoRepository.findById(id).orElseThrow();
	}

	public Alumno guardarAlumno(Alumno alumno) {
		return alumnoRepository.save(alumno);
	}

	public void actualizarAlumno(Long id, Alumno datos) {
		Alumno alumno = alumnoRepository.findById(id).orElseThrow();
		alumno.setDni(datos.getDni());
		alumno.setNombre(datos.getNombre());
		alumno.setApellidos(datos.getApellidos());
		alumnoRepository.save(alumno);
	}

	public void eliminarAlumno(Long id) {
		alumnoRepository.deleteById(id);
	}

}
