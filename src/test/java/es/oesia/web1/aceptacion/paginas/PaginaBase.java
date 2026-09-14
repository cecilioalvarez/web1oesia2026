package es.oesia.web1.aceptacion.paginas;

import java.time.LocalDate;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
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

	/**
	 * Busca la fila de una tabla que contiene una celda con el texto exacto
	 * indicado, para poder localizar y accionar los controles de esa fila en
	 * concreto (por ejemplo, cuando data-test se repite en varias filas).
	 */
	protected WebElement fila(String texto) {
		return driver.findElement(By.xpath("//tr[td[normalize-space(text())='" + texto + "']]"));
	}

	/**
	 * Los inputs "date" nativos no aceptan de forma fiable sendKeys con el
	 * texto ISO (el orden esperado depende del locale del navegador), así que
	 * se fija el valor directamente vía JavaScript con el formato ISO que
	 * entienden internamente.
	 */
	protected void escribirFecha(String dataTest, LocalDate fecha) {
		((JavascriptExecutor) driver).executeScript("arguments[0].value = arguments[1];", elemento(dataTest),
				fecha.toString());
		pausaVisual();
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
