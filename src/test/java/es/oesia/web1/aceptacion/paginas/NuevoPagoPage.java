package es.oesia.web1.aceptacion.paginas;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Page Object del formulario de nuevo pago (/matriculas/{id}/pago/nuevo).
 */
public class NuevoPagoPage extends PaginaBase {

	public NuevoPagoPage(WebDriver driver) {
		super(driver);
		new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.titleIs("Nuevo pago"));
	}

	public NuevoPagoPage escribirFecha(LocalDate fecha) {
		escribirFecha("fecha", fecha);
		return this;
	}

	public NuevoPagoPage escribirImporte(BigDecimal importe) {
		elemento("importe").sendKeys(importe.toPlainString());
		pausaVisual();
		return this;
	}

	public NuevoPagoPage rellenar(LocalDate fecha, BigDecimal importe) {
		return escribirFecha(fecha).escribirImporte(importe);
	}

	public PagoGuardadoPage guardar() {
		elemento("guardar").click();
		return new PagoGuardadoPage(driver);
	}

}
