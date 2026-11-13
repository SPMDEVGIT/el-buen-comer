package org.example.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DetallePedidoTest {

    @Test
    void subtotalPlatosFuertes() {
        Producto p = new PlatoFuerte("P1", "Bandeja", "d", 20000, 30, 500, TipoPreparacion.ASADO, true);
        assertEquals(48000, new DetallePedido(p, 2).subtotal(), 0.01);
    }

    @Test
    void subtotalBebidasEspeciales() {
        Producto b = new Bebida("B1", "Jugo", "d", 5000, 5, 500, 5, true, true);
        assertEquals(17000, new DetallePedido(b, 2).subtotal(), 0.01);
    }

    @Test
    void subtotalPostresRefrigerados() {
        Producto d = new Postre("D1", "Flan", "d", 10000, 10, 200, 4, true);
        assertEquals(36000, new DetallePedido(d, 3).subtotal(), 0.01);
    }

    @Test
    void cantidadCeroLanzaExcepcion() {
        Producto d = new Postre("D1", "Flan", "d", 10000, 10, 200, 4, true);
        assertThrows(IllegalArgumentException.class, () -> new DetallePedido(d, 0));
    }

    @Test
    void productoNullLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> new DetallePedido(null, 1));
    }
}
