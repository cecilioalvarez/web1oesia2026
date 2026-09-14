package es.oesia.web1.negocio;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;

/**
 * Matrícula de un alumno en una impartición concreta de un curso. Es el
 * concepto intermedio entre Alumno e Imparticion: un alumno puede tener
 * muchas matrículas y una impartición puede tener muchas matrículas.
 */
@Entity
public class Matricula extends BaseEntity {

	@NotNull(message = "La fecha de matriculación es obligatoria")
	private LocalDate fecha;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "alumno_id")
	private Alumno alumno;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "imparticion_id")
	private Imparticion imparticion;

	@OneToOne(mappedBy = "matricula", fetch = FetchType.LAZY)
	private Pago pago;

	public Matricula() {
	}

	/**
	 * @param fecha       fecha de matriculación
	 * @param alumno      alumno matriculado
	 * @param imparticion impartición en la que se matricula
	 */
	public Matricula(LocalDate fecha, Alumno alumno, Imparticion imparticion) {
		this.fecha = fecha;
		this.alumno = alumno;
		this.imparticion = imparticion;
	}

	/**
	 * @return la fecha de matriculación
	 */
	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}

	/**
	 * @return el alumno matriculado
	 */
	public Alumno getAlumno() {
		return alumno;
	}

	public void setAlumno(Alumno alumno) {
		this.alumno = alumno;
	}

	/**
	 * @return la impartición en la que está matriculado el alumno
	 */
	public Imparticion getImparticion() {
		return imparticion;
	}

	public void setImparticion(Imparticion imparticion) {
		this.imparticion = imparticion;
	}

	/**
	 * @return el pago de esta matrícula, o {@code null} si aún no tiene uno
	 *         asociado
	 */
	public Pago getPago() {
		return pago;
	}

	/**
	 * Asigna el pago a esta matrícula y sincroniza el lado propietario de la
	 * relación, dejando el pago apuntando a {@code this}.
	 *
	 * @param pago pago a asignar, o {@code null} para desvincularlo
	 */
	public void setPago(Pago pago) {
		this.pago = pago;
		if (pago != null) {
			pago.setMatricula(this);
		}
	}

}
