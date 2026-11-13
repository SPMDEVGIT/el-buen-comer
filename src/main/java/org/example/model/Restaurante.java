package org.example.model;

import java.util.ArrayList;
import java.util.List;

public class Restaurante {

    private String nombre;
    private String nit;
    private String direccion;
    private List<Producto> productos;
    private List<Cliente> clientes;
    private List<Pedido> pedidos;

    public Restaurante(String nombre, String nit, String direccion) {
        this.nombre = nombre;
        this.nit = nit;
        this.direccion = direccion;
        this.productos = new ArrayList<>();
        this.clientes = new ArrayList<>();
        this.pedidos = new ArrayList<>();
    }

    // ---------- Registro y estado ----------

    public boolean registrarProducto(Producto producto) {
        if (producto == null || buscarProducto(producto.getCodigo()) != null) {
            return false;
        }
        productos.add(producto);
        return true;
    }

    public boolean registrarCliente(Cliente cliente) {
        if (cliente == null || buscarCliente(cliente.getDocumento()) != null) {
            return false;
        }
        clientes.add(cliente);
        return true;
    }

    public boolean registrarPedido(Pedido pedido) {
        if (pedido == null || buscarPedido(pedido.getCodigo()) != null) {
            return false;
        }
        if (pedido.getCliente() == null || buscarCliente(pedido.getCliente().getDocumento()) == null) {
            return false;
        }
        pedido.setEstado(EstadoPedido.PENDIENTE);
        pedidos.add(pedido);
        return true;
    }

    public boolean confirmarPedido(String codigo) {
        Pedido pedido = buscarPedido(codigo);
        if (pedido == null) {
            return false;
        }
        for (DetallePedido detalle : pedido.getDetalles()) {
            if (buscarProducto(detalle.producto().getCodigo()) == null) {
                return false;
            }
        }
        if (pedido.confirmar()) {
            pedido.getCliente().agregarPedido(pedido);
            return true;
        }
        return false;
    }

    public boolean marcarPedidoEntregado(String codigo) {
        Pedido pedido = buscarPedido(codigo);
        return pedido != null && pedido.marcarEntregado();
    }

    public boolean cancelarPedido(String codigo) {
        Pedido pedido = buscarPedido(codigo);
        return pedido != null && pedido.cancelar();
    }

    // ---------- Consultas ----------

    public double calcularTotalPedido(String codigo) {
        Pedido pedido = buscarPedido(codigo);
        if (pedido == null) {
            throw new IllegalArgumentException("El pedido no existe: " + codigo);
        }
        return pedido.calcularTotal();
    }

    public List<Pedido> consultarHistorialCliente(String documento) {
        Cliente cliente = buscarCliente(documento);
        if (cliente == null) {
            throw new IllegalArgumentException("El cliente no existe: " + documento);
        }
        return cliente.getHistorialPedidos();
    }

    public List<Producto> consultarProductosDisponibles() {
        List<Producto> resultado = new ArrayList<>();
        for (Producto producto : productos) {
            if (producto.cumpleCalidad()) {
                resultado.add(producto);
            }
        }
        return resultado;
    }

    public List<Producto> consultarProductosNoAptos() {
        List<Producto> resultado = new ArrayList<>();
        for (Producto producto : productos) {
            if (!producto.cumpleCalidad()) {
                resultado.add(producto);
            }
        }
        return resultado;
    }

    // ---------- Ventas ----------

    public double calcularIngresosRestaurante() {
        double suma = 0;
        for (Pedido pedido : pedidos) {
            if (esPedidoValido(pedido)) {
                suma += pedido.calcularSubtotalProductos();
            }
        }
        return suma;
    }

    public double calcularIngresosDomicilios() {
        double suma = 0;
        for (Pedido pedido : pedidos) {
            if (esPedidoValido(pedido)) {
                suma += pedido.calcularCostoDomicilio();
            }
        }
        return suma;
    }

    public Producto obtenerProductoMasVendido() {
        Producto masVendido = null;
        int maxUnidades = 0;
        for (Producto producto : productos) {
            int unidades = contarUnidadesVendidas(producto);
            if (unidades > maxUnidades) {
                maxUnidades = unidades;
                masVendido = producto;
            }
        }
        return masVendido;
    }

    public Categoria obtenerCategoriaMayorIngresos() {
        Categoria mejor = null;
        double maxIngresos = 0;
        for (Categoria categoria : Categoria.values()) {
            double ingresos = calcularIngresosCategoria(categoria);
            if (ingresos > maxIngresos) {
                maxIngresos = ingresos;
                mejor = categoria;
            }
        }
        return mejor;
    }

    // ---------- Busquedas ----------

    public Producto buscarProducto(String codigo) {
        for (Producto producto : productos) {
            if (producto.getCodigo().equals(codigo)) {
                return producto;
            }
        }
        return null;
    }

    public Cliente buscarCliente(String documento) {
        for (Cliente cliente : clientes) {
            if (cliente.getDocumento().equals(documento)) {
                return cliente;
            }
        }
        return null;
    }

    public Pedido buscarPedido(String codigo) {
        for (Pedido pedido : pedidos) {
            if (pedido.getCodigo().equals(codigo)) {
                return pedido;
            }
        }
        return null;
    }

    // ---------- Apoyo privado ----------

    private boolean esPedidoValido(Pedido pedido) {
        return pedido.getEstado() == EstadoPedido.CONFIRMADO
                || pedido.getEstado() == EstadoPedido.ENTREGADO;
    }

    private int contarUnidadesVendidas(Producto producto) {
        int unidades = 0;
        for (Pedido pedido : pedidos) {
            if (!esPedidoValido(pedido)) {
                continue;
            }
            for (DetallePedido detalle : pedido.getDetalles()) {
                if (detalle.producto().getCodigo().equals(producto.getCodigo())) {
                    unidades += detalle.cantidad();
                }
            }
        }
        return unidades;
    }

    private double calcularIngresosCategoria(Categoria categoria) {
        double suma = 0;
        for (Pedido pedido : pedidos) {
            if (!esPedidoValido(pedido)) {
                continue;
            }
            for (DetallePedido detalle : pedido.getDetalles()) {
                if (detalle.producto().getCategoria() == categoria) {
                    suma += detalle.subtotal();
                }
            }
        }
        return suma;
    }

    // ---------- Getters y setters ----------

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public List<Cliente> getClientes() {
        return clientes;
    }

    public List<Pedido> getPedidos() {
        return pedidos;
    }

    @Override
    public String toString() {
        return "Restaurante{nombre='" + nombre + "', nit='" + nit + "', direccion='" + direccion
                + "', productos=" + productos.size() + ", clientes=" + clientes.size()
                + ", pedidos=" + pedidos.size() + "}";
    }
}