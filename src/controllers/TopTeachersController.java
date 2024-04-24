package controllers;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import utils.JavafxFunctions;
import utils.Prefs;
import utils.SanityClient;
import utils.UserData;

public class TopTeachersController {

    class TeacherentHBox {
        // We created this class in order to sort the Top studnets
        HBox hbox;
        int daysOfAbsence;

        public TeacherentHBox(HBox hbox, int daysOfAbsence) {
            this.hbox = hbox;
            this.daysOfAbsence = daysOfAbsence;
        }
    }

    // An array will hold all the containers of `outstaniding hbox in order to sort
    // them and displaying them later
    List<TeacherentHBox> teachers = new ArrayList<>();

    @FXML
    VBox topTeachersContainer;

    @FXML
    public void initialize() throws URISyntaxException, IOException, InterruptedException {
        JSONArray result = SanityClient.client("*[_type=='teacher']"); // Fetching all the teachers inside an array

        // Get the information of all the studnets then add it to `StudentHBox`
        // ArrayList
        result.forEach(teacher -> {
            String teacher_id = ((JSONObject) teacher).getString("_id");
            String teacherFirstName = ((JSONObject) teacher).getString("teacherFirstName");
            String teacherLastName = ((JSONObject) teacher).getString("teacherLastName");
            String fullName = teacherFirstName + " " + teacherLastName;

            int teacherDaysOfAbsence = ((JSONObject) teacher).getInt("teacherDaysOfAbsence");
            ;
            HBox topStudentContainer = JavafxFunctions.createTeacherContainer_TopAndWorstPage(fullName,
                    teacherDaysOfAbsence,
                    teacher_id);

            // add them to the ArrayList `StudentHBox`
            this.teachers.add(new TeacherentHBox(topStudentContainer, teacherDaysOfAbsence));
        });

        // And finally the sorgin part using the built-in class in java `Comparator`
        Collections.sort(teachers, new Comparator<TeacherentHBox>() {
            @Override
            public int compare(TeacherentHBox s2, TeacherentHBox s1) {
                return Integer.compare(s2.daysOfAbsence, s1.daysOfAbsence); // Sorting them by from the greater to the
                                                                            // smaller
            }
        });

        // Add sorted HBoxes to the VBox
        for (TeacherentHBox s : teachers) {
            topTeachersContainer.getChildren().add(s.hbox); // Then displaying them in the UI
        }
    }

    @FXML
    public void goBack(MouseEvent event) throws IOException {
        // Go back to the previous page when clicking on the button
        JavafxFunctions.goBack(event);
    }

}
