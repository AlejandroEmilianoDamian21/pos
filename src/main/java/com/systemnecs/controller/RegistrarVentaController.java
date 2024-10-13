package com.systemnecs.controller;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

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
    private TableView<?> tablaProductos;

    @FXML
    private TableColumn<?, ?> colProductos;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    void buscarCodigo(KeyEvent event) {

    }

    @FXML
    void buscarProducto(KeyEvent event) {

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

    }

    @FXML
    void tablaProductosMouseClicked(MouseEvent event) {

    }

}
