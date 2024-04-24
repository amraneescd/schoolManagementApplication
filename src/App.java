
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utils.Prefs;

public class App extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        String fxmlPath = Prefs.getFxmlPath();

        // Load the FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        Parent root = loader.load();

        // Set the scene dimensions to fit the screen
        Scene scene = new Scene(root, 870, 550);

        // Set the title of the stage
        stage.setTitle("Sign in Page");

        // Add the scene to the stage and show it
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
