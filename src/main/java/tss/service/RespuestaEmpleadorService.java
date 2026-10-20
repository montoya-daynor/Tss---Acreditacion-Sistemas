package tss.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tss.entity.RespuestaEmpleador;
import tss.repository.RespuestaEmpleadorRepository;

import java.util.List;

@Service
public class RespuestaEmpleadorService {

    private final RespuestaEmpleadorRepository repository;
    private volatile List<RespuestaEmpleador> cache = null;

    public RespuestaEmpleadorService(RespuestaEmpleadorRepository repository) {
        this.repository = repository;
    }

    public void invalidarCache() {
        this.cache = null;
    }

    // GUARDA LA RESPUESTA DEL EMPLEADOR Y SUS CARGOS EN BD
    @Transactional
    public RespuestaEmpleador guardar(RespuestaEmpleador respuesta) {
        if (respuesta.getCargos() != null)
            respuesta.getCargos().forEach(c -> c.setRespuestaEmpleador(respuesta));
        RespuestaEmpleador guardado = repository.save(respuesta);
        invalidarCache();
        return guardado;
    }

    // RETORNA TODAS LAS RESPUESTAS DE EMPLEADORES (CON CACHÉ EN MEMORIA)
    public List<RespuestaEmpleador> listarTodos() {
        List<RespuestaEmpleador> local = cache;
        if (local == null) {
            synchronized (this) {
                if (cache == null) {
                    cache = repository.findAll();
                }
                local = cache;
            }
        }
        return local;
    }
}
