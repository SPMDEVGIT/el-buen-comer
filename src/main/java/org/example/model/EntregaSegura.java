package org.example.model;

public class EntregaSegura implements EmpresaDomicilio {

    private static final double TARIFA_BASE = 7000;
    private static final double VALOR_KM = 1000;
    private static final double RECARGO = 3000;
    private static final double DISTANCIA_RECARGO = 5;
    private static final double DISTANCIA_MAXIMA = 20;

    @Override
    public String getNombre() {
        return "EntregaSegura";
    }

    @Override
    public boolean aceptaRecorrido(double distanciaKm) {
        return distanciaKm > 0 && distanciaKm <= DISTANCIA_MAXIMA;
    }

    @Override
    public double calcularTarifa(double distanciaKm) {
        double tarifa = TARIFA_BASE + VALOR_KM * distanciaKm;
        // a 5 km exactos no hay recargo
        if (distanciaKm > DISTANCIA_RECARGO) {
            tarifa += RECARGO;
        }
        return tarifa;
    }

    @Override
    public String toString() {
        return getNombre();
    }
}
