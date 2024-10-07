package com.systemnecs;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) throws IOException {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            HBox root = fxmlLoader.load(getClass().getResource("/fxml/Login.fxml"));
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Punto de Venta");
            stage.getIcons().add(new Image(this.getClass().getResourceAsStream("/images/pos-terminal.png")));
            stage.setScene(scene);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setResizable(false);
            stage.setIconified(false);
            stage.showAndWait();
            }catch (IOException e){
            e.printStackTrace();
            System.out.println("Error al cargar Login");
        }

        }
}
