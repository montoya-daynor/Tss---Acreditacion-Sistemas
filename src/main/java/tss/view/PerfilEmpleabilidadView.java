package tss.view;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import tss.entity.RespuestaTitulado;
import tss.service.ReporteService;
import tss.service.RespuestaTituladoService;
import tss.view.componente.dashboard.GraficosFactory;
import tss.view.componente.dashboard.PanelInsercionLaboral;
import tss.view.componente.dashboard.PanelKpiEmpleabilidad;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

// VISTA DE PERFIL Y EMPLEABILIDAD — HU1: INSERCIÓN LABORAL Y TRAYECTORIA PROFESIONAL
@Route(value = "perfil-empleabilidad", layout = MainLayout.class)
@PageTitle("Perfil y Empleabilidad - Análisis de Mercado UMSS")
public class PerfilEmpleabilidadView extends VerticalLayout {

    public PerfilEmpleabilidadView(RespuestaTituladoService tituladoService, ReporteService reporteService) {
        setPadding(true);
        setSpacing(true);
        setWidthFull();

        List<RespuestaTitulado> datos = tituladoService.listarTodos();
        GraficosFactory graficos     = new GraficosFactory(reporteService);

        // ─── 1. ENCABEZADO INSTITUCIONAL ──────────────────────────────────────────
        add(buildEncabezado());

        // ─── 2. BLOQUE 1: ESTADÍSTICAS DE ANTIGÜEDAD Y DESEMPLEO ────────────────────
        add(seccionTitulo("Estadísticas de Antigüedad Profesional y Período de Desempleo"));
        Map<String, Object> resumenStats = reporteService.calcularResumenAntigüedad(datos);
        add(new PanelKpiEmpleabilidad(resumenStats));

        // ─── 3. BLOQUE 2: SEGMENTACIÓN, INSERCIÓN Y FRECUENCIAS ─────────────────────
        Map<String, Object> madurez      = reporteService.calcularSegmentacionMadurez(datos);
        double tasaInsercion             = reporteService.calcularTasaInsercionActiva(datos);

        List<Map<String, Object>> freqSituacion = reporteService.calcularFrecuenciaRelativa(
                datos, RespuestaTitulado::getSituacionLaboral);
        List<Map<String, Object>> freqTiempoEmpleo = reporteService.calcularFrecuenciaRelativa(
                datos, RespuestaTitulado::getTiempoPrimerEmpleo);
        List<Map<String, Object>> freqPrimerEmpleo = reporteService.calcularFrecuenciaRelativa(
                datos, r -> r.getPrimerEmpleo() != null ? (r.getPrimerEmpleo() ? "SÍ" : "NO") : null);
        List<Map<String, Object>> freqCantidadEmpleos = reporteService.calcularFrecuenciaRelativa(
                datos, RespuestaTitulado::getCantidadEmpleos);

        // ORDEN CRONOLÓGICO: Ya trabajaba antes de titularse, Menos de 1 mes, 1-4, 4-8, 8-12, Más de 12 meses
        List<String> ordenCronologico = List.of(
                "Ya trabajaba antes de titularse",
                "Menos de 1 mes",
                "Entre 1 - 4 meses",
                "Entre 4 - 8 meses",
                "Entre 8 - 12 meses",
                "Más de 12 meses"
        );
        freqTiempoEmpleo.sort(Comparator.comparingInt(m -> {
            String cat = m.get("categoria").toString().trim();
            for (int i = 0; i < ordenCronologico.size(); i++) {
                if (cat.equalsIgnoreCase(ordenCronologico.get(i)) ||
                    (i == 2 && cat.contains("1 - 4")) ||
                    (i == 3 && cat.contains("4 - 8")) ||
                    (i == 4 && cat.contains("8 - 12"))) {
                    return i;
                }
            }
            return 999;
        }));

        // ORDEN DE ROTACIÓN: 1, 2, 3, Más de 3
        List<String> ordenRotacion = List.of("1", "2", "3", "Más de 3");
        freqCantidadEmpleos.sort(Comparator.comparingInt(m -> {
            String cat = m.get("categoria").toString().trim();
            for (int i = 0; i < ordenRotacion.size(); i++) {
                if (cat.equalsIgnoreCase(ordenRotacion.get(i))) {
                    return i;
                }
            }
            return 999;
        }));

        add(new PanelInsercionLaboral(
                madurez, tasaInsercion,
                freqSituacion, freqTiempoEmpleo, freqPrimerEmpleo, freqCantidadEmpleos));

        // ─── 4. BLOQUE 3: GRÁFICOS DE DISTRIBUCIÓN ───────────────────────────────
        add(seccionTitulo("Distribución Gráfica de Indicadores de Empleabilidad"));

        long nTiempo = freqTiempoEmpleo.stream().mapToLong(f -> ((Number) f.get("cantidad")).longValue()).sum();
        long nRotacion = freqCantidadEmpleos.stream().mapToLong(f -> ((Number) f.get("cantidad")).longValue()).sum();

        Div gridBarras = gridResponsivo();
        gridBarras.add(
            seccionGrafico("Situación Laboral Actual",
                graficos.barras(datos, RespuestaTitulado::getSituacionLaboral, "Situación Laboral")),
            seccionGrafico("Tiempo para Conseguir Primer Empleo (n = " + nTiempo + ")",
                graficos.barrasDesdeFrecuencia(freqTiempoEmpleo, "Tiempo de Inserción")),
            seccionGrafico("Rotación Laboral (Cantidad de Empleos) (n = " + nRotacion + ")",
                graficos.barrasDesdeFrecuencia(freqCantidadEmpleos, "Cantidad de Empleos"))
        );
        add(gridBarras);
    }

    private Div buildEncabezado() {
        Div contenedor = new Div();
        contenedor.getStyle().set("margin-bottom", "0.5rem");

        H2 titulo = new H2("Perfil y Empleabilidad — Historia de Usuario 1");
        titulo.getStyle()
                .set("margin", "0 0 0.3rem 0")
                .set("color", "#003366")
                .set("font-size", "1.6rem")
                .set("font-weight", "700");

        Span subtitulo = new Span("Estudio analítico sobre inserción en el mercado, antigüedad laboral, rotación de empleos y tiempo de búsqueda de los graduados.");
        subtitulo.getStyle()
                .set("color", "#475569")
                .set("font-size", "0.95rem");

        contenedor.add(titulo, subtitulo);
        return contenedor;
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

    private Div gridResponsivo() {
        Div div = new Div();
        div.getStyle()
                .set("display", "grid")
                .set("grid-template-columns", "repeat(auto-fit, minmax(320px, 1fr))")
                .set("gap", "1rem")
                .set("width", "100%")
                .set("margin-bottom", "1rem");
        return div;
    }

    private Div seccionGrafico(String titulo, com.vaadin.flow.component.Component grafico) {
        Div card = new Div();
        card.getStyle()
                .set("background", "#f8f9fa")
                .set("border", "1px solid #e2e8f0")
                .set("border-radius", "8px")
                .set("padding", "1.25rem")
                .set("box-shadow", "0 1px 4px rgba(0,0,0,0.06)");

        H3 h = new H3(titulo);
        h.getStyle()
                .set("margin", "0 0 0.75rem 0")
                .set("font-size", "1rem")
                .set("font-weight", "700")
                .set("color", "#003366");

        card.add(h, grafico);
        return card;
    }
}
