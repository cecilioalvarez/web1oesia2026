package es.oesia.web1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import es.oesia.web1.negocio.Curso;
import es.oesia.web1.negocio.Imparticion;
import es.oesia.web1.servicio.CursosServicio;
import jakarta.validation.Valid;

@Controller
public class ImparticionController {

	private final CursosServicio cursosServicio;

	public ImparticionController(CursosServicio cursosServicio) {
		this.cursosServicio = cursosServicio;
	}

	@GetMapping("/cursos/{id}/imparticiones/nueva")
	public String formularioNuevaImparticion(@PathVariable Long id, Model model) {
		Curso curso = cursosServicio.obtenerCurso(id);
		model.addAttribute("curso", curso);
		model.addAttribute("imparticion", new Imparticion());
		return "nuevaimparticion";
	}

	@GetMapping("/cursos/{cursoId}/imparticiones")
	public String listarImparticiones(@PathVariable Long cursoId, Model model) {
		Curso curso = cursosServicio.obtenerCurso(cursoId);
		model.addAttribute("curso", curso);
		model.addAttribute("imparticiones", curso.getImparticiones());
		return "listaimparticiones";
	}

	@PostMapping("/cursos/{cursoId}/imparticiones")
	public String insertarImparticion(@PathVariable Long cursoId,
			@Valid @ModelAttribute("imparticion") Imparticion imparticion, BindingResult bindingResult,
			Model model) {
		System.out.println("CONTROLLER entrada id=" + imparticion.getId() + " hash=" + System.identityHashCode(imparticion)
				+ " errores=" + bindingResult.getAllErrors());
		if (bindingResult.hasErrors()) {
			model.addAttribute("curso", cursosServicio.obtenerCurso(cursoId));
			return "nuevaimparticion";
		}
		cursosServicio.añadirImparticion(cursoId, imparticion);
		return "redirect:/cursos";
	}

	@GetMapping("/cursos/{cursoId}/imparticiones/{id}/editar")
	public String formularioEditarImparticion(@PathVariable Long cursoId, @PathVariable Long id, Model model) {
		Curso curso = cursosServicio.obtenerCurso(cursoId);
		model.addAttribute("curso", curso);
		model.addAttribute("imparticion", cursosServicio.obtenerImparticion(id));
		return "editarimparticion";
	}

	@PostMapping("/cursos/{cursoId}/imparticiones/{id}/editar")
	public String editarImparticion(@PathVariable Long cursoId, @PathVariable Long id,
			@Valid @ModelAttribute("imparticion") Imparticion imparticion, BindingResult bindingResult,
			Model model) {
		if (bindingResult.hasErrors()) {
			model.addAttribute("curso", cursosServicio.obtenerCurso(cursoId));
			return "editarimparticion";
		}
		cursosServicio.actualizarImparticion(id, imparticion);
		return "redirect:/cursos/{cursoId}/imparticiones";
	}

	@PostMapping("/cursos/{cursoId}/imparticiones/{id}/eliminar")
	public String eliminarImparticion(@PathVariable Long cursoId, @PathVariable Long id) {
		cursosServicio.eliminarImparticion(id);
		return "redirect:/cursos/{cursoId}/imparticiones";
	}

}
