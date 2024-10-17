package com.systemnecs.controller;

import com.systemnecs.dao.ClienteDAO;
import com.systemnecs.dao.ProductoDAO;
import com.systemnecs.model.Cliente;
import com.systemnecs.model.Comercio;
import com.systemnecs.model.DetalleVenta;
import com.systemnecs.model.Producto;
import com.systemnecs.util.ConexionBD;
import com.systemnecs.util.CurrencyCell;
import com.systemnecs.util.SearchComboBox;
import com.systemnecs.util.DoubleCell;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.net.URL;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Objects;
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
    private TableView<DetalleVenta> tablaPedidos;

    @FXML
    private TableColumn<DetalleVenta, String> colProducto;

    @FXML
    private TableColumn colcantidad;

    @FXML
    private TableColumn colvalor;

    @FXML
    private TableColumn<DetalleVenta, Double> coltotal;

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

    private Integer iva;



    @Override
    public void initialize(URL location, ResourceBundle resources) {

        Comercio comercio = Comercio.getInstance(null);
        if (comercio != null) {
            this.iva = comercio.getIva();
        } else {
            // Maneja el caso en que comercio sea null, por ejemplo, asignando un valor predeterminado
            this.iva = 1;  // Asigna un valor predeterminado si es necesario
            Logger.getLogger(RegistrarVentaController.class.getName()).log(Level.WARNING, "Comercio es nulo.");
        }

        tablaPedidos.setEditable(true);
        tablaPedidos.getSelectionModel().setCellSelectionEnabled(true);
        tablaPedidos.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tablaPedidos.setItems(listaPedido);
        tablaPedidos.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        colProducto.setCellValueFactory(tc -> tc.getValue().getProducto().nombreproductoProperty());

        colcantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        colcantidad.setStyle("-fx-alignment: CENTER");
        colcantidad.setCellFactory(tc -> new DoubleCell<>());
        colcantidad.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<DetalleVenta, Double>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<DetalleVenta, Double> e) {
                if (!Objects.equals(e.getNewValue(), e.getOldValue())) {
                    ((DetalleVenta) e.getTableView().getItems().get(e.getTablePosition().getRow())).setCantidad(e.getNewValue());
                    calcular();
                    com.systemnecs.util.Metodos.changeSizeOnColumn(coltotal, tablaPedidos, e.getTablePosition().getRow());
                }
            }
        });

        colvalor.setCellValueFactory(new PropertyValueFactory<>("precioventa"));
        colvalor.setCellFactory(tc -> new CurrencyCell<>());
        colvalor.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<DetalleVenta, Double>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<DetalleVenta, Double> e) {
                if (!Objects.equals(e.getNewValue(), e.getOldValue())) {
                    ((DetalleVenta) e.getTableView().getItems().get(e.getTablePosition().getRow())).setPrecioventa(e.getNewValue());
                    calcular();
                    com.systemnecs.util.Metodos.changeSizeOnColumn(colvalor, tablaPedidos, e.getTablePosition().getRow());
                }
            }
        });

        coltotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        coltotal.setCellFactory(tc -> new CurrencyCell<>());
        coltotal.setEditable(false);

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
            colProductosPrecio.setCellFactory(tc -> new TableCell<Producto, Double>() {
                @Override
                protected void updateItem(Double precio, boolean empty) {
                    super.updateItem(precio, empty);
                    if (empty || precio == null) {
                        setText(null);
                    } else {
                        // Formatea el precio con el símbolo de dólar
                        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);
                        setText(currencyFormat.format(precio));
                    }
                }
            });
        } catch (SQLException ex) {
            org.controlsfx.control.Notifications.create().title("Aviso").text("No se cargaron los productos").position(Pos.CENTER).showWarning();
            Logger.getLogger(RegistrarVentaController.class.getName()).log(Level.SEVERE, null, ex);
        } 

        //txtTituloEmpresa.setText(Comercio.getInstance(null).getNombre());
        lblIva.setText("IVA: ("+this.iva+"%)");
    }

    @FXML
    void buscarCodigo(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER && cjCodigoBarras.getText() != null && !cjCodigoBarras.getText().isEmpty()) {

            listaPedido.stream().
                    filter(p -> p.getProducto().getCodigodebarras().equals(cjCodigoBarras.getText()))
                    .findFirst().map((t) -> {
                t.setCantidad((t.getCantidad() + 1));
                cjCodigoBarras.setText(null);
                return t;
            }).orElseGet(() -> {
                listaProductos.stream()
                        .filter((p) -> p.getCodigodebarras().contains(cjCodigoBarras.getText()))
                        .findFirst()
                        .ifPresent(p -> {
                            DetalleVenta dv = new DetalleVenta();
                            dv.setProducto(p);
                            dv.setCantidad(1);
                            dv.setPrecioventa(p.getPrecio());
                            listaPedido.add(dv);
                            com.systemnecs.util.Metodos.changeSizeOnColumn(colProducto, tablaPedidos, -1);
                            cjCodigoBarras.setText(null);
                        });
                return null;
            });
            calcular();
        } else if (event.getCode() == KeyCode.DOWN) {
            tablaPedidos.requestFocus();
            tablaPedidos.getFocusModel().focus(0, colcantidad);
            tablaPedidos.getSelectionModel().select(0, colcantidad);
        } else if (event.getCode() == KeyCode.RIGHT && cjCodigoBarras.getText() == null) {
            cjBuscarProducto.requestFocus();
        }
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

    private void calcular() {
        double suma = listaPedido.stream().mapToDouble(ped -> ped.getCantidad()*ped.getPrecioventa()).sum();
        double iva = (suma*this.iva)/100.0;
        txtIva.setText(NumberFormat.getCurrencyInstance(Locale.US).format(iva));
        txtSubtotal.setText(NumberFormat.getCurrencyInstance(Locale.US).format((suma)));
        txtTotal.setText(NumberFormat.getCurrencyInstance(Locale.US).format((suma+iva)));
    }



}
