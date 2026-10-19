package tss.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Entity
@Table(name = "respuesta_titulado")
public class RespuestaTitulado {

    // =========================================================================
    // DICCIONARIO DE 55 COLUMNAS - encuesta_titulados_limpio_v2.csv
    // =========================================================================

    // Col 0: Id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // Col 1: Hora de inicio
    @Column(name = "hora_inicio")
    private LocalDateTime horaInicio;

    // Col 2: Hora de finalización
    @Column(name = "hora_fin")
    private LocalDateTime horaFin;

    // Col 3: Correo electrónico de registro
    @Column(name = "correo_registro", length = 255)
    private String correoRegistro;

    // Col 4: Rango de edad
    @Column(name = "rango_edad", length = 20)
    private String rangoEdad;

    // Col 5: Género
    @Column(name = "genero", length = 10)
    private String genero;

    // Col 6: ¿Ha realizado formación complementaria? (SI / NO)
    @Column(name = "tiene_formacion_complementaria", length = 2)
    private String tieneFormacionComplementaria;

    // Col 7: Programa de mayor nivel concluido o en curso
    @Column(name = "nivel_programa_cursado", length = 20)
    private String nivelProgramaCursado;

    // Col 8: ¿Dónde cursó el programa?
    @Column(name = "institucion_cursada", length = 60)
    private String institucionCursada;

    // Col 9: Fuente de financiamiento
    @Column(name = "financiamiento_programa", length = 60)
    private String financiamientoPrograma;

    // Col 10: Nombre del programa
    @Column(name = "nombre_programa", columnDefinition = "TEXT")
    private String nombrePrograma;

    // Col 11: ¿Interesado en estudiar un posgrado? (SI / NO)
    @Column(name = "interes_posgrado", length = 2)
    private String interesPosgrado;

    // Col 12: Área de interés del posgrado
    @Column(name = "area_interes_posgrado", length = 40)
    private String areaInteresPosgrado;

    // Col 13: Organización educativa preferida
    @Column(name = "institucion_preferida", length = 60)
    private String institucionPreferida;

    // Col 14: Opinión sobre el posgrado de la FCyT
    @Column(name = "opinion_posgrado_fcyt", length = 50)
    private String opinionPosgradoFcyt;

    // Col 15: Años de vida profesional (decimal)
    @Column(name = "anios_vida_profesional", columnDefinition = "NUMERIC(5,2)")
    private Double aniosVidaProfesional;

    // Col 16: Años de desempleo (decimal)
    @Column(name = "anios_desempleo", columnDefinition = "NUMERIC(5,2)")
    private Double aniosDesempleo;

    // Col 17: Situación laboral actual
    @Column(name = "situacion_laboral", length = 70)
    private String situacionLaboral;

    // Col 18: Empresa donde trabaja
    @Column(name = "empresa", length = 150)
    private String empresa;

    // Col 19: Rubro de la organización
    @Column(name = "rubro", length = 40)
    private String rubro;

    // Col 20: Otro rubro
    @Column(name = "rubro_otro", length = 100)
    private String rubroOtro;

    // Col 21: Sector del trabajo
    @Column(name = "sector", length = 10)
    private String sector;

    // Col 22: Departamento de trabajo
    @Column(name = "departamento", length = 20)
    private String departamento;

    // Col 23: Área dentro de la organización
    @Column(name = "area_organizacion", length = 80)
    private String areaOrganizacion;

    // Col 24: Otra área
    @Column(name = "area_otra", length = 150)
    private String areaOtra;

    // Col 25: Cargo
    @Column(name = "cargo", length = 20)
    private String cargo;

    // Col 26: Otro cargo
    @Column(name = "cargo_otro", length = 150)
    private String cargoOtro;

    // Col 27: ¿Las labores son pertinentes a su formación?
    @Column(name = "pertinencia_labores", length = 30)
    private String pertinenciaLabores;

    // Col 28: Antigüedad en el trabajo actual
    @Column(name = "antiguedad_trabajo_actual", length = 30)
    private String antiguedadTrabajoActual;

