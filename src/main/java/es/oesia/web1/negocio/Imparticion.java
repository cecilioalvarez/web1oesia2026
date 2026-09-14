package es.oesia.web1.negocio;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;

@Entity
public class Imparticion {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull(message = "La fecha de inicio es obligatoria")
	private LocalDate fechaInicio;

	@NotNull(message = "La fecha de fin es obligatoria")
	private LocalDate fechaFin;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "curso_id")
	private Curso curso;

	@OneToMany(mappedBy = "imparticion", orphanRemoval = true)
	private List<Matricula> matriculas = new ArrayList<>();

	public Imparticion() {
	}

	public Imparticion(LocalDate fechaInicio, LocalDate fechaFin, Curso curso) {
		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
		this.curso = curso;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDate getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(LocalDate fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public LocalDate getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(LocalDate fechaFin) {
		this.fechaFin = fechaFin;
	}

	public Curso getCurso() {
		return curso;
	}

	public void setCurso(Curso curso) {
		this.curso = curso;
	}

	public List<Matricula> getMatriculas() {
		return matriculas;
	}

	public void setMatriculas(List<Matricula> matriculas) {
		this.matriculas = matriculas;
	}

	public void addMatricula(Matricula matricula) {
		matriculas.add(matricula);
		matricula.setImparticion(this);
	}

	public void removeMatricula(Matricula matricula) {
		matriculas.remove(matricula);
		matricula.setImparticion(null);
	}

}
