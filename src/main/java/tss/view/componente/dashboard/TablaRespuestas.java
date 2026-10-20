package tss.view.componente.dashboard;

import com.vaadin.flow.component.grid.Grid;
import tss.entity.RespuestaTitulado;

import java.util.List;

public class TablaRespuestas extends Grid<RespuestaTitulado> {

    public TablaRespuestas(List<RespuestaTitulado> datos) {
        super(RespuestaTitulado.class, false);

        // Identificación
        addColumn(RespuestaTitulado::getId).setHeader("ID").setWidth("60px").setFlexGrow(0);
        addColumn(RespuestaTitulado::getHoraInicio).setHeader("Hora Inicio").setAutoWidth(true);
        addColumn(RespuestaTitulado::getHoraFin).setHeader("Hora Fin").setAutoWidth(true);
        addColumn(RespuestaTitulado::getCorreoRegistro).setHeader("Correo").setAutoWidth(true);
        addColumn(RespuestaTitulado::getNombre).setHeader("Nombre").setAutoWidth(true);
        addColumn(RespuestaTitulado::getHoraPublicacion).setHeader("Hora Publicación").setAutoWidth(true);

        // Datos personales
        addColumn(RespuestaTitulado::getEdad).setHeader("Edad").setAutoWidth(true);
        addColumn(RespuestaTitulado::getGenero).setHeader("Género").setAutoWidth(true);

        // Formación complementaria
        addColumn(RespuestaTitulado::getTieneFormacionComplementaria).setHeader("¿Realizó Posgrado?").setAutoWidth(true);
        addColumn(RespuestaTitulado::getNivelProgramaCursado).setHeader("Nivel Posgrado").setAutoWidth(true);
        addColumn(RespuestaTitulado::getInstitucionCursada).setHeader("Institución Cursada").setAutoWidth(true);
        addColumn(RespuestaTitulado::getFinanciamientoPrograma).setHeader("Financiamiento Posgrado").setAutoWidth(true);
        addColumn(RespuestaTitulado::getNombrePrograma).setHeader("Nombre Programa").setAutoWidth(true);

        // Interés posgrado
        addColumn(RespuestaTitulado::getInteresPosgrado).setHeader("¿Interés Posgrado?").setAutoWidth(true);
        addColumn(RespuestaTitulado::getAreaInteresPosgrado).setHeader("Área Interés").setAutoWidth(true);
        addColumn(RespuestaTitulado::getInstitucionPreferida).setHeader("Institución Preferida").setAutoWidth(true);
        addColumn(RespuestaTitulado::getOpinionPosgradoFcyt).setHeader("Opinión Posgrado FCyT").setAutoWidth(true);

        // Trayectoria laboral
        addColumn(RespuestaTitulado::getAniosVidaProfesionalDecimalCalculado).setHeader("Años Vida Prof.").setAutoWidth(true);
        addColumn(RespuestaTitulado::getAniosDesempleadoDecimalCalculado).setHeader("Años Desempleo").setAutoWidth(true);
        addColumn(RespuestaTitulado::getSituacionLaboral).setHeader("Situación Laboral").setAutoWidth(true);
        addColumn(RespuestaTitulado::getEmpresa).setHeader("Empresa").setAutoWidth(true);
        addColumn(RespuestaTitulado::getRubro).setHeader("Rubro").setAutoWidth(true);
        addColumn(RespuestaTitulado::getRubroOtro).setHeader("Otro Rubro").setAutoWidth(true);
        addColumn(RespuestaTitulado::getSector).setHeader("Sector").setAutoWidth(true);
        addColumn(RespuestaTitulado::getDepartamento).setHeader("Departamento").setAutoWidth(true);
        addColumn(RespuestaTitulado::getAreaOrganizacion).setHeader("Área Organización").setAutoWidth(true);
        addColumn(RespuestaTitulado::getAreaOtra).setHeader("Otra Área").setAutoWidth(true);
        addColumn(RespuestaTitulado::getCargo).setHeader("Cargo").setAutoWidth(true);
        addColumn(RespuestaTitulado::getCargoOtro).setHeader("Otro Cargo").setAutoWidth(true);
        addColumn(RespuestaTitulado::getPertinenciaLabores).setHeader("Pertinencia Labores").setAutoWidth(true);
        addColumn(RespuestaTitulado::getAntiguedadTrabajoActual).setHeader("Antigüedad").setAutoWidth(true);
        addColumn(RespuestaTitulado::getMedioObtencionTrabajo).setHeader("Medio Obtención").setAutoWidth(true);
        addColumn(RespuestaTitulado::getRangoRemuneracion).setHeader("Remuneración").setAutoWidth(true);

        // Emprendimiento
        addColumn(RespuestaTitulado::getOrigenEmprendimiento).setHeader("Origen Emprendimiento").setAutoWidth(true);
        addColumn(RespuestaTitulado::getTipoEntregable).setHeader("Tipo Entregable").setAutoWidth(true);
        addColumn(RespuestaTitulado::getFinanciamientoNegocio).setHeader("Financiamiento Negocio").setAutoWidth(true);
        addColumn(RespuestaTitulado::getSatisfaccionNegocio).setHeader("Satisfacción Negocio").setAutoWidth(true);
        addColumn(RespuestaTitulado::getImportanciaFormacionNegocio).setHeader("Importancia Formación Negocio").setAutoWidth(true);

        // Trayectoria empleos
        addColumn(RespuestaTitulado::getEsPrimerEmpleo).setHeader("¿Primer Empleo?").setAutoWidth(true);
        addColumn(RespuestaTitulado::getRazonNoTrabaja).setHeader("Razón No Trabaja").setAutoWidth(true);
        addColumn(RespuestaTitulado::getTrabajoPrevio).setHeader("Trabajo Previo").setAutoWidth(true);
        addColumn(RespuestaTitulado::getTiempoPrimerEmpleo).setHeader("Tiempo Primer Empleo").setAutoWidth(true);
        addColumn(RespuestaTitulado::getCantidadEmpleos).setHeader("Cantidad Empleos").setAutoWidth(true);

        // Evaluación curricular
        addColumn(RespuestaTitulado::getSatisfaccionFormacion).setHeader("Satisfacción Formación").setAutoWidth(true);
        addColumn(RespuestaTitulado::getConcordanciaFormacion).setHeader("Concordancia Formación").setAutoWidth(true);
        addColumn(RespuestaTitulado::getAspectosUtilesTexto).setHeader("Aspectos Útiles").setAutoWidth(true);
        addColumn(RespuestaTitulado::getAspectosMejorarTexto).setHeader("Aspectos a Mejorar").setAutoWidth(true);
        addColumn(RespuestaTitulado::getAsignaturasVentaja).setHeader("Asignaturas Ventaja").setAutoWidth(true);
        addColumn(RespuestaTitulado::getAsignaturaVentajaOtra).setHeader("Otra Asignatura Ventaja").setAutoWidth(true);
        addColumn(RespuestaTitulado::getAsignaturasPocoUtiles).setHeader("Asignaturas Poco Útiles").setAutoWidth(true);
        addColumn(RespuestaTitulado::getAsignaturaPocoUtilOtra).setHeader("Otra Asignatura Poco Útil").setAutoWidth(true);

        // Red de contactos
        addColumn(RespuestaTitulado::getInteresRedContactos).setHeader("¿Interés Red?").setAutoWidth(true);
        addColumn(RespuestaTitulado::getEmailContacto).setHeader("Email Contacto").setAutoWidth(true);
        addColumn(RespuestaTitulado::getTelefonoContacto).setHeader("Teléfono").setAutoWidth(true);
        addColumn(RespuestaTitulado::getLinkedin).setHeader("LinkedIn").setAutoWidth(true);

        setItems(datos);
        setHeight("500px");
        setWidthFull();
    }
}
