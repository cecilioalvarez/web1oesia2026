package es.oesia.web1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import es.oesia.web1.negocio.Alumno;
import es.oesia.web1.servicio.AlumnoServicio;
import jakarta.validation.Valid;

@Controller
public class AlumnoController {

	private final AlumnoServicio alumnoServicio;

	public AlumnoController(AlumnoServicio alumnoServicio) {
		this.alumnoServicio = alumnoServicio;
	}

	@GetMapping("/alumnos")
	public String listarAlumnos(Model model) {
		model.addAttribute("alumnos", alumnoServicio.listarAlumnos());
		return "listaalumnos";
	}

	@GetMapping("/alumnos/nuevo")
	public String formularioNuevoAlumno(Model model) {
		model.addAttribute("alumno", new Alumno());
		return "nuevoalumno";
	}

	@PostMapping("/alumnos")
	public String insertarAlumno(@Valid @ModelAttribute("alumno") Alumno alumno, BindingResult bindingResult,
			Model model) {
		if (bindingResult.hasErrors()) {
			return "nuevoalumno";
		}
		Alumno alumnoGuardado = alumnoServicio.guardarAlumno(alumno);
		model.addAttribute("alumno", alumnoGuardado);
		return "alumnoguardado";
	}

	@GetMapping("/alumnos/{id}/editar")
	public String formularioEditarAlumno(@PathVariable Long id, Model model) {
		model.addAttribute("alumno", alumnoServicio.obtenerAlumno(id));
		return "editaralumno";
	}

	@PostMapping("/alumnos/{id}/editar")
	public String editarAlumno(@PathVariable Long id, @Valid @ModelAttribute("alumno") Alumno alumno,
			BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return "editaralumno";
		}
		alumnoServicio.actualizarAlumno(id, alumno);
		return "redirect:/alumnos";
	}

	@PostMapping("/alumnos/{id}/eliminar")
	public String eliminarAlumno(@PathVariable Long id) {
		alumnoServicio.eliminarAlumno(id);
		return "redirect:/alumnos";
	}

}
