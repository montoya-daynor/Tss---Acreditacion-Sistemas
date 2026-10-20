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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@Service
public class CsvEmpleadoresImportService {

    private final JdbcTemplate jdbcTemplate;
    private final RespuestaEmpleadorService empleadorService;

    private static final DateTimeFormatter[] DATE_FORMATTERS = new DateTimeFormatter[] {
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),
            DateTimeFormatter.ofPattern("d/M/yyyy H:m:s"),
            DateTimeFormatter.ofPattern("d/M/yyyy H:m"),
            DateTimeFormatter.ofPattern("d-M-yyyy H:m:s"),
            DateTimeFormatter.ofPattern("d-M-yyyy H:m"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"),
            DateTimeFormatter.ISO_LOCAL_DATE_TIME
    };

    public CsvEmpleadoresImportService(JdbcTemplate jdbcTemplate, RespuestaEmpleadorService empleadorService) {
        this.jdbcTemplate = jdbcTemplate;
        this.empleadorService = empleadorService;
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

                if (header.length != 39) {
                    return new ImportResult(false, 0,
                            "El archivo no tiene las 39 columnas requeridas (se encontraron " + header.length + "). Verifique que el separador sea punto y coma (;).",
                            advertencias);
                }

                String[] fila;
                int filaNum = 1;

                while ((fila = csvReader.readNext()) != null) {
                    filaNum++;
                    if (fila.length == 0 || (fila.length == 1 && fila[0].trim().isEmpty())) {
                        continue;
                    }

                    if (fila.length != 39) {
                        return new ImportResult(false, 0,
                                "Error en la fila " + filaNum + ": tiene " + fila.length + " columnas en lugar de 39.",
                                advertencias);
                    }

                    Long id = parseLong(fila[0]);
                    LocalDateTime fechaRespuesta = parseFecha(fila[1]);
                    String nombreOrganizacion = cleanString(fila[2]);
                    String tipoOrganizacion = cleanString(fila[3]);
                    String tamanoOrganizacion = cleanString(fila[4]);
                    String rubro = cleanString(fila[5]);
                    String contratoIngenieros = cleanString(fila[6]);
                    String previsionContratacion = cleanString(fila[7]);
                    String nivelFormacionExigido = cleanString(fila[8]);
                    String medioConvocatoria = cleanString(fila[9]);

                    String cargoApoyo = cleanString(fila[10]);
                    String cargoTecnico = cleanString(fila[11]);
                    String cargoAnalista = cleanString(fila[12]);
                    String cargoEspecialista = cleanString(fila[13]);
                    String cargoSupervisor = cleanString(fila[14]);
                    String cargoJefatura = cleanString(fila[15]);
                    String cargoConsultor = cleanString(fila[16]);
                    String cargoDocente = cleanString(fila[17]);

                    String areasEmergentes = cleanString(fila[18]);
                    String herramientasRequeridas = cleanString(fila[19]);
                    String habilidadesRequeridas = cleanString(fila[20]);

                    String lkConfianzaFormadora = cleanString(fila[21]);
                    String lkTituloConsistente = cleanString(fila[22]);
                    String lkAutoridadesConsultan = cleanString(fila[23]);
                    String lkConocePerfilEgreso = cleanString(fila[24]);
                    String lkPerfilCoherenteDesempeno = cleanString(fila[25]);
                    String lkFormacionDesempenoDestacado = cleanString(fila[26]);
                    String lkCarreraIncorporaOpinion = cleanString(fila[27]);
                    String lkCompetenciasLaborales = cleanString(fila[28]);
                    String lkValoresEtica = cleanString(fila[29]);
                    String lkParticipoRetroalimentacion = cleanString(fila[30]);
                    String lkVinculosOportunidades = cleanString(fila[31]);
                    String lkParticipoVinculacionEntorno = cleanString(fila[32]);
                    String lkParticipoDifusionEmpleo = cleanString(fila[33]);
                    String lkParticipoVinculacionProfesionales = cleanString(fila[34]);
                    String lkConsultadoDesempeno = cleanString(fila[35]);
                    String lkCarreraConsultoPertinencia = cleanString(fila[36]);
                    String lkRecurreUmssTalento = cleanString(fila[37]);
                    String lkConsultadoAutoevaluacion = cleanString(fila[38]);

                    batchArgs.add(new Object[] {
                            id, fechaRespuesta, nombreOrganizacion, tipoOrganizacion, tamanoOrganizacion, rubro,
                            contratoIngenieros, previsionContratacion, nivelFormacionExigido, medioConvocatoria,
                            cargoApoyo, cargoTecnico, cargoAnalista, cargoEspecialista, cargoSupervisor, cargoJefatura, cargoConsultor, cargoDocente,
                            areasEmergentes, herramientasRequeridas, habilidadesRequeridas,
                            lkConfianzaFormadora, lkTituloConsistente, lkAutoridadesConsultan, lkConocePerfilEgreso, lkPerfilCoherenteDesempeno,
                            lkFormacionDesempenoDestacado, lkCarreraIncorporaOpinion, lkCompetenciasLaborales, lkValoresEtica,
                            lkParticipoRetroalimentacion, lkVinculosOportunidades, lkParticipoVinculacionEntorno, lkParticipoDifusionEmpleo,
                            lkParticipoVinculacionProfesionales, lkConsultadoDesempeno, lkCarreraConsultoPertinencia, lkRecurreUmssTalento,
                            lkConsultadoAutoevaluacion
                    });
                }
            }

