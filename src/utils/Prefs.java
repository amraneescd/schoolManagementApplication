package utils;

import javafx.event.ActionEvent;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class Prefs {

    public static void setLoggedIn() {
        // After the user login, this function will store a preference for that
        // this for the sake of not asking the email and password everytime
        Preferences globalData = Preferences.userRoot();
        globalData.putBoolean("loggedIn", true);
    }

    public static boolean isLoggedIn() {
        // This function check if the preference `loggedIn` true or not.
        // in case true, then it will not ask for the email and password
        Preferences globalData = Preferences.userRoot();
        Boolean isLoggedInBefore = globalData.getBoolean("loggedIn", false);
        return isLoggedInBefore;
    }

    public static void loadData() throws URISyntaxException, IOException,
            InterruptedException {

        // when the program launch, of course we need to import the user data whether he
        // was logged in before or not
        Preferences globalData = Preferences.userRoot();
        String email = globalData.get("email", null);
        String password = globalData.get("password", null);
        String userRole = globalData.get("userRole", null);

        if (email != null && password != null && userRole != null) {
            SanityClient.isUserExists(email, password); // check if the user is exists indeed and set his data
        }
    }

    public static void saveEmailAndPasswordAndRole(String email, String password, String userRole) {
        // After loggin in, we need to save user's data in order to load his data from
        // the database using his saved email and password
        Preferences globalData = Preferences.userRoot();
        globalData.put("email", email);
        globalData.put("password", password);
        globalData.put("userRole", userRole);
    }

    public static String getFxmlPath() {
        // when the user launch the application, this function will decide what the page
        // should be displaying.
        // whether it was the signin page, student page, admin page, etc..
        Preferences globalData = Preferences.userRoot();
        String email = globalData.get("email", null);
        String password = globalData.get("password", null);
        String userRole = globalData.get("userRole", null);

        String fxmlPath;
        if (email != null && password != null && userRole != null) {
            if (userRole.equals("student")) {
                fxmlPath = "/views/StudentHomePage.fxml";

            } else { // else admin
                fxmlPath = "/views/AdminHomePage.fxml";

            }
        } else {
            fxmlPath = "/views/SignPage.fxml";
        }

        return fxmlPath;
    }

    public static void logout(ActionEvent event) throws IOException, BackingStoreException {
        // Function responsible of logging the user out

        // remove the `loggedIn` (preference who remembers the logged in user)
        Preferences globalData = Preferences.userRoot();
        globalData.remove("loggedIn");
        globalData.remove("email");
        globalData.remove("password");
        globalData.remove("userRole");

        clearUserDataClass(); // clear the user data from the `UserData` class

        // after removing the `loggedIn`, return to the sign in page
        JavafxFunctions signInPage = new JavafxFunctions();
        signInPage.initializeStage(event, "../views/SignPage.fxml", "Sign in Page");
    }

    public static void clearUserDataClass() {
        // in any case we need to clear the user data from the `UserData` class, this
        // function will be used

        // clearing User class data
        UserData.userFirstName = "";
        UserData.userLastName = "";
        UserData.userEmail = "";
        UserData.userRole = "";
        UserData.studentGrade = "";
        UserData.studentClass = "";
        UserData.studentTeachers.clear();
    }
}
