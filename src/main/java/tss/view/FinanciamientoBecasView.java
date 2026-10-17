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

// FINANCIAMIENTO Y BECAS HU3: ESTUDIO DE CONDICIONES ECONÓMICAS Y PRUEBA CHI-CUADRADA
@Route(value = "financiamiento-becas", layout = MainLayout.class)
@PageTitle("Financiamiento y Becas - Análisis de Mercado UMSS")
public class FinanciamientoBecasView extends VerticalLayout {

    public FinanciamientoBecasView(RespuestaTituladoService tituladoService, ReporteService reporteService) {
        setPadding(true);
        setSpacing(true);
        setWidthFull();

        List<RespuestaTitulado> datos = tituladoService.listarTodos();
        GraficosFactory graficos = new GraficosFactory(reporteService);

        Map<String, Object> kpis = reporteService.calcularResumenFinanciamiento(datos);
        Map<String, Object> contingencia = reporteService.calcularTablaContingenciaChiCuadrada(datos);
        List<Map<String, Object>> fuentes = reporteService.calcularDistribucionFuentes(datos);

        // 1. ENCABEZADO INSTITUCIONAL
        add(buildEncabezado());

        // 2. TARJETAS DE RESUMEN (KPIS SUPERIORES)
        add(buildGridKpis(kpis));

        // 3. DISTRIBUCIÓN SIMPLE DE FUENTES DE FINANCIAMIENTO
        add(buildSeccionDistribucion(datos, fuentes, graficos, reporteService));

        // 4. TABLA DE CONTINGENCIA CRUZADA (NIVEL DE POSGRADO VS FUENTE)
        add(buildSeccionContingencia(contingencia));

        // 5. PANEL DE LA PRUEBA CHI-CUADRADA Y DIAGNÓSTICO AUTOMÁTICO
        add(buildSeccionPruebaChi(kpis, reporteService.generarDiagnosticoHU3(kpis)));
    }

    private Div buildEncabezado() {
        Div contenedor = new Div();
        contenedor.getStyle().set("margin-bottom", "0.5rem");

        H2 titulo = new H2("Financiamiento y Becas — Historia de Usuario 3");
        titulo.getStyle()
                .set("margin", "0 0 0.3rem 0")
                .set("color", "#003366")
                .set("font-size", "1.6rem")
                .set("font-weight", "700");

        Span subtitulo = new Span("Análisis de dependencia económica, cobertura de becas y prueba de independencia Chi-Cuadrada (χ²) entre el nivel de posgrado y la fuente de financiamiento.");
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

        long total = ((Number) kpis.get("total")).longValue();
        long autofinanciados = ((Number) kpis.get("autofinanciados")).longValue();
        double pctAuto = ((Number) kpis.get("pctAutofinanciamiento")).doubleValue();
        boolean alertaAuto = Boolean.TRUE.equals(kpis.get("alertaAutofinanciamiento"));

        long conBeca = ((Number) kpis.get("conBeca")).longValue();
        double pctBeca = ((Number) kpis.get("pctCoberturaBeca")).doubleValue();

        double chi2 = ((Number) kpis.get("chiCuadrada")).doubleValue();
        int gl = ((Number) kpis.get("gradosLibertad")).intValue();
        double pVal = ((Number) kpis.get("pValor")).doubleValue();
        boolean dep = Boolean.TRUE.equals(kpis.get("existeDependencia"));

        // KPI 1: % Autofinanciamiento
        grid.add(crearCardKpi(
                "% de Autofinanciamiento (Recursos Propios)",
                String.format("%.1f%%", pctAuto),
                String.format("%d de %d titulados con posgrado", autofinanciados, total),
                alertaAuto ? "Alerta: Alta vulnerabilidad económica / dependencia de recursos propios" : "Financiado totalmente con recursos propios",
                alertaAuto
        ));

        // KPI 2: % Cobertura de Becas
        grid.add(crearCardKpi(
                "% de Cobertura de Becas",
                String.format("%.1f%%", pctBeca),
                String.format("%d de %d titulados con posgrado", conBeca, total),
                "Accedieron a financiamiento total o parcial por beca",
                false
        ));

        // KPI 3: Valor Chi-Cuadrada
        grid.add(crearCardKpi(
                "Valor Chi-Cuadrada (χ²)",
                String.format("χ² = %.2f", chi2),
                String.format("gl = %d grados de libertad", gl),
                "Fórmula matemática del cruce bivariado",
                false
        ));

        // KPI 4: Resultado de la Prueba
        grid.add(crearCardKpi(
                "Resultado de la Prueba",
                dep ? "Existe Dependencia" : "Son Independientes",
                String.format("p-valor = %.4f (α = 0.05)", pVal),
                dep ? "Existe dependencia financiera (p < 0.05)" : "Variables estadísticamente independientes (p ≥ 0.05)",
                false
        ));

        return grid;
    }