            if (batchArgs.isEmpty()) {
                return new ImportResult(false, 0, "No se encontraron filas de datos para importar.", advertencias);
            }

            try {
                jdbcTemplate.execute("DELETE FROM respuesta_empleador_cargo;");
            } catch (Exception ignored) {}
            jdbcTemplate.execute("DELETE FROM respuesta_empleador;");

            String insertSql = """
                INSERT INTO respuesta_empleador (
                    id, fecha_respuesta, nombre_organizacion, tipo_organizacion, tamano_organizacion, rubro,
                    contrato_ingenieros_5_anios, prevision_contratacion, nivel_formacion_exigido, medio_convocatoria,
                    cargo_apoyo, cargo_tecnico, cargo_analista, cargo_especialista, cargo_supervisor, cargo_jefatura, cargo_consultor, cargo_docente,
                    areas_emergentes, herramientas_requeridas, habilidades_requeridas,
                    lk_confianza_formadora, lk_titulo_consistente, lk_autoridades_consultan, lk_conoce_perfil_egreso, lk_perfil_coherente_desempeno,
                    lk_formacion_desempeno_destacado, lk_carrera_incorpora_opinion, lk_competencias_laborales, lk_valores_etica,
                    lk_participo_retroalimentacion, lk_vinculos_oportunidades, lk_participo_vinculacion_entorno, lk_participo_difusion_empleo,
                    lk_participo_vinculacion_profesionales, lk_consultado_desempeno, lk_carrera_consulto_pertinencia, lk_recurre_umss_talento,
                    lk_consultado_autoevaluacion
                ) VALUES (
                    ?, ?, ?, ?, ?, ?,
                    ?, ?, ?, ?,
                    ?, ?, ?, ?, ?, ?, ?, ?,
                    ?, ?, ?,
                    ?, ?, ?, ?, ?,
                    ?, ?, ?, ?,
                    ?, ?, ?, ?,
                    ?, ?, ?, ?,
                    ?
                )
            """;

            jdbcTemplate.batchUpdate(insertSql, batchArgs);

            try {
                jdbcTemplate.execute("SELECT setval(pg_get_serial_sequence('respuesta_empleador', 'id'), COALESCE(max(id), 1)) FROM respuesta_empleador;");
            } catch (Exception ignored) {}

            // Invalidar caché en memoria
            empleadorService.invalidarCache();

            return new ImportResult(true, batchArgs.size(),
                    "Importación completada con éxito. Se importaron " + batchArgs.size() + " registros de empleadores.",
                    advertencias);

        } catch (Exception e) {
            return new ImportResult(false, 0, "Error procesando el archivo CSV: " + e.getMessage(), advertencias);
        }
    }

    private String cleanString(String val) {
        if (val == null) return null;
        String trimmed = val.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private Long parseLong(String val) {
        if (val == null || val.trim().isEmpty()) return null;
        try {
            return Long.parseLong(val.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private LocalDateTime parseFecha(String val) {
        if (val == null || val.trim().isEmpty()) return null;
        String s = val.trim();
        for (DateTimeFormatter fmt : DATE_FORMATTERS) {
            try {
                return LocalDateTime.parse(s, fmt);
            } catch (DateTimeParseException ignored) {}
        }
        return null;
    }
}
