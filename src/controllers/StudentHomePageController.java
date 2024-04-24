package controllers;

import javafx.event.ActionEvent;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.prefs.BackingStoreException;

import org.json.JSONObject;

import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import utils.JavafxFunctions;
import utils.UserData;
import utils.Prefs;

public class StudentHomePageController {
    @FXML
    VBox teachersContainer;

    @FXML
    Text studentNameContainer;

    @FXML
    Text gradeContainer;

    @FXML
    Text classTextContainer;

    @FXML
    public void initialize() throws URISyntaxException, IOException, InterruptedException, BackingStoreException {
        // function will run as soon as the page loaded
        // ............

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
        // globalData.clear();
        displayStudentData();
        displayStudentTeachersData();

    }

    public void displayStudentData() {
        // This function will display the logged in student data from the class
        // `UserData`
        // (which holds the student data from the database)

        JSONObject studentData = UserData.getStudentData(); // Gets the logged in student data

        // Then store them in variables
        String stuedntName = studentData.getString("userFirstName") + " " + studentData.getString("userLastName");
        String studentGrade = studentData.getString("studentGrade");
        String studentClass = studentData.getString("studentClass");

        // set the the UI to student data for displaying later
        studentNameContainer.setText(stuedntName);
        gradeContainer.setText(studentGrade);
        classTextContainer.setText(studentClass);
    }

    public void displayStudentTeachersData() throws URISyntaxException, IOException, InterruptedException {
        // This function will display all the student teachers
        // ............
        int teachersCount = UserData.studentTeachers.size();

        // Loop thgouh all the student teachers, and every loop create a teacher
        // container then displays it.
        for (int index = 0; index <= teachersCount - 1; index++) {
            String teacher_id = UserData.studentTeachers.get(index);

            JSONObject teacherData = UserData.getStudentTeachers(teacher_id);
            String teacherName = teacherData.getString("teacherFirstName") + " "
                    + teacherData.getString("teacherLastName");

            int ratingStarsCount = teacherData.getInt("ratingStarsCount"); // Fetch the rating stars of the teacer to
                                                                           // the user

            // Create a teacher container and pass it the teacher name, and the star rating.
            HBox teacherContainer = JavafxFunctions.createTeacherContainer(teacherName, ratingStarsCount);
            teachersContainer.getChildren().add(teacherContainer); // Display the entire teacher container

            teacherContainer.setOnMouseClicked(event -> {
                // Wen click on the teacher prfoile, this function will handle the displaying
                // teacher profile

                JavafxFunctions userProfile = new JavafxFunctions();
                try {
                    userProfile.goToUserProfile(event, teacher_id);
                } catch (IOException e) {
                }
            });
        }
    }

    @FXML
    public void logout(ActionEvent event) throws IOException, BackingStoreException {
        // log out when clicking on the button
        Prefs.logout(event);
    }

    @FXML
    void topStudents(ActionEvent event) throws IOException {
        // go to `TopStudents` page when click on the button
        JavafxFunctions topStudents = new JavafxFunctions();
        topStudents.initializeStage(event, "/views/TopStudents.fxml", "Top Students");
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
