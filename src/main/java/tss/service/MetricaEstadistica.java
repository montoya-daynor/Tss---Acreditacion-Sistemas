package tss.service;

// MODELO DE DATOS PARA MÉTRICAS ESTADÍSTICAS DESCRIPTIVAS
public record MetricaEstadistica(
    String variable,
    double media,
    double mediana,
    double desviacionEstandar,
    double min,
    double max,
    int muestra
) {}
