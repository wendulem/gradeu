package gradebook;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JFrame;

import org.bson.Document;

import com.mongodb.client.MongoCollection;

/*
 * 
 *  Creates the student panel and the back action listener
 * 
 */

class GradeStudent {
	private static JFrame student_view_frame;
	private StudentPanel view_student;
	private GradeBack gradeback_reference;

	public GradeStudent(MongoCollection<Document> collection, List<String> nametype, GradeMain grademain,
			GradeBack gradebackreference) {

		gradeback_reference = gradebackreference;
		backActionListener backaction = new backActionListener();

		int check = nametype.size();
		// A JPanel is created and all the score data is fed in for it to be shown for
		// each subject
		view_student = new StudentPanel(check, backaction, collection);

//Below is extra formatting of the JFrame after the panel has an instance created		
		student_view_frame = new JFrame(grademain.getFrameString());
		student_view_frame.add(view_student);
		student_view_frame.pack();
		student_view_frame.setSize(700, 500);
		student_view_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public GradeStudent() {

	}

	// Action listener to go back to the main panel from external subject specific
	// ones
	class backActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ae) {
			// TODO Auto-generated method stub
			student_view_frame.setVisible(false);
			JFrame studentFrame = gradeback_reference.getStudentFrame();
			studentFrame.setVisible(true);
		}
	}

	// Gets the student frame to be reference when each view button is clicked
	static JFrame getViewFrame() {
		return student_view_frame;
	}
} //End of class
