package tss.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "respuesta_titulado")
public class RespuestaTitulado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime fechaRegistro = LocalDateTime.now();
    private String edad;
    private String genero;
    private Boolean realizoPosgrado;
    private String nivelPosgrado;
    private String institucionPosgrado;
    private String financiamientoPosgrado;
    private String nombrePrograma;
    private Boolean interesadoPosgrado;
    private String areaInteresPosgrado;
    private String institucionInteresPosgrado;
    private String opinionPosgradoFcyt;
    private Integer aniosVidaProfesional;
    private Integer aniosDesempleado;
    private String situacionLaboral;
    private Boolean primerEmpleo;
    private String tiempoPrimerEmpleo;
    private String cantidadEmpleos;
    private String satisfaccionFormacion;
    private String concordanciaMercado;
    private Boolean interesadoRedGraduados;
    private String email;
    private String telefono;
    private String linkedin;

    @OneToOne(mappedBy = "respuestaTitulado", cascade = CascadeType.ALL)
    private RespuestaEmprendedor emprendedor;

    @OneToMany(mappedBy = "respuestaTitulado", cascade = CascadeType.ALL)
    private List<RespuestaTituladoAspectoUtil> aspectosUtiles;

    @OneToMany(mappedBy = "respuestaTitulado", cascade = CascadeType.ALL)
    private List<RespuestaTituladoAspectoMejorar> aspectosMejorar;

    @OneToMany(mappedBy = "respuestaTitulado", cascade = CascadeType.ALL)
    private List<RespuestaTituladoAsignaturaUtil> asignaturasUtiles;

    @OneToMany(mappedBy = "respuestaTitulado", cascade = CascadeType.ALL)
    private List<RespuestaTituladoAsignaturaInutile> asignaturasInutiles;

    public Long getId() { return id; }
    public void setId(Long v) { this.id = v; }
    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDateTime v) { this.fechaRegistro = v; }
    public String getEdad() { return edad; }
    public void setEdad(String v) { this.edad = v; }
    public String getGenero() { return genero; }
    public void setGenero(String v) { this.genero = v; }
    public Boolean getRealizoPosgrado() { return realizoPosgrado; }
    public void setRealizoPosgrado(Boolean v) { this.realizoPosgrado = v; }
    public String getNivelPosgrado() { return nivelPosgrado; }
    public void setNivelPosgrado(String v) { this.nivelPosgrado = v; }
    public String getInstitucionPosgrado() { return institucionPosgrado; }
    public void setInstitucionPosgrado(String v) { this.institucionPosgrado = v; }
    public String getFinanciamientoPosgrado() { return financiamientoPosgrado; }
    public void setFinanciamientoPosgrado(String v) { this.financiamientoPosgrado = v; }
    public String getNombrePrograma() { return nombrePrograma; }
    public void setNombrePrograma(String v) { this.nombrePrograma = v; }
    public Boolean getInteresadoPosgrado() { return interesadoPosgrado; }
    public void setInteresadoPosgrado(Boolean v) { this.interesadoPosgrado = v; }
    public String getAreaInteresPosgrado() { return areaInteresPosgrado; }
    public void setAreaInteresPosgrado(String v) { this.areaInteresPosgrado = v; }
    public String getInstitucionInteresPosgrado() { return institucionInteresPosgrado; }
    public void setInstitucionInteresPosgrado(String v) { this.institucionInteresPosgrado = v; }
    public String getOpinionPosgradoFcyt() { return opinionPosgradoFcyt; }
    public void setOpinionPosgradoFcyt(String v) { this.opinionPosgradoFcyt = v; }
    public Integer getAniosVidaProfesional() { return aniosVidaProfesional; }
    public void setAniosVidaProfesional(Integer v) { this.aniosVidaProfesional = v; }
    public Integer getAniosDesempleado() { return aniosDesempleado; }
    public void setAniosDesempleado(Integer v) { this.aniosDesempleado = v; }
    public String getSituacionLaboral() { return situacionLaboral; }
    public void setSituacionLaboral(String v) { this.situacionLaboral = v; }
    public Boolean getPrimerEmpleo() { return primerEmpleo; }
    public void setPrimerEmpleo(Boolean v) { this.primerEmpleo = v; }
    public String getTiempoPrimerEmpleo() { return tiempoPrimerEmpleo; }
    public void setTiempoPrimerEmpleo(String v) { this.tiempoPrimerEmpleo = v; }
    public String getCantidadEmpleos() { return cantidadEmpleos; }
    public void setCantidadEmpleos(String v) { this.cantidadEmpleos = v; }
    public String getSatisfaccionFormacion() { return satisfaccionFormacion; }
    public void setSatisfaccionFormacion(String v) { this.satisfaccionFormacion = v; }
    public String getConcordanciaMercado() { return concordanciaMercado; }
    public void setConcordanciaMercado(String v) { this.concordanciaMercado = v; }
    public Boolean getInteresadoRedGraduados() { return interesadoRedGraduados; }
    public void setInteresadoRedGraduados(Boolean v) { this.interesadoRedGraduados = v; }
    public String getEmail() { return email; }
    public void setEmail(String v) { this.email = v; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String v) { this.telefono = v; }
    public String getLinkedin() { return linkedin; }
    public void setLinkedin(String v) { this.linkedin = v; }
    public RespuestaEmprendedor getEmprendedor() { return emprendedor; }
    public void setEmprendedor(RespuestaEmprendedor v) { this.emprendedor = v; }
    public List<RespuestaTituladoAspectoUtil> getAspectosUtiles() { return aspectosUtiles; }
    public void setAspectosUtiles(List<RespuestaTituladoAspectoUtil> v) { this.aspectosUtiles = v; }
    public List<RespuestaTituladoAspectoMejorar> getAspectosMejorar() { return aspectosMejorar; }
    public void setAspectosMejorar(List<RespuestaTituladoAspectoMejorar> v) { this.aspectosMejorar = v; }
    public List<RespuestaTituladoAsignaturaUtil> getAsignaturasUtiles() { return asignaturasUtiles; }
    public void setAsignaturasUtiles(List<RespuestaTituladoAsignaturaUtil> v) { this.asignaturasUtiles = v; }
    public List<RespuestaTituladoAsignaturaInutile> getAsignaturasInutiles() { return asignaturasInutiles; }
    public void setAsignaturasInutiles(List<RespuestaTituladoAsignaturaInutile> v) { this.asignaturasInutiles = v; }
}
