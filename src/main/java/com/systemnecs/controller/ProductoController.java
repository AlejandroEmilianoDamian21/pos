package com.systemnecs.controller;

import com.systemnecs.dao.ProductoDAO;
import com.systemnecs.model.Producto;
import com.systemnecs.util.ConexionBD;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    private TableColumn<Producto, Double> colStock;

    @FXML
    private TableColumn<Producto, Double> colStockMinimo;

    @FXML
    private TableColumn<Producto, Double> colPrecio;

    @FXML
    private TableColumn<Producto, LocalDate> colFechaVencimiento;

    private ObservableList<Producto> listaProductos = FXCollections.observableArrayList();

    private ConexionBD conexionBD = new ConexionBD();
    private ProductoDAO productoDAO;

    private Stage stageProducto;
    private RegistrarProductoController registrarProductoController;


    private ObjectProperty<Producto> objProducto = new SimpleObjectProperty<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        colNombre.setCellValueFactory(param -> param.getValue().nombreproductoProperty());

        colCodigo.setCellValueFactory(param -> param.getValue().codigodebarrasProperty());

        colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        colStock.setCellFactory(param -> {
            return new TableCell<Producto, Double>(){
                @Override
                protected void updateItem(Double value, boolean empty) {
                    super.updateItem(value, empty);
                    if (value == null || empty) {
                        setText(null);
                        setStyle(null);
                    } else {
                        setAlignment(Pos.CENTER);
                        setText("" +value);
                        Producto producto = getTableView().getItems().get(getIndex());
                        if(value > producto.getStockminimo()){
                            setStyle("-fx-background-color: #4CAF50; fx-font-weight: bold; -fx-text-fill: white; ");
                        }else if(value > 1 && value <= getTableView().getItems().get(getIndex()).getStockminimo() ){
                            setStyle("-fx-background-color: #FFFF00; fx-font-weight: bold; -fx-text-fill: black; ");
                        }else if(value < 1){
                            setStyle("-fx-background-color: #F44336; fx-font-weight: bold; -fx-text-fill: white; ");
                        }
                    }
                }
            };
        });
        colStockMinimo.setCellValueFactory(new PropertyValueFactory<>("stockminimo"));

        colPrecio.setCellValueFactory( new PropertyValueFactory<>("precio"));

        colPrecio.setCellFactory(column -> new TableCell<Producto, Double>() {
            @Override
            protected void updateItem(Double precio, boolean empty) {
                super.updateItem(precio, empty);
                if (empty || precio == null) {
                    setText(null);
                } else {
                    // Agregar el símbolo de pesos y formatear el precio
                    setText(String.format("$%.2f", precio));
                }
            }

        });

        colFechaVencimiento.setCellValueFactory(new PropertyValueFactory<>("fechavencimiento"));
        colFechaVencimiento.setCellFactory(param -> {
            return new TableCell<Producto, LocalDate>(){
                @Override
                protected void updateItem(LocalDate value, boolean empty) {
                    super.updateItem(value, empty);
                    if(!isEmpty() || value != null){
                        setText(DateTimeFormatter.ofPattern("EEEE dd MMM yyyy").format(value));
                        if (Period.between(LocalDate.now(),value).isNegative() || Period.between(LocalDate.now(),value).isZero()){
                            setStyle("-fx-background-color: #F44336; fx-font-weight: bold; -fx-text-fill: white; ");
                        }else if (LocalDate.now().plusDays(5).isBefore(value)) {
                            setStyle("-fx-background-color: #4CAF50; fx-font-weight: bold; -fx-text-fill: white;");
                        }else if ((LocalDate.now().plusDays(5).isBefore(value) || LocalDate.now().plusDays(5).equals(value)) || (value.isBefore(LocalDate.now().plusDays(5)) && value.isAfter(LocalDate.now()))) {
                            setStyle("-fx-background-color: #FFFF00; fx-font-weight: bold;");
                        }
                    }else{
                        setStyle(null);
                        setText(null);
                    }
                }
            };
        });

        listarProductos(null);
        tablaProductos.setItems(listaProductos);

        objProducto.bind(tablaProductos.getSelectionModel().selectedItemProperty());
    }

    @FXML
    void borrarProducto(ActionEvent event) throws SQLException {
        if(objProducto.get()== null){
            com.systemnecs.util.Metodos.rotarError(tablaProductos);
            Alert alert = new Alert(Alert.AlertType.ERROR, "Debe seleccionar un producto en la tabla para eliminar", ButtonType.OK);
            alert.showAndWait();
            return;
        }
        Alert a = new Alert(Alert.AlertType.CONFIRMATION, "¿Desea eliminar el producto?", ButtonType.YES, ButtonType.NO);
        a.setHeaderText(this.objProducto.get().getNombreproducto());
        if (a.showAndWait().get() == ButtonType.YES){
            conexionBD.conectar();
            productoDAO = new ProductoDAO(conexionBD);
            boolean delete =  productoDAO.delete(objProducto.get().getIdproducto());
            listarProductos(null);
            this.conexionBD.CERRAR();
        }
    }

    @FXML
    void buscarProductoKeyReleased(KeyEvent event) {

    }

    @FXML
    void editarProducto(ActionEvent event) throws IOException {
        if(objProducto.get()== null){
            com.systemnecs.util.Metodos.rotarError(tablaProductos);
            Alert alert = new Alert(Alert.AlertType.ERROR, "Debe seleccionar un producto en la tabla para Editar", ButtonType.OK);
            alert.showAndWait();
            return;
        }
        root.setEffect(new GaussianBlur(7.0));
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/RegistrarProducto.fxml"));
        AnchorPane ap = loader.load();
        registrarProductoController = loader.getController();
        Scene scene = new Scene(ap);
        stageProducto = new Stage();
        stageProducto.getIcons().add(new Image(this.getClass().getResourceAsStream("/images/productos.png")));
        stageProducto.setScene(scene);
        stageProducto.initOwner(root.getScene().getWindow());
        stageProducto.initModality(Modality.WINDOW_MODAL);
        stageProducto.initStyle(StageStyle.DECORATED);
        stageProducto.setResizable(false);
        stageProducto.setOnCloseRequest((WindowEvent e) -> {
            root.setEffect(null);
        });
        stageProducto.setOnHidden((WindowEvent e) -> {
            root.setEffect(null);
        });

        try {
            this.conexionBD.conectar();
            productoDAO = new ProductoDAO(conexionBD);
            registrarProductoController.setProducto(productoDAO.getById(objProducto.get().getIdproducto()));
        }catch (SQLException ex){
            Logger.getLogger(ProductoController.class.getName()).log(Level.SEVERE, null, ex);
            org.controlsfx.control.Notifications.create().title("Aviso").text("Error al intentar buscar el producto\n"+ex.getMessage()).position(Pos.CENTER).showError();
        }finally {
            this.conexionBD.CERRAR();
        }
        stageProducto.setTitle("Editar Producto");
        stageProducto.showAndWait();
        if (registrarProductoController.isSTATUS()) {
            listarProductos(null);
        }
    }

    @FXML
    void listarProductos(ActionEvent event) {
        Task<List<Producto>> listTask = new Task<List<Producto>>() {
            @Override
            protected List<Producto> call() throws Exception {
                conexionBD.conectar();
                productoDAO = new ProductoDAO(conexionBD);
                return productoDAO.getProductos();
            }
        };

        listTask.setOnFailed(event1 -> {
            System.out.println("Error a listar productos");
            conexionBD.CERRAR();
            tablaProductos.setPlaceholder(null);
        });
        listTask.setOnSucceeded(event1 -> {
            tablaProductos.setPlaceholder(null);
            conexionBD.CERRAR();
            listaProductos.setAll(listTask.getValue());
            tablaProductos.getColumns().forEach(column -> {
                com.systemnecs.util.Metodos.changeSizeOnColumn(column, tablaProductos, -1);
            });
        });

        ProgressIndicator progressIndicator = new ProgressIndicator();
        tablaProductos.setPlaceholder(progressIndicator);

        Thread hilo = new Thread(listTask);
        hilo.start();
    }

    @FXML
    void nuevoProducto(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/RegistrarProducto.fxml"));
        AnchorPane anchorPane = loader.load();
        registrarProductoController = loader.getController();
        Scene scene = new Scene(anchorPane);
        stageProducto = new Stage();
        stageProducto.setTitle("Nuevo Producto");
        stageProducto.getIcons().add(new Image(this.getClass().getResourceAsStream("/images/productos.png")));
        stageProducto.setScene(scene);
        stageProducto.initOwner(root.getScene().getWindow());
        stageProducto.initModality(Modality.WINDOW_MODAL);
        stageProducto.initStyle(StageStyle.DECORATED);
        stageProducto.setResizable(false);
        stageProducto.setOnCloseRequest((WindowEvent e) -> {
            root.setEffect(null);
        });
        stageProducto.setOnHidden((WindowEvent e) -> {
            root.setEffect(null);
        });
        root.setEffect(new GaussianBlur(7.0));
        stageProducto.showAndWait();
        listarProductos(null);

    }
}
