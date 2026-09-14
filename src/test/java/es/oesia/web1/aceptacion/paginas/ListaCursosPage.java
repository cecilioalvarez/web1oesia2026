package es.oesia.web1.aceptacion.paginas;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Page Object del listado de cursos (/cursos).
 */
public class ListaCursosPage extends PaginaBase {

	public ListaCursosPage(WebDriver driver) {
		super(driver);
		new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.titleIs("Listado de cursos"));
	}

	public static ListaCursosPage visitar(WebDriver driver, String urlBase) {
		driver.get(urlBase + "/cursos");
		return new ListaCursosPage(driver);
	}

	/**
	 * Pulsa "Añadir impartición" en la fila del curso con el título indicado.
	 */
	public NuevaImparticionPage anadirImparticionAlCurso(String titulo) {
		fila(titulo).findElement(By.cssSelector("[data-test=anadir-imparticion]")).click();
		return new NuevaImparticionPage(driver);
	}

}
