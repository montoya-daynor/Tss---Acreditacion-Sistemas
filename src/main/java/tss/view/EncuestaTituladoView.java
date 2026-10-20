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
import tss.entity.RespuestaTitulado;
import tss.service.CsvTituladosImportService;
import tss.service.ExcelTituladosImportService;
import tss.service.RespuestaTituladoService;
import tss.view.componente.dashboard.TablaRespuestas;

import java.util.List;

@Route(value = "", layout = MainLayout.class)
@PageTitle("Encuesta Titulados - Ingeniería en Sistemas UMSS")
public class EncuestaTituladoView extends VerticalLayout {

    public EncuestaTituladoView(RespuestaTituladoService service,
                                CsvTituladosImportService csvImportService,
                                ExcelTituladosImportService excelImportService) {
        setPadding(true);
        setSpacing(true);
        setWidthFull();

        List<RespuestaTitulado> datos = service.listarTodos();

        HorizontalLayout panelAcciones = new HorizontalLayout();
        panelAcciones.setAlignItems(FlexComponent.Alignment.CENTER);
        panelAcciones.setSpacing(true);

        MemoryBuffer buffer = new MemoryBuffer();
        Upload upload = new Upload(buffer);
        upload.setAcceptedFileTypes(".csv", "text/csv", ".xlsx",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        upload.setMaxFileSize(25 * 1024 * 1024);
        upload.setMaxFiles(1);
        upload.setDropAllowed(false);

        Button btnCargar = new Button("⬆ Cargar Excel / CSV Titulados", new Icon(VaadinIcon.UPLOAD));
        btnCargar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        upload.setUploadButton(btnCargar);

        upload.addSucceededListener(event -> {
            try {
                boolean esExcel = event.getFileName().toLowerCase().endsWith(".xlsx");
                boolean exito;
                String mensaje;
                if (esExcel) {
                    var result = excelImportService.importar(buffer.getInputStream());
                    exito = result.isExito();
                    mensaje = result.getMensaje();
                } else {
                    var result = csvImportService.importar(buffer.getInputStream());
                    exito = result.isExito();
                    mensaje = result.getMensaje();
                }
                if (exito) {
                    Notification notif = Notification.show(mensaje, 4000, Notification.Position.TOP_CENTER);
                    notif.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                    UI ui = UI.getCurrent();
                    if (ui != null) ui.getPage().reload();
                } else {
                    Notification notif = Notification.show("Error: " + mensaje, 6000, Notification.Position.TOP_CENTER);
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
            new H2("Encuesta a Titulados — Ingeniería en Sistemas UMSS"),
            panelAcciones,
            new H3("Respuestas Recibidas (" + datos.size() + ")"),
            new TablaRespuestas(datos)
        );
    }
}