    // Col 29: Medio por el que obtuvo el trabajo
    @Column(name = "medio_obtencion_trabajo", length = 80)
    private String medioObtencionTrabajo;

    // Col 30: Remuneración mensual
    @Column(name = "rango_remuneracion", length = 40)
    private String rangoRemuneracion;

    // Col 31: Origen del emprendimiento
    @Column(name = "origen_emprendimiento", length = 40)
    private String origenEmprendimiento;

    // Col 32: Tipo de entregable del negocio
    @Column(name = "tipo_entregable", length = 20)
    private String tipoEntregable;

    // Col 33: ¿Requirió financiamiento externo?
    @Column(name = "financiamiento_negocio", length = 60)
    private String financiamientoNegocio;

    // Col 34: Satisfacción con el negocio
    @Column(name = "satisfaccion_negocio", length = 20)
    private String satisfaccionNegocio;

    // Col 35: Importancia de la formación para el negocio
    @Column(name = "importancia_formacion_negocio", length = 20)
    private String importanciaFormacionNegocio;

    // Col 36: ¿Es su primer empleo?
    @Column(name = "es_primer_empleo", length = 2)
    private String esPrimerEmpleo;

    // Col 37: Razón por la que no trabaja en su área
    @Column(name = "razon_no_trabaja", length = 100)
    private String razonNoTrabaja;

    // Col 38: ¿Ha tenido algún trabajo antes?
    @Column(name = "trabajo_previo", length = 2)
    private String trabajoPrevio;

    // Col 39: Tiempo para conseguir el primer trabajo
    @Column(name = "tiempo_primer_empleo", length = 40)
    private String tiempoPrimerEmpleo;

    // Col 40: Cantidad de empleos desde que se tituló
    @Column(name = "cantidad_empleos", length = 10)
    private String cantidadEmpleos;

    // Col 41: Satisfacción con la formación recibida
    @Column(name = "satisfaccion_formacion", length = 30)
    private String satisfaccionFormacion;

    // Col 42: Concordancia formación-mercado laboral
    @Column(name = "concordancia_formacion", length = 30)
    private String concordanciaFormacion;

    // Col 43: Aspectos útiles de la carrera
    @Column(name = "aspectos_utiles", columnDefinition = "TEXT")
    private String aspectosUtiles;

    // Col 44: Aspectos a mejorar
    @Column(name = "aspectos_mejorar", columnDefinition = "TEXT")
    private String aspectosMejorar;

    // Col 45: 5 asignaturas con ventaja competitiva
    @Column(name = "asignaturas_ventaja", columnDefinition = "TEXT")
    private String asignaturasVentaja;

    // Col 46: Otra asignatura (ventaja)
    @Column(name = "asignatura_ventaja_otra", columnDefinition = "TEXT")
    private String asignaturaVentajaOtra;

    // Col 47: 5 asignaturas poco útiles
    @Column(name = "asignaturas_poco_utiles", columnDefinition = "TEXT")
    private String asignaturasPocoUtiles;

    // Col 48: Otra asignatura (poco útil)
    @Column(name = "asignatura_poco_util_otra", columnDefinition = "TEXT")
    private String asignaturaPocoUtilOtra;

    // Col 49: ¿Interés en la red de contactos de la carrera?
    @Column(name = "interes_red_contactos", length = 2)
    private String interesRedContactos;

    // Col 50: Email de contacto
    @Column(name = "email_contacto", length = 150)
    private String emailContacto;

    // Col 51: Teléfono de contacto
    @Column(name = "telefono_contacto", length = 40)
    private String telefonoContacto;

    // Col 52: LinkedIn
    @Column(name = "linkedin", length = 255)
    private String linkedin;

    // Col 53: Años de vida profesional [MESES]
    @Column(name = "meses_vida_profesional")
    private Integer mesesVidaProfesional;

    // Col 54: Desempleo [MESES]
    @Column(name = "meses_desempleo")
    private Integer mesesDesempleo;

