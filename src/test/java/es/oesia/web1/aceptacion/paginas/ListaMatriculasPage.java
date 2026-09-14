package es.oesia.web1.aceptacion.paginas;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Page Object del listado de matrículas de un alumno (/alumnos/{id}/matriculas).
 */
public class ListaMatriculasPage extends PaginaBase {

	public ListaMatriculasPage(WebDriver driver) {
		super(driver);
		new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.titleIs("Matrículas del alumno"));
	}

	public String urlActual() {
		return driver.getCurrentUrl();
	}

	public boolean contieneTexto(String texto) {
		return driver.findElement(By.tagName("body")).getText().contains(texto);
	}

}
