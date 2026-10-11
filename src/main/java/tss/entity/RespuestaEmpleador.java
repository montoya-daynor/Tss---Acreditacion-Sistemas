package tss.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "respuesta_empleador")
public class RespuestaEmpleador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime fechaRegistro = LocalDateTime.now();
    private String email;
    private String nombreEmpresa;
    private String tipoOrganizacion;
    private String tamanio;
    private String rubro;
    private Boolean contratoIngenieros;
    private String probabilidadContratacion;
    private String nivelFormacionRequerido;
    private String medioConvocatoria;

    @Column(columnDefinition = "TEXT")
    private String areasConocimientoEmergentes;
    @Column(columnDefinition = "TEXT")
    private String herramientasTecnologicas;
    @Column(columnDefinition = "TEXT")
    private String habilidadesRequeridas;

    private String likert1;
    private String likert2;
    private String likert3;
    private String likert4;
    private String likert5;
    private String likert6;
    private String likert7;
    private String likert8;
    private String likert9;
    private String likert10;

    @OneToMany(mappedBy = "respuestaEmpleador", cascade = CascadeType.ALL)
    private List<RespuestaEmpleadorCargo> cargos;

    public Long getId() { return id; }
    public void setId(Long v) { this.id = v; }
    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDateTime v) { this.fechaRegistro = v; }
    public String getEmail() { return email; }
    public void setEmail(String v) { this.email = v; }
    public String getNombreEmpresa() { return nombreEmpresa; }
    public void setNombreEmpresa(String v) { this.nombreEmpresa = v; }
    public String getTipoOrganizacion() { return tipoOrganizacion; }
    public void setTipoOrganizacion(String v) { this.tipoOrganizacion = v; }
    public String getTamanio() { return tamanio; }
    public void setTamanio(String v) { this.tamanio = v; }
    public String getRubro() { return rubro; }
    public void setRubro(String v) { this.rubro = v; }
    public Boolean getContratoIngenieros() { return contratoIngenieros; }
    public void setContratoIngenieros(Boolean v) { this.contratoIngenieros = v; }
    public String getProbabilidadContratacion() { return probabilidadContratacion; }
    public void setProbabilidadContratacion(String v) { this.probabilidadContratacion = v; }
    public String getNivelFormacionRequerido() { return nivelFormacionRequerido; }
    public void setNivelFormacionRequerido(String v) { this.nivelFormacionRequerido = v; }
    public String getMedioConvocatoria() { return medioConvocatoria; }
    public void setMedioConvocatoria(String v) { this.medioConvocatoria = v; }
    public String getAreasConocimientoEmergentes() { return areasConocimientoEmergentes; }
    public void setAreasConocimientoEmergentes(String v) { this.areasConocimientoEmergentes = v; }
    public String getHerramientasTecnologicas() { return herramientasTecnologicas; }
    public void setHerramientasTecnologicas(String v) { this.herramientasTecnologicas = v; }
    public String getHabilidadesRequeridas() { return habilidadesRequeridas; }
    public void setHabilidadesRequeridas(String v) { this.habilidadesRequeridas = v; }
    public String getLikert1() { return likert1; }
    public void setLikert1(String v) { this.likert1 = v; }
    public String getLikert2() { return likert2; }
    public void setLikert2(String v) { this.likert2 = v; }
    public String getLikert3() { return likert3; }
    public void setLikert3(String v) { this.likert3 = v; }
    public String getLikert4() { return likert4; }
    public void setLikert4(String v) { this.likert4 = v; }
    public String getLikert5() { return likert5; }
    public void setLikert5(String v) { this.likert5 = v; }
    public String getLikert6() { return likert6; }
    public void setLikert6(String v) { this.likert6 = v; }
    public String getLikert7() { return likert7; }
    public void setLikert7(String v) { this.likert7 = v; }
    public String getLikert8() { return likert8; }
    public void setLikert8(String v) { this.likert8 = v; }
    public String getLikert9() { return likert9; }
    public void setLikert9(String v) { this.likert9 = v; }
    public String getLikert10() { return likert10; }
    public void setLikert10(String v) { this.likert10 = v; }
    public List<RespuestaEmpleadorCargo> getCargos() { return cargos; }
    public void setCargos(List<RespuestaEmpleadorCargo> v) { this.cargos = v; }
}
