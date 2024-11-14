/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tallerxml;

/**
 *
 * @author osori
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;

public class Formulario extends JFrame {

    private JTable tablaProductos;
    private ProductoManager productoManager;
    private DefaultTableModel modeloTabla;

    private JTextField codigoField;
    private JTextField nombreField;
    private JTextField precioField;
    private JTextField categoriaField;

    public Formulario() {
        productoManager = new ProductoManager();
        modeloTabla = new DefaultTableModel();
        tablaProductos = new JTable(modeloTabla);
        JScrollPane scrollPane = new JScrollPane(tablaProductos);

        // Configuración de la ventana
        setLayout(new BorderLayout());
        setTitle("Gestión de Productos");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Panel para campos de texto
        JPanel panelFormulario = new JPanel(new GridLayout(6, 2));
        panelFormulario.add(new JLabel("Código:"));
        codigoField = new JTextField();
        panelFormulario.add(codigoField);

        panelFormulario.add(new JLabel("Nombre:"));
        nombreField = new JTextField();
        panelFormulario.add(nombreField);

        panelFormulario.add(new JLabel("Precio:"));
        precioField = new JTextField();
        panelFormulario.add(precioField);

        panelFormulario.add(new JLabel("Categoría:"));
        categoriaField = new JTextField();
        panelFormulario.add(categoriaField);

        JButton btnAgregar = new JButton("Agregar Producto");
        panelFormulario.add(btnAgregar);

        JButton btnModificar = new JButton("Modificar Producto");
        panelFormulario.add(btnModificar);

        JButton btnEliminar = new JButton("Eliminar Producto");
        panelFormulario.add(btnEliminar);

        add(panelFormulario, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Cargar productos desde XML
        cargarProductosDesdeXML();

        // Acción del botón para agregar productos
        btnAgregar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                agregarProducto();
            }
        });

        // Acción del botón para modificar productos
        btnModificar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modificarProducto();
            }
        });

        // Acción del botón para eliminar productos
        btnEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarProducto();
            }
        });
    }

    private void cargarProductosDesdeXML() {
        ArrayList<Producto> productos = productoManager.cargarProductos();
        modeloTabla.setColumnIdentifiers(new String[]{"Código", "Nombre", "Precio", "Categoría"});
        for (Producto producto : productos) {
            modeloTabla.addRow(new Object[]{producto.getCodigo(), producto.getNombre(), producto.getPrecio(), producto.getCategoria()});
        }
    }

    private void agregarProducto() {
        String codigo = codigoField.getText();
        String nombre = nombreField.getText();
        String precioStr = precioField.getText();
        String categoria = categoriaField.getText();

        if (codigo.isEmpty() || nombre.isEmpty() || precioStr.isEmpty() || categoria.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.");
            return;
        }

        try {
            double precio = Double.parseDouble(precioStr);
            Producto nuevoProducto = new Producto(codigo, nombre, precio, categoria);
            ArrayList<Producto> productos = productoManager.cargarProductos();
            productos.add(nuevoProducto);
            productoManager.guardarProductos(productos);
            modeloTabla.addRow(new Object[]{nuevoProducto.getCodigo(), nuevoProducto.getNombre(), nuevoProducto.getPrecio(), nuevoProducto.getCategoria()});
            limpiarCampos();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El precio debe ser un número válido.");
        }
    }

    private void modificarProducto() {
        int filaSeleccionada = tablaProductos.getSelectedRow();
        if (filaSeleccionada != -1) {
            String codigo = (String) modeloTabla.getValueAt(filaSeleccionada, 0);
            String nombre = nombreField.getText();
            String precioStr = precioField.getText();
            String categoria = categoriaField.getText();

            if (nombre.isEmpty() || precioStr.isEmpty() || categoria.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.");
                return;
            }

            try {
                double precio = Double.parseDouble(precioStr);
                Producto productoModificado = new Producto(codigo, nombre, precio, categoria);

                // Actualizar el JTable
                modeloTabla.setValueAt(nombre, filaSeleccionada, 1);
                modeloTabla.setValueAt(precio, filaSeleccionada, 2);
                modeloTabla.setValueAt(categoria, filaSeleccionada, 3);

                // Guardar los cambios en el archivo XML
                ArrayList<Producto> productos = productoManager.cargarProductos();
                productos.set(filaSeleccionada, productoModificado);
                productoManager.guardarProductos(productos);
                limpiarCampos();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "El precio debe ser un número válido.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecciona un producto para modificar.");
        }
    }

    private void eliminarProducto() {
        int filaSeleccionada = tablaProductos.getSelectedRow();
        if (filaSeleccionada != -1) {
            int confirmacion = JOptionPane.showConfirmDialog(this, "¿Estás seguro de eliminar este producto?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirmacion == JOptionPane.YES_OPTION) {
                String codigo = (String) modeloTabla.getValueAt(filaSeleccionada, 0);
                ArrayList<Producto> productos = productoManager.cargarProductos();
                productos.removeIf(p -> p.getCodigo().equals(codigo));
                productoManager.guardarProductos(productos);

                modeloTabla.removeRow(filaSeleccionada);
                limpiarCampos();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecciona un producto para eliminar.");
        }
    }

    private void limpiarCampos() {
        codigoField.setText("");
        nombreField.setText("");
        precioField.setText("");
        categoriaField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Formulario().setVisible(true);
            }
        });
    }
}
