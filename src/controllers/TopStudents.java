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

public class TopStudents {

    class StudentHBox {
        // We created this class in order to sort the Top studnets
        HBox hbox;
        int grade;

        public StudentHBox(HBox hbox, int grade) {
            this.hbox = hbox;
            this.grade = grade;
        }
    }

    // An array will hold all the containers of `outstaniding hbox in order to sort
    // them and displaying them later
    List<StudentHBox> students = new ArrayList<>();

    @FXML
    VBox topStudentsContainer;

    @FXML
    public void initialize() throws URISyntaxException, IOException, InterruptedException {
        JSONArray result = SanityClient.client("*[_type=='student']"); // Fetching all the students inside an array

        // Get the information of all the studnets then add it to `StudentHBox`
        // ArrayList
        result.forEach(student -> {
            String student_id = ((JSONObject) student).getString("_id");
            String studentFirstName = ((JSONObject) student).getString("userFirstName");
            String studentLastName = ((JSONObject) student).getString("userLastName");
            String fullName = studentFirstName + " " + studentLastName;

            String studentClass = ((JSONObject) student).getString("studentClass");
            int studentGrade = ((JSONObject) student).getInt("studentGrade");

            System.err.println(student_id);
            HBox topStudentContainer = JavafxFunctions.createStudentContainer(fullName, studentClass,
                    Integer.toString(studentGrade),
                    student_id);

            // add them to the ArrayList `StudentHBox`
            this.students.add(new StudentHBox(topStudentContainer, studentGrade));
        });

        // And finally the sorgin part using the built-in class in java `Comparator`
        Collections.sort(students, new Comparator<StudentHBox>() {
            @Override
            public int compare(StudentHBox s2, StudentHBox s1) {
                return Integer.compare(s1.grade, s2.grade); // Sorting them by from the greater to the smaller
            }
        });

        // Add sorted HBoxes to the VBox
        for (StudentHBox s : students) {
            topStudentsContainer.getChildren().add(s.hbox); // Then displaying them in the UI
        }
    }

    @FXML
    public void goBack(MouseEvent event) throws IOException {
        // Go back to the previous page when clicking on the button
        JavafxFunctions.goBack(event);
    }

}
