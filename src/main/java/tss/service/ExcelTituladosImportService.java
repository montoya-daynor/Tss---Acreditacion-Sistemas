package tss.service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// MAPEO EXACTO DEL EXCEL DE MICROSOFT FORMS (167 columnas)
// Columnas de datos relevantes:
//  0=Id, 1=HoraInicio, 2=HoraFin, 3=Correo
//  4=Nombre, 5=TotalPuntos, 6=ComentariosCuestionario, 7=HoraPublicacion  <- ignoradas
//  8=Edad, 11=Genero
//  14=TieneFormacion
//  17=NivelPosgrado, 20=InstCursada, 23=Financiamiento, 26=NombrePrograma
//  29=InteresPosgrado, 32=AreaInteres, 35=InstPreferida
//  38=Puntos FCyT (ignorar), 39=Comentarios FCyT (ignorar)
//  40=OpinionPosgradoFcyt
//  43=AniosVidaProf, 46=AniosDesempleo
//  49=SituacionLaboral, 52=Empresa, 55=Rubro, 58=RubroOtro
//  61=Sector, 64=Depto, 67=AreaOrg, 70=AreaOtra, 73=Cargo, 76=CargoOtro
//  79=Puntos Column (ignorar), 80=Comentarios Column (ignorar)
//  81=PertinenciaLabores, 84=Antiguedad, 87=MedioObtencion, 90=RangoRemuneracion
//  93=OrigenEmprendimiento, 96=TipoEntregable, 99=FinanciamientoNegocio
//  102=Puntos Column1 (ignorar), 103=Comentarios Column1 (ignorar)
//  104=SatisfaccionNegocio
//  107=Puntos Column2 (ignorar), 108=Comentarios Column2 (ignorar)
//  109=ImportanciaFormacion
//  112=EsPrimerEmpleo, 115=RazonNoTrabaja, 118=TrabajoPrevio
//  121=TiempoPrimerEmpleo, 124=CantidadEmpleos
//  127=Puntos Column3 (ignorar), 128=Comentarios Column3 (ignorar)
//  129=SatisfaccionFormacion
//  132=Puntos Column4 (ignorar), 133=Comentarios Column4 (ignorar)
//  134=ConcordanciaFormacion
//  137=AspectosUtiles, 140=AspectosMejorar
//  143=AsignaturasVentaja, 146=AsignaturaVentajaOtra
//  149=AsignaturasPocoUtiles, 152=AsignaturaPocoUtilOtra
//  155=InteresRed, 158=Email, 161=Telefono, 164=LinkedIn
@Service
public class ExcelTituladosImportService {

    private final JdbcTemplate jdbcTemplate;
    private final RespuestaTituladoService tituladoService;

    public ExcelTituladosImportService(JdbcTemplate jdbcTemplate, RespuestaTituladoService tituladoService) {
        this.jdbcTemplate = jdbcTemplate;
        this.tituladoService = tituladoService;
    }

    public static class ImportResult {
        private final boolean exito;
        private final String mensaje;

        public ImportResult(boolean exito, String mensaje) {
            this.exito = exito;
            this.mensaje = mensaje;
        }

        public boolean isExito() { return exito; }
        public String getMensaje() { return mensaje; }
    }

