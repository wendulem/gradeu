/**
 * 
 * Creates the JPanel from which all the additional score data can be viewed by students
 *  depending on the subject view button pressed
 * 
 **/

package gradebook;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.bson.Document;

import com.mongodb.client.MongoCollection;

import gradebook.GradeStudent.BackActionListener;

@SuppressWarnings("serial")
class StudentPanel extends JPanel {
   private JLabel percent_labels[];
   private JLabel type_labels[];
   private JLabel score_labels[];
   private int check_value;
   private GridBagConstraints gbc_field;
   private MongoOperations mongo_type_data;
   private MongoCollection<Document> call_collection;

   StudentPanel(int check, BackActionListener backaction,
         MongoCollection<Document> collection) {
      // Below is all the score data that is used to occupy the area
      // from each view
      // button
      mongo_type_data = new MongoOperations();
      call_collection = collection;
      check_value = check;
      GridBagConstraints gbc = new GridBagConstraints();
      gbc.weightx = 1;
      gbc.weighty = 0;
      gbc_field = gbc;
      this.setLayout(new GridBagLayout());
      JLabel assignmentgrade = new JLabel("Grade");
      gbc.gridx = 3;
      gbc.gridy = 2;
      this.add(assignmentgrade, gbc);

      JLabel assignmenttype = new JLabel("Score Fraction");
      gbc.gridx = 5;
      gbc.gridy = 2;
      this.add(assignmenttype, gbc);

      JLabel percentage = new JLabel("Percentage");
      gbc.gridx = 7;
      gbc.gridy = 2;
      this.add(percentage, gbc);

      JButton backbutton = new JButton("Back");
      gbc.gridx = 1;
      gbc.gridy = 2;
      backbutton.addActionListener(backaction);
      this.add(backbutton, gbc);

      type_labels = new JLabel[check];
      score_labels = new JLabel[check];
      percent_labels = new JLabel[check];
      gradesFill();
   }

   private void gradesFill() {
      List<Double> gottype = mongo_type_data
            .calcScoreDoubles(call_collection, "scoregot");
      List<Double> outtype = mongo_type_data
            .calcScoreDoubles(call_collection, "scoreout");
      List<String> nametype = mongo_type_data
            .calcScoreStrings(call_collection, "scorename");
      // This method occupies JLabel areas for this afformentioned data
      for (int i = 0; i < check_value; i++) {
         type_labels[i] = new JLabel(nametype.get(i));
         gbc_field.gridx = 3;
         gbc_field.gridy = 3 + i;
         this.add(type_labels[i], gbc_field);

         score_labels[i] = new JLabel(
               gottype.get(i) + "/" + outtype.get(i));
         gbc_field.gridx = 5;
         gbc_field.gridy = 3 + i;
         this.add(score_labels[i], gbc_field);

         double percentround = (gottype.get(i) / outtype.get(i)) * 100;
         String percentstring = ConstantInterface
               .roundValue(percentround, 1) + "%";
         percent_labels[i] = new JLabel(percentstring);
         gbc_field.gridx = 7;
         gbc_field.gridy = 3 + i;
         this.add(percent_labels[i], gbc_field);
      }
   }
} // End of class
