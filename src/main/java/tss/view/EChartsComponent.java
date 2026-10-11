package tss.view;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;

// COMPONENTE JAVA QUE ENVUELVE EL WEB COMPONENT echarts-chart
@Tag("echarts-chart")
@JsModule("./echarts-chart.js")
public class EChartsComponent extends Component {

    // PASA LAS OPCIONES AL WEB COMPONENT VIA PROPIEDAD DE TEXTO
    public void setOptions(String optionsJson) {
        getElement().setProperty("optionsJson", optionsJson);
    }
}
