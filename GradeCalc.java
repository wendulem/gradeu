/**
 * 
 *Gathers score data and processes with weights to be used in GUI elements 
 * 
 **/

package gradebook;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;

class GradeCalc implements ConstantInterface {
   private MongoCollection<Document> call_collection;
   private List<String> score_name;
   private double subject_super;
   private JFrame student_frame;
   private GradeMain grade_main;
   private ActionListener current_action;
   private GradeBack gradeback_reference;
   private String local_frame_string;
   private LoginPanel login_panel;
   private String used_subject;
   private List<Integer> valid_parts_static;
   private String user_string;
   private static List<String> type_strings;
   static {
      type_strings = new ArrayList<>();
      type_strings.add("hw");
      type_strings.add("classwork");
      type_strings.add("quiz");
      type_strings.add("test");
   }

   GradeCalc(String subject, GradeMain grademain,
         GradeBack gradebackreference) {

      login_panel = grademain.getLoginPanel();
      local_frame_string = grademain.getFrameString();
      gradeback_reference = gradebackreference;
      grade_main = grademain;
      used_subject = subject;
      String uservalue = grademain.getUserValue();
      user_string = uservalue + subject;
      student_frame = gradebackreference.getStudentFrame();
      actionListeners(subject);
      valid_parts_static = gradebackreference.getSubjectWeights();
   }

   void calculateNeeded() {
      call_collection = DATABASE.getCollection(user_string);
      // References MongoOperations methods to get score data
      MongoOperations mongoscoredata = new MongoOperations();
      List<String> scoretype = mongoscoredata
            .calcScoreStrings(call_collection, "scoretype");
      score_name = mongoscoredata.calcScoreStrings(call_collection,
            "scorename");

      // Instance to handle all the various calculations needed for the
      // super values
      // and weighting
      GradeTypeCalc typecalc = new GradeTypeCalc(scoretype,
            call_collection, valid_parts_static, type_strings);
      // Array of the nonweighted supers for the current subject to be
      // used made final
      List<Double> supersnw = new ArrayList<>();
      for (int j = 0; j < type_strings.size(); j++) {
         double genericsuper = typecalc
               .processCalc(type_strings.get(j));
         supersnw.add(genericsuper);
      }

      Map<String, Integer> typemap = new HashMap<>();

      // This is all the data required to properly weight each gradetype
      // for a subject
      int subvalue = typecalc.getSubValue();
      typemap = typecalc.getTypeMap();

      // This carries out the weight calculations and makes occupies a
      // hashmap with
      // the weights and type
      int scalevalue = 100 - subvalue;
      Map<Integer, Double> scalemap = new HashMap<>();
      for (int i = 0; i < type_strings.size(); i++) {
         if (typemap.containsKey(type_strings.get(i))) {
            int incrementparts = typemap.get(type_strings.get(i));
            double incrementalter = incrementparts
                  * (100.0 / scalevalue);
            scalemap.put(incrementparts, incrementalter);
         }
      }
      // This uses the weights and sends the weighted supers to be
      // finalized
      List<Double> supersw = new ArrayList<>();
      for (int k = 0; k < supersnw.size(); k++) {
         if (scalemap.containsKey(typemap.get(type_strings.get(k)))) {
            supersw.add(supersnw.get(k)
                  * scalemap.get(typemap.get(type_strings.get(k))));
         }
      }
      finalCalcs(supersw);
   }

   private void finalCalcs(List<Double> supersw) {
      // This checks if the values are NaN so they can be set to zero
      for (int i = 0; i < supersw.size(); i++) {
         if (Double.isNaN(supersw.get(i))) {
            supersw.set(i, 0.0);
         }
      }
      // Gets the subject super based on these various supers and rounds
      // it off
      double subjectsuper = 0;
      for (int p = 0; p < supersw.size(); p++) {
         subjectsuper += supersw.get(p);
      }
      subject_super = ConstantInterface.roundValue(subjectsuper, 1);
   }

//This method is used within the class but also in another one to make the logout listener hence the if
   void actionListeners(String subject) {
      if (subject == "logout") {
         LogoutActionListener logoutaction = new LogoutActionListener();
         current_action = logoutaction;
      } else {
         SubjectActionListener subjectaction = new SubjectActionListener();
         current_action = subjectaction;
      }
   }

   // Below are the action listeners for the above method
   class SubjectActionListener implements ActionListener {
      @Override
      public void actionPerformed(ActionEvent ae) {
         // TODO Auto-generated method stub
         student_frame.setVisible(false);
         new GradeStudent(call_collection, score_name, grade_main,
               gradeback_reference);
         JFrame studentviewframe = GradeStudent.getViewFrame();
         studentviewframe.setVisible(true);
      }
   }

   class LogoutActionListener implements ActionListener {
      @Override
      public void actionPerformed(ActionEvent ae) {
         // TODO Auto-generated method stub
         student_frame.dispose();
         JFrame userframe = grade_main.getUserFrame();
         login_panel.operateFields();
         userframe.setVisible(true);
      }
   }

   double getSubjectSuper() {
      return subject_super;
   }

   ActionListener getCurrentAction() {
      return current_action;
   }

   String getUsedSubject() {
      return used_subject;
   }

   String getFrameString() {
      return local_frame_string;
   }
} // End of class