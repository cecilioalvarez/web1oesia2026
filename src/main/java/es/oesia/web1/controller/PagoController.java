package es.oesia.web1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import es.oesia.web1.negocio.Pago;
import es.oesia.web1.servicio.PagoServicio;
import jakarta.validation.Valid;

@Controller
public class PagoController {

	private final PagoServicio pagoServicio;

	public PagoController(PagoServicio pagoServicio) {
		this.pagoServicio = pagoServicio;
	}

	@GetMapping("/matriculas/{matriculaId}/pago/nuevo")
	public String formularioNuevoPago(@PathVariable Long matriculaId, Model model) {
		model.addAttribute("matricula", pagoServicio.obtenerMatricula(matriculaId));
		model.addAttribute("pago", new Pago());
		return "nuevopago";
	}

	@PostMapping("/matriculas/{matriculaId}/pago")
	public String insertarPago(@PathVariable Long matriculaId, @Valid @ModelAttribute("pago") Pago pago,
			BindingResult bindingResult, Model model) {
		if (bindingResult.hasErrors()) {
			model.addAttribute("matricula", pagoServicio.obtenerMatricula(matriculaId));
			return "nuevopago";
		}
		Pago pagoGuardado = pagoServicio.guardarPago(matriculaId, pago);
		model.addAttribute("pago", pagoGuardado);
		return "pagoguardado";
	}

}
