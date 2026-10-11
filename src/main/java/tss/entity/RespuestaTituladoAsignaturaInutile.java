package tss.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "respuesta_titulado_asignatura_inutile")
public class RespuestaTituladoAsignaturaInutile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "respuesta_titulado_id")
    private RespuestaTitulado respuestaTitulado;

    private String asignatura;

    public Long getId() { return id; }
    public void setId(Long v) { this.id = v; }
    public RespuestaTitulado getRespuestaTitulado() { return respuestaTitulado; }
    public void setRespuestaTitulado(RespuestaTitulado v) { this.respuestaTitulado = v; }
    public String getAsignatura() { return asignatura; }
    public void setAsignatura(String v) { this.asignatura = v; }
}
