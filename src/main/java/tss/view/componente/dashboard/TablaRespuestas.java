package tss.view.componente.dashboard;

import com.vaadin.flow.component.grid.Grid;
import tss.entity.RespuestaTitulado;

import java.util.List;

// TABLA CON TODAS LAS RESPUESTAS INDIVIDUALES
public class TablaRespuestas extends Grid<RespuestaTitulado> {

    public TablaRespuestas(List<RespuestaTitulado> datos) {
        super(RespuestaTitulado.class, false);
        addColumn(RespuestaTitulado::getId).setHeader("ID").setWidth("60px").setFlexGrow(0);
        addColumn(RespuestaTitulado::getEdad).setHeader("Edad").setAutoWidth(true);
        addColumn(RespuestaTitulado::getGenero).setHeader("Género").setAutoWidth(true);
        addColumn(RespuestaTitulado::getSituacionLaboral).setHeader("Situación Laboral").setAutoWidth(true);
        addColumn(RespuestaTitulado::getAniosVidaProfesional).setHeader("Años Prof.").setAutoWidth(true);
        addColumn(RespuestaTitulado::getSatisfaccionFormacion).setHeader("Satisfacción").setAutoWidth(true);
        addColumn(RespuestaTitulado::getConcordanciaMercado).setHeader("Concordancia").setAutoWidth(true);
        addColumn(RespuestaTitulado::getAreaInteresPosgrado).setHeader("Área Interés").setAutoWidth(true);
        setItems(datos);
        setHeight("300px");
        setWidthFull();
    }
}
