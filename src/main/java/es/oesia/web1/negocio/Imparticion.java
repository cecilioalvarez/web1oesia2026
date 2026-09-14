package es.oesia.web1.negocio;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;

/**
 * Edición concreta de un {@link Curso}, delimitada por una fecha de inicio y
 * una fecha de fin. Una impartición puede tener muchas {@link Matricula}, una
 * por cada alumno matriculado en ella.
 */
@Entity
public class Imparticion extends BaseEntity {

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

	/**
	 * @param fechaInicio fecha de inicio de la impartición
	 * @param fechaFin    fecha de fin de la impartición
	 * @param curso       curso del que es una edición
	 */
	public Imparticion(LocalDate fechaInicio, LocalDate fechaFin, Curso curso) {
		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
		this.curso = curso;
	}

	/**
	 * @return la fecha de inicio de la impartición
	 */
	public LocalDate getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(LocalDate fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	/**
	 * @return la fecha de fin de la impartición
	 */
	public LocalDate getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(LocalDate fechaFin) {
		this.fechaFin = fechaFin;
	}

	/**
	 * @return el curso del que esta impartición es una edición
	 */
	public Curso getCurso() {
		return curso;
	}

	public void setCurso(Curso curso) {
		this.curso = curso;
	}

	/**
	 * @return las matrículas de esta impartición
	 */
	public List<Matricula> getMatriculas() {
		return matriculas;
	}

	public void setMatriculas(List<Matricula> matriculas) {
		this.matriculas = matriculas;
	}

	/**
	 * Añade la matrícula a esta impartición y sincroniza el lado inverso de la
	 * relación, dejando la impartición de la matrícula apuntando a
	 * {@code this}.
	 *
	 * @param matricula matrícula a añadir
	 */
	public void addMatricula(Matricula matricula) {
		matriculas.add(matricula);
		matricula.setImparticion(this);
	}

	/**
	 * Quita la matrícula de esta impartición y sincroniza el lado inverso de
	 * la relación, dejando la matrícula sin impartición asociada.
	 *
	 * @param matricula matrícula a quitar
	 */
	public void removeMatricula(Matricula matricula) {
		matriculas.remove(matricula);
		matricula.setImparticion(null);
	}

}
