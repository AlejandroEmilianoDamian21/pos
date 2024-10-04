package com.systemnecs.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.systemnecs.dao.UsuarioDAO;
import com.systemnecs.model.Usuario;
import com.systemnecs.util.ConexionBD;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.fxml.Initializable;

import java.net.URL;
import java.sql.SQLException;
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

    private ConexionBD conexionBD;

    private UsuarioDAO usuarioDAO;

    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    void login(ActionEvent event) throws SQLException {
        String username = cjUsername.getText().trim();
        String password = cjPassword.getText().trim();

        conexionBD = new ConexionBD();
        conexionBD.conectar();

        usuarioDAO = new UsuarioDAO(conexionBD);
        Usuario usuario =  usuarioDAO.getUsuario(username,password);

        if(usuario == null){
            System.out.println("El usuario o contraseña incorrecta");
            return;
        }
        //Mostrar la vida del DASHBOARD
        System.out.println("Dashboard");

    }

}
