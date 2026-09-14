package es.oesia.web1.negocio;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;

/**
 * Matrícula de un alumno en una impartición concreta de un curso. Es el
 * concepto intermedio entre Alumno e Imparticion: un alumno puede tener
 * muchas matrículas y una impartición puede tener muchas matrículas.
 */
@Entity
public class Matricula {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull(message = "La fecha de matriculación es obligatoria")
	private LocalDate fecha;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "alumno_id")
	private Alumno alumno;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "imparticion_id")
	private Imparticion imparticion;

	public Matricula() {
	}

	public Matricula(LocalDate fecha, Alumno alumno, Imparticion imparticion) {
		this.fecha = fecha;
		this.alumno = alumno;
		this.imparticion = imparticion;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}

	public Alumno getAlumno() {
		return alumno;
	}

	public void setAlumno(Alumno alumno) {
		this.alumno = alumno;
	}

	public Imparticion getImparticion() {
		return imparticion;
	}

	public void setImparticion(Imparticion imparticion) {
		this.imparticion = imparticion;
	}

}
