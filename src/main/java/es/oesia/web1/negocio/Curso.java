package es.oesia.web1.negocio;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Max;

/**
 * Curso ofertado por el centro. Un curso puede tener muchas
 * {@link Imparticion}, cada una de las cuales representa una edición
 * concreta del curso en un rango de fechas.
 */
@Entity
public class Curso extends BaseEntity {

	private String titulo;

	@Max(value = 200, message = "La duración no puede superar las 200 horas")
	private int duracion;

	private String descripcion;

	@OneToMany(mappedBy = "curso", orphanRemoval = true)
	private List<Imparticion> imparticiones = new ArrayList<>();

	public Curso() {
	}

	/**
	 * @param titulo      título del curso
	 * @param duracion    duración del curso, en horas
	 * @param descripcion descripción del curso
	 */
	public Curso(String titulo, int duracion, String descripcion) {
		this.titulo = titulo;
		this.duracion = duracion;
		this.descripcion = descripcion;
	}

	/**
	 * @return el título del curso
	 */
	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	/**
	 * @return la duración del curso, en horas
	 */
	public int getDuracion() {
		return duracion;
	}

	public void setDuracion(int duracion) {
		this.duracion = duracion;
	}

	/**
	 * @return la descripción del curso
	 */
	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	/**
	 * @return las imparticiones del curso
	 */
	public List<Imparticion> getImparticiones() {
		return imparticiones;
	}

	public void setImparticiones(List<Imparticion> imparticiones) {
		this.imparticiones = imparticiones;
	}

	/**
	 * Añade la impartición a este curso y sincroniza el lado inverso de la
	 * relación, dejando el curso de la impartición apuntando a {@code this}.
	 *
	 * @param imparticion impartición a añadir
	 */
	public void addImparticion(Imparticion imparticion) {
		imparticiones.add(imparticion);
		imparticion.setCurso(this);
	}

	/**
	 * Quita la impartición de este curso y sincroniza el lado inverso de la
	 * relación, dejando la impartición sin curso asociado.
	 *
	 * @param imparticion impartición a quitar
	 */
	public void removeImparticion(Imparticion imparticion) {
		imparticiones.remove(imparticion);
		imparticion.setCurso(null);
	}

}
