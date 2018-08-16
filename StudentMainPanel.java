/**
 * 
 * Gets the subject masters and various elements to create the main student panel
 *  from which additional data can be accessed 
 * 
 **/

package gradebook;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
class StudentMainPanel extends JPanel {
   StudentMainPanel(GradeBack gradeback) {
      // JPanel class for GradeBack and for the main student panel and
      // below are the
      // Labels with the supers
      List<ActionListener> actionlisteners = gradeback
            .getActionListeners();
      List<String> subjectmasters = gradeback.getSubjectMasters();
      ActionListener logoutaction = gradeback.getLogoutAction();
      GridBagConstraints gbc = new GridBagConstraints();
      this.setLayout(new GridBagLayout());
      gbc.weightx = 1;
      gbc.weighty = 1;
      StudentMainPanel studentpanel = this;
      for (int i = 0; i < 5; i++) {
         if (i < 3) {
            JLabel label = new JLabel(subjectmasters.get(i));
            gbc.gridx = 0;
            gbc.gridy = 1 + i;
            studentpanel.add(label, gbc);
         }
         if (i > 2) {
            JLabel label = new JLabel(subjectmasters.get(i));
            gbc.gridx = 3;
            gbc.gridy = i - 2;
            studentpanel.add(label, gbc);
         }
      }
      // Below, the logout and view (detailed info on subject)buttons
      // get their action
      // listeners and are added
      List<ActionListener> actionnames = new ArrayList<>();
      List<JButton> viewbuttons = new ArrayList<>();

      for (int i = 0; i < actionlisteners.size(); i++) {
         actionnames.add(actionlisteners.get(i));
         JButton subjectbutton = new JButton("View");
         viewbuttons.add(subjectbutton);
      }
      actionnames.add(logoutaction);
      JButton logoutbutton = new JButton("Logout");
      viewbuttons.add(logoutbutton);

      // View buttons are added next to each super so they need to have
      // their specific
      // actions and locations below
      for (int i = 0; i < 6; i++) {
         JButton currentbutton = viewbuttons.get(i);
         currentbutton.addActionListener(actionnames.get(i));
         if (i < 3) {
            gbc.gridx = 1;
            gbc.gridy = 1 + i;
            this.add(currentbutton, gbc);
         }
         if (i > 2) {
            gbc.gridx = 4;
            gbc.gridy = i - 2;
            this.add(currentbutton, gbc);
         }
      }
   }
} // End of class