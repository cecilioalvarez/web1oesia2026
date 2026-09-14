package es.oesia.web1.servicio;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.oesia.web1.negocio.Matricula;
import es.oesia.web1.negocio.Pago;
import es.oesia.web1.repository.MatriculaRepository;
import es.oesia.web1.repository.PagoRepository;

@Service
@Transactional
public class PagoServicio {

	private final PagoRepository pagoRepository;
	private final MatriculaRepository matriculaRepository;

	public PagoServicio(PagoRepository pagoRepository, MatriculaRepository matriculaRepository) {
		this.pagoRepository = pagoRepository;
		this.matriculaRepository = matriculaRepository;
	}

	@Transactional(readOnly = true)
	public Matricula obtenerMatricula(Long matriculaId) {
		return matriculaRepository.findById(matriculaId).orElseThrow();
	}

	public Pago guardarPago(Long matriculaId, Pago pago) {
		Matricula matricula = matriculaRepository.findById(matriculaId).orElseThrow();
		matricula.setPago(pago);
		return pagoRepository.save(pago);
	}

}
