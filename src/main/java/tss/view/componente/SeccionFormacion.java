package tss.view.componente;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.TextField;

// SECCIÓN 1: CAPTURA FORMACIÓN COMPLEMENTARIA Y POSGRADO REALIZADO (HU2, HU3)
public class SeccionFormacion extends FormLayout {

    private final RadioButtonGroup<String> realizoPosgrado = new RadioButtonGroup<>();
    private final ComboBox<String> nivelPosgrado = new ComboBox<>("Nivel de posgrado");
    private final ComboBox<String> institucionPosgrado = new ComboBox<>("Institución");
    private final ComboBox<String> financiamientoPosgrado = new ComboBox<>("Fuente de financiamiento");
    private final TextField nombrePrograma = new TextField("Nombre del programa");

    public SeccionFormacion() {
        realizoPosgrado.setLabel("¿Ha realizado algún programa de formación complementaria?");
        realizoPosgrado.setItems("SI", "NO");

        nivelPosgrado.setItems("DIPLOMADO", "ESPECIALIDAD", "MAESTRIA", "DOCTORADO", "POS DOCTORADO");

        institucionPosgrado.setItems("EUPG", "DIRECCIÓN DE POSGRADO FCyT", "IBNORCA",
                "UNIVERSIDADES INTERNACIONALES", "UNIVERSIDADES PÚBLICAS NACIONALES",
                "UNIVERSIDADES PRIVADAS NACIONALES", "OTRO");

        financiamientoPosgrado.setItems("FINANCIADO CON RECURSOS PROPIOS",
                "TOTAL FINANCIAMIENTO POR MEDIO DE UNA BECA",
                "FINANCIADO PARCIALMENTE POR MEDIO DE UNA BECA");

        add(realizoPosgrado, nivelPosgrado, institucionPosgrado, financiamientoPosgrado, nombrePrograma);
    }

    public Boolean getRealizoPosgrado() { return "SI".equals(realizoPosgrado.getValue()); }
    public String getNivelPosgrado() { return nivelPosgrado.getValue(); }
    public String getInstitucionPosgrado() { return institucionPosgrado.getValue(); }
    public String getFinanciamientoPosgrado() { return financiamientoPosgrado.getValue(); }
    public String getNombrePrograma() { return nombrePrograma.getValue(); }

    public void limpiar() {
        realizoPosgrado.clear();
        nivelPosgrado.clear();
        institucionPosgrado.clear();
        financiamientoPosgrado.clear();
        nombrePrograma.clear();
    }
}
