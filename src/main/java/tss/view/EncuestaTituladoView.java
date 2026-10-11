package tss.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import tss.entity.RespuestaTitulado;
import tss.service.RespuestaTituladoService;
import tss.view.componente.*;

// VISTA PRINCIPAL: ENSAMBLA LOS COMPONENTES DE CADA SECCIÓN Y GESTIONA EL ENVÍO
@Route(value = "", layout = MainLayout.class)
@PageTitle("Encuesta Titulados - Ingeniería en Sistemas UMSS")
public class EncuestaTituladoView extends VerticalLayout {

    private final SeccionDatosGenerales datosGenerales = new SeccionDatosGenerales();
    private final SeccionFormacion formacion = new SeccionFormacion();
    private final SeccionIntereses intereses = new SeccionIntereses();
    private final SeccionLaboral laboral = new SeccionLaboral();
    private final SeccionCurricular curricular = new SeccionCurricular();
    private final SeccionContacto contacto = new SeccionContacto();

    private final RespuestaTituladoService service;

    public EncuestaTituladoView(RespuestaTituladoService service) {
        this.service = service;
        setSizeFull();
        setPadding(true);
        setSpacing(true);

        add(
            new H2("Encuesta de Opinión a Titulados de Ingeniería en Sistemas"),
            new H3("Sección 0: Datos Generales"), datosGenerales,
            new H3("Formación Complementaria y Posgrado"), formacion,
            new H3("Intereses Futuros de Posgrado"), intereses,
            new H3("Trayectoria y Situación Laboral"), laboral,
            new H3("Evaluación de la Calidad Curricular"), curricular,
            new H3("Red de Graduados y Datos de Contacto"), contacto,
            buildBotonEnviar()
        );
    }

    // RECOPILA DATOS DE TODOS LOS COMPONENTES, CONSTRUYE LA ENTIDAD Y LA PERSISTE
    private Button buildBotonEnviar() {
        Button btn = new Button("Enviar Encuesta", e -> {
            try {
                RespuestaTitulado respuesta = new RespuestaTitulado();

                respuesta.setEdad(datosGenerales.getEdad());
                respuesta.setGenero(datosGenerales.getGenero());

                respuesta.setRealizoPosgrado(formacion.getRealizoPosgrado());
                respuesta.setNivelPosgrado(formacion.getNivelPosgrado());
                respuesta.setInstitucionPosgrado(formacion.getInstitucionPosgrado());
                respuesta.setFinanciamientoPosgrado(formacion.getFinanciamientoPosgrado());
                respuesta.setNombrePrograma(formacion.getNombrePrograma());

                respuesta.setInteresadoPosgrado(intereses.getInteresadoPosgrado());
                respuesta.setAreaInteresPosgrado(intereses.getAreaInteresPosgrado());
                respuesta.setInstitucionInteresPosgrado(intereses.getInstitucionInteresPosgrado());
                respuesta.setOpinionPosgradoFcyt(intereses.getOpinionPosgradoFcyt());

                respuesta.setAniosVidaProfesional(laboral.getAniosVidaProfesional());
                respuesta.setAniosDesempleado(laboral.getAniosDesempleado());
                respuesta.setSituacionLaboral(laboral.getSituacionLaboral());
                respuesta.setPrimerEmpleo(laboral.getPrimerEmpleo());
                respuesta.setTiempoPrimerEmpleo(laboral.getTiempoPrimerEmpleo());
                respuesta.setCantidadEmpleos(laboral.getCantidadEmpleos());

                respuesta.setSatisfaccionFormacion(curricular.getSatisfaccionFormacion());
                respuesta.setConcordanciaMercado(curricular.getConcordanciaMercado());
                respuesta.setAspectosUtiles(curricular.getAspectosUtiles());
                respuesta.setAspectosMejorar(curricular.getAspectosMejorar());
                respuesta.setAsignaturasUtiles(curricular.getAsignaturasUtiles());
                respuesta.setAsignaturasInutiles(curricular.getAsignaturasInutiles());

                respuesta.setInteresadoRedGraduados(contacto.getInteresadoRed());
                respuesta.setEmail(contacto.getEmail());
                respuesta.setTelefono(contacto.getTelefono());
                respuesta.setLinkedin(contacto.getLinkedin());

                service.guardar(respuesta);

                Notification.show("¡Encuesta enviada exitosamente! Gracias por su colaboración.",
                        4000, Notification.Position.MIDDLE)
                        .addThemeVariants(NotificationVariant.LUMO_SUCCESS);

                limpiarFormulario();
            } catch (Exception ex) {
                Notification.show("Error al enviar la encuesta. Intente nuevamente.",
                        4000, Notification.Position.MIDDLE)
                        .addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });
        btn.getStyle().set("margin-top", "20px");
        return btn;
    }

    // LIMPIA TODOS LOS COMPONENTES DESPUÉS DE UN ENVÍO EXITOSO
    private void limpiarFormulario() {
        datosGenerales.limpiar();
        formacion.limpiar();
        intereses.limpiar();
        laboral.limpiar();
        curricular.limpiar();
        contacto.limpiar();
    }
}
