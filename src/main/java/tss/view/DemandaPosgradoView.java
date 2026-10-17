package tss.view;

import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import tss.entity.RespuestaTitulado;
import tss.service.ReporteService;
import tss.service.RespuestaTituladoService;
import tss.view.componente.dashboard.GraficosFactory;

import java.util.List;
import java.util.Map;

// DEMANDA DE POSGRADO HU2: ESTUDIO DE MERCADO PARA FORMACIÓN CONTINUA Y POSGRADO
@Route(value = "demanda-posgrado", layout = MainLayout.class)
@PageTitle("Demanda de Posgrado - Análisis de Mercado UMSS")
public class DemandaPosgradoView extends VerticalLayout {

    public DemandaPosgradoView(RespuestaTituladoService tituladoService, ReporteService reporteService) {
        setPadding(true);
        setSpacing(true);
        setWidthFull();

        List<RespuestaTitulado> datos = tituladoService.listarTodos();
        GraficosFactory graficos = new GraficosFactory(reporteService);

        Map<String, Object> kpis = reporteService.calcularResumenDemandaPosgrado(datos);
        List<Map<String, Object>> tablaArea = reporteService.calcularTablaAreaConAcumulado(datos);

        // 1. TÍTULO Y DESCRIPCIÓN INSTITUCIONAL
        add(buildEncabezado());

        // 2. CUATRO TARJETAS DE RESUMEN (KPIS RÁPIDOS)
        add(buildGridKpis(kpis));

        // 3. TABLA DE FRECUENCIAS POR ÁREA TECNOLÓGICA (CÁLCULO CENTRAL HU2)
        add(buildSeccionTabla(tablaArea, ((Number) kpis.get("total")).intValue()));

        // 4. GRÁFICOS DE BARRAS DE DISTRIBUCIÓN
        add(buildSeccionGraficos(datos, graficos));

        // 5. PÁRRAFO DE CONCLUSIÓN GENERADO AUTOMÁTICAMENTE
        add(buildSeccionConclusion(reporteService.generarConclusionHU2(datos, tablaArea)));
    }

    private Div buildEncabezado() {
        Div contenedor = new Div();
        contenedor.getStyle().set("margin-bottom", "0.5rem");

        H2 titulo = new H2("Demanda de Posgrado — Historia de Usuario 2");
        titulo.getStyle()
                .set("margin", "0 0 0.3rem 0")
                .set("color", "#003366")
                .set("font-size", "1.6rem")
                .set("font-weight", "700");

        Span subtitulo = new Span("Estudio analítico de la intención de posgrado, grado académico solicitado y áreas tecnológicas de mayor interés entre los titulados.");
        subtitulo.getStyle()
                .set("color", "#475569")
                .set("font-size", "0.95rem");

        contenedor.add(titulo, subtitulo);
        return contenedor;
    }

    private Div buildGridKpis(Map<String, Object> kpis) {
        Div grid = new Div();
        grid.getStyle()
                .set("display", "grid")
                .set("grid-template-columns", "repeat(auto-fit, minmax(240px, 1fr))")
                .set("gap", "1rem")
                .set("width", "100%")
                .set("margin-bottom", "1rem");

        int total = ((Number) kpis.get("total")).intValue();
        long interesados = ((Number) kpis.get("interesados")).longValue();
        double pctInteres = ((Number) kpis.get("pctInteres")).doubleValue();

        long conPrevios = ((Number) kpis.get("conPrevios")).longValue();
        double pctPrevios = ((Number) kpis.get("pctPrevios")).doubleValue();

        String topArea = (String) kpis.get("topArea");
        long topAreaCant = ((Number) kpis.get("topAreaCant")).longValue();
        double topAreaPct = ((Number) kpis.get("topAreaPct")).doubleValue();

        String topNivel = (String) kpis.get("topNivel");
        long topNivelCant = ((Number) kpis.get("topNivelCant")).longValue();
        double topNivelPct = ((Number) kpis.get("topNivelPct")).doubleValue();

        grid.add(crearCardKpi(
                "Interés en Posgrado",
                String.format("%.1f%%", pctInteres),
                String.format("%d de %d encuestados", interesados, total),
                "Manifiestan intención de estudiar un posgrado"
        ));

        grid.add(crearCardKpi(
                "Con Estudios Previos",
                String.format("%.1f%%", pctPrevios),
                String.format("%d de %d encuestados", conPrevios, total),
                "Cursaron diplomado, maestría o posgrado antes"
        ));

        grid.add(crearCardKpi(
                "Área Más Demandada (Top 1)",
                topArea,
                String.format("%d votos | %.1f%% del total", topAreaCant, topAreaPct),
                "Especialidad con mayor cantidad de preferencias"
        ));

        grid.add(crearCardKpi(
                "Nivel Más Buscado",
                topNivel,
                String.format("%d votos | %.1f%% del total", topNivelCant, topNivelPct),
                "Grado académico con mayor interés formativo"
        ));

        return grid;
    }