    // =========================================================================
    // RELACIONES PARA FORMULARIOS Y VISTAS EXISTENTES
    // =========================================================================
    @OneToOne(mappedBy = "respuestaTitulado", cascade = CascadeType.ALL)
    private RespuestaEmprendedor emprendedor;

    @OneToMany(mappedBy = "respuestaTitulado", cascade = CascadeType.ALL)
    private List<RespuestaTituladoAspectoUtil> aspectosUtilesRel;

    @OneToMany(mappedBy = "respuestaTitulado", cascade = CascadeType.ALL)
    private List<RespuestaTituladoAspectoMejorar> aspectosMejorarRel;

    @OneToMany(mappedBy = "respuestaTitulado", cascade = CascadeType.ALL)
    private List<RespuestaTituladoAsignaturaUtil> asignaturasUtilesRel;

    @OneToMany(mappedBy = "respuestaTitulado", cascade = CascadeType.ALL)
    private List<RespuestaTituladoAsignaturaInutile> asignaturasInutilesRel;

    // =========================================================================
    // GETTERS Y SETTERS PRINCIPALES
    // =========================================================================

    public Long getId() { return id; }
    public void setId(Long v) { this.id = v; }

    public LocalDateTime getHoraInicio() { return horaInicio; }
    public void setHoraInicio(LocalDateTime v) { this.horaInicio = v; }

    public LocalDateTime getHoraFin() { return horaFin; }
    public void setHoraFin(LocalDateTime v) { this.horaFin = v; }

    public String getCorreoRegistro() { return correoRegistro; }
    public void setCorreoRegistro(String v) { this.correoRegistro = v; }

    public String getRangoEdad() { return rangoEdad; }
    public void setRangoEdad(String v) { this.rangoEdad = v; }

    public String getGenero() { return genero; }
    public void setGenero(String v) { this.genero = v; }

    public String getTieneFormacionComplementaria() { return tieneFormacionComplementaria; }
    public void setTieneFormacionComplementaria(String v) { this.tieneFormacionComplementaria = v; }

    public String getNivelProgramaCursado() { return nivelProgramaCursado; }
    public void setNivelProgramaCursado(String v) { this.nivelProgramaCursado = v; }

    public String getInstitucionCursada() { return institucionCursada; }
    public void setInstitucionCursada(String v) { this.institucionCursada = v; }

    public String getFinanciamientoPrograma() { return financiamientoPrograma; }
    public void setFinanciamientoPrograma(String v) { this.financiamientoPrograma = v; }

    public String getNombrePrograma() { return nombrePrograma; }
    public void setNombrePrograma(String v) { this.nombrePrograma = v; }

    public String getInteresPosgrado() { return interesPosgrado; }
    public void setInteresPosgrado(String v) { this.interesPosgrado = v; }

    public String getAreaInteresPosgrado() { return areaInteresPosgrado; }
    public void setAreaInteresPosgrado(String v) { this.areaInteresPosgrado = v; }

    public String getInstitucionPreferida() { return institucionPreferida; }
    public void setInstitucionPreferida(String v) { this.institucionPreferida = v; }

    public String getOpinionPosgradoFcyt() { return opinionPosgradoFcyt; }
    public void setOpinionPosgradoFcyt(String v) { this.opinionPosgradoFcyt = v; }

    public Double getAniosVidaProfesionalDecimal() { return aniosVidaProfesional; }
    public void setAniosVidaProfesional(Double v) { this.aniosVidaProfesional = v; }

    public Double getAniosDesempleoDecimal() { return aniosDesempleo; }
    public void setAniosDesempleo(Double v) { this.aniosDesempleo = v; }

    public String getSituacionLaboral() { return situacionLaboral; }
    public void setSituacionLaboral(String v) { this.situacionLaboral = v; }

    public String getEmpresa() { return empresa; }
    public void setEmpresa(String v) { this.empresa = v; }

    public String getRubro() { return rubro; }
    public void setRubro(String v) { this.rubro = v; }

    public String getRubroOtro() { return rubroOtro; }
    public void setRubroOtro(String v) { this.rubroOtro = v; }

    public String getSector() { return sector; }
    public void setSector(String v) { this.sector = v; }

