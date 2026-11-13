package org.example.model;

/**
 * Empresa RapidoExpress: tarifa base $5.000 + $1.500 por km, sin recargos.
 * Cubre hasta 12 km.
 */
public class RapidoExpress implements EmpresaDomicilio {

    private static final double TARIFA_BASE = 5000;
    private static final double VALOR_KM = 1500;
    private static final double DISTANCIA_MAXIMA = 12; // km

    @Override
    public String getNombre() {
        return "RapidoExpress";
    }

    /** Acepta distancias mayores que 0 y hasta 12 km inclusive. */
    @Override
    public boolean aceptaRecorrido(double distanciaKm) {
        return distanciaKm > 0 && distanciaKm <= DISTANCIA_MAXIMA;
    }

    /** Tarifa base + valor por km recorrido. */
    @Override
    public double calcularTarifa(double distanciaKm) {
        return TARIFA_BASE + VALOR_KM * distanciaKm;
    }

    @Override
    public String toString() {
        return getNombre();
    }
}

