package com.systemnecs.controller;

import animatefx.animation.RollIn;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.systemnecs.dao.UsuarioDAO;
import com.systemnecs.model.Usuario;
import com.systemnecs.util.ConexionBD;
import com.systemnecs.util.Sesion;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.fxml.Initializable;
import javafx.stage.Stage;

import java.io.IOException;
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
            new animatefx.animation.Tada(cjUsername).play();
            new animatefx.animation.Tada(cjPassword).play();

            org.controlsfx.control.Notifications
                    .create()
                    .title("Mensaje")
                    .text("Usuario o contraseña incorrecta")
                    .position(Pos.CENTER).showInformation();
            return;
        }

        //Mostrar el DASHBOARD
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Dashboard.fxml"));
            BorderPane borderPane = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(borderPane));
            stage.setTitle("Dashboard"+ " - "+ "Usuario:"+Sesion.getUsuario(null).getNombre());
            stage.getIcons().add(new Image(this.getClass().getResourceAsStream("/images/dashboard.png")));
            stage.setMaximized(true);
            com.systemnecs.util.Metodos.closeEffect(root);
            stage.show();
        }catch (IOException e){
            e.printStackTrace();
            System.out.println("Error al cargar la vista del Dashboard");
        }
    }

}
