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
      gbc.weightx = 0.5;
      gbc.weighty = 0.5;
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
      for (int i = 0; i < 4; i++) {
         gbc.gridx = 2 + i;
         gbc.gridy = 7;
         gbc.weightx = 1;
         gbc.weighty = 1;
         field_array.get(i).setToolTipText(tooltext.get(i));
         this.add(field_array.get(i), gbc);
         field_array.get(i)
               .setMinimumSize(field_array.get(i).getPreferredSize());
      }
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
         gbc.gridx = 6;
         gbc.weightx = 0.5;
         gbc.weighty = 0.5;
         if (i == 0) {
            gbc.gridy = i;
            this.add(button, gbc);
            button.addActionListener(actionlist.get(j));
         }
         if (i == 1) {
            gbc.gridy = 7;
            this.add(button, gbc);
            button.addActionListener(actionlist.get(j));
         }
      }

      // Sets the locations and actionlisteners for buttons of each of
      // the subjects
      // hardcoded in GradeMain
      for (int i = 0; i < 5; i++) {
         gbc.weightx = 0.5;
         gbc.weighty = 0.5;
         String buttonname = SUBJECT_ARRAY[i];
         buttonname = buttonname.substring(0, 1).toUpperCase()
               + buttonname.substring(1);
         JButton button = new JButton(buttonname);
         button.addActionListener(actionlist.get(i));
         if (i == 0) {
            gbc.gridx = 6;
            gbc.gridy = 1;
         }
         if (i > 0) {
            gbc.gridx = 2 + (i - 1);
            gbc.gridy = 1;
         }
         this.add(button, gbc);
         buttons_array.add(button);
      }
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
