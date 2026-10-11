package tss.view.componente;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.IntegerField;

// SECCIÓN 3: CAPTURA TRAYECTORIA LABORAL Y SITUACIÓN ACTUAL (HU1)
public class SeccionLaboral extends FormLayout {

    private final IntegerField aniosVidaProfesional = new IntegerField("Años de vida profesional");
    private final IntegerField aniosDesempleado = new IntegerField("Años desempleado");
    private final RadioButtonGroup<String> situacionLaboral = new RadioButtonGroup<>();
    private final RadioButtonGroup<String> primerEmpleo = new RadioButtonGroup<>();
    private final ComboBox<String> tiempoPrimerEmpleo = new ComboBox<>("Tiempo para conseguir primer empleo");
    private final ComboBox<String> cantidadEmpleos = new ComboBox<>("Cantidad de empleos");

    public SeccionLaboral() {
        aniosVidaProfesional.setMin(0);
        aniosDesempleado.setMin(0);

        situacionLaboral.setLabel("Situación laboral actual");
        situacionLaboral.setItems(
                "Actualmente me encuentro trabajando en una organización",
                "Actualmente NO trabajo",
                "Actualmente dedico mi tiempo COMPLETO a un emprendimiento propio");

        primerEmpleo.setLabel("¿El trabajo actual es su primer empleo?");
        primerEmpleo.setItems("SI", "NO");

        tiempoPrimerEmpleo.setItems("Ya trabajaba antes de titularse", "Menos de 1 mes",
                "Entre 1 - 4 meses", "Entre 4 - 8 meses", "Entre 8 - 12 meses", "Más de 12 meses");

        cantidadEmpleos.setItems("1", "2", "3", "Más de 3");

        add(aniosVidaProfesional, aniosDesempleado, situacionLaboral,
                primerEmpleo, tiempoPrimerEmpleo, cantidadEmpleos);
    }

    public Integer getAniosVidaProfesional() { return aniosVidaProfesional.getValue(); }
    public Integer getAniosDesempleado() { return aniosDesempleado.getValue(); }
    public String getSituacionLaboral() { return situacionLaboral.getValue(); }
    public Boolean getPrimerEmpleo() { return "SI".equals(primerEmpleo.getValue()); }
    public String getTiempoPrimerEmpleo() { return tiempoPrimerEmpleo.getValue(); }
    public String getCantidadEmpleos() { return cantidadEmpleos.getValue(); }

    public void limpiar() {
        aniosVidaProfesional.clear();
        aniosDesempleado.clear();
        situacionLaboral.clear();
        primerEmpleo.clear();
        tiempoPrimerEmpleo.clear();
        cantidadEmpleos.clear();
    }
}
