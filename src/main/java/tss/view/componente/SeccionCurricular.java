package tss.view.componente;

import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import tss.entity.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

// SECCIÓN 4 (HU4): EVALÚA BRECHAS ENTRE FORMACIÓN RECIBIDA Y REQUERIMIENTOS DEL MERCADO
public class SeccionCurricular extends VerticalLayout {

    private final RadioButtonGroup<String> satisfaccionFormacion = new RadioButtonGroup<>();
    private final RadioButtonGroup<String> concordanciaMercado = new RadioButtonGroup<>();
    private final CheckboxGroup<String> aspectosUtiles = new CheckboxGroup<>();
    private final CheckboxGroup<String> aspectosMejorar = new CheckboxGroup<>();
    private final CheckboxGroup<String> asignaturasUtiles = new CheckboxGroup<>();
    private final CheckboxGroup<String> asignaturasInutiles = new CheckboxGroup<>();

    public SeccionCurricular() {
        satisfaccionFormacion.setLabel("Grado de satisfacción con la formación recibida");
        satisfaccionFormacion.setItems("Totalmente en desacuerdo", "En desacuerdo",
                "De acuerdo", "Totalmente de acuerdo");

        concordanciaMercado.setLabel("Concordancia entre formación y requerimientos del mercado");
        concordanciaMercado.setItems("Totalmente en desacuerdo", "En desacuerdo",
                "De acuerdo", "Totalmente de acuerdo");

        aspectosUtiles.setLabel("Aspectos de la carrera que resultaron de bastante utilidad");
        aspectosUtiles.setItems(
                "La aplicación práctica de conceptos teóricos en proyectos reales.",
                "La oportunidad de realizar pasantías o prácticas profesionales.",
                "La distribución de los horarios de clases.",
                "El desarrollo de habilidades analíticas y de resolución de problemas.",
                "La colaboración de equipos multidisciplinarios para resolver problemas complejos.",
                "La enseñanza de herramientas y software específico de la industria.");

        aspectosMejorar.setLabel("Aspectos de la carrera que pueden mejorarse");
        aspectosMejorar.setItems(
                "Mayor integración de la teoría con aplicaciones prácticas en el plan de estudios.",
                "Mayor relacionamiento del estudiante con las empresas por medio de prácticas profesionales.",
                "La distribución de horarios de clases.",
                "Actualización constante del plan de estudios para atender tendencias actuales.",
                "Incremento de actividades extracurriculares.",
                "Ofrecimiento de más opciones de especialización.");

        asignaturasUtiles.setLabel("5 asignaturas que le brindaron ventaja competitiva");
        asignaturasUtiles.setItems("Introducción", "Base de Datos", "Inteligencia Artificial",
                "Redes de Computadora", "Electivas", "T.I.S.", "Básicas (Física, Cálculo, Álgebras)",
                "Planificación de Proyectos y Gestión", "Tecnología Redes Avanzadas");

        asignaturasInutiles.setLabel("Asignaturas que no resultaron de mucha utilidad");
        asignaturasInutiles.setItems("Básicas (Física, Cálculo, Álgebras)",
                "Aplic. Interactivas para Televisión Digital", "Telefonía IP", "Web Semánticas",
                "Graficación por Computadora", "Robótica", "Taller de Programación en Bajo Nivel",
                "Teoría de Autómatas y Leng. Formales", "Programación Funcional",
                "Teoría de Grafos", "Contabilidad Básica");

        add(satisfaccionFormacion, concordanciaMercado,
                aspectosUtiles, aspectosMejorar, asignaturasUtiles, asignaturasInutiles);
    }

    public String getSatisfaccionFormacion() { return satisfaccionFormacion.getValue(); }
    public String getConcordanciaMercado() { return concordanciaMercado.getValue(); }

    // CONVIERTE LOS SETS DE CHECKBOXES A LISTAS DE ENTIDADES JPA
    public List<RespuestaTituladoAspectoUtil> getAspectosUtiles() {
        return aspectosUtiles.getSelectedItems().stream()
                .map(a -> { var e = new RespuestaTituladoAspectoUtil(); e.setAspecto(a); return e; })
                .collect(Collectors.toList());
    }

    public List<RespuestaTituladoAspectoMejorar> getAspectosMejorar() {
        return aspectosMejorar.getSelectedItems().stream()
                .map(a -> { var e = new RespuestaTituladoAspectoMejorar(); e.setAspecto(a); return e; })
                .collect(Collectors.toList());
    }

    public List<RespuestaTituladoAsignaturaUtil> getAsignaturasUtiles() {
        return asignaturasUtiles.getSelectedItems().stream()
                .map(a -> { var e = new RespuestaTituladoAsignaturaUtil(); e.setAsignatura(a); return e; })
                .collect(Collectors.toList());
    }

    public List<RespuestaTituladoAsignaturaInutile> getAsignaturasInutiles() {
        return asignaturasInutiles.getSelectedItems().stream()
                .map(a -> { var e = new RespuestaTituladoAsignaturaInutile(); e.setAsignatura(a); return e; })
                .collect(Collectors.toList());
    }

    public void limpiar() {
        satisfaccionFormacion.clear();
        concordanciaMercado.clear();
        aspectosUtiles.clear();
        aspectosMejorar.clear();
        asignaturasUtiles.clear();
        asignaturasInutiles.clear();
    }
}
