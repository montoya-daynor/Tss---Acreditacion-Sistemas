package tss.view.componente.dashboard;

import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.server.streams.DownloadHandler;
import com.vaadin.flow.server.streams.DownloadResponse;
import tss.service.ReporteService;

// BOTONES DE DESCARGA EXCEL Y CSV
public class PanelExportacion extends HorizontalLayout {

    public PanelExportacion(ReporteService reporteService) {
        DownloadHandler excelHandler = DownloadHandler.fromInputStream(
                e -> new DownloadResponse(reporteService.exportarExcel(), "reporte.xlsx",
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", -1),
                "reporte.xlsx");
        DownloadHandler csvHandler = DownloadHandler.fromInputStream(
                e -> new DownloadResponse(reporteService.exportarCsv(), "reporte.csv", "text/csv", -1),
                "reporte.csv");

        add(new Anchor(excelHandler, "⬇ Exportar Excel"),
            new Anchor(csvHandler,   "⬇ Exportar CSV"));
    }
}
