import { injectGlobalWebcomponentCss } from 'Frontend/generated/jar-resources/theme-util.js';

import '@vaadin/vertical-layout/src/vaadin-vertical-layout.js';
import '@vaadin/app-layout/src/vaadin-app-layout.js';
import '@vaadin/horizontal-layout/src/vaadin-horizontal-layout.js';
import '@vaadin/app-layout/src/vaadin-drawer-toggle.js';
import '@vaadin/button/src/vaadin-button.js';
import '@vaadin/tooltip/src/vaadin-tooltip.js';
import 'Frontend/generated/jar-resources/disableOnClickFunctions.js';
import '@vaadin/scroller/src/vaadin-scroller.js';
import '@vaadin/side-nav/src/vaadin-side-nav.js';
import '@vaadin/side-nav/src/vaadin-side-nav-item.js';
import 'Frontend/echarts-chart.js';
import '@vaadin/upload/src/vaadin-upload.js';
import 'Frontend/generated/jar-resources/vaadin-upload/uploadManagerConnector.ts';
import '@vaadin/icons/vaadin-iconset.js';
import '@vaadin/icon/src/vaadin-icon.js';
import '@vaadin/notification/src/vaadin-notification.js';
import 'Frontend/generated/jar-resources/flow-component-renderer.js';
import 'Frontend/generated/jar-resources/flow-component-directive.js';
import '@vaadin/grid/src/vaadin-grid.js';
import '@vaadin/grid/src/vaadin-grid-column.js';
import '@vaadin/grid/src/vaadin-grid-sorter.js';
import '@vaadin/checkbox/src/vaadin-checkbox.js';
import 'Frontend/generated/jar-resources/vaadin-grid/gridConnector.ts';
import 'Frontend/generated/jar-resources/vaadin-grid/vaadin-grid-flow-selection-column.ts';
import '@vaadin/grid/src/vaadin-grid-column-group.js';
import 'Frontend/generated/jar-resources/lit-renderer.ts';
import '@vaadin/context-menu/src/vaadin-context-menu.js';
import 'Frontend/generated/jar-resources/vaadin-context-menu/contextMenuConnector.ts';
import 'Frontend/generated/jar-resources/vaadin-context-menu/contextMenuTargetConnector.ts';
import '@vaadin/form-layout/src/vaadin-form-layout.js';
import '@vaadin/form-layout/src/vaadin-form-item.js';
import '@vaadin/form-layout/src/vaadin-form-row.js';
import '@vaadin/radio-group/src/vaadin-radio-group.js';
import '@vaadin/radio-group/src/vaadin-radio-button.js';
import '@vaadin/combo-box/src/vaadin-combo-box.js';
import 'Frontend/generated/jar-resources/vaadin-combo-box/comboBoxConnector.ts';
import '@vaadin/multi-select-combo-box/src/vaadin-multi-select-combo-box.js';
import '@vaadin/text-field/src/vaadin-text-field.js';
import '@vaadin/integer-field/src/vaadin-integer-field.js';
import '@vaadin/checkbox-group/src/vaadin-checkbox-group.js';
import '@vaadin/email-field/src/vaadin-email-field.js';
import '@vaadin/common-frontend/ConnectionIndicator.js';
import 'Frontend/generated/jar-resources/ReactRouterOutletElement.tsx';

const loadOnDemand = (key) => {
  const pending = [];
  if (key === '07ef5c7cefba8d5971904caa1bbe77cad50beacb1ef5c4b474645538a547a77e') {
    pending.push(import('./chunks/chunk-69bdf03dd729812e2a497537fdcbdfbab3adc949b536a7c71e2131b8a6237080.js'));
  }
  if (key === 'a296e8591eabc539ce6db1dc022dda3ad4f75f6e070f14a40201ff521dd35d43') {
    pending.push(import('./chunks/chunk-69bdf03dd729812e2a497537fdcbdfbab3adc949b536a7c71e2131b8a6237080.js'));
  }
  if (key === '0dc0c0c6f694ae886f55954345dec45da4e1c460eaae2caf26923c74ff22b063') {
    pending.push(import('./chunks/chunk-69bdf03dd729812e2a497537fdcbdfbab3adc949b536a7c71e2131b8a6237080.js'));
  }
  if (key === 'bde4567ac0e3da1d7e30b74abff997b3aeaefd3f6ef9c58a78c838add3e14450') {
    pending.push(import('./chunks/chunk-153c8d3f530ed742dc45e7ce922f55b76c11b1c9d592285dca09740898063b21.js'));
  }
  if (key === '907e522ba676c59f4a17c8b368c4594ca0a252ff62af15053cbea4d394737806') {
    pending.push(import('./chunks/chunk-153c8d3f530ed742dc45e7ce922f55b76c11b1c9d592285dca09740898063b21.js'));
  }
  return Promise.all(pending);
}
window.Vaadin = window.Vaadin || {};
window.Vaadin.Flow = window.Vaadin.Flow || {};
window.Vaadin.Flow.loadOnDemand = loadOnDemand;
window.Vaadin.Flow.resetFocus = () => {
 let ae=document.activeElement;
 while(ae&&ae.shadowRoot) ae = ae.shadowRoot.activeElement;
 return !ae || ae.blur() || ae.focus() || true;
}