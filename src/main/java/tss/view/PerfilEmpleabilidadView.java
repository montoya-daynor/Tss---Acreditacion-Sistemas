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
import tss.view.componente.dashboard.PanelInsercionLaboral;
import tss.view.componente.dashboard.PanelKpiEmpleabilidad;

import java.util.List;
import java.util.Map;

// VISTA DE PERFIL Y EMPLEABILIDAD — HU1: INSERCIÓN LABORAL Y TRAYECTORIA PROFESIONAL
@Route(value = "perfil-empleabilidad", layout = MainLayout.class)
@PageTitle("Perfil y Empleabilidad - Análisis UMSS")
public class PerfilEmpleabilidadView extends VerticalLayout {

    public PerfilEmpleabilidadView(RespuestaTituladoService tituladoService, ReporteService reporteService) {
        setPadding(true);
        setSpacing(true);
        setWidthFull();

        List<RespuestaTitulado> datos = tituladoService.listarTodos();
        GraficosFactory graficos     = new GraficosFactory(reporteService);

        // ─── ENCABEZADO ──────────────────────────────────────────────────────────
        add(new H2("💼 Perfil y Empleabilidad de Titulados — HU1"));

        // ─── BLOQUE 1: ESTADÍSTICAS DE ANTIGÜEDAD Y DESEMPLEO ────────────────────
        add(new H3("📈 Estadísticas de Antigüedad Profesional y Período de Desempleo"));
        Map<String, Object> resumenStats = reporteService.calcularResumenAntigüedad(datos);
        add(new PanelKpiEmpleabilidad(resumenStats));

        // ─── BLOQUE 2: SEGMENTACIÓN, INSERCIÓN Y FRECUENCIAS ─────────────────────
        Map<String, Object> madurez      = reporteService.calcularSegmentacionMadurez(datos);
        double tasaInsercion             = reporteService.calcularTasaInsercionActiva(datos);

        List<Map<String, Object>> freqSituacion = reporteService.calcularFrecuenciaRelativa(
                datos, RespuestaTitulado::getSituacionLaboral);
        List<Map<String, Object>> freqTiempoEmpleo = reporteService.calcularFrecuenciaRelativa(
                datos, RespuestaTitulado::getTiempoPrimerEmpleo);
        List<Map<String, Object>> freqPrimerEmpleo = reporteService.calcularFrecuenciaRelativa(
                datos, r -> r.getPrimerEmpleo() != null ? (r.getPrimerEmpleo() ? "SÍ" : "NO") : "Sin dato");
        List<Map<String, Object>> freqCantidadEmpleos = reporteService.calcularFrecuenciaRelativa(
                datos, RespuestaTitulado::getCantidadEmpleos);

        add(new PanelInsercionLaboral(
                madurez, tasaInsercion,
                freqSituacion, freqTiempoEmpleo, freqPrimerEmpleo, freqCantidadEmpleos));

        // ─── BLOQUE 3: GRÁFICOS DE BARRAS ────────────────────────────────────────
        add(new H3("📊 Distribución Visual de Indicadores de Empleabilidad"));

        Div gridBarras = gridResponsivo();
        gridBarras.add(
            seccion("Situación Laboral Actual",
                graficos.barras(datos, RespuestaTitulado::getSituacionLaboral, "Situación Laboral")),
            seccion("Tiempo para Conseguir Primer Empleo",
                graficos.barras(datos, RespuestaTitulado::getTiempoPrimerEmpleo, "Tiempo de Inserción")),
            seccion("Rotación Laboral (Cantidad de Empleos)",
                graficos.barras(datos, RespuestaTitulado::getCantidadEmpleos, "Cantidad de Empleos"))
        );
        add(gridBarras);
    }

    private Div gridResponsivo() {
        Div div = new Div();
        div.getStyle()
                .set("display", "grid")
                .set("grid-template-columns", "repeat(auto-fit, minmax(320px, 1fr))")
                .set("gap", "1rem")
                .set("width", "100%");
        return div;
    }

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
