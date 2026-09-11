package es.oesia.web1.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HolaController {

	@GetMapping("/hola")
	public String hola(@RequestParam(defaultValue = "mundo") String nombre) {
		return "Hola, " + nombre + "!";
	}

}
