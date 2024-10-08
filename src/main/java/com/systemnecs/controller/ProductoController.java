package com.systemnecs.controller;

import com.systemnecs.dao.ProductoDAO;
import com.systemnecs.model.Producto;
import com.systemnecs.util.ConexionBD;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class ProductoController implements Initializable {
    @FXML
    private AnchorPane root;

    @FXML
    private JFXButton btnListar;

    @FXML
    private JFXButton btnNuevo;

    @FXML
    private JFXButton btnEditar;

    @FXML
    private JFXButton btnBorrar;

    @FXML
    private JFXTextField cjBuscar;

    @FXML
    private TableView<Producto> tablaProductos;

    @FXML
    private TableColumn<Producto, String> colNombre;

    @FXML
    private TableColumn<Producto, String> colCodigo;

    @FXML
    private TableColumn<Producto, Integer> colStock;

    @FXML
    private TableColumn<Producto, Integer> colStockMinimo;

    @FXML
    private TableColumn<Producto, Double> colPrecio;

    @FXML
    private TableColumn<Producto, LocalDate> colFechaVencimiento;

    private ObservableList<Producto> listaProductos = FXCollections.observableArrayList();

    private ConexionBD conexionBD = new ConexionBD();
    private ProductoDAO productoDAO;

    private Stage stageProducto;
    private RegistrarProductoController registrarProductoController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        colNombre.setCellValueFactory(param -> param.getValue().nombreproductoProperty());

        colCodigo.setCellValueFactory(param -> param.getValue().codigodebarrasProperty());

        colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));

        colStockMinimo.setCellValueFactory(new PropertyValueFactory<>("stockminimo"));

        colPrecio.setCellValueFactory( new PropertyValueFactory<>("precio"));

        /*colPrecio.setCellFactory(column -> new TableCell<Producto, Double>() {
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
        */
        colFechaVencimiento.setCellValueFactory(new PropertyValueFactory<>("fechavencimiento"));

        tablaProductos.setItems(listaProductos);
    }

    @FXML
    void borrarProducto(ActionEvent event) {

    }

    @FXML
    void buscarProductoKeyReleased(KeyEvent event) {

    }

    @FXML
    void editarProducto(ActionEvent event) {

    }

    @FXML
    void listarProductos(ActionEvent event) {

        conexionBD.conectar();
        productoDAO = new ProductoDAO(conexionBD);

        Task<List<Producto>> listTask = new Task<List<Producto>>() {
            @Override
            protected List<Producto> call() throws Exception {
                conexionBD.conectar();
                productoDAO = new ProductoDAO(conexionBD);
                return productoDAO.getProductos();
            }
        };

        listTask.setOnFailed(event1 -> {
            conexionBD.CERRAR();
            tablaProductos.setPlaceholder(null);
        });

        listTask.setOnSucceeded(event1 -> {
            tablaProductos.setPlaceholder(null);
            conexionBD.CERRAR();
            listaProductos.setAll(listTask.getValue());
            tablaProductos.getColumns().forEach(column -> {
                com.systemnecs.util.Metodos.changeSizeOnColumn(column, tablaProductos, -1);
            });
        });

        ProgressIndicator progressIndicator = new ProgressIndicator();
        progressIndicator.setMinSize(100, 100);   // Tamaño mínimo
        progressIndicator.setPrefSize(150, 150);  // Tamaño preferido
        progressIndicator.setMaxSize(150, 150);   // Tamaño máximo
        tablaProductos.setPlaceholder(progressIndicator);

        Thread hilo = new Thread(listTask);
        hilo.start();
    }

    @FXML
    void nuevoProducto(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/RegistrarProducto.fxml"));
        AnchorPane anchorPane = loader.load();
        registrarProductoController = loader.getController();
        Scene scene = new Scene(anchorPane);
        stageProducto = new Stage();
        stageProducto.setTitle("Nuevo Producto");
        stageProducto.getIcons().add(new Image(this.getClass().getResourceAsStream("/images/productos.png")));
        stageProducto.setScene(scene);
        stageProducto.initOwner(root.getScene().getWindow());
        stageProducto.initModality(Modality.WINDOW_MODAL);
        stageProducto.initStyle(StageStyle.DECORATED);
        stageProducto.setResizable(false);
        stageProducto.setOnCloseRequest((WindowEvent e) -> {
            root.setEffect(null);
        });
        stageProducto.setOnHidden((WindowEvent e) -> {
            root.setEffect(null);
        });
        stageProducto.showAndWait();
        listarProductos(null);
        root.setEffect(new GaussianBlur(7.0));

    }

}
