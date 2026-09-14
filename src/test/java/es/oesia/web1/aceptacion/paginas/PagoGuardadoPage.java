package es.oesia.web1.aceptacion.paginas;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Page Object de la pantalla de confirmación mostrada tras guardar un pago.
 */
public class PagoGuardadoPage extends PaginaBase {

	public PagoGuardadoPage(WebDriver driver) {
		super(driver);
		new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.titleIs("Pago guardado"));
	}

	public String id() {
		return elemento("pago-id").getText();
	}

	public String fecha() {
		return elemento("pago-fecha").getText();
	}

	public String importe() {
		return elemento("pago-importe").getText();
	}

	public ListaMatriculasPage aceptar() {
		elemento("aceptar").click();
		return new ListaMatriculasPage(driver);
	}

}
