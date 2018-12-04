package core;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class View extends Application {
    public static void main(String[] args) {
        launch(args);
    }

	@Override
	public void start(Stage stage) throws IOException {
        stage.setTitle("Rummikub");
        Parent root = FXMLLoader.load(getClass().getResource("/core/StartScreen.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
	}
}
