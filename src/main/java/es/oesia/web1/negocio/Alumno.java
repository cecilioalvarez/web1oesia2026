package es.oesia.web1.negocio;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Alumno {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "El DNI es obligatorio")
	private String dni;

	@NotBlank(message = "El nombre es obligatorio")
	private String nombre;

	@NotBlank(message = "Los apellidos son obligatorios")
	private String apellidos;

	@OneToMany(mappedBy = "alumno", orphanRemoval = true)
	private List<Matricula> matriculas = new ArrayList<>();

	public Alumno() {
	}

	public Alumno(String dni, String nombre, String apellidos) {
		this.dni = dni;
		this.nombre = nombre;
		this.apellidos = apellidos;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDni() {
		return dni;
	}

	public void setDni(String dni) {
		this.dni = dni;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public List<Matricula> getMatriculas() {
		return matriculas;
	}

	public void setMatriculas(List<Matricula> matriculas) {
		this.matriculas = matriculas;
	}

	public void addMatricula(Matricula matricula) {
		matriculas.add(matricula);
		matricula.setAlumno(this);
	}

	public void removeMatricula(Matricula matricula) {
		matriculas.remove(matricula);
		matricula.setAlumno(null);
	}

}
