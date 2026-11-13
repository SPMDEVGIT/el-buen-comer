package org.example.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BebidaTest {

    private Bebida crear(double volumen, double temp, boolean fria, boolean especial) {
        return new Bebida("B1", "Jugo", "Jugo natural", 5000, 5, volumen, temp, fria, especial);
    }

    @Test
    void precioVentaSinPreparacionEspecial() {
        assertEquals(5500, crear(500, 5, true, false).calcularPrecioVenta(), 0.01);
    }

    @Test
    void precioVentaConPreparacionEspecial() {
        assertEquals(8500, crear(500, 5, true, true).calcularPrecioVenta(), 0.01);
    }

    @Test
    void cumpleCalidadBebidaFria() {
        assertTrue(crear(500, 5, true, false).cumpleCalidad());
    }

    @Test
    void noCumpleBebidaCalienteA40Grados() {
        assertFalse(crear(500, 40, false, false).cumpleCalidad());
    }

    @Test
    void noCumpleConVolumenBajo() {
        assertFalse(crear(100, 5, true, false).cumpleCalidad());
    }
}
