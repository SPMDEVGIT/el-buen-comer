package org.example.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EntregaSeguraTest {

    private final EntregaSegura empresa = new EntregaSegura();

    @Test
    void tarifa5KmSinRecargo() {
        assertEquals(12000, empresa.calcularTarifa(5), 0.01);
    }

    @Test
    void tarifa6KmConRecargo() {
        assertEquals(16000, empresa.calcularTarifa(6), 0.01);
    }

    @Test
    void tarifa20Km() {
        assertEquals(30000, empresa.calcularTarifa(20), 0.01);
    }

    @Test
    void aceptaVeinteKm() {
        assertTrue(empresa.aceptaRecorrido(20));
    }

    @Test
    void rechazaVeintiunKm() {
        assertFalse(empresa.aceptaRecorrido(21));
    }
}
