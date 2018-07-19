package gradebook;

import java.awt.event.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;

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

import gradebook.grademain.inputActionListener;

import java.util.ArrayList;
import java.util.List;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

public class gradeback {
	public static JFrame studentFrame;
	JPanel mainStudent = new JPanel();
	public static List<Double> mathScoreGot;
	public static List<Double> mathScoreOut;
	public static List<String> mathScoreType;
	public static List<String> mathScoreName;
	public static List<Double> scienceScoreGot;
	public static List<Double> scienceScoreOut;
	public static List<String> scienceScoreType;
	public static List<String> scienceScoreName;
	public static List<Double> historyScoreGot;
	public static List<Double> historyScoreOut;
	public static List<String> historyScoreType;
	public static List<String> historyScoreName;
	public static List<Double> englishScoreGot;
	public static List<Double> englishScoreOut;
	public static List<String> englishScoreType;
	public static List<String> englishScoreName;
	public static List<Double> spanishScoreGot;
	public static List<Double> spanishScoreOut;
	public static List<String> spanishScoreType;
	public static List<String> spanishScoreName;
	private static double englishSuper;
	private static double mathSuper;
	private static double historySuper;
	private static double scienceSuper;
	private static double spanishSuper;
	private static String localFrameString;
	private static String currentButton;
	
public gradeback(String nameOfUser){
	String nameOfFrame = "Welcome " + nameOfUser;
	gradeback.localFrameString = nameOfFrame;
	studentFrame = new JFrame(nameOfFrame);
	studentFrame.add(mainStudent);
	GridBagConstraints gbc = new GridBagConstraints();
	mainStudent.setLayout(new GridBagLayout());
	
	JLabel englishClass = new JLabel("English: " + englishSuper);
	gbc.gridx = 0;
	gbc.gridy = 1;
	gbc.weightx = 1;
	gbc.weighty = 1;
	mainStudent.add(englishClass, gbc);
	
	JLabel mathClass = new JLabel("Math: " + mathSuper);
	gbc.gridx = 0;
	gbc.gridy = 2;
	gbc.weightx = 1;
	gbc.weighty = 1;
	mainStudent.add(mathClass, gbc);
	
	JLabel scienceClass = new JLabel("Science: " + scienceSuper);
	gbc.gridx = 0;
	gbc.gridy = 3;
	gbc.weightx = 1;
	gbc.weighty = 1;
	mainStudent.add(scienceClass, gbc);
	
	JLabel spanishClass = new JLabel("Spanish: " + spanishSuper);
	gbc.gridx = 3;
	gbc.gridy = 1;
	gbc.weightx = 1;
	gbc.weighty = 1;
	mainStudent.add(spanishClass, gbc);
	
	JLabel historyClass = new JLabel("History: " + historySuper);
	gbc.gridx = 3;
	gbc.gridy = 2;
	gbc.weightx = 1;
	gbc.weighty = 1;
	mainStudent.add(historyClass, gbc);
	
	JButton englishButton = new JButton("View");
	gbc.gridx = 1;
	gbc.gridy = 1;
	gbc.weightx = 1;
	gbc.weighty = 1;
	mainStudent.add(englishButton, gbc);
	englishActionListener englishAction = new englishActionListener();
	englishButton.addActionListener(englishAction);
	
	JButton mathButton = new JButton("View");
	gbc.gridx = 1;
	gbc.gridy = 2;	
	gbc.weightx = 1;
	gbc.weighty = 1;
	mainStudent.add(mathButton, gbc);
	mathActionListener mathAction = new mathActionListener();
	mathButton.addActionListener(mathAction);
	
	JButton scienceButton = new JButton("View");
	gbc.gridx = 1;
	gbc.gridy = 3;	
	gbc.weightx = 1;
	gbc.weighty = 1;
	mainStudent.add(scienceButton, gbc);
	scienceActionListener scienceAction = new scienceActionListener();
	scienceButton.addActionListener(scienceAction);
	
	JButton spanishButton = new JButton ("View");
	gbc.gridx = 4;
	gbc.gridy = 1;
	gbc.weightx = 1;
	gbc.weighty = 1;
	mainStudent.add(spanishButton, gbc);
	spanishActionListener spanishAction = new spanishActionListener();
	spanishButton.addActionListener(spanishAction);

	JButton historyButton = new JButton ("View");
	gbc.gridx = 4;
	gbc.gridy = 2;	
	gbc.weightx = 1;
	gbc.weighty = 1;
	mainStudent.add(historyButton, gbc);
	historyActionListener historyAction = new historyActionListener();
	historyButton.addActionListener(historyAction);
	
	gbc.gridwidth = 1;
	studentFrame.pack();
		studentFrame.setSize(400,300);
		studentFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
}

public static void gradeCalculations(String userValue, MongoDatabase database) {
//ENGLISH and MATH and HISTORY and SCIENCE making the strings for the appropriate collection
	String englishString = userValue + "english";
	String mathString = userValue + "math";
	String historyString = userValue + "history";
	String scienceString = userValue + "science";
	String spanishString = userValue + "spanish";
	
//ENGLISH and MATH and HISTORY and SCIENCE naming a native collection after a collection within the database
	MongoCollection<Document> englishCollection = database
			.getCollection(englishString);
	MongoCollection<Document> mathCollection = database
			.getCollection(mathString);
	MongoCollection<Document> historyCollection = database
			.getCollection(historyString);
	MongoCollection<Document> scienceCollection = database
			.getCollection(scienceString);
	MongoCollection<Document> spanishCollection = database
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
	
//ALL Setting the scoreGotLength element that is used to give some sort of cap on the list arrays ADJUSTED BY EACH
	int scoreGotLength = englishScoreType.size();

//ENGLISH creating the arrays for all the different types of grade data needed
	ArrayList<Double> englishHWOut = new ArrayList<Double>(scoreGotLength);
	ArrayList<Double> englishCWOut = new ArrayList<Double>(scoreGotLength);
	ArrayList<Double> englishTOut = new ArrayList<Double>(scoreGotLength);
	ArrayList<Double> englishQOut = new ArrayList<Double>(scoreGotLength);
	ArrayList<Double> englishHWGot = new ArrayList<Double>(scoreGotLength);
	ArrayList<Double> englishCWGot = new ArrayList<Double>(scoreGotLength);
	ArrayList<Double> englishTGot = new ArrayList<Double>(scoreGotLength);
	ArrayList<Double> englishQGot = new ArrayList<Double>(scoreGotLength);
	
//ENGLISH creating the for loop that assigns the values to the corresponding list array based on the previous 3
	for (int i=0 ; i < scoreGotLength ; i++) {
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

//MATH Reassigning the length variable
	scoreGotLength = mathScoreType.size();
	
//MATH creating the arrays for all the different types of grade data needed
	ArrayList<Double> mathHWOut = new ArrayList<Double>(scoreGotLength);
	ArrayList<Double> mathCWOut = new ArrayList<Double>(scoreGotLength);
	ArrayList<Double> mathTOut = new ArrayList<Double>(scoreGotLength);
	ArrayList<Double> mathQOut = new ArrayList<Double>(scoreGotLength);
	ArrayList<Double> mathHWGot = new ArrayList<Double>(scoreGotLength);
	ArrayList<Double> mathCWGot = new ArrayList<Double>(scoreGotLength);
	ArrayList<Double> mathTGot = new ArrayList<Double>(scoreGotLength);
	ArrayList<Double> mathQGot = new ArrayList<Double>(scoreGotLength);

//MATH creating the for loop that assigns the values to the corresponding list array based on the type and out/got 3
	for (int i=0 ; i < scoreGotLength ; i++) {
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
	
//HISTORY Reassigning the length variable
		scoreGotLength = historyScoreType.size();
		
//HISTORY creating the arrays for all the different types of grade data needed
		ArrayList<Double> historyHWOut = new ArrayList<Double>(scoreGotLength);
		ArrayList<Double> historyCWOut = new ArrayList<Double>(scoreGotLength);
		ArrayList<Double> historyTOut = new ArrayList<Double>(scoreGotLength);
		ArrayList<Double> historyQOut = new ArrayList<Double>(scoreGotLength);
		ArrayList<Double> historyHWGot = new ArrayList<Double>(scoreGotLength);
		ArrayList<Double> historyCWGot = new ArrayList<Double>(scoreGotLength);
		ArrayList<Double> historyTGot = new ArrayList<Double>(scoreGotLength);
		ArrayList<Double> historyQGot = new ArrayList<Double>(scoreGotLength);

//HISTORY creating the for loop that assigns the values to the corresponding list array based on the type and out/got 3
		for (int i=0 ; i < scoreGotLength ; i++) {
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
		
//SCIENCE Reassigning the length variable
		scoreGotLength = scienceScoreType.size();
		
//SCIENCE creating the arrays for all the different types of grade data needed
		ArrayList<Double> scienceHWOut = new ArrayList<Double>(scoreGotLength);
		ArrayList<Double> scienceCWOut = new ArrayList<Double>(scoreGotLength);
		ArrayList<Double> scienceTOut = new ArrayList<Double>(scoreGotLength);
		ArrayList<Double> scienceQOut = new ArrayList<Double>(scoreGotLength);
		ArrayList<Double> scienceHWGot = new ArrayList<Double>(scoreGotLength);
		ArrayList<Double> scienceCWGot = new ArrayList<Double>(scoreGotLength);
		ArrayList<Double> scienceTGot = new ArrayList<Double>(scoreGotLength);
		ArrayList<Double> scienceQGot = new ArrayList<Double>(scoreGotLength);

//SCIENCE creating the for loop that assigns the values to the corresponding list array based on the type and out/got 3
		for (int i=0 ; i < scoreGotLength ; i++) {
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
		
//SPANISH Reassigning the length variable
		scoreGotLength = spanishScoreType.size();
		
//SPANISH creating the arrays for all the different types of grade data needed
		ArrayList<Double> spanishHWOut = new ArrayList<Double>(scoreGotLength);
		ArrayList<Double> spanishCWOut = new ArrayList<Double>(scoreGotLength);
		ArrayList<Double> spanishTOut = new ArrayList<Double>(scoreGotLength);
		ArrayList<Double> spanishQOut = new ArrayList<Double>(scoreGotLength);
		ArrayList<Double> spanishHWGot = new ArrayList<Double>(scoreGotLength);
		ArrayList<Double> spanishCWGot = new ArrayList<Double>(scoreGotLength);
		ArrayList<Double> spanishTGot = new ArrayList<Double>(scoreGotLength);
		ArrayList<Double> spanishQGot = new ArrayList<Double>(scoreGotLength);

//SPANISH creating the for loop that assigns the values to the corresponding list array based on the type and out/got 3
		for (int i=0 ; i < scoreGotLength ; i++) {
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
		
//ENGLISH SCORES
	
		//englishHomework
		double ehwScore = 0;
		for(int i = 0; i < englishHWGot.size(); i++) {
		    ehwScore += englishHWGot.get(i);
		  }

		double ehwTotal = 0;
		for(int i = 0; i < englishHWOut.size(); i++){
		    ehwTotal += englishHWOut.get(i);
		  }
		double ehwSuper = (ehwScore / ehwTotal) * 0.1;
		  
		//englishClasswork
		double ecwScore = 0;
		for(int i = 0; i < englishCWGot.size(); i++) {
		    ecwScore += englishCWGot.get(i);
		  }

		double ecwTotal = 0;
		for(int i = 0; i < englishCWOut.size(); i++){
		    ecwTotal += englishCWOut.get(i);
		  }
		double ecwSuper = (ecwScore / ecwTotal) * 0.2;

		//englishTests
		double etScore = 0;
		for(int i = 0; i < englishTGot.size(); i++) {
		    etScore += englishTGot.get(i);
		  }

		double etTotal = 0;
		for(int i = 0; i < englishTOut.size(); i++){
		    etTotal += englishTOut.get(i);
		  }
		double etSuper = (etScore / etTotal) * 0.4;
		 
		//englishQuizzes
		double eqScore = 0;
		for(int i = 0; i < englishQGot.size(); i++) {
		    eqScore += englishQGot.get(i);
		  }

		double eqTotal = 0;
		for(int i = 0; i < englishQOut.size(); i++){
		    eqTotal += englishQOut.get(i);
		  }
		double eqSuper = (eqScore / eqTotal) * 0.3;
		
//MATH SCORES		
		
		//mathHomework
		double mhwScore = 0;
		for(int i = 0; i < mathHWGot.size(); i++) {
			mhwScore += mathHWGot.get(i);
		}

		double mhwTotal = 0;
		for(int i = 0; i < mathHWOut.size(); i++){
			mhwTotal += mathHWOut.get(i);
		}
		double mhwSuper = (mhwScore / mhwTotal) * 0.1;
				  
		//mathClasswork
		double mcwScore = 0;
		for(int i = 0; i < mathCWGot.size(); i++) {
			mcwScore += mathCWGot.get(i);
		}

		double mcwTotal = 0;
		for(int i = 0; i < mathCWOut.size(); i++){
			mcwTotal += mathCWOut.get(i);
		}
		double mcwSuper = (mcwScore / mcwTotal) * 0.2;

		//mathTests
		double mtScore = 0;
		for(int i = 0; i < mathTGot.size(); i++) {
			mtScore += mathTGot.get(i);
		}

		double mtTotal = 0;
		for(int i = 0; i < mathTOut.size(); i++){
			mtTotal += mathTOut.get(i);
		}
			double mtSuper = (mtScore / mtTotal) * 0.4;
				 
		//mathQuizzes
		double mqScore = 0;
		for(int i = 0; i < mathQGot.size(); i++) {
			mqScore += mathQGot.get(i);
		}

		double mqTotal = 0;
		for(int i = 0; i < mathQOut.size(); i++){
			mqTotal += mathQOut.get(i);
		}
		double mqSuper = (mqScore / mqTotal) * 0.3;
		
//HISTORY SCORES
		
		//historyHomework
		double hhwScore = 0;
		for(int i = 0; i < historyHWGot.size(); i++) {
			hhwScore += historyHWGot.get(i);
		}

		double hhwTotal = 0;
		for(int i = 0; i < historyHWOut.size(); i++){
			hhwTotal += historyHWOut.get(i);
		}
		double hhwSuper = (hhwScore / hhwTotal) * 0.1;
			  
		//historyClasswork
		double hcwScore = 0;
		for(int i = 0; i < historyCWGot.size(); i++) {
			hcwScore += historyCWGot.get(i);
		}

		double hcwTotal = 0;
		for(int i = 0; i < historyCWOut.size(); i++){
			hcwTotal += historyCWOut.get(i);
		}
		double hcwSuper = (hcwScore / hcwTotal) * 0.2;

		//historyTests
		double htScore = 0;
		for(int i = 0; i < historyTGot.size(); i++) {
			htScore += historyTGot.get(i);
		}

		double htTotal = 0;
		for(int i = 0; i < historyTOut.size(); i++){
			htTotal += historyTOut.get(i);
		}
		double htSuper = (htScore / htTotal) * 0.4;
			 
		//historyQuizzes
		double hqScore = 0;
		for(int i = 0; i < historyQGot.size(); i++) {
			hqScore += historyQGot.get(i);
		}

		double hqTotal = 0;
		for(int i = 0; i < historyQOut.size(); i++){
			 hqTotal += historyQOut.get(i);
		}
		double hqSuper = (hqScore / hqTotal) * 0.3;
				
//SCIENCE SCORES		
		
		//scienceHomework
		double schwScore = 0;
		for(int i = 0; i < scienceHWGot.size(); i++) {
			schwScore += scienceHWGot.get(i);
		}

		double schwTotal = 0;
		for(int i = 0; i < scienceHWOut.size(); i++){
			schwTotal += scienceHWOut.get(i);
		}
		double schwSuper = (schwScore / schwTotal) * 0.1;
						  
		//scienceClasswork
		double sccwScore = 0;
		for(int i = 0; i < scienceCWGot.size(); i++) {
			sccwScore += scienceCWGot.get(i);
		}

		double sccwTotal = 0;
		for(int i = 0; i < scienceCWOut.size(); i++){
			sccwTotal += scienceCWOut.get(i);
		}
		double sccwSuper = (sccwScore / sccwTotal) * 0.2;

		//scienceTests
		double sctScore = 0;
		for(int i = 0; i < scienceTGot.size(); i++) {
			sctScore += scienceTGot.get(i);
		}

		double sctTotal = 0;
		for(int i = 0; i < scienceTOut.size(); i++){
			sctTotal += scienceTOut.get(i);
		}
		double sctSuper = (sctScore / sctTotal) * 0.4;
						 
		//scienceQuizzes
		double scqScore = 0;
		for(int i = 0; i < scienceQGot.size(); i++) {
			scqScore += scienceQGot.get(i);
		}

		double scqTotal = 0;
		for(int i = 0; i < scienceQOut.size(); i++){
			scqTotal += scienceQOut.get(i);
		}
		double scqSuper = (scqScore / scqTotal) * 0.3;

//SPANISH SCORES
		
		//spanishHomework
		double sphwScore = 0;
		for(int i = 0; i < spanishHWGot.size(); i++) {
			sphwScore += spanishHWGot.get(i);
		}
		double sphwTotal = 0;
		for(int i = 0; i < spanishHWOut.size(); i++){
			sphwTotal += spanishHWOut.get(i);
		}
		double sphwSuper = (sphwScore / sphwTotal) * 0.1;
			  
		//spanishClasswork
		double spcwScore = 0;
		for(int i = 0; i < spanishCWGot.size(); i++) {
			spcwScore += spanishCWGot.get(i);
		}

		double spcwTotal = 0;
		for(int i = 0; i < spanishCWOut.size(); i++){
			spcwTotal += spanishCWOut.get(i);
		}
		double spcwSuper = (spcwScore / spcwTotal) * 0.2;

		//spanishTests
		double sptScore = 0;
		for(int i = 0; i < spanishTGot.size(); i++) {
			sptScore += spanishTGot.get(i);
		}

		double sptTotal = 0;
		for(int i = 0; i < spanishTOut.size(); i++){
			sptTotal += spanishTOut.get(i);
		}
		double sptSuper = (sptScore / sptTotal) * 0.4;
			 
		//spanishQuizzes
		double spqScore = 0;
		for(int i = 0; i < spanishQGot.size(); i++) {
			spqScore += spanishQGot.get(i);
		}

		double spqTotal = 0;
		for(int i = 0; i < spanishQOut.size(); i++){
			 spqTotal += spanishQOut.get(i);
			  }
		double spqSuper = (spqScore / spqTotal) * 0.3;
			
//final calcs
	englishSuper = (eqSuper+etSuper+ecwSuper+ehwSuper)*100;
	englishSuper = round(englishSuper,1);
	mathSuper = (mqSuper+mtSuper+mcwSuper+mhwSuper)*100;
	mathSuper = round (mathSuper,1);
	historySuper = (hqSuper+htSuper+hcwSuper+hhwSuper)*100;
	historySuper = round (historySuper,1);
	scienceSuper = (scqSuper+sctSuper+sccwSuper+schwSuper)*100;
	scienceSuper = round (scienceSuper,1);
	spanishSuper = (spqSuper+sptSuper+spcwSuper+sphwSuper)*100;
	spanishSuper = round (spanishSuper,1);
}

static double round (double value, int precision) {
    int scale = (int) Math.pow(10, precision);
    return (double) Math.round(value * scale) / scale;
}

class mathActionListener implements ActionListener {
	@Override
public void actionPerformed(ActionEvent ae) {
	// TODO Auto-generated method stub
		currentButton = "math";	
		studentFrame.setVisible(false);
		new gradestudent(localFrameString,currentButton,mathScoreGot,mathScoreOut,mathScoreType,mathScoreName);
		gradestudent.studentViewFrame.setVisible(true);
	}
}

class englishActionListener implements ActionListener {
	@Override
public void actionPerformed(ActionEvent ae) {
	// TODO Auto-generated method stub
		currentButton = "english";	
		studentFrame.setVisible(false);
		new gradestudent(localFrameString,currentButton,englishScoreGot,englishScoreOut,englishScoreType,englishScoreName);
		gradestudent.studentViewFrame.setVisible(true);
	}
}


class scienceActionListener implements ActionListener {
	@Override
public void actionPerformed(ActionEvent ae) {
	// TODO Auto-generated method stub
		currentButton = "science";	
		studentFrame.setVisible(false);
		new gradestudent(localFrameString,currentButton,scienceScoreGot,scienceScoreOut,scienceScoreType,scienceScoreName);
		gradestudent.studentViewFrame.setVisible(true);
	}
}


class spanishActionListener implements ActionListener {
	@Override
public void actionPerformed(ActionEvent ae) {
	// TODO Auto-generated method stub
		currentButton = "spanish";	
		studentFrame.setVisible(false);
		new gradestudent(localFrameString,currentButton,spanishScoreGot,spanishScoreOut,spanishScoreType,spanishScoreName);
		gradestudent.studentViewFrame.setVisible(true);
	}
}


class historyActionListener implements ActionListener {
	@Override
public void actionPerformed(ActionEvent ae) {
	// TODO Auto-generated method stub
		currentButton = "history";	
		studentFrame.setVisible(false);
		new gradestudent(localFrameString,currentButton,historyScoreGot,historyScoreOut,historyScoreType,historyScoreName);
		gradestudent.studentViewFrame.setVisible(true);
	}
}


}





