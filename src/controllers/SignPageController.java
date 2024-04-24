package controllers;

import utils.JavafxFunctions;
import utils.Prefs;
import utils.SanityClient;
import utils.UserData;

import java.io.IOException;
import java.net.URISyntaxException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public class SignPageController {

    @FXML
    private Text errorMessage;

    @FXML
    private TextField emailField;
    @FXML
    private TextField passwordField;

    @FXML
    StackPane loadingContainer;

    @FXML
    void checkLoginInfo(ActionEvent event) throws IOException, URISyntaxException, InterruptedException {
        // This function gets invoke when the user press on the `login` button.
        // Retrive all the associated information with the email and password from the
        // Sanity Client

        // ............

        // Get the value of the email and passwrod fields
        String email = emailField.getText();
        String password = passwordField.getText();

        // pass the email and passwrod to check if the user information are correct
        boolean isUserExists = SanityClient.isUserExists(email, password);
        loadingContainer.setVisible(true);

        if (isUserExists) {
            System.out.print("loading");

            String userRole = UserData.userRole;

            Prefs.saveEmailAndPasswordAndRole(email, password, userRole); // save the email and passwrod globally

            // in case information are correct, Redirect to the Home page
            JavafxFunctions homePage = new JavafxFunctions(); // we should create an instance because the function is
                                                              // not static. (it contains non-static function)
            // Creaet the Home Page and open it
            String fxmlPath = Prefs.getFxmlPath();
            homePage.initializeStage(event, fxmlPath, "Home");
            // loadingContainer.setVisible(false);

        } else {
            // in case not, show an error message to the user.
            errorMessage.setVisible(true);
        }

    }
}
