package org.example.model;

public class Postre extends Producto {

    private double pesoGramos;
    private double temperaturaAlmacenamiento;
    private boolean requiereRefrigeracion;

    public Postre(String codigo, String nombre, String descripcion, double precioBase,
                  int tiempoPreparacion, double pesoGramos, double temperaturaAlmacenamiento,
                  boolean requiereRefrigeracion) {
        super(codigo, nombre, descripcion, precioBase, tiempoPreparacion);
        this.pesoGramos = pesoGramos;
        this.temperaturaAlmacenamiento = temperaturaAlmacenamiento;
        this.requiereRefrigeracion = requiereRefrigeracion;
    }

    @Override
    public double calcularPrecioVenta() {
        // el 5 % adicional por refrigeracion es sobre el precio base
        if (requiereRefrigeracion) {
            return getPrecioBase() * 1.20;
        }
        return getPrecioBase() * 1.15;
    }

    @Override
    public boolean cumpleCalidad() {
        boolean pesoValido = pesoGramos >= 80 && pesoGramos <= 500;
        boolean temperaturaValida;
        if (requiereRefrigeracion) {
            temperaturaValida = temperaturaAlmacenamiento >= 0 && temperaturaAlmacenamiento <= 6;
        } else {
            temperaturaValida = temperaturaAlmacenamiento >= 15 && temperaturaAlmacenamiento <= 30;
        }
        return pesoValido && temperaturaValida;
    }

    @Override
    public Categoria getCategoria() {
        return Categoria.POSTRE;
    }

    public double getPesoGramos() {
        return pesoGramos;
    }

    public void setPesoGramos(double pesoGramos) {
        this.pesoGramos = pesoGramos;
    }

    public double getTemperaturaAlmacenamiento() {
        return temperaturaAlmacenamiento;
    }

    public void setTemperaturaAlmacenamiento(double temperaturaAlmacenamiento) {
        this.temperaturaAlmacenamiento = temperaturaAlmacenamiento;
    }

    public boolean isRequiereRefrigeracion() {
        return requiereRefrigeracion;
    }

    public void setRequiereRefrigeracion(boolean requiereRefrigeracion) {
        this.requiereRefrigeracion = requiereRefrigeracion;
    }

    @Override
    public String toString() {
        return "Postre{" + super.toString() + ", pesoGramos=" + pesoGramos
                + ", temperaturaAlmacenamiento=" + temperaturaAlmacenamiento
                + ", requiereRefrigeracion=" + requiereRefrigeracion + "}";
    }
}
