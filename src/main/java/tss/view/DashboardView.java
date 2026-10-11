package tss.view;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import tss.entity.RespuestaTitulado;
import tss.service.ReporteService;
import tss.service.RespuestaTituladoService;
import tss.view.componente.dashboard.GraficosFactory;
import tss.view.componente.dashboard.PanelExportacion;
import tss.view.componente.dashboard.TablaRespuestas;

import java.util.List;

// DASHBOARD HU5: ENSAMBLA LOS COMPONENTES DE REPORTE Y VISUALIZACIÓN
@Route(value = "dashboard", layout = MainLayout.class)
@PageTitle("Dashboard - Análisis de Mercado UMSS")
public class DashboardView extends VerticalLayout {

    public DashboardView(RespuestaTituladoService tituladoService, ReporteService reporteService) {
        setPadding(true);
        setSpacing(true);
        setWidthFull();

        List<RespuestaTitulado> datos = tituladoService.listarTodos();
        GraficosFactory graficos = new GraficosFactory(reporteService);

        // SECCIÓN SUPERIOR: TÍTULO Y EXPORTACIÓN
        add(
            new H2("Dashboard — Análisis de Mercado Ingeniería en Sistemas"),
            new PanelExportacion(reporteService),
            new H3("Respuestas Recibidas (" + datos.size() + ")"),
            new TablaRespuestas(datos)
        );

        // GRID RESPONSIVE: 2 COLUMNAS EN DESKTOP, 1 EN MÓVIL
        Div gridBarras = gridResponsivo();
        gridBarras.add(
            seccion("Satisfacción con la Formación",
                graficos.barras(datos, RespuestaTitulado::getSatisfaccionFormacion, "Satisfacción")),
            seccion("Área de Interés de Posgrado",
                graficos.barras(datos, RespuestaTitulado::getAreaInteresPosgrado, "Área de Interés")),
            seccion("Fuente de Financiamiento",
                graficos.barras(datos, RespuestaTitulado::getFinanciamientoPosgrado, "Financiamiento")),
            seccion("Concordancia Formación vs. Mercado",
                graficos.barras(datos, RespuestaTitulado::getConcordanciaMercado, "Concordancia"))
        );
        add(gridBarras);

        // GRÁFICOS DE ANCHO COMPLETO
        add(
            new H3("Radar: Satisfacción vs. Concordancia"),
            graficos.radar(datos),
            new H3("Mapa de Calor: Satisfacción vs. Área de Interés"),
            graficos.mapaCalor(datos)
        );
    }

    // CONTENEDOR CON CSS GRID RESPONSIVE
    private Div gridResponsivo() {
        Div div = new Div();
        div.getStyle()
            .set("display", "grid")
            .set("grid-template-columns", "repeat(auto-fit, minmax(320px, 1fr))")
            .set("gap", "1rem")
            .set("width", "100%");
        return div;
    }

    // TARJETA CON TÍTULO Y GRÁFICO
    private Div seccion(String titulo, com.vaadin.flow.component.Component grafico) {
        Div card = new Div();
        card.getStyle()
            .set("background", "#f8f9fa")
            .set("border-radius", "8px")
            .set("padding", "1rem")
            .set("box-shadow", "0 1px 4px rgba(0,0,0,0.1)");
        H3 h = new H3(titulo);
        h.getStyle().set("margin", "0 0 0.5rem 0").set("font-size", "1rem").set("color", "#003366");
        card.add(h, grafico);
        return card;
    }
}
