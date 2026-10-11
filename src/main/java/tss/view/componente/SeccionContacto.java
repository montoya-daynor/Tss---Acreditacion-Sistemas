package tss.view.componente;

import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;

// SECCIÓN 5: CAPTURA INTERÉS EN RED DE GRADUADOS Y DATOS DE CONTACTO
public class SeccionContacto extends FormLayout {

    private final RadioButtonGroup<String> interesadoRed = new RadioButtonGroup<>();
    private final EmailField email = new EmailField("Email de contacto");
    private final TextField telefono = new TextField("Teléfono (WhatsApp)");
    private final TextField linkedin = new TextField("LinkedIn");

    public SeccionContacto() {
        interesadoRed.setLabel("¿Le interesaría formar parte de una red de contactos oficial?");
        interesadoRed.setItems("SI", "NO");

        add(interesadoRed, email, telefono, linkedin);
    }

    public Boolean getInteresadoRed() { return "SI".equals(interesadoRed.getValue()); }
    public String getEmail() { return email.getValue(); }
    public String getTelefono() { return telefono.getValue(); }
    public String getLinkedin() { return linkedin.getValue(); }

    public void limpiar() {
        interesadoRed.clear();
        email.clear();
        telefono.clear();
        linkedin.clear();
    }
}
