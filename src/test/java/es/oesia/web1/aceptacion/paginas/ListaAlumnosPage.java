package es.oesia.web1.aceptacion.paginas;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Page Object del listado de alumnos (/alumnos).
 */
public class ListaAlumnosPage extends PaginaBase {

	public ListaAlumnosPage(WebDriver driver) {
		super(driver);
		new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.titleIs("Listado de alumnos"));
	}

	public static ListaAlumnosPage visitar(WebDriver driver, String urlBase) {
		driver.get(urlBase + "/alumnos");
		return new ListaAlumnosPage(driver);
	}

	public String urlActual() {
		return driver.getCurrentUrl();
	}

	public boolean contieneTexto(String texto) {
		return driver.findElement(By.tagName("body")).getText().contains(texto);
	}

	/**
	 * Pulsa "Matricular" en la fila del alumno con el DNI indicado.
	 */
	public NuevaMatriculaPage matricular(String dni) {
		fila(dni).findElement(By.cssSelector("[data-test=matricular]")).click();
		return new NuevaMatriculaPage(driver);
	}

}
