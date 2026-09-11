package es.oesia.web1;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import es.oesia.web1.negocio.Curso;
import es.oesia.web1.repository.CursoRepository;

@SpringBootApplication
public class Web1Application {

	public static void main(String[] args) {
		SpringApplication.run(Web1Application.class, args);
	}

	@Bean
	CommandLineRunner cargarDatosIniciales(CursoRepository cursoRepository) {
		return args -> cursoRepository.saveAll(java.util.List.of(
				new Curso("Java desde cero", 40, "Fundamentos del lenguaje Java"),
				new Curso("Spring Boot avanzado", 30, "Desarrollo de APIs REST con Spring Boot"),
				new Curso("Bases de datos", 25, "Introducción a SQL y modelado de datos")
		));
	}

}
