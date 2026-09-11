package es.oesia.web1.negocio;

import jakarta.validation.constraints.Max;

public class Curso {

	private String titulo;

	@Max(value = 200, message = "La duración no puede superar las 200 horas")
	private int duracion;

	private String descripcion;

	public Curso() {
	}

	public Curso(String titulo, int duracion, String descripcion) {
		this.titulo = titulo;
		this.duracion = duracion;
		this.descripcion = descripcion;
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

}
