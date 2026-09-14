package es.oesia.web1.negocio;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

/**
 * Superclase de las entidades del modelo de negocio: centraliza el
 * identificador y sus accesores para no repetirlos en cada entidad.
 */
@MappedSuperclass
public abstract class BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/**
	 * @return el identificador de la entidad, o {@code null} si aún no se ha
	 *         persistido
	 */
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
