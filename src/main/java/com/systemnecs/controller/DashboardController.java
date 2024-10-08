package com.systemnecs.controller;

import com.systemnecs.util.Sesion;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import com.jfoenix.controls.JFXTabPane;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    @FXML
    private BorderPane root;

    @FXML
    private MenuBar menuBar;

    @FXML
    private Menu menuArchivo;

    @FXML
    private MenuItem menuconfig;

    @FXML
    private MenuItem menuSalir;

    @FXML
    private Menu menuProductos;

    @FXML
    private MenuItem menuVerProductos;

    @FXML
    private Menu menuVentas;

    @FXML
    private MenuItem menuRealizarVenta;

    @FXML
    private Menu menuClientes;

    @FXML
    private MenuItem nuevoCliente;

    @FXML
    private JFXTabPane tabPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
    @FXML
    void abrirConfiguracion(ActionEvent event) {

    }

    @FXML
    void mostraRealizarVenta(ActionEvent event) {

    }

    @FXML
    void mostrarTablaProductos(ActionEvent event) throws IOException {
        root.setEffect(new GaussianBlur(7.0));
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/RegistrarProducto.fxml"));
        AnchorPane anchorPane = loader.load();
        Scene scene = new Scene(anchorPane);
        Stage stage = new Stage();
        stage.setTitle("Nuevo Cliente");
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

    @FXML
    void nuevoCliente(ActionEvent event) throws IOException {
        try {
            root.setEffect(new GaussianBlur(7.0));
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/RegistrarCliente.fxml"));
            VBox vBox = loader.load();
            Scene scene = new Scene(vBox);
            Stage stage = new Stage();
            stage.setTitle("Nuevo Cliente");
            stage.getIcons().add(new Image(this.getClass().getResourceAsStream("/images/add_user.png")));
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
        }catch (IOException e){
            e.printStackTrace();
            System.out.println("Error al cargar la vista del RegistrarCliente");
        }
    }

    @FXML
    void salir(ActionEvent event) {

    }
}