    public String getDepartamento() { return departamento; }
    public void setDepartamento(String v) { this.departamento = v; }

    public String getAreaOrganizacion() { return areaOrganizacion; }
    public void setAreaOrganizacion(String v) { this.areaOrganizacion = v; }

    public String getAreaOtra() { return areaOtra; }
    public void setAreaOtra(String v) { this.areaOtra = v; }

    public String getCargo() { return cargo; }
    public void setCargo(String v) { this.cargo = v; }

    public String getCargoOtro() { return cargoOtro; }
    public void setCargoOtro(String v) { this.cargoOtro = v; }

    public String getPertinenciaLabores() { return pertinenciaLabores; }
    public void setPertinenciaLabores(String v) { this.pertinenciaLabores = v; }

    public String getAntiguedadTrabajoActual() { return antiguedadTrabajoActual; }
    public void setAntiguedadTrabajoActual(String v) { this.antiguedadTrabajoActual = v; }

    public String getMedioObtencionTrabajo() { return medioObtencionTrabajo; }
    public void setMedioObtencionTrabajo(String v) { this.medioObtencionTrabajo = v; }

    public String getRangoRemuneracion() { return rangoRemuneracion; }
    public void setRangoRemuneracion(String v) { this.rangoRemuneracion = v; }

    public String getOrigenEmprendimiento() { return origenEmprendimiento; }
    public void setOrigenEmprendimiento(String v) { this.origenEmprendimiento = v; }

    public String getTipoEntregable() { return tipoEntregable; }
    public void setTipoEntregable(String v) { this.tipoEntregable = v; }

    public String getFinanciamientoNegocio() { return financiamientoNegocio; }
    public void setFinanciamientoNegocio(String v) { this.financiamientoNegocio = v; }

    public String getSatisfaccionNegocio() { return satisfaccionNegocio; }
    public void setSatisfaccionNegocio(String v) { this.satisfaccionNegocio = v; }

    public String getImportanciaFormacionNegocio() { return importanciaFormacionNegocio; }
    public void setImportanciaFormacionNegocio(String v) { this.importanciaFormacionNegocio = v; }

    public String getEsPrimerEmpleo() {
        if (situacionLaboral != null && situacionLaboral.contains("NO trabajo")) {
            return null;
        }
        return esPrimerEmpleo;
    }
    public void setEsPrimerEmpleo(String v) { this.esPrimerEmpleo = v; }

    public String getRazonNoTrabaja() { return razonNoTrabaja; }
    public void setRazonNoTrabaja(String v) { this.razonNoTrabaja = v; }

    public String getTrabajoPrevio() { return trabajoPrevio; }
    public void setTrabajoPrevio(String v) { this.trabajoPrevio = v; }

    public String getTiempoPrimerEmpleo() { return tiempoPrimerEmpleo; }
    public void setTiempoPrimerEmpleo(String v) { this.tiempoPrimerEmpleo = v; }

    public String getCantidadEmpleos() { return cantidadEmpleos; }
    public void setCantidadEmpleos(String v) { this.cantidadEmpleos = v; }

    public String getSatisfaccionFormacion() { return satisfaccionFormacion; }
    public void setSatisfaccionFormacion(String v) { this.satisfaccionFormacion = v; }

    public String getConcordanciaFormacion() { return concordanciaFormacion; }
    public void setConcordanciaFormacion(String v) { this.concordanciaFormacion = v; }

    public String getAspectosUtilesTexto() { return aspectosUtiles; }
    public void setAspectosUtilesTexto(String v) { this.aspectosUtiles = v; }

    public String getAspectosMejorarTexto() { return aspectosMejorar; }
    public void setAspectosMejorarTexto(String v) { this.aspectosMejorar = v; }

    public String getAsignaturasVentaja() { return asignaturasVentaja; }
    public void setAsignaturasVentaja(String v) { this.asignaturasVentaja = v; }

    public String getAsignaturaVentajaOtra() { return asignaturaVentajaOtra; }
    public void setAsignaturaVentajaOtra(String v) { this.asignaturaVentajaOtra = v; }

