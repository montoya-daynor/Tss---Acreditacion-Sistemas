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

    @Column(name = "fecha_respuesta")
    private LocalDateTime fechaRespuesta = LocalDateTime.now();

    @Column(name = "nombre_organizacion")
    private String nombreOrganizacion;

    @Column(name = "tipo_organizacion")
    private String tipoOrganizacion;

    @Column(name = "tamano_organizacion")
    private String tamanoOrganizacion;

    @Column(name = "rubro")
    private String rubro;

    @Column(name = "contrato_ingenieros_5_anios")
    private String contratoIngenieros5Anios;

    @Column(name = "prevision_contratacion")
    private String previsionContratacion;

    @Column(name = "nivel_formacion_exigido")
    private String nivelFormacionExigido;

    @Column(name = "medio_convocatoria")
    private String medioConvocatoria;

    @Column(name = "cargo_apoyo")
    private String cargoApoyo;

    @Column(name = "cargo_tecnico")
    private String cargoTecnico;

    @Column(name = "cargo_analista")
    private String cargoAnalista;

    @Column(name = "cargo_especialista")
    private String cargoEspecialista;

    @Column(name = "cargo_supervisor")
    private String cargoSupervisor;

    @Column(name = "cargo_jefatura")
    private String cargoJefatura;

    @Column(name = "cargo_consultor")
    private String cargoConsultor;

    @Column(name = "cargo_docente")
    private String cargoDocente;

    @Column(name = "areas_emergentes", columnDefinition = "TEXT")
    private String areasEmergentes;

    @Column(name = "herramientas_requeridas", columnDefinition = "TEXT")
    private String herramientasRequeridas;

    @Column(name = "habilidades_requeridas", columnDefinition = "TEXT")
    private String habilidadesRequeridas;

    @Column(name = "lk_confianza_formadora")
    private String lkConfianzaFormadora;

    @Column(name = "lk_titulo_consistente")
    private String lkTituloConsistente;

    @Column(name = "lk_autoridades_consultan")
    private String lkAutoridadesConsultan;

    @Column(name = "lk_conoce_perfil_egreso")
    private String lkConocePerfilEgreso;

    @Column(name = "lk_perfil_coherente_desempeno")
    private String lkPerfilCoherenteDesempeno;

    @Column(name = "lk_formacion_desempeno_destacado")
    private String lkFormacionDesempenoDestacado;

    @Column(name = "lk_carrera_incorpora_opinion")
    private String lkCarreraIncorporaOpinion;

    @Column(name = "lk_competencias_laborales")
    private String lkCompetenciasLaborales;

    @Column(name = "lk_valores_etica")
    private String lkValoresEtica;

    @Column(name = "lk_participo_retroalimentacion")
    private String lkParticipoRetroalimentacion;

    @Column(name = "lk_vinculos_oportunidades")
    private String lkVinculosOportunidades;

    @Column(name = "lk_participo_vinculacion_entorno")
    private String lkParticipoVinculacionEntorno;

    @Column(name = "lk_participo_difusion_empleo")
    private String lkParticipoDifusionEmpleo;

    @Column(name = "lk_participo_vinculacion_profesionales")
    private String lkParticipoVinculacionProfesionales;

    @Column(name = "lk_consultado_desempeno")
    private String lkConsultadoDesempeno;

    @Column(name = "lk_carrera_consulto_pertinencia")
    private String lkCarreraConsultoPertinencia;

    @Column(name = "lk_recurre_umss_talento")
    private String lkRecurreUmssTalento;

    @Column(name = "lk_consultado_autoevaluacion")
    private String lkConsultadoAutoevaluacion;

    @OneToMany(mappedBy = "respuestaEmpleador", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<RespuestaEmpleadorCargo> cargos;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDateTime getFechaRespuesta() { return fechaRespuesta; }
    public void setFechaRespuesta(LocalDateTime fechaRespuesta) { this.fechaRespuesta = fechaRespuesta; }

    public String getNombreOrganizacion() { return nombreOrganizacion; }
    public void setNombreOrganizacion(String nombreOrganizacion) { this.nombreOrganizacion = nombreOrganizacion; }

    public String getTipoOrganizacion() { return tipoOrganizacion; }
    public void setTipoOrganizacion(String tipoOrganizacion) { this.tipoOrganizacion = tipoOrganizacion; }

    public String getTamanoOrganizacion() { return tamanoOrganizacion; }
    public void setTamanoOrganizacion(String tamanoOrganizacion) { this.tamanoOrganizacion = tamanoOrganizacion; }

    public String getRubro() { return rubro; }
    public void setRubro(String rubro) { this.rubro = rubro; }

    public String getContratoIngenieros5Anios() { return contratoIngenieros5Anios; }
    public void setContratoIngenieros5Anios(String contratoIngenieros5Anios) { this.contratoIngenieros5Anios = contratoIngenieros5Anios; }

    public String getPrevisionContratacion() { return previsionContratacion; }
    public void setPrevisionContratacion(String previsionContratacion) { this.previsionContratacion = previsionContratacion; }

    public String getNivelFormacionExigido() { return nivelFormacionExigido; }
    public void setNivelFormacionExigido(String nivelFormacionExigido) { this.nivelFormacionExigido = nivelFormacionExigido; }

    public String getMedioConvocatoria() { return medioConvocatoria; }
    public void setMedioConvocatoria(String medioConvocatoria) { this.medioConvocatoria = medioConvocatoria; }

    public String getCargoApoyo() { return cargoApoyo; }
    public void setCargoApoyo(String cargoApoyo) { this.cargoApoyo = cargoApoyo; }

    public String getCargoTecnico() { return cargoTecnico; }
    public void setCargoTecnico(String cargoTecnico) { this.cargoTecnico = cargoTecnico; }

    public String getCargoAnalista() { return cargoAnalista; }
    public void setCargoAnalista(String cargoAnalista) { this.cargoAnalista = cargoAnalista; }

    public String getCargoEspecialista() { return cargoEspecialista; }
    public void setCargoEspecialista(String cargoEspecialista) { this.cargoEspecialista = cargoEspecialista; }

    public String getCargoSupervisor() { return cargoSupervisor; }
    public void setCargoSupervisor(String cargoSupervisor) { this.cargoSupervisor = cargoSupervisor; }

    public String getCargoJefatura() { return cargoJefatura; }
    public void setCargoJefatura(String cargoJefatura) { this.cargoJefatura = cargoJefatura; }

    public String getCargoConsultor() { return cargoConsultor; }
    public void setCargoConsultor(String cargoConsultor) { this.cargoConsultor = cargoConsultor; }

    public String getCargoDocente() { return cargoDocente; }
    public void setCargoDocente(String cargoDocente) { this.cargoDocente = cargoDocente; }

    public String getAreasEmergentes() { return areasEmergentes; }
    public void setAreasEmergentes(String areasEmergentes) { this.areasEmergentes = areasEmergentes; }

    public String getHerramientasRequeridas() { return herramientasRequeridas; }
    public void setHerramientasRequeridas(String herramientasRequeridas) { this.herramientasRequeridas = herramientasRequeridas; }

    public String getHabilidadesRequeridas() { return habilidadesRequeridas; }
    public void setHabilidadesRequeridas(String habilidadesRequeridas) { this.habilidadesRequeridas = habilidadesRequeridas; }

    public String getLkConfianzaFormadora() { return lkConfianzaFormadora; }
    public void setLkConfianzaFormadora(String lkConfianzaFormadora) { this.lkConfianzaFormadora = lkConfianzaFormadora; }

    public String getLkTituloConsistente() { return lkTituloConsistente; }
    public void setLkTituloConsistente(String lkTituloConsistente) { this.lkTituloConsistente = lkTituloConsistente; }

    public String getLkAutoridadesConsultan() { return lkAutoridadesConsultan; }
    public void setLkAutoridadesConsultan(String lkAutoridadesConsultan) { this.lkAutoridadesConsultan = lkAutoridadesConsultan; }

    public String getLkConocePerfilEgreso() { return lkConocePerfilEgreso; }
    public void setLkConocePerfilEgreso(String lkConocePerfilEgreso) { this.lkConocePerfilEgreso = lkConocePerfilEgreso; }

    public String getLkPerfilCoherenteDesempeno() { return lkPerfilCoherenteDesempeno; }
    public void setLkPerfilCoherenteDesempeno(String lkPerfilCoherenteDesempeno) { this.lkPerfilCoherenteDesempeno = lkPerfilCoherenteDesempeno; }

    public String getLkFormacionDesempenoDestacado() { return lkFormacionDesempenoDestacado; }
    public void setLkFormacionDesempenoDestacado(String lkFormacionDesempenoDestacado) { this.lkFormacionDesempenoDestacado = lkFormacionDesempenoDestacado; }

    public String getLkCarreraIncorporaOpinion() { return lkCarreraIncorporaOpinion; }
    public void setLkCarreraIncorporaOpinion(String lkCarreraIncorporaOpinion) { this.lkCarreraIncorporaOpinion = lkCarreraIncorporaOpinion; }

    public String getLkCompetenciasLaborales() { return lkCompetenciasLaborales; }
    public void setLkCompetenciasLaborales(String lkCompetenciasLaborales) { this.lkCompetenciasLaborales = lkCompetenciasLaborales; }

    public String getLkValoresEtica() { return lkValoresEtica; }
    public void setLkValoresEtica(String lkValoresEtica) { this.lkValoresEtica = lkValoresEtica; }

    public String getLkParticipoRetroalimentacion() { return lkParticipoRetroalimentacion; }
    public void setLkParticipoRetroalimentacion(String lkParticipoRetroalimentacion) { this.lkParticipoRetroalimentacion = lkParticipoRetroalimentacion; }

    public String getLkVinculosOportunidades() { return lkVinculosOportunidades; }
    public void setLkVinculosOportunidades(String lkVinculosOportunidades) { this.lkVinculosOportunidades = lkVinculosOportunidades; }

    public String getLkParticipoVinculacionEntorno() { return lkParticipoVinculacionEntorno; }
    public void setLkParticipoVinculacionEntorno(String lkParticipoVinculacionEntorno) { this.lkParticipoVinculacionEntorno = lkParticipoVinculacionEntorno; }

    public String getLkParticipoDifusionEmpleo() { return lkParticipoDifusionEmpleo; }
    public void setLkParticipoDifusionEmpleo(String lkParticipoDifusionEmpleo) { this.lkParticipoDifusionEmpleo = lkParticipoDifusionEmpleo; }

    public String getLkParticipoVinculacionProfesionales() { return lkParticipoVinculacionProfesionales; }
    public void setLkParticipoVinculacionProfesionales(String lkParticipoVinculacionProfesionales) { this.lkParticipoVinculacionProfesionales = lkParticipoVinculacionProfesionales; }

    public String getLkConsultadoDesempeno() { return lkConsultadoDesempeno; }
    public void setLkConsultadoDesempeno(String lkConsultadoDesempeno) { this.lkConsultadoDesempeno = lkConsultadoDesempeno; }

    public String getLkCarreraConsultoPertinencia() { return lkCarreraConsultoPertinencia; }
    public void setLkCarreraConsultoPertinencia(String lkCarreraConsultoPertinencia) { this.lkCarreraConsultoPertinencia = lkCarreraConsultoPertinencia; }

    public String getLkRecurreUmssTalento() { return lkRecurreUmssTalento; }
    public void setLkRecurreUmssTalento(String lkRecurreUmssTalento) { this.lkRecurreUmssTalento = lkRecurreUmssTalento; }

    public String getLkConsultadoAutoevaluacion() { return lkConsultadoAutoevaluacion; }
    public void setLkConsultadoAutoevaluacion(String lkConsultadoAutoevaluacion) { this.lkConsultadoAutoevaluacion = lkConsultadoAutoevaluacion; }

    public List<RespuestaEmpleadorCargo> getCargos() { return cargos; }
    public void setCargos(List<RespuestaEmpleadorCargo> cargos) { this.cargos = cargos; }
}
