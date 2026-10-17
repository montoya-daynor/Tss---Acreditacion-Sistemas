package tss.view.componente.dashboard;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;

import java.util.Map;

// PANEL DE 4 KPI CARDS CON COLORES Y EMOJIS — HU1 PERFIL Y EMPLEABILIDAD
public class PanelKpiEmpleabilidad extends Div {

    public PanelKpiEmpleabilidad(Map<String, Object> resumen) {
        getStyle()
                .set("display", "grid")
                .set("grid-template-columns", "repeat(auto-fit, minmax(220px, 1fr))")
                .set("gap", "1rem")
                .set("width", "100%")
                .set("margin-bottom", "1.5rem");

        int    n             = ((Number) resumen.get("totalEncuestados")).intValue();
        double mediaAntig    = ((Number) resumen.get("mediaAntigüedad")).doubleValue();
        double medianaAntig  = ((Number) resumen.get("medianaAntigüedad")).doubleValue();
        double desvAntig     = ((Number) resumen.get("desvAntigüedad")).doubleValue();
        int    nAntig        = ((Number) resumen.get("nAntigüedad")).intValue();
        double mediaDesemp   = ((Number) resumen.get("mediaDesempleo")).doubleValue();
        double medianaDesemp = ((Number) resumen.get("medianaDesempleo")).doubleValue();
        double desvDesemp    = ((Number) resumen.get("desvDesempleo")).doubleValue();
        int    nDesemp       = ((Number) resumen.get("nDesempleo")).intValue();

        // CARD 1: Total de Encuestados  — azul institucional
        add(crearCard("Total de Encuestados", String.valueOf(n),
                "titulados respondieron la encuesta", "👥", "#003366", "#dbeafe"));

        // CARD 2: Antigüedad Profesional — verde
        add(crearCardEstadistico("Antigüedad Profesional",
                mediaAntig, medianaAntig, desvAntig, nAntig,
                "💼", "años de vida profesional", "#065f46", "#d1fae5"));

        // CARD 3: Período de Desempleo — ámbar
        add(crearCardEstadistico("Período de Desempleo",
                mediaDesemp, medianaDesemp, desvDesemp, nDesemp,
                "⏳", "años buscando empleo", "#92400e", "#fef3c7"));

        // CARD 4: Leyenda — celeste
        add(crearCardInfo());
    }

    // TARJETA SIMPLE CON COLOR DE ACENTO
    private Div crearCard(String titulo, String valorPrincipal, String descripcion,
                          String emoji, String colorTexto, String colorFondo) {
        Div card = new Div();
        card.getStyle()
                .set("background", colorFondo)
                .set("border-radius", "12px")
                .set("padding", "1.25rem")
                .set("box-shadow", "0 2px 8px rgba(0,0,0,0.08)")
                .set("display", "flex").set("flex-direction", "column").set("gap", "0.5rem");

        Span emojiBadge = new Span(emoji);
        emojiBadge.getStyle().set("font-size", "2rem");

        H3 tit = new H3(titulo);
        tit.getStyle().set("margin", "0").set("font-size", "0.85rem").set("font-weight", "700")
                .set("color", colorTexto).set("text-transform", "uppercase").set("letter-spacing", "0.05em");

        Span valor = new Span(valorPrincipal);
        valor.getStyle().set("font-size", "3rem").set("font-weight", "900")
                .set("color", colorTexto).set("line-height", "1");

        Span desc = new Span(descripcion);
        desc.getStyle().set("font-size", "0.8rem").set("color", colorTexto)
                .set("opacity", "0.7");

        card.add(emojiBadge, tit, valor, desc);
        return card;
    }

