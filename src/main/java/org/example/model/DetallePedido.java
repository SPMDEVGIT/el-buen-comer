package org.example.model;

public record DetallePedido(Producto producto, int cantidad) {

    public DetallePedido {
        if (producto == null) {
            throw new IllegalArgumentException("El producto no puede ser null");
        }
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero");
        }
    }

    public double subtotal() {
        return producto.calcularPrecioVenta() * cantidad;
    }
}