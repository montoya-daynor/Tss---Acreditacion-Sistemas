package tss.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "respuesta_empleador_cargo")
public class RespuestaEmpleadorCargo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "respuesta_empleador_id")
    private RespuestaEmpleador respuestaEmpleador;

    private String cargo;

    public Long getId() { return id; }
    public void setId(Long v) { this.id = v; }
    public RespuestaEmpleador getRespuestaEmpleador() { return respuestaEmpleador; }
    public void setRespuestaEmpleador(RespuestaEmpleador v) { this.respuestaEmpleador = v; }
    public String getCargo() { return cargo; }
    public void setCargo(String v) { this.cargo = v; }
}
