package tss.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tss.entity.RespuestaTitulado;

/**
 * Repositorio de RespuestaTitulado.
 * JpaRepository provee automáticamente los métodos CRUD:
 * save(), findById(), findAll(), deleteById(), etc.
 * No necesitamos escribir SQL, Spring Data JPA lo genera solo.
 */
public interface RespuestaTituladoRepository extends JpaRepository<RespuestaTitulado, Long> {
}
