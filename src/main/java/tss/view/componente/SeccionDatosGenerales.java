package tss.view.componente;

import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;

// SECCIÓN 0: CAPTURA EDAD Y GÉNERO DEL TITULADO
public class SeccionDatosGenerales extends FormLayout {

    private final RadioButtonGroup<String> edad = new RadioButtonGroup<>();
    private final RadioButtonGroup<String> genero = new RadioButtonGroup<>();

    public SeccionDatosGenerales() {
        edad.setLabel("Edad");
        edad.setItems("Menos de 25 años", "25 a 34 años", "35 años o más");

        genero.setLabel("Género");
        genero.setItems("Hombre", "Mujer");

        add(edad, genero);
    }

    public String getEdad() { return edad.getValue(); }
    public String getGenero() { return genero.getValue(); }

    public void limpiar() {
        edad.clear();
        genero.clear();
    }
}
