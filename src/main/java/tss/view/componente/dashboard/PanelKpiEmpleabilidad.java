package tss.view.componente.dashboard;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;

import java.util.Map;

// PANEL DE KPIS DE PERFIL Y EMPLEABILIDAD — EXACTAMENTE IGUAL A LAS DEMÁS PESTAÑAS
public class PanelKpiEmpleabilidad extends Div {

    public PanelKpiEmpleabilidad(Map<String, Object> resumen) {
        getStyle()
                .set("display", "grid")
                .set("grid-template-columns", "repeat(auto-fit, minmax(240px, 1fr))")
                .set("gap", "1rem")
                .set("width", "100%")
                .set("margin-bottom", "1rem");

        int    n             = ((Number) resumen.get("totalEncuestados")).intValue();
        double mediaAntig    = ((Number) resumen.get("mediaAntigüedad")).doubleValue();
        double medianaAntig  = ((Number) resumen.get("medianaAntigüedad")).doubleValue();
        double desvAntig     = ((Number) resumen.get("desvAntigüedad")).doubleValue();
        int    nAntig        = ((Number) resumen.get("nAntigüedad")).intValue();
        double mediaDesemp   = ((Number) resumen.get("mediaDesempleo")).doubleValue();
        double medianaDesemp = ((Number) resumen.get("medianaDesempleo")).doubleValue();
        double desvDesemp    = ((Number) resumen.get("desvDesempleo")).doubleValue();
        int    nDesemp       = ((Number) resumen.get("nDesempleo")).intValue();

        // CARD 1: Total de Encuestados
        add(crearCardKpi(
                "Total de Encuestados",
                String.valueOf(n),
                n + " titulados con respuesta",
                "Muestra total de participantes en el estudio"
        ));

        // CARD 2: Antigüedad Profesional
        add(crearCardKpi(
                "Antigüedad Profesional",
                String.format("%.2f años", mediaAntig),
                String.format("Mediana: %.2f | Desv: ±%.2f", medianaAntig, desvAntig),
                String.format("Promedio de vida profesional (N=%d)", nAntig)
        ));

        // CARD 3: Período de Desempleo
        add(crearCardKpi(
                "Período de Desempleo",
                String.format("%.2f años", mediaDesemp),
                String.format("Mediana: %.2f | Desv: ±%.2f", medianaDesemp, desvDesemp),
                String.format("Tiempo promedio de búsqueda (N=%d)", nDesemp)
        ));

        // CARD 4: Muestra Válida
        add(crearCardKpi(
                "Tasa de Datos Completos",
                String.format("%.1f%%", n > 0 ? (nAntig * 100.0 / n) : 0.0),
                String.format("%d de %d completaron sección laboral", nAntig, n),
                "Proporción de respuestas con información laboral"
        ));
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
}
