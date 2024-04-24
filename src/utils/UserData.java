package utils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import org.json.JSONObject;
import org.json.JSONArray;

public class UserData {

    // After login, student data will be assigned to these class properties
    public static String _id;
    public static String userFirstName;
    public static String userLastName;
    public static String userEmail;
    public static String userRole;

    // Properties only for students
    public static String studentGrade;
    public static String studentClass;
    public static ArrayList<String> studentTeachers = new ArrayList<String>();

    public static void setUserData(JSONObject ObjResult) throws URISyntaxException, IOException, InterruptedException {
        // This function will set the class properties
        // ............

        // After getting the data, we assign it to the class properties
        _id = ObjResult.getString("_id");
        userFirstName = ObjResult.getString("userFirstName");
        userLastName = ObjResult.getString("userLastName");
        userEmail = ObjResult.getString("userEmail");
        userRole = ObjResult.getString("userRole");

        if (ObjResult.has("studentGrade") && ObjResult.has("studentClass") && ObjResult.has("teachers")) {
            // In case the logged in user is not a student (means admin) it will not have
            // the following properties

            studentGrade = ObjResult.getString("studentGrade");
            studentClass = ObjResult.getString("studentClass");

            JSONArray teachers = ObjResult.getJSONArray("teachers");

            // set the `studentTeachers` property (Array)
            teachers.forEach((teacher) -> {
                JSONObject ObjTeacher = (JSONObject) teacher;
                String teacher_id = ObjTeacher.getString("_ref");

                studentTeachers.add(teacher_id);
            });
        }
    }

    public static JSONObject getStudentData() {
        // in time we need to access the user data from this class, this function will
        // be called

        // ............

        // Return all the class properties (student data) in a json object
        JSONObject studentData = new JSONObject();

        studentData.put("userFirstName", userFirstName);
        studentData.put("userLastName", userLastName);
        studentData.put("userEmail", userEmail);
        studentData.put("userRole", userRole);
        studentData.put("studentGrade", studentGrade);
        studentData.put("studentClass", studentClass);
        studentData.put("studentTeachers", studentTeachers);
        return studentData;
    }

    // Get student's teacher data
    public static JSONObject getStudentTeachers(String teacher_id)
            throws URISyntaxException, IOException, InterruptedException {

        // This function will recive `the teacher_id` then gets all its data (teacher
        // name, rating stars for the user) then retun these data

        String query = "*[_id=='" + teacher_id + "']";
        JSONArray result = SanityClient.client(query); // query to the database
        JSONObject ObjResult = (JSONObject) result.get(0);

        String teacherFirstName = ObjResult.getString("teacherFirstName");
        String teacherLastName = ObjResult.getString("teacherLastName");
        JSONArray studentsRating = ObjResult.getJSONArray("studentsRating");
        int ratingStarsCount = 0;

        if (ObjResult.has("studentsRating")) {
            // Check first the teacher have rated students or not Get the value of the
            // `rating`

            for (int i = 0; i < studentsRating.length(); i++) {
                JSONArray ratedStudent = (JSONArray) ObjResult.getJSONArray("studentsRating");
                JSONObject ObjStudent = (JSONObject) ratedStudent.get(i); // the rated student
                JSONObject student = (JSONObject) ObjStudent.getJSONObject("student");
                String ratedStudent_id = student.getString("_ref");

                JSONObject studentRating = studentsRating.getJSONObject(i);

                if (ratedStudent_id.equals(_id)) {
                    // After getting all the rated stuedents that the teacher has rated...
                    // we only want to display the rating that belongs to the logged in student
                    // so here we compare if the student who the teacher has rated is the logged in
                    // student or not
                    // if so, then store the number of the rating stars

                    ratingStarsCount = studentRating.getInt("rating");
                }
            }
        }

        // and then return all the data in a json object
        JSONObject teacherData = new JSONObject();
        teacherData.put("teacherFirstName", teacherFirstName);
        teacherData.put("teacherLastName", teacherLastName);
        teacherData.put("ratingStarsCount", ratingStarsCount);

        return teacherData;
    }

}
