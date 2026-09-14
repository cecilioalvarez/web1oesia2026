package es.oesia.web1.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import es.oesia.web1.negocio.Alumno;

public interface AlumnoRepository extends JpaRepository<Alumno, Long> {
}