    public String getAsignaturasPocoUtiles() { return asignaturasPocoUtiles; }
    public void setAsignaturasPocoUtiles(String v) { this.asignaturasPocoUtiles = v; }

    public String getAsignaturaPocoUtilOtra() { return asignaturaPocoUtilOtra; }
    public void setAsignaturaPocoUtilOtra(String v) { this.asignaturaPocoUtilOtra = v; }

    public String getInteresRedContactos() { return interesRedContactos; }
    public void setInteresRedContactos(String v) { this.interesRedContactos = v; }

    public String getEmailContacto() { return emailContacto; }
    public void setEmailContacto(String v) { this.emailContacto = v; }

    public String getTelefonoContacto() { return telefonoContacto; }
    public void setTelefonoContacto(String v) { this.telefonoContacto = v; }

    public String getLinkedin() { return linkedin; }
    public void setLinkedin(String v) { this.linkedin = v; }

    public Integer getMesesVidaProfesional() { return mesesVidaProfesional; }
    public void setMesesVidaProfesional(Integer v) { this.mesesVidaProfesional = v; }

    public Integer getMesesDesempleo() { return mesesDesempleo; }
    public void setMesesDesempleo(Integer v) { this.mesesDesempleo = v; }

    // =========================================================================
    // MÉTODOS DE COMPATIBILIDAD CON VISTAS Y SERVICIOS EXISTENTES
    // =========================================================================

    public LocalDateTime getFechaRegistro() { return horaInicio != null ? horaInicio : LocalDateTime.now(); }
    public void setFechaRegistro(LocalDateTime v) { if (this.horaInicio == null) this.horaInicio = v; }

    public String getEdad() { return rangoEdad; }
    public void setEdad(String v) { this.rangoEdad = v; }

    public Boolean getRealizoPosgrado() {
        return "SI".equalsIgnoreCase(tieneFormacionComplementaria);
    }
    public void setRealizoPosgrado(Boolean v) {
        this.tieneFormacionComplementaria = Boolean.TRUE.equals(v) ? "SI" : "NO";
    }

    public String getNivelPosgrado() { return nivelProgramaCursado; }
    public void setNivelPosgrado(String v) { this.nivelProgramaCursado = v; }

    public String getInstitucionPosgrado() { return institucionCursada; }
    public void setInstitucionPosgrado(String v) { this.institucionCursada = v; }

    public String getFinanciamientoPosgrado() { return financiamientoPrograma; }
    public void setFinanciamientoPosgrado(String v) { this.financiamientoPrograma = v; }

    public Boolean getInteresadoPosgrado() {
        return "SI".equalsIgnoreCase(interesPosgrado);
    }
    public void setInteresadoPosgrado(Boolean v) {
        this.interesPosgrado = Boolean.TRUE.equals(v) ? "SI" : "NO";
    }

    public String getInstitucionInteresPosgrado() { return institucionPreferida; }
    public void setInstitucionInteresPosgrado(String v) { this.institucionPreferida = v; }

    public Integer getAniosVidaProfesional() {
        if (aniosVidaProfesional != null) return aniosVidaProfesional.intValue();
        if (mesesVidaProfesional != null) return mesesVidaProfesional / 12;
        return null;
    }
    public void setAniosVidaProfesional(Integer v) {
        this.aniosVidaProfesional = v != null ? v.doubleValue() : null;
        this.mesesVidaProfesional = v != null ? v * 12 : null;
    }

    public Integer getAniosDesempleado() {
        if (aniosDesempleo != null) return aniosDesempleo.intValue();
        if (mesesDesempleo != null) return mesesDesempleo / 12;
        return null;
    }
    public void setAniosDesempleado(Integer v) {
        this.aniosDesempleo = v != null ? v.doubleValue() : null;
        this.mesesDesempleo = v != null ? v * 12 : null;
    }

    public Double getAniosVidaProfesionalDecimalCalculado() {
        if (aniosVidaProfesional != null) return aniosVidaProfesional;
        if (mesesVidaProfesional != null) return mesesVidaProfesional / 12.0;
        return null;
    }

