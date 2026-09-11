package es.oesia.web1.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import es.oesia.web1.negocio.Curso;

@Component
public class CursoRepository {

	private final List<Curso> cursos = new ArrayList<>(List.of(
			new Curso("Java desde cero", 40, "Fundamentos del lenguaje Java"),
			new Curso("Spring Boot avanzado", 30, "Desarrollo de APIs REST con Spring Boot"),
			new Curso("Bases de datos", 25, "Introducción a SQL y modelado de datos")
	));

	public List<Curso> findAll() {
		return cursos;
	}

	public void insertar(Curso curso) {
		cursos.add(curso);
	}

}
