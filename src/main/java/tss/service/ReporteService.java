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
