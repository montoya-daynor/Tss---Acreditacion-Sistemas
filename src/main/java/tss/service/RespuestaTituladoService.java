package tss.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tss.entity.*;
import tss.repository.RespuestaTituladoRepository;

import java.util.List;

@Service
public class RespuestaTituladoService {

    private final RespuestaTituladoRepository repository;

    public RespuestaTituladoService(RespuestaTituladoRepository repository) {
        this.repository = repository;
    }

    // GUARDA LA RESPUESTA COMPLETA DEL TITULADO Y SUS RELACIONES EN BD
    @Transactional
    public RespuestaTitulado guardar(RespuestaTitulado respuesta) {
        if (respuesta.getAspectosUtiles() != null)
            respuesta.getAspectosUtiles().forEach(a -> a.setRespuestaTitulado(respuesta));
        if (respuesta.getAspectosMejorar() != null)
            respuesta.getAspectosMejorar().forEach(a -> a.setRespuestaTitulado(respuesta));
        if (respuesta.getAsignaturasUtiles() != null)
            respuesta.getAsignaturasUtiles().forEach(a -> a.setRespuestaTitulado(respuesta));
        if (respuesta.getAsignaturasInutiles() != null)
            respuesta.getAsignaturasInutiles().forEach(a -> a.setRespuestaTitulado(respuesta));
        if (respuesta.getEmprendedor() != null)
            respuesta.getEmprendedor().setRespuestaTitulado(respuesta);
        return repository.save(respuesta);
    }

    // RETORNA TODAS LAS RESPUESTAS PARA EL DASHBOARD
    public List<RespuestaTitulado> listarTodos() {
        return repository.findAll();
    }
}
