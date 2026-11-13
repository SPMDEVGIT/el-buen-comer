package org.example.model;

/**
 * Bebida del menu. Su calidad depende del volumen y de la temperatura de servicio,
 * que cambia segun sea fria o caliente.
 */
public class Bebida extends Producto {

    private double volumenMl;           // volumen servido en mililitros
    private double temperatura;         // temperatura de servicio en grados Celsius
    private boolean esFria;             // true = bebida fria, false = caliente
    private boolean preparacionEspecial; // genera un recargo fijo en el precio

    /** Crea una bebida; los cinco primeros parametros los recibe {@link Producto}. */
    public Bebida(String codigo, String nombre, String descripcion, double precioBase,
                  int tiempoPreparacion, double volumenMl, double temperatura,
                  boolean esFria, boolean preparacionEspecial) {
        super(codigo, nombre, descripcion, precioBase, tiempoPreparacion);
        this.volumenMl = volumenMl;
        this.temperatura = temperatura;
        this.esFria = esFria;
        this.preparacionEspecial = preparacionEspecial;
    }

    /** Precio base + 10 % y, si requiere preparacion especial, + $3.000 fijos. */
    @Override
    public double calcularPrecioVenta() {
        double precio = getPrecioBase() * 1.10;
        if (preparacionEspecial) {
            precio += 3000;
        }
        return precio;
    }

    /**
     * Cumple si el volumen esta entre 150 y 1500 ml y la temperatura esta en rango:
     * 0-10 °C para bebidas frias, 55-85 °C para calientes.
     */
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

