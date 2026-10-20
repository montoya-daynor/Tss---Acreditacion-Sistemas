package tss.service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.stereotype.Service;
import tss.entity.RespuestaTitulado;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ReporteService {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final RespuestaTituladoService tituladoService;

    public ReporteService(RespuestaTituladoService tituladoService) {
        this.tituladoService = tituladoService;
    }

    // CALCULA FRECUENCIA ABSOLUTA DE UN CAMPO CATEGÓRICO
    public Map<String, Long> frecuenciaPor(List<RespuestaTitulado> datos,
            Function<RespuestaTitulado, String> extractor) {
        return datos.stream()
                .filter(r -> extractor.apply(r) != null)
                .collect(Collectors.groupingBy(extractor, LinkedHashMap::new, Collectors.counting()));
    }

    // GENERA MATRIZ DE FRECUENCIA CRUZADA ENTRE DOS VARIABLES
    public Map<String, Map<String, Long>> matrizCruzada(List<RespuestaTitulado> datos,
            Function<RespuestaTitulado, String> filaFn,
            Function<RespuestaTitulado, String> columnaFn) {
        Map<String, Map<String, Long>> matriz = new LinkedHashMap<>();
        datos.stream()
                .filter(r -> filaFn.apply(r) != null && columnaFn.apply(r) != null)
                .forEach(r -> matriz
                        .computeIfAbsent(filaFn.apply(r), k -> new LinkedHashMap<>())
                        .merge(columnaFn.apply(r), 1L, Long::sum));
        return matriz;
    }

    // NORMALIZACIÓN BÁSICA DE TEXTO: TRIM, MINÚSCULAS Y SIN TILDES
    public static String normalizarTextoBasico(String s) {
        if (s == null) return null;
        String t = s.trim().toLowerCase();
        t = java.text.Normalizer.normalize(t, java.text.Normalizer.Form.NFD);
        t = t.replaceAll("\\p{M}", "");
        return t.replaceAll("\\s+", " ");
    }

    // NORMALIZACIÓN DE SATISFACCIÓN SEGÚN REGLA
    public String normalizarSatisfaccionFormacion(String s) {
        String t = normalizarTextoBasico(s);
        if (t == null || t.isEmpty() || t.equals("-") || t.equals("n/a")) return null;
        if (t.contains("totalmente") && t.contains("desacuerdo")) {
            return "Totalmente en desacuerdo";
        }
        if (t.contains("totalmente") && t.contains("acuerdo")) {
            return "Totalmente de acuerdo";
        }
        if (t.contains("desacuerdo")) {
            return "En desacuerdo";
        }
        if (t.contains("acuerdo") || t.contains("satisfecho")) {
            return "De acuerdo";
        }
        return null;
    }

    // NORMALIZACIÓN DE ÁREAS SEGÚN REGLA
    public String normalizarAreaInteresPosgrado(String s) {
        String t = normalizarTextoBasico(s);
        if (t == null || t.isEmpty() || t.equals("-") || t.equals("n/a")) return null;
        if (t.equals("ia") || t.contains("inteligencia artificial")) {
            return "IA";
        }
        if (t.contains("software")) {
            return "Ingeniería de Software";
        }
        if (t.contains("red") || t.contains("seguridad")) {
            return "Redes y Seguridad";
        }
        if (t.contains("base") || t.contains("dato")) {
            return "Base de Datos";
        }
        if (t.contains("robotica")) {
            return "Robótica";
        }
        return null;
    }

    // MATRIZ CRUZADA NORMALIZADA: SATISFACCIÓN VS ÁREA DE INTERÉS (n = 123)
    public Map<String, Map<String, Long>> calcularMatrizSatisfaccionVsArea(List<RespuestaTitulado> datos) {
        Map<String, Map<String, Long>> matriz = new LinkedHashMap<>();
        for (RespuestaTitulado r : datos) {
            String sat = normalizarSatisfaccionFormacion(r.getSatisfaccionFormacion());
            String area = normalizarAreaInteresPosgrado(r.getAreaInteresPosgrado());
            if (sat != null && area != null) {
                matriz.computeIfAbsent(sat, k -> new LinkedHashMap<>())
                      .merge(area, 1L, Long::sum);
            }
        }
        return matriz;
    }

    // CÁLCULO DE MEDIA, MEDIANA Y DESVIACIÓN ESTÁNDAR PARA UNA SERIE DE DATOS
    public MetricaEstadistica calcularMetrica(String nombreVariable, List<Double> valores) {
        if (valores == null || valores.isEmpty()) {
            return new MetricaEstadistica(nombreVariable, 0.0, 0.0, 0.0, 0.0, 0.0, 0);
        }
        int n = valores.size();
        List<Double> ordenados = valores.stream().sorted().toList();

        double suma = ordenados.stream().mapToDouble(Double::doubleValue).sum();
        double media = suma / n;

        double mediana;
        if (n % 2 == 1) {
            mediana = ordenados.get(n / 2);
        } else {
            mediana = (ordenados.get((n / 2) - 1) + ordenados.get(n / 2)) / 2.0;
        }

        double varianza = 0.0;
        if (n > 1) {
            double sumCuadrados = 0.0;
            for (double v : ordenados) {
                sumCuadrados += Math.pow(v - media, 2);
            }
            varianza = sumCuadrados / (n - 1);
        }
        double desvEstandar = Math.sqrt(varianza);
        double min = ordenados.get(0);
        double max = ordenados.get(n - 1);

        return new MetricaEstadistica(
                nombreVariable,
                redondear(media, 2),
                redondear(mediana, 2),
                redondear(desvEstandar, 2),
                redondear(min, 2),
                redondear(max, 2),
                n
        );
    }

    // GENERA EL CONJUNTO DE MÉTRICAS ESTADÍSTICAS DEL ESTUDIO
    public List<MetricaEstadistica> calcularEstadisticasDescriptivas(List<RespuestaTitulado> datos) {
        List<MetricaEstadistica> metricas = new ArrayList<>();

        // 1. Años de Vida Profesional
        List<Double> aniosProf = datos.stream()
                .map(RespuestaTitulado::getAniosVidaProfesionalDecimalCalculado)
                .filter(Objects::nonNull)
                .toList();
        metricas.add(calcularMetrica("Años de Vida Profesional", aniosProf));

        // 2. Años Desempleado / Búsqueda
        List<Double> aniosDesemp = datos.stream()
                .map(RespuestaTitulado::getAniosDesempleadoDecimalCalculado)
                .filter(Objects::nonNull)
                .toList();
        metricas.add(calcularMetrica("Años de Búsqueda Laboral", aniosDesemp));

        // 3. Grado de Satisfacción con la Formación (Escala 1 a 4)
        List<Double> satisfaccion = datos.stream()
                .map(RespuestaTitulado::getSatisfaccionFormacion)
                .filter(Objects::nonNull)
                .map(this::convertirLikertANumero)
                .filter(Objects::nonNull)
                .toList();
        metricas.add(calcularMetrica("Satisfacción con Formación (1-4)", satisfaccion));

        // 4. Concordancia Formación vs. Mercado (Escala 1 a 4)
        List<Double> concordancia = datos.stream()
                .map(RespuestaTitulado::getConcordanciaMercado)
                .filter(Objects::nonNull)
                .map(this::convertirLikertANumero)
                .filter(Objects::nonNull)
                .toList();
        metricas.add(calcularMetrica("Concordancia Formación vs. Mercado (1-4)", concordancia));

        return metricas;
    }

    // CONVERSIÓN DE RESPUESTAS TIPO LIKERT A VALOR NUMÉRICO PARA EL CÁLCULO
    private Double convertirLikertANumero(String texto) {
        if (texto == null) return null;
        return switch (texto.trim()) {
            case "Totalmente en desacuerdo" -> 1.0;
            case "En desacuerdo" -> 2.0;
            case "De acuerdo" -> 3.0;
            case "Totalmente de acuerdo" -> 4.0;
            default -> null;
        };
    }

    private double redondear(double valor, int decimales) {
        double factor = Math.pow(10, decimales);
        return Math.round(valor * factor) / factor;
    }

    // ─── HU1: PERFIL Y EMPLEABILIDAD ────────────────────────────────────────────

    // RESUMEN ESTADÍSTICO DE ANTIGÜEDAD PROFESIONAL Y DESEMPLEO PARA LOS 4 KPI CARDS
    public Map<String, Object> calcularResumenAntigüedad(List<RespuestaTitulado> datos) {
        Map<String, Object> resultado = new LinkedHashMap<>();

        int total = datos.size();
        resultado.put("totalEncuestados", total);

        List<Double> aniosProf = datos.stream()
                .map(RespuestaTitulado::getAniosVidaProfesionalDecimalCalculado)
                .filter(Objects::nonNull)
                .sorted().toList();

        List<Double> mesesDesemp = datos.stream()
                .map(r -> {
                    if (r.getMesesDesempleo() != null) return r.getMesesDesempleo().doubleValue();
                    if (r.getAniosDesempleoDecimal() != null) return r.getAniosDesempleoDecimal() * 12.0;
                    return null;
                })
                .filter(Objects::nonNull)
                .sorted().toList();

        List<Double> aniosDesemp = mesesDesemp.stream()
                .map(m -> m / 12.0)
                .sorted().toList();

        int nDesemp = mesesDesemp.size();
        double medianaMeses = 0.0;
        double medianaDesempAnios = 0.0;
        if (nDesemp > 0) {
            medianaMeses = nDesemp % 2 == 1
                    ? mesesDesemp.get(nDesemp / 2)
                    : (mesesDesemp.get((nDesemp / 2) - 1) + mesesDesemp.get(nDesemp / 2)) / 2.0;
            // Calculada desde meses sin redondeos intermedios: 10.5 / 12.0 = 0.875
            medianaDesempAnios = medianaMeses / 12.0;
        }

        resultado.put("mediaAntigüedad",    aniosProf.isEmpty()  ? 0.0 : redondear(aniosProf.stream().mapToDouble(Double::doubleValue).average().orElse(0), 2));
        resultado.put("medianaAntigüedad",  calcularMediana(aniosProf));
        resultado.put("desvAntigüedad",     calcularDesv(aniosProf));
        resultado.put("nAntigüedad",        aniosProf.size());

        resultado.put("mediaDesempleo",        aniosDesemp.isEmpty() ? 0.0 : redondear(aniosDesemp.stream().mapToDouble(Double::doubleValue).average().orElse(0), 2));
        resultado.put("medianaDesempleo",      redondear(medianaDesempAnios, 2));
        resultado.put("medianaDesempleoMeses", redondear(medianaMeses, 1));
        resultado.put("desvDesempleo",         calcularDesv(aniosDesemp));
        resultado.put("nDesempleo",            nDesemp);

        return resultado;
    }

    // SEGMENTACIÓN POR MADUREZ PROFESIONAL (JUNIOR ≤3 AÑOS / SENIOR >3 AÑOS)
    public Map<String, Object> calcularSegmentacionMadurez(List<RespuestaTitulado> datos) {
        Map<String, Object> resultado = new LinkedHashMap<>();
        int total = datos.size();

        long junior = datos.stream()
                .filter(r -> r.getAniosVidaProfesional() != null && r.getAniosVidaProfesional() <= 3)
                .count();
        long senior = datos.stream()
                .filter(r -> r.getAniosVidaProfesional() != null && r.getAniosVidaProfesional() > 3)
                .count();
        long sinDato = total - junior - senior;

        double pctJunior = total > 0 ? redondear(junior * 100.0 / total, 1) : 0.0;
        double pctSenior = total > 0 ? redondear(senior * 100.0 / total, 1) : 0.0;

        resultado.put("junior",    junior);
        resultado.put("senior",    senior);
        resultado.put("sinDato",   sinDato);
        resultado.put("pctJunior", pctJunior);
        resultado.put("pctSenior", pctSenior);
        resultado.put("total",     total);
        return resultado;
    }

    // TASA DE INSERCIÓN LABORAL ACTIVA (TRABAJANDO + EMPRENDIENDO)
    public double calcularTasaInsercionActiva(List<RespuestaTitulado> datos) {
        long activos = datos.stream()
                .filter(r -> r.getSituacionLaboral() != null && (
                        r.getSituacionLaboral().contains("trabajando en una organización") ||
                        r.getSituacionLaboral().contains("EMPRENDIMIENTO") ||
                        r.getSituacionLaboral().toLowerCase().contains("emprendimiento")))
                .count();
        return datos.isEmpty() ? 0.0 : redondear(activos * 100.0 / datos.size(), 2);
    }

    // DISTRIBUCIÓN CON FRECUENCIA ABSOLUTA Y RELATIVA ORDENADA DE MAYOR A MENOR
    public List<Map<String, Object>> calcularFrecuenciaRelativa(List<RespuestaTitulado> datos,
            Function<RespuestaTitulado, String> extractor) {
        Map<String, Long> freq = frecuenciaPor(datos, extractor);
        long total = freq.values().stream().mapToLong(Long::longValue).sum();

        return freq.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .map(e -> {
                    Map<String, Object> fila = new LinkedHashMap<>();
                    fila.put("categoria", e.getKey());
                    fila.put("cantidad",  e.getValue());
                    fila.put("porcentaje", total > 0 ? redondear(e.getValue() * 100.0 / total, 1) : 0.0);
                    return fila;
                })
                .collect(Collectors.toList());
    }

    // AUXILIARES MATEMÁTICOS INTERNOS
    private double calcularMediana(List<Double> ordenados) {
        if (ordenados.isEmpty()) return 0.0;
        int n = ordenados.size();
        return n % 2 == 1
                ? redondear(ordenados.get(n / 2), 2)
                : redondear((ordenados.get((n / 2) - 1) + ordenados.get(n / 2)) / 2.0, 2);
    }

    private double calcularDesv(List<Double> ordenados) {
        if (ordenados.size() < 2) return 0.0;
        double media = ordenados.stream().mapToDouble(Double::doubleValue).average().orElse(0);
        double sumCuad = ordenados.stream().mapToDouble(v -> Math.pow(v - media, 2)).sum();
        return redondear(Math.sqrt(sumCuad / (ordenados.size() - 1)), 2);
    }

    // ─── HU2: DEMANDA DE POSGRADO ───────────────────────────────────────────────

    // RESUMEN DE KPIS PARA DEMANDA DE POSGRADO
    public Map<String, Object> calcularResumenDemandaPosgrado(List<RespuestaTitulado> datos) {
        Map<String, Object> kpis = new LinkedHashMap<>();
        int total = datos.size();

        // 1. % Interés en Posgrado
        long interesados = datos.stream()
                .filter(r -> Boolean.TRUE.equals(r.getInteresadoPosgrado()))
                .count();
        double pctInteres = total > 0 ? redondear(interesados * 100.0 / total, 1) : 0.0;

        // 2. % con Estudios Previos
        long conPrevios = datos.stream()
                .filter(r -> Boolean.TRUE.equals(r.getRealizoPosgrado()))
                .count();
        double pctPrevios = total > 0 ? redondear(conPrevios * 100.0 / total, 1) : 0.0;

        // 3. Área Más Demandada (Top 1)
        List<Map<String, Object>> freqAreas = calcularFrecuenciaRelativa(datos, RespuestaTitulado::getAreaInteresPosgrado);
        String topArea = "-";
        long topAreaCant = 0;
        double topAreaPct = 0.0;
        if (!freqAreas.isEmpty()) {
            topArea = (String) freqAreas.get(0).get("categoria");
            topAreaCant = (long) freqAreas.get(0).get("cantidad");
            topAreaPct = (double) freqAreas.get(0).get("porcentaje");
        }

        // 4. Nivel Más Buscado
        List<Map<String, Object>> freqNivel = calcularFrecuenciaRelativa(datos, RespuestaTitulado::getNivelPosgrado);
        String topNivel = "-";
        long topNivelCant = 0;
        double topNivelPct = 0.0;
        if (!freqNivel.isEmpty()) {
            topNivel = (String) freqNivel.get(0).get("categoria");
            topNivelCant = (long) freqNivel.get(0).get("cantidad");
            topNivelPct = (double) freqNivel.get(0).get("porcentaje");
        }

        kpis.put("total", total);
        kpis.put("interesados", interesados);
        kpis.put("pctInteres", pctInteres);
        kpis.put("conPrevios", conPrevios);
        kpis.put("pctPrevios", pctPrevios);
        kpis.put("topArea", topArea);
        kpis.put("topAreaCant", topAreaCant);
        kpis.put("topAreaPct", topAreaPct);
        kpis.put("topNivel", topNivel);
        kpis.put("topNivelCant", topNivelCant);
        kpis.put("topNivelPct", topNivelPct);

        return kpis;
    }

    // TABLA DE FRECUENCIAS POR ÁREA TECNOLÓGICA CON PORCENTAJE ACUMULADO
    public List<Map<String, Object>> calcularTablaAreaConAcumulado(List<RespuestaTitulado> datos) {
        List<Map<String, Object>> filas = calcularFrecuenciaRelativa(datos, RespuestaTitulado::getAreaInteresPosgrado);
        double acumulado = 0.0;
        boolean anteriorSupero70 = false;

        for (Map<String, Object> fila : filas) {
            double pct = (double) fila.get("porcentaje");
            acumulado = redondear(acumulado + pct, 1);
            if (acumulado > 100.0) acumulado = 100.0;
            fila.put("porcentajeAcumulado", acumulado);

            // Resaltar áreas que juntas alcanzan o superan el 70% (principio de Pareto)
            boolean esFoco = !anteriorSupero70;
            fila.put("esFoco70", esFoco);
            if (acumulado >= 70.0) {
                anteriorSupero70 = true;
            }
        }
        return filas;
    }

    // PÁRRAFO DE CONCLUSIÓN GENERADO AUTOMÁTICAMENTE PARA HU2
    public String generarConclusionHU2(List<RespuestaTitulado> datos, List<Map<String, Object>> tablaArea) {
        if (datos.isEmpty()) {
            return "No se registran datos suficientes de titulados para generar el análisis de demanda de posgrado.";
        }
        Map<String, Object> kpis = calcularResumenDemandaPosgrado(datos);
        int total = (int) kpis.get("total");
        long interesados = (long) kpis.get("interesados");
        double pctInteres = (double) kpis.get("pctInteres");
        long conPrevios = (long) kpis.get("conPrevios");
        double pctPrevios = (double) kpis.get("pctPrevios");

        List<String> areasFoco = new ArrayList<>();
        double pctFocoTotal = 0.0;
        for (Map<String, Object> fila : tablaArea) {
            if (Boolean.TRUE.equals(fila.get("esFoco70"))) {
                areasFoco.add(fila.get("categoria") + " (" + fila.get("porcentaje") + "%)");
                pctFocoTotal = (double) fila.get("porcentajeAcumulado");
            }
        }

        String textoAreas = areasFoco.isEmpty() ? "las áreas registradas" : String.join(", ", areasFoco);
        String topNivel = (String) kpis.get("topNivel");

        return String.format(
            "Con base en el levantamiento de información realizado sobre un universo de %d titulados, el %.1f%% (%d encuestados) manifiesta interés en realizar estudios de posgrado, mientras que el %.1f%% (%d encuestados) ya cuenta con estudios previos. La demanda formativa se concentra prioritariamente en las áreas de %s, las cuales en conjunto representan el %.1f%% del total de preferencias (concentración crítica superior al 70%% según el Principio de Pareto). Respecto al grado formativo, el nivel más solicitado es %s. Se recomienda priorizar la formulación y apertura de programas académicos en estas disciplinas estratégicas para maximizar la captación y pertinencia formativa en la FCyT.",
            total, pctInteres, interesados, pctPrevios, conPrevios, textoAreas, pctFocoTotal, topNivel
        );
    }

    // ─── HU3: FINANCIAMIENTO Y BECAS (CHI-CUADRADA) ───────────────────────────

    public String normalizarNivelPosgrado(String raw) {
        if (raw == null || raw.isBlank()) return "Otros";
        String r = raw.trim().toUpperCase();
        if (r.contains("DIPLOMADO")) return "Diplomado";
        if (r.contains("ESPECIALIDAD")) return "Especialidad";
        if (r.contains("MAESTR")) return "Maestría";
        if (r.contains("DOCTOR")) return "Doctorado";
        if (r.contains("POS DOCTOR") || r.contains("POST")) return "Pos Doctorado";
        return raw;
    }

    public String normalizarFuenteFinanciamiento(String raw) {
        if (raw == null || raw.isBlank()) return "Otros";
        String r = raw.trim().toUpperCase();
        if (r.contains("RECURSOS PROPIOS") || r.contains("PROPIOS")) return "Recursos Propios";
        if (r.contains("PARCIALMENTE") || r.contains("PARCIAL")) return "Beca Parcial";
        if (r.contains("TOTAL") && r.contains("BECA")) return "Beca Total";
        if (r.contains("CREDITO") || r.contains("CRÉDITO") || r.contains("PATROCINIO") || r.contains("EMPRESARIAL"))
            return "Crédito / Patrocinio";
        if (r.contains("BECA")) return "Beca Total";
        return "Crédito / Patrocinio";
    }

    // DISTRIBUCIÓN SIMPLE DE FUENTES DE FINANCIAMIENTO
    public List<Map<String, Object>> calcularDistribucionFuentes(List<RespuestaTitulado> datos) {
        List<RespuestaTitulado> conFuente = datos.stream()
                .filter(r -> r.getFinanciamientoPosgrado() != null && !r.getFinanciamientoPosgrado().isBlank())
                .collect(Collectors.toList());

        int total = conFuente.size();
        Map<String, Long> freq = new LinkedHashMap<>();

        // Inicializamos las fuentes canónicas de la HU3
        String[] orden = {"Recursos Propios", "Beca Parcial", "Beca Total", "Crédito / Patrocinio"};
        for (String f : orden) freq.put(f, 0L);

        for (RespuestaTitulado r : conFuente) {
            String norm = normalizarFuenteFinanciamiento(r.getFinanciamientoPosgrado());
            freq.merge(norm, 1L, Long::sum);
        }

        List<Map<String, Object>> lista = new ArrayList<>();
        for (Map.Entry<String, Long> entry : freq.entrySet()) {
            if (entry.getValue() > 0 || Arrays.asList(orden).contains(entry.getKey())) {
                Map<String, Object> fila = new LinkedHashMap<>();
                fila.put("categoria", entry.getKey());
                fila.put("cantidad", entry.getValue());
                fila.put("porcentaje", total > 0 ? redondear(entry.getValue() * 100.0 / total, 1) : 0.0);
                lista.add(fila);
            }
        }
        return lista;
    }

    // TABLA DE CONTINGENCIA CRUZADA Y CÁLCULO DE CHI-CUADRADA
    public Map<String, Object> calcularTablaContingenciaChiCuadrada(List<RespuestaTitulado> datos) {
        List<String> filasDefinidas = Arrays.asList("Diplomado", "Especialidad", "Maestría", "Doctorado");
        List<String> colsDefinidas = Arrays.asList("Recursos Propios", "Beca Parcial", "Beca Total", "Crédito / Patrocinio");

        Map<String, Map<String, Long>> observados = new LinkedHashMap<>();
        for (String f : filasDefinidas) {
            Map<String, Long> colMap = new LinkedHashMap<>();
            for (String c : colsDefinidas) colMap.put(c, 0L);
            observados.put(f, colMap);
        }

        // Poblamos con los datos observados
        long n = 0;
        for (RespuestaTitulado r : datos) {
            if (r.getNivelPosgrado() != null && !r.getNivelPosgrado().isBlank()
                    && r.getFinanciamientoPosgrado() != null && !r.getFinanciamientoPosgrado().isBlank()) {
                String fNorm = normalizarNivelPosgrado(r.getNivelPosgrado());
                String cNorm = normalizarFuenteFinanciamiento(r.getFinanciamientoPosgrado());

                observados.computeIfAbsent(fNorm, k -> new LinkedHashMap<>())
                          .merge(cNorm, 1L, Long::sum);
                n++;
            }
        }

        // Totales por fila y columna
        Map<String, Long> totalFila = new LinkedHashMap<>();
        for (String f : observados.keySet()) {
            long sumF = observados.get(f).values().stream().mapToLong(Long::longValue).sum();
            totalFila.put(f, sumF);
        }

        Map<String, Long> totalColumna = new LinkedHashMap<>();
        for (String c : colsDefinidas) {
            long sumC = 0;
            for (String f : observados.keySet()) {
                sumC += observados.get(f).getOrDefault(c, 0L);
            }
            totalColumna.put(c, sumC);
        }

        // Frecuencias esperadas y Chi-cuadrada
        Map<String, Map<String, Double>> esperados = new LinkedHashMap<>();
        double chiCuadrada = 0.0;

        int filasActivas = (int) totalFila.values().stream().filter(v -> v > 0).count();
        int colsActivas = (int) totalColumna.values().stream().filter(v -> v > 0).count();
        int gl = Math.max(1, (Math.max(1, filasActivas) - 1) * (Math.max(1, colsActivas) - 1));

        for (String f : observados.keySet()) {
            long rf = totalFila.getOrDefault(f, 0L);
            Map<String, Double> espRow = new LinkedHashMap<>();
            for (String c : colsDefinidas) {
                long cc = totalColumna.getOrDefault(c, 0L);
                double eij = n > 0 ? (rf * (double) cc) / n : 0.0;
                espRow.put(c, redondear(eij, 2));

                if (eij > 0) {
                    long oij = observados.get(f).getOrDefault(c, 0L);
                    chiCuadrada += Math.pow(oij - eij, 2) / eij;
                }
            }
            esperados.put(f, espRow);
        }

        chiCuadrada = redondear(chiCuadrada, 2);
        double pValor = calcularPValorChiCuadrada(chiCuadrada, gl);
        pValor = redondear(pValor, 4);
        boolean existeDependencia = pValor < 0.05;

        Map<String, Object> resultado = new LinkedHashMap<>();
        resultado.put("filas", new ArrayList<>(observados.keySet()));
        resultado.put("columnas", colsDefinidas);
        resultado.put("observados", observados);
        resultado.put("esperados", esperados);
        resultado.put("totalFila", totalFila);
        resultado.put("totalColumna", totalColumna);
        resultado.put("totalGeneral", n);
        resultado.put("chiCuadrada", chiCuadrada);
        resultado.put("gradosLibertad", gl);
        resultado.put("pValor", pValor);
        resultado.put("existeDependencia", existeDependencia);
        resultado.put("conclusionPrueba", existeDependencia
                ? "Existe dependencia financiera (p < 0.05)"
                : "Son independientes (p ≥ 0.05)");

        return resultado;
    }

    // RESUMEN DE KPIS SUPERIORES PARA HU3
    public Map<String, Object> calcularResumenFinanciamiento(List<RespuestaTitulado> datos) {
        Map<String, Object> analisisChi = calcularTablaContingenciaChiCuadrada(datos);
        long total = (long) analisisChi.get("totalGeneral");

        List<Map<String, Object>> dist = calcularDistribucionFuentes(datos);

        long autofinanciados = 0;
        double pctAutofinanciamiento = 0.0;
        long conBeca = 0;
        double pctCoberturaBeca = 0.0;

        for (Map<String, Object> item : dist) {
            String cat = (String) item.get("categoria");
            long cant = ((Number) item.get("cantidad")).longValue();
            double pct = ((Number) item.get("porcentaje")).doubleValue();

            if ("Recursos Propios".equalsIgnoreCase(cat)) {
                autofinanciados = cant;
                pctAutofinanciamiento = pct;
            } else if (cat.toLowerCase().contains("beca")) {
                conBeca += cant;
                pctCoberturaBeca += pct;
            }
        }
        pctCoberturaBeca = redondear(pctCoberturaBeca, 1);

        Map<String, Object> kpis = new LinkedHashMap<>();
        kpis.put("total", total);
        kpis.put("autofinanciados", autofinanciados);
        kpis.put("pctAutofinanciamiento", pctAutofinanciamiento);
        kpis.put("alertaAutofinanciamiento", pctAutofinanciamiento > 70.0);
        kpis.put("conBeca", conBeca);
        kpis.put("pctCoberturaBeca", pctCoberturaBeca);
        kpis.put("chiCuadrada", analisisChi.get("chiCuadrada"));
        kpis.put("gradosLibertad", analisisChi.get("gradosLibertad"));
        kpis.put("pValor", analisisChi.get("pValor"));
        kpis.put("existeDependencia", analisisChi.get("existeDependencia"));
        kpis.put("conclusionPrueba", analisisChi.get("conclusionPrueba"));

        return kpis;
    }

    // INTERPRETACIÓN AUTOMÁTICA DEL DIAGNÓSTICO HU3
    public String generarDiagnosticoHU3(Map<String, Object> kpis) {
        long total = ((Number) kpis.get("total")).longValue();
        if (total == 0) {
            return "No se registran datos suficientes de fuentes de financiamiento y nivel de posgrado para calcular la prueba Chi-Cuadrada.";
        }

        double pctAuto = ((Number) kpis.get("pctAutofinanciamiento")).doubleValue();
        double chi2 = ((Number) kpis.get("chiCuadrada")).doubleValue();
        int gl = ((Number) kpis.get("gradosLibertad")).intValue();
        double pVal = ((Number) kpis.get("pValor")).doubleValue();
        boolean dep = Boolean.TRUE.equals(kpis.get("existeDependencia"));

        String nivelRestriccion = pctAuto > 70.0 ? "una alta vulnerabilidad económica y fuerte restricción" : "una moderada dependencia";
        String resultadoChi = dep
                ? String.format("se confirma dependencia financiera en la muestra (p = %.4f < 0.05)", pVal)
                : String.format("las variables resultan estadísticamente independientes (p = %.4f ≥ 0.05)", pVal);

        String accionRecomendada = dep
                ? "lo que justifica gestionar convenios con entidades financieras y ampliar el programa de becas de la FCyT especialmente para niveles de Maestría y Especialidad."
                : "lo que sugiere una distribución equilibrada de fuentes entre los diferentes niveles académicos cursados.";

        return String.format(
            "El %.1f%% de los titulados financia sus estudios con recursos propios, evidenciando %s al acceso de becas y financiamiento externo. Al cruzar el nivel de posgrado con la fuente de financiamiento mediante la prueba Chi-Cuadrada (χ² = %.2f, gl = %d, p = %.4f), %s, %s",
            pctAuto, nivelRestriccion, chi2, gl, pVal, resultadoChi, accionRecomendada
        );
    }

    // ─── ALGORITMOS MATEMÁTICOS PARA CÁLCULO DE VALOR P CHI-CUADRADA ─────────

    public static double calcularPValorChiCuadrada(double chi2, int df) {
        if (df <= 0 || chi2 <= 0.0) return 1.0;
        double a = df / 2.0;
        double x = chi2 / 2.0;
        return Math.max(0.0, Math.min(1.0, gammaQ(a, x)));
    }

    private static double gammaQ(double a, double x) {
        if (x < 0.0 || a <= 0.0) return 1.0;
        if (x == 0.0) return 1.0;
        if (x < a + 1.0) {
            return 1.0 - gammaP_Series(a, x);
        } else {
            return gammaQ_ContinuedFraction(a, x);
        }
    }

    private static double gammaP_Series(double a, double x) {
        double sum = 1.0 / a;
        double del = sum;
        double ap = a;
        for (int n = 1; n <= 100; n++) {
            ap += 1.0;
            del *= x / ap;
            sum += del;
            if (Math.abs(del) < Math.abs(sum) * 1e-12) break;
        }
        return sum * Math.exp(-x + a * Math.log(x) - logGamma(a));
    }

    private static double gammaQ_ContinuedFraction(double a, double x) {
        double b = x + 1.0 - a;
        double c = 1.0 / 1e-30;
        double d = 1.0 / b;
        double h = d;
        for (int i = 1; i <= 100; i++) {
            double an = -i * (i - a);
            b += 2.0;
            d = an * d + b;
            if (Math.abs(d) < 1e-30) d = 1e-30;
            c = b + an / c;
            if (Math.abs(c) < 1e-30) c = 1e-30;
            d = 1.0 / d;
            double del = d * c;
            h *= del;
            if (Math.abs(del - 1.0) < 1e-12) break;
        }
        return h * Math.exp(-x + a * Math.log(x) - logGamma(a));
    }

    private static double logGamma(double x) {
        double[] cof = {
            76.18009172947146,
            -86.50532032941677,
            24.01409824083091,
            -1.231739572450155,
            0.1208650973866179e-2,
            -0.5395239384953e-5
        };
        double y = x;
        double tmp = x + 5.5;
        tmp -= (x + 0.5) * Math.log(tmp);
        double ser = 1.000000000190015;
        for (int j = 0; j <= 5; j++) {
            y += 1.0;
            ser += cof[j] / y;
        }
        return -tmp + Math.log(2.5066282746310005 * ser / x);
    }



    // EXPORTA EXCEL PROFESIONAL CON MÚLTIPLES HOJAS Y ESTILOS INSTITUCIONALES
    public ByteArrayInputStream exportarExcel() throws IOException {
        List<RespuestaTitulado> datos = tituladoService.listarTodos();
        try (XSSFWorkbook wb = new XSSFWorkbook()) {

            // ESTILOS REUTILIZABLES
            XSSFCellStyle estPortada  = estiloPortada(wb);
            XSSFCellStyle estTitulo   = estiloTitulo(wb);
            XSSFCellStyle estHeader   = estiloHeader(wb);
            XSSFCellStyle estDatoPar  = estiloDato(wb, true);
            XSSFCellStyle estDatoImpar = estiloDato(wb, false);
            XSSFCellStyle estFreqH    = estiloFreqHeader(wb);
            XSSFCellStyle estFreqD    = estiloFreqDato(wb);
            XSSFCellStyle estFreqPct  = estiloFreqPct(wb);

            crearHojaPortada(wb, estPortada, datos.size());
            crearHojaDatos(wb, datos, estTitulo, estHeader, estDatoPar, estDatoImpar);
            crearHojaFrecuencia(wb, datos, "Satisfacción Formación",
                    RespuestaTitulado::getSatisfaccionFormacion, estTitulo, estFreqH, estFreqD, estFreqPct);
            crearHojaFrecuencia(wb, datos, "Área de Interés",
                    RespuestaTitulado::getAreaInteresPosgrado, estTitulo, estFreqH, estFreqD, estFreqPct);
            crearHojaFrecuencia(wb, datos, "Financiamiento",
                    RespuestaTitulado::getFinanciamientoPosgrado, estTitulo, estFreqH, estFreqD, estFreqPct);
            crearHojaFrecuencia(wb, datos, "Concordancia Mercado",
                    RespuestaTitulado::getConcordanciaMercado, estTitulo, estFreqH, estFreqD, estFreqPct);
            crearHojaMatriz(wb, datos, estTitulo, estFreqH, estFreqD);
            crearHojaEstadisticas(wb, calcularEstadisticasDescriptivas(datos), estTitulo, estFreqH, estFreqD);

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            wb.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }

    // HOJA 1: PORTADA INSTITUCIONAL
    private void crearHojaPortada(XSSFWorkbook wb, XSSFCellStyle estilo, int total) {
        Sheet s = wb.createSheet("Portada");
        s.setColumnWidth(0, 10000);

        String[][] filas = {
            {"UNIVERSIDAD MAYOR DE SAN SIMÓN"},
            {"Facultad de Ciencias y Tecnología"},
            {"Carrera de Ingeniería en Sistemas"},
            {""},
            {"ANÁLISIS DE MERCADO — TITULADOS"},
            {"Proceso de Autoevaluación y Acreditación ARCUSUR"},
            {""},
            {"Fecha de generación: " + LocalDateTime.now().format(FMT)},
            {"Total de respuestas: " + total},
            {""},
            {"Contenido del reporte:"},
            {"  • Hoja 'Datos Completos'   — Todas las respuestas individuales"},
            {"  • Hoja 'Satisfacción'       — Frecuencia: satisfacción con la formación"},
            {"  • Hoja 'Área de Interés'    — Frecuencia: área de interés de posgrado"},
            {"  • Hoja 'Financiamiento'     — Frecuencia: fuente de financiamiento"},
            {"  • Hoja 'Concordancia'       — Frecuencia: concordancia formación-mercado"},
            {"  • Hoja 'Matriz Cruzada'     — Satisfacción × Área de interés"},
            {"  • Hoja 'Estadísticas'       — Métricas descriptivas: media, mediana y desviación estándar"},
        };

        for (int i = 0; i < filas.length; i++) {
            Row row = s.createRow(i);
            row.setHeightInPoints(22);
            Cell cell = row.createCell(0);
            cell.setCellValue(filas[i][0]);
            if (i < 6 || i == 10) cell.setCellStyle(estilo);
        }
    }

    // HOJA 2: DATOS COMPLETOS CON TODOS LOS CAMPOS
    private void crearHojaDatos(XSSFWorkbook wb, List<RespuestaTitulado> datos,
            XSSFCellStyle estTitulo, XSSFCellStyle estHeader,
            XSSFCellStyle estPar, XSSFCellStyle estImpar) {
        Sheet s = wb.createSheet("Datos Completos");

        // TÍTULO
        Row tRow = s.createRow(0);
        tRow.setHeightInPoints(20);
        Cell tCell = tRow.createCell(0);
        tCell.setCellValue("Respuestas Individuales — Titulados Ingeniería en Sistemas");
        tCell.setCellStyle(estTitulo);
        s.addMergedRegion(new CellRangeAddress(0, 0, 0, 19));

        // ENCABEZADOS
        String[] cols = {
            "ID", "Fecha Registro", "Edad", "Género",
            "Realizó Posgrado", "Nivel Posgrado", "Institución Posgrado", "Financiamiento Posgrado", "Nombre Programa",
            "Interesado Posgrado", "Área Interés", "Institución Interés", "Opinión Posgrado FCyT",
            "Años Vida Prof.", "Años Desempleado", "Situación Laboral", "Primer Empleo",
            "Tiempo 1er Empleo", "Cantidad Empleos", "Satisfacción Formación", "Concordancia Mercado"
        };
        Row hRow = s.createRow(1);
        hRow.setHeightInPoints(18);
        for (int i = 0; i < cols.length; i++) {
            Cell c = hRow.createCell(i);
            c.setCellValue(cols[i]);
            c.setCellStyle(estHeader);
            s.setColumnWidth(i, 5500);
        }

        // DATOS
        int rowNum = 2;
        for (RespuestaTitulado r : datos) {
            Row row = s.createRow(rowNum);
            XSSFCellStyle est = (rowNum % 2 == 0) ? estPar : estImpar;
            setCelda(row, 0,  str(r.getId()), est);
            setCelda(row, 1,  r.getFechaRegistro() != null ? r.getFechaRegistro().format(FMT) : "", est);
            setCelda(row, 2,  str(r.getEdad()), est);
            setCelda(row, 3,  str(r.getGenero()), est);
            setCelda(row, 4,  bool(r.getRealizoPosgrado()), est);
            setCelda(row, 5,  str(r.getNivelPosgrado()), est);
            setCelda(row, 6,  str(r.getInstitucionPosgrado()), est);
            setCelda(row, 7,  str(r.getFinanciamientoPosgrado()), est);
            setCelda(row, 8,  str(r.getNombrePrograma()), est);
            setCelda(row, 9,  bool(r.getInteresadoPosgrado()), est);
            setCelda(row, 10, str(r.getAreaInteresPosgrado()), est);
            setCelda(row, 11, str(r.getInstitucionInteresPosgrado()), est);
            setCelda(row, 12, str(r.getOpinionPosgradoFcyt()), est);
            setCelda(row, 13, str(r.getAniosVidaProfesional()), est);
            setCelda(row, 14, str(r.getAniosDesempleado()), est);
            setCelda(row, 15, str(r.getSituacionLaboral()), est);
            setCelda(row, 16, bool(r.getPrimerEmpleo()), est);
            setCelda(row, 17, str(r.getTiempoPrimerEmpleo()), est);
            setCelda(row, 18, str(r.getCantidadEmpleos()), est);
            setCelda(row, 19, str(r.getSatisfaccionFormacion()), est);
            setCelda(row, 20, str(r.getConcordanciaMercado()), est);
            rowNum++;
        }

        // FILA TOTAL
        Row totRow = s.createRow(rowNum);
        Cell totLabel = totRow.createCell(0);
        totLabel.setCellValue("TOTAL DE RESPUESTAS: " + datos.size());
        totLabel.setCellStyle(estHeader);
        s.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 0, 5));
    }

    // HOJA DE FRECUENCIA PARA UNA VARIABLE CATEGÓRICA
    private void crearHojaFrecuencia(XSSFWorkbook wb, List<RespuestaTitulado> datos,
            String nombre, Function<RespuestaTitulado, String> extractor,
            XSSFCellStyle estTitulo, XSSFCellStyle estHeader,
            XSSFCellStyle estDato, XSSFCellStyle estPct) {
        Sheet s = wb.createSheet(nombre);
        s.setColumnWidth(0, 12000);
        s.setColumnWidth(1, 5000);
        s.setColumnWidth(2, 5000);
        s.setColumnWidth(3, 5000);

        Row tRow = s.createRow(0);
        tRow.setHeightInPoints(20);
        Cell tCell = tRow.createCell(0);
        tCell.setCellValue("Distribución de Frecuencias — " + nombre);
        tCell.setCellStyle(estTitulo);
        s.addMergedRegion(new CellRangeAddress(0, 0, 0, 3));

        Row hRow = s.createRow(1);
        hRow.setHeightInPoints(16);
        for (String[] h : new String[][]{{"0","Categoría"},{"1","Frec. Absoluta"},{"2","Frec. Relativa (%)"},{"3","Frec. Acumulada"}}) {
            Cell c = hRow.createCell(Integer.parseInt(h[0]));
            c.setCellValue(h[1]);
            c.setCellStyle(estHeader);
        }

        Map<String, Long> freq = frecuenciaPor(datos, extractor);
        long total = freq.values().stream().mapToLong(Long::longValue).sum();
        long acum = 0;
        int rowNum = 2;
        for (Map.Entry<String, Long> e : freq.entrySet()) {
            acum += e.getValue();
            Row row = s.createRow(rowNum++);
            Cell cat = row.createCell(0); cat.setCellValue(e.getKey()); cat.setCellStyle(estDato);
            Cell fa  = row.createCell(1); fa.setCellValue(e.getValue()); fa.setCellStyle(estDato);
            Cell fr  = row.createCell(2);
            fr.setCellValue(total > 0 ? Math.round(e.getValue() * 1000.0 / total) / 10.0 : 0);
            fr.setCellStyle(estPct);
            Cell fac = row.createCell(3);
            fac.setCellValue(total > 0 ? Math.round(acum * 1000.0 / total) / 10.0 : 0);
            fac.setCellStyle(estPct);
        }

        // FILA TOTAL
        Row totRow = s.createRow(rowNum);
        Cell lbl = totRow.createCell(0); lbl.setCellValue("TOTAL"); lbl.setCellStyle(estHeader);
        Cell tot = totRow.createCell(1); tot.setCellValue(total); tot.setCellStyle(estHeader);
        Cell pct = totRow.createCell(2); pct.setCellValue(100.0); pct.setCellStyle(estHeader);
    }

    // HOJA DE MATRIZ CRUZADA SATISFACCIÓN × ÁREA DE INTERÉS
    private void crearHojaMatriz(XSSFWorkbook wb, List<RespuestaTitulado> datos,
            XSSFCellStyle estTitulo, XSSFCellStyle estHeader, XSSFCellStyle estDato) {
        Sheet s = wb.createSheet("Matriz Cruzada");

        String[] areas = {"IA", "Robótica", "Ingeniería de Software", "Base de Datos", "Redes de Datos y Seguridad"};
        String[] sats  = {"Totalmente de acuerdo", "De acuerdo", "En desacuerdo", "Totalmente en desacuerdo"};

        Row tRow = s.createRow(0);
        tRow.setHeightInPoints(20);
        Cell tCell = tRow.createCell(0);
        tCell.setCellValue("Matriz Cruzada — Satisfacción con la Formación × Área de Interés de Posgrado");
        tCell.setCellStyle(estTitulo);
        s.addMergedRegion(new CellRangeAddress(0, 0, 0, areas.length + 1));

        // ENCABEZADO DE COLUMNAS
        Row hRow = s.createRow(1);
        Cell corner = hRow.createCell(0);
        corner.setCellValue("Satisfacción \\ Área");
        corner.setCellStyle(estHeader);
        s.setColumnWidth(0, 9000);
        for (int j = 0; j < areas.length; j++) {
            Cell c = hRow.createCell(j + 1);
            c.setCellValue(areas[j]);
            c.setCellStyle(estHeader);
            s.setColumnWidth(j + 1, 6000);
        }
        Cell totH = hRow.createCell(areas.length + 1);
        totH.setCellValue("TOTAL FILA");
        totH.setCellStyle(estHeader);

        Map<String, Map<String, Long>> matriz = matrizCruzada(datos,
                RespuestaTitulado::getSatisfaccionFormacion,
                RespuestaTitulado::getAreaInteresPosgrado);

        long[] totalesCol = new long[areas.length];
        int rowNum = 2;
        for (String sat : sats) {
            Row row = s.createRow(rowNum++);
            Cell lbl = row.createCell(0); lbl.setCellValue(sat); lbl.setCellStyle(estHeader);
            Map<String, Long> cols = matriz.getOrDefault(sat, Map.of());
            long totFila = 0;
            for (int j = 0; j < areas.length; j++) {
                long val = cols.getOrDefault(areas[j], 0L);
                totalesCol[j] += val;
                totFila += val;
                Cell c = row.createCell(j + 1); c.setCellValue(val); c.setCellStyle(estDato);
            }
            Cell tf = row.createCell(areas.length + 1); tf.setCellValue(totFila); tf.setCellStyle(estHeader);
        }

        // FILA TOTALES COLUMNA
        Row totRow = s.createRow(rowNum);
        Cell totLbl = totRow.createCell(0); totLbl.setCellValue("TOTAL COLUMNA"); totLbl.setCellStyle(estHeader);
        long gran = 0;
        for (int j = 0; j < areas.length; j++) {
            gran += totalesCol[j];
            Cell c = totRow.createCell(j + 1); c.setCellValue(totalesCol[j]); c.setCellStyle(estHeader);
        }
        Cell gt = totRow.createCell(areas.length + 1); gt.setCellValue(gran); gt.setCellStyle(estHeader);
    }

    // HOJA DE MÉTRICAS ESTADÍSTICAS: MEDIA, MEDIANA, DESVIACIÓN ESTÁNDAR
    private void crearHojaEstadisticas(XSSFWorkbook wb, List<MetricaEstadistica> metricas,
            XSSFCellStyle estTitulo, XSSFCellStyle estHeader, XSSFCellStyle estDato) {
        Sheet s = wb.createSheet("Estadísticas");
        s.setColumnWidth(0, 11000);
        s.setColumnWidth(1, 4000);
        s.setColumnWidth(2, 4000);
        s.setColumnWidth(3, 4500);
        s.setColumnWidth(4, 3000);
        s.setColumnWidth(5, 3000);
        s.setColumnWidth(6, 3000);

        Row tRow = s.createRow(0);
        tRow.setHeightInPoints(20);
        Cell tCell = tRow.createCell(0);
        tCell.setCellValue("Análisis Estadístico Descriptivo — Media, Mediana y Desviación Estándar");
        tCell.setCellStyle(estTitulo);
        s.addMergedRegion(new CellRangeAddress(0, 0, 0, 6));

        Row hRow = s.createRow(1);
        hRow.setHeightInPoints(16);
        String[] headers = {"Variable Evaluada", "Media (x̄)", "Mediana (Me)", "Desv. Estándar (s)", "Mínimo", "Máximo", "Muestra (N)"};
        for (int i = 0; i < headers.length; i++) {
            Cell c = hRow.createCell(i);
            c.setCellValue(headers[i]);
            c.setCellStyle(estHeader);
        }

        int rowNum = 2;
        for (MetricaEstadistica m : metricas) {
            Row row = s.createRow(rowNum++);
            Cell c0 = row.createCell(0); c0.setCellValue(m.variable()); c0.setCellStyle(estDato);
            Cell c1 = row.createCell(1); c1.setCellValue(m.media()); c1.setCellStyle(estDato);
            Cell c2 = row.createCell(2); c2.setCellValue(m.mediana()); c2.setCellStyle(estDato);
            Cell c3 = row.createCell(3); c3.setCellValue(m.desviacionEstandar()); c3.setCellStyle(estDato);
            Cell c4 = row.createCell(4); c4.setCellValue(m.min()); c4.setCellStyle(estDato);
            Cell c5 = row.createCell(5); c5.setCellValue(m.max()); c5.setCellStyle(estDato);
            Cell c6 = row.createCell(6); c6.setCellValue(m.muestra()); c6.setCellStyle(estDato);
        }
    }

    // EXPORTA CSV COMPLETO CON TODOS LOS CAMPOS Y ESCAPE RFC 4180
    public ByteArrayInputStream exportarCsv() {
        List<RespuestaTitulado> datos = tituladoService.listarTodos();
        StringBuilder sb = new StringBuilder('\uFEFF'); // BOM para Excel
        sb.append("ID,Fecha Registro,Edad,Género,Realizó Posgrado,Nivel Posgrado,")
          .append("Institución Posgrado,Financiamiento Posgrado,Nombre Programa,")
          .append("Interesado Posgrado,Área Interés,Institución Interés,Opinión Posgrado FCyT,")
          .append("Años Vida Prof.,Años Desempleado,Situación Laboral,Primer Empleo,")
          .append("Tiempo 1er Empleo,Cantidad Empleos,Satisfacción Formación,Concordancia Mercado\n");

        for (RespuestaTitulado r : datos) {
            sb.append(csv(r.getId())).append(",")
              .append(csv(r.getFechaRegistro() != null ? r.getFechaRegistro().format(FMT) : "")).append(",")
              .append(csv(r.getEdad())).append(",")
              .append(csv(r.getGenero())).append(",")
              .append(csv(bool(r.getRealizoPosgrado()))).append(",")
              .append(csv(r.getNivelPosgrado())).append(",")
              .append(csv(r.getInstitucionPosgrado())).append(",")
              .append(csv(r.getFinanciamientoPosgrado())).append(",")
              .append(csv(r.getNombrePrograma())).append(",")
              .append(csv(bool(r.getInteresadoPosgrado()))).append(",")
              .append(csv(r.getAreaInteresPosgrado())).append(",")
              .append(csv(r.getInstitucionInteresPosgrado())).append(",")
              .append(csv(r.getOpinionPosgradoFcyt())).append(",")
              .append(csv(r.getAniosVidaProfesional())).append(",")
              .append(csv(r.getAniosDesempleado())).append(",")
              .append(csv(r.getSituacionLaboral())).append(",")
              .append(csv(bool(r.getPrimerEmpleo()))).append(",")
              .append(csv(r.getTiempoPrimerEmpleo())).append(",")
              .append(csv(r.getCantidadEmpleos())).append(",")
              .append(csv(r.getSatisfaccionFormacion())).append(",")
              .append(csv(r.getConcordanciaMercado())).append("\n");
        }
        return new ByteArrayInputStream(sb.toString().getBytes(StandardCharsets.UTF_8));
    }

    // HELPERS
    private void setCelda(Row row, int col, String val, XSSFCellStyle est) {
        Cell c = row.createCell(col); c.setCellValue(val); c.setCellStyle(est);
    }
    private String str(Object v)    { return v != null ? v.toString() : ""; }
    private String bool(Boolean v)  { return v == null ? "" : (v ? "Sí" : "No"); }
    private String csv(Object v)    {
        if (v == null) return "";
        String s = v.toString();
        if (s.contains(",") || s.contains("\"") || s.contains("\n"))
            return "\"" + s.replace("\"", "\"\"") + "\"";
        return s;
    }

    // DEFINICIONES DE ESTILOS EXCEL
    private XSSFCellStyle estiloPortada(XSSFWorkbook wb) {
        XSSFCellStyle s = wb.createCellStyle();
        XSSFFont f = wb.createFont();
        f.setBold(true); f.setFontHeightInPoints((short) 14);
        f.setColor(new XSSFColor(new byte[]{(byte)0, (byte)51, (byte)102}, null));
        s.setFont(f);
        return s;
    }
    private XSSFCellStyle estiloTitulo(XSSFWorkbook wb) {
        XSSFCellStyle s = wb.createCellStyle();
        s.setFillForegroundColor(new XSSFColor(new byte[]{(byte)0, (byte)51, (byte)102}, null));
        s.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        XSSFFont f = wb.createFont();
        f.setBold(true); f.setFontHeightInPoints((short) 13); f.setColor(IndexedColors.WHITE.getIndex());
        s.setFont(f);
        s.setAlignment(HorizontalAlignment.CENTER);
        return s;
    }
    private XSSFCellStyle estiloHeader(XSSFWorkbook wb) {
        XSSFCellStyle s = wb.createCellStyle();
        s.setFillForegroundColor(new XSSFColor(new byte[]{(byte)0, (byte)102, (byte)153}, null));
        s.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        XSSFFont f = wb.createFont();
        f.setBold(true); f.setColor(IndexedColors.WHITE.getIndex());
        s.setFont(f);
        s.setBorderBottom(BorderStyle.THIN); s.setBorderTop(BorderStyle.THIN);
        s.setBorderLeft(BorderStyle.THIN);  s.setBorderRight(BorderStyle.THIN);
        s.setAlignment(HorizontalAlignment.CENTER);
        s.setWrapText(true);
        return s;
    }
    private XSSFCellStyle estiloDato(XSSFWorkbook wb, boolean par) {
        XSSFCellStyle s = wb.createCellStyle();
        if (par) {
            s.setFillForegroundColor(new XSSFColor(new byte[]{(byte)235, (byte)244, (byte)255}, null));
            s.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        }
        s.setBorderBottom(BorderStyle.THIN); s.setBorderTop(BorderStyle.THIN);
        s.setBorderLeft(BorderStyle.THIN);  s.setBorderRight(BorderStyle.THIN);
        return s;
    }
    private XSSFCellStyle estiloFreqHeader(XSSFWorkbook wb) { return estiloHeader(wb); }
    private XSSFCellStyle estiloFreqDato(XSSFWorkbook wb)   { return estiloDato(wb, true); }
    private XSSFCellStyle estiloFreqPct(XSSFWorkbook wb) {
        XSSFCellStyle s = estiloDato(wb, false);
        DataFormat df = wb.createDataFormat();
        s.setDataFormat(df.getFormat("0.0\"%\""));
        s.setAlignment(HorizontalAlignment.CENTER);
        return s;
    }
}
