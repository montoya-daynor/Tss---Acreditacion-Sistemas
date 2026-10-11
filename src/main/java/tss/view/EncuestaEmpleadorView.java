package tss.view;

import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

// VISTA ENCUESTA EMPLEADORES — EN DESARROLLO
@Route(value = "empleadores", layout = MainLayout.class)
@PageTitle("Encuesta Empleadores - Ingeniería en Sistemas UMSS")
public class EncuestaEmpleadorView extends VerticalLayout {

    public EncuestaEmpleadorView() {
        setPadding(true);
        setSpacing(true);
        add(
            new H2("Encuesta a Empleadores"),
            new Paragraph("Esta sección está en desarrollo.")
        );
    }
}
