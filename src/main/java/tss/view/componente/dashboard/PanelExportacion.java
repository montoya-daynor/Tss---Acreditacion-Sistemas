package tss.view.componente.dashboard;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.server.streams.DownloadHandler;
import com.vaadin.flow.server.streams.DownloadResponse;
import tss.service.CsvTituladosImportService;
import tss.service.ReporteService;

// BOTONES DE DESCARGA EXCEL/CSV Y BOTÓN DE CARGA CSV
public class PanelExportacion extends HorizontalLayout {

    public PanelExportacion(ReporteService reporteService) {
        this(reporteService, null);
    }

    public PanelExportacion(ReporteService reporteService, CsvTituladosImportService importService) {
        setAlignItems(FlexComponent.Alignment.CENTER);
        setSpacing(true);

        DownloadHandler excelHandler = DownloadHandler.fromInputStream(
                e -> new DownloadResponse(reporteService.exportarExcel(), "reporte.xlsx",
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", -1),
                "reporte.xlsx");
        DownloadHandler csvHandler = DownloadHandler.fromInputStream(
                e -> new DownloadResponse(reporteService.exportarCsv(), "reporte.csv", "text/csv", -1),
                "reporte.csv");

        Anchor anchorExcel = new Anchor(excelHandler, "⬇ Exportar Excel");
        Anchor anchorCsv = new Anchor(csvHandler, "⬇ Exportar CSV");

        // BOTÓN ÚNICO DE CARGA CSV
        if (importService != null) {
            MemoryBuffer buffer = new MemoryBuffer();
            Upload upload = new Upload(buffer);
            upload.setAcceptedFileTypes(".csv", "text/csv");
            upload.setMaxFileSize(25 * 1024 * 1024);
            upload.setMaxFiles(1);
            upload.setDropAllowed(false);

            Button btnCargar = new Button("⬆ Cargar CSV", new Icon(VaadinIcon.UPLOAD));
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

            add(upload);
        }

        add(anchorExcel, anchorCsv);
    }
}
