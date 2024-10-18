package com.systemnecs.controller;

import com.systemnecs.dao.VentaDAO;
import com.systemnecs.model.Venta;
import com.systemnecs.util.ConexionBD;
import impl.org.controlsfx.tableview2.filter.filtermenubutton.DefaultFilterMenuButtonFactory;
import javafx.fxml.Initializable;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.awt.event.KeyEvent;
import java.net.URL;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ResourceBundle;

public class PagarController implements Initializable {

    @FXML
    private VBox root;

    @FXML
    private Text txtTotalAPagar;

    @FXML
    private JFXTextField cjValorIngreso;

    @FXML
    private Text txtCambio;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    void cjValorIngresoKeyPressed(KeyEvent event) {
    }

    @FXML
    void cjValorIngresokeyReleased(KeyEvent event) {

    }
}
