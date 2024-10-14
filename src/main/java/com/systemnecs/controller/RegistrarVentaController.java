package com.systemnecs.controller;

import com.systemnecs.dao.ClienteDAO;
import com.systemnecs.dao.ProductoDAO;
import com.systemnecs.model.Cliente;
import com.systemnecs.model.Comercio;
import com.systemnecs.model.DetalleVenta;
import com.systemnecs.model.Producto;
import com.systemnecs.util.ConexionBD;
import com.systemnecs.util.SearchComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.net.URL;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RegistrarVentaController implements Initializable {

    @FXML
    private VBox root;

    @FXML
    private JFXTextField cjCodigoBarras;

    @FXML
    private TableView<?> tablaPedidos;

    @FXML
    private TableColumn<?, ?> colProducto;

    @FXML
    private TableColumn<?, ?> colcantidad;

    @FXML
    private TableColumn<?, ?> colvalor;

    @FXML
    private TableColumn<?, ?> coltotal;

    @FXML
    private JFXButton btnQuitarProducto;

    @FXML
    private JFXButton btnAgregarProducto;

    @FXML
    private Text txtTituloEmpresa;

    @FXML
    private GridPane gridPane;

    @FXML
    private JFXDatePicker cjFecha;

    @FXML
    private JFXComboBox<?> comboFormaDePago;

    @FXML
    private Label lblIva;

    @FXML
    private Text txtSubtotal;

    @FXML
    private Text txtIva;

    @FXML
    private Text txtTotal;

    @FXML
    private JFXButton btnCancelar;

    @FXML
    private JFXButton btnPagar;

    @FXML
    private JFXTextField cjBuscarProducto;

    @FXML
    private TableView<Producto> tablaProductos;

    @FXML
    private TableColumn<Producto, String> colProductos;

    @FXML
    private TableColumn<Producto, Double> colProductosPrecio;

    SearchComboBox<Cliente> comboCliente = new SearchComboBox<>();

    private ConexionBD conexionBD = new ConexionBD();
    private ClienteDAO clienteDAO;
    private ProductoDAO productoDAO;

    FilteredList<Producto> filtro;

    ObservableList<Producto> listaProductos = FXCollections.observableArrayList();
    ObservableList<DetalleVenta> listaPedido = FXCollections.observableArrayList();

    //private Integer iva = Comercio.getInstance(null).getIva();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try {
            this.conexionBD.conectar();
            clienteDAO = new ClienteDAO(this.conexionBD);
            comboCliente.getItems().addAll(clienteDAO.getAll());
            comboCliente.setFilter((Cliente t, String u) -> {
                if (t.getNombrecliente().toUpperCase().contains(u.toUpperCase())) {
                    return true;
                } else if (t.getApellidocliente().toUpperCase().contains(u.toUpperCase())) {
                    return true;
                }
                return false;
            });
        }catch (SQLException ex){
            Logger.getLogger(RegistrarVentaController.class.getName()).log(Level.SEVERE, null, ex);
        }finally {
            this.conexionBD.CERRAR();
        }
        gridPane.add(comboCliente,0 ,1);
        comboCliente.setMaxHeight(Double.MAX_VALUE);

        try {
            this.conexionBD.conectar();
            productoDAO = new ProductoDAO(this.conexionBD);
            listaProductos.addAll(productoDAO.getAll());
            tablaProductos.setItems(listaProductos);
            filtro = new FilteredList(listaProductos, p -> true);
            colProductos.setCellValueFactory(param -> param.getValue().nombreproductoProperty());
            colProductosPrecio.setCellValueFactory(param -> param.getValue().precioProperty().asObject());
            colProductosPrecio.setCellFactory(column -> new TableCell<Producto, Double>() {
                @Override
                protected void updateItem(Double precio, boolean empty) {
                    super.updateItem(precio, empty);
                    if (empty || precio == null) {
                        setText(null);
                    } else {
                        // Agregar el símbolo de pesos y formatear el precio
                        setText(String.format("$%.2f", precio));
                    }
                }

            });
        } catch (SQLException ex) {
            org.controlsfx.control.Notifications.create().title("Aviso").text("No se cargaron los productos").position(Pos.CENTER).showWarning();
            Logger.getLogger(RegistrarVentaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    void buscarCodigo(KeyEvent event) {

    }

    @FXML
    void buscarProducto(KeyEvent event) {
        switch (event.getCode()) {
            case DOWN:
                tablaProductos.requestFocus();
                tablaProductos.getSelectionModel().select(0, colProductos);
                break;
            case ESCAPE:
                cjCodigoBarras.requestFocus();
                break;
            default:
                // Se debe configurar el listener solo una vez durante la inicialización
                cjBuscarProducto.textProperty().addListener((observableValue, oldValue, newValue) -> {
                    filtro.setPredicate(param -> {
                        // Si el campo de búsqueda está vacío, mostrar todos los productos
                        if (newValue == null || newValue.isEmpty()) {
                            return true;
                        }

                        // Filtrar según el nombre del producto
                        String lowerCaseFilter = newValue.toLowerCase();
                        return param.getNombreproducto().toLowerCase().contains(lowerCaseFilter);
                    });
                });

                // Se debe configurar el SortedList solo una vez durante la inicialización
                SortedList<Producto> sortedData = new SortedList<>(filtro);
                sortedData.comparatorProperty().bind(tablaProductos.comparatorProperty());
                tablaProductos.setItems(sortedData);
        }
    }

    @FXML
    void cancelarPedido(ActionEvent event) {

    }

    @FXML
    void pagar(ActionEvent event) {

    }

    @FXML
    void restarCantidad(ActionEvent event) {

    }

    @FXML
    void sumarCantidad(ActionEvent event) {

    }

    @FXML
    void tablaPedidosKeyPressed(KeyEvent event) {

    }

    @FXML
    void tablaProductosKeyPressed(KeyEvent event) {
        if (event.getCode().isDigitKey()) {
            final TablePosition focusedCell = tablaPedidos.focusModelProperty().get().focusedCellProperty().get();
            tablaPedidos.edit(focusedCell.getRow(), focusedCell.getTableColumn());
        } else if (event.getCode() == KeyCode.ESCAPE) {
            cjCodigoBarras.requestFocus();
            tablaPedidos.getSelectionModel().clearSelection();
        }
    }

    @FXML
    void tablaProductosMouseClicked(MouseEvent event) {
        if (event.getClickCount()== 2){
            Producto pr = tablaProductos.getSelectionModel().getSelectedItem();
            agregarPedido(pr);
        }
    }

    void agregarPedido(Producto pr){
        listaPedido.stream()
                .filter(p -> p.getProducto().getIdproducto() == (pr.getIdproducto()))
                .findFirst().map((t) ->{
                t.setCantidad((t.getCantidad() + 1));
                com.systemnecs.util.Metodos.changeSizeOnColumn(coltotal, tablaPedidos, -1);
                cjCodigoBarras.setText(null);
                return t;
        }).orElseGet(()->{
            DetalleVenta dv = new DetalleVenta();
            dv.setProducto(pr);
            dv.setCantidad(1);
            dv.setPrecioventa(pr.getPrecio());
            listaPedido.add(dv);
            com.systemnecs.util.Metodos.changeSizeOnColumn(colProductos, tablaPedidos, -1);
            cjCodigoBarras.setText(null);
            return dv;
        });
        calcular();
    }

    private void calcular() {
        double suma = listaPedido.stream().mapToDouble(ped -> ped.getCantidad()*ped.getPrecioventa()).sum();
        //double iva = (suma*this.iva)/100.0;
        //txtIva.setText(NumberFormat.getCurrencyInstance().format(iva));
        txtSubtotal.setText(NumberFormat.getCurrencyInstance().format((suma)));
        //txtTotal.setText(NumberFormat.getCurrencyInstance().format((suma+iva)));
    }

    public void actualizarComboClientes() {
        try {
            this.conexionBD.conectar();
            clienteDAO = new ClienteDAO(this.conexionBD);
            comboCliente.getItems().clear(); // Limpiar el ComboBox
            comboCliente.getItems().addAll(clienteDAO.getAll()); // Volver a agregar los clientes desde la base de datos
        } catch (SQLException ex) {
            Logger.getLogger(RegistrarVentaController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            this.conexionBD.CERRAR();
        }
    }

}
