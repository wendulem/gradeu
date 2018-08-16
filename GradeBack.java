/**
 * 
 *Creates JLabels and action listeners for each subject in the student view, triggering other calculations 
 * 
 **/

package gradebook;

import javax.swing.*;

import org.bson.Document;

import com.mongodb.client.MongoCollection;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

class GradeBack implements ConstantInterface {
   private JFrame student_frame;
   private GradeBack gradeback_reference;
   private ActionListener logout_action;
   private List<Integer> subject_weights;
   private List<String> subject_masters;
   private List<ActionListener> action_listeners;

   GradeBack(GradeMain grademain) {
      // Making the object a field so that it can be passed later and
      // not make another
      // instance necessary
      gradeback_reference = this;
      // Taking a string to be displayed at the top of the frame
      String nameofframe = grademain.getFrameString();
      student_frame = new JFrame(nameofframe);
      subject_masters = new ArrayList<>();
      action_listeners = new ArrayList<>();
      int length = SUBJECT_ARRAY.length;
      for (int i = 0; i < length; i++) {
         String currentsubject = SUBJECT_ARRAY[i];
         // Getting a specific collection based on the various subjects
         MongoCollection<Document> collection = DATABASE
               .getCollection(currentsubject);
         // Getting the weights of all the different constant subjects
         MongoOperations mongoaction = new MongoOperations();
         subject_weights = mongoaction.getSubjectWeights(collection);

         GradeCalc genericcalc = new GradeCalc(currentsubject,
               grademain, gradeback_reference);
         genericcalc.calculateNeeded();
         // Takes the subject and make it a presentable string for the
         // JLabels
         String usedsubject = genericcalc.getUsedSubject() + ":";
         String modifysubject = usedsubject.substring(0, 1)
               .toUpperCase() + usedsubject.substring(1).toLowerCase();
         subject_masters.add(
               modifysubject + " " + genericcalc.getSubjectSuper());
         ActionListener genericaction = genericcalc.getCurrentAction();
         action_listeners.add(genericaction);
         if (i == (length - 1)) {
            genericcalc.actionListeners("logout");
            logout_action = genericcalc.getCurrentAction();
         }
      }
      // Makes the main student panel instance and prepares it properly
      StudentMainPanel mainstudent = new StudentMainPanel(this);

      student_frame.add(mainstudent);
      student_frame.pack();
      student_frame.setSize(400, 300);
      student_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
   }

   ActionListener getLogoutAction() {
      return logout_action;
   }

   JFrame getStudentFrame() {
      return student_frame;
   }

   List<Integer> getSubjectWeights() {
      return subject_weights;
   }

   List<String> getSubjectMasters() {
      return subject_masters;
   }

   List<ActionListener> getActionListeners() {
      return action_listeners;
   }
} // End of class
