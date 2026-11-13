package org.example.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Pedido de un cliente: lista de productos, modalidad de entrega y estado.
 * Calcula sus totales y controla las transiciones de estado permitidas.
 */
public class Pedido {

    private String codigo;                  // identificador unico
    private LocalDate fecha;
    private Cliente cliente;
    private ModalidadEntrega modalidad;
    private EstadoPedido estado;
    private List<DetallePedido> detalles;
    private EmpresaDomicilio empresaDomicilio; // solo en pedidos a domicilio
    private double distanciaKm;                // solo en pedidos a domicilio

    /** Pedido para consumo en el restaurante: modalidad RESTAURANTE, estado PENDIENTE, sin domicilio. */
    public Pedido(String codigo, LocalDate fecha, Cliente cliente) {
        this.codigo = codigo;
        this.fecha = fecha;
        this.cliente = cliente;
        this.modalidad = ModalidadEntrega.RESTAURANTE;
        this.estado = EstadoPedido.PENDIENTE;
        this.detalles = new ArrayList<>();
    }

    /** Pedido a domicilio: igual al anterior pero con modalidad DOMICILIO, empresa y distancia en km. */
    public Pedido(String codigo, LocalDate fecha, Cliente cliente,
                  EmpresaDomicilio empresaDomicilio, double distanciaKm) {
        this(codigo, fecha, cliente);
        this.modalidad = ModalidadEntrega.DOMICILIO;
        this.empresaDomicilio = empresaDomicilio;
        this.distanciaKm = distanciaKm;
    }

    /** Agrega una linea al pedido; lanza IllegalArgumentException si el producto es null o la cantidad no es positiva. */
    public void agregarDetalle(Producto producto, int cantidad) {
        detalles.add(new DetallePedido(producto, cantidad));
    }

    /** @return suma de los subtotales de todas las lineas (sin domicilio) */
    public double calcularSubtotalProductos() {
        double suma = 0;
        for (DetallePedido detalle : detalles) {
            suma += detalle.subtotal();
        }
        return suma;
    }

    /** @return tarifa de la empresa para la distancia del pedido, o 0 si no es a domicilio */
    public double calcularCostoDomicilio() {
        if (modalidad == ModalidadEntrega.DOMICILIO && empresaDomicilio != null) {
            return empresaDomicilio.calcularTarifa(distanciaKm);
        }
        return 0;
    }

    /** @return subtotal de productos mas costo de domicilio */
    public double calcularTotal() {
        return calcularSubtotalProductos() + calcularCostoDomicilio();
    }

    /**
     * Un pedido puede confirmarse si esta PENDIENTE, tiene al menos un producto, todos
     * sus productos cumplen calidad y, si es a domicilio, la empresa acepta la distancia.
     */
    public boolean puedeConfirmarse() {
        if (estado != EstadoPedido.PENDIENTE || detalles.isEmpty()) {
            return false;
        }
        for (DetallePedido detalle : detalles) {
            if (!detalle.producto().cumpleCalidad()) {
                return false;
            }
        }
        if (modalidad == ModalidadEntrega.DOMICILIO) {
            return empresaDomicilio != null && empresaDomicilio.aceptaRecorrido(distanciaKm);
        }
        return true;
    }

    /** PENDIENTE -> CONFIRMADO si {@link #puedeConfirmarse()}; @return true si cambio de estado */
    public boolean confirmar() {
        if (puedeConfirmarse()) {
            estado = EstadoPedido.CONFIRMADO;
            return true;
        }
        return false;
    }

    /** CONFIRMADO -> ENTREGADO; @return false si el pedido no estaba CONFIRMADO */
    public boolean marcarEntregado() {
        if (estado == EstadoPedido.CONFIRMADO) {
            estado = EstadoPedido.ENTREGADO;
            return true;
        }
        return false;
    }

    /** PENDIENTE o CONFIRMADO -> CANCELADO; @return false si ya estaba entregado o cancelado */
    public boolean cancelar() {
        if (estado == EstadoPedido.PENDIENTE || estado == EstadoPedido.CONFIRMADO) {
            estado = EstadoPedido.CANCELADO;
            return true;
        }
        return false;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public ModalidadEntrega getModalidad() {
        return modalidad;
    }

    public void setModalidad(ModalidadEntrega modalidad) {
        this.modalidad = modalidad;
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    public void setEstado(EstadoPedido estado) {
        this.estado = estado;
    }

    public List<DetallePedido> getDetalles() {
        return detalles;
    }

    public EmpresaDomicilio getEmpresaDomicilio() {
        return empresaDomicilio;
    }

    public void setEmpresaDomicilio(EmpresaDomicilio empresaDomicilio) {
        this.empresaDomicilio = empresaDomicilio;
    }

    public double getDistanciaKm() {
        return distanciaKm;
    }

    public void setDistanciaKm(double distanciaKm) {
        this.distanciaKm = distanciaKm;
    }

    @Override
    public String toString() {
        return "Pedido{codigo='" + codigo + "', fecha=" + fecha + ", cliente=" + cliente.getDocumento()
                + ", modalidad=" + modalidad + ", estado=" + estado + ", detalles=" + detalles.size()
                + ", distanciaKm=" + distanciaKm + "}";
    }
}
