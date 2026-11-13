package org.example.model;

/** Postre del menu. Su calidad depende del peso y de la temperatura de almacenamiento. */
public class Postre extends Producto {

    private double pesoGramos;
    private double temperaturaAlmacenamiento; // grados Celsius
    private boolean requiereRefrigeracion;    // define el rango de temperatura valido

    /** Crea un postre; los cinco primeros parametros los recibe {@link Producto}. */
    public Postre(String codigo, String nombre, String descripcion, double precioBase,
                  int tiempoPreparacion, double pesoGramos, double temperaturaAlmacenamiento,
                  boolean requiereRefrigeracion) {
        super(codigo, nombre, descripcion, precioBase, tiempoPreparacion);
        this.pesoGramos = pesoGramos;
        this.temperaturaAlmacenamiento = temperaturaAlmacenamiento;
        this.requiereRefrigeracion = requiereRefrigeracion;
    }

    /** Precio base + 15 %; si requiere refrigeracion se suma otro 5 % del precio base (+ 20 % en total). */
    @Override
    public double calcularPrecioVenta() {
        // el 5 % adicional por refrigeracion es sobre el precio base
        if (requiereRefrigeracion) {
            return getPrecioBase() * 1.20;
        }
        return getPrecioBase() * 1.15;
    }

    /**
     * Cumple si pesa entre 80 y 500 g y la temperatura esta en rango:
     * 0-6 °C si requiere refrigeracion, 15-30 °C en caso contrario.
     */
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
