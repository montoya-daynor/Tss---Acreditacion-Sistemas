package tss.view;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import tss.entity.RespuestaEmpleador;
import tss.service.CsvEmpleadoresImportService;
import tss.service.RespuestaEmpleadorService;
import tss.view.componente.dashboard.TablaEmpleadores;

import java.util.List;

// VISTA ENCUESTA EMPLEADORES — CARGA CSV Y VISUALIZACIÓN DE DATOS
@Route(value = "empleadores", layout = MainLayout.class)
@PageTitle("Encuesta Empleadores - Ingeniería en Sistemas UMSS")
public class EncuestaEmpleadorView extends VerticalLayout {

    public EncuestaEmpleadorView(RespuestaEmpleadorService empleadorService, CsvEmpleadoresImportService importService) {
        setPadding(true);
        setSpacing(true);
        setWidthFull();

        List<RespuestaEmpleador> datos = empleadorService.listarTodos();

        // PANEL DE ACCIONES CON BOTÓN DE CARGA CSV (IGUAL AL DASHBOARD)
        HorizontalLayout panelAcciones = new HorizontalLayout();
        panelAcciones.setAlignItems(FlexComponent.Alignment.CENTER);
        panelAcciones.setSpacing(true);

        MemoryBuffer buffer = new MemoryBuffer();
        Upload upload = new Upload(buffer);
        upload.setAcceptedFileTypes(".csv", "text/csv");
        upload.setMaxFileSize(25 * 1024 * 1024);
        upload.setMaxFiles(1);
        upload.setDropAllowed(false);

        Button btnCargar = new Button("⬆ Cargar CSV Empleadores", new Icon(VaadinIcon.UPLOAD));
        btnCargar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        upload.setUploadButton(btnCargar);

        upload.addSucceededListener(event -> {
            try {
                var result = importService.importar(buffer.getInputStream());
                if (result.isExito()) {
                    Notification notif = Notification.show(result.getMensaje(), 4000, Notification.Position.TOP_CENTER);
                    notif.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                    UI ui = UI.getCurrent();
                    if (ui != null) {
                        ui.getPage().reload();
                    }
                } else {
                    Notification notif = Notification.show("Error: " + result.getMensaje(), 6000, Notification.Position.TOP_CENTER);
                    notif.addThemeVariants(NotificationVariant.LUMO_ERROR);
                    upload.clearFileList();
                }
            } catch (Exception ex) {
                Notification notif = Notification.show("Error al importar: " + ex.getMessage(), 5000, Notification.Position.TOP_CENTER);
                notif.addThemeVariants(NotificationVariant.LUMO_ERROR);
                upload.clearFileList();
            }
        });

        upload.addFailedListener(event -> {
            Notification notif = Notification.show("Error al subir archivo: " + event.getReason().getMessage(), 4000, Notification.Position.TOP_CENTER);
            notif.addThemeVariants(NotificationVariant.LUMO_ERROR);
            upload.clearFileList();
        });

        panelAcciones.add(upload);

        add(
            new H2("Encuesta a Empleadores — Ingeniería en Sistemas UMSS"),
            panelAcciones,
            new H3("Respuestas Recibidas (" + datos.size() + ")"),
            new TablaEmpleadores(datos)
        );
    }
}
