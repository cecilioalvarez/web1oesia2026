package es.oesia.web1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import es.oesia.web1.repository.CursoRepository;

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

}
