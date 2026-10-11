package tss.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "respuesta_emprendedor")
public class RespuestaEmprendedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "respuesta_titulado_id")
    private RespuestaTitulado respuestaTitulado;

    private String origenEmprendimiento;
    private String tipoEntregable;
    private String financiamientoExterno;
    private String satisfaccionNegocio;
    private String importanciaFormacion;

    public Long getId() { return id; }
    public void setId(Long v) { this.id = v; }
    public RespuestaTitulado getRespuestaTitulado() { return respuestaTitulado; }
    public void setRespuestaTitulado(RespuestaTitulado v) { this.respuestaTitulado = v; }
    public String getOrigenEmprendimiento() { return origenEmprendimiento; }
    public void setOrigenEmprendimiento(String v) { this.origenEmprendimiento = v; }
    public String getTipoEntregable() { return tipoEntregable; }
    public void setTipoEntregable(String v) { this.tipoEntregable = v; }
    public String getFinanciamientoExterno() { return financiamientoExterno; }
    public void setFinanciamientoExterno(String v) { this.financiamientoExterno = v; }
    public String getSatisfaccionNegocio() { return satisfaccionNegocio; }
    public void setSatisfaccionNegocio(String v) { this.satisfaccionNegocio = v; }
    public String getImportanciaFormacion() { return importanciaFormacion; }
    public void setImportanciaFormacion(String v) { this.importanciaFormacion = v; }
}
