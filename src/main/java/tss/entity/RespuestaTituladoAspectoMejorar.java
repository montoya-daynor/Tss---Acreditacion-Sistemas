package tss.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "respuesta_titulado_aspecto_mejorar")
public class RespuestaTituladoAspectoMejorar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "respuesta_titulado_id")
    private RespuestaTitulado respuestaTitulado;

    private String aspecto;

    public Long getId() { return id; }
    public void setId(Long v) { this.id = v; }
    public RespuestaTitulado getRespuestaTitulado() { return respuestaTitulado; }
    public void setRespuestaTitulado(RespuestaTitulado v) { this.respuestaTitulado = v; }
    public String getAspecto() { return aspecto; }
    public void setAspecto(String v) { this.aspecto = v; }
}
