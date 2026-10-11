import { LitElement, html, css } from 'lit';

class EChartsChart extends LitElement {
    static properties = {
        optionsJson: { type: String }
    };

    static styles = css`
        :host { display: block; width: 100%; }
        #chart {
            width: 100%;
            height: clamp(250px, 40vw, 400px);
        }
        @media (max-width: 600px) {
            #chart { height: 260px; }
        }
    `;

    constructor() {
        super();
        this.optionsJson = '';
        this._chart = null;
        this._resizeObserver = null;
    }

    render() {
        return html`<div id="chart"></div>`;
    }

    // CARGA ECHARTS DESDE CDN Y RENDERIZA EL GRÁFICO
    firstUpdated() {
        if (window.echarts) {
            this._init();
        } else {
            const script = document.createElement('script');
            script.src = 'https://cdn.jsdelivr.net/npm/echarts@5/dist/echarts.min.js';
            script.onload = () => this._init();
            document.head.appendChild(script);
        }
    }

    _init() {
        const div = this.shadowRoot.getElementById('chart');
        this._chart = window.echarts.init(div);
        this._applyOptions();
        // REDIMENSIONA EL GRÁFICO CUANDO CAMBIA EL TAMAÑO DEL CONTENEDOR
        this._resizeObserver = new ResizeObserver(() => this._chart?.resize());
        this._resizeObserver.observe(div);
    }

    _applyOptions() {
        if (this._chart && this.optionsJson) {
            try {
                this._chart.setOption(JSON.parse(this.optionsJson), true);
            } catch(e) { console.error('ECharts parse error', e); }
        }
    }

    // ACTUALIZA EL GRÁFICO CUANDO CAMBIAN LAS OPCIONES DESDE JAVA
    updated(changed) {
        if (changed.has('optionsJson')) this._applyOptions();
    }

    disconnectedCallback() {
        super.disconnectedCallback();
        this._resizeObserver?.disconnect();
        this._chart?.dispose();
    }
}

customElements.define('echarts-chart', EChartsChart);
