package com.systemnecs.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class RegistrarProductoController implements Initializable {
    @FXML
    private AnchorPane root;

    @FXML
    private JFXTextField cjnombre;

    @FXML
    private JFXTextField cjcodigo;

    @FXML
    private JFXTextField cjreferencia;

    @FXML
    private JFXTextField cjstock;

    @FXML
    private JFXTextField cjstockminimo;

    @FXML
    private JFXTextField cjprecio;

    @FXML
    private JFXDatePicker cjfechavencimiento;

    @FXML
    private JFXButton btnGuardar;

    @FXML
    private ImageView imageView;

    @FXML
    private JFXButton btnTomarFoto;

    @FXML
    private JFXButton btnBuscarImagen;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    void abrirCamara(ActionEvent event) {

    }

    @FXML
    void buscarImagen(ActionEvent event) {

    }

    @FXML
    void guardar(ActionEvent event) {

    }
}
