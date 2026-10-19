package tss;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

@SpringBootApplication
public class TssRecoleccionApplication {

	static {
		cargarDotEnv();
	}

	public static void main(String[] args) {
		SpringApplication.run(TssRecoleccionApplication.class, args);
	}

	public static void cargarDotEnv() {
		File envFile = new File(".env");
		if (!envFile.exists()) {
			return;
		}
		try (BufferedReader reader = new BufferedReader(new FileReader(envFile))) {
			String linea;
			while ((linea = reader.readLine()) != null) {
				linea = linea.trim();
				if (linea.isEmpty() || linea.startsWith("#")) continue;
				int idx = linea.indexOf('=');
				if (idx > 0) {
					String key = linea.substring(0, idx).trim();
					String val = linea.substring(idx + 1).trim();
					if ((val.startsWith("\"") && val.endsWith("\"")) || (val.startsWith("'") && val.endsWith("'"))) {
						val = val.substring(1, val.length() - 1);
					}
					if (System.getProperty(key) == null && System.getenv(key) == null) {
						System.setProperty(key, val);
					}
				}
			}
		} catch (Exception ignored) {
		}
	}

}
