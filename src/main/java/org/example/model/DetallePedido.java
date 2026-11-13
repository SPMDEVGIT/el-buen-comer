package org.example.model;

/**
 * Linea de un pedido: un producto y la cantidad pedida. Es inmutable.
 *
 * @param producto producto pedido (no puede ser null)
 * @param cantidad unidades pedidas (debe ser mayor que cero)
 */
public record DetallePedido(Producto producto, int cantidad) {

    /** Constructor compacto: valida los datos y lanza IllegalArgumentException si son invalidos. */
    public DetallePedido {
        if (producto == null) {
            throw new IllegalArgumentException("El producto no puede ser null");
        }
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero");
        }
    }

    /** @return precio de venta unitario del producto multiplicado por la cantidad */
    public double subtotal() {
        return producto.calcularPrecioVenta() * cantidad;
    }
}