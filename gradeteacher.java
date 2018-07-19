package gradebook;

import java.awt.event.*;
import javax.swing.*;
import static javax.swing.JOptionPane.showMessageDialog;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.ServerAddress;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Projections;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;

import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.Arrays;

import com.mongodb.BasicDBObject;
import com.mongodb.Block;

import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.client.MongoCursor;
import static com.mongodb.client.model.Filters.*;
import com.mongodb.client.result.DeleteResult;
import static com.mongodb.client.model.Updates.*;
import com.mongodb.client.result.UpdateResult;

import java.util.ArrayList;
import java.util.List;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import com.mongodb.client.MongoCollection;
import static com.mongodb.client.model.Projections.*;

public class gradeteacher  {
	
	private JFrame teacherViewFrame;
	private JPanel viewTeacher;
	private int arrayValueCap = 4;
	//(gradeback.spanishScoreType.size() + gradeback.scienceScoreType.size() + gradeback.mathScoreType.size() +
	//gradeback.englishScoreType.size() + gradeback.historyScoreType.size())/5 + 1;
	private JLabel typeLabels[] = new JLabel[arrayValueCap];
	private JLabel scoreLabels[] = new JLabel[arrayValueCap];
	private JLabel percentLabels[] = new JLabel[arrayValueCap];
	JTextField gradeOut;
	JTextField gradeTotal;
	JTextField gradeName;
	JButton submitTeacherInput;
	
	private ArrayList<String> fullNamesArray = new ArrayList<String>();
	//static JPanel[] panels = new JPanel[4];
	private JComboBox<String> studentDrop;
	private String usernameOfSelected;
	private String currentGrades;
	
	private List<Double> mathScoreGot;
	private List<Double> mathScoreOut;
	private List<String> mathScoreType;
	private List<String> mathScoreName;
	private List<Double> scienceScoreGot;
	private List<Double> scienceScoreOut;
	private List<String> scienceScoreType;
	private List<String> scienceScoreName;
	private List<Double> historyScoreGot;
	private List<Double> historyScoreOut;
	private List<String> historyScoreType;
	private List<String> historyScoreName;
	private List<Double> englishScoreGot;
	private List<Double> englishScoreOut;
	private List<String> englishScoreType;
	private List<String> englishScoreName;
	private List<Double> spanishScoreGot;
	private List<Double> spanishScoreOut;
	private List<String> spanishScoreType;
	private List<String> spanishScoreName;
	private MongoCollection <Document> profileCollection;
	
	private int dropSelected;
	private MongoDatabase databaseNew;
	