    @Transactional
    public ImportResult importar(InputStream inputStream) {
        try (Workbook wb = new XSSFWorkbook(inputStream)) {
            Sheet sheet = wb.getSheetAt(0);
            int totalFilas = sheet.getLastRowNum();

            if (totalFilas < 1) return new ImportResult(false, "El archivo no contiene datos.");

            Row header = sheet.getRow(0);
            if (header == null || header.getLastCellNum() < 100) {
                return new ImportResult(false, "El archivo no parece ser el Excel de Microsoft Forms de Titulados.");
            }

            List<Object[]> batchArgs = new ArrayList<>();

            for (int r = 1; r <= totalFilas; r++) {
                Row row = sheet.getRow(r);
                if (row == null) continue;

                Long id = getLong(row, 0);
                if (id == null) continue;

                LocalDateTime horaInicio        = getFecha(row, 1);
                LocalDateTime horaFin           = getFecha(row, 2);
                String correo                   = getString(row, 3);
                String nombre                   = getString(row, 4);
                // col 5=TotalPuntos, 6=ComentariosCuestionario -> ignoradas
                LocalDateTime horaPublicacion   = getFecha(row, 7);
                String rangoEdad                = getString(row, 8);
                // col 9=Puntos, 10=Comentarios -> ignoradas
                String genero                   = getString(row, 11);
                // col 12=Puntos, 13=Comentarios -> ignoradas
                String tieneFormacion           = getString(row, 14);
                // col 15=Puntos, 16=Comentarios -> ignoradas
                String nivelPosgrado            = getString(row, 17);
                // col 18=Puntos, 19=Comentarios -> ignoradas
                String instCursada              = getString(row, 20);
                // col 21=Puntos, 22=Comentarios -> ignoradas
                String financiamiento           = getString(row, 23);
                // col 24=Puntos, 25=Comentarios -> ignoradas
                String nombrePrograma           = getString(row, 26);
                // col 27=Puntos, 28=Comentarios -> ignoradas
                String interesPosgrado          = getString(row, 29);
                // col 30=Puntos, 31=Comentarios -> ignoradas
                String areaInteres              = getString(row, 32);
                // col 33=Puntos, 34=Comentarios -> ignoradas
                String instPreferida            = getString(row, 35);
                // col 36=Puntos, 37=Comentarios -> ignoradas
                // col 38=Puntos FCyT (sin pregunta principal), 39=Comentarios FCyT -> ignoradas
                String opinionPosgradoFcyt      = getString(row, 40);
                // col 41=Puntos, 42=Comentarios -> ignoradas
                String aniosVidaProfStr         = getString(row, 43);
                // col 44=Puntos, 45=Comentarios -> ignoradas
                String aniosDesempleoStr        = getString(row, 46);
                // col 47=Puntos, 48=Comentarios -> ignoradas
                String situacionLaboral         = getString(row, 49);
                // col 50=Puntos, 51=Comentarios -> ignoradas
                String empresa                  = getString(row, 52);
                // col 53=Puntos, 54=Comentarios -> ignoradas
                String rubro                    = getString(row, 55);
                // col 56=Puntos, 57=Comentarios -> ignoradas
                String rubroOtro                = getString(row, 58);
                // col 59=Puntos, 60=Comentarios -> ignoradas
                String sector                   = getString(row, 61);
                // col 62=Puntos, 63=Comentarios -> ignoradas
                String depto                    = getString(row, 64);
                // col 65=Puntos, 66=Comentarios -> ignoradas
                String areaOrg                  = getString(row, 67);
                // col 68=Puntos, 69=Comentarios -> ignoradas
                String areaOtra                 = getString(row, 70);
                // col 71=Puntos, 72=Comentarios -> ignoradas
                String cargo                    = getString(row, 73);
                // col 74=Puntos, 75=Comentarios -> ignoradas
                String cargoOtro                = getString(row, 76);
                // col 77=Puntos, 78=Comentarios -> ignoradas
                // col 79=Puntos Column, 80=Comentarios Column -> ignoradas
                String pertinencia              = getString(row, 81);
                // col 82=Puntos, 83=Comentarios -> ignoradas
                String antiguedad               = getString(row, 84);
                // col 85=Puntos, 86=Comentarios -> ignoradas
                String medioObtencion           = getString(row, 87);
                // col 88=Puntos, 89=Comentarios -> ignoradas
                String rangoRemuneracion        = getString(row, 90);
                // col 91=Puntos, 92=Comentarios -> ignoradas
                String origenEmprendimiento     = getString(row, 93);
                // col 94=Puntos, 95=Comentarios -> ignoradas
                String tipoEntregable           = getString(row, 96);
                // col 97=Puntos, 98=Comentarios -> ignoradas
                String financNegocio            = getString(row, 99);
                // col 100=Puntos, 101=Comentarios -> ignoradas
                // col 102=Puntos Column1, 103=Comentarios Column1 -> ignoradas
                String satisfNegocio            = getString(row, 104);
                // col 105=Puntos, 106=Comentarios -> ignoradas
                // col 107=Puntos Column2, 108=Comentarios Column2 -> ignoradas
                String importanciaFormacion     = getString(row, 109);
                // col 110=Puntos, 111=Comentarios -> ignoradas
                String esPrimerEmpleo           = getString(row, 112);
                // col 113=Puntos, 114=Comentarios -> ignoradas
                String razonNoTrabaja           = getString(row, 115);
                // col 116=Puntos, 117=Comentarios -> ignoradas
                String trabajoPrevio            = getString(row, 118);
                // col 119=Puntos, 120=Comentarios -> ignoradas
                String tiempoPrimerEmpleo       = getString(row, 121);
                // col 122=Puntos, 123=Comentarios -> ignoradas
                String cantidadEmpleos          = getString(row, 124);
                // col 125=Puntos, 126=Comentarios -> ignoradas
                // col 127=Puntos Column3, 128=Comentarios Column3 -> ignoradas
                String satisfaccionFormacion    = getString(row, 129);
                // col 130=Puntos, 131=Comentarios -> ignoradas
                // col 132=Puntos Column4, 133=Comentarios Column4 -> ignoradas
                String concordanciaFormacion    = getString(row, 134);
                // col 135=Puntos, 136=Comentarios -> ignoradas
                String aspectosUtiles           = getString(row, 137);
                // col 138=Puntos, 139=Comentarios -> ignoradas
                String aspectosMejorar          = getString(row, 140);
                // col 141=Puntos, 142=Comentarios -> ignoradas
                String asignaturasVentaja       = getString(row, 143);
                // col 144=Puntos, 145=Comentarios -> ignoradas
                String asignaturaVentajaOtra    = getString(row, 146);
                // col 147=Puntos, 148=Comentarios -> ignoradas
                String asignaturasPocoUtiles    = getString(row, 149);
                // col 150=Puntos, 151=Comentarios -> ignoradas
                String asignaturaPocoUtilOtra   = getString(row, 152);
                // col 153=Puntos, 154=Comentarios -> ignoradas
                String interesRed               = getString(row, 155);
                // col 156=Puntos, 157=Comentarios -> ignoradas
                String email                    = getString(row, 158);
                // col 159=Puntos, 160=Comentarios -> ignoradas
                String telefono                 = getString(row, 161);
                // col 162=Puntos, 163=Comentarios -> ignoradas
                String linkedin                 = getString(row, 164);

                // Parsear años de vida profesional y desempleo (pueden ser texto como "5 años" o número)
                Double aniosVidaProf  = parseDouble(aniosVidaProfStr);
                Double aniosDesempleo = parseDouble(aniosDesempleoStr);

                if (rangoEdad == null || genero == null) continue;

                batchArgs.add(new Object[]{
                        id, horaInicio, horaFin, correo, nombre, horaPublicacion, rangoEdad, genero,
                        tieneFormacion, nivelPosgrado, instCursada, financiamiento, nombrePrograma,
                        interesPosgrado, areaInteres, instPreferida, opinionPosgradoFcyt,
                        aniosVidaProf, aniosDesempleo, situacionLaboral, empresa, rubro, rubroOtro,
                        sector, depto, areaOrg, areaOtra, cargo, cargoOtro, pertinencia,
                        antiguedad, medioObtencion, rangoRemuneracion, origenEmprendimiento,
                        tipoEntregable, financNegocio, satisfNegocio, importanciaFormacion,
                        esPrimerEmpleo, razonNoTrabaja, trabajoPrevio, tiempoPrimerEmpleo, cantidadEmpleos,
                        satisfaccionFormacion, concordanciaFormacion, aspectosUtiles, aspectosMejorar,
                        asignaturasVentaja, asignaturaVentajaOtra, asignaturasPocoUtiles, asignaturaPocoUtilOtra,
                        interesRed, email, telefono, linkedin,
                        null, null // meses_vida_profesional, meses_desempleo
                });
            }

            if (batchArgs.isEmpty()) return new ImportResult(false, "No se encontraron filas de datos válidas.");

            jdbcTemplate.execute("DELETE FROM respuesta_titulado;");

            String sql = """
                INSERT INTO respuesta_titulado (
                    id, hora_inicio, hora_fin, correo_registro, nombre, hora_publicacion, rango_edad, genero,
                    tiene_formacion_complementaria, nivel_programa_cursado, institucion_cursada,
                    financiamiento_programa, nombre_programa,
                    interes_posgrado, area_interes_posgrado, institucion_preferida, opinion_posgrado_fcyt,
                    anios_vida_profesional, anios_desempleo, situacion_laboral, empresa, rubro, rubro_otro,
                    sector, departamento, area_organizacion, area_otra, cargo, cargo_otro, pertinencia_labores,
                    antiguedad_trabajo_actual, medio_obtencion_trabajo, rango_remuneracion,
                    origen_emprendimiento, tipo_entregable, financiamiento_negocio,
                    satisfaccion_negocio, importancia_formacion_negocio,
                    es_primer_empleo, razon_no_trabaja, trabajo_previo, tiempo_primer_empleo, cantidad_empleos,
                    satisfaccion_formacion, concordancia_formacion, aspectos_utiles, aspectos_mejorar,
                    asignaturas_ventaja, asignatura_ventaja_otra, asignaturas_poco_utiles, asignatura_poco_util_otra,
                    interes_red_contactos, email_contacto, telefono_contacto, linkedin,
                    meses_vida_profesional, meses_desempleo
                ) VALUES (
                    ?,?,?,?,?,?,?,?, ?,?,?,?,?, ?,?,?,?, ?,?,?,?,?,?, ?,?,?,?,?,?,?, ?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?,?,?,?, ?,?,?,?, ?,?,?,?, ?,?
                )
            """;

            jdbcTemplate.batchUpdate(sql, batchArgs);
            jdbcTemplate.execute("SELECT setval(pg_get_serial_sequence('respuesta_titulado','id'), COALESCE(max(id),1)) FROM respuesta_titulado;");
            tituladoService.invalidarCache();

            return new ImportResult(true, "¡" + batchArgs.size() + " registros importados exitosamente desde Excel!");

        } catch (Exception e) {
            return new ImportResult(false, "Error al procesar el Excel: " + e.getMessage());
        }
    }

