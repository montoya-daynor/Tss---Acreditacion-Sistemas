package tss.view.componente.dashboard;

import tss.entity.RespuestaTitulado;
import tss.service.ReporteService;
import tss.view.EChartsComponent;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

// FÁBRICA DE GRÁFICOS ECHARTS PARA EL DASHBOARD
public class GraficosFactory {

    private final ReporteService reporteService;

    public GraficosFactory(ReporteService reporteService) {
        this.reporteService = reporteService;
    }

    // GRÁFICO DE BARRAS HORIZONTALES PARA UNA VARIABLE CATEGÓRICA
    public EChartsComponent barras(List<RespuestaTitulado> datos,
            Function<RespuestaTitulado, String> extractor, String titulo) {
        Map<String, Long> freq = reporteService.frecuenciaPor(datos, extractor);
        String cats = freq.keySet().stream()
                .map(k -> "\"" + k.replace("\"", "\\\"") + "\"")
                .collect(Collectors.joining(","));
        String vals = freq.values().stream().map(String::valueOf).collect(Collectors.joining(","));

        String json = """
            {
              "tooltip": {},
              "title": { "text": "%s", "left": "center" },
              "xAxis": { "type": "value" },
              "yAxis": { "type": "category", "data": [%s] },
              "series": [{ "type": "bar", "data": [%s],
                "label": { "show": true, "position": "right" } }]
            }
            """.formatted(titulo, cats, vals);

        EChartsComponent chart = new EChartsComponent();
        chart.setOptions(json);
        return chart;
    }

    // GRÁFICO RADAR COMPARANDO SATISFACCIÓN VS CONCORDANCIA
    public EChartsComponent radar(List<RespuestaTitulado> datos) {
        String[] cats = {"Totalmente de acuerdo", "De acuerdo",
                         "En desacuerdo", "Totalmente en desacuerdo"};

        Map<String, Long> sat  = reporteService.frecuenciaPor(datos, RespuestaTitulado::getSatisfaccionFormacion);
        Map<String, Long> conc = reporteService.frecuenciaPor(datos, RespuestaTitulado::getConcordanciaMercado);
        long maxVal = Math.max(
            sat.values().stream().mapToLong(Long::longValue).max().orElse(1),
            conc.values().stream().mapToLong(Long::longValue).max().orElse(1)
        ) + 1;

        StringBuilder ind = new StringBuilder(), sv = new StringBuilder(), cv = new StringBuilder();
        for (String cat : cats) {
            if (ind.length() > 0) { ind.append(","); sv.append(","); cv.append(","); }
            ind.append("{\"name\":\"").append(cat).append("\",\"max\":").append(maxVal).append("}");
            sv.append(sat.getOrDefault(cat, 0L));
            cv.append(conc.getOrDefault(cat, 0L));
        }

        String json = """
            {
              "tooltip": {},
              "legend": { "data": ["Satisfacción", "Concordancia"] },
              "radar": { "indicator": [%s] },
              "series": [{ "type": "radar", "data": [
                { "value": [%s], "name": "Satisfacción" },
                { "value": [%s], "name": "Concordancia" }
              ]}]
            }
            """.formatted(ind, sv, cv);

        EChartsComponent chart = new EChartsComponent();
        chart.setOptions(json);
        return chart;
    }

    // MAPA DE CALOR CRUZANDO SATISFACCIÓN VS ÁREA DE INTERÉS
    public EChartsComponent mapaCalor(List<RespuestaTitulado> datos) {
        String[] filas    = {"Totalmente en desacuerdo", "En desacuerdo",
                             "De acuerdo", "Totalmente de acuerdo"};
        String[] columnas = {"IA", "Robótica", "Ingeniería de Software",
                             "Base de Datos", "Redes y Seguridad"};

        Map<String, Map<String, Long>> matriz = reporteService.matrizCruzada(datos,
                RespuestaTitulado::getSatisfaccionFormacion,
                RespuestaTitulado::getAreaInteresPosgrado);

        StringBuilder puntos = new StringBuilder();
        for (int i = 0; i < filas.length; i++)
            for (int j = 0; j < columnas.length; j++) {
                long val = matriz.getOrDefault(filas[i], Map.of()).getOrDefault(columnas[j], 0L);
                if (puntos.length() > 0) puntos.append(",");
                puntos.append("[").append(j).append(",").append(i).append(",").append(val).append("]");
            }

        String xCats = Arrays.stream(columnas).map(c -> "\"" + c + "\"").collect(Collectors.joining(","));
        String yCats = Arrays.stream(filas).map(f -> "\"" + f + "\"").collect(Collectors.joining(","));

        String json = """
            {
              "tooltip": { "position": "top" },
              "grid": { "height": "60%%", "top": "10%%" },
              "xAxis": { "type": "category", "data": [%s], "splitArea": { "show": true } },
              "yAxis": { "type": "category", "data": [%s], "splitArea": { "show": true } },
              "visualMap": { "min": 0, "max": 5, "calculable": true,
                "orient": "horizontal", "left": "center", "bottom": "15%%" },
              "series": [{ "type": "heatmap", "data": [%s],
                "label": { "show": true },
                "emphasis": { "itemStyle": { "shadowBlur": 10 } } }]
            }
            """.formatted(xCats, yCats, puntos);

        EChartsComponent chart = new EChartsComponent();
        chart.setOptions(json);
        return chart;
    }
}
