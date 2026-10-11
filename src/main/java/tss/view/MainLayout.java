package tss.view;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.theme.lumo.LumoUtility;

// LAYOUT PRINCIPAL CON SIDEBAR DE NAVEGACIÓN INSTITUCIONAL
public class MainLayout extends AppLayout implements RouterLayout {

    public MainLayout() {
        setPrimarySection(Section.DRAWER);
        addToDrawer(buildSidebar());
        addToNavbar(buildNavbar());
    }

    // BARRA SUPERIOR CON TOGGLE Y NOMBRE DE LA APP
    private HorizontalLayout buildNavbar() {
        DrawerToggle toggle = new DrawerToggle();
        toggle.setAriaLabel("Menú de navegación");

        Span appName = new Span("Análisis de Mercado — UMSS");
        appName.getStyle()
            .set("font-weight", "600")
            .set("font-size", "1rem")
            .set("color", "#003366");

        HorizontalLayout navbar = new HorizontalLayout(toggle, appName);
        navbar.setAlignItems(FlexComponent.Alignment.CENTER);
        navbar.setWidthFull();
        navbar.getStyle().set("padding", "0 1rem");
        return navbar;
    }

    // SIDEBAR CON LOGO, NAVEGACIÓN Y PIE INSTITUCIONAL
    private VerticalLayout buildSidebar() {
        VerticalLayout sidebar = new VerticalLayout();
        sidebar.setSizeFull();
        sidebar.setPadding(false);
        sidebar.setSpacing(false);
        sidebar.getStyle()
            .set("background", "linear-gradient(180deg, #003366 0%, #005599 100%)")
            .set("min-height", "100vh");

        sidebar.add(buildLogo(), buildNav(), buildFooter());
        return sidebar;
    }

    // LOGO E IDENTIDAD INSTITUCIONAL
    private Div buildLogo() {
        Div logo = new Div();
        logo.getStyle()
            .set("padding", "1.5rem 1rem 1rem 1rem")
            .set("border-bottom", "1px solid rgba(255,255,255,0.15)")
            .set("text-align", "center");

        Span sigla = new Span("UMSS");
        sigla.getStyle()
            .set("display", "block")
            .set("font-size", "2rem")
            .set("font-weight", "800")
            .set("color", "#ffffff")
            .set("letter-spacing", "4px");

        Span facultad = new Span("Facultad de Ciencias y Tecnología");
        facultad.getStyle()
            .set("display", "block")
            .set("font-size", "0.7rem")
            .set("color", "rgba(255,255,255,0.75)")
            .set("margin-top", "0.25rem");

        Span carrera = new Span("Ingeniería en Sistemas");
        carrera.getStyle()
            .set("display", "block")
            .set("font-size", "0.75rem")
            .set("font-weight", "600")
            .set("color", "rgba(255,255,255,0.9)")
            .set("margin-top", "0.15rem");

        logo.add(sigla, facultad, carrera);
        return logo;
    }

    // MENÚ DE NAVEGACIÓN CON SECCIONES
    private Scroller buildNav() {
        SideNav nav = new SideNav();
        nav.getStyle()
            .set("--vaadin-side-nav-item-padding", "0.6rem 1rem")
            .set("--vaadin-side-nav-item-color", "rgba(255,255,255,0.85)")
            .set("--vaadin-side-nav-item-hover-background", "rgba(255,255,255,0.12)")
            .set("--vaadin-side-nav-item-active-background", "rgba(255,255,255,0.2)")
            .set("--vaadin-side-nav-item-active-color", "#ffffff")
            .set("width", "100%");

        // SECCIÓN ENCUESTAS
        Span lblEncuestas = etiquetaSeccion("ENCUESTAS");

        SideNavItem encuestaTitulados = new SideNavItem(
            "📋  Titulados", EncuestaTituladoView.class);
        estilizarItem(encuestaTitulados);

        SideNavItem encuestaEmpleadores = new SideNavItem(
            "🏢  Empleadores", EncuestaEmpleadorView.class);
        estilizarItem(encuestaEmpleadores);

        // SECCIÓN ANÁLISIS
        Span lblAnalisis = etiquetaSeccion("ANÁLISIS");

        SideNavItem dashboard = new SideNavItem(
            "📊  Dashboard", DashboardView.class);
        estilizarItem(dashboard);

        nav.addItem(encuestaTitulados, encuestaEmpleadores, dashboard);

        VerticalLayout navContainer = new VerticalLayout(lblEncuestas, encuestaTitulados,
                encuestaEmpleadores, lblAnalisis, dashboard);
        navContainer.setPadding(false);
        navContainer.setSpacing(false);
        navContainer.getStyle().set("padding", "0.5rem 0");

        Scroller scroller = new Scroller(navContainer);
        scroller.getStyle().set("flex", "1");
        return scroller;
    }

    // PIE DEL SIDEBAR CON VERSIÓN
    private Div buildFooter() {
        Div footer = new Div();
        footer.getStyle()
            .set("padding", "1rem")
            .set("border-top", "1px solid rgba(255,255,255,0.15)")
            .set("text-align", "center");

        Span version = new Span("ARCUSUR · v1.0");
        version.getStyle()
            .set("font-size", "0.7rem")
            .set("color", "rgba(255,255,255,0.5)");

        Span anio = new Span("© 2025 FCyT - UMSS");
        anio.getStyle()
            .set("display", "block")
            .set("font-size", "0.65rem")
            .set("color", "rgba(255,255,255,0.4)")
            .set("margin-top", "0.2rem");

        footer.add(version, anio);
        return footer;
    }

    // ETIQUETA DE SECCIÓN EN EL MENÚ
    private Span etiquetaSeccion(String texto) {
        Span lbl = new Span(texto);
        lbl.getStyle()
            .set("display", "block")
            .set("font-size", "0.65rem")
            .set("font-weight", "700")
            .set("letter-spacing", "1.5px")
            .set("color", "rgba(255,255,255,0.45)")
            .set("padding", "1rem 1rem 0.25rem 1rem");
        return lbl;
    }

    // APLICA ESTILO BASE A CADA ITEM DE NAVEGACIÓN
    private void estilizarItem(SideNavItem item) {
        item.getStyle()
            .set("border-radius", "6px")
            .set("margin", "0.1rem 0.5rem")
            .set("font-size", "0.9rem");
    }
}
