package tss.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tss.entity.RespuestaEmpleador;
import tss.repository.RespuestaEmpleadorRepository;

import java.util.List;

@Service
public class RespuestaEmpleadorService {

    private final RespuestaEmpleadorRepository repository;

    public RespuestaEmpleadorService(RespuestaEmpleadorRepository repository) {
        this.repository = repository;
    }

    // GUARDA LA RESPUESTA DEL EMPLEADOR Y SUS CARGOS EN BD
    @Transactional
    public RespuestaEmpleador guardar(RespuestaEmpleador respuesta) {
        if (respuesta.getCargos() != null)
            respuesta.getCargos().forEach(c -> c.setRespuestaEmpleador(respuesta));
        return repository.save(respuesta);
    }

    // RETORNA TODAS LAS RESPUESTAS DE EMPLEADORES PARA EL DASHBOARD
    public List<RespuestaEmpleador> listarTodos() {
        return repository.findAll();
    }
}
