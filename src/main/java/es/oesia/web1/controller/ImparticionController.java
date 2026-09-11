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

	@PostMapping("/cursos/{id}/imparticiones")
	public String insertarImparticion(@PathVariable Long id,
			@Valid @ModelAttribute("imparticion") Imparticion imparticion, BindingResult bindingResult,
			Model model) {
		if (bindingResult.hasErrors()) {
			model.addAttribute("curso", cursosServicio.obtenerCurso(id));
			return "nuevaimparticion";
		}
		cursosServicio.añadirImparticion(id, imparticion);
		return "redirect:/cursos";
	}

}
