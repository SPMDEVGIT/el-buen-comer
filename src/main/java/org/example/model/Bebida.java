package org.example.model;

public class Bebida extends Producto {

    private double volumenMl;
    private double temperatura;
    private boolean esFria;
    private boolean preparacionEspecial;

    public Bebida(String codigo, String nombre, String descripcion, double precioBase,
                  int tiempoPreparacion, double volumenMl, double temperatura,
                  boolean esFria, boolean preparacionEspecial) {
        super(codigo, nombre, descripcion, precioBase, tiempoPreparacion);
        this.volumenMl = volumenMl;
        this.temperatura = temperatura;
        this.esFria = esFria;
        this.preparacionEspecial = preparacionEspecial;
    }

    @Override
    public double calcularPrecioVenta() {
        double precio = getPrecioBase() * 1.10;
        if (preparacionEspecial) {
            precio += 3000;
        }
        return precio;
    }

    @Override
    public boolean cumpleCalidad() {
        boolean volumenValido = volumenMl >= 150 && volumenMl <= 1500;
        boolean temperaturaValida;
        if (esFria) {
            temperaturaValida = temperatura >= 0 && temperatura <= 10;
        } else {
            temperaturaValida = temperatura >= 55 && temperatura <= 85;
        }
        return volumenValido && temperaturaValida;
    }

    @Override
    public Categoria getCategoria() {
        return Categoria.BEBIDA;
    }

    public double getVolumenMl() {
        return volumenMl;
    }

    public void setVolumenMl(double volumenMl) {
        this.volumenMl = volumenMl;
    }

    public double getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(double temperatura) {
        this.temperatura = temperatura;
    }

    public boolean isEsFria() {
        return esFria;
    }

    public void setEsFria(boolean esFria) {
        this.esFria = esFria;
    }

    public boolean isPreparacionEspecial() {
        return preparacionEspecial;
    }

    public void setPreparacionEspecial(boolean preparacionEspecial) {
        this.preparacionEspecial = preparacionEspecial;
    }

    @Override
    public String toString() {
        return "Bebida{" + super.toString() + ", volumenMl=" + volumenMl + ", temperatura=" + temperatura
                + ", esFria=" + esFria + ", preparacionEspecial=" + preparacionEspecial + "}";
    }
}

