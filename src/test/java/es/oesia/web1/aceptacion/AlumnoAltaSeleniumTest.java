package es.oesia.web1.aceptacion;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;

import es.oesia.web1.aceptacion.paginas.AlumnoGuardadoPage;
import es.oesia.web1.aceptacion.paginas.ListaAlumnosPage;
import es.oesia.web1.aceptacion.paginas.NuevoAlumnoPage;
import es.oesia.web1.aceptacion.paginas.PaginaBase;

/**
 * Prueba de aceptación de extremo a extremo: levanta la aplicación completa en un
 * puerto aleatorio y usa un navegador real (Edge) para simular la navegación de
 * un usuario rellenando y guardando el formulario de alta de alumno, comprobando
 * la pantalla de confirmación y la vuelta al listado. La navegación por cada
 * pantalla se encapsula en los Page Objects del paquete "paginas".
 *
 * Por defecto el navegador se abre visible para poder observar la navegación.
 * Para ejecutarlo en modo headless (por ejemplo en un servidor de CI sin
 * pantalla) lanzar con -Dselenium.headless=true.
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class AlumnoAltaSeleniumTest {

	@LocalServerPort
	private int puerto;

	private WebDriver driver;

	@BeforeEach
	void iniciarNavegador() {
		EdgeOptions opciones = new EdgeOptions();
		if (PaginaBase.HEADLESS) {
			opciones.addArguments("--headless=new", "--disable-gpu");
		}
		opciones.addArguments("--window-size=1280,800");
		driver = new EdgeDriver(opciones);
	}

	@AfterEach
	void cerrarNavegador() {
		if (driver != null) {
			driver.quit();
		}
	}

	private String urlBase() {
		return "http://localhost:" + puerto;
	}

	@Test
	void rellenarYGuardarElFormularioDeAlumnoMuestraLaConfirmacionYPermiteVolverAlListado() {
		NuevoAlumnoPage formulario = NuevoAlumnoPage.visitar(driver, urlBase());
		assertEquals("Nuevo alumno", formulario.titulo());

		AlumnoGuardadoPage confirmacion = formulario.rellenar("12345678A", "Juan", "Pérez García").guardar();

		assertEquals("12345678A", confirmacion.dni());
		assertEquals("Juan", confirmacion.nombre());
		assertEquals("Pérez García", confirmacion.apellidos());
		assertFalse(confirmacion.id().isBlank());

		ListaAlumnosPage listado = confirmacion.aceptar();

		assertEquals(urlBase() + "/alumnos", listado.urlActual());
		assertTrue(listado.contieneTexto("12345678A"));
	}

}
