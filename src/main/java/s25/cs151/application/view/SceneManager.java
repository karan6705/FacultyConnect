package s25.cs151.application.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.IOException;

public class SceneManager {
    private Stage stage;

    public SceneManager(Stage stage) {
        this.stage = stage;
    }

    public static void switchScene(ActionEvent event, String fxmlPath, double width, double height) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource(fxmlPath));
            Parent root = loader.load();

            Stage stage;

            // Use the stage from the event if available
            if (event != null) {
                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            } else {
                // If no event, we assume it's the primary stage that was already created
                stage = (Stage) root.getScene().getWindow();
                if (stage == null) {
                    // If the stage is still null, we create a new one (for initial window)
                    stage = new Stage();
                }
            }

            Scene scene = new Scene(root, width, height);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Replace with a dialog or logger in production
        }
    }



    public static void switchScene(ActionEvent event, String fxmlPath) {
        switchScene(event, fxmlPath, 600, 400); // Default size
    }
}

