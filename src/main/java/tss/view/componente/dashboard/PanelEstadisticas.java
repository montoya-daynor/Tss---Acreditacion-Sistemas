package tss.view.componente.dashboard;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import tss.service.MetricaEstadistica;

import java.util.List;
import java.util.Locale;

// PANEL DE ANÁLISIS ESTADÍSTICO DESCRIPTIVO: MEDIA, MEDIANA Y DESVIACIÓN ESTÁNDAR
public class PanelEstadisticas extends VerticalLayout {

    private static final Locale LOC_ES = Locale.forLanguageTag("es-ES");

    public PanelEstadisticas(List<MetricaEstadistica> metricas, com.vaadin.flow.component.Component graficoEstadistico) {
        setWidthFull();
        setPadding(false);
        setSpacing(true);

        // ENCABEZADO DE LA SECCIÓN
        H3 titulo = new H3("Análisis Estadístico Descriptivo");
        titulo.getStyle()
                .set("color", "#003366")
                .set("margin", "1rem 0 0.25rem 0");

        Paragraph descripcion = new Paragraph(
                "Métricas de tendencia central (Media, Mediana) y dispersión (Desviación Estándar) " +
                "calculadas a partir de las respuestas cuantitativas y escalas de los titulados.");
        descripcion.getStyle()
                .set("color", "#555555")
                .set("font-size", "0.9rem")
                .set("margin-bottom", "1rem");

        // CONTENEDOR DE TARJETAS KPI RESUMEN (RESPONSIVE GRID)
        Div gridTarjetas = new Div();
        gridTarjetas.getStyle()
                .set("display", "grid")
                .set("grid-template-columns", "repeat(auto-fit, minmax(240px, 1fr))")
                .set("gap", "1rem")
                .set("width", "100%")
                .set("margin-bottom", "1.25rem");

        for (MetricaEstadistica m : metricas) {
            gridTarjetas.add(crearTarjetaMetrica(m));
        }

        // TABLA ESTRUCTURADA DE INDICADORES
        Grid<MetricaEstadistica> tabla = new Grid<>(MetricaEstadistica.class, false);
        tabla.addColumn(MetricaEstadistica::variable).setHeader("Variable Evaluada").setFlexGrow(2);
        tabla.addColumn(m -> String.format(LOC_ES, "%.2f", m.media())).setHeader("Media (x̄)").setAutoWidth(true);
        tabla.addColumn(m -> String.format(LOC_ES, "%.2f", m.mediana())).setHeader("Mediana (Me)").setAutoWidth(true);
        tabla.addColumn(m -> String.format(LOC_ES, "± %.2f", m.desviacionEstandar())).setHeader("Desv. Estándar (s)").setAutoWidth(true);
        tabla.addColumn(m -> String.format(LOC_ES, "%.1f", m.min())).setHeader("Mínimo").setAutoWidth(true);
        tabla.addColumn(m -> String.format(LOC_ES, "%.1f", m.max())).setHeader("Máximo").setAutoWidth(true);
        tabla.addColumn(MetricaEstadistica::muestra).setHeader("Muestra (N)").setAutoWidth(true);

        tabla.setItems(metricas);
        tabla.setAllRowsVisible(true);
        tabla.setWidthFull();

        Div contenedorTabla = new Div(tabla);
        contenedorTabla.getStyle()
                .set("background", "#ffffff")
                .set("border", "1px solid #e2e8f0")
                .set("border-radius", "8px")
                .set("padding", "0.5rem")
                .set("box-shadow", "0 1px 3px rgba(0,0,0,0.06)")
                .set("width", "100%");

        add(titulo, descripcion, gridTarjetas, contenedorTabla);

        if (graficoEstadistico != null) {
            Div contenedorGrafico = new Div();
            contenedorGrafico.getStyle()
                    .set("background", "#ffffff")
                    .set("border", "1px solid #e2e8f0")
                    .set("border-radius", "8px")
                    .set("padding", "1rem")
                    .set("margin-top", "1rem")
                    .set("box-shadow", "0 1px 3px rgba(0,0,0,0.06)")
                    .set("width", "100%");

            H3 tituloGrafico = new H3("Comparativa de Tendencia Central y Dispersión");
            tituloGrafico.getStyle()
                    .set("margin", "0 0 0.5rem 0")
                    .set("font-size", "1rem")
                    .set("color", "#003366");

            contenedorGrafico.add(tituloGrafico, graficoEstadistico);
            add(contenedorGrafico);
        }
    }

    // TARJETA INDIVIDUAL DE MÉTRICA CON DISEÑO MODERNO
    private Div crearTarjetaMetrica(MetricaEstadistica m) {
        Div card = new Div();
        card.getStyle()
                .set("background", "#ffffff")
                .set("border-radius", "8px")
                .set("padding", "1rem")
                .set("border", "1px solid #e2e8f0")
                .set("border-top", "4px solid #003366")
                .set("box-shadow", "0 2px 4px rgba(0,0,0,0.05)")
                .set("display", "flex")
                .set("flex-direction", "column")
                .set("gap", "0.5rem");

        Span titulo = new Span(m.variable());
        titulo.getStyle()
                .set("font-weight", "600")
                .set("font-size", "0.95rem")
                .set("color", "#1e293b");

        // VALOR PRINCIPAL (MEDIA)
        Div bloqueMedia = new Div();
        Span lblMedia = new Span("Media: ");
        lblMedia.getStyle().set("color", "#64748b").set("font-size", "0.85rem");

        Span valMedia = new Span(String.format(LOC_ES, "%.2f", m.media()));
        valMedia.getStyle()
                .set("font-size", "1.6rem")
                .set("font-weight", "700")
                .set("color", "#003366");

        bloqueMedia.add(lblMedia, valMedia);

        // DETALLE DE MEDIANA Y DESVIACIÓN ESTÁNDAR
        Div filaDetalles = new Div();
        filaDetalles.getStyle()
                .set("display", "flex")
                .set("justify-content", "space-between")
                .set("border-top", "1px solid #f1f5f9")
                .set("padding-top", "0.5rem")
                .set("font-size", "0.85rem")
                .set("color", "#475569");

        Span spanMediana = new Span("Mediana: " + String.format(LOC_ES, "%.2f", m.mediana()));
        spanMediana.getStyle().set("font-weight", "500");

        Span spanDesv = new Span("Desv: ±" + String.format(LOC_ES, "%.2f", m.desviacionEstandar()));
        spanDesv.getStyle().set("font-weight", "500").set("color", "#d97706");

        filaDetalles.add(spanMediana, spanDesv);

        // PIE DE TARJETA: MUESTRA Y RANGO
        Div filaPie = new Div();
        filaPie.getStyle()
                .set("display", "flex")
                .set("justify-content", "space-between")
                .set("font-size", "0.75rem")
                .set("color", "#94a3b8");

        Span rango = new Span("Rango: [" + String.format(LOC_ES, "%.1f", m.min()) + " - " + String.format(LOC_ES, "%.1f", m.max()) + "]");
        Span n = new Span("N = " + m.muestra());

        filaPie.add(rango, n);
        card.add(titulo, bloqueMedia, filaDetalles, filaPie);
        return card;
    }
}
