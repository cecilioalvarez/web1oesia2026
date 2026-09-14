package es.oesia.web1.aceptacion.paginas;

import java.time.Duration;
import java.time.LocalDate;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Page Object del formulario de nueva matrícula (/alumnos/{id}/matriculas/nueva).
 */
public class NuevaMatriculaPage extends PaginaBase {

	public NuevaMatriculaPage(WebDriver driver) {
		super(driver);
		new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.titleIs("Nueva matrícula"));
	}

	public NuevaMatriculaPage escribirFecha(LocalDate fecha) {
		escribirFecha("fecha", fecha);
		return this;
	}

	/**
	 * Selecciona, en la tabla de imparticiones disponibles, la fila cuya
	 * fecha de inicio coincide con la indicada.
	 */
	public NuevaMatriculaPage seleccionarImparticionConFechaInicio(LocalDate fechaInicio) {
		fila(fechaInicio.toString()).findElement(By.cssSelector("[data-test=imparticion]")).click();
		pausaVisual();
		return this;
	}

	public NuevoPagoPage guardar() {
		elemento("guardar").click();
		return new NuevoPagoPage(driver);
	}

}