    private Div crearCardKpi(String titulo, String valorPrincipal, String valorSecundario, String descripcion) {
        Div card = new Div();
        card.getStyle()
                .set("background", "#f8f9fa")
                .set("border", "1px solid #e2e8f0")
                .set("border-radius", "8px")
                .set("padding", "1.2rem")
                .set("box-shadow", "0 1px 4px rgba(0,0,0,0.06)")
                .set("display", "flex")
                .set("flex-direction", "column")
                .set("gap", "0.35rem");

        Span tit = new Span(titulo);
        tit.getStyle()
                .set("font-size", "0.82rem")
                .set("font-weight", "600")
                .set("color", "#475569")
                .set("text-transform", "uppercase")
                .set("letter-spacing", "0.5px");

        Span val = new Span(valorPrincipal);
        val.getStyle()
                .set("font-size", "1.7rem")
                .set("font-weight", "800")
                .set("color", "#003366")
                .set("line-height", "1.1");

        Span sub = new Span(valorSecundario);
        sub.getStyle()
                .set("font-size", "0.85rem")
                .set("font-weight", "600")
                .set("color", "#1e293b");

        Span desc = new Span(descripcion);
        desc.getStyle()
                .set("font-size", "0.75rem")
                .set("color", "#64748b");

        card.add(tit, val, sub, desc);
        return card;
    }

