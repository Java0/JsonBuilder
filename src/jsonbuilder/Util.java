package jsonbuilder;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Util {

    public static void loadFXMLWithDefault(Stage primaryStage, String FXMLPath) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(Util.class.getResource("/"+FXMLPath)));
        Scene scene = new Scene(root);
        primaryStage.setTitle("JsonBuilder");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }


}
