package es.oesia.web1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import es.oesia.web1.negocio.Curso;
import es.oesia.web1.repository.CursoRepository;
import jakarta.validation.Valid;

@Controller
public class CursoController {

	private final CursoRepository cursoRepository;

	public CursoController(CursoRepository cursoRepository) {
		this.cursoRepository = cursoRepository;
	}

	@GetMapping("/cursos")
	public String listarCursos(Model model) {
		model.addAttribute("cursos", cursoRepository.findAll());
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
		cursoRepository.insertar(curso);
		return "redirect:/cursos";
	}

}
