package tss.view.componente.dashboard;

import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.List;
import java.util.Map;

// BLOQUE DE SEGMENTACIÓN, INSERCIÓN Y FRECUENCIAS — ESTILO NEUTRO INSTITUCIONAL
public class PanelInsercionLaboral extends VerticalLayout {

    public PanelInsercionLaboral(
            Map<String, Object> madurez,
            double tasaInsercion,
            List<Map<String, Object>> freqSituacion,
            List<Map<String, Object>> freqTiempoPrimerEmpleo,
            List<Map<String, Object>> freqPrimerEmpleo,
            List<Map<String, Object>> freqCantidadEmpleos) {

        setPadding(false);
        setSpacing(false);
        setWidthFull();

        // ── A: SEGMENTACIÓN POR MADUREZ PROFESIONAL ──────────────────────────────
        add(seccionTitulo("Segmentación por Madurez Profesional"));

        Div gridMadurez = new Div();
        gridMadurez.getStyle()
                .set("display", "grid")
                .set("grid-template-columns", "repeat(auto-fit, minmax(260px, 1fr))")
                .set("gap", "1rem")
                .set("width", "100%")
                .set("margin-bottom", "1rem");

        int    total     = ((Number) madurez.get("total")).intValue();
        long   junior    = ((Number) madurez.get("junior")).longValue();
        long   senior    = ((Number) madurez.get("senior")).longValue();
        long   sinDato   = ((Number) madurez.get("sinDato")).longValue();
        double pctJunior = ((Number) madurez.get("pctJunior")).doubleValue();
        double pctSenior = ((Number) madurez.get("pctSenior")).doubleValue();

        gridMadurez.add(crearCardKpi(
                "Profesional Junior (0 a 3 años)",
                String.format("%.1f%%", pctJunior),
                String.format("%d de %d titulados", junior, total),
                "Graduados en etapa temprana de desarrollo profesional"
        ));
        gridMadurez.add(crearCardKpi(
                "Mando Consolidado / Senior (> 3 años)",
                String.format("%.1f%%", pctSenior),
                String.format("%d de %d titulados", senior, total),
                "Profesionales con trayectoria consolidada en la industria"
        ));
        if (sinDato > 0) {
            double pctSin = total > 0 ? Math.round(sinDato * 1000.0 / total) / 10.0 : 0.0;
            gridMadurez.add(crearCardKpi(
                    "Sin Dato Registrado",
                    String.format("%.1f%%", pctSin),
                    String.format("%d de %d titulados", sinDato, total),
                    "No especificaron años de vida profesional"
            ));
        }
        add(gridMadurez);

        // ── B: KPI — TASA DE INSERCIÓN ACTIVA ────────────────────────────────────
        add(seccionTitulo("Tasa de Inserción Laboral Activa"));

        Div kpiBox = new Div();
        kpiBox.getStyle()
                .set("background", "#f8f9fa")
                .set("border", "1px solid #e2e8f0")
                .set("border-radius", "8px")
                .set("padding", "1.2rem")
                .set("display", "flex")
                .set("flex-wrap", "wrap")
                .set("align-items", "center")
                .set("justify-content", "space-between")
                .set("gap", "1.5rem")
                .set("width", "100%")
                .set("margin-bottom", "1rem")
                .set("box-shadow", "0 1px 4px rgba(0,0,0,0.06)");

        Div valorKpi = new Div();
        valorKpi.getStyle().set("display", "flex").set("flex-direction", "column").set("gap", "0.2rem");

        Span subKpi = new Span("Indicador de Ocupación Activa");
        subKpi.getStyle()
                .set("font-size", "0.82rem")
                .set("font-weight", "600")
                .set("color", "#475569")
                .set("text-transform", "uppercase")
                .set("letter-spacing", "0.5px");

        Span numKpi = new Span(String.format("%.2f%%", tasaInsercion));
        numKpi.getStyle()
                .set("font-size", "1.8rem")
                .set("font-weight", "800")
                .set("color", "#003366")
                .set("line-height", "1.1");

        Span labelKpi = new Span("de los titulados encuestados se encuentran actualmente en actividad laboral");
        labelKpi.getStyle()
                .set("color", "#64748b")
                .set("font-size", "0.85rem");

        valorKpi.add(subKpi, numKpi, labelKpi);

        Div infoKpi = new Div();
        infoKpi.getStyle().set("display", "flex").set("flex-direction", "column").set("gap", "0.25rem");

        Span c1 = new Span("• Empleados en organizaciones, empresas o entidades");
        c1.getStyle().set("font-size", "0.82rem").set("color", "#334155");
        Span c2 = new Span("• Emprendimientos propios o trabajo independiente");
        c2.getStyle().set("font-size", "0.82rem").set("color", "#334155");
        Span c3 = new Span("(Personas en búsqueda o desempleadas no computan en la tasa activa)");
        c3.getStyle().set("font-size", "0.75rem").set("color", "#64748b").set("font-style", "italic");

        infoKpi.add(c1, c2, c3);
        kpiBox.add(valorKpi, infoKpi);
        add(kpiBox);

        // ── C: DISTRIBUCIÓN POR SITUACIÓN LABORAL ────────────────────────────────
        add(seccionTitulo("Distribución por Situación Laboral Actual"));
        add(tablaFrecuencia(freqSituacion));

        // ── D: MÉTRICAS DE INSERCIÓN INICIAL ─────────────────────────────────────
        add(seccionTitulo("Métricas de Inserción Inicial y Rotación"));

        Div gridInserciones = new Div();
        gridInserciones.getStyle()
                .set("display", "grid")
                .set("grid-template-columns", "repeat(auto-fit, minmax(300px, 1fr))")
                .set("gap", "1rem")
                .set("width", "100%")
                .set("margin-bottom", "1rem");

        gridInserciones.add(bloqueFrecuencia(
                "Tiempo para Conseguir el Primer Empleo", freqTiempoPrimerEmpleo));
        gridInserciones.add(bloqueFrecuencia(
                "¿Es su Trabajo Actual el Primer Empleo?", freqPrimerEmpleo));
        gridInserciones.add(bloqueFrecuencia(
                "Rotación Laboral (Cantidad de Empleos)", freqCantidadEmpleos));
        add(gridInserciones);
    }