    private String getString(Row row, int col) {
        Cell cell = row.getCell(col, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
        if (cell == null) return null;
        String val = switch (cell.getCellType()) {
            case STRING  -> cell.getStringCellValue().trim();
            case NUMERIC -> DateUtil.isCellDateFormatted(cell)
                    ? cell.getLocalDateTimeCellValue().toString()
                    : String.valueOf((long) cell.getNumericCellValue());
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            default      -> null;
        };
        return (val == null || val.isBlank()) ? null : val;
    }

    private Double parseDouble(String valor) {
        if (valor == null) return null;
        // Extraer solo la parte numérica (ej: "5 años" -> 5.0)
        String limpio = valor.trim().replaceAll("[^0-9.,]", "").replace(",", ".");
        if (limpio.isEmpty()) return null;
        try { return Double.parseDouble(limpio); }
        catch (Exception e) { return null; }
    }

    private Long getLong(Row row, int col) {
        Cell cell = row.getCell(col, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
        if (cell == null) return null;
        return switch (cell.getCellType()) {
            case NUMERIC -> (long) cell.getNumericCellValue();
            case STRING  -> { try { yield Long.parseLong(cell.getStringCellValue().trim()); } catch (Exception e) { yield null; } }
            default -> null;
        };
    }

    private LocalDateTime getFecha(Row row, int col) {
        Cell cell = row.getCell(col, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
        if (cell == null) return null;
        try {
            if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell))
                return cell.getLocalDateTimeCellValue();
        } catch (Exception ignored) {}
        return null;
    }
}
