package tss;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tss.entity.RespuestaEmpleador;
import tss.service.CsvEmpleadoresImportService;
import tss.service.RespuestaEmpleadorService;

import java.io.InputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CsvEmpleadoresImportServiceTest {

    static {
        TssRecoleccionApplication.cargarDotEnv();
    }

    @Autowired
    private CsvEmpleadoresImportService importService;

    @Autowired
    private RespuestaEmpleadorService empleadorService;

    @Test
    void testImportEmpleadoresCsvOficial() throws Exception {
        try (InputStream is = getClass().getResourceAsStream("/encuesta_empleadores.csv")) {
            assertNotNull(is, "El archivo encuesta_empleadores.csv debe existir en classpath");

            var result = importService.importar(is);
            assertTrue(result.isExito(), "La importación debe ser exitosa: " + result.getMensaje());
            assertEquals(53, result.getFilasImportadas(), "Deben importarse exactamente 53 registros");

            List<RespuestaEmpleador> lista = empleadorService.listarTodos();
            assertEquals(53, lista.size(), "La lista en el servicio debe contener 53 empleadores");

            // Verificar primer registro
            RespuestaEmpleador primero = lista.stream().filter(r -> r.getId() == 1L).findFirst().orElse(null);
            assertNotNull(primero);
            assertEquals("Tribunal Departamental de Justicia", primero.getNombreOrganizacion());
            assertEquals("Público", primero.getTipoOrganizacion());
            assertEquals("Grande", primero.getTamanoOrganizacion());
            assertEquals("Soporte técnico y mantenimiento de sistemas", primero.getRubro());
            assertEquals("SI", primero.getContratoIngenieros5Anios());
            assertEquals("Sí, alta probabilidad", primero.getPrevisionContratacion());
            assertEquals("Ciberseguridad", primero.getAreasEmergentes());
            assertEquals("Totalmente de acuerdo", primero.getLkConfianzaFormadora());
        }
    }
}