    private Div crearCardKpi(String titulo, String valorPrincipal, String valorSecundario, String descripcion, boolean resaltarAlerta) {
        Div card = new Div();
        card.getStyle()
                .set("background", "#f8f9fa")
                .set("border", resaltarAlerta ? "1px solid #cbd5e1" : "1px solid #e2e8f0")
                .set("border-left", resaltarAlerta ? "4px solid #b91c1c" : "4px solid #003366")
                .set("border-radius", "8px")
                .set("padding", "1.2rem")
                .set("box-shadow", "0 1px 4px rgba(0,0,0,0.06)")
                .set("display", "flex")
                .set("flex-direction", "column")
                .set("gap", "0.35rem");

        Span tit = new Span(titulo);
        tit.getStyle()
                .set("font-size", "0.8rem")
                .set("font-weight", "600")
                .set("color", "#475569")
                .set("text-transform", "uppercase")
                .set("letter-spacing", "0.5px");

        Span val = new Span(valorPrincipal);
        val.getStyle()
                .set("font-size", "1.6rem")
                .set("font-weight", "800")
                .set("color", resaltarAlerta ? "#991b1b" : "#003366")
                .set("line-height", "1.1");

        Span sub = new Span(valorSecundario);
        sub.getStyle()
                .set("font-size", "0.85rem")
                .set("font-weight", "600")
                .set("color", "#1e293b");

        Span desc = new Span(descripcion);
        desc.getStyle()
                .set("font-size", "0.75rem")
                .set("color", resaltarAlerta ? "#b91c1c" : "#64748b")
                .set("font-weight", resaltarAlerta ? "600" : "400");

        card.add(tit, val, sub, desc);
        return card;
    }

