/**
 * 
 *Fills the gui with score data, does additional data processing, and allows for data deletion and addition
 * 
 **/

package gradebook;

import java.awt.event.*;
import javax.swing.*;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

class GradeTeacher implements ConstantInterface {

   private JFrame teacher_view_frame;
   private GradeTeacherPanel view_teacher;
   private List<JLabel> type_labels;
   private List<JLabel> score_labels;
   private List<JLabel> percent_labels;
   private List<JButton> remove_buttons;
   private JTextField grade_out;
   private JTextField grade_total;
   private JTextField grade_name;
   private JTextField grade_type;
   private List<String> full_names_array;
   private List<String> username_array;
   private JComboBox<String> student_drop;
   private String current_grades;
   private int drop_selected;
   private Map<JButton, JLabel> button_cache;
   private GradeMain grade_main_object;
   private LoginPanel login_panel;
   private String query_user;
   private List<JButton> buttons_array;
   private JPanel student_data_panel;
   private JScrollPane scroll_pane;

   GradeTeacher(GradeMain grademain) {
      username_array = new ArrayList<>();
      full_names_array = new ArrayList<>();
      type_labels = new ArrayList<>();
      score_labels = new ArrayList<>();
      percent_labels = new ArrayList<>();
      remove_buttons = new ArrayList<>();
      button_cache = new HashMap<>();
      login_panel = grademain.getLoginPanel();
      student_data_panel = new JPanel();
      student_data_panel.setBackground(Color.WHITE);
      scroll_pane = new JScrollPane();
      grade_main_object = grademain;
      teacher_view_frame = new JFrame("Welcome Administrator");

      // Creates action listeners for each subject from the GradeMain
      // static list
      List<ActionListener> actionlist = new ArrayList<>();
      for (int i = 0; i < SUBJECT_ARRAY.length; i++) {
         String subject = SUBJECT_ARRAY[i];
         GenericActionListener actionlistener = new GenericActionListener(
               subject);
         actionlist.add(actionlistener);
      }
      AddActionListener addaction = new AddActionListener();
      LogoutActionListener logoutaction = new LogoutActionListener();
      actionlist.add(logoutaction);
      actionlist.add(addaction);
      // This runs mongo queries to get the name and username of the
      // various students
      getNames();
      // This takes this data to make arraylists and allow for the
      // dropbox dynamics to
      // be functional
      gradeSetupTeacher();
      view_teacher = new GradeTeacherPanel(actionlist, student_drop);
      // Gets the difference aspects from the teacher_view to use in the
      // adding of
      // user input to the DB
      grade_name = view_teacher.getNameBox();
      grade_type = view_teacher.getTypeBox();
      grade_total = view_teacher.getGotBox();
      grade_out = view_teacher.getOutBox();

      // Swing code to format the frame after the panel is instantiated
      // above
      buttons_array = view_teacher.getButtonsArray();
      scroll_pane.setPreferredSize(new Dimension(400, 70));
      scroll_pane.setMinimumSize(new Dimension(400, 70));
      scroll_pane.setMaximumSize(new Dimension(400, 70));
      scroll_pane.setViewportView(student_data_panel);
      scroll_pane.setHorizontalScrollBarPolicy(
            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
      scroll_pane.setVerticalScrollBarPolicy(
            ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
      scroll_pane.setAlignmentX(JComponent.CENTER_ALIGNMENT);
      GridBagConstraints gbc = new GridBagConstraints();
      gbc.gridx = 1;
      gbc.gridy = 2;
      view_teacher.add(scroll_pane, gbc);
      teacher_view_frame.add(view_teacher);
      teacher_view_frame.pack();
      teacher_view_frame.setMinimumSize(teacher_view_frame.getSize());
      teacher_view_frame.setSize(700, 500);
      teacher_view_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

   }

   void getNames() {
      full_names_array.add("None");
      MongoOperations studentmongo = new MongoOperations();
      MongoCollection<Document> profilecollection = grade_main_object
            .getProfileCollection();
      FindIterable<Document> studentnames = studentmongo
            .studentFullUser(profilecollection);
      for (Document studentnamesdoc : studentnames) {
         full_names_array.add(studentnamesdoc.getString("fullname"));
         username_array.add(studentnamesdoc.getString("username"));
      }
   }

   // Single use is to set the teacher view frame visible in whatever
   // scenario
   void setFrame() {
      teacher_view_frame.setVisible(true);
   }

   // As previously mentioned, this sets up the combobox with values
   public void gradeSetupTeacher() {
      int namesamount = full_names_array.size();
      String[] comboboxarray = full_names_array
            .toArray(new String[namesamount]);
      student_drop = new JComboBox<String>(comboboxarray);
      DropboxActionListener dropaction = new DropboxActionListener();
      student_drop.addActionListener(dropaction);
   }

   class RemoveActionListener implements ActionListener {
      @Override
      public void actionPerformed(ActionEvent ae) {
         // TODO Auto-generated method stub

         // Gets the source of the button and gets the index of the
         // button in the lists
         // so the corresponding JLabel is deleted
         JButton button = (JButton) ae.getSource();
         drop_selected = student_drop.getSelectedIndex();
         int usernameindex = drop_selected - 1;
         query_user = username_array.get(usernameindex);
         System.out.println(button_cache);
         System.out.println(button);
         JLabel scorenameofline = button_cache.get(button);
         String scorenameoflinestring = scorenameofline.getText();

         Document document = new Document();
         document.put("scorename", scorenameoflinestring);
         editCalcs(document, "delete");
         int indexused = Arrays.asList(SUBJECT_ARRAY)
               .indexOf(current_grades);
         student_data_panel.remove(button);
         buttons_array.get(indexused).doClick();
      }
   }

   /*
    * 
    * Filling the GUI with JLabels of current scoredata
    * 
    */

   void fillGrade(List<Double> gottype, List<Double> outtype,
         List<String> typetype, List<String> nametype) {

      // Clears the previous values so two different subjects don't
      // stack
      percent_labels.clear();
      type_labels.clear();
      score_labels.clear();
      remove_buttons.clear();

      student_data_panel.setLayout(new GridBagLayout());
      GridBagConstraints gbc = new GridBagConstraints();
      // Takes all the different data for each score document to make
      // labels in the
      // teacher view
      int check = (typetype.size() + gottype.size() + outtype.size()
            + nametype.size()) / 4;
      for (int i = 0; i < check; i++) {
         gbc.weightx = 1;
         gbc.weighty = 1;

         JLabel typelabel = new JLabel(nametype.get(i));
         type_labels.add(typelabel);
         gbc.gridx = 1;
         gbc.gridy = 2 + i;
         student_data_panel.add(type_labels.get(i), gbc);

         JLabel scorelabel = new JLabel(
               gottype.get(i) + "/" + outtype.get(i));
         score_labels.add(scorelabel);
         gbc.gridx = 2;
         gbc.gridy = 2 + i;
         student_data_panel.add(score_labels.get(i), gbc);

         double percentround = (gottype.get(i) / outtype.get(i)) * 100;
         String percentstring = ConstantInterface
               .roundValue(percentround, 1) + "%";

         JLabel percentlabel = new JLabel(percentstring);
         percent_labels.add(percentlabel);
         gbc.gridx = 3;
         gbc.gridy = 2 + i;
         student_data_panel.add(percent_labels.get(i), gbc);

         JButton removebutton = new JButton("Remove");
         RemoveActionListener removeaction = new RemoveActionListener();
         removebutton.addActionListener(removeaction);
         remove_buttons.add(removebutton);
         gbc.gridx = 4;
         gbc.gridy = 2 + i;
         student_data_panel.add(remove_buttons.get(i), gbc);

         // This sets a remove button and typelabel to be corresponding
         // for later
         // operations
         button_cache.put(remove_buttons.get(i), type_labels.get(i));
      }
      student_data_panel.validate();
   }

//This takes subjects as an arguement to show score data depending on the subject
   class GenericActionListener implements ActionListener {
      private String current_subject;

      public GenericActionListener(String subject) {
         current_subject = subject;
      }

      @Override
      public void actionPerformed(ActionEvent ae) {
         // TODO Auto-generated method stub
         int labellengths = (percent_labels.size());
         for (int i = 0; i < labellengths; i++) {
            student_data_panel.remove(percent_labels.get(i));
            student_data_panel.remove(type_labels.get(i));
            student_data_panel.remove(score_labels.get(i));
            student_data_panel.remove(remove_buttons.get(i));
         }
         current_grades = current_subject;
         GradeTeacherCalc subjectcalc = new GradeTeacherCalc(query_user,
               current_subject);
         List<Double> scoregot = subjectcalc.getScoreGot();
         List<Double> scoreout = subjectcalc.getScoreOut();
         List<String> scorename = subjectcalc.getScoreName();
         List<String> scoretype = subjectcalc.getScoreType();
         fillGrade(scoregot, scoreout, scoretype, scorename);
         student_data_panel.revalidate();
         student_data_panel.repaint();
      }
   }

   // Gives the user that the score data should be taken from based on a
   // combobox
   class DropboxActionListener implements ActionListener {
      @Override
      public void actionPerformed(ActionEvent ae) {
         // TODO Auto-generated method stub
         drop_selected = student_drop.getSelectedIndex();
         int usernameIndex = drop_selected - 1;
         query_user = username_array.get(usernameIndex);
      }
   }

   class AddActionListener implements ActionListener {
      @Override
      public void actionPerformed(ActionEvent ae) {
         // TODO Auto-generated method stub
         collectionAlterations();
         // Resets all the boxes after the input creates a document in
         // the database
         grade_out.setText("");
         grade_total.setText("");
         grade_name.setText("");
         grade_type.setText("");
      }
   }

   // Resets everything to be set to the default login screen
   class LogoutActionListener implements ActionListener {
      @Override
      public void actionPerformed(ActionEvent ae) {
         // TODO Auto-generated method stub
         teacher_view_frame.dispose();
         JFrame userFrame = grade_main_object.getUserFrame();
         login_panel.operateFields();
         userFrame.setVisible(true);
      }
   }

   // Adds the values from the input boxes to the database
   private void collectionAlterations() {
      String assignmentname = grade_name.getText();
      double assignmentgot = Double.parseDouble(grade_total.getText());
      double assignmentout = Double.parseDouble(grade_out.getText());
      String assignmenttype = grade_type.getText();

      Document additiondoc = new Document("scorename", assignmentname)
            .append("scoreout", assignmentout)
            .append("scoregot", assignmentgot)
            .append("scoretype", assignmenttype);

      drop_selected = student_drop.getSelectedIndex();
      int usernameIndex = drop_selected - 1;
      query_user = username_array.get(usernameIndex);

      editCalcs(additiondoc, "insert");
      int indexused = Arrays.asList(SUBJECT_ARRAY)
            .indexOf(current_grades);
      buttons_array.get(indexused).doClick();
   }

   // Specific deleting and insert methods to be used in each action
   // listener

   private void editCalcs(Document doc, String action) {
      GradeTeacherCalc subjectcalc = new GradeTeacherCalc(query_user,
            current_grades);
      MongoCollection<Document> collection = subjectcalc
            .getCollectionName();
      if (action == "delete") {
         collection.deleteOne(doc);
      }
      if (action == "insert") {
         collection.insertOne(doc);
      }
   }
} // End of class
