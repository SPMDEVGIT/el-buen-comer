package org.example.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RapidoExpressTest {

    private final RapidoExpress empresa = new RapidoExpress();

    @Test
    void tarifa4Km() {
        assertEquals(11000, empresa.calcularTarifa(4), 0.01);
    }

    @Test
    void tarifa12Km() {
        assertEquals(23000, empresa.calcularTarifa(12), 0.01);
    }

    @Test
    void aceptaDoceKm() {
        assertTrue(empresa.aceptaRecorrido(12));
    }

    @Test
    void rechazaDoceYMedioKm() {
        assertFalse(empresa.aceptaRecorrido(12.5));
    }

    @Test
    void rechazaCeroKm() {
        assertFalse(empresa.aceptaRecorrido(0));
    }
}
