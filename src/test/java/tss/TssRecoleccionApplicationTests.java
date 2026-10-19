package tss;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import tss.service.RespuestaTituladoService;
import tss.service.ReporteService;
import tss.entity.RespuestaTitulado;
import java.util.*;

@SpringBootTest
class TssRecoleccionApplicationTests {

	static {
		TssRecoleccionApplication.cargarDotEnv();
	}

	@Autowired
	private RespuestaTituladoService tituladoService;

	@Autowired
	private ReporteService reporteService;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Test
	void testCorreccionesEstadisticas() {
		List<RespuestaTitulado> datos = tituladoService.listarTodos();
		assertEquals(148, datos.size(), "Debe haber 148 registros en total");

		// 1. Desempleo: promedio 1.00 (0.998), mediana 0.88 (10.5 meses), desv 2.15
		Map<String, Object> resumen = reporteService.calcularResumenAntigüedad(datos);
		double mediaDesemp = ((Number) resumen.get("mediaDesempleo")).doubleValue();
		double medianaDesemp = ((Number) resumen.get("medianaDesempleo")).doubleValue();
		double medianaMeses = ((Number) resumen.get("medianaDesempleoMeses")).doubleValue();
		double desvDesemp = ((Number) resumen.get("desvDesempleo")).doubleValue();

		assertEquals(1.00, mediaDesemp, 0.01, "El promedio de desempleo debe ser 1.00 año");
		assertEquals(0.88, medianaDesemp, 0.01, "La mediana de desempleo debe ser 0.88 años (10.5 meses)");
		assertEquals(10.5, medianaMeses, 0.1, "La mediana en meses debe ser exactamente 10.5 meses");
		assertEquals(2.15, desvDesemp, 0.01, "La desviación estándar de desempleo debe ser 2.15");

		// Coma decimal formateada
		Locale locEs = Locale.forLanguageTag("es-ES");
		String formatoDesemp = String.format(locEs, "Mediana: %.2f años (%.1f meses)", medianaDesemp, medianaMeses);
		assertEquals("Mediana: 0,88 años (10,5 meses)", formatoDesemp);

		// 2. Primer empleo: NO debe ser exactamente 87 (excluyendo a los 25 que no trabajan)
		List<Map<String, Object>> freqPrimerEmpleo = reporteService.calcularFrecuenciaRelativa(
				datos, r -> r.getPrimerEmpleo() != null ? (r.getPrimerEmpleo() ? "SÍ" : "NO") : null);

		Map<String, Long> cantidadesPrimer = new HashMap<>();
		for (Map<String, Object> fila : freqPrimerEmpleo) {
			cantidadesPrimer.put((String) fila.get("categoria"), ((Number) fila.get("cantidad")).longValue());
		}
		assertEquals(87L, cantidadesPrimer.get("NO"), "Primer empleo 'NO' debe ser exactamente 87");
		assertEquals(36L, cantidadesPrimer.get("SÍ"), "Primer empleo 'SÍ' debe ser 36");
		assertFalse(cantidadesPrimer.containsKey("Sin dato"), "No debe haber categoría 'Sin dato'");

		// 3. Tiempo para el primer empleo: porcentajes calculados sobre los 101 que respondieron
		List<Map<String, Object>> freqTiempo = reporteService.calcularFrecuenciaRelativa(
				datos, RespuestaTitulado::getTiempoPrimerEmpleo);

		long totalTiempo = freqTiempo.stream().mapToLong(f -> ((Number) f.get("cantidad")).longValue()).sum();
		assertEquals(101L, totalTiempo, "Debe calcularse sobre los 101 que respondieron (n = 101)");

		// Orden cronológico
		List<String> ordenCronologico = List.of(
				"Ya trabajaba antes de titularse",
				"Menos de 1 mes",
				"Entre 1 - 4 meses",
				"Entre 4 - 8 meses",
				"Entre 8 - 12 meses",
				"Más de 12 meses"
		);
		freqTiempo.sort(Comparator.comparingInt(m -> {
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

		assertEquals("Ya trabajaba antes de titularse", freqTiempo.get(0).get("categoria"));
		assertEquals("Menos de 1 mes", freqTiempo.get(1).get("categoria"));
		assertEquals("Entre 1 - 4 meses", freqTiempo.get(2).get("categoria"));
		assertEquals("Entre 4 - 8 meses", freqTiempo.get(3).get("categoria"));
		assertEquals("Entre 8 - 12 meses", freqTiempo.get(4).get("categoria"));
		assertEquals("Más de 12 meses", freqTiempo.get(5).get("categoria"));

		// 4. Rotación: orden como 1, 2, 3, Más de 3 y n = 101
		List<Map<String, Object>> freqRotacion = reporteService.calcularFrecuenciaRelativa(
				datos, RespuestaTitulado::getCantidadEmpleos);
		long totalRotacion = freqRotacion.stream().mapToLong(f -> ((Number) f.get("cantidad")).longValue()).sum();
		assertEquals(101L, totalRotacion, "La rotación también debe ser n = 101");

		List<String> ordenRotacion = List.of("1", "2", "3", "Más de 3");
		freqRotacion.sort(Comparator.comparingInt(m -> {
			String cat = m.get("categoria").toString().trim();
			for (int i = 0; i < ordenRotacion.size(); i++) {
				if (cat.equalsIgnoreCase(ordenRotacion.get(i))) return i;
			}
			return 999;
		}));
		assertEquals("1", freqRotacion.get(0).get("categoria"));
		assertEquals("2", freqRotacion.get(1).get("categoria"));
		assertEquals("3", freqRotacion.get(2).get("categoria"));
		assertEquals("Más de 3", freqRotacion.get(3).get("categoria"));
	}

}
