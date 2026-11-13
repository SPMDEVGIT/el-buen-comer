package org.example.model;

public interface EmpresaDomicilio {

    String getNombre();

    boolean aceptaRecorrido(double distanciaKm);

    double calcularTarifa(double distanciaKm);
}
