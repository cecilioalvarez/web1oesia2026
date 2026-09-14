package es.oesia.web1.aceptacion.paginas;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Page Object de la pantalla de confirmación mostrada tras guardar un alumno.
 */
public class AlumnoGuardadoPage extends PaginaBase {

	public AlumnoGuardadoPage(WebDriver driver) {
		super(driver);
		new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.titleIs("Alumno guardado"));
	}

	public String id() {
		return elemento("alumno-id").getText();
	}

	public String dni() {
		return elemento("alumno-dni").getText();
	}

	public String nombre() {
		return elemento("alumno-nombre").getText();
	}

	public String apellidos() {
		return elemento("alumno-apellidos").getText();
	}

	public ListaAlumnosPage aceptar() {
		elemento("aceptar").click();
		return new ListaAlumnosPage(driver);
	}

}
