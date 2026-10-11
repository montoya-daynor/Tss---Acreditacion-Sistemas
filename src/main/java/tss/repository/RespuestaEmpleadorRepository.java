package tss.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tss.entity.RespuestaEmpleador;

/**
 * Repositorio de RespuestaEmpleador.
 * Igual que RespuestaTituladoRepository, hereda todos los métodos CRUD.
 */
public interface RespuestaEmpleadorRepository extends JpaRepository<RespuestaEmpleador, Long> {
}
