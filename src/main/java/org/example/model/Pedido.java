package org.example.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Pedido {

    private String codigo;
    private LocalDate fecha;
    private Cliente cliente;
    private ModalidadEntrega modalidad;
    private EstadoPedido estado;
    private List<DetallePedido> detalles;
    private EmpresaDomicilio empresaDomicilio;
    private double distanciaKm;

    // Pedido para consumo en el restaurante
    public Pedido(String codigo, LocalDate fecha, Cliente cliente) {
        this.codigo = codigo;
        this.fecha = fecha;
        this.cliente = cliente;
        this.modalidad = ModalidadEntrega.RESTAURANTE;
        this.estado = EstadoPedido.PENDIENTE;
        this.detalles = new ArrayList<>();
    }

    // Pedido a domicilio
    public Pedido(String codigo, LocalDate fecha, Cliente cliente,
                  EmpresaDomicilio empresaDomicilio, double distanciaKm) {
        this(codigo, fecha, cliente);
        this.modalidad = ModalidadEntrega.DOMICILIO;
        this.empresaDomicilio = empresaDomicilio;
        this.distanciaKm = distanciaKm;
    }

    public void agregarDetalle(Producto producto, int cantidad) {
        detalles.add(new DetallePedido(producto, cantidad));
    }

    public double calcularSubtotalProductos() {
        double suma = 0;
        for (DetallePedido detalle : detalles) {
            suma += detalle.subtotal();
        }
        return suma;
    }

    public double calcularCostoDomicilio() {
        if (modalidad == ModalidadEntrega.DOMICILIO && empresaDomicilio != null) {
            return empresaDomicilio.calcularTarifa(distanciaKm);
        }
        return 0;
    }

    public double calcularTotal() {
        return calcularSubtotalProductos() + calcularCostoDomicilio();
    }

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

    public boolean confirmar() {
        if (puedeConfirmarse()) {
            estado = EstadoPedido.CONFIRMADO;
            return true;
        }
        return false;
    }

    public boolean marcarEntregado() {
        if (estado == EstadoPedido.CONFIRMADO) {
            estado = EstadoPedido.ENTREGADO;
            return true;
        }
        return false;
    }

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
