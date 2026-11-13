package org.example.model;

/** Plato fuerte del menu. Su calidad depende del peso, el tiempo de preparacion y la coccion. */
public class PlatoFuerte extends Producto {

    private double pesoGramos;
    private TipoPreparacion tipoPreparacion; // asado, frito, horneado o plancha
    private boolean coccionAprobada;         // resultado de la revision de cocina

    /** Crea un plato fuerte; los cinco primeros parametros los recibe {@link Producto}. */
    public PlatoFuerte(String codigo, String nombre, String descripcion, double precioBase,
                       int tiempoPreparacion, double pesoGramos, TipoPreparacion tipoPreparacion,
                       boolean coccionAprobada) {
        super(codigo, nombre, descripcion, precioBase, tiempoPreparacion);
        this.pesoGramos = pesoGramos;
        this.tipoPreparacion = tipoPreparacion;
        this.coccionAprobada = coccionAprobada;
    }

    /** Precio base + 20 %. */
    @Override
    public double calcularPrecioVenta() {
        return getPrecioBase() * 1.20;
    }

    /** Cumple si pesa entre 200 y 900 g, se prepara en 10-60 min y la coccion esta aprobada. */
    @Override
    public boolean cumpleCalidad() {
        return pesoGramos >= 200 && pesoGramos <= 900
                && getTiempoPreparacion() >= 10 && getTiempoPreparacion() <= 60
                && coccionAprobada;
    }

    @Override
    public Categoria getCategoria() {
        return Categoria.PLATO_FUERTE;
    }

    public double getPesoGramos() {
        return pesoGramos;
    }

    public void setPesoGramos(double pesoGramos) {
        this.pesoGramos = pesoGramos;
    }

    public TipoPreparacion getTipoPreparacion() {
        return tipoPreparacion;
    }

    public void setTipoPreparacion(TipoPreparacion tipoPreparacion) {
        this.tipoPreparacion = tipoPreparacion;
    }

    public boolean isCoccionAprobada() {
        return coccionAprobada;
    }

    public void setCoccionAprobada(boolean coccionAprobada) {
        this.coccionAprobada = coccionAprobada;
    }

    @Override
    public String toString() {
        return "PlatoFuerte{" + super.toString() + ", pesoGramos=" + pesoGramos
                + ", tipoPreparacion=" + tipoPreparacion + ", coccionAprobada=" + coccionAprobada + "}";
    }
}

