package tss;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import tss.service.CsvTituladosImportService;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CsvTituladosImportServiceTest {

    static {
        TssRecoleccionApplication.cargarDotEnv();
    }

    @Autowired
    private CsvTituladosImportService importService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void testImportSampleCsv() {
        // Build a mock 55-column CSV with 1 header and 2 rows
        StringBuilder csv = new StringBuilder();
        // 55 headers
        for (int i = 0; i < 55; i++) {
            csv.append("Header").append(i).append(i == 54 ? "\n" : ";");
        }

        // Row 1
        String[] row1 = new String[55];
        row1[0] = "1";
        row1[1] = "15/03/2024 10:30";
        row1[2] = "15/03/2024 10:45";
        row1[3] = "anonymous";
        row1[4] = "23 - 26 años";
        row1[5] = "Hombre";
        row1[6] = "SI";
        row1[7] = "DIPLOMADO";
        row1[8] = "DIRECCIÓN DE POSGRADO FCyT";
        row1[9] = "FINANCIADO CON RECURSOS PROPIOS";
        row1[10] = "Diplomado en Desarrollo Web";
        row1[11] = "SI";
        row1[12] = "IA";
        row1[13] = "DIRECCIÓN DE POSGRADO FCyT";
        row1[14] = "De acuerdo";
        row1[15] = "2,5";
        row1[16] = "0,5";
        row1[17] = "Actualmente me encuentro trabajando en una organización";
        row1[18] = "Empresa X";
        row1[19] = "Desarrollo de Software";
        row1[20] = "";
        row1[21] = "PRIVADO";
        row1[22] = "COCHABAMBA";
        row1[23] = "Desarrollo";
        row1[24] = "";
        row1[25] = "Desarrollo";
        row1[26] = "";
        row1[27] = "De acuerdo";
        row1[28] = "Entre 2 a 5 años";
        row1[29] = "POSTULACIÓN COMPETITIVA (CONVOCATORIAS PÚBLICAS Y PRIVADAS)";
        row1[30] = "Entre Bs. 5.001 - Bs. 7.000";
        row1[31] = "";
        row1[32] = "";
        row1[33] = "";
        row1[34] = "";
        row1[35] = "";
        row1[36] = "NO";
        row1[37] = "";
        row1[38] = "";
        row1[39] = "Menos de 1 mes";
        row1[40] = "2";
        row1[41] = "Satisfecho";
        row1[42] = "De acuerdo";
        row1[43] = "Programación, Base de Datos";
        row1[44] = "Infraestructura";
        row1[45] = "Programación, Redes";
        row1[46] = "-";
        row1[47] = "Química";
        row1[48] = "";
        row1[49] = "SI";
        row1[50] = "contacto@ejemplo.com";
        row1[51] = "77712345";
        row1[52] = "https://linkedin.com/in/titulado1";
        row1[53] = "30";
        row1[54] = "6";

        for (int i = 0; i < 55; i++) {
            csv.append(row1[i]).append(i == 54 ? "\n" : ";");
        }

        ByteArrayInputStream stream = new ByteArrayInputStream(csv.toString().getBytes(StandardCharsets.UTF_8));
        var result = importService.importar(stream);

        assertTrue(result.isExito(), "Import should be successful: " + result.getMensaje());
        assertEquals(1, result.getFilasImportadas());

        Integer count = jdbcTemplate.queryForObject("SELECT count(*) FROM respuesta_titulado", Integer.class);
        assertEquals(1, count);

        // Clean up test row
        jdbcTemplate.execute("DELETE FROM respuesta_titulado;");
    }
}
