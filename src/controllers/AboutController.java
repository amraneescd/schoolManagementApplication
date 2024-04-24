package controllers;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import utils.JavafxFunctions;

public class AboutController {

    @FXML
    public void goBack(MouseEvent event) throws IOException {
        // Go back to the previous page when clicking on the button
        JavafxFunctions.goBack(event);
    }
}
