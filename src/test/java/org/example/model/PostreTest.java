package org.example.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PostreTest {

    private Postre crear(double peso, double temp, boolean refrigeracion) {
        return new Postre("D1", "Tres leches", "Postre frio", 10000, 15, peso, temp, refrigeracion);
    }

    @Test
    void precioVentaSinRefrigeracion() {
        assertEquals(11500, crear(200, 20, false).calcularPrecioVenta(), 0.01);
    }

    @Test
    void precioVentaRefrigerado() {
        assertEquals(12000, crear(200, 4, true).calcularPrecioVenta(), 0.01);
    }

    @Test
    void cumpleCalidadRefrigerado() {
        assertTrue(crear(200, 4, true).cumpleCalidad());
    }

    @Test
    void noCumpleRefrigeradoA10Grados() {
        assertFalse(crear(200, 10, true).cumpleCalidad());
    }

    @Test
    void noCumpleConPesoAlto() {
        assertFalse(crear(600, 4, true).cumpleCalidad());
    }
}