	gradeteacher(MongoDatabase database, MongoCollection<Document> profileCollection) {
	this.profileCollection = profileCollection;
	databaseNew = database;
	viewTeacher = new JPanel();	
	teacherViewFrame = new JFrame("Welcome Administrator");
	GridBagConstraints gbc = new GridBagConstraints();
	viewTeacher.setLayout(new GridBagLayout());
	
	getNames();
	gradeSetupTeacher();
	
	gbc.gridx = 0;
	gbc.gridy = 0;
	gbc.weightx = 0.5;
	gbc.weighty = 0.5;
	viewTeacher.add(studentDrop, gbc);
	
	gradeOut = new JTextField(10);
	gbc.gridx = 5;
	gbc.gridy = 6;
	gbc.weightx = 1;
	gbc.weighty = 1;
	viewTeacher.add(gradeOut, gbc);
	
	gradeTotal = new JTextField(10);
	gbc.gridx = 4;
	gbc.gridy = 6;
	gbc.weightx = 1;
	gbc.weighty = 1;
	viewTeacher.add(gradeTotal, gbc);
	
	gradeName = new JTextField(10);
	gbc.gridx = 3;
	gbc.gridy = 6;
	gbc.weightx = 1;
	gbc.weighty = 1;
	viewTeacher.add(gradeName, gbc);
	
	submitTeacherInput = new JButton("Add");
	gbc.gridx = 6;
	gbc.gridy = 6;
	gbc.weightx = 0.5;
	gbc.weighty = 0.5;
	viewTeacher.add(submitTeacherInput, gbc);

	JButton studentEnglish = new JButton("English");
	gbc.gridx = 2;
	gbc.gridy = 1;
	gbc.weightx = 0.5;
	gbc.weighty = 0.5;
	viewTeacher.add(studentEnglish, gbc);
	
	JButton studentHistory = new JButton("History");
	gbc.gridx = 3;
	gbc.gridy = 1;
	gbc.weightx = 0.5;
	gbc.weighty = 0.5;
	viewTeacher.add(studentHistory, gbc);
	
	JButton studentScience = new JButton("Science");
	gbc.gridx = 4;
	gbc.gridy = 1;
	gbc.weightx = 0.5;
	gbc.weighty = 0.5;
	viewTeacher.add(studentScience, gbc);
	
	JButton studentMath = new JButton("Math");
	gbc.gridx = 5;
	gbc.gridy = 1;
	gbc.weightx = 0.5;
	gbc.weighty = 0.5;
	viewTeacher.add(studentMath, gbc);
	
	JButton studentSpanish = new JButton("Spanish");
	gbc.gridx = 6;
	gbc.gridy = 1;
	gbc.weightx = 0.5;
	gbc.weighty = 0.5;
	viewTeacher.add(studentSpanish, gbc);
		
	math2ActionListener mathAction = new math2ActionListener();
	studentMath.addActionListener(mathAction);
	
	english2ActionListener englishAction = new english2ActionListener();
	studentEnglish.addActionListener(englishAction);
	
	science2ActionListener scienceAction = new science2ActionListener();
	studentScience.addActionListener(scienceAction);
	
	spanish2ActionListener spanishAction = new spanish2ActionListener();
	studentSpanish.addActionListener(spanishAction);
	
	history2ActionListener historyAction = new history2ActionListener();
	studentHistory.addActionListener(historyAction);
	
	teacherViewFrame.add(viewTeacher);
	teacherViewFrame.pack();
	teacherViewFrame.setSize(700,500);
	teacherViewFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
}
	
