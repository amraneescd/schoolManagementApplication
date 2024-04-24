package utils;

import javafx.scene.Cursor;
import javafx.scene.Node;
import java.io.IOException;
import java.util.EventObject;

import org.json.JSONObject;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class JavafxFunctions {

    public interface Controller {
        // this interface is for the sake of passing data to a controller in case we
        // needed
        void setData(String data);
    }

    // Simply change the scene, give it the `EventObject`, bath for the new scene,
    // title of the new scene, and in case you want to pass a data for the new scene
    // controller, then pass it through `data
    public Stage initializeStage(EventObject event, String fxmlFilePath, String title, String... data)
            throws IOException {
        // Load the new FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFilePath));
        Parent root = loader.load();

        if (data.length > 0) {
            // This will pass data to the new controller ONLY if the there there is data to
            // pass.
            String StringData = data[0];
            Controller controller = loader.getController();
            controller.setData(StringData);
        }

        // Get the current stage and set the new scene
        Stage stage = (Stage) ((Node) (event).getSource()).getScene().getWindow();
        stage.setScene(new Scene(root, 870, 550));

        // Show the new scene
        stage.setTitle(title);
        stage.show();

        return stage;
    }

    public void goToUserProfile(EventObject event, String user_id) throws IOException {
        // This function will open the clicked user/teacher profile.
        // it passes the `user_id` to `initializeStage` function
        // then `initializeStage` passes this id to `UserProfileController` to display
        // the user data

        initializeStage(event, "/views/UserProfile.fxml", "User Profile", user_id);
    }

    public static HBox createTeacherContainer(String teacherName, int ratingStarsCount) {
        // This function will create the teacher container in the StudentHomePage.
        // And this container will have the teacher properties from the Sanity database

        // ............

        // Create the teacher image
        ImageView imageView = new ImageView();
        Image image = new Image("/assets/images/defaultMaleProfile.png");
        imageView.setImage(image);
        imageView.setFitWidth(80);
        imageView.setFitHeight(70);

        // Create the teacher name
        Text teacherNameContainre = new Text();
        teacherNameContainre.fontProperty();
        teacherNameContainre.setText(teacherName);
        teacherNameContainre.setFill(Paint.valueOf("white"));
        teacherNameContainre.setFont(Font.font("Verdana", FontWeight.BOLD, 18));

        // include the stars rating container
        HBox ratingStarsContainer = createRatingStarsContainer(ratingStarsCount);

        // Create the container that will hold both teacher name and his rating for the
        // student
        VBox nameAndRatingContainer = new VBox();
        nameAndRatingContainer.setAlignment(Pos.CENTER_LEFT);
        nameAndRatingContainer.getChildren().addAll(teacherNameContainre, ratingStarsContainer);

        // Create the container for all the previous elements
        HBox teacherContainer = new HBox();
        teacherContainer.getChildren().addAll(imageView, nameAndRatingContainer);
        teacherContainer.setCursor(Cursor.HAND);

        return teacherContainer; // Finally return the entire container
    }

    public static HBox createRatingStarsContainer(int ratingStarsCount) {
        // This function will handle the logic of adding rating stars
        // for example if the teacher rated a student `4`. then this function will
        // create `4` stars and put them in an `HBox` then return it in order to display
        // it in the UI

        // ............

        // Create the teacher image
        HBox ratingStarsContainer = new HBox();

        for (int index = 0; index <= ratingStarsCount - 1; index++) {
            ImageView imageView = new ImageView();
            Image image = new Image("/assets/images/ratingStar.png");
            imageView.setImage(image);
            imageView.setFitWidth(20);
            imageView.setFitHeight(20);

            ratingStarsContainer.getChildren().add(imageView);

        }

        return ratingStarsContainer;

    }

    public static HBox createTeacherContainer_AdminPage(String teacherName, int teacherDaysOfAbsence,
            String teacher_id) {
        // This function will create a teacher container for the `AdminHomePage`

        ImageView imageView = new ImageView();
        Image image = new Image("/assets/images/defaultMaleProfile.png");
        imageView.setImage(image);
        imageView.setFitWidth(80);
        imageView.setFitHeight(70);

        // Create the teacher name
        Text teacherNameText = new Text();
        teacherNameText.fontProperty();
        teacherNameText.setText(teacherName);
        teacherNameText.setFill(Paint.valueOf("white"));
        teacherNameText.setFont(Font.font("Verdana", FontWeight.BOLD, 18));

        Text teacherDaysOfAbsenceText = new Text();
        teacherDaysOfAbsenceText.setText("Total Days of absence (" + Integer.toString(teacherDaysOfAbsence) + ")");
        teacherDaysOfAbsenceText.setFill(Paint.valueOf("gray"));
        teacherDaysOfAbsenceText.setFont(Font.font("Verdana", FontWeight.BOLD, 10));

        // Create the container that will hold both teacher name and his rating for the
        // student
        VBox nameAndDaysOfAbsenceContainer = new VBox();
        nameAndDaysOfAbsenceContainer.setAlignment(Pos.CENTER_LEFT);
        nameAndDaysOfAbsenceContainer.getChildren().addAll(teacherNameText, teacherDaysOfAbsenceText);

        // Create the container for all the previous elements
        HBox teacherContainer = new HBox();
        teacherContainer.getChildren().addAll(imageView, nameAndDaysOfAbsenceContainer);
        teacherContainer.setCursor(Cursor.HAND);

        teacherContainer.setOnMouseClicked(event -> {
            // Wen click on the teacher prfoile, this function will handle the displaying
            // teacher profile

            JavafxFunctions userProfile = new JavafxFunctions();
            try {
                userProfile.goToUserProfile(event, teacher_id);
            } catch (IOException e) {
            }
        });

        return teacherContainer;
    }

    public static HBox createTeacherContainer_TopAndWorstPage(String teacherName, int teacherDaysOfAbsence,
            String teacher_id) {
        // This function will create a teacher container for the `AdminHomePage`

        ImageView imageView = new ImageView();
        Image image = new Image("/assets/images/defaultMaleProfile.png");
        imageView.setImage(image);
        imageView.setFitWidth(80);
        imageView.setFitHeight(70);

        // Create the teacher name
        Text teacherNameText = new Text();
        teacherNameText.fontProperty();
        teacherNameText.setText(teacherName);
        teacherNameText.setFill(Paint.valueOf("black"));
        teacherNameText.setFont(Font.font("Verdana", FontWeight.BOLD, 18));

        Text teacherDaysOfAbsenceText = new Text();
        teacherDaysOfAbsenceText.setText("Total Days of absence (" + Integer.toString(teacherDaysOfAbsence) + ")");
        teacherDaysOfAbsenceText.setFill(Paint.valueOf("gray"));
        teacherDaysOfAbsenceText.setFont(Font.font("Verdana", FontWeight.BOLD, 10));

        // Create the container that will hold both teacher name and his rating for the
        // student
        VBox nameAndDaysOfAbsenceContainer = new VBox();
        nameAndDaysOfAbsenceContainer.setAlignment(Pos.CENTER_LEFT);
        nameAndDaysOfAbsenceContainer.getChildren().addAll(teacherNameText, teacherDaysOfAbsenceText);

        // Create the container for all the previous elements
        HBox teacherContainer = new HBox();
        teacherContainer.getChildren().addAll(imageView, nameAndDaysOfAbsenceContainer);
        teacherContainer.setCursor(Cursor.HAND);

        teacherContainer.setOnMouseClicked(event -> {
            // Wen click on the teacher prfoile, this function will handle the displaying
            // teacher profile

            JavafxFunctions userProfile = new JavafxFunctions();
            try {
                userProfile.goToUserProfile(event, teacher_id);
            } catch (IOException e) {
            }
        });

        return teacherContainer;
    }

    public static HBox createStudentContainer(String studentName, String studentClass, String grade,
            String student_id) {
        // This function will create the student container in the AdminHomePage.
        // And this container will have the student properties from the Sanity database

        // ............

        // Create the student image
        ImageView imageView = new ImageView();
        Image image = new Image("/assets/images/defaultMaleProfile.png");
        imageView.setImage(image);
        imageView.setFitWidth(80);
        imageView.setFitHeight(70);

        StackPane imageViewContainer = new StackPane(imageView);

        // Create the teacher name
        Text studentNameContainer = new Text();
        studentNameContainer.fontProperty();
        studentNameContainer.setText(studentName);
        studentNameContainer.setFill(Paint.valueOf("black"));
        studentNameContainer.setFont(Font.font("Verdana", FontWeight.BOLD, 18));

        // Create the total grade text
        Text gradeContainer = new Text();
        gradeContainer.fontProperty();
        gradeContainer.setText("Total Grade (" + grade + "%)");
        gradeContainer.setFill(Paint.valueOf("gray"));
        gradeContainer.setFont(Font.font("Verdana", FontWeight.BOLD, 12));

        // Create the container that will hold both teacher name and his rating for the
        // student
        VBox nameAndGradeContainer = new VBox();
        nameAndGradeContainer.setAlignment(Pos.CENTER);
        nameAndGradeContainer.getChildren().addAll(studentNameContainer, gradeContainer);

        Text studentClassText = new Text();
        studentClassText.setText("cls." + studentClass);
        studentClassText.setFill(Paint.valueOf("white"));
        studentClassText.setFont(Font.font("Verdana", FontWeight.BOLD, 14));

        StackPane studentClassContainer = new StackPane(studentClassText);
        studentClassContainer.setId("classContainer");

        // Create the container for all the previous elements
        HBox studentContainer = new HBox();
        studentContainer.getChildren().addAll(imageViewContainer, nameAndGradeContainer, studentClassContainer);
        studentContainer.setCursor(Cursor.HAND);
        studentContainer.setId("studentContainer");

        studentContainer.setOnMouseClicked(event -> {
            // Wen click on the teacher prfoile, this function will handle the displaying
            // teacher profile

            JavafxFunctions userProfile = new JavafxFunctions();
            try {
                userProfile.goToUserProfile(event, student_id);
            } catch (IOException e) {
            }
        });

        return studentContainer;

    }

    public static void goBack(MouseEvent event) throws IOException {
        // Go back to the previous page when clicking on the button
        JavafxFunctions goBack = new JavafxFunctions();
        System.err.println(UserData.userRole);

        // Then load the right fxml file according to the user role (student or admin)
        if (UserData.userRole.equals("student")) {
            // everytime StudentHomePage` fxml loads, it will reload data again from
            // database. and thus, there will be duplicated elemnts.
            // to solve this we clear the data everytime so when fetching the data again, no
            // duplicated information or elements
            Prefs.clearUserDataClass();

            goBack.initializeStage(event, "/views/StudentHomePage.fxml", "Home Page");
        } else {
            goBack.initializeStage(event, "/views/AdminHomePage.fxml", "Home Page");
        }

    }
}