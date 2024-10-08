package com.systemnecs.controller;

import com.systemnecs.dao.ProductoDAO;
import com.systemnecs.model.Producto;
import com.systemnecs.util.ConexionBD;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.scene.Scene;
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        colNombre.setCellValueFactory(param -> param.getValue().nombreproductoProperty());

        colCodigo.setCellValueFactory(param -> param.getValue().codigodebarrasProperty());

        colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));

        colStockMinimo.setCellValueFactory(new PropertyValueFactory<>("stockminimo"));

        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));

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
        try {
            listaProductos.setAll(productoDAO.getProductos());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void nuevoProducto(ActionEvent event) throws IOException {
        root.setEffect(new GaussianBlur(7.0));
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/RegistrarProducto.fxml"));
        AnchorPane anchorPane = loader.load();
        Scene scene = new Scene(anchorPane);
        Stage stage = new Stage();
        stage.setTitle("Nuevo Producto");
        stage.getIcons().add(new Image(this.getClass().getResourceAsStream("/images/productos.png")));
        stage.setScene(scene);
        stage.initOwner(root.getScene().getWindow());
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initStyle(StageStyle.DECORATED);
        stage.setResizable(false);
        stage.setOnCloseRequest((WindowEvent e) -> {
            root.setEffect(null);
        });
        stage.setOnHidden((WindowEvent e) -> {
            root.setEffect(null);
        });
        stage.showAndWait();
    }

}
