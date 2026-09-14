package es.oesia.web1.aceptacion;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDate;

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
import es.oesia.web1.aceptacion.paginas.ListaCursosPage;
import es.oesia.web1.aceptacion.paginas.ListaMatriculasPage;
import es.oesia.web1.aceptacion.paginas.NuevaMatriculaPage;
import es.oesia.web1.aceptacion.paginas.NuevoAlumnoPage;
import es.oesia.web1.aceptacion.paginas.NuevoPagoPage;
import es.oesia.web1.aceptacion.paginas.PagoGuardadoPage;
import es.oesia.web1.aceptacion.paginas.PaginaBase;

/**
 * Prueba de aceptación de extremo a extremo del flujo completo: alta de
 * alumno, alta de una impartición del curso sembrado "Java desde cero",
 * matriculación del alumno en ella y pago de esa matrícula, comprobando la
 * pantalla de confirmación del pago y que ambos (matrícula y pago) quedan
 * reflejados en el listado de matrículas del alumno.
 *
 * Sufijo "IT" (en vez de "Test") a propósito: requiere Edge instalado, así que
 * la ejecuta el plugin Failsafe con "mvn verify" y queda excluida de
 * "mvn test", que debe poder ejecutarse en cualquier máquina sin navegador.
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class MatriculaConPagoSeleniumIT {

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
	void matricularUnAlumnoYPagarLaMatriculaQuedaConfirmadoYReflejadoEnElListado() {
		String dni = "55667788C";
		LocalDate fechaInicioImparticion = LocalDate.of(2030, 1, 12);
		LocalDate fechaFinImparticion = LocalDate.of(2030, 2, 12);
		LocalDate fechaMatriculacion = LocalDate.of(2030, 1, 10);
		LocalDate fechaPago = LocalDate.of(2030, 1, 11);
		BigDecimal importe = new BigDecimal("150.00");

		AlumnoGuardadoPage confirmacionAlumno = NuevoAlumnoPage.visitar(driver, urlBase())
				.rellenar(dni, "Laura", "Gómez Ruiz")
				.guardar();
		ListaAlumnosPage listaAlumnos = confirmacionAlumno.aceptar();
		assertTrue(listaAlumnos.contieneTexto(dni));

		ListaCursosPage.visitar(driver, urlBase())
				.anadirImparticionAlCurso("Java desde cero")
				.rellenar(fechaInicioImparticion, fechaFinImparticion)
				.guardar();

		NuevaMatriculaPage formularioMatricula = ListaAlumnosPage.visitar(driver, urlBase()).matricular(dni);
		NuevoPagoPage formularioPago = formularioMatricula
				.escribirFecha(fechaMatriculacion)
				.seleccionarImparticionConFechaInicio(fechaInicioImparticion)
				.guardar();

		PagoGuardadoPage confirmacionPago = formularioPago.rellenar(fechaPago, importe).guardar();

		assertFalse(confirmacionPago.id().isBlank());
		assertEquals(fechaPago.toString(), confirmacionPago.fecha());
		assertEquals("150.00", confirmacionPago.importe());

		ListaMatriculasPage listaMatriculas = confirmacionPago.aceptar();

		assertTrue(listaMatriculas.urlActual().endsWith("/matriculas"));
		assertTrue(listaMatriculas.contieneTexto("Java desde cero"));
		assertTrue(listaMatriculas.contieneTexto(fechaPago.toString()));
		assertTrue(listaMatriculas.contieneTexto("150.00"));
	}

}
