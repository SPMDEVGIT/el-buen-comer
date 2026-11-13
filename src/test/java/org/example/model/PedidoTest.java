package org.example.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class PedidoTest {

    private Cliente cliente;
    private Producto plato;
    private Producto bebida;

    @BeforeEach
    void preparar() {
        cliente = new Cliente("1", "Ana Perez", "3000000000", "ana@mail.com", "Calle 1");
        plato = new PlatoFuerte("P1", "Bandeja", "d", 20000, 30, 500, TipoPreparacion.ASADO, true);
        bebida = new Bebida("B1", "Jugo", "d", 5000, 5, 500, 5, true, false);
    }

    private void llenar(Pedido pedido) {
        pedido.agregarDetalle(plato, 2);
        pedido.agregarDetalle(bebida, 1);
    }

    @Test
    void totalEnRestaurante() {
        Pedido pedido = new Pedido("PED1", LocalDate.now(), cliente);
        llenar(pedido);
        assertEquals(53500, pedido.calcularTotal(), 0.01);
    }

    @Test
    void totalADomicilioConRapidoExpress() {
        Pedido pedido = new Pedido("PED1", LocalDate.now(), cliente, new RapidoExpress(), 4);
        llenar(pedido);
        assertEquals(64500, pedido.calcularTotal(), 0.01);
    }

    @Test
    void pedidoValidoSeConfirmaUnaVez() {
        Pedido pedido = new Pedido("PED1", LocalDate.now(), cliente);
        llenar(pedido);
        assertTrue(pedido.confirmar());
        assertFalse(pedido.confirmar());
    }

    @Test
    void pedidoPendienteNoSePuedeEntregar() {
        Pedido pedido = new Pedido("PED1", LocalDate.now(), cliente);
        llenar(pedido);
        assertFalse(pedido.marcarEntregado());
    }

    @Test
    void domicilioDe15KmConRapidoExpressNoSeConfirma() {
        Pedido pedido = new Pedido("PED1", LocalDate.now(), cliente, new RapidoExpress(), 15);
        llenar(pedido);
        assertFalse(pedido.confirmar());
        assertEquals(EstadoPedido.PENDIENTE, pedido.getEstado());
    }
}
