package es.oesia.web1.aceptacion.paginas;

import org.openqa.selenium.WebDriver;

/**
 * Page Object del formulario de alta de alumno (/alumnos/nuevo).
 */
public class NuevoAlumnoPage extends PaginaBase {

	public NuevoAlumnoPage(WebDriver driver) {
		super(driver);
	}

	public static NuevoAlumnoPage visitar(WebDriver driver, String urlBase) {
		driver.get(urlBase + "/alumnos/nuevo");
		return new NuevoAlumnoPage(driver);
	}

	public NuevoAlumnoPage escribirDni(String dni) {
		elemento("dni").sendKeys(dni);
		pausaVisual();
		return this;
	}

	public NuevoAlumnoPage escribirNombre(String nombre) {
		elemento("nombre").sendKeys(nombre);
		pausaVisual();
		return this;
	}

	public NuevoAlumnoPage escribirApellidos(String apellidos) {
		elemento("apellidos").sendKeys(apellidos);
		pausaVisual();
		return this;
	}

	public NuevoAlumnoPage rellenar(String dni, String nombre, String apellidos) {
		return escribirDni(dni).escribirNombre(nombre).escribirApellidos(apellidos);
	}

	public AlumnoGuardadoPage guardar() {
		elemento("guardar").click();
		return new AlumnoGuardadoPage(driver);
	}

}
