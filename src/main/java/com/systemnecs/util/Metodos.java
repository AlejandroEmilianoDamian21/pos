package com.systemnecs.util;

import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.util.Duration;

import javafx.scene.Node;

public class Metodos {
    public static  void closeEffect(Node node){
        final Stage stage = (Stage) node.getScene().getWindow();
        ScaleTransition st = new ScaleTransition(Duration.millis(100), node);
        st.setToX(0);
        st.setToY(0);
        st.setToZ(0);
        st.play();
        st.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                stage.close();
            }
        });
    }
}
