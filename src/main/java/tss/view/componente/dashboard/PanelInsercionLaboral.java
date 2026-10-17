package tss.view.componente.dashboard;

import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.List;
import java.util.Map;

// BLOQUE DE SEGMENTACIÓN, INSERCIÓN Y FRECUENCIAS CON COLORES Y EMOJIS — HU1
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
        add(seccionTitulo("📊 Segmentación por Madurez Profesional"));

        Div gridMadurez = new Div();
        gridMadurez.getStyle()
                .set("display", "grid")
                .set("grid-template-columns", "repeat(auto-fit, minmax(260px, 1fr))")
                .set("gap", "1rem").set("width", "100%").set("margin-bottom", "1.5rem");

        int    total     = ((Number) madurez.get("total")).intValue();
        long   junior    = ((Number) madurez.get("junior")).longValue();
        long   senior    = ((Number) madurez.get("senior")).longValue();
        long   sinDato   = ((Number) madurez.get("sinDato")).longValue();
        double pctJunior = ((Number) madurez.get("pctJunior")).doubleValue();
        double pctSenior = ((Number) madurez.get("pctSenior")).doubleValue();

        gridMadurez.add(cardMadurez("🌱 Profesional Junior",
                "De 0 a 3 años de experiencia",
                junior, pctJunior, "#d1fae5", "#065f46"));
        gridMadurez.add(cardMadurez("🎯 Mando Consolidado / Senior",
                "Más de 3 años de experiencia",
                senior, pctSenior, "#dbeafe", "#1e3a8a"));
        if (sinDato > 0) {
            double pctSin = total > 0 ? Math.round(sinDato * 1000.0 / total) / 10.0 : 0.0;
            gridMadurez.add(cardMadurez("❓ Sin dato",
                    "No registró años de vida profesional",
                    sinDato, pctSin, "#f3f4f6", "#374151"));
        }
        add(gridMadurez);

        // ── B: KPI — TASA DE INSERCIÓN ACTIVA ────────────────────────────────────
        add(seccionTitulo("🏆 Tasa de Inserción Laboral Activa"));

        Div kpiBox = new Div();
        kpiBox.getStyle()
                .set("background", "linear-gradient(135deg, #003366 0%, #1d4ed8 100%)")
                .set("border-radius", "12px").set("padding", "1.5rem 2rem")
                .set("display", "flex").set("align-items", "center").set("gap", "2rem")
                .set("width", "100%").set("margin-bottom", "1.5rem")
                .set("box-shadow", "0 4px 14px rgba(0,51,102,0.3)");

        Div valorKpi = new Div();
        valorKpi.getStyle().set("display", "flex").set("flex-direction", "column");
        Span numKpi = new Span(String.format("%.2f%%", tasaInsercion));
        numKpi.getStyle().set("font-size", "3.5rem").set("font-weight", "900")
                .set("color", "#ffffff").set("line-height", "1");
        Span labelKpi = new Span("de los titulados se encuentran laboralmente activos");
        labelKpi.getStyle().set("color", "rgba(255,255,255,0.8)").set("font-size", "0.9rem").set("margin-top", "0.3rem");
        valorKpi.add(numKpi, labelKpi);

        Div infoKpi = new Div();
        infoKpi.getStyle().set("display", "flex").set("flex-direction", "column").set("gap", "0.3rem");
        infoKpi.add(badgeKpi("✅", "Trabajando en una organización"));
        infoKpi.add(badgeKpi("🚀", "Emprendimiento propio a tiempo completo"));
        Span nota = new Span("❌  Desempleados no cuentan en la tasa activa");
        nota.getStyle().set("font-size", "0.8rem").set("color", "rgba(255,255,255,0.5)");
        infoKpi.add(nota);

        kpiBox.add(valorKpi, infoKpi);
        add(kpiBox);

        // ── C: DISTRIBUCIÓN POR SITUACIÓN LABORAL ────────────────────────────────
        add(seccionTitulo("📋 Distribución por Situación Laboral Actual"));
        add(tablaFrecuencia(freqSituacion, total));

        // ── D: MÉTRICAS DE INSERCIÓN INICIAL ─────────────────────────────────────
        add(seccionTitulo("⏱ Métricas de Inserción Inicial"));

        Div gridInserciones = new Div();
        gridInserciones.getStyle()
                .set("display", "grid")
                .set("grid-template-columns", "repeat(auto-fit, minmax(300px, 1fr))")
                .set("gap", "1rem").set("width", "100%").set("margin-bottom", "1rem");

        gridInserciones.add(bloqueFrecuencia(
                "⏳ Tiempo para Conseguir el Primer Empleo", freqTiempoPrimerEmpleo, "#7c3aed"));
        gridInserciones.add(bloqueFrecuencia(
                "🔑 ¿Es su Trabajo Actual el Primer Empleo?", freqPrimerEmpleo, "#0369a1"));
        gridInserciones.add(bloqueFrecuencia(
                "🔄 Rotación Laboral (Cantidad de Empleos)", freqCantidadEmpleos, "#b45309"));
        add(gridInserciones);
    }

    // ─── HELPERS ─────────────────────────────────────────────────────────────────

    private H3 seccionTitulo(String texto) {
        H3 h = new H3(texto);
        h.getStyle()
                .set("color", "#003366").set("margin", "1.25rem 0 0.75rem 0")
                .set("font-size", "1.05rem").set("font-weight", "700")
                .set("border-bottom", "2px solid #e2e8f0").set("padding-bottom", "0.4rem");
        return h;
    }

    private Div cardMadurez(String titulo, String descripcion, long cantidad,
                             double porcentaje, String colorFondo, String colorTexto) {
        Div card = new Div();
        card.getStyle()
                .set("background", colorFondo).set("border-radius", "12px")
                .set("padding", "1.25rem").set("box-shadow", "0 2px 8px rgba(0,0,0,0.07)")
                .set("display", "flex").set("flex-direction", "column").set("gap", "0.5rem");

        Span tit = new Span(titulo);
        tit.getStyle().set("font-weight", "800").set("font-size", "1rem").set("color", colorTexto);

        Span desc = new Span(descripcion);
        desc.getStyle().set("font-size", "0.8rem").set("color", colorTexto).set("opacity", "0.7");

        Div valor = new Div();
        valor.getStyle().set("display", "flex").set("align-items", "baseline").set("gap", "0.6rem");

        Span num = new Span(cantidad + " titulados");
        num.getStyle().set("font-size", "1.8rem").set("font-weight", "900").set("color", colorTexto);

        Span pct = new Span(String.format("%.1f%%", porcentaje));
        pct.getStyle().set("font-size", "1.1rem").set("font-weight", "700")
                .set("color", colorTexto).set("opacity", "0.8");

        valor.add(num, pct);
        card.add(tit, desc, valor);
        return card;
    }

    private Div badgeKpi(String emoji, String texto) {
        Div b = new Div();
        b.getStyle().set("display", "flex").set("align-items", "center").set("gap", "0.4rem");
        Span e = new Span(emoji);
        Span t = new Span(texto);
        t.getStyle().set("color", "rgba(255,255,255,0.88)").set("font-size", "0.87rem").set("font-weight", "500");
        b.add(e, t);
        return b;
    }

    private Div tablaFrecuencia(List<Map<String, Object>> filas, int total) {
        Div contenedor = new Div();
        contenedor.getStyle()
                .set("border-radius", "10px").set("overflow", "hidden")
                .set("box-shadow", "0 2px 8px rgba(0,0,0,0.07)").set("margin-bottom", "1rem");

        // Encabezado
        Div header = new Div();
        header.getStyle()
                .set("display", "grid").set("grid-template-columns", "1fr 70px 70px 180px")
                .set("background", "#003366").set("padding", "0.6rem 1rem").set("gap", "0.5rem");
        for (String col : new String[]{"Situación Laboral", "N", "%", "Proporción"}) {
            Span s = new Span(col);
            s.getStyle().set("color", "#ffffff").set("font-weight", "700").set("font-size", "0.8rem");
            header.add(s);
        }
        contenedor.add(header);

        int idx = 0;
        for (Map<String, Object> fila : filas) {
            String cat  = fila.get("categoria").toString();
            long cant   = ((Number) fila.get("cantidad")).longValue();
            double pct  = ((Number) fila.get("porcentaje")).doubleValue();
            boolean activo = cat.toLowerCase().contains("trabajando") ||
                             cat.toLowerCase().contains("emprendimiento");
            String bg = activo ? "#f0fdf4" : (idx % 2 == 0 ? "#ffffff" : "#f8fafc");

            Div row = new Div();
            row.getStyle().set("display", "grid")
                    .set("grid-template-columns", "1fr 70px 70px 180px")
                    .set("padding", "0.6rem 1rem").set("gap", "0.5rem")
                    .set("background", bg).set("border-top", "1px solid #e2e8f0")
                    .set("align-items", "center");

            Span catSpan = new Span(cat);
            catSpan.getStyle().set("font-size", "0.85rem").set("color", "#1e293b");
            if (activo) catSpan.getStyle().set("font-weight", "700");

            Span cantSpan = new Span(String.valueOf(cant));
            cantSpan.getStyle().set("font-weight", "700").set("color", "#003366").set("text-align", "center");

            Span pctSpan = new Span(String.format("%.1f%%", pct));
            pctSpan.getStyle().set("font-weight", "700")
                    .set("color", activo ? "#047857" : "#475569").set("text-align", "center");

            Div barra = new Div();
            barra.getStyle().set("background", "#e2e8f0").set("border-radius", "4px").set("height", "8px");
            Div rel = new Div();
            rel.getStyle().set("background", activo ? "#10b981" : "#6366f1")
                    .set("border-radius", "4px").set("height", "100%")
                    .set("width", Math.min(pct, 100) + "%");
            barra.add(rel);

            row.add(catSpan, cantSpan, pctSpan, barra);
            contenedor.add(row);
            idx++;
        }
        return contenedor;
    }

    private Div bloqueFrecuencia(String titulo, List<Map<String, Object>> filas, String colorAccent) {
        Div bloque = new Div();
        bloque.getStyle()
                .set("border-radius", "10px").set("overflow", "hidden")
                .set("box-shadow", "0 2px 8px rgba(0,0,0,0.07)");

        Div header = new Div();
        header.getStyle()
                .set("background", colorAccent).set("padding", "0.6rem 1rem");
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
                    .set("padding", "0.5rem 1rem").set("background", bg)
                    .set("border-top", "1px solid #f1f5f9")
                    .set("display", "flex").set("flex-direction", "column").set("gap", "0.2rem");

            Div linea = new Div();
            linea.getStyle().set("display", "flex").set("justify-content", "space-between")
                    .set("align-items", "center");

            Span catSpan = new Span(cat);
            catSpan.getStyle().set("font-size", "0.83rem").set("color", "#334155").set("flex", "1");

            Span badge = new Span(cant + "  |  " + String.format("%.1f%%", pct));
            badge.getStyle().set("font-size", "0.8rem").set("font-weight", "800")
                    .set("color", colorAccent).set("white-space", "nowrap");

            linea.add(catSpan, badge);

            Div barraContenedor = new Div();
            barraContenedor.getStyle().set("background", "#f1f5f9")
                    .set("border-radius", "3px").set("height", "5px");
            Div barraRel = new Div();
            barraRel.getStyle().set("background", colorAccent).set("border-radius", "3px")
                    .set("height", "100%").set("width", Math.min(pct, 100) + "%");
            barraContenedor.add(barraRel);

            row.add(linea, barraContenedor);
            bloque.add(row);
            idx++;
        }
        return bloque;
    }
}
