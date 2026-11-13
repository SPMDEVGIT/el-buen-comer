package org.example.model;

/**
 * Empresa EntregaSegura: tarifa base $7.000 + $1.000 por km, con un recargo de $3.000
 * si el recorrido supera los 5 km. Cubre hasta 20 km.
 */
public class EntregaSegura implements EmpresaDomicilio {

    private static final double TARIFA_BASE = 7000;
    private static final double VALOR_KM = 1000;
    private static final double RECARGO = 3000;        // se suma solo por encima de DISTANCIA_RECARGO
    private static final double DISTANCIA_RECARGO = 5; // km
    private static final double DISTANCIA_MAXIMA = 20; // km

    @Override
    public String getNombre() {
        return "EntregaSegura";
    }

    /** Acepta distancias mayores que 0 y hasta 20 km inclusive. */
    @Override
    public boolean aceptaRecorrido(double distanciaKm) {
        return distanciaKm > 0 && distanciaKm <= DISTANCIA_MAXIMA;
    }

    /** Tarifa base + valor por km, mas el recargo si la distancia supera 5 km. */
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
