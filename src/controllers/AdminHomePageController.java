package controllers;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.prefs.BackingStoreException;

import org.json.JSONArray;
import org.json.JSONObject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import utils.JavafxFunctions;
import utils.Prefs;
import utils.SanityClient;

public class AdminHomePageController {

    @FXML
    private Text totalStudentsContainer1;
    @FXML
    private Text totalStudentsContainer2;

    @FXML
    private GridPane studentsContainer;

    @FXML
    VBox teachersContainer;

    @FXML
    public void initialize() throws URISyntaxException, IOException, InterruptedException {
        // check if the user has logged in before or this is the first time
        Boolean isLoggedInBefore = Prefs.isLoggedIn();
        if (isLoggedInBefore.equals(true)) {
            // if this is the first time the user log in, then this block WILL NOT execute.
            // Because we already saved the user data in `SignPageController` when the user
            // loggedin.
            // So duplicate the user data will cause issues.

            // in case he logged before, then this block will execute.
            // because since the user was already logged, then the `SignPageController`
            // wasn't executed.
            // then the user data wasn't save before, so we save it now.

            Prefs.loadData();
        }

        Prefs.setLoggedIn();// finally tells the program the user was signed in before

        displayStudents();
        displayTeachers();
    }

    int colIndex = 0;
    int rowIndex = 0;
    int studnetsCount = 0;

    public void displayStudents() throws URISyntaxException, IOException, InterruptedException {

        // This function will simply retrive all the studnets data from the Sanity
        // database
        // then display it in javafx `HBox`

        JSONArray students = SanityClient.client("*[_type=='student']");
        students.forEach((student) -> {
            if (((JSONObject) student).getString("userRole").equals("student")) { // Only the student (excluding the
                                                                                  // admins)
                studnetsCount++;

                // storing student data in variables
                String student_id = ((JSONObject) student).getString("_id");
                String studentFirstName = ((JSONObject) student).getString("userFirstName");
                String studentLastName = ((JSONObject) student).getString("userLastName");
                String studentName = studentFirstName + " " + studentLastName;
                String studentGrade = ((JSONObject) student).getString("studentGrade");
                String studentClass = ((JSONObject) student).getString("studentClass");

                // include those data in an `Hbox`
                HBox studentContainer = JavafxFunctions.createStudentContainer(studentName, studentClass,
                        studentGrade, student_id);

                studentsContainer.add(studentContainer, rowIndex, colIndex); // then add it to the UI
                rowIndex++;

                // because it's a grid we need to specify the exact position (column and row)
                // and we are in a for loop so we can't speicfy them...
                // so we use variabels that changes in every loop to keep the UI updated
                if (rowIndex % 2 == 0) { // go to the next column
                    colIndex++;
                    rowIndex = 0; // clear the value of the row index, in order to start over from the next line
                }

            }

        });

        // setting the `students count` in the UI in both locations
        // (top and right sidebar in the UI)
        totalStudentsContainer1.setText(Integer.toString(studnetsCount));
        totalStudentsContainer2.setText(Integer.toString(studnetsCount));
    }

    public void displayTeachers() throws URISyntaxException, IOException, InterruptedException {
        // Similar to the previous function, but displaying the teachers this time

        JSONArray teachers = SanityClient.client("*[_type=='teacher']");
        teachers.forEach((teacher) -> {
            String teacher_id = ((JSONObject) teacher).getString("_id");
            String teacherFirstName = ((JSONObject) teacher).getString("teacherFirstName");
            String teacherLastName = ((JSONObject) teacher).getString("teacherLastName");
            String teacherName = teacherFirstName + " " + teacherLastName;
            int teacherDaysOfAbsence = ((JSONObject) teacher).getInt("teacherDaysOfAbsence");

            HBox teacherContainer = JavafxFunctions.createTeacherContainer_AdminPage(teacherName, teacherDaysOfAbsence,
                    teacher_id);
            teachersContainer.getChildren().add(teacherContainer);

        });
    }

    @FXML
    public void logout(ActionEvent event) throws IOException, BackingStoreException {
        // log out when pressing the button
        Prefs.logout(event);
    }

    @FXML
    void topStudents(ActionEvent event) throws IOException {
        // go to `TopStudents` page when click on the button
        JavafxFunctions topStudents = new JavafxFunctions();
        topStudents.initializeStage(event, "/views/TopStudents.fxml", "Top Students Students");
    }

    @FXML
    void worstStudents(ActionEvent event) throws IOException {
        // go to `WorstStudents` page when click on the button
        JavafxFunctions topStudents = new JavafxFunctions();
        topStudents.initializeStage(event, "/views/WorstStudents.fxml", "Top Students");
    }

    @FXML
    void topTeachers(ActionEvent event) throws IOException {
        // go to `TopTeachers` page when click on the button
        JavafxFunctions topStudents = new JavafxFunctions();
        topStudents.initializeStage(event, "/views/TopTeachers.fxml", "Top Students");
    }

    @FXML
    void worstTeachers(ActionEvent event) throws IOException {
        // go to `WorstTeachers` page when click on the button
        JavafxFunctions topStudents = new JavafxFunctions();
        topStudents.initializeStage(event, "/views/WorstTeachers.fxml", "Top Students");
    }

    @FXML
    void about(ActionEvent event) throws IOException {
        // go to `About` page when click on the button
        JavafxFunctions topStudents = new JavafxFunctions();
        topStudents.initializeStage(event, "/views/About.fxml", "Top Students");
    }

}
