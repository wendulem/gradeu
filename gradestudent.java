package gradebook;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class gradestudent {
	public static JFrame studentViewFrame;
	JPanel viewStudent = new JPanel();

	public gradestudent(String localFrameString, String currentButton, List<Double> gotType, List<Double> outType,
			List<String> typeType, List<String> nameType) {
		studentViewFrame = new JFrame(localFrameString);
		studentViewFrame.add(viewStudent);
		GridBagConstraints gbc = new GridBagConstraints();
		viewStudent.setLayout(new GridBagLayout());

//PRELIMINARY LABELS AND BACK BUTTON
		JLabel assignmentGrade = new JLabel("Grade");
		gbc.gridx = 3;
		gbc.gridy = 2;
		gbc.weightx = 1;
		gbc.weighty = 1;
		viewStudent.add(assignmentGrade, gbc);

		JLabel assignmentType = new JLabel("Score Fraction");
		gbc.gridx = 5;
		gbc.gridy = 2;
		gbc.weightx = 1;
		gbc.weighty = 1;
		viewStudent.add(assignmentType, gbc);

		JLabel percentage = new JLabel("Percentage");
		gbc.gridx = 7;
		gbc.gridy = 2;
		gbc.weightx = 1;
		gbc.weighty = 1;
		viewStudent.add(percentage, gbc);

		JButton backButton = new JButton("Back");
		gbc.gridx = 1;
		gbc.gridy = 2;
		gbc.weightx = 1;
		gbc.weighty = 1;
		backActionListener backAction = new backActionListener();
		backButton.addActionListener(backAction);
		viewStudent.add(backButton, gbc);

		int check = typeType.size();

		JLabel typeLabels[] = new JLabel[check];
		JLabel scoreLabels[] = new JLabel[check];
		JLabel percentLabels[] = new JLabel[check];

//FOR LOOP TO ESTABLISH GRADES
		for (int i = 0; i < check; i++) {
			typeLabels[i] = new JLabel(nameType.get(i));
			gbc.gridx = 3;
			gbc.gridy = 3 + i;
			gbc.weightx = 1;
			gbc.weighty = 1;
			viewStudent.add(typeLabels[i], gbc);

			scoreLabels[i] = new JLabel(gotType.get(i) + "/" + outType.get(i));
			gbc.gridx = 5;
			gbc.gridy = 3 + i;
			gbc.weightx = 1;
			gbc.weighty = 1;
			viewStudent.add(scoreLabels[i], gbc);

			double percentRound = (gotType.get(i) / outType.get(i)) * 100;
			String percentString = round(percentRound, 1) + "%";
			percentLabels[i] = new JLabel(percentString);
			gbc.gridx = 7;
			gbc.gridy = 3 + i;
			gbc.weightx = 1;
			gbc.weighty = 1;
			viewStudent.add(percentLabels[i], gbc);

		}
//FURTHER JFRAME FORMATTING		
		gbc.gridwidth = 1;
		studentViewFrame.pack();
		studentViewFrame.setSize(700, 500);
		studentViewFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public gradestudent() {

	}

	static double round(double value, int precision) {
		int scale = (int) Math.pow(10, precision);
		return (double) Math.round(value * scale) / scale;
	}

	class backActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ae) {
			// TODO Auto-generated method stub
			studentViewFrame.setVisible(false);
			gradeback.studentFrame.setVisible(true);
		}
	}

}
