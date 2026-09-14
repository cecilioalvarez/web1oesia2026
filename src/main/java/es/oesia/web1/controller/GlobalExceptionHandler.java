package es.oesia.web1.controller;

import java.util.NoSuchElementException;

import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {



	@ExceptionHandler(NoSuchElementException.class)
	public String manejarCursoNoEncontrado(RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute("error",
				"El curso solicitado ya no existe. Puede que haya sido eliminado por otra persona.");
		return "redirect:/cursos";
	}

}
