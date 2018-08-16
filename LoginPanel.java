/**
 * 
 *Sets up the JPanel for grademain and collects user input for authentication
 * 
 **/

package gradebook;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

@SuppressWarnings("serial")
class LoginPanel extends JPanel {

   private JPasswordField pass_field;
   private JTextField user_field;
   private String user_value;
   private String pass_value;
   private GradeMain grade_main;

   LoginPanel(GradeMain grademain) {
      grade_main = grademain;
      // This class is the JPanel for GradeMain and below is the swing
      // code
      setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
      JLabel userlabel = new JLabel("Enter Username:");
      userlabel.setAlignmentX(Component.CENTER_ALIGNMENT);
      add(userlabel);
      user_field = new JTextField(12);
      user_field.setAlignmentX(Component.CENTER_ALIGNMENT);
      user_field.setMaximumSize(user_field.getPreferredSize());
      add(user_field);
      JLabel passlabel = new JLabel("Enter Password:");
      passlabel.setAlignmentX(Component.CENTER_ALIGNMENT);
      add(passlabel);
      pass_field = new JPasswordField(12);
      pass_field.setAlignmentX(Component.CENTER_ALIGNMENT);
      pass_field.setMaximumSize(user_field.getPreferredSize());
      pass_field.setEchoChar('*');
      add(pass_field);
      // Action listener for submit which will being the authentication
      InputActionListener passuseraction = new InputActionListener();
      JButton submitbutton = new JButton("Submit");
      submitbutton.addActionListener(passuseraction);
      submitbutton.setAlignmentX(Component.CENTER_ALIGNMENT);
      add(submitbutton);
   }

   private class InputActionListener implements ActionListener {
      @Override
      public void actionPerformed(ActionEvent ae) {
         // TODO Auto-generated method stub
         user_value = user_field.getText();
         // Getting the values from user input to be used in
         // authentications once
         // submitted
         char[] password = pass_field.getPassword();
         pass_value = new String(password);
         grade_main.operateMain(grade_main);
      }
   }

   // Below resets the textfields once a user logs out or submits
   void operateFields() {
      user_field.setText("");
      pass_field.setText("");
   }

   // These get the user inputs to be used in the seperate grademain
   // class
   String getUserValue() {
      return user_value;
   }

   String getPassValue() {
      return pass_value;
   }

} // End of class
