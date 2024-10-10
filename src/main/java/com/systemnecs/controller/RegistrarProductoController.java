package com.systemnecs.controller;

import animatefx.animation.Tada;
import com.github.sarxos.webcam.Webcam;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import com.systemnecs.dao.ProductoDAO;
import com.systemnecs.model.Producto;
import com.systemnecs.util.ConexionBD;
import com.systemnecs.util.Metodos;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.Initializable;
import javafx.util.StringConverter;
import javafx.stage.FileChooser;
import org.controlsfx.control.Notifications;

import javax.imageio.ImageIO;
import java.io.*;

import java.awt.image.BufferedImage;
import java.net.URL;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

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

    private boolean estadoCamara = false;
    private Webcam selWebCam = null;
    private BufferedImage bufferImage;
    private ObjectProperty<Image> imageProperty = new SimpleObjectProperty<>();

    private ConexionBD conexionBD = new ConexionBD();
    private ProductoDAO productoDAO;

    private Producto producto = null;

    private boolean STATUS = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cjprecio.setTextFormatter(new TextFormatter<>(new StringConverter<Double>() {
            @Override
            public String toString(Double object) {
                if (object != null){
                    return NumberFormat.getCurrencyInstance().format(object);
                }
                return NumberFormat.getCurrencyInstance().format(0.0);
            }

            @Override
            public Double fromString(String valor) {
                try {
                    return NumberFormat.getCurrencyInstance().parse(valor).doubleValue();
                } catch (ParseException e) {
                    try {
                        return NumberFormat.getCurrencyInstance().parse("$".concat(valor)).doubleValue();
                    } catch (ParseException ex) {
                        ex.printStackTrace();
                    }
                }
                return 0.0;
            }
        }));
    }

    @FXML
    void abrirCamara(ActionEvent event) {

        if (Webcam.getWebcams().size() < 1) {
            Alert a = new Alert(Alert.AlertType.INFORMATION, "NO HAY CAMARAS DISPONIBLES", ButtonType.OK);
            a.showAndWait();
            return;
        }

        if (!estadoCamara) {
            imageView.imageProperty().unbind();
            imageView.setImage(new Image(getClass().getResourceAsStream("/images/load.gif")));
            Task<Void> webCamIntilizer = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    if (selWebCam == null) {
                        selWebCam = Webcam.getWebcams().get(0);
                        selWebCam.open();
                    }

                    estadoCamara = true;
                    Task<Void> task = new Task<Void>() {
                        @Override
                        protected Void call() throws Exception {
                            while (estadoCamara) {
                                try {
                                    if ((bufferImage = selWebCam.getImage()) != null) {
                                        Platform.runLater(() -> {
                                            final Image mainiamge = SwingFXUtils.toFXImage(bufferImage, null);
                                            imageProperty.set(mainiamge);
                                        });
                                        bufferImage.flush();
                                    }
                                } catch (Exception e) {
                                } finally {
                                }
                            }
                            return null;
                        }
                    };
                    Thread th = new Thread(task);
                    th.setDaemon(true);
                    th.start();
                    imageView.imageProperty().bind(imageProperty);

                    return null;
                }
            };
            new Thread(webCamIntilizer).start();
        } else {
            estadoCamara = false;
        }
    }

    @FXML
    void buscarImagen(ActionEvent event) {
        /**/ // Crear un FileChooser
        FileChooser fileChooser = new FileChooser();

        // Filtro para permitir solo la selección de archivos de imagen
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Imágenes", "*.jpg", "*.jpeg", "*.png")
        );

        // Mostrar el cuadro de diálogo para que el usuario seleccione un archivo
        File file = fileChooser.showOpenDialog(root.getScene().getWindow());

        // Verificar que el archivo no sea null (usuario seleccionó un archivo)
        if (file != null) {
            try {
                // Convertir el archivo en una imagen de JavaFX
                FileInputStream inputStream = new FileInputStream(file);
                Image image = new Image(inputStream);

                // Asignar la imagen seleccionada al ImageView
                imageView.setImage(image);
                System.out.println("Imagen cargada correctamente en el ImageView.");

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                // En caso de error, puedes mostrar una alerta o manejar la excepción
                Alert alert = new Alert(Alert.AlertType.ERROR, "No se pudo cargar la imagen", ButtonType.OK);
                alert.showAndWait();
            }
        }
    }
    /**/
    @FXML
    void guardar(ActionEvent event) {

        if (cjnombre.getText().isEmpty()) {
            new Tada(cjnombre).play();
            org.controlsfx.control.Notifications.create().title("Aviso").text("Ingrese el nombre del p").position(Pos.CENTER).showWarning();
            return;
        }
        double precio = 0;
        try {
            precio = (NumberFormat.getCurrencyInstance().parse((cjprecio.getText())).doubleValue());
        } catch (ParseException ex) {
            new Tada(cjprecio).play();
            org.controlsfx.control.Notifications.create().title("Aviso").text("Precio no valido\n" + ex.getMessage()).position(Pos.CENTER).showError();
            return;
        }
        if (cjfechavencimiento.getValue() == null) {
            new Tada(cjfechavencimiento).play();
            org.controlsfx.control.Notifications.create().title("Aviso").text("Seleccione una fecha de vencimiento").position(Pos.CENTER).showWarning();
            return;
        }

        if (getProducto() == null) {
            this.producto = new Producto();
        }

        producto.setNombreproducto(cjnombre.getText().trim());
        producto.setCodigodebarras(cjcodigo.getText().trim());
        producto.setReferencia(cjreferencia.getText().trim());
        producto.setStock(Double.parseDouble(cjstock.getText()));
        producto.setStockminimo(Double.parseDouble(cjstockminimo.getText()));
        producto.setPrecio(precio);
        producto.setFechavencimiento(cjfechavencimiento.getValue());

        if (imageView.getImage() != null) {
            producto.setImagen(com.systemnecs.util.Metodos.ImageToByte(imageView.getImage()));
        } else {
            producto.setImagen(null); // Si no hay imagen seleccionada, guarda null
        }
        conexionBD.conectar();
        productoDAO = new ProductoDAO(conexionBD);
        try {
            int guardar = productoDAO.guardar(producto);
            if ( guardar > 0 ){
                setSTATUS(true);
                com.systemnecs.util.Metodos.closeEffect(root);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setProducto(Producto producto) {
        this.producto = producto;

        cjnombre.setText(producto.getNombreproducto().trim());
        cjcodigo.setText(producto.getCodigodebarras());
        cjreferencia.setText(producto.getReferencia());
        cjstock.setText(""+ producto.getStock());
        cjstockminimo.setText(""+ producto.getStockminimo());
        cjprecio.setText((NumberFormat.getCurrencyInstance().format(producto.getPrecio())));
        cjfechavencimiento.setValue(producto.getFechavencimiento());
        try {
            if (producto.getImagen() != null) {
                imageView.setImage(SwingFXUtils.toFXImage(ImageIO.read(new ByteArrayInputStream(producto.getImagen())), null));
            }
            else {
                imageView.setImage(null); // Limpiar el imageView si no hay imagen
            }
        } catch (IOException ex) {
            Logger.getLogger(RegistrarProductoController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    public Producto getProducto() {
        return producto;
    }

    public boolean isSTATUS() {
        return STATUS;
    }

    public void setSTATUS(boolean STATUS) {
        this.STATUS = STATUS;
    }
}
