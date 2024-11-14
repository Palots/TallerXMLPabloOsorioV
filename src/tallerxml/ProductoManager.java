package tallerxml;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author osori
 */
import javax.xml.parsers.*;
import org.w3c.dom.*;
import java.io.*;
import java.util.*;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class ProductoManager {

    private static final String FILE_PATH = "productos.xml";

    public ArrayList<Producto> cargarProductos() {
        ArrayList<Producto> productos = new ArrayList<>();
        try {
            File file = new File(FILE_PATH);
            if (!file.exists()) {
                return productos;
            }

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(file);
            NodeList nodeList = document.getElementsByTagName("producto");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    String codigo = element.getElementsByTagName("codigo").item(0).getTextContent();
                    String nombre = element.getElementsByTagName("nombre").item(0).getTextContent();
                    double precio = Double.parseDouble(element.getElementsByTagName("precio").item(0).getTextContent());
                    String categoria = element.getElementsByTagName("categoria").item(0).getTextContent();

                    productos.add(new Producto(codigo, nombre, precio, categoria));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return productos;
    }

    public void guardarProductos(ArrayList<Producto> productos) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();
            Element root = document.createElement("productos");
            document.appendChild(root);

            for (Producto producto : productos) {
                Element productoElement = document.createElement("producto");

                Element codigoElement = document.createElement("codigo");
                codigoElement.appendChild(document.createTextNode(producto.getCodigo()));
                productoElement.appendChild(codigoElement);

                Element nombreElement = document.createElement("nombre");
                nombreElement.appendChild(document.createTextNode(producto.getNombre()));
                productoElement.appendChild(nombreElement);

                Element precioElement = document.createElement("precio");
                precioElement.appendChild(document.createTextNode(String.valueOf(producto.getPrecio())));
                productoElement.appendChild(precioElement);

                Element categoriaElement = document.createElement("categoria");
                categoriaElement.appendChild(document.createTextNode(producto.getCategoria()));
                productoElement.appendChild(categoriaElement);

                root.appendChild(productoElement);
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(new File(FILE_PATH));
            transformer.transform(source, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