    // TARJETA ESTADÍSTICA CON MEDIA, MEDIANA Y DESV. EST.
    private Div crearCardEstadistico(String titulo, double media, double mediana, double desv,
                                     int n, String emoji, String unidad,
                                     String colorTexto, String colorFondo) {
        Div card = new Div();
        card.getStyle()
                .set("background", colorFondo)
                .set("border-radius", "12px")
                .set("padding", "1.25rem")
                .set("box-shadow", "0 2px 8px rgba(0,0,0,0.08)")
                .set("display", "flex").set("flex-direction", "column").set("gap", "0.4rem");

        Span emojiBadge = new Span(emoji);
        emojiBadge.getStyle().set("font-size", "2rem");

        H3 tit = new H3(titulo);
        tit.getStyle().set("margin", "0").set("font-size", "0.85rem").set("font-weight", "700")
                .set("color", colorTexto).set("text-transform", "uppercase").set("letter-spacing", "0.05em");

        Span valorMedia = new Span(String.format("%.2f", media));
        valorMedia.getStyle().set("font-size", "2.4rem").set("font-weight", "900")
                .set("color", colorTexto).set("line-height", "1");

        Span etqMedia = new Span("Media (x\u0304)  \u2014  " + unidad);
        etqMedia.getStyle().set("font-size", "0.75rem").set("color", colorTexto).set("opacity", "0.7");

        Div filaDetalle = new Div();
        filaDetalle.getStyle()
                .set("display", "grid").set("grid-template-columns", "1fr 1fr")
                .set("gap", "0.25rem")
                .set("border-top", "1px solid rgba(0,0,0,0.1)")
                .set("padding-top", "0.5rem").set("margin-top", "0.25rem");
        filaDetalle.add(metricaFila("Mediana (Me)", String.format("%.2f", mediana), colorTexto));
        filaDetalle.add(metricaFila("Desv. Est. (s)", String.format("\u00b1%.2f", desv), colorTexto));

        Span etqN = new Span("N = " + n + " titulados con dato");
        etqN.getStyle().set("font-size", "0.72rem").set("color", colorTexto).set("opacity", "0.5");

        card.add(emojiBadge, tit, valorMedia, etqMedia, filaDetalle, etqN);
        return card;
    }

    // CARD DE LEYENDA
    private Div crearCardInfo() {
        Div card = new Div();
        card.getStyle()
                .set("background", "#e0f2fe")
                .set("border-radius", "12px")
                .set("padding", "1.25rem")
                .set("box-shadow", "0 2px 8px rgba(0,0,0,0.08)")
                .set("display", "flex").set("flex-direction", "column").set("gap", "0.6rem");

        Span emoji = new Span("📘");
        emoji.getStyle().set("font-size", "2rem");

        H3 tit = new H3("Guía de Métricas");
        tit.getStyle().set("margin", "0").set("font-size", "0.85rem").set("font-weight", "700")
                .set("color", "#0c4a6e").set("text-transform", "uppercase");

        card.add(emoji, tit);

        for (String[] def : new String[][]{
            {"x\u0304 Media",       "Promedio aritmético del conjunto"},
            {"Me Mediana",   "Valor central (robusto a extremos)"},
            {"s Desv. Est.", "Dispersión respecto a la media"}
        }) {
            Div fila = new Div();
            fila.getStyle().set("display", "flex").set("flex-direction", "column");
            Span clave = new Span(def[0]);
            clave.getStyle().set("font-weight", "800").set("color", "#0369a1").set("font-size", "0.82rem");
            Span desc = new Span(def[1]);
            desc.getStyle().set("font-size", "0.75rem").set("color", "#0c4a6e").set("opacity", "0.75");
            fila.add(clave, desc);
            card.add(fila);
        }
        return card;
    }

    private Div metricaFila(String label, String valor, String colorTexto) {
        Div f = new Div();
        f.getStyle().set("display", "flex").set("flex-direction", "column");
        Span l = new Span(label);
        l.getStyle().set("font-size", "0.72rem").set("color", colorTexto).set("opacity", "0.65");
        Span v = new Span(valor);
        v.getStyle().set("font-size", "1rem").set("font-weight", "800").set("color", colorTexto);
        f.add(l, v);
        return f;
    }
}
