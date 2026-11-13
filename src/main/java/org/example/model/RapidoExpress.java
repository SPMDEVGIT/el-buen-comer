package org.example.model;



public class RapidoExpress implements EmpresaDomicilio {

    private static final double TARIFA_BASE = 5000;
    private static final double VALOR_KM = 1500;
    private static final double DISTANCIA_MAXIMA = 12;

    @Override
    public String getNombre() {
        return "RapidoExpress";
    }

    @Override
    public boolean aceptaRecorrido(double distanciaKm) {
        return distanciaKm > 0 && distanciaKm <= DISTANCIA_MAXIMA;
    }

    @Override
    public double calcularTarifa(double distanciaKm) {
        return TARIFA_BASE + VALOR_KM * distanciaKm;
    }

    @Override
    public String toString() {
        return getNombre();
    }
}

