package org.example.model;

/**
 * Clase base de los productos del menu. Define los datos comunes y delega en las
 * subclases (plato fuerte, bebida, postre) el precio de venta, el control de calidad
 * y la categoria.
 */
public abstract class Producto {

    private String codigo;            // identificador unico dentro del restaurante
    private String nombre;
    private String descripcion;
    private double precioBase;        // precio antes de recargos, en pesos
    private int tiempoPreparacion;    // en minutos

    /** Crea un producto con los datos comunes a todas las categorias. */
    public Producto(String codigo, String nombre, String descripcion, double precioBase, int tiempoPreparacion) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precioBase = precioBase;
        this.tiempoPreparacion = tiempoPreparacion;
    }

    /** @return precio final al cliente: precio base mas los recargos propios de la categoria */
    public abstract double calcularPrecioVenta();

    /** @return true si el producto cumple los estandares de calidad de su categoria */
    public abstract boolean cumpleCalidad();

    /** @return categoria del producto (se usa para agrupar ingresos) */
    public abstract Categoria getCategoria();

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getPrecioBase() {
        return precioBase;
    }

    public void setPrecioBase(double precioBase) {
        this.precioBase = precioBase;
    }

    public int getTiempoPreparacion() {
        return tiempoPreparacion;
    }

    public void setTiempoPreparacion(int tiempoPreparacion) {
        this.tiempoPreparacion = tiempoPreparacion;
    }

    @Override
    public String toString() {
        return "Producto{codigo='" + codigo + "', nombre='" + nombre + "', descripcion='" + descripcion
                + "', precioBase=" + precioBase + ", tiempoPreparacion=" + tiempoPreparacion + "}";
    }
}
