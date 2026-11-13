package org.example.model;

/**
 * Ciclo de vida de un pedido: nace PENDIENTE, pasa a CONFIRMADO y luego a ENTREGADO;
 * puede pasar a CANCELADO desde PENDIENTE o CONFIRMADO.
 */
public enum EstadoPedido {
    PENDIENTE, CONFIRMADO, ENTREGADO, CANCELADO
}