    private H3 seccionTitulo(String texto) {
        H3 h = new H3(texto);
        h.getStyle()
                .set("color", "#003366")
                .set("margin", "1.25rem 0 0.5rem 0")
                .set("font-size", "1.15rem")
                .set("font-weight", "700");
        return h;
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

    private Div tablaFrecuencia(List<Map<String, Object>> filas) {
        Div contenedor = new Div();
        contenedor.getStyle()
                .set("background", "#ffffff")
                .set("border", "1px solid #e2e8f0")
                .set("border-radius", "8px")
                .set("overflow", "hidden")
                .set("box-shadow", "0 1px 4px rgba(0,0,0,0.06)")
                .set("margin-bottom", "1rem");

        // Encabezado
        Div header = new Div();
        header.getStyle()
                .set("display", "grid")
                .set("grid-template-columns", "1fr 80px 80px 180px")
                .set("background", "#003366")
                .set("padding", "0.7rem 1rem")
                .set("gap", "0.5rem");

        for (String col : new String[]{"Situación Laboral", "N°", "%", "Proporción"}) {
            Span s = new Span(col);
            s.getStyle().set("color", "#ffffff").set("font-weight", "700").set("font-size", "0.82rem");
            header.add(s);
        }
        contenedor.add(header);

        int idx = 0;
        for (Map<String, Object> fila : filas) {
            String cat = fila.get("categoria").toString();
            long cant  = ((Number) fila.get("cantidad")).longValue();
            double pct = ((Number) fila.get("porcentaje")).doubleValue();
            String bg  = idx % 2 == 0 ? "#ffffff" : "#f8fafc";

            Div row = new Div();
            row.getStyle().set("display", "grid")
                    .set("grid-template-columns", "1fr 80px 80px 180px")
                    .set("padding", "0.6rem 1rem")
                    .set("gap", "0.5rem")
                    .set("background", bg)
                    .set("border-top", "1px solid #e2e8f0")
                    .set("align-items", "center");

            Span catSpan = new Span(cat);
            catSpan.getStyle().set("font-size", "0.85rem").set("color", "#1e293b");

            Span cantSpan = new Span(String.valueOf(cant));
            cantSpan.getStyle().set("font-weight", "700").set("color", "#003366").set("text-align", "center");

            Span pctSpan = new Span(String.format("%.1f%%", pct));
            pctSpan.getStyle().set("font-weight", "700").set("color", "#1e293b").set("text-align", "center");

            Div barra = new Div();
            barra.getStyle().set("background", "#e2e8f0").set("border-radius", "4px").set("height", "8px");
            Div rel = new Div();
            rel.getStyle().set("background", "#003366")
                    .set("border-radius", "4px").set("height", "100%")
                    .set("width", Math.min(pct, 100) + "%");
            barra.add(rel);

            row.add(catSpan, cantSpan, pctSpan, barra);
            contenedor.add(row);
            idx++;
        }
        return contenedor;
    }

    private Div bloqueFrecuencia(String titulo, List<Map<String, Object>> filas) {
        Div bloque = new Div();
        bloque.getStyle()
                .set("background", "#ffffff")
                .set("border", "1px solid #e2e8f0")
                .set("border-radius", "8px")
                .set("overflow", "hidden")
                .set("box-shadow", "0 1px 4px rgba(0,0,0,0.06)");

        Div header = new Div();
        header.getStyle()
                .set("background", "#003366")
                .set("padding", "0.65rem 1rem");

        Span titSpan = new Span(titulo);
        titSpan.getStyle().set("color", "#ffffff").set("font-weight", "700").set("font-size", "0.85rem");
        header.add(titSpan);
        bloque.add(header);

        int idx = 0;
        for (Map<String, Object> fila : filas) {
            String cat = fila.get("categoria").toString();
            long cant  = ((Number) fila.get("cantidad")).longValue();
            double pct = ((Number) fila.get("porcentaje")).doubleValue();
            String bg  = idx % 2 == 0 ? "#ffffff" : "#f8fafc";

            Div row = new Div();
            row.getStyle()
                    .set("padding", "0.55rem 1rem")
                    .set("background", bg)
                    .set("border-top", "1px solid #f1f5f9")
                    .set("display", "flex")
                    .set("flex-direction", "column")
                    .set("gap", "0.25rem");

            Div linea = new Div();
            linea.getStyle().set("display", "flex").set("justify-content", "space-between")
                    .set("align-items", "center");

            Span catSpan = new Span(cat);
            catSpan.getStyle().set("font-size", "0.83rem").set("color", "#334155").set("flex", "1");

            Span badge = new Span(cant + "  |  " + String.format("%.1f%%", pct));
            badge.getStyle().set("font-size", "0.8rem").set("font-weight", "700")
                    .set("color", "#003366").set("white-space", "nowrap");

            linea.add(catSpan, badge);

            Div barraContenedor = new Div();
            barraContenedor.getStyle().set("background", "#e2e8f0")
                    .set("border-radius", "3px").set("height", "5px");
            Div barraRel = new Div();
            barraRel.getStyle().set("background", "#003366").set("border-radius", "3px")
                    .set("height", "100%").set("width", Math.min(pct, 100) + "%");
            barraContenedor.add(barraRel);

            row.add(linea, barraContenedor);
            bloque.add(row);
            idx++;
        }
        return bloque;
    }
}
