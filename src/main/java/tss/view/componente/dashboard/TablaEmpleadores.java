package tss.view.componente.dashboard;

import com.vaadin.flow.component.grid.Grid;
import tss.entity.RespuestaEmpleador;

import java.util.List;

// TABLA CON TODAS LAS RESPUESTAS INDIVIDUALES DE EMPLEADORES
public class TablaEmpleadores extends Grid<RespuestaEmpleador> {

    public TablaEmpleadores(List<RespuestaEmpleador> datos) {
        super(RespuestaEmpleador.class, false);

        addColumn(r -> r.getId() != null ? r.getId() : "")
                .setHeader("ID").setWidth("60px").setFlexGrow(0).setSortable(true);

        addColumn(r -> r.getNombreOrganizacion() != null ? r.getNombreOrganizacion() : "—")
                .setHeader("Organización").setAutoWidth(true).setSortable(true).setResizable(true);

        addColumn(r -> r.getTipoOrganizacion() != null ? r.getTipoOrganizacion() : "—")
                .setHeader("Tipo").setAutoWidth(true).setSortable(true).setResizable(true);

        addColumn(r -> r.getTamanoOrganizacion() != null ? r.getTamanoOrganizacion() : "—")
                .setHeader("Tamaño").setAutoWidth(true).setSortable(true).setResizable(true);

        addColumn(r -> r.getRubro() != null ? r.getRubro() : "—")
                .setHeader("Rubro").setAutoWidth(true).setSortable(true).setResizable(true);

        addColumn(r -> r.getContratoIngenieros5Anios() != null ? r.getContratoIngenieros5Anios() : "—")
                .setHeader("Contrató Ing. (5 años)").setAutoWidth(true).setSortable(true).setResizable(true);

        addColumn(r -> r.getPrevisionContratacion() != null ? r.getPrevisionContratacion() : "—")
                .setHeader("Previsión Contratación").setAutoWidth(true).setSortable(true).setResizable(true);

        addColumn(r -> r.getNivelFormacionExigido() != null ? r.getNivelFormacionExigido() : "—")
                .setHeader("Nivel Exigido").setAutoWidth(true).setSortable(true).setResizable(true);

        addColumn(r -> r.getMedioConvocatoria() != null ? r.getMedioConvocatoria() : "—")
                .setHeader("Medio Convocatoria").setAutoWidth(true).setSortable(true).setResizable(true);

        addColumn(r -> r.getAreasEmergentes() != null ? r.getAreasEmergentes() : "—")
                .setHeader("Áreas Emergentes").setWidth("250px").setResizable(true);

        addColumn(r -> r.getHerramientasRequeridas() != null ? r.getHerramientasRequeridas() : "—")
                .setHeader("Herramientas Requeridas").setWidth("250px").setResizable(true);

        addColumn(r -> r.getHabilidadesRequeridas() != null ? r.getHabilidadesRequeridas() : "—")
                .setHeader("Habilidades Requeridas").setWidth("250px").setResizable(true);

        addColumn(r -> r.getLkConfianzaFormadora() != null ? r.getLkConfianzaFormadora() : "—")
                .setHeader("Confianza Formadora").setAutoWidth(true).setSortable(true).setResizable(true);

        addColumn(r -> r.getLkTituloConsistente() != null ? r.getLkTituloConsistente() : "—")
                .setHeader("Título Consistente").setAutoWidth(true).setSortable(true).setResizable(true);

        addColumn(r -> r.getLkCompetenciasLaborales() != null ? r.getLkCompetenciasLaborales() : "—")
                .setHeader("Competencias Laborales").setAutoWidth(true).setSortable(true).setResizable(true);

        addColumn(r -> r.getLkValoresEtica() != null ? r.getLkValoresEtica() : "—")
                .setHeader("Valores y Ética").setAutoWidth(true).setSortable(true).setResizable(true);

        setItems(datos);
        setHeight("550px");
        setWidthFull();
    }
}
