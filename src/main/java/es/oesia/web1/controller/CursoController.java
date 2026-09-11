package es.oesia.web1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.oesia.web1.negocio.Curso;
import es.oesia.web1.servicio.CursosServicio;
import jakarta.validation.Valid;

@Controller
public class CursoController {

	private final CursosServicio cursosServicio;

	public CursoController(CursosServicio cursosServicio) {
		this.cursosServicio = cursosServicio;
	}

	@GetMapping("/cursos")
	public String listarCursos(@RequestParam(name = "titulo", required = false) String titulo, Model model) {
		model.addAttribute("cursos", cursosServicio.listarCursos(titulo));
		model.addAttribute("titulo", titulo);
		return "listacursos";
	}

	@GetMapping("/cursos/nuevo")
	public String formularioNuevoCurso(Model model) {
		model.addAttribute("curso", new Curso());
		return "nuevocurso";
	}

	@PostMapping("/cursos")
	public String insertarCurso(@Valid @ModelAttribute("curso") Curso curso, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return "nuevocurso";
		}
		cursosServicio.guardarCurso(curso);
		return "redirect:/cursos";
	}

	@PostMapping("/cursos/{id}/eliminar")
	public String eliminarCurso(@PathVariable Long id) {
		cursosServicio.eliminarCurso(id);
		return "redirect:/cursos";
	}

}
