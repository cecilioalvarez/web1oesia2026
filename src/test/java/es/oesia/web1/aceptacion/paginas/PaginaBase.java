package es.oesia.web1.aceptacion.paginas;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Comportamiento común a todos los Page Objects: acceso al driver, búsqueda de
 * elementos por el atributo data-test y una pequeña pausa para poder observar
 * la navegación cuando el navegador no se ejecuta en modo headless.
 */
public abstract class PaginaBase {

	public static final boolean HEADLESS = Boolean.getBoolean("selenium.headless");

	private static final long PAUSA_VISUAL_MS = 600;

	protected final WebDriver driver;

	protected PaginaBase(WebDriver driver) {
		this.driver = driver;
	}

	protected WebElement elemento(String dataTest) {
		return driver.findElement(By.cssSelector("[data-test=" + dataTest + "]"));
	}

	protected void pausaVisual() {
		if (!HEADLESS) {
			try {
				Thread.sleep(PAUSA_VISUAL_MS);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
	}

	public String titulo() {
		return driver.getTitle();
	}

}