    public Double getAniosDesempleadoDecimalCalculado() {
        if (aniosDesempleo != null) return aniosDesempleo;
        if (mesesDesempleo != null) return mesesDesempleo / 12.0;
        return null;
    }

    public Boolean getPrimerEmpleo() {
        if (situacionLaboral != null && situacionLaboral.contains("NO trabajo")) {
            return null;
        }
        if (esPrimerEmpleo == null || esPrimerEmpleo.trim().isEmpty()) {
            return null;
        }
        if ("SI".equalsIgnoreCase(esPrimerEmpleo.trim()) || "SÍ".equalsIgnoreCase(esPrimerEmpleo.trim())) {
            return true;
        }
        if ("NO".equalsIgnoreCase(esPrimerEmpleo.trim())) {
            return false;
        }
        return null;
    }
    public void setPrimerEmpleo(Boolean v) {
        this.esPrimerEmpleo = Boolean.TRUE.equals(v) ? "SI" : "NO";
    }

    public String getConcordanciaMercado() { return concordanciaFormacion; }
    public void setConcordanciaMercado(String v) { this.concordanciaFormacion = v; }

    public Boolean getInteresadoRedGraduados() {
        return "SI".equalsIgnoreCase(interesRedContactos);
    }
    public void setInteresadoRedGraduados(Boolean v) {
        this.interesRedContactos = Boolean.TRUE.equals(v) ? "SI" : "NO";
    }

    public String getEmail() { return emailContacto != null ? emailContacto : correoRegistro; }
    public void setEmail(String v) { this.emailContacto = v; if (this.correoRegistro == null) this.correoRegistro = v; }

    public String getTelefono() { return telefonoContacto; }
    public void setTelefono(String v) { this.telefonoContacto = v; }

    public RespuestaEmprendedor getEmprendedor() { return emprendedor; }
    public void setEmprendedor(RespuestaEmprendedor v) { this.emprendedor = v; }

    public List<RespuestaTituladoAspectoUtil> getAspectosUtiles() { return aspectosUtilesRel; }
    public void setAspectosUtiles(List<RespuestaTituladoAspectoUtil> v) {
        this.aspectosUtilesRel = v;
        if (v != null && !v.isEmpty()) {
            this.aspectosUtiles = v.stream().map(RespuestaTituladoAspectoUtil::getAspecto).filter(Objects::nonNull).collect(Collectors.joining(", "));
        }
    }

    public List<RespuestaTituladoAspectoMejorar> getAspectosMejorar() { return aspectosMejorarRel; }
    public void setAspectosMejorar(List<RespuestaTituladoAspectoMejorar> v) {
        this.aspectosMejorarRel = v;
        if (v != null && !v.isEmpty()) {
            this.aspectosMejorar = v.stream().map(RespuestaTituladoAspectoMejorar::getAspecto).filter(Objects::nonNull).collect(Collectors.joining(", "));
        }
    }

    public List<RespuestaTituladoAsignaturaUtil> getAsignaturasUtiles() { return asignaturasUtilesRel; }
    public void setAsignaturasUtiles(List<RespuestaTituladoAsignaturaUtil> v) {
        this.asignaturasUtilesRel = v;
        if (v != null && !v.isEmpty()) {
            this.asignaturasVentaja = v.stream().map(RespuestaTituladoAsignaturaUtil::getAsignatura).filter(Objects::nonNull).collect(Collectors.joining(", "));
        }
    }

    public List<RespuestaTituladoAsignaturaInutile> getAsignaturasInutiles() { return asignaturasInutilesRel; }
    public void setAsignaturasInutiles(List<RespuestaTituladoAsignaturaInutile> v) {
        this.asignaturasInutilesRel = v;
        if (v != null && !v.isEmpty()) {
            this.asignaturasPocoUtiles = v.stream().map(RespuestaTituladoAsignaturaInutile::getAsignatura).filter(Objects::nonNull).collect(Collectors.joining(", "));
        }
    }
}
