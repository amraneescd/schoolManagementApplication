package controllers;

import java.io.IOException;
import java.net.URISyntaxException;

import org.json.JSONArray;
import org.json.JSONObject;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import utils.JavafxFunctions;
import utils.SanityClient;
import utils.UserData;

public class UserProfileController implements JavafxFunctions.Controller {

    String _id;
    String previousfxmlFilePath;
    String previousTitle;

    @FXML
    VBox userContainer, commentsContainer;

    @FXML
    TextArea commentField;

    @Override
    public void setData(String data) {
        // this function gets the user id from outside this controller,
        // then implment the necessary logic to diaplay the user profile
        this._id = data;

        try {
            displayStudentData();
        } catch (URISyntaxException | IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void displayStudentData() throws URISyntaxException, IOException, InterruptedException {
        // When clicking on a user profile (student or teacher). we pass his `_id` to
        // dispaly his information in this file

        // ............
        // get data of the clicked user profile from the database
        JSONArray result = SanityClient.client("*[_id=='" + _id + "']");
        JSONObject user = (JSONObject) result.get(0);

        // create the profile image for the UI
        ImageView imageView = new ImageView();
        Image userProfile = new Image("/assets/images/defaultMaleProfile.png");
        imageView.setFitWidth(200);
        imageView.setFitHeight(180);
        imageView.setImage(userProfile);

        if (user.has("comments")) {
            // in case some has commented on the user profile before
            JSONArray comments = user.getJSONArray("comments");
            displayComments(comments);
        }

        String userName;
        Text userNameText;

        // students and teachers have different properties, so we need to check the
        // role first, then assign the right propperties for each one

        if (user.getString("userRole").equals("student")) {
            // in case the clicked user profile is a studnet

            userName = user.getString("userFirstName") + " " + user.getString("userLastName");
            String studnetGrade = user.getString("studentGrade");
            String studnetCLass = user.getString("studentClass");

            // user name
            userNameText = new Text();
            userNameText.setFill(Paint.valueOf("black"));
            userNameText.setFont(Font.font("Verdana", FontWeight.BOLD, 18));
            userNameText.setText(userName);

            Text studnetGradeText = new Text();
            studnetGradeText.setFill(Paint.valueOf("black"));
            studnetGradeText.setFont(Font.font("Verdana", FontWeight.BOLD, 18));
            studnetGradeText.setText("Student Grade: " + studnetGrade + "%");

            Text studnetCLassText = new Text();
            studnetCLassText.setFill(Paint.valueOf("black"));
            studnetCLassText.setFont(Font.font("Verdana", FontWeight.BOLD, 18));
            studnetCLassText.setText("Studnet Class: " + studnetCLass);

            // finally display the infromation
            userContainer.getChildren().addAll(imageView, userNameText, studnetGradeText, studnetCLassText);

        } else {
            // in case the clicked user profile is a studnet
            userName = user.getString("teacherFirstName") + " " + user.getString("teacherLastName");

            // Create the username container for the UI
            userNameText = new Text();
            userNameText.setFill(Paint.valueOf("black"));
            userNameText.setFont(Font.font("Verdana", FontWeight.BOLD, 18));
            userNameText.setText(userName);

            userContainer.getChildren().addAll(imageView, userNameText); // finally display the infromation

        }
    }

    public void displayComments(JSONArray comments) {
        // This function will display all the comments associated with the
        // clicked user profile

        if (comments.length() > 0) {
            comments.forEach((comment) -> {

                // in case there are indeed comments, then the following
                // code gets the comment and the user's data who posted the comment
                // then finally display it in the UI

                String commentContent = (((JSONObject) comment).getString("comment"));
                JSONObject commenttedStudent = (((JSONObject) comment).getJSONObject("student"));
                String commenttedStudent_id = commenttedStudent.getString("_ref");

                try {

                    JSONObject commenttedStudentData = (JSONObject) SanityClient
                            .client("*[_id=='" + commenttedStudent_id + "']").get(0);
                    String commenttedStudentFirstName = commenttedStudentData.getString("userFirstName");
                    String commenttedStudentLastName = commenttedStudentData.getString("userLastName");
                    String fullName = commenttedStudentFirstName + " " + commenttedStudentLastName;

                    HBox commentContainer = createCommentContainer(fullName, commentContent);
                    commentsContainer.getChildren().add(commentContainer);

                } catch (URISyntaxException | IOException | InterruptedException e) {
                    System.err.println(e);
                }
            });

        } else {
            System.err.println("No comments");
        }

    }

    public HBox createCommentContainer(String commenttedStudentName, String commentContext) {
        // This function will handle the UI aspect, the comment needs to be well
        // represented in a nice container that holds the data of the user who posted
        // the comment.

        // Create the profile image
        ImageView imageView = new ImageView();
        Image image = new Image("/assets/images/defaultMaleProfile.png");
        imageView.setImage(image);
        imageView.setFitWidth(80);
        imageView.setFitHeight(70);

        // Create the name container
        Text commenttedStudentNameText = new Text();
        commenttedStudentNameText.fontProperty();
        commenttedStudentNameText.setText(commenttedStudentName);
        commenttedStudentNameText.setFill(Paint.valueOf("white"));
        commenttedStudentNameText.setFont(Font.font("Verdana", FontWeight.BOLD, 12));

        // the comment itself
        Text commentContextText = new Text();
        commentContextText.setText(commentContext);
        commentContextText.setFill(Paint.valueOf("white"));
        commentContextText.setFont(Font.font("Verdana", FontWeight.LIGHT, 18));

        // Create the container that will hold both name and the comment
        VBox namdAndCommentContainer = new VBox();
        namdAndCommentContainer.setAlignment(Pos.CENTER_LEFT);
        namdAndCommentContainer.getChildren().addAll(commenttedStudentNameText, commentContextText);

        // Create the container for all the previous elements
        HBox commentContainer = new HBox();
        commentContainer.getChildren().addAll(imageView, namdAndCommentContainer);

        return commentContainer; // finally return it for the sake of displaying it in the UI
    }

    @FXML
    public void postComment() {
        // handling posting a comment
        String comment = commentField.getText();

        if (!comment.equals("")) {
            // if the comment is not empty, post it
            // .............
            // Update the UI
            String fullNmae = UserData.userFirstName + " " + UserData.userLastName;
            HBox commentContainer = createCommentContainer(fullNmae, comment);
            commentsContainer.getChildren().add(commentContainer);
        }
    }

    @FXML
    public void goBack(MouseEvent event) throws IOException {
        // Go back to the previous page when clicking on the button
        JavafxFunctions.goBack(event);
    }
}