	void getNames() {
	FindIterable<Document> studentNames = grademain.profileCollection.find(eq("role", "student")).projection(include("fullname"));
	for (Document studentNamesDoc : studentNames) {
	      fullNamesArray.add(studentNamesDoc.getString("fullname"));

	}
}
	void setFrame() {
		teacherViewFrame.setVisible(true);
	}

double round (double value, int precision) {
    int scale = (int) Math.pow(10, precision);
    return (double) Math.round(value * scale) / scale;
}

public void gradeSetupTeacher() {
	int namesAmount = fullNamesArray.size();
	String [] comboboxArray = fullNamesArray.toArray(new String[namesAmount]);
	studentDrop = new JComboBox<String>(comboboxArray);
	
	dropboxActionListener dropAction = new dropboxActionListener();
	
	studentDrop.addActionListener(dropAction);
	
	
}

public void gradeCalculatingTeacher(String queryName) {
	BasicDBObject userQuery = new BasicDBObject();
	
	FindIterable<Document> usernameDocument;
	userQuery.put("fullname",queryName);
	Document usernameDoc = new Document();
	usernameDoc.put(queryName, userQuery);
	usernameDocument = profileCollection.find(eq(userQuery));
			for (Document additionalUsernames  : usernameDocument) {
				usernameOfSelected = additionalUsernames.getString("username");
			}
	//ENGLISH and MATH and HISTORY and SCIENCE making the strings for the appropriate collection
		String englishString = usernameOfSelected + "english";
		String mathString = usernameOfSelected + "math";
		String historyString = usernameOfSelected + "history";
		String scienceString = usernameOfSelected + "science";
		String spanishString = usernameOfSelected + "spanish";
		
	//ENGLISH and MATH and HISTORY and SCIENCE naming a native collection after a collection within the database
		MongoCollection<Document> englishCollection = databaseNew
				.getCollection(englishString);
		MongoCollection<Document> mathCollection = databaseNew
				.getCollection(mathString);
		MongoCollection<Document> historyCollection = databaseNew
				.getCollection(historyString);
		MongoCollection<Document> scienceCollection = databaseNew
				.getCollection(scienceString);
		MongoCollection<Document> spanishCollection = databaseNew
				.getCollection(spanishString);
		
	/*ENGLISH creating 3 different list arrays for the type of each score, the score the person got for each
	score and the total they could have gotten*/
		englishScoreType = new ArrayList<>(); 
		englishCollection.find()
		.projection(Projections.include("scoretype")).forEach((Block<Document>) d -> { englishScoreType.add(d.getString("scoretype")); });
		englishScoreOut = new ArrayList<>(); 
		englishCollection.find()
		.projection(Projections.include("scoreout")).forEach((Block<Document>) d -> { englishScoreOut.add(d.getDouble("scoreout")); });
		englishScoreGot = new ArrayList<>(); 
		englishCollection.find()
		.projection(Projections.include("scoregot")).forEach((Block<Document>) d -> { englishScoreGot.add(d.getDouble("scoregot")); });
		englishScoreName = new ArrayList<>(); 
		englishCollection.find()
		.projection(Projections.include("scorename")).forEach((Block<Document>) d -> { englishScoreName.add(d.getString("scorename")); });
		
	/*MATH creating 3 different list arrays for the type of each score, the score the person got for each
	score and the total they could have gotten*/
		mathScoreType = new ArrayList<>(); 
		mathCollection.find()
		.projection(Projections.include("scoretype")).forEach((Block<Document>) d -> { mathScoreType.add(d.getString("scoretype")); });
		mathScoreOut = new ArrayList<>(); 
		mathCollection.find()
		.projection(Projections.include("scoreout")).forEach((Block<Document>) d -> { mathScoreOut.add(d.getDouble("scoreout")); });
		mathScoreGot = new ArrayList<>(); 
		mathCollection.find()
		.projection(Projections.include("scoregot")).forEach((Block<Document>) d -> { mathScoreGot.add(d.getDouble("scoregot")); });
		mathScoreName = new ArrayList<>(); 
		mathCollection.find()
		.projection(Projections.include("scorename")).forEach((Block<Document>) d -> { mathScoreName.add(d.getString("scorename")); });
		
	/*HISTORY creating 3 different list arrays for the type of each score, the score the person got for each
	score and the total they could have gotten*/
		historyScoreType = new ArrayList<>(); 
		historyCollection.find()
		.projection(Projections.include("scoretype")).forEach((Block<Document>) d -> { historyScoreType.add(d.getString("scoretype")); });
		historyScoreOut = new ArrayList<>(); 
		historyCollection.find()
		.projection(Projections.include("scoreout")).forEach((Block<Document>) d -> { historyScoreOut.add(d.getDouble("scoreout")); });
		historyScoreGot = new ArrayList<>(); 
		historyCollection.find()
		.projection(Projections.include("scoregot")).forEach((Block<Document>) d -> { historyScoreGot.add(d.getDouble("scoregot")); });
		historyScoreName = new ArrayList<>(); 
		historyCollection.find()
		.projection(Projections.include("scorename")).forEach((Block<Document>) d -> { historyScoreName.add(d.getString("scorename")); });
			
	/*SCIENCE creating 3 different list arrays for the type of each score, the score the person got for each
	score and the total they could have gotten*/
		scienceScoreType = new ArrayList<>(); 
		scienceCollection.find()
		.projection(Projections.include("scoretype")).forEach((Block<Document>) d -> { scienceScoreType.add(d.getString("scoretype")); });
		scienceScoreOut = new ArrayList<>(); 
		scienceCollection.find()
		.projection(Projections.include("scoreout")).forEach((Block<Document>) d -> { scienceScoreOut.add(d.getDouble("scoreout")); });
		scienceScoreGot = new ArrayList<>(); 
		scienceCollection.find()
		.projection(Projections.include("scoregot")).forEach((Block<Document>) d -> { scienceScoreGot.add(d.getDouble("scoregot")); });	
		scienceScoreName = new ArrayList<>(); 
		scienceCollection.find()
		.projection(Projections.include("scorename")).forEach((Block<Document>) d -> { scienceScoreName.add(d.getString("scorename")); });
		
	/*SPANISH creating 3 different list arrays for the type of each score, the score the person got for each
	score and the total they could have gotten*/
		spanishScoreType = new ArrayList<>(); 
		spanishCollection.find()
		.projection(Projections.include("scoretype")).forEach((Block<Document>) d -> { spanishScoreType.add(d.getString("scoretype")); });
		spanishScoreOut = new ArrayList<>(); 
		spanishCollection.find()
		.projection(Projections.include("scoreout")).forEach((Block<Document>) d -> { spanishScoreOut.add(d.getDouble("scoreout")); });
		spanishScoreGot = new ArrayList<>(); 
		spanishCollection.find()
		.projection(Projections.include("scoregot")).forEach((Block<Document>) d -> { spanishScoreGot.add(d.getDouble("scoregot")); });		
		spanishScoreName = new ArrayList<>(); 
		spanishCollection.find()
		.projection(Projections.include("scorename")).forEach((Block<Document>) d -> { spanishScoreName.add(d.getString("scorename")); });


		
//ENGLISH creating the arrays for all the different types of grade data needed
	ArrayList<Double> englishHWOut = new ArrayList<Double>(arrayValueCap);
	ArrayList<Double> englishCWOut = new ArrayList<Double>(arrayValueCap);
	ArrayList<Double> englishTOut = new ArrayList<Double>(arrayValueCap);
	ArrayList<Double> englishQOut = new ArrayList<Double>(arrayValueCap);
	ArrayList<Double> englishHWGot = new ArrayList<Double>(arrayValueCap);
	ArrayList<Double> englishCWGot = new ArrayList<Double>(arrayValueCap);
	ArrayList<Double> englishTGot = new ArrayList<Double>(arrayValueCap);
	ArrayList<Double> englishQGot = new ArrayList<Double>(arrayValueCap);

//ENGLISH creating the for loop that assigns the values to the corresponding list array based on the previous 3
	for (int i=0 ; i < arrayValueCap ; i++) {
		String assignmentType = englishScoreType.get(i);
		
		switch (assignmentType) {
			case "quiz" :
			englishQGot.add(englishScoreGot.get(i));
			englishQOut.add(englishScoreOut.get(i));
			break;
			case "test" :
			englishTGot.add(englishScoreGot.get(i));
			englishTOut.add(englishScoreOut.get(i));
			break;
			case "hw" :
			englishHWGot.add(englishScoreGot.get(i));
			englishHWOut.add(englishScoreOut.get(i));
			break;
			case "classwork" :
			englishCWGot.add(englishScoreGot.get(i));
			englishCWOut.add(englishScoreOut.get(i));
			break;
			
		}
	}

//MATH creating the arrays for all the different types of grade data needed
	ArrayList<Double> mathHWOut = new ArrayList<Double>(arrayValueCap);
	ArrayList<Double> mathCWOut = new ArrayList<Double>(arrayValueCap);
	ArrayList<Double> mathTOut = new ArrayList<Double>(arrayValueCap);
	ArrayList<Double> mathQOut = new ArrayList<Double>(arrayValueCap);
	ArrayList<Double> mathHWGot = new ArrayList<Double>(arrayValueCap);
	ArrayList<Double> mathCWGot = new ArrayList<Double>(arrayValueCap);
	ArrayList<Double> mathTGot = new ArrayList<Double>(arrayValueCap);
	ArrayList<Double> mathQGot = new ArrayList<Double>(arrayValueCap);

//MATH creating the for loop that assigns the values to the corresponding list array based on the type and out/got 3
	for (int i=0 ; i < arrayValueCap ; i++) {
		String assignmentType = mathScoreType.get(i);
		
		switch (assignmentType) {
			case "quiz" :
			mathQGot.add(mathScoreGot.get(i));
			mathQOut.add(mathScoreOut.get(i));
			break;
			case "test" :
			mathTGot.add(mathScoreGot.get(i));
			mathTOut.add(mathScoreOut.get(i));
			break;
			case "hw" :
			mathHWGot.add(mathScoreGot.get(i));
			mathHWOut.add(mathScoreOut.get(i));
			break;
			case "classwork" :
			mathCWGot.add(mathScoreGot.get(i));
			mathCWOut.add(mathScoreOut.get(i));
			break;
			
		}
	}
	
//HISTORY creating the arrays for all the different types of grade data needed
		ArrayList<Double> historyHWOut = new ArrayList<Double>(arrayValueCap);
		ArrayList<Double> historyCWOut = new ArrayList<Double>(arrayValueCap);
		ArrayList<Double> historyTOut = new ArrayList<Double>(arrayValueCap);
		ArrayList<Double> historyQOut = new ArrayList<Double>(arrayValueCap);
		ArrayList<Double> historyHWGot = new ArrayList<Double>(arrayValueCap);
		ArrayList<Double> historyCWGot = new ArrayList<Double>(arrayValueCap);
		ArrayList<Double> historyTGot = new ArrayList<Double>(arrayValueCap);
		ArrayList<Double> historyQGot = new ArrayList<Double>(arrayValueCap);

//HISTORY creating the for loop that assigns the values to the corresponding list array based on the type and out/got 3
		for (int i=0 ; i < arrayValueCap ; i++) {
			String assignmentType = historyScoreType.get(i);
			
			switch (assignmentType) {
			case "quiz" :
			historyQGot.add(historyScoreGot.get(i));
			historyQOut.add(historyScoreOut.get(i));
			break;
			case "test" :
			historyTGot.add(historyScoreGot.get(i));
			historyTOut.add(historyScoreOut.get(i));
			break;
			case "hw" :
			historyHWGot.add(historyScoreGot.get(i));
			historyHWOut.add(historyScoreOut.get(i));
			break;
			case "classwork" :
			historyCWGot.add(historyScoreGot.get(i));
			historyCWOut.add(historyScoreOut.get(i));
			break;
				
			}
		}
	
//SCIENCE creating the arrays for all the different types of grade data needed
		ArrayList<Double> scienceHWOut = new ArrayList<Double>(arrayValueCap);
		ArrayList<Double> scienceCWOut = new ArrayList<Double>(arrayValueCap);
		ArrayList<Double> scienceTOut = new ArrayList<Double>(arrayValueCap);
		ArrayList<Double> scienceQOut = new ArrayList<Double>(arrayValueCap);
		ArrayList<Double> scienceHWGot = new ArrayList<Double>(arrayValueCap);
		ArrayList<Double> scienceCWGot = new ArrayList<Double>(arrayValueCap);
		ArrayList<Double> scienceTGot = new ArrayList<Double>(arrayValueCap);
		ArrayList<Double> scienceQGot = new ArrayList<Double>(arrayValueCap);

//SCIENCE creating the for loop that assigns the values to the corresponding list array based on the type and out/got 3
		for (int i=0 ; i < arrayValueCap ; i++) {
		String assignmentType = scienceScoreType.get(i);
			
		switch (assignmentType) {
			case "quiz" :
				scienceQGot.add(scienceScoreGot.get(i));
				scienceQOut.add(scienceScoreOut.get(i));
				break;
				case "test" :
				scienceTGot.add(scienceScoreGot.get(i));
				scienceTOut.add(scienceScoreOut.get(i));
				break;
				case "hw" :
				scienceHWGot.add(scienceScoreGot.get(i));
				scienceHWOut.add(scienceScoreOut.get(i));
				break;
				case "classwork" :
				scienceCWGot.add(scienceScoreGot.get(i));
				scienceCWOut.add(scienceScoreOut.get(i));
				break;
				
			}
		}
		
//SPANISH creating the arrays for all the different types of grade data needed
		ArrayList<Double> spanishHWOut = new ArrayList<Double>(arrayValueCap);
		ArrayList<Double> spanishCWOut = new ArrayList<Double>(arrayValueCap);
		ArrayList<Double> spanishTOut = new ArrayList<Double>(arrayValueCap);
		ArrayList<Double> spanishQOut = new ArrayList<Double>(arrayValueCap);
		ArrayList<Double> spanishHWGot = new ArrayList<Double>(arrayValueCap);
		ArrayList<Double> spanishCWGot = new ArrayList<Double>(arrayValueCap);
		ArrayList<Double> spanishTGot = new ArrayList<Double>(arrayValueCap);
		ArrayList<Double> spanishQGot = new ArrayList<Double>(arrayValueCap);

//SPANISH creating the for loop that assigns the values to the corresponding list array based on the type and out/got 3
		for (int i=0 ; i < arrayValueCap ; i++) {
					String assignmentType = spanishScoreType.get(i);
					
		switch (assignmentType) {
			case "quiz" :
			spanishQGot.add(spanishScoreGot.get(i));
				spanishQOut.add(spanishScoreOut.get(i));
				break;
				case "test" :
				spanishTGot.add(spanishScoreGot.get(i));
				spanishTOut.add(spanishScoreOut.get(i));
				break;
				case "hw" :
				spanishHWGot.add(spanishScoreGot.get(i));
				spanishHWOut.add(spanishScoreOut.get(i));
				break;
				case "classwork" :
				spanishCWGot.add(spanishScoreGot.get(i));
				spanishCWOut.add(spanishScoreOut.get(i));
				break;
		}
	}
						
}		
		void fillGrade(List<Double> gotType, List<Double> outType, List<String> typeType, List<String> nameType) {
			GridBagConstraints gbc = new GridBagConstraints();
			int check = typeType.size();
			for (int i=0; i < check; i++) {			
			typeLabels[i] = new JLabel(nameType.get(i));
			gbc.gridx = 3;
			gbc.gridy = 2+i;
			gbc.weightx = 0.5;
			gbc.weighty = 0.5;
			viewTeacher.add(typeLabels[i],gbc);
					
			scoreLabels[i] = new JLabel(gotType.get(i)+ "/" + outType.get(i));
			gbc.gridx = 4;
			gbc.gridy = 2+i;
			gbc.weightx = 0.5;
			gbc.weighty = 0.5;
			viewTeacher.add(scoreLabels[i],gbc);
					
			double percentRound = (gotType.get(i)/outType.get(i))*100;
			String percentString = round(percentRound,1)+"%";
			
			percentLabels[i] = new JLabel(percentString);
			gbc.gridx = 5;
			gbc.gridy = 2+i;
			gbc.weightx = 0.5;
			gbc.weighty = 0.5;	
			viewTeacher.add(percentLabels[i], gbc);
			
		}

}
		
class math2ActionListener implements ActionListener {
	@Override
public void actionPerformed(ActionEvent ae) {
	// TODO Auto-generated method stub
	currentGrades = "math";
	fillGrade(mathScoreGot,mathScoreOut,mathScoreType,mathScoreName);
	}
}

class english2ActionListener implements ActionListener {
	@Override
public void actionPerformed(ActionEvent ae) {
	// TODO Auto-generated method stub
	currentGrades = "english";
	fillGrade(englishScoreGot,englishScoreOut,englishScoreType,englishScoreName);
	}
}


class science2ActionListener implements ActionListener {
	@Override
public void actionPerformed(ActionEvent ae) {
	// TODO Auto-generated method stub
	currentGrades = "science";	
	fillGrade(scienceScoreGot,scienceScoreOut,scienceScoreType,scienceScoreName);
	}
}


class spanish2ActionListener implements ActionListener {
	@Override
public void actionPerformed(ActionEvent ae) {
	// TODO Auto-generated method stub
	currentGrades = "spanish";	
	fillGrade(spanishScoreGot,spanishScoreOut,spanishScoreType,spanishScoreName);
	}
}


class history2ActionListener implements ActionListener {
	@Override
public void actionPerformed(ActionEvent ae) {
	// TODO Auto-generated method stub
	currentGrades = "history";
	fillGrade(historyScoreGot,historyScoreOut,historyScoreType,historyScoreName);
	}
}

class dropboxActionListener implements ActionListener {
	@Override
public void actionPerformed(ActionEvent ae) {
	// TODO Auto-generated method stub
		dropSelected = studentDrop.getSelectedIndex();
		String queryName = fullNamesArray.get(dropSelected);
		gradeCalculatingTeacher(queryName);
		
	}
}

}



