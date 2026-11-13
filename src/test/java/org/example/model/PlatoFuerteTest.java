package org.example.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PlatoFuerteTest {

    private PlatoFuerte crear(double peso, int tiempo, boolean coccion) {
        return new PlatoFuerte("P1", "Bandeja", "Plato tipico", 20000, tiempo,
                peso, TipoPreparacion.ASADO, coccion);
    }

    @Test
    void precioVentaEs24000() {
        assertEquals(24000, crear(500, 30, true).calcularPrecioVenta(), 0.01);
    }

    @Test
    void cumpleCalidadConDatosValidos() {
        assertTrue(crear(500, 30, true).cumpleCalidad());
    }

    @Test
    void noCumpleCalidadConPesoBajo() {
        assertFalse(crear(150, 30, true).cumpleCalidad());
    }

    @Test
    void noCumpleCalidadConTiempoAlto() {
        assertFalse(crear(500, 70, true).cumpleCalidad());
    }

    @Test
    void noCumpleCalidadConCoccionNoAprobada() {
        assertFalse(crear(500, 30, false).cumpleCalidad());
    }
}