    private Div buildSeccionDistribucion(List<RespuestaTitulado> datos, List<Map<String, Object>> fuentes,
                                         GraficosFactory graficos, ReporteService reporteService) {
        Div contenedor = new Div();
        contenedor.getStyle()
                .set("background", "#f8f9fa")
                .set("border", "1px solid #e2e8f0")
                .set("border-radius", "8px")
                .set("padding", "1.25rem")
                .set("box-shadow", "0 1px 4px rgba(0,0,0,0.06)")
                .set("width", "100%")
                .set("margin-bottom", "1rem");

        H3 titulo = new H3("Distribución de Fuentes de Financiamiento (Frecuencias)");
        titulo.getStyle()
                .set("margin", "0 0 0.25rem 0")
                .set("color", "#003366")
                .set("font-size", "1.15rem");

        Span desc = new Span("Proporción y conteo de titulados que utilizaron recursos propios, becas o créditos formativos.");
        desc.getStyle().set("color", "#64748b").set("font-size", "0.85rem").set("display", "block").set("margin-bottom", "1rem");

        contenedor.add(titulo, desc);

        Div grid = new Div();
        grid.getStyle()
                .set("display", "grid")
                .set("grid-template-columns", "repeat(auto-fit, minmax(320px, 1fr))")
                .set("gap", "1.5rem")
                .set("align-items", "center");

        // 1. Gráfico de Torta / Dona
        Div boxGrafico = new Div();
        boxGrafico.getStyle()
                .set("background", "#ffffff")
                .set("border", "1px solid #e2e8f0")
                .set("border-radius", "8px")
                .set("padding", "0.75rem");
        boxGrafico.add(graficos.torta(datos,
                r -> r.getFinanciamientoPosgrado() != null ? reporteService.normalizarFuenteFinanciamiento(r.getFinanciamientoPosgrado()) : null,
                "Fuentes de Financiamiento"));

        // 2. Lista de barras de frecuencia
        Div boxFrecuencias = new Div();
        boxFrecuencias.getStyle()
                .set("display", "flex")
                .set("flex-direction", "column")
                .set("gap", "0.85rem");

        for (Map<String, Object> item : fuentes) {
            String cat = (String) item.get("categoria");
            long cant = ((Number) item.get("cantidad")).longValue();
            double pct = ((Number) item.get("porcentaje")).doubleValue();

            Div itemDiv = new Div();
            itemDiv.getStyle()
                    .set("background", "#ffffff")
                    .set("border", "1px solid #e2e8f0")
                    .set("border-radius", "6px")
                    .set("padding", "0.75rem 1rem");

            Div rowTit = new Div();
            rowTit.getStyle().set("display", "flex").set("justify-content", "space-between").set("align-items", "center");

            Span tit = new Span(cat);
            tit.getStyle().set("font-weight", "600").set("font-size", "0.9rem").set("color", "#1e293b");

            Span nums = new Span(String.format("%d personas (%s%%)", cant, String.format("%.1f", pct)));
            nums.getStyle().set("font-weight", "700").set("font-size", "0.88rem").set("color", "#003366");

            rowTit.add(tit, nums);

            Div track = new Div();
            track.getStyle()
                    .set("height", "7px")
                    .set("background", "#e2e8f0")
                    .set("border-radius", "4px")
                    .set("overflow", "hidden")
                    .set("margin-top", "0.45rem");

            Div fill = new Div();
            fill.getStyle()
                    .set("width", Math.min(pct, 100.0) + "%")
                    .set("height", "100%")
                    .set("background", "#003366")
                    .set("border-radius", "4px");
            track.add(fill);

            itemDiv.add(rowTit, track);
            boxFrecuencias.add(itemDiv);
        }

        grid.add(boxGrafico, boxFrecuencias);
        contenedor.add(grid);
        return contenedor;
    }

