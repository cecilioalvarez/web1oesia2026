package es.oesia.web1.negocio;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;

/**
 * Alumno del centro. Un alumno puede tener muchas {@link Matricula}, cada una
 * de las cuales lo vincula a una {@link Imparticion} concreta.
 */
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

	/**
	 * @param dni        DNI del alumno
	 * @param nombre     nombre del alumno
	 * @param apellidos  apellidos del alumno
	 */
	public Alumno(String dni, String nombre, String apellidos) {
		this.dni = dni;
		this.nombre = nombre;
		this.apellidos = apellidos;
	}

	/**
	 * @return el identificador del alumno, o {@code null} si aún no se ha
	 *         persistido
	 */
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return el DNI del alumno
	 */
	public String getDni() {
		return dni;
	}

	public void setDni(String dni) {
		this.dni = dni;
	}

	/**
	 * @return el nombre del alumno
	 */
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * @return los apellidos del alumno
	 */
	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	/**
	 * @return las matrículas del alumno
	 */
	public List<Matricula> getMatriculas() {
		return matriculas;
	}

	public void setMatriculas(List<Matricula> matriculas) {
		this.matriculas = matriculas;
	}

	/**
	 * Añade la matrícula a este alumno y sincroniza el lado inverso de la
	 * relación, dejando el alumno de la matrícula apuntando a {@code this}.
	 *
	 * @param matricula matrícula a añadir
	 */
	public void addMatricula(Matricula matricula) {
		matriculas.add(matricula);
		matricula.setAlumno(this);
	}

	/**
	 * Quita la matrícula de este alumno y sincroniza el lado inverso de la
	 * relación, dejando la matrícula sin alumno asociado.
	 *
	 * @param matricula matrícula a quitar
	 */
	public void removeMatricula(Matricula matricula) {
		matriculas.remove(matricula);
		matricula.setAlumno(null);
	}

}