    private Div buildSeccionTabla(List<Map<String, Object>> filas, int total) {
        Div contenedor = new Div();
        contenedor.getStyle()
                .set("background", "#f8f9fa")
                .set("border", "1px solid #e2e8f0")
                .set("border-radius", "8px")
                .set("padding", "1.25rem")
                .set("box-shadow", "0 1px 4px rgba(0,0,0,0.06)")
                .set("width", "100%")
                .set("margin-bottom", "1rem");

        H3 titulo = new H3("Tabla de Frecuencias por Área Tecnológica");
        titulo.getStyle()
                .set("margin", "0 0 0.25rem 0")
                .set("color", "#003366")
                .set("font-size", "1.15rem");

        Span nota = new Span("Distribución ordenada de mayor a menor demanda. Se resalta la concentración crítica superior al 70% (Principio de Pareto).");
        nota.getStyle()
                .set("display", "block")
                .set("color", "#64748b")
                .set("font-size", "0.85rem")
                .set("margin-bottom", "1rem");

        contenedor.add(titulo, nota);

        // TABLA ESTRUCTURADA
        Div tabla = new Div();
        tabla.getStyle()
                .set("border", "1px solid #cbd5e1")
                .set("border-radius", "6px")
                .set("overflow", "hidden");

        // ENCABEZADO DE LA TABLA
        Div header = new Div();
        header.getStyle()
                .set("display", "grid")
                .set("grid-template-columns", "1.8fr 110px 180px 130px 150px")
                .set("background", "#e9ecef")
                .set("padding", "0.75rem 1rem")
                .set("gap", "0.75rem")
                .set("border-bottom", "2px solid #cbd5e1")
                .set("font-weight", "700")
                .set("font-size", "0.85rem")
                .set("color", "#003366");

        header.add(
                new Span("Área Tecnológica"),
                centrarSpan(new Span("Frecuencia (fi)")),
                new Span("Porcentaje Relativo (%)"),
                centrarSpan(new Span("% Acumulado")),
                centrarSpan(new Span("Concentración"))
        );
        tabla.add(header);

        // FILAS DE DATOS
        long sumaFi = 0;
        int index = 0;
        for (Map<String, Object> fila : filas) {
            String area = (String) fila.get("categoria");
            long fi = ((Number) fila.get("cantidad")).longValue();
            double pct = ((Number) fila.get("porcentaje")).doubleValue();
            double pctAcum = ((Number) fila.get("porcentajeAcumulado")).doubleValue();
            boolean esFoco = Boolean.TRUE.equals(fila.get("esFoco70"));

            sumaFi += fi;

            String fondoFila = esFoco ? "#f1f5f9" : (index % 2 == 0 ? "#ffffff" : "#f8fafc");

            Div row = new Div();
            row.getStyle()
                    .set("display", "grid")
                    .set("grid-template-columns", "1.8fr 110px 180px 130px 150px")
                    .set("padding", "0.65rem 1rem")
                    .set("gap", "0.75rem")
                    .set("background", fondoFila)
                    .set("border-bottom", "1px solid #e2e8f0")
                    .set("align-items", "center")
                    .set("font-size", "0.88rem");

            // 1. Área
            Span spanArea = new Span(area != null && !area.isBlank() ? area : "(Sin especificar)");
            spanArea.getStyle()
                    .set("font-weight", esFoco ? "700" : "500")
                    .set("color", esFoco ? "#003366" : "#1e293b");

            // 2. Frecuencia absoluta (fi)
            Span spanFi = new Span(String.valueOf(fi));
            spanFi.getStyle()
                    .set("font-weight", "700")
                    .set("text-align", "center")
                    .set("color", "#003366");

            // 3. Barra visual + porcentaje
            Div barraCol = new Div();
            barraCol.getStyle().set("display", "flex").set("align-items", "center").set("gap", "0.5rem");

            Div track = new Div();
            track.getStyle()
                    .set("flex", "1")
                    .set("height", "8px")
                    .set("background", "#e2e8f0")
                    .set("border-radius", "4px")
                    .set("overflow", "hidden");

            Div fill = new Div();
            fill.getStyle()
                    .set("width", Math.min(pct, 100.0) + "%")
                    .set("height", "100%")
                    .set("background", esFoco ? "#003366" : "#64748b")
                    .set("border-radius", "4px");
            track.add(fill);

            Span spanPct = new Span(String.format("%.1f%%", pct));
            spanPct.getStyle()
                    .set("font-weight", esFoco ? "700" : "600")
                    .set("width", "45px")
                    .set("color", esFoco ? "#003366" : "#334155");

            barraCol.add(track, spanPct);

            // 4. Porcentaje acumulado
            Span spanAcum = new Span(String.format("%.1f%%", pctAcum));
            spanAcum.getStyle()
                    .set("font-weight", "700")
                    .set("text-align", "center")
                    .set("color", esFoco ? "#003366" : "#475569");

            // 5. Badge o etiqueta de concentración
            Span spanPrioridad = new Span(esFoco ? "Foco Crítico (≥70%)" : "Complementario");
            spanPrioridad.getStyle()
                    .set("text-align", "center")
                    .set("padding", "0.2rem 0.5rem")
                    .set("border-radius", "4px")
                    .set("font-size", "0.75rem")
                    .set("font-weight", "600")
                    .set("display", "inline-block")
                    .set("background", esFoco ? "#e2e8f0" : "#f1f5f9")
                    .set("color", esFoco ? "#003366" : "#64748b")
                    .set("border", esFoco ? "1px solid #cbd5e1" : "1px solid #e2e8f0");

            row.add(spanArea, spanFi, barraCol, spanAcum, spanPrioridad);
            tabla.add(row);
            index++;
        }

        // FILA DE TOTAL
        Div filaTotal = new Div();
        filaTotal.getStyle()
                .set("display", "grid")
                .set("grid-template-columns", "1.8fr 110px 180px 130px 150px")
                .set("padding", "0.75rem 1rem")
                .set("gap", "0.75rem")
                .set("background", "#e9ecef")
                .set("border-top", "2px solid #cbd5e1")
                .set("font-weight", "800")
                .set("font-size", "0.9rem")
                .set("color", "#003366");

        Span titTotal = new Span("Total Respuestas Registradas");
        Span valTotalFi = new Span(String.valueOf(sumaFi));
        valTotalFi.getStyle().set("text-align", "center");
        Span valTotalPct = new Span("100.0%");
        valTotalPct.getStyle().set("text-align", "left").set("padding-left", "0.5rem");
        Span valTotalAcum = new Span("100.0%");
        valTotalAcum.getStyle().set("text-align", "center");
        Span vacio = new Span("-");
        vacio.getStyle().set("text-align", "center");

        filaTotal.add(titTotal, valTotalFi, valTotalPct, valTotalAcum, vacio);
        tabla.add(filaTotal);

        contenedor.add(tabla);
        return contenedor;
    }

