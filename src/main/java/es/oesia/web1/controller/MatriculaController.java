package es.oesia.web1.controller;

import java.time.LocalDate;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.oesia.web1.negocio.Matricula;
import es.oesia.web1.servicio.MatriculaServicio;

@Controller
public class MatriculaController {

	private final MatriculaServicio matriculaServicio;

	public MatriculaController(MatriculaServicio matriculaServicio) {
		this.matriculaServicio = matriculaServicio;
	}

	@GetMapping("/alumnos/{alumnoId}/matriculas")
	public String listarMatriculas(@PathVariable Long alumnoId, Model model) {
		model.addAttribute("alumno", matriculaServicio.obtenerAlumno(alumnoId));
		model.addAttribute("matriculas", matriculaServicio.listarMatriculasDeAlumno(alumnoId));
		return "listamatriculas";
	}

	@GetMapping("/alumnos/{alumnoId}/matriculas/nueva")
	public String formularioNuevaMatricula(@PathVariable Long alumnoId, Model model) {
		model.addAttribute("alumno", matriculaServicio.obtenerAlumno(alumnoId));
		model.addAttribute("imparticionesDisponibles", matriculaServicio.listarImparticionesDisponibles(alumnoId));
		return "nuevamatricula";
	}

	@PostMapping("/alumnos/{alumnoId}/matriculas")
	public String insertarMatricula(@PathVariable Long alumnoId,
			@RequestParam(required = false) LocalDate fecha,
			@RequestParam(required = false) Long imparticionId,
			Model model) {
		if (fecha == null || imparticionId == null) {
			model.addAttribute("error", "Debe seleccionar una fecha y una impartición");
			model.addAttribute("alumno", matriculaServicio.obtenerAlumno(alumnoId));
			model.addAttribute("imparticionesDisponibles", matriculaServicio.listarImparticionesDisponibles(alumnoId));
			model.addAttribute("fecha", fecha);
			model.addAttribute("imparticionId", imparticionId);
			return "nuevamatricula";
		}
		Matricula matricula = matriculaServicio.matricular(alumnoId, imparticionId, fecha);
		return "redirect:/matriculas/" + matricula.getId() + "/pago/nuevo";
	}

}
