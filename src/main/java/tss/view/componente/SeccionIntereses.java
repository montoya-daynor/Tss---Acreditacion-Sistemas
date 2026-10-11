package tss.view.componente;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;

// SECCIÓN 2: CAPTURA INTERESES FUTUROS DE POSGRADO Y OPINIÓN SOBRE FCyT (HU2)
public class SeccionIntereses extends FormLayout {

    private final RadioButtonGroup<String> interesadoPosgrado = new RadioButtonGroup<>();
    private final ComboBox<String> areaInteresPosgrado = new ComboBox<>("Área de interés");
    private final ComboBox<String> institucionInteresPosgrado = new ComboBox<>("Organización educativa preferida");
    private final RadioButtonGroup<String> opinionPosgradoFcyt = new RadioButtonGroup<>();

    public SeccionIntereses() {
        interesadoPosgrado.setLabel("¿Estaría interesado en realizar estudios de posgrado?");
        interesadoPosgrado.setItems("SI", "NO");

        areaInteresPosgrado.setItems("IA", "Robótica", "Ingeniería de Software",
                "Base de Datos", "Redes de Datos y Seguridad");

        institucionInteresPosgrado.setItems("EUPG", "DIRECCIÓN DE POSGRADO FCyT", "IBNORCA",
                "UNIVERSIDADES INTERNACIONALES", "UNIVERSIDADES PÚBLICAS NACIONALES",
                "UNIVERSIDADES PRIVADAS NACIONALES", "OTRO");

        opinionPosgradoFcyt.setLabel("¿Existen programas de posgrado en FCyT afines a Ingeniería en Sistemas?");
        opinionPosgradoFcyt.setItems("Totalmente en desacuerdo", "En desacuerdo",
                "De acuerdo", "Totalmente de acuerdo");

        add(interesadoPosgrado, areaInteresPosgrado, institucionInteresPosgrado, opinionPosgradoFcyt);
    }

    public Boolean getInteresadoPosgrado() { return "SI".equals(interesadoPosgrado.getValue()); }
    public String getAreaInteresPosgrado() { return areaInteresPosgrado.getValue(); }
    public String getInstitucionInteresPosgrado() { return institucionInteresPosgrado.getValue(); }
    public String getOpinionPosgradoFcyt() { return opinionPosgradoFcyt.getValue(); }

    public void limpiar() {
        interesadoPosgrado.clear();
        areaInteresPosgrado.clear();
        institucionInteresPosgrado.clear();
        opinionPosgradoFcyt.clear();
    }
}
