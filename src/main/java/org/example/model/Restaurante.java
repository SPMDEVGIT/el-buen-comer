package org.example.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase principal del dominio: administra el menu, los clientes y los pedidos, y
 * calcula las ventas. Las operaciones de registro y cambio de estado devuelven
 * true/false segun hayan podido realizarse.
 */
public class Restaurante {

    private String nombre;
    private String nit;
    private String direccion;
    private List<Producto> productos;
    private List<Cliente> clientes;
    private List<Pedido> pedidos;

    /** Crea un restaurante con menu, clientes y pedidos vacios. */
    public Restaurante(String nombre, String nit, String direccion) {
        this.nombre = nombre;
        this.nit = nit;
        this.direccion = direccion;
        this.productos = new ArrayList<>();
        this.clientes = new ArrayList<>();
        this.pedidos = new ArrayList<>();
    }

    // ---------- Registro y estado ----------

    /** Agrega el producto al menu; false si es null o ya existe el codigo. */
    public boolean registrarProducto(Producto producto) {
        if (producto == null || buscarProducto(producto.getCodigo()) != null) {
            return false;
        }
        productos.add(producto);
        return true;
    }

    /** Registra el cliente; false si es null o ya existe el documento. */
    public boolean registrarCliente(Cliente cliente) {
        if (cliente == null || buscarCliente(cliente.getDocumento()) != null) {
            return false;
        }
        clientes.add(cliente);
        return true;
    }

    /**
     * Registra el pedido en estado PENDIENTE. Falla si es null, si el codigo ya existe
     * o si su cliente no esta registrado en el restaurante.
     */
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

    /**
     * Confirma el pedido si existe, todos sus productos pertenecen al menu y el propio
     * pedido puede confirmarse. Al confirmar, lo agrega al historial del cliente.
     */
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

    /** Marca como entregado un pedido confirmado; false si no existe o no esta CONFIRMADO. */
    public boolean marcarPedidoEntregado(String codigo) {
        Pedido pedido = buscarPedido(codigo);
        return pedido != null && pedido.marcarEntregado();
    }

    /** Cancela un pedido PENDIENTE o CONFIRMADO; false si no existe o ya no se puede cancelar. */
    public boolean cancelarPedido(String codigo) {
        Pedido pedido = buscarPedido(codigo);
        return pedido != null && pedido.cancelar();
    }

    // ---------- Consultas ----------

    /** @return total (productos + domicilio) del pedido; lanza IllegalArgumentException si no existe */
    public double calcularTotalPedido(String codigo) {
        Pedido pedido = buscarPedido(codigo);
        if (pedido == null) {
            throw new IllegalArgumentException("El pedido no existe: " + codigo);
        }
        return pedido.calcularTotal();
    }

    /** @return pedidos confirmados del cliente; lanza IllegalArgumentException si no existe */
    public List<Pedido> consultarHistorialCliente(String documento) {
        Cliente cliente = buscarCliente(documento);
        if (cliente == null) {
            throw new IllegalArgumentException("El cliente no existe: " + documento);
        }
        return cliente.getHistorialPedidos();
    }

    /** @return productos del menu que cumplen los estandares de calidad */
    public List<Producto> consultarProductosDisponibles() {
        List<Producto> resultado = new ArrayList<>();
        for (Producto producto : productos) {
            if (producto.cumpleCalidad()) {
                resultado.add(producto);
            }
        }
        return resultado;
    }

    /** @return productos del menu que NO cumplen los estandares de calidad */
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

    /** @return ventas de productos de pedidos CONFIRMADOS o ENTREGADOS (sin domicilios) */
    public double calcularIngresosRestaurante() {
        double suma = 0;
        for (Pedido pedido : pedidos) {
            if (esPedidoValido(pedido)) {
                suma += pedido.calcularSubtotalProductos();
            }
        }
        return suma;
    }

    /** @return suma de los costos de domicilio de pedidos CONFIRMADOS o ENTREGADOS */
    public double calcularIngresosDomicilios() {
        double suma = 0;
        for (Pedido pedido : pedidos) {
            if (esPedidoValido(pedido)) {
                suma += pedido.calcularCostoDomicilio();
            }
        }
        return suma;
    }

    /** @return producto con mas unidades vendidas (en caso de empate, el primero registrado); null si no hay ventas */
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

    /** @return categoria con mayores ingresos por productos vendidos; null si no hay ventas */
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

    /** @return el producto con ese codigo, o null si no existe */
    public Producto buscarProducto(String codigo) {
        for (Producto producto : productos) {
            if (producto.getCodigo().equals(codigo)) {
                return producto;
            }
        }
        return null;
    }

    /** @return el cliente con ese documento, o null si no existe */
    public Cliente buscarCliente(String documento) {
        for (Cliente cliente : clientes) {
            if (cliente.getDocumento().equals(documento)) {
                return cliente;
            }
        }
        return null;
    }

    /** @return el pedido con ese codigo, o null si no existe */
    public Pedido buscarPedido(String codigo) {
        for (Pedido pedido : pedidos) {
            if (pedido.getCodigo().equals(codigo)) {
                return pedido;
            }
        }
        return null;
    }

    // ---------- Apoyo privado ----------

    /** Un pedido cuenta para las ventas solo si esta CONFIRMADO o ENTREGADO. */
    private boolean esPedidoValido(Pedido pedido) {
        return pedido.getEstado() == EstadoPedido.CONFIRMADO
                || pedido.getEstado() == EstadoPedido.ENTREGADO;
    }

    /** Suma las unidades del producto en pedidos validos. */
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

    /** Suma los subtotales de las lineas de esa categoria en pedidos validos. */
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