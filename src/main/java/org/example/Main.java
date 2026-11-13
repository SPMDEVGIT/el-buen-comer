package org.example;

import org.example.model.*;

import javax.swing.JOptionPane;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

/**
 * Interfaz de usuario del sistema, basada en cuadros de dialogo de Swing (JOptionPane).
 * Muestra un menu en bucle y delega la logica de negocio en {@link Restaurante}.
 * Si el usuario cierra o cancela un dialogo se lanza IllegalStateException, que el
 * bucle principal interpreta como "operacion cancelada".
 */
public class Main {

    private static final String TITULO = "El Buen Comer";
    private static final String CANCELADO = "Operacion cancelada";

    private static Restaurante restaurante; // unico restaurante en memoria (no hay persistencia)

    /** Carga los datos de demostracion y atiende el menu hasta que el usuario elige salir. */
    public static void main(String[] args) {
        cargarDatosDemostracion();

        String[] opciones = {
                "1. Registrar producto",
                "2. Registrar cliente",
                "3. Registrar pedido",
                "4. Confirmar pedido",
                "5. Calcular total de un pedido",
                "6. Consultar historial de un cliente",
                "7. Actualizar estado de un pedido",
                "8. Consultar ventas",
                "9. Salir"
        };

        boolean salir = false;
        while (!salir) {
            Object seleccion = JOptionPane.showInputDialog(null, "Seleccione una opcion:",
                    TITULO + " - Menu", JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);
            if (seleccion == null || seleccion.equals(opciones[8])) {
                salir = true;
                continue;
            }
            // cada opcion se ejecuta aparte; los errores de validacion se muestran sin cerrar el programa
            try {
                if (seleccion.equals(opciones[0])) {
                    registrarProducto();
                } else if (seleccion.equals(opciones[1])) {
                    registrarCliente();
                } else if (seleccion.equals(opciones[2])) {
                    registrarPedido();
                } else if (seleccion.equals(opciones[3])) {
                    confirmarPedido();
                } else if (seleccion.equals(opciones[4])) {
                    calcularTotalPedido();
                } else if (seleccion.equals(opciones[5])) {
                    consultarHistorialCliente();
                } else if (seleccion.equals(opciones[6])) {
                    actualizarEstadoPedido();
                } else if (seleccion.equals(opciones[7])) {
                    consultarVentas();
                }
            } catch (IllegalStateException e) {
                // el usuario cancelo un dialogo
                mostrar(CANCELADO, JOptionPane.INFORMATION_MESSAGE);
            } catch (IllegalArgumentException e) {
                mostrar("Error: " + e.getMessage(), JOptionPane.ERROR_MESSAGE);
            }
        }
        mostrar("Hasta pronto", JOptionPane.INFORMATION_MESSAGE);
    }

    // ---------- Datos de demostracion ----------

    /** Crea el restaurante con un cliente y cuatro productos (uno de ellos no apto) para poder probar el menu. */
    private static void cargarDatosDemostracion() {
        restaurante = new Restaurante("El Buen Comer", "900.123.456-7", "Carrera 14 # 12-30, Armenia");
        restaurante.registrarCliente(new Cliente("1094000111", "Maria Lopez", "3105551234",
                "maria@correo.com", "Calle 10 # 5-20"));
        restaurante.registrarProducto(new PlatoFuerte("P01", "Bandeja paisa", "Plato tipico completo",
                20000, 30, 600, TipoPreparacion.PLANCHA, true));
        restaurante.registrarProducto(new Bebida("B01", "Limonada de coco", "Limonada helada",
                5000, 5, 400, 5, true, true));
        restaurante.registrarProducto(new Postre("D01", "Tres leches", "Postre refrigerado",
                10000, 15, 200, 4, true));
        // producto que no cumple calidad (coccion no aprobada y peso bajo)
        restaurante.registrarProducto(new PlatoFuerte("P99", "Costillas en prueba", "Aun sin aprobar",
                25000, 45, 150, TipoPreparacion.ASADO, false));
    }

    // ---------- Opciones del menu ----------

    /** Opcion 1: pide los datos comunes y los propios del tipo elegido, y registra el producto. */
    private static void registrarProducto() {
        String[] tipos = {"Plato fuerte", "Bebida", "Postre"};
        int tipo = elegirOpcion("Tipo de producto", tipos);

        String codigo = leerTexto("Codigo del producto:");
        if (restaurante.buscarProducto(codigo) != null) {
            throw new IllegalArgumentException("Ya existe un producto con el codigo " + codigo);
        }
        String nombre = leerTexto("Nombre:");
        String descripcion = leerTexto("Descripcion:");
        double precioBase = leerDecimal("Precio base:");
        int tiempo = leerEntero("Tiempo de preparacion (minutos):");

        Producto producto;
        if (tipo == 0) {
            double peso = leerDecimal("Peso (gramos):");
            TipoPreparacion preparacion = TipoPreparacion.values()[
                    elegirOpcion("Tipo de preparacion", nombresEnum(TipoPreparacion.values()))];
            boolean coccion = leerSiNo("¿Coccion aprobada?");
            producto = new PlatoFuerte(codigo, nombre, descripcion, precioBase, tiempo, peso, preparacion, coccion);
        } else if (tipo == 1) {
            double volumen = leerDecimal("Volumen (ml):");
            double temperatura = leerDecimal("Temperatura (°C):");
            boolean fria = leerSiNo("¿Es una bebida fria?");
            boolean especial = leerSiNo("¿Requiere preparacion especial?");
            producto = new Bebida(codigo, nombre, descripcion, precioBase, tiempo, volumen, temperatura, fria, especial);
        } else {
            double peso = leerDecimal("Peso (gramos):");
            double temperatura = leerDecimal("Temperatura de almacenamiento (°C):");
            boolean refrigeracion = leerSiNo("¿Requiere refrigeracion?");
            producto = new Postre(codigo, nombre, descripcion, precioBase, tiempo, peso, temperatura, refrigeracion);
        }

        if (restaurante.registrarProducto(producto)) {
            String calidad = producto.cumpleCalidad() ? "cumple calidad" : "NO cumple calidad (no apto)";
            mostrar("Producto registrado: " + producto.getNombre() + "\nPrecio de venta: "
                            + formatearPesos(producto.calcularPrecioVenta()) + "\nEstado: " + calidad,
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            mostrar("No se pudo registrar el producto", JOptionPane.ERROR_MESSAGE);
        }
    }

    /** Opcion 2: registra un cliente nuevo; rechaza documentos repetidos. */
    private static void registrarCliente() {
        String documento = leerTexto("Documento:");
        if (restaurante.buscarCliente(documento) != null) {
            throw new IllegalArgumentException("Ya existe un cliente con el documento " + documento);
        }
        String nombre = leerTexto("Nombre completo:");
        String telefono = leerTexto("Telefono:");
        String correo = leerTexto("Correo:");
        String direccion = leerTexto("Direccion:");

        if (restaurante.registrarCliente(new Cliente(documento, nombre, telefono, correo, direccion))) {
            mostrar("Cliente registrado: " + nombre, JOptionPane.INFORMATION_MESSAGE);
        } else {
            mostrar("No se pudo registrar el cliente", JOptionPane.ERROR_MESSAGE);
        }
    }

    /** Opcion 3: crea un pedido (en restaurante o a domicilio) y le agrega productos en un bucle. */
    private static void registrarPedido() {
        String codigo = leerTexto("Codigo del pedido:");
        if (restaurante.buscarPedido(codigo) != null) {
            throw new IllegalArgumentException("Ya existe un pedido con el codigo " + codigo);
        }
        String documento = leerTexto("Documento del cliente:");
        Cliente cliente = restaurante.buscarCliente(documento);
        if (cliente == null) {
            throw new IllegalArgumentException("El cliente no esta registrado");
        }

        int modalidad = elegirOpcion("Modalidad de entrega", nombresEnum(ModalidadEntrega.values()));
        Pedido pedido;
        if (ModalidadEntrega.values()[modalidad] == ModalidadEntrega.DOMICILIO) {
            int empresa = elegirOpcion("Empresa de domicilio", new String[]{"RapidoExpress", "EntregaSegura"});
            EmpresaDomicilio empresaDomicilio = empresa == 0 ? new RapidoExpress() : new EntregaSegura();
            double distancia = leerDecimal("Distancia (km):");
            pedido = new Pedido(codigo, LocalDate.now(), cliente, empresaDomicilio, distancia);
        } else {
            pedido = new Pedido(codigo, LocalDate.now(), cliente);
        }

        if (!restaurante.registrarPedido(pedido)) {
            mostrar("No se pudo registrar el pedido", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // se agregan productos hasta que el usuario termine
        boolean seguir = true;
        while (seguir) {
            String codigoProducto = leerTexto("Codigo del producto a agregar:");
            Producto producto = restaurante.buscarProducto(codigoProducto);
            if (producto == null) {
                mostrar("No existe el producto " + codigoProducto, JOptionPane.ERROR_MESSAGE);
            } else {
                int cantidad = leerEntero("Cantidad:");
                try {
                    pedido.agregarDetalle(producto, cantidad);
                    mostrar("Agregado: " + cantidad + " x " + producto.getNombre(), JOptionPane.INFORMATION_MESSAGE);
                } catch (IllegalArgumentException e) {
                    mostrar("Error: " + e.getMessage(), JOptionPane.ERROR_MESSAGE);
                }
            }
            seguir = JOptionPane.showConfirmDialog(null, "¿Desea agregar otro producto?", TITULO,
                    JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
        }

        mostrar("Pedido " + codigo + " registrado en estado PENDIENTE con " + pedido.getDetalles().size()
                + " producto(s)", JOptionPane.INFORMATION_MESSAGE);
    }

    /** Opcion 4: confirma un pedido pendiente; si no se puede, explica el motivo. */
    private static void confirmarPedido() {
        String codigo = leerTexto("Codigo del pedido a confirmar:");
        Pedido pedido = restaurante.buscarPedido(codigo);
        if (pedido == null) {
            throw new IllegalArgumentException("El pedido no existe");
        }
        if (restaurante.confirmarPedido(codigo)) {
            mostrar("Pedido " + codigo + " confirmado. Total: "
                    + formatearPesos(pedido.calcularTotal()), JOptionPane.INFORMATION_MESSAGE);
        } else {
            mostrar("No se pudo confirmar el pedido.\nMotivo: " + motivoNoConfirmar(pedido),
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /** Opcion 5: muestra subtotal de productos, costo de domicilio y total de un pedido. */
    private static void calcularTotalPedido() {
        String codigo = leerTexto("Codigo del pedido:");
        double total = restaurante.calcularTotalPedido(codigo);
        Pedido pedido = restaurante.buscarPedido(codigo);
        mostrar("Pedido " + codigo + "\nSubtotal productos: " + formatearPesos(pedido.calcularSubtotalProductos())
                + "\nDomicilio: " + formatearPesos(pedido.calcularCostoDomicilio())
                + "\nTotal: " + formatearPesos(total), JOptionPane.INFORMATION_MESSAGE);
    }

    /** Opcion 6: lista los pedidos confirmados de un cliente. */
    private static void consultarHistorialCliente() {
        String documento = leerTexto("Documento del cliente:");
        List<Pedido> historial = restaurante.consultarHistorialCliente(documento);
        if (historial.isEmpty()) {
            mostrar("El cliente no tiene pedidos confirmados", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        StringBuilder texto = new StringBuilder("Historial del cliente " + documento + ":\n");
        for (Pedido pedido : historial) {
            texto.append("\nCodigo: ").append(pedido.getCodigo())
                    .append(" | Fecha: ").append(pedido.getFecha())
                    .append(" | Estado: ").append(pedido.getEstado())
                    .append(" | Total: ").append(formatearPesos(pedido.calcularTotal()));
        }
        mostrar(texto.toString(), JOptionPane.INFORMATION_MESSAGE);
    }

    /** Opcion 7: marca un pedido como entregado o lo cancela, segun las transiciones permitidas. */
    private static void actualizarEstadoPedido() {
        String codigo = leerTexto("Codigo del pedido:");
        Pedido pedido = restaurante.buscarPedido(codigo);
        if (pedido == null) {
            throw new IllegalArgumentException("El pedido no existe");
        }
        int accion = elegirOpcion("Estado actual: " + pedido.getEstado() + "\n¿Que desea hacer?",
                new String[]{"Marcar como entregado", "Cancelar pedido"});
        boolean exito;
        if (accion == 0) {
            exito = restaurante.marcarPedidoEntregado(codigo);
        } else {
            exito = restaurante.cancelarPedido(codigo);
        }
        if (exito) {
            mostrar("Pedido actualizado. Nuevo estado: " + pedido.getEstado(), JOptionPane.INFORMATION_MESSAGE);
        } else {
            mostrar("No se pudo actualizar: el pedido esta en estado " + pedido.getEstado()
                    + " y no permite esa accion", JOptionPane.ERROR_MESSAGE);
        }
    }

    /** Opcion 8: reporte de productos aptos/no aptos, ingresos, producto mas vendido y mejor categoria. */
    private static void consultarVentas() {
        StringBuilder texto = new StringBuilder();
        texto.append("PRODUCTOS DISPONIBLES:\n").append(listarProductos(restaurante.consultarProductosDisponibles()));
        texto.append("\nPRODUCTOS NO APTOS:\n").append(listarProductos(restaurante.consultarProductosNoAptos()));
        texto.append("\nIngresos del restaurante: ").append(formatearPesos(restaurante.calcularIngresosRestaurante()));
        texto.append("\nIngresos por domicilios: ").append(formatearPesos(restaurante.calcularIngresosDomicilios()));

        Producto masVendido = restaurante.obtenerProductoMasVendido();
        texto.append("\nProducto mas vendido: ").append(masVendido == null ? "sin ventas" : masVendido.getNombre());
        Categoria categoria = restaurante.obtenerCategoriaMayorIngresos();
        texto.append("\nCategoria con mayores ingresos: ").append(categoria == null ? "sin ventas" : categoria);
        mostrar(texto.toString(), JOptionPane.INFORMATION_MESSAGE);
    }

    // ---------- Apoyo ----------

    /** Arma un texto con codigo, nombre, categoria y precio de venta de cada producto. */
    private static String listarProductos(List<Producto> productos) {
        if (productos.isEmpty()) {
            return "  (ninguno)\n";
        }
        StringBuilder texto = new StringBuilder();
        for (Producto producto : productos) {
            texto.append("  ").append(producto.getCodigo()).append(" - ").append(producto.getNombre())
                    .append(" (").append(producto.getCategoria()).append(") ")
                    .append(formatearPesos(producto.calcularPrecioVenta())).append("\n");
        }
        return texto.toString();
    }

    /** Replica las reglas de confirmacion para devolver el primer motivo por el que falla. */
    private static String motivoNoConfirmar(Pedido pedido) {
        if (pedido.getEstado() != EstadoPedido.PENDIENTE) {
            return "el pedido ya esta en estado " + pedido.getEstado();
        }
        if (pedido.getDetalles().isEmpty()) {
            return "el pedido no tiene productos";
        }
        for (DetallePedido detalle : pedido.getDetalles()) {
            Producto producto = detalle.producto();
            if (restaurante.buscarProducto(producto.getCodigo()) == null) {
                return "el producto " + producto.getNombre() + " no pertenece al menu";
            }
            if (!producto.cumpleCalidad()) {
                return "el producto " + producto.getNombre() + " no cumple los estandares de calidad";
            }
        }
        if (pedido.getModalidad() == ModalidadEntrega.DOMICILIO) {
            if (pedido.getEmpresaDomicilio() == null) {
                return "el pedido a domicilio no tiene empresa asignada";
            }
            if (!pedido.getEmpresaDomicilio().aceptaRecorrido(pedido.getDistanciaKm())) {
                return pedido.getEmpresaDomicilio().getNombre() + " no acepta un recorrido de "
                        + pedido.getDistanciaKm() + " km";
            }
        }
        return "no se cumplen las condiciones de confirmacion";
    }

    /** Formatea un valor como pesos redondeados, con punto de miles (ej. $12.500). */
    private static String formatearPesos(double valor) {
        DecimalFormatSymbols simbolos = new DecimalFormatSymbols(Locale.US);
        simbolos.setGroupingSeparator('.');
        DecimalFormat formato = new DecimalFormat("#,##0", simbolos);
        return "$" + formato.format(Math.round(valor));
    }

    /** Muestra un mensaje en un dialogo; {@code tipo} es una constante de JOptionPane (info, error...). */
    private static void mostrar(String mensaje, int tipo) {
        JOptionPane.showMessageDialog(null, mensaje, TITULO, tipo);
    }

    /** Convierte los valores de un enum en un arreglo con sus nombres, para usarlos como opciones. */
    private static String[] nombresEnum(Enum<?>[] valores) {
        String[] nombres = new String[valores.length];
        for (int i = 0; i < valores.length; i++) {
            nombres[i] = valores[i].name();
        }
        return nombres;
    }

    // Devuelve el indice elegido; lanza IllegalStateException si se cierra el dialogo
    private static int elegirOpcion(String mensaje, String[] opciones) {
        int resultado = JOptionPane.showOptionDialog(null, mensaje, TITULO, JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);
        if (resultado == JOptionPane.CLOSED_OPTION) {
            throw new IllegalStateException(CANCELADO);
        }
        return resultado;
    }

    /** Pregunta Si/No; Cancelar o cerrar el dialogo lanza IllegalStateException. */
    private static boolean leerSiNo(String mensaje) {
        int resultado = JOptionPane.showConfirmDialog(null, mensaje, TITULO, JOptionPane.YES_NO_CANCEL_OPTION);
        if (resultado == JOptionPane.CANCEL_OPTION || resultado == JOptionPane.CLOSED_OPTION) {
            throw new IllegalStateException(CANCELADO);
        }
        return resultado == JOptionPane.YES_OPTION;
    }

    // Repite la pregunta si el campo esta vacio; Cancelar lanza IllegalStateException
    private static String leerTexto(String mensaje) {
        while (true) {
            String texto = JOptionPane.showInputDialog(null, mensaje, TITULO, JOptionPane.QUESTION_MESSAGE);
            if (texto == null) {
                throw new IllegalStateException(CANCELADO);
            }
            texto = texto.trim();
            if (!texto.isEmpty()) {
                return texto;
            }
            mostrar("El campo no puede estar vacio", JOptionPane.ERROR_MESSAGE);
        }
    }

    /** Lee un decimal >= 0 (acepta coma o punto); repite la pregunta mientras el valor sea invalido. */
    private static double leerDecimal(String mensaje) {
        while (true) {
            String texto = leerTexto(mensaje);
            try {
                double valor = Double.parseDouble(texto.replace(',', '.'));
                if (valor < 0 || Double.isNaN(valor) || Double.isInfinite(valor)) {
                    mostrar("Ingrese un numero valido mayor o igual a cero", JOptionPane.ERROR_MESSAGE);
                } else {
                    return valor;
                }
            } catch (NumberFormatException e) {
                mostrar("Ingrese un numero valido", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /** Lee un entero > 0; repite la pregunta mientras el valor sea invalido. */
    private static int leerEntero(String mensaje) {
        while (true) {
            String texto = leerTexto(mensaje);
            try {
                int valor = Integer.parseInt(texto);
                if (valor <= 0) {
                    mostrar("Ingrese un entero mayor que cero", JOptionPane.ERROR_MESSAGE);
                } else {
                    return valor;
                }
            } catch (NumberFormatException e) {
                mostrar("Ingrese un numero entero valido", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