    @SuppressWarnings("unchecked")
    private Div buildSeccionContingencia(Map<String, Object> contingencia) {
        Div contenedor = new Div();
        contenedor.getStyle()
                .set("background", "#f8f9fa")
                .set("border", "1px solid #e2e8f0")
                .set("border-radius", "8px")
                .set("padding", "1.25rem")
                .set("box-shadow", "0 1px 4px rgba(0,0,0,0.06)")
                .set("width", "100%")
                .set("margin-bottom", "1rem");

        H3 titulo = new H3("Tabla de Contingencia Cruzada (El núcleo de la HU 3)");
        titulo.getStyle()
                .set("margin", "0 0 0.25rem 0")
                .set("color", "#003366")
                .set("font-size", "1.15rem");

        Span nota = new Span("Matriz de doble entrada comparando frecuencias observadas (O) y frecuencias esperadas (E) para cada combinación de Nivel de Posgrado y Fuente de Financiamiento.");
        nota.getStyle().set("color", "#64748b").set("font-size", "0.85rem").set("display", "block").set("margin-bottom", "1rem");

        contenedor.add(titulo, nota);

        List<String> filas = (List<String>) contingencia.get("filas");
        List<String> columnas = (List<String>) contingencia.get("columnas");
        Map<String, Map<String, Long>> observados = (Map<String, Map<String, Long>>) contingencia.get("observados");
        Map<String, Map<String, Double>> esperados = (Map<String, Map<String, Double>>) contingencia.get("esperados");
        Map<String, Long> totalFila = (Map<String, Long>) contingencia.get("totalFila");
        Map<String, Long> totalColumna = (Map<String, Long>) contingencia.get("totalColumna");
        long totalGeneral = ((Number) contingencia.get("totalGeneral")).longValue();

        Div tabla = new Div();
        tabla.getStyle()
                .set("border", "1px solid #cbd5e1")
                .set("border-radius", "6px")
                .set("overflow-x", "auto");

        // Configuración de columnas dinámicas
        String gridCols = "1.8fr repeat(" + columnas.size() + ", 1fr) 1.1fr";

        // ENCABEZADO
        Div header = new Div();
        header.getStyle()
                .set("display", "grid")
                .set("grid-template-columns", gridCols)
                .set("background", "#e9ecef")
                .set("padding", "0.75rem 1rem")
                .set("gap", "0.75rem")
                .set("border-bottom", "2px solid #cbd5e1")
                .set("font-weight", "700")
                .set("font-size", "0.85rem")
                .set("color", "#003366");

        header.add(new Span("Nivel de Posgrado \\ Fuente"));
        for (String c : columnas) {
            Span colSpan = new Span(c);
            colSpan.getStyle().set("text-align", "center");
            header.add(colSpan);
        }
        Span totFilaSpan = new Span("Total Fila");
        totFilaSpan.getStyle().set("text-align", "center");
        header.add(totFilaSpan);
        tabla.add(header);

        // FILAS
        int idx = 0;
        for (String f : filas) {
            String fondo = idx % 2 == 0 ? "#ffffff" : "#f8fafc";
            Div row = new Div();
            row.getStyle()
                    .set("display", "grid")
                    .set("grid-template-columns", gridCols)
                    .set("padding", "0.7rem 1rem")
                    .set("gap", "0.75rem")
                    .set("background", fondo)
                    .set("border-bottom", "1px solid #e2e8f0")
                    .set("align-items", "center")
                    .set("font-size", "0.88rem");

            Span fSpan = new Span(f);
            fSpan.getStyle().set("font-weight", "600").set("color", "#1e293b");
            row.add(fSpan);

            for (String c : columnas) {
                long o = observados.getOrDefault(f, Map.of()).getOrDefault(c, 0L);
                double e = esperados.getOrDefault(f, Map.of()).getOrDefault(c, 0.0);

                Div celda = new Div();
                celda.getStyle()
                        .set("display", "flex")
                        .set("flex-direction", "column")
                        .set("align-items", "center");

                Span oSpan = new Span(String.valueOf(o));
                oSpan.getStyle().set("font-weight", "700").set("color", o > 0 ? "#003366" : "#94a3b8");

                Span eSpan = new Span(String.format("E: %.1f", e));
                eSpan.getStyle().set("font-size", "0.72rem").set("color", "#64748b");

                celda.add(oSpan, eSpan);
                row.add(celda);
            }

            long tf = totalFila.getOrDefault(f, 0L);
            Span tfSpan = new Span(String.valueOf(tf));
            tfSpan.getStyle().set("font-weight", "800").set("text-align", "center").set("color", "#003366");
            row.add(tfSpan);

            tabla.add(row);
            idx++;
        }

        // TOTAL COLUMNA
        Div totalRow = new Div();
        totalRow.getStyle()
                .set("display", "grid")
                .set("grid-template-columns", gridCols)
                .set("padding", "0.75rem 1rem")
                .set("gap", "0.75rem")
                .set("background", "#e9ecef")
                .set("border-top", "2px solid #cbd5e1")
                .set("font-weight", "800")
                .set("font-size", "0.9rem")
                .set("color", "#003366")
                .set("align-items", "center");

        totalRow.add(new Span("Total Columna"));
        for (String c : columnas) {
            long tc = totalColumna.getOrDefault(c, 0L);
            Span tcSpan = new Span(String.valueOf(tc));
            tcSpan.getStyle().set("text-align", "center");
            totalRow.add(tcSpan);
        }

        Span nSpan = new Span(String.valueOf(totalGeneral));
        nSpan.getStyle().set("text-align", "center").set("font-size", "1rem");
        totalRow.add(nSpan);

        tabla.add(totalRow);
        contenedor.add(tabla);
        return contenedor;
    }

