package es.oesia.web1.aceptacion.paginas;

import java.time.Duration;
import java.time.LocalDate;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Page Object del formulario de nueva impartición (/cursos/{id}/imparticiones/nueva).
 */
public class NuevaImparticionPage extends PaginaBase {

	public NuevaImparticionPage(WebDriver driver) {
		super(driver);
		new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.titleIs("Nueva impartición"));
	}

	public NuevaImparticionPage escribirFechaInicio(LocalDate fechaInicio) {
		escribirFecha("fecha-inicio", fechaInicio);
		return this;
	}

	public NuevaImparticionPage escribirFechaFin(LocalDate fechaFin) {
		escribirFecha("fecha-fin", fechaFin);
		return this;
	}

	public NuevaImparticionPage rellenar(LocalDate fechaInicio, LocalDate fechaFin) {
		return escribirFechaInicio(fechaInicio).escribirFechaFin(fechaFin);
	}

	public ListaCursosPage guardar() {
		elemento("guardar").click();
		return new ListaCursosPage(driver);
	}

}
