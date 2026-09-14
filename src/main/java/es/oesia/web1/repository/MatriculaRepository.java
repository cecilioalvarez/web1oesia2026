package es.oesia.web1.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import es.oesia.web1.negocio.Matricula;

public interface MatriculaRepository extends JpaRepository<Matricula, Long> {
}
