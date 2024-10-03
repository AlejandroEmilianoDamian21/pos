package com.systemnecs.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    private HBox root;

    @FXML
    private JFXButton btnWhatsapp;

    @FXML
    private JFXButton btnPagina;

    @FXML
    private JFXTextField cjUsername;

    @FXML
    private JFXPasswordField cjPassword;

    @FXML
    private JFXButton btnIniciar;

    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    void login(ActionEvent event) {
        System.out.println("Hola");
    }

}
