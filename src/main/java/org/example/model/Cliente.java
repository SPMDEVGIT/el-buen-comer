package org.example.model;

import java.util.ArrayList;
import java.util.List;

/** Cliente del restaurante, identificado por su documento. Guarda su historial de pedidos. */
public class Cliente {

    private String documento;           // identificador unico
    private String nombreCompleto;
    private String telefono;
    private String correo;
    private String direccion;
    private List<Pedido> historialPedidos; // pedidos confirmados del cliente

    /** Crea un cliente con el historial vacio. */
    public Cliente(String documento, String nombreCompleto, String telefono, String correo, String direccion) {
        this.documento = documento;
        this.nombreCompleto = nombreCompleto;
        this.telefono = telefono;
        this.correo = correo;
        this.direccion = direccion;
        this.historialPedidos = new ArrayList<>();
    }

    /** Agrega un pedido al historial (el restaurante lo hace al confirmarlo). */
    public void agregarPedido(Pedido pedido) {
        historialPedidos.add(pedido);
    }

    public List<Pedido> getHistorialPedidos() {
        return historialPedidos;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    @Override
    public String toString() {
        return "Cliente{documento='" + documento + "', nombreCompleto='" + nombreCompleto
                + "', telefono='" + telefono + "', correo='" + correo + "', direccion='" + direccion + "'}";
    }
}

