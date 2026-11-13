package org.example.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class RestauranteTest {

    private Restaurante restaurante;
    private Cliente cliente;
    private Producto plato;
    private Producto bebida;

    @BeforeEach
    void preparar() {
        restaurante = new Restaurante("El Buen Comer", "900123", "Carrera 1");
        cliente = new Cliente("1", "Ana Perez", "3000000000", "ana@mail.com", "Calle 1");
        plato = new PlatoFuerte("P1", "Bandeja", "d", 20000, 30, 500, TipoPreparacion.ASADO, true);
        bebida = new Bebida("B1", "Jugo", "d", 5000, 5, 500, 5, true, false);
        restaurante.registrarProducto(plato);
        restaurante.registrarProducto(bebida);
        restaurante.registrarCliente(cliente);
    }

    private void llenar(Pedido pedido) {
        pedido.agregarDetalle(plato, 2);
        pedido.agregarDetalle(bebida, 1);
    }

    @Test
    void noRegistraProductoDuplicado() {
        Producto repetido = new Bebida("B1", "Otro", "d", 4000, 5, 500, 5, true, false);
        assertFalse(restaurante.registrarProducto(repetido));
    }

    @Test
    void noRegistraClienteDuplicado() {
        Cliente repetido = new Cliente("1", "Otro", "3", "o@mail.com", "Calle 2");
        assertFalse(restaurante.registrarCliente(repetido));
    }

    @Test
    void noConfirmaPedidoConProductoNoApto() {
        Producto malo = new PlatoFuerte("P2", "Crudo", "d", 20000, 30, 500, TipoPreparacion.FRITO, false);
        restaurante.registrarProducto(malo);
        Pedido pedido = new Pedido("PED1", LocalDate.now(), cliente);
        pedido.agregarDetalle(malo, 1);
        restaurante.registrarPedido(pedido);
        assertFalse(restaurante.confirmarPedido("PED1"));
        assertEquals(EstadoPedido.PENDIENTE, pedido.getEstado());
    }

    @Test
    void pedidoConfirmadoApareceEnHistorial() {
        Pedido pedido = new Pedido("PED1", LocalDate.now(), cliente);
        llenar(pedido);
        restaurante.registrarPedido(pedido);
        assertTrue(restaurante.confirmarPedido("PED1"));
        assertTrue(restaurante.consultarHistorialCliente("1").contains(pedido));
    }

    @Test
    void ingresosConPedidoConfirmadoYCancelado() {
        Pedido domicilio = new Pedido("PED1", LocalDate.now(), cliente, new RapidoExpress(), 4);
        llenar(domicilio);
        Pedido cancelado = new Pedido("PED2", LocalDate.now(), cliente);
        llenar(cancelado);
        restaurante.registrarPedido(domicilio);
        restaurante.registrarPedido(cancelado);
        restaurante.confirmarPedido("PED1");
        restaurante.cancelarPedido("PED2");
        assertEquals(53500, restaurante.calcularIngresosRestaurante(), 0.01);
        assertEquals(11000, restaurante.calcularIngresosDomicilios(), 0.01);
    }
}
