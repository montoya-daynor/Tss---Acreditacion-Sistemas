package tss.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import tss.entity.RespuestaTitulado;

import java.util.List;

public interface RespuestaTituladoRepository extends JpaRepository<RespuestaTitulado, Long> {

    @Override
    @EntityGraph(attributePaths = {"emprendedor"})
    List<RespuestaTitulado> findAll();
}
