package es.oesia.web1.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import es.oesia.web1.negocio.Curso;

public interface CursoRepository extends JpaRepository<Curso, Long> {

	List<Curso> findByTituloContainingIgnoreCase(String titulo);

}
