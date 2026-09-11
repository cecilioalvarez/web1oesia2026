package es.oesia.web1.negocio;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Max;

@Entity
public class Curso {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String titulo;

	@Max(value = 200, message = "La duración no puede superar las 200 horas")
	private int duracion;

	private String descripcion;

	@OneToMany(mappedBy = "curso", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Imparticion> imparticiones = new ArrayList<>();

	public Curso() {
	}

	public Curso(String titulo, int duracion, String descripcion) {
		this.titulo = titulo;
		this.duracion = duracion;
		this.descripcion = descripcion;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public int getDuracion() {
		return duracion;
	}

	public void setDuracion(int duracion) {
		this.duracion = duracion;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public List<Imparticion> getImparticiones() {
		return imparticiones;
	}

	public void setImparticiones(List<Imparticion> imparticiones) {
		this.imparticiones = imparticiones;
	}

	public void addImparticion(Imparticion imparticion) {
		imparticiones.add(imparticion);
		imparticion.setCurso(this);
	}

	public void removeImparticion(Imparticion imparticion) {
		imparticiones.remove(imparticion);
		imparticion.setCurso(null);
	}

}
