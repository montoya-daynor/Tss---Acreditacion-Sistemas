package tss.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tss.entity.*;
import tss.repository.RespuestaTituladoRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class RespuestaTituladoService {

    private final RespuestaTituladoRepository repository;
    private volatile List<RespuestaTitulado> cache = null;

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
        RespuestaTitulado guardado = repository.save(respuesta);
        invalidarCache();
        return guardado;
    }

    // RETORNA TODAS LAS RESPUESTAS PARA EL DASHBOARD CON CACHÉ EN MEMORIA
    public List<RespuestaTitulado> listarTodos() {
        if (cache == null) {
            synchronized (this) {
                if (cache == null) {
                    cache = Collections.unmodifiableList(new ArrayList<>(repository.findAll()));
                }
            }
        }
        return cache;
    }

    // INVALIDA LA CACHÉ CUANDO SE IMPORTA O ACTUALIZA LA INFORMACIÓN
    public synchronized void invalidarCache() {
        this.cache = null;
    }
}
