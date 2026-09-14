package es.oesia.web1.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import es.oesia.web1.negocio.Pago;

public interface PagoRepository extends JpaRepository<Pago, Long> {
}
