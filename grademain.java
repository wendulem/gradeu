package gradebook;
import java.awt.event.*;
import javax.swing.*;
import static javax.swing.JOptionPane.showMessageDialog;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.ServerAddress;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Projections;
import com.mongodb.client.MongoCollection;

import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.Arrays;

import com.mongodb.BasicDBObject;
import com.mongodb.Block;

import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.client.MongoCursor;
import static com.mongodb.client.model.Filters.*;
import com.mongodb.client.result.DeleteResult;
import static com.mongodb.client.model.Updates.*;
import com.mongodb.client.result.UpdateResult;
import java.util.ArrayList;
import java.util.List;

import java.awt.Component;

public class grademain{
	private static JFrame userFrame;
	JPanel p = new JPanel();
	JButton b = new JButton("Submit");
	JTextField userField = new JTextField(12);

	static JPasswordField passField = new JPasswordField(12);
	private static String userValue;
	private static String passValue;
	static MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb+srv://wendulem:9og8op33ttkLkpYj@cluster0-ugj9q.mongodb.net/test?retryWrites=true"));
	static MongoDatabase database = mongoClient.getDatabase("GradeU");
	static MongoCollection<Document> profileCollection = database.getCollection("profiles");
	
public static void main(String[] args){
	new grademain();
	}
	 
public grademain() {
		userFrame = new JFrame("GradeU Login");
		userFrame.add(p);
		
		JLabel userLabel = new JLabel("Enter Username:");
		JLabel passLabel = new JLabel("Enter Password:");
		userLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		userField.setAlignmentX(Component.CENTER_ALIGNMENT);
		passLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		passField.setAlignmentX(Component.CENTER_ALIGNMENT);
		b.setAlignmentX(Component.CENTER_ALIGNMENT);
		userFrame.setResizable(true);
		userField.setMaximumSize(userField.getPreferredSize());
		passField.setMaximumSize(userField.getPreferredSize());
		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
		p.add(userLabel);
		p.add(userField);
		p.add(passLabel);
		p.add(passField);
		p.add(b);
		userFrame.pack();
		userFrame.setSize(400,300);
		userFrame.setVisible(true);
		userFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		inputActionListener passUserAction = new inputActionListener();
		b.addActionListener(passUserAction);
		passField.setEchoChar('*');
	}
	
public static void operatemain() {
	DBObject obj = new BasicDBObject();
	obj.put( "username", userValue);
	obj.put("password", passValue);
			Document userDocument = profileCollection
			.find((Bson) obj)
			.projection(Projections.fields(Projections.include("role","firstname","fullname"), Projections.excludeId())).first();
	
	String userRole = null;
	String nameOfUser = null;
	
	try {		
	userRole = userDocument.getString("role");
	nameOfUser = userDocument.getString("firstname");
	}catch(NullPointerException e) {
	showMessageDialog(null, "Please Enter a Valid Username or Password!");	
	}
	System.out.println(userRole);
	System.out.println(nameOfUser);
	
	if (userRole.equals("admin")) {
		userFrame.setVisible(false);
		gradeteacher teacherReference = new gradeteacher(database, profileCollection);
		teacherReference.setFrame();
		System.out.println("ADMIN ACCESS GRANTED");
	} else if (userRole.equals("student")) {
		userFrame.setVisible(false);
		gradeback.gradeCalculations(userValue, database);
		new gradeback(nameOfUser);
		gradeback.studentFrame.setVisible(true);
		System.out.println("STUDENT ACCESS GRANTED");
		} 
}
	
public class inputActionListener implements ActionListener {
		@Override
	public void actionPerformed(ActionEvent ae) {
		// TODO Auto-generated method stub
			userValue = userField.getText();	
			char[] password = passField.getPassword();
			passValue = new String(password);
			grademain.operatemain();
		}
	}
}



	

