package com.systemnecs.controller;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import com.jfoenix.controls.JFXTabPane;
import javafx.fxml.FXML;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;

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
    void mostrarTablaProductos(ActionEvent event) {

    }

    @FXML
    void nuevoCliente(ActionEvent event) {

    }

    @FXML
    void salir(ActionEvent event) {

    }
}
