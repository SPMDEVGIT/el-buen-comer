package org.example.model;

/** Contrato de una empresa de domicilios: cobertura maxima y tarifa segun la distancia. */
public interface EmpresaDomicilio {

    /** @return nombre comercial de la empresa */
    String getNombre();

    /** @return true si la empresa cubre un recorrido de {@code distanciaKm} kilometros */
    boolean aceptaRecorrido(double distanciaKm);

    /** @return costo del domicilio en pesos para la distancia indicada */
    double calcularTarifa(double distanciaKm);
}
