package es.oesia.web1.negocio;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * Pago de una {@link Matricula}. Cada matrícula tiene como máximo un pago
 * vinculado.
 */
@Entity
public class Pago extends BaseEntity {

	@NotNull(message = "La fecha del pago es obligatoria")
	private LocalDate fecha;

	@NotNull(message = "El importe es obligatorio")
	@Positive(message = "El importe debe ser mayor que cero")
	private BigDecimal importe;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "matricula_id", nullable = false, unique = true)
	private Matricula matricula;

	public Pago() {
	}

	/**
	 * @param fecha     fecha del pago
	 * @param importe   importe del pago
	 * @param matricula matrícula a la que corresponde el pago
	 */
	public Pago(LocalDate fecha, BigDecimal importe, Matricula matricula) {
		this.fecha = fecha;
		this.importe = importe;
		this.matricula = matricula;
	}

	/**
	 * @return la fecha del pago
	 */
	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}

	/**
	 * @return el importe del pago
	 */
	public BigDecimal getImporte() {
		return importe;
	}

	public void setImporte(BigDecimal importe) {
		this.importe = importe;
	}

	/**
	 * @return la matrícula a la que corresponde este pago
	 */
	public Matricula getMatricula() {
		return matricula;
	}

	public void setMatricula(Matricula matricula) {
		this.matricula = matricula;
	}

}
