package tss.service;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@Service
public class CsvTituladosImportService {

    private final JdbcTemplate jdbcTemplate;

    private static final DateTimeFormatter[] DATE_FORMATTERS = new DateTimeFormatter[] {
            DateTimeFormatter.ofPattern("d/M/yyyy H:m:s"),
            DateTimeFormatter.ofPattern("d/M/yyyy H:m"),
            DateTimeFormatter.ofPattern("d-M-yyyy H:m:s"),
            DateTimeFormatter.ofPattern("d-M-yyyy H:m"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"),
            DateTimeFormatter.ISO_LOCAL_DATE_TIME
    };

    public CsvTituladosImportService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public static class ImportResult {
        private final boolean exito;
        private final int filasImportadas;
        private final String mensaje;
        private final List<String> advertencias;

        public ImportResult(boolean exito, int filasImportadas, String mensaje, List<String> advertencias) {
            this.exito = exito;
            this.filasImportadas = filasImportadas;
            this.mensaje = mensaje;
            this.advertencias = advertencias;
        }

        public boolean isExito() { return exito; }
        public int getFilasImportadas() { return filasImportadas; }
        public String getMensaje() { return mensaje; }
        public List<String> getAdvertencias() { return advertencias; }
    }

    @Transactional
    public ImportResult importar(InputStream inputStream) {
        List<String> advertencias = new ArrayList<>();
        List<Object[]> batchArgs = new ArrayList<>();

        try (InputStreamReader isr = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
            CSVParser parser = new CSVParserBuilder().withSeparator(';').build();
            try (CSVReader csvReader = new CSVReaderBuilder(isr).withCSVParser(parser).build()) {
                String[] header = csvReader.readNext();
                if (header == null) {
                    return new ImportResult(false, 0, "El archivo CSV está vacío.", advertencias);
                }

                // Limpiar BOM en la primera columna si existe
                if (header.length > 0 && header[0] != null && header[0].startsWith("\uFEFF")) {
                    header[0] = header[0].substring(1);
                }

                if (header.length != 55) {
                    return new ImportResult(false, 0,
                            "El archivo no tiene las 55 columnas requeridas (se encontraron " + header.length + "). Verifique que el separador sea punto y coma (;).",
                            advertencias);
                }

                String[] fila;
                int filaNum = 1; // 1 = encabezado

                while ((fila = csvReader.readNext()) != null) {
                    filaNum++;
                    if (fila.length == 0 || (fila.length == 1 && fila[0].trim().isEmpty())) {
                        continue; // Fila vacía al final
                    }

                    if (fila.length != 55) {
                        return new ImportResult(false, 0,
                                "Error en la fila " + filaNum + ": tiene " + fila.length + " columnas en lugar de 55.",
                                advertencias);
                    }

                    // 0. Id
                    Long id = parseLong(fila[0]);
                    if (id == null) {
                        return new ImportResult(false, 0,
                                "Error en la fila " + filaNum + ": El campo ID no puede ser nulo o vacío.",
                                advertencias);
                    }

                    // 1. Hora inicio, 2. Hora fin
                    LocalDateTime horaInicio = parseFecha(fila[1]);
                    LocalDateTime horaFin = parseFecha(fila[2]);

                    // 3 a 6
                    String correoRegistro = cleanString(fila[3]);
                    String rangoEdad = cleanString(fila[4]);
                    String genero = cleanString(fila[5]);
                    String tieneFormacion = cleanString(fila[6]);

                    if (rangoEdad == null || genero == null) {
                        return new ImportResult(false, 0,
                                "Error en la fila " + filaNum + ": Rango de edad y Género son obligatorios.",
                                advertencias);
                    }

                    // 7 a 10: Posgrado cursado
                    String nivelCursado = cleanString(fila[7]);
                    String instCursada = cleanString(fila[8]);
                    String financiamientoProg = cleanString(fila[9]);
                    String nombreProg = cleanString(fila[10]);

                    // 11 a 14: Interés posgrado
                    String interesPosgrado = cleanString(fila[11]);
                    String areaInteresPosgrado = cleanString(fila[12]);
                    String instPreferida = cleanString(fila[13]);
                    String opinionPosgrado = cleanString(fila[14]);

                    if (interesPosgrado == null) {
                        return new ImportResult(false, 0,
                                "Error en la fila " + filaNum + ": Interés en posgrado no puede estar vacío.",
                                advertencias);
                    }

                    // 15 y 16: Decimales
                    Double aniosVidaProf = parseDecimal(fila[15]);
                    Double aniosDesemp = parseDecimal(fila[16]);

                    // 17 a 30: Laboral
                    String situacionLaboral = cleanString(fila[17]);
                    if (situacionLaboral == null) {
                        return new ImportResult(false, 0,
                                "Error en la fila " + filaNum + ": Situación laboral no puede estar vacía.",
                                advertencias);
                    }

                    String empresa = cleanString(fila[18]);
                    String rubro = cleanString(fila[19]);
                    String rubroOtro = cleanString(fila[20]);
                    String sector = cleanString(fila[21]);
                    String depto = cleanString(fila[22]);
                    String areaOrg = cleanString(fila[23]);
                    String areaOtra = cleanString(fila[24]);
                    String cargo = cleanString(fila[25]);
                    String cargoOtro = cleanString(fila[26]);
                    String pertinenciaLabores = cleanString(fila[27]);
                    String antiguedadActual = cleanString(fila[28]);
                    String medioObtencion = cleanString(fila[29]);
                    String rangoRemuneracion = cleanString(fila[30]);

                    // 31 a 35: Emprendimiento
                    String origenEmprendimiento = cleanString(fila[31]);
                    String tipoEntregable = cleanString(fila[32]);
                    String financiamientoNegocio = cleanString(fila[33]);
                    String satisfaccionNegocio = cleanString(fila[34]);
                    String importanciaFormacionNegocio = cleanString(fila[35]);

                    // 36 a 40: Trayectoria
                    String esPrimerEmpleo = cleanString(fila[36]);
                    String razonNoTrabaja = cleanString(fila[37]);
                    String trabajoPrevio = cleanString(fila[38]);
                    String tiempoPrimerEmpleo = cleanString(fila[39]);
                    String cantidadEmpleos = cleanString(fila[40]);

                    // 41 a 48: Satisfacción y Asignaturas
                    String satisfaccionFormacion = cleanString(fila[41]);
                    String concordanciaFormacion = cleanString(fila[42]);
                    String aspectosUtiles = cleanString(fila[43]);
                    String aspectosMejorar = cleanString(fila[44]);
                    String asignaturasVentaja = cleanString(fila[45]);
                    String asignaturaVentajaOtra = cleanString(fila[46]);
                    String asignaturasPocoUtiles = cleanString(fila[47]);
                    String asignaturaPocoUtilOtra = cleanString(fila[48]);

                    // 49 a 52: Contacto
                    String interesRedContactos = cleanString(fila[49]);
                    String emailContacto = cleanString(fila[50]);
                    String telefonoContacto = cleanString(fila[51]);
                    String linkedin = cleanString(fila[52]);

                    // 53 y 54: Meses enteros
                    Integer mesesVidaProf = parseEntero(fila[53]);
                    Integer mesesDesemp = parseEntero(fila[54]);

                    batchArgs.add(new Object[] {
                            id, horaInicio, horaFin, correoRegistro, rangoEdad, genero,
                            tieneFormacion, nivelCursado, instCursada, financiamientoProg, nombreProg,
                            interesPosgrado, areaInteresPosgrado, instPreferida, opinionPosgrado,
                            aniosVidaProf, aniosDesemp, situacionLaboral, empresa, rubro, rubroOtro,
                            sector, depto, areaOrg, areaOtra, cargo, cargoOtro, pertinenciaLabores,
                            antiguedadActual, medioObtencion, rangoRemuneracion, origenEmprendimiento,
                            tipoEntregable, financiamientoNegocio, satisfaccionNegocio, importanciaFormacionNegocio,
                            esPrimerEmpleo, razonNoTrabaja, trabajoPrevio, tiempoPrimerEmpleo, cantidadEmpleos,
                            satisfaccionFormacion, concordanciaFormacion, aspectosUtiles, aspectosMejorar,
                            asignaturasVentaja, asignaturaVentajaOtra, asignaturasPocoUtiles, asignaturaPocoUtilOtra,
                            interesRedContactos, emailContacto, telefonoContacto, linkedin,
                            mesesVidaProf, mesesDesemp
                    });
                }
            }

            if (batchArgs.isEmpty()) {
                return new ImportResult(false, 0, "No se encontraron filas de datos para importar.", advertencias);
            }

            // Limpiar la tabla previa para garantizar los 148 registros exactos del conjunto
            jdbcTemplate.execute("DELETE FROM respuesta_titulado;");

            String insertSql = """
                INSERT INTO respuesta_titulado (
                    id, hora_inicio, hora_fin, correo_registro, rango_edad, genero,
                    tiene_formacion_complementaria, nivel_programa_cursado, institucion_cursada, financiamiento_programa, nombre_programa,
                    interes_posgrado, area_interes_posgrado, institucion_preferida, opinion_posgrado_fcyt,
                    anios_vida_profesional, anios_desempleo, situacion_laboral, empresa, rubro, rubro_otro,
                    sector, departamento, area_organizacion, area_otra, cargo, cargo_otro, pertinencia_labores,
                    antiguedad_trabajo_actual, medio_obtencion_trabajo, rango_remuneracion, origen_emprendimiento,
                    tipo_entregable, financiamiento_negocio, satisfaccion_negocio, importancia_formacion_negocio,
                    es_primer_empleo, razon_no_trabaja, trabajo_previo, tiempo_primer_empleo, cantidad_empleos,
                    satisfaccion_formacion, concordancia_formacion, aspectos_utiles, aspectos_mejorar,
                    asignaturas_ventaja, asignatura_ventaja_otra, asignaturas_poco_utiles, asignatura_poco_util_otra,
                    interes_red_contactos, email_contacto, telefono_contacto, linkedin,
                    meses_vida_profesional, meses_desempleo
                ) VALUES (
                    ?, ?, ?, ?, ?, ?,
                    ?, ?, ?, ?, ?,
                    ?, ?, ?, ?,
                    ?, ?, ?, ?, ?, ?,
                    ?, ?, ?, ?, ?, ?, ?,
                    ?, ?, ?, ?,
                    ?, ?, ?, ?,
                    ?, ?, ?, ?, ?,
                    ?, ?, ?, ?,
                    ?, ?, ?, ?,
                    ?, ?, ?, ?,
                    ?, ?
                )
            """;

            jdbcTemplate.batchUpdate(insertSql, batchArgs);

            // Ajustar secuencia de id al valor máximo
            jdbcTemplate.execute("SELECT setval(pg_get_serial_sequence('respuesta_titulado', 'id'), COALESCE(max(id), 1)) FROM respuesta_titulado;");

            return new ImportResult(true, batchArgs.size(),
                    "¡" + batchArgs.size() + " registros cargados exitosamente!", advertencias);

        } catch (Exception e) {
            return new ImportResult(false, 0, "Error al procesar el archivo CSV: " + e.getMessage(), advertencias);
        }
    }

    private LocalDateTime parseFecha(String valor) {
        if (valor == null || valor.trim().isEmpty()) return null;
        String clean = valor.trim();
        for (DateTimeFormatter fmt : DATE_FORMATTERS) {
            try {
                return LocalDateTime.parse(clean, fmt);
            } catch (DateTimeParseException ignored) {}
        }
        try {
            LocalDate d = LocalDate.parse(clean, DateTimeFormatter.ofPattern("d/M/yyyy"));
            return d.atStartOfDay();
        } catch (Exception ignored) {}
        return null;
    }

    private Double parseDecimal(String valor) {
        if (valor == null || valor.trim().isEmpty()) return null;
        String clean = valor.trim().replace(",", ".");
        try {
            return Double.parseDouble(clean);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Integer parseEntero(String valor) {
        if (valor == null || valor.trim().isEmpty()) return null;
        String clean = valor.trim();
        try {
            return Integer.parseInt(clean);
        } catch (NumberFormatException e) {
            try {
                return (int) Math.round(Double.parseDouble(clean.replace(",", ".")));
            } catch (Exception ex) {
                return null;
            }
        }
    }

    private Long parseLong(String valor) {
        if (valor == null || valor.trim().isEmpty()) return null;
        try {
            return Long.parseLong(valor.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private String cleanString(String valor) {
        if (valor == null) return null;
        String clean = valor.trim();
        return clean.isEmpty() ? null : clean;
    }
}