    private Div buildSeccionGraficos(List<RespuestaTitulado> datos, GraficosFactory graficos) {
        Div grid = new Div();
        grid.getStyle()
                .set("display", "grid")
                .set("grid-template-columns", "repeat(auto-fit, minmax(320px, 1fr))")
                .set("gap", "1rem")
                .set("width", "100%")
                .set("margin-bottom", "1rem");

        grid.add(
                crearCardGrafico("Distribución por Nivel de Posgrado",
                        graficos.barras(datos, RespuestaTitulado::getNivelPosgrado, "Nivel de Posgrado")),
                crearCardGrafico("Institución Educativa Preferida",
                        graficos.barras(datos, RespuestaTitulado::getInstitucionInteresPosgrado, "Institución de Interés"))
        );

        return grid;
    }

    private Div crearCardGrafico(String titulo, com.vaadin.flow.component.Component grafico) {
        Div card = new Div();
        card.getStyle()
                .set("background", "#f8f9fa")
                .set("border", "1px solid #e2e8f0")
                .set("border-radius", "8px")
                .set("padding", "1rem")
                .set("box-shadow", "0 1px 4px rgba(0,0,0,0.06)");

        H3 h = new H3(titulo);
        h.getStyle()
                .set("margin", "0 0 0.5rem 0")
                .set("font-size", "1rem")
                .set("color", "#003366")
                .set("font-weight", "700");

        card.add(h, grafico);
        return card;
    }

    private Div buildSeccionConclusion(String conclusion) {
        Div card = new Div();
        card.getStyle()
                .set("background", "#f8f9fa")
                .set("border", "1px solid #e2e8f0")
                .set("border-left", "5px solid #003366")
                .set("border-radius", "6px")
                .set("padding", "1.25rem 1.5rem")
                .set("box-shadow", "0 1px 4px rgba(0,0,0,0.06)")
                .set("width", "100%");

        H3 tit = new H3("Diagnóstico y Conclusiones del Estudio de Demanda");
        tit.getStyle()
                .set("margin", "0 0 0.5rem 0")
                .set("color", "#003366")
                .set("font-size", "1.1rem")
                .set("font-weight", "700");

        Paragraph p = new Paragraph(conclusion);
        p.getStyle()
                .set("margin", "0")
                .set("color", "#1e293b")
                .set("font-size", "0.95rem")
                .set("line-height", "1.65");

        card.add(tit, p);
        return card;
    }

    private Span centrarSpan(Span s) {
        s.getStyle().set("text-align", "center");
        return s;
    }
}
