# Sistema de Análisis de Mercado y Seguimiento a Graduados
### Carrera de Ingeniería de Sistemas — Facultad de Ciencias y Tecnología (FCyT)
### Universidad Mayor de San Simón (UMSS) · Acreditación ARCUSUR

Sistema web desarrollado con **Spring Boot** y **Vaadin** para la recolección, tabulación y análisis estadístico de opiniones de titulados y empleadores, orientado a la toma de decisiones curriculares y al proceso de acreditación regional **ARCUSUR**.

---

## 📋 Tabla de Contenidos

- [Descripción General](#-descripción-general)
- [Características y Módulos](#-características-y-módulos)
- [Requisitos Previos](#-requisitos-previos)
- [Variables de Entorno](#-variables-de-entorno)
- [Instalación y Configuración](#-instalación-y-configuración)
- [Ejecución del Sistema](#-ejecución-del-sistema)
  - [Opción 1: Mediante script rápido (Windows)](#opción-1-mediante-script-rápido-windows)
  - [Opción 2: Mediante Maven Wrapper (Línea de Comandos)](#opción-2-mediante-maven-wrapper-línea-de-comandos)
- [Estructura del Proyecto](#-estructura-del-proyecto)
- [Tecnologías Utilizadas](#-tecnologías-utilizadas)
- [Exportación y Reportes](#-exportación-y-reportes)
- [Créditos y Licencia](#-créditos-y-licencia)

---

## 💡 Descripción General

El sistema permite capturar y procesar información clave sobre:
- Trayectoria y empleabilidad de titulados en Ingeniería de Sistemas.
- Pertinencia de las asignaturas y habilidades adquiridas en el plan de estudios.
- Demanda y preferencias en formación de posgrado y educación continua.
- Modalidades de financiamiento y necesidad de becas.
- Percepción y retroalimentación de empleadores sobre el desempeño profesional de los egresados.

Incorpora dashboards analíticos con gráficos interactivos (Apache ECharts) y exportación de datos a formatos estándar (**Excel .xlsx** y **CSV**).

---

## 🚀 Características y Módulos

### 1. Módulo de Encuestas
- **📋 Encuesta a Titulados (`/`)**:
  - *Sección 0:* Datos sociodemográficos (edad, género).
  - *Sección 1:* Formación complementaria y posgrado cursado.
  - *Sección 2:* Intereses futuros en programas de posgrado (maestrías, diplomados, certificaciones).
  - *Sección 3:* Trayectoria, inserción y situación laboral actual.
  - *Sección 4:* Evaluación de calidad curricular (materias de mayor/menor utilidad, áreas de mejora).
  - *Sección 5:* Red de graduados y datos de contacto.
- **🏢 Encuesta a Empleadores**:
  - Recolección de información sobre cargos demandados, competencias técnicas, habilidades blandas y nivel de satisfacción de los empleadores.

### 2. Módulo de Análisis y Dashboards
- **📊 Dashboard General**: Resumen de indicadores clave (KPIs), métricas estadísticas globales y tabla de respuestas con filtros.
- **💼 Perfil y Empleabilidad**: Indicadores de tasa de empleo, tiempo de inserción en el mercado, relación con el perfil profesional y salarios promedio.
- **🎓 Demanda de Posgrado**: Áreas temáticas de mayor interés (Inteligencia Artificial, Ciberseguridad, Gestión TI, Ciencia de Datos, etc.) y modalidades preferidas (virtual, presencial, híbrida).
- **💰 Financiamiento y Becas**: Preferencias de financiamiento, disposición de inversión y requerimiento de apoyo económico.

### 3. Reportes y Descargas
- Descarga directa de datos tabulados en formato **Excel (.xlsx)** y **CSV** para análisis estadístico avanzado externo (R, Python, SPSS).

---

## ⚙️ Requisitos Previos

Antes de ejecutar el proyecto, asegúrese de contar con las siguientes herramientas instaladas en su equipo:

1. **Java Development Kit (JDK)**:
   - Versión requerida: **Java 21 LTS** (o superior).
   - Verificar instalación ejecutando:
     ```bash
     java -version
     ```
2. **Base de Datos PostgreSQL**:
   - PostgreSQL 14 o superior (instalación local o base de datos en la nube como Neon, Supabase o AWS RDS).
3. **Git**:
   - Para clonar el repositorio y control de versiones.
   - Verificar instalación:
     ```bash
     git --version
     ```
4. **Navegador Web**:
   - Google Chrome, Microsoft Edge, Mozilla Firefox o cualquier navegador moderno con soporte para JavaScript.

> [!NOTE]
> No es necesario instalar Apache Maven por separado en el sistema, ya que el proyecto incluye el envoltorio **Maven Wrapper** (`mvnw` / `mvnw.cmd`).

---

## 🔐 Variables de Entorno

El sistema requiere las credenciales de conexión a la base de datos PostgreSQL mediante las siguientes variables de entorno:

| Variable | Descripción | Ejemplo / Valor por defecto |
| :--- | :--- | :--- |
| `DB_URL` | URL JDBC de conexión a PostgreSQL | `jdbc:postgresql://localhost:5432/tss_db` |
| `DB_USERNAME` | Usuario de la base de datos | `postgres` |
| `DB_PASSWORD` | Contraseña del usuario | `tu_password_aqui` |

---

## 📦 Instalación y Configuración

### 1. Clonar el Repositorio
```bash
git clone https://github.com/montoya-daynor/Tss---Acreditacion-Sistemas.git
cd "Acreditacion sistemas"
```

### 2. Configurar la Base de Datos
Cree una base de datos PostgreSQL vacía (si utiliza una instancia local):
```sql
CREATE DATABASE tss_db;
```
> El sistema utiliza Hibernate con `spring.jpa.hibernate.ddl-auto=update`, por lo que las tablas e índices se crearán automáticamente al iniciar la aplicación por primera vez.

---

## 💻 Ejecución del Sistema

### Opción 1: Mediante script automático (Recomendado)

El proyecto incluye scripts configurados con las variables de base de datos listas para iniciar:

- **En Git Bash (Terminal Bash en Windows / Linux / Mac):**
  ```bash
  ./run.sh
  ```
- **En Windows CMD / PowerShell o haciendo doble clic:**
  ```cmd
  run.cmd
  ```

---

### Opción 2: Mediante Maven Wrapper (Línea de Comandos)

#### En Git Bash (Windows):
```bash
export DB_URL="jdbc:postgresql://ep-royal-bread-b6nzwcdw-pooler.c-2.sa-east-1.aws.neon.tech/neondb?sslmode=require"
export DB_USERNAME="neondb_owner"
export DB_PASSWORD="tu_password"
./mvnw spring-boot:run
```

#### En Windows PowerShell:
```powershell
$env:DB_URL="jdbc:postgresql://ep-royal-bread-b6nzwcdw-pooler.c-2.sa-east-1.aws.neon.tech/neondb?sslmode=require"
$env:DB_USERNAME="neondb_owner"
$env:DB_PASSWORD="tu_password"
.\mvnw.cmd spring-boot:run
```

#### En Windows CMD (Símbolo del sistema):
```cmd
set DB_URL=jdbc:postgresql://ep-royal-bread-b6nzwcdw-pooler.c-2.sa-east-1.aws.neon.tech/neondb?sslmode=require
set DB_USERNAME=neondb_owner
set DB_PASSWORD=tu_password
mvnw.cmd spring-boot:run
```

#### En Linux / macOS:
```bash
export DB_URL="jdbc:postgresql://localhost:5432/tss_db"
export DB_USERNAME="postgres"
export DB_PASSWORD="tu_password"
./mvnw spring-boot:run
```

---

## 🌐 Acceso a la Aplicación

Una vez iniciada la aplicación, la consola mostrará que el servidor Spring Boot está activo en el puerto `8082`:

- **URL de acceso local**: [http://localhost:8082](http://localhost:8082)
- La aplicación abrirá automáticamente una pestaña en su navegador predeterminado (configurado por `vaadin.launch-browser=true`).

---

## 🗂️ Estructura del Proyecto

```text
├── .mvn/                         # Configuración de Maven Wrapper
├── src/
│   ├── main/
│   │   ├── bundles/              # Frontend development bundle generado por Vaadin
│   │   ├── java/
│   │   │   └── tss/
│   │   │       ├── entity/       # Entidades JPA (RespuestaTitulado, RespuestaEmpleador, etc.)
│   │   │       ├── repository/   # Repositorios Spring Data JPA
│   │   │       ├── service/      # Lógica de negocio y servicios de generación de reportes
│   │   │       ├── view/         # Vistas Vaadin Flow (Dashboard, Encuestas, Análisis)
│   │   │       │   └── componente/ # Subcomponentes y gráficos de dashboard
│   │   │       └── TssRecoleccionApplication.java # Clase principal Spring Boot
│   │   └── resources/
│   │       └── application.properties # Configuración de Spring Boot y Vaadin
│   └── test/                     # Pruebas unitarias y de integración
├── mvnw / mvnw.cmd               # Scripts ejecutables de Maven Wrapper
├── pom.xml                       # Gestión de dependencias y plugins Maven
├── run.cmd                       # Script batch para arranque en Windows
└── README.md                     # Documentación del proyecto
```

---

## 🛠️ Tecnologías Utilizadas

- **Lenguaje**: Java 21 LTS
- **Framework Backend**: [Spring Boot 4.x / 3.x](https://spring.io/projects/spring-boot) (Spring Data JPA, Hibernate)
- **Framework Frontend / UI**: [Vaadin Flow 25.3.1](https://vaadin.com/)
- **Visualización de Datos**: Apache ECharts
- **Procesamiento de Archivos**:
  - [Apache POI 5.3.0](https://poi.apache.org/) (Exportación a Microsoft Excel `.xlsx`)
  - [OpenCSV 5.9](https://opencsv.sourceforge.net/) (Exportación a texto separado por comas `.csv`)
- **Gestión de Dependencias y Build**: Apache Maven

---

## 📊 Exportación y Reportes

Desde la sección **Dashboard**, el personal autorizado puede descargar los conjuntos de datos recopilados con un solo clic:
- **`⬇ Exportar Excel`**: Genera un archivo `.xlsx` estructurado por hojas y columnas formateadas para revisión ejecutiva.
- **`⬇ Exportar CSV`**: Genera un archivo `.csv` en formato crudo para ingesta en herramientas de análisis y BI.

---

## 🏛️ Créditos Institucionales

- **Universidad Mayor de San Simón (UMSS)**
- **Facultad de Ciencias y Tecnología (FCyT)**
- **Carrera de Ingeniería de Sistemas**
- Proyecto enmarcado en los estándares del sistema de acreditación **ARCUSUR**.
