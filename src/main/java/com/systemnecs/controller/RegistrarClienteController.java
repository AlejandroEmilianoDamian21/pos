package com.systemnecs.controller;

import animatefx.animation.Tada;
import com.systemnecs.dao.ClienteDAO;
import com.systemnecs.model.Cliente;
import com.systemnecs.util.ConexionBD;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RegistrarClienteController implements Initializable {

    @FXML
    private VBox root;

    @FXML
    private HBox header;

    @FXML
    private Text txtCliente;

    @FXML
    private JFXTextField cjDocumento;

    @FXML
    private JFXTextField cjNombre;

    @FXML
    private JFXTextField cjApellido;

    @FXML
    private JFXTextField cjDireccion;

    @FXML
    private JFXTextField cjTelefono;

    @FXML
    private JFXButton btnGuardar;

    private Cliente cliente;
    private ConexionBD conexionBD = new ConexionBD();
    private ClienteDAO clienteDAO;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    void guardar(ActionEvent event) {
        if (cjNombre.getText().isEmpty()) {
            new Tada(cjNombre).play();
            org.controlsfx.control.Notifications.create()
                    .title("Aviso")
                    .text("Ingrese el nombre del Cliente")
                    .position(Pos.CENTER)
                    .showWarning();
            return;
        }
        if (cjDocumento.getText().isEmpty()) {
            new Tada(cjDocumento).play();
            org.controlsfx.control.Notifications.create()
                    .title("Aviso")
                    .text("Ingrese el numero de identificacion")
                    .position(Pos.CENTER)
                    .showWarning();
            return;
        }

        if(getCliente()== null){
            cliente = new Cliente();
        }

        cliente.setNombrecliente(cjNombre.getText());
        cliente.setApellidocliente(cjApellido.getText());
        cliente.setNumerodocumento(cjDocumento.getText());
        cliente.setDireccioncliente(cjDireccion.getText());
        cliente.setTelefonocliente(cjTelefono.getText());

        try{
            this.conexionBD.conectar();
            clienteDAO = new ClienteDAO(conexionBD);
            int n = clienteDAO.guardar(cliente);
                if (cliente.getIdcliente() == 0){
                    cliente.setIdcliente(n);
                }
            /*org.controlsfx.control.Notifications.create()
                    .title("Aviso")
                    .text("Cliente guardado")
                    .position(Pos.CENTER)
                    .hideAfter(Duration.seconds(5))
                    .showConfirm();*/
            com.systemnecs.util.Metodos.closeEffect(root);
        }catch (SQLException ex){
            Logger.getLogger(RegistrarClienteController.class.getName()).log(Level.SEVERE,null, ex);
            org.controlsfx.control.Notifications.create()
                    .title("Aviso")
                    .text("NO SE PUDO GUARDAR EL CLIENTE\n"+ ex.getMessage())
                    .position(Pos.CENTER)
                    .showError();
        } finally {
            this.conexionBD.CERRAR();
        }
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

}

