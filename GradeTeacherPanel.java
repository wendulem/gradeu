/**
 * 
 *Adds the GUI elements to the teacher panel which includes input fields and elements to display additional data 
 * 
 **/

package gradebook;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

@SuppressWarnings("serial")
class GradeTeacherPanel extends JPanel implements ConstantInterface {
   List<JButton> buttons_array;
   List<JTextField> field_array;

   GradeTeacherPanel(List<ActionListener> actionlist,
         JComboBox<String> studentdrop) {
      GridBagConstraints gbc = new GridBagConstraints();
      this.setLayout(new GridBagLayout());
      gbc.gridx = 0;
      gbc.gridy = 0;
      gbc.weightx = 0;
      gbc.weighty = 0;
      // Adds the student combobox that was passed as a arugument to
      // reduce data
      // arguments
      this.add(studentdrop, gbc);

      field_array = new ArrayList<>();
      List<String> tooltext = new ArrayList<>();
      // Occupies as tool tip list to be used in an indexing loop with
      // the different
      // boxes they're used with
      tooltext.add("Enter the name of the assignment.");
      tooltext.add(
            "Enter the points the student received for the assignment.");
      tooltext.add(
            "Enter the total points available for the assignment.");
      tooltext.add("Enter the type of grade the assignment is,"
            + " in the format of either: quiz,hw,classwork,test.");
      for (int i = 0; i < 4; i++) {
         JTextField field = new JTextField(10);
         field_array.add(field);
      }
      // Makes the various fields from the field array specific and sets
      // their tool
      // tips
      JPanel addboxespanel = new JPanel();
      addboxespanel
            .setLayout(new BoxLayout(addboxespanel, BoxLayout.X_AXIS));
      for (int i = 0; i < 4; i++) {
         field_array.get(i).setToolTipText(tooltext.get(i));
         addboxespanel.add(field_array.get(i));
         field_array.get(i)
               .setMinimumSize(field_array.get(i).getPreferredSize());
      }
      gbc.gridx = 1;
      gbc.gridy = 3;
      this.add(addboxespanel, gbc);
      // Sets the location and action listeners for the logout and add
      // specialty
      // buttons
      buttons_array = new ArrayList<>();
      List<String> buttonnames = new ArrayList<>();
      buttonnames.add("Logout");
      buttonnames.add("Add");
      for (int i = 0; i < 2; i++) {
         JButton button = new JButton(buttonnames.get(i));
         int j = 5 + i;
         gbc.gridx = 3;
         gbc.weightx = 0;
         gbc.weighty = 0;
         if (i == 0) {
            gbc.gridy = i;
            this.add(button, gbc);
            button.addActionListener(actionlist.get(j));
         }
         if (i == 1) {
            gbc.gridy = 3;
            this.add(button, gbc);
            button.addActionListener(actionlist.get(j));
         }
      }

      // Sets the locations and actionlisteners for buttons of each of
      // the subjects
      // hardcoded in GradeMain
      JPanel buttonpanel = new JPanel();
      buttonpanel
            .setLayout(new BoxLayout(buttonpanel, BoxLayout.X_AXIS));
      for (int i = 0; i < 5; i++) {
         String buttonname = SUBJECT_ARRAY[i];
         buttonname = buttonname.substring(0, 1).toUpperCase()
               + buttonname.substring(1);
         JButton button = new JButton(buttonname);
         button.addActionListener(actionlist.get(i));
         buttonpanel.add(button);
         buttons_array.add(button);
      }
      gbc.gridx = 1;
      gbc.gridy = 1;
      this.add(buttonpanel, gbc);
   }

   // Gets the buttons and boxes to be used in operations such as the
   // .onClick() to
   // refresh grades
   List<JButton> getButtonsArray() {
      return buttons_array;
   }

   JTextField getNameBox() {
      return field_array.get(0);
   }

   JTextField getGotBox() {
      return field_array.get(1);
   }

   JTextField getOutBox() {
      return field_array.get(2);
   }

   JTextField getTypeBox() {
      return field_array.get(3);
   }
} // End of the class