    private Div buildSeccionPruebaChi(Map<String, Object> kpis, String interpretacion) {
        Div card = new Div();
        card.getStyle()
                .set("background", "#f8f9fa")
                .set("border", "1px solid #e2e8f0")
                .set("border-left", "5px solid #003366")
                .set("border-radius", "6px")
                .set("padding", "1.25rem 1.5rem")
                .set("box-shadow", "0 1px 4px rgba(0,0,0,0.06)")
                .set("width", "100%");

        H3 tit = new H3("Panel de la Prueba Chi-Cuadrada (χ²) y Diagnóstico Automático");
        tit.getStyle()
                .set("margin", "0 0 0.5rem 0")
                .set("color", "#003366")
                .set("font-size", "1.15rem")
                .set("font-weight", "700");

        Span formula = new Span("Fórmula aplicada:  χ² = Σ [ (O - E)² / E ]  comparando cada frecuencia observada contra su frecuencia esperada teórica bajo la hipótesis nula de independencia.");
        formula.getStyle()
                .set("font-size", "0.85rem")
                .set("color", "#475569")
                .set("display", "block")
                .set("margin-bottom", "1rem");

        // 3 Datos matemáticos
        Div gridMath = new Div();
        gridMath.getStyle()
                .set("display", "grid")
                .set("grid-template-columns", "repeat(auto-fit, minmax(200px, 1fr))")
                .set("gap", "1rem")
                .set("margin-bottom", "1.25rem");

        double chi2 = ((Number) kpis.get("chiCuadrada")).doubleValue();
        int gl = ((Number) kpis.get("gradosLibertad")).intValue();
        double pVal = ((Number) kpis.get("pValor")).doubleValue();

        gridMath.add(
                crearCajaDatoMatematico("Estadístico χ²", String.format("%.2f", chi2), "Suma cuadrática estandarizada"),
                crearCajaDatoMatematico("Grados de Libertad (gl)", String.valueOf(gl), "(Filas - 1) × (Columnas - 1)"),
                crearCajaDatoMatematico("Valor p (p-value)", String.format("%.4f", pVal), pVal < 0.05 ? "Significativo (p < 0.05)" : "No significativo (p ≥ 0.05)")
        );

        // Interpretación automática
        Div boxInterpretacion = new Div();
        boxInterpretacion.getStyle()
                .set("background", "#ffffff")
                .set("border", "1px solid #e2e8f0")
                .set("border-radius", "6px")
                .set("padding", "1rem 1.25rem");

        Span lblInt = new Span("Interpretación Diagnóstica Automatizada:");
        lblInt.getStyle()
                .set("display", "block")
                .set("font-size", "0.82rem")
                .set("font-weight", "700")
                .set("color", "#003366")
                .set("text-transform", "uppercase")
                .set("margin-bottom", "0.35rem");

        Paragraph p = new Paragraph(interpretacion);
        p.getStyle()
                .set("margin", "0")
                .set("color", "#1e293b")
                .set("font-size", "0.95rem")
                .set("line-height", "1.65");

        boxInterpretacion.add(lblInt, p);

        card.add(tit, formula, gridMath, boxInterpretacion);
        return card;
    }

    private Div crearCajaDatoMatematico(String titulo, String valor, String subtitulo) {
        Div box = new Div();
        box.getStyle()
                .set("background", "#ffffff")
                .set("border", "1px solid #cbd5e1")
                .set("border-radius", "6px")
                .set("padding", "0.85rem 1rem")
                .set("display", "flex")
                .set("flex-direction", "column")
                .set("gap", "0.2rem");

        Span t = new Span(titulo);
        t.getStyle().set("font-size", "0.78rem").set("font-weight", "600").set("color", "#475569");

        Span v = new Span(valor);
        v.getStyle().set("font-size", "1.45rem").set("font-weight", "800").set("color", "#003366");

        Span s = new Span(subtitulo);
        s.getStyle().set("font-size", "0.75rem").set("color", "#64748b");

        box.add(t, v, s);
        return box;
    }
}
