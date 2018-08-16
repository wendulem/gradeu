/**
 * 
 *Takes user input and authenticates it to instantiate other classes and show
 *the right JFrames and data
 * 
 **/

package gradebook;

import javax.swing.*;
import static javax.swing.JOptionPane.showMessageDialog;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

class GradeMain implements ConstantInterface {
   private JFrame user_frame;
   private LoginPanel login_panel;
   private GradeMain grade_main;
   private MongoOperations mongo_setup;
   private MongoCollection<Document> profile_collection;
   private String user_value;
   private String pass_value;
   private String name_of_user;
   private static final int USER_HEIGHT = 300;
   private static final int USER_WIDTH = 400;

   public static void main(String[] args) {
      @SuppressWarnings("unused")
      GradeMain grademain = new GradeMain();
   }

   private GradeMain() {
      // Establishing MongoClient Connection and getting down to the
      // authentication
      // collection
      mongo_setup = new MongoOperations();
      profile_collection = DATABASE.getCollection("profiles");
      grade_main = this;
      // Creating new instance of class that extends JPanel for
      // GradeMain and doing
      // swing operations
      login_panel = new LoginPanel(this);
      user_frame = new JFrame("GradeU Login");
      user_frame.add(login_panel);
      user_frame.setResizable(true);
      user_frame.pack();
      user_frame.setSize(USER_WIDTH, USER_HEIGHT);
      user_frame.setVisible(true);
      user_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      name_of_user = null;
   }

   @SuppressWarnings("unused")
   void operateMain(GradeMain grademain) {
      // This method comes from the JPanel action listeners and does the
      // authentication
      user_value = login_panel.getUserValue();
      pass_value = login_panel.getPassValue();
      DBObject obj = new BasicDBObject();
      obj.put("username", user_value);
      obj.put("password", pass_value);
      // Querying Mongo to authenticate the login based off user
      // credentials
      Document userdocument = mongo_setup
            .loginDetails(profile_collection, obj);
      String userrole = null;
      try {
         // If the input is valid it will check here to get the values
         // and process them
         userrole = userdocument.getString("role");
         name_of_user = userdocument.getString("firstname");
      } catch (NullPointerException e) {
         // Catches null pointer for an invalid input and prompts you to
         // retry
         showMessageDialog(null,
               "Please Enter a Valid Username or Password!");
      }
      // Checks here to process the user based on their role in the
      // database
      if (userrole.equals("admin")) {
         user_frame.setVisible(false);
         GradeTeacher teacherreference = new GradeTeacher(grademain);
         teacherreference.setFrame();
         System.out.println("ADMIN ACCESS GRANTED");
      } else if (userrole.equals("student")) {
         user_frame.setVisible(false);
         GradeBack gradereference = new GradeBack(grade_main);
         JFrame studentframe = gradereference.getStudentFrame();
         studentframe.setVisible(true);
         System.out.println("STUDENT ACCESS GRANTED");
      }
   }

   // Gets the frame to later be used in the logout buttons
   JFrame getUserFrame() {
      return user_frame;
   }

   // Gets the profile collection for later use
   MongoCollection<Document> getProfileCollection() {
      return profile_collection;
   }

   LoginPanel getLoginPanel() {
      return login_panel;
   }

   String getUserValue() {
      return user_value;
   }

   String getFrameString() {
      return "Welcome " + name_of_user;
   }
} // End of class
