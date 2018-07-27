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
import java.util.HashMap;

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
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import com.mongodb.client.MongoCollection;
import static com.mongodb.client.model.Projections.*;

public class gradeteacher {

	private JFrame teacherViewFrame;
	private JPanel viewTeacher;
	private int arrayValueCap = 1;
	private ArrayList<JLabel> typeLabels = new ArrayList<JLabel>(arrayValueCap);
	private ArrayList<JLabel> scoreLabels = new ArrayList<JLabel>(arrayValueCap);
	private ArrayList<JLabel> percentLabels = new ArrayList<JLabel>(arrayValueCap);
	private ArrayList<JButton> removeButtons = new ArrayList<JButton>(arrayValueCap);
	private JTextField gradeOut;
	private JTextField gradeTotal;
	private JTextField gradeName;
	private JTextField gradeType;
	private JButton submitTeacherInput;

	private ArrayList<String> fullNamesArray = new ArrayList<String>();
	private ArrayList<String> usernameArray = new ArrayList<String>();
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
	private MongoCollection<Document> profileCollection;

	private int dropSelected;
	private MongoDatabase databaseNew;

	private MongoCollection<Document> englishCollection;
	private MongoCollection<Document> mathCollection;
	private MongoCollection<Document> historyCollection;
	private MongoCollection<Document> scienceCollection;
	private MongoCollection<Document> spanishCollection;

	private String assignmentName;
	private double assignmentOut;
	private double assignmentGot;
	private String assignmentType;

	private JButton studentEnglish;
	private JButton studentHistory;
	private JButton studentScience;
	private JButton studentMath;
	private JButton studentSpanish;
	private JButton logoutButton;

	private HashMap<JButton, JLabel> buttonCache;

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
		gradeOut.setToolTipText("Enter the total points available for the assignment.");
		gbc.gridx = 4;
		gbc.gridy = 7;
		gbc.weightx = 1;
		gbc.weighty = 1;
		viewTeacher.add(gradeOut, gbc);

		gradeTotal = new JTextField(10);
		gradeTotal.setToolTipText("Enter the points the student received for the assignment.");
		gbc.gridx = 3;
		gbc.gridy = 7;
		gbc.weightx = 1;
		gbc.weighty = 1;
		viewTeacher.add(gradeTotal, gbc);

		gradeName = new JTextField(10);
		gradeName.setToolTipText("Enter the name of the assignment.");
		gbc.gridx = 2;
		gbc.gridy = 7;
		gbc.weightx = 1;
		gbc.weighty = 1;
		viewTeacher.add(gradeName, gbc);

		gradeType = new JTextField(10);
		gradeType.setToolTipText(
				"Enter the type of grade the assignment is, in the format of either: quiz,hw,classwork,test.");
		gbc.gridx = 5;
		gbc.gridy = 7;
		gbc.weightx = 1;
		gbc.weighty = 1;
		viewTeacher.add(gradeType, gbc);

		gradeType.setMinimumSize(gradeType.getPreferredSize());
		gradeName.setMinimumSize(gradeName.getPreferredSize());
		gradeTotal.setMinimumSize(gradeTotal.getPreferredSize());
		gradeOut.setMinimumSize(gradeOut.getPreferredSize());

		submitTeacherInput = new JButton("Add");
		gbc.gridx = 6;
		gbc.gridy = 7;
		gbc.weightx = 0.5;
		gbc.weighty = 0.5;
		viewTeacher.add(submitTeacherInput, gbc);

		studentEnglish = new JButton("English");
		gbc.gridx = 2;
		gbc.gridy = 1;
		gbc.weightx = 0.5;
		gbc.weighty = 0.5;
		viewTeacher.add(studentEnglish, gbc);

		studentHistory = new JButton("History");
		gbc.gridx = 3;
		gbc.gridy = 1;
		gbc.weightx = 0.5;
		gbc.weighty = 0.5;
		viewTeacher.add(studentHistory, gbc);

		studentScience = new JButton("Science");
		gbc.gridx = 4;
		gbc.gridy = 1;
		gbc.weightx = 0.5;
		gbc.weighty = 0.5;
		viewTeacher.add(studentScience, gbc);

		studentMath = new JButton("Math");
		gbc.gridx = 5;
		gbc.gridy = 1;
		gbc.weightx = 0.5;
		gbc.weighty = 0.5;
		viewTeacher.add(studentMath, gbc);

		studentSpanish = new JButton("Spanish");
		gbc.gridx = 6;
		gbc.gridy = 1;
		gbc.weightx = 0.5;
		gbc.weighty = 0.5;
		viewTeacher.add(studentSpanish, gbc);

		logoutButton = new JButton("Logout");
		gbc.gridx = 6;
		gbc.gridy = 0;
		gbc.weightx = 1;
		gbc.weighty = 1;
		viewTeacher.add(logoutButton, gbc);

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

		addActionListener addAction = new addActionListener();
		submitTeacherInput.addActionListener(addAction);

		logoutActionListener logoutAction = new logoutActionListener();
		logoutButton.addActionListener(logoutAction);

		teacherViewFrame.add(viewTeacher);
		teacherViewFrame.pack();
		teacherViewFrame.setSize(700, 500);
		teacherViewFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

	void getNames() {
		fullNamesArray.add("None");
		this.profileCollection = grademain.getProfileCollection();
		FindIterable<Document> studentNames = profileCollection.find(eq("role", "student"))
				.projection(include("fullname", "username"));
		for (Document studentNamesDoc : studentNames) {

			fullNamesArray.add(studentNamesDoc.getString("fullname"));
			usernameArray.add(studentNamesDoc.getString("username"));

		}
	}

	void setFrame() {
		teacherViewFrame.setVisible(true);
	}

	double round(double value, int precision) {
		int scale = (int) Math.pow(10, precision);
		return (double) Math.round(value * scale) / scale;
	}

	public void gradeSetupTeacher() {
		int namesAmount = fullNamesArray.size();
		String[] comboboxArray = fullNamesArray.toArray(new String[namesAmount]);

		studentDrop = new JComboBox<String>(comboboxArray);

		dropboxActionListener dropAction = new dropboxActionListener();

		studentDrop.addActionListener(dropAction);

	}

	public void gradeCalculatingTeacher(String queryName, String queryUser) {

		// ENGLISH and MATH and HISTORY and SCIENCE making the strings for the
		// appropriate collection
		String englishString = queryUser + "english";
		String mathString = queryUser + "math";
		String historyString = queryUser + "history";
		String scienceString = queryUser + "science";
		String spanishString = queryUser + "spanish";

		// ENGLISH and MATH and HISTORY and SCIENCE naming a native collection after a
		// collection within the database
		englishCollection = databaseNew.getCollection(englishString);
		mathCollection = databaseNew.getCollection(mathString);
		historyCollection = databaseNew.getCollection(historyString);
		scienceCollection = databaseNew.getCollection(scienceString);
		spanishCollection = databaseNew.getCollection(spanishString);

		/*
		 * ENGLISH creating 3 different list arrays for the type of each score, the
		 * score the person got for each score and the total they could have gotten
		 */

		// change this to uniform way if it is plausible
		// englishScoreType = new ArrayList<>();
		// FindIterable<Document> olay =
		// englishCollection.find().projection(Projections.include("scoretype")); //WHAT
		// IS THE D IN THIS CASE THAT HOLD THE VALUE
		// System.out.println(olay);
		// for (Document scoreTypeDoc : olay) {
		// englishScoreType.add(scoreTypeDoc.getString("scoretype"));
		// }

		englishScoreType = new ArrayList<>();
		englishCollection.find().projection(Projections.include("scoretype")).forEach((Block<Document>) d -> {
			englishScoreType.add(d.getString("scoretype"));
		});
		englishScoreOut = new ArrayList<>();
		englishCollection.find().projection(Projections.include("scoreout")).forEach((Block<Document>) d -> {
			englishScoreOut.add(d.getDouble("scoreout"));
		});
		englishScoreGot = new ArrayList<>();
		englishCollection.find().projection(Projections.include("scoregot")).forEach((Block<Document>) d -> {
			englishScoreGot.add(d.getDouble("scoregot"));
		});
		englishScoreName = new ArrayList<>();
		englishCollection.find().projection(Projections.include("scorename")).forEach((Block<Document>) d -> {
			englishScoreName.add(d.getString("scorename"));
		});

		System.out.println(englishScoreType);
		/*
		 * MATH creating 3 different list arrays for the type of each score, the score
		 * the person got for each score and the total they could have gotten
		 */
		mathScoreType = new ArrayList<>();
		mathCollection.find().projection(Projections.include("scoretype")).forEach((Block<Document>) d -> {
			mathScoreType.add(d.getString("scoretype"));
		});
		mathScoreOut = new ArrayList<>();
		mathCollection.find().projection(Projections.include("scoreout")).forEach((Block<Document>) d -> {
			mathScoreOut.add(d.getDouble("scoreout"));
		});
		mathScoreGot = new ArrayList<>();
		mathCollection.find().projection(Projections.include("scoregot")).forEach((Block<Document>) d -> {
			mathScoreGot.add(d.getDouble("scoregot"));
		});
		mathScoreName = new ArrayList<>();
		mathCollection.find().projection(Projections.include("scorename")).forEach((Block<Document>) d -> {
			mathScoreName.add(d.getString("scorename"));
		});

		/*
		 * HISTORY creating 3 different list arrays for the type of each score, the
		 * score the person got for each score and the total they could have gotten
		 */
		historyScoreType = new ArrayList<>();
		historyCollection.find().projection(Projections.include("scoretype")).forEach((Block<Document>) d -> {
			historyScoreType.add(d.getString("scoretype"));
		});
		historyScoreOut = new ArrayList<>();
		historyCollection.find().projection(Projections.include("scoreout")).forEach((Block<Document>) d -> {
			historyScoreOut.add(d.getDouble("scoreout"));
		});
		historyScoreGot = new ArrayList<>();
		historyCollection.find().projection(Projections.include("scoregot")).forEach((Block<Document>) d -> {
			historyScoreGot.add(d.getDouble("scoregot"));
		});
		historyScoreName = new ArrayList<>();
		historyCollection.find().projection(Projections.include("scorename")).forEach((Block<Document>) d -> {
			historyScoreName.add(d.getString("scorename"));
		});

		/*
		 * SCIENCE creating 3 different list arrays for the type of each score, the
		 * score the person got for each score and the total they could have gotten
		 */
		scienceScoreType = new ArrayList<>();
		scienceCollection.find().projection(Projections.include("scoretype")).forEach((Block<Document>) d -> {
			scienceScoreType.add(d.getString("scoretype"));
		});
		scienceScoreOut = new ArrayList<>();
		scienceCollection.find().projection(Projections.include("scoreout")).forEach((Block<Document>) d -> {
			scienceScoreOut.add(d.getDouble("scoreout"));
		});
		scienceScoreGot = new ArrayList<>();
		scienceCollection.find().projection(Projections.include("scoregot")).forEach((Block<Document>) d -> {
			scienceScoreGot.add(d.getDouble("scoregot"));
		});
		scienceScoreName = new ArrayList<>();
		scienceCollection.find().projection(Projections.include("scorename")).forEach((Block<Document>) d -> {
			scienceScoreName.add(d.getString("scorename"));
		});

		System.out.println(scienceScoreType);
		/*
		 * SPANISH creating 3 different list arrays for the type of each score, the
		 * score the person got for each score and the total they could have gotten
		 */
		spanishScoreType = new ArrayList<>();
		spanishCollection.find().projection(Projections.include("scoretype")).forEach((Block<Document>) d -> {
			spanishScoreType.add(d.getString("scoretype"));
		});
		spanishScoreOut = new ArrayList<>();
		spanishCollection.find().projection(Projections.include("scoreout")).forEach((Block<Document>) d -> {
			spanishScoreOut.add(d.getDouble("scoreout"));
		});
		spanishScoreGot = new ArrayList<>();
		spanishCollection.find().projection(Projections.include("scoregot")).forEach((Block<Document>) d -> {
			spanishScoreGot.add(d.getDouble("scoregot"));
		});
		spanishScoreName = new ArrayList<>();
		spanishCollection.find().projection(Projections.include("scorename")).forEach((Block<Document>) d -> {
			spanishScoreName.add(d.getString("scorename"));
		});

//ENGLISH creating the arrays for all the different types of grade data needed
		int englishValueCap = (englishScoreType.size() + englishScoreName.size() + englishScoreGot.size()
				+ englishScoreOut.size()) / 4;

		ArrayList<Double> englishHWOut = new ArrayList<Double>(englishValueCap);
		ArrayList<Double> englishCWOut = new ArrayList<Double>(englishValueCap);
		ArrayList<Double> englishTOut = new ArrayList<Double>(englishValueCap);
		ArrayList<Double> englishQOut = new ArrayList<Double>(englishValueCap);
		ArrayList<Double> englishHWGot = new ArrayList<Double>(englishValueCap);
		ArrayList<Double> englishCWGot = new ArrayList<Double>(englishValueCap);
		ArrayList<Double> englishTGot = new ArrayList<Double>(englishValueCap);
		ArrayList<Double> englishQGot = new ArrayList<Double>(englishValueCap);

//ENGLISH creating the for loop that assigns the values to the corresponding list array based on the previous 3
		for (int i = 0; i < englishValueCap; i++) {
			String assignmentType = englishScoreType.get(i);

			switch (assignmentType) {
			case "quiz":
				englishQGot.add(englishScoreGot.get(i));
				englishQOut.add(englishScoreOut.get(i));
				break;
			case "test":
				englishTGot.add(englishScoreGot.get(i));
				englishTOut.add(englishScoreOut.get(i));
				break;
			case "hw":
				englishHWGot.add(englishScoreGot.get(i));
				englishHWOut.add(englishScoreOut.get(i));
				break;
			case "classwork":
				englishCWGot.add(englishScoreGot.get(i));
				englishCWOut.add(englishScoreOut.get(i));
				break;

			}
		}

//MATH creating the arrays for all the different types of grade data needed
		int mathValueCap = (mathScoreType.size() + mathScoreName.size() + mathScoreGot.size() + mathScoreOut.size())
				/ 4;

		ArrayList<Double> mathHWOut = new ArrayList<Double>(mathValueCap);
		ArrayList<Double> mathCWOut = new ArrayList<Double>(mathValueCap);
		ArrayList<Double> mathTOut = new ArrayList<Double>(mathValueCap);
		ArrayList<Double> mathQOut = new ArrayList<Double>(mathValueCap);
		ArrayList<Double> mathHWGot = new ArrayList<Double>(mathValueCap);
		ArrayList<Double> mathCWGot = new ArrayList<Double>(mathValueCap);
		ArrayList<Double> mathTGot = new ArrayList<Double>(mathValueCap);
		ArrayList<Double> mathQGot = new ArrayList<Double>(mathValueCap);

//MATH creating the for loop that assigns the values to the corresponding list array based on the type and out/got 3
		for (int i = 0; i < mathValueCap; i++) {
			String assignmentType = mathScoreType.get(i);

			switch (assignmentType) {
			case "quiz":
				mathQGot.add(mathScoreGot.get(i));
				mathQOut.add(mathScoreOut.get(i));
				break;
			case "test":
				mathTGot.add(mathScoreGot.get(i));
				mathTOut.add(mathScoreOut.get(i));
				break;
			case "hw":
				mathHWGot.add(mathScoreGot.get(i));
				mathHWOut.add(mathScoreOut.get(i));
				break;
			case "classwork":
				mathCWGot.add(mathScoreGot.get(i));
				mathCWOut.add(mathScoreOut.get(i));
				break;

			}
		}

//HISTORY creating the arrays for all the different types of grade data needed
		int historyValueCap = (historyScoreType.size() + historyScoreName.size() + historyScoreGot.size()
				+ historyScoreOut.size()) / 4;

		ArrayList<Double> historyHWOut = new ArrayList<Double>(historyValueCap);
		ArrayList<Double> historyCWOut = new ArrayList<Double>(historyValueCap);
		ArrayList<Double> historyTOut = new ArrayList<Double>(historyValueCap);
		ArrayList<Double> historyQOut = new ArrayList<Double>(historyValueCap);
		ArrayList<Double> historyHWGot = new ArrayList<Double>(historyValueCap);
		ArrayList<Double> historyCWGot = new ArrayList<Double>(historyValueCap);
		ArrayList<Double> historyTGot = new ArrayList<Double>(historyValueCap);
		ArrayList<Double> historyQGot = new ArrayList<Double>(historyValueCap);

//HISTORY creating the for loop that assigns the values to the corresponding list array based on the type and out/got 3
		for (int i = 0; i < historyValueCap; i++) {
			String assignmentType = historyScoreType.get(i);

			switch (assignmentType) {
			case "quiz":
				historyQGot.add(historyScoreGot.get(i));
				historyQOut.add(historyScoreOut.get(i));
				break;
			case "test":
				historyTGot.add(historyScoreGot.get(i));
				historyTOut.add(historyScoreOut.get(i));
				break;
			case "hw":
				historyHWGot.add(historyScoreGot.get(i));
				historyHWOut.add(historyScoreOut.get(i));
				break;
			case "classwork":
				historyCWGot.add(historyScoreGot.get(i));
				historyCWOut.add(historyScoreOut.get(i));
				break;

			}
		}

//SCIENCE creating the arrays for all the different types of grade data needed
		int scienceValueCap = (scienceScoreType.size() + scienceScoreName.size() + scienceScoreGot.size()
				+ scienceScoreOut.size()) / 4;

		ArrayList<Double> scienceHWOut = new ArrayList<Double>(scienceValueCap);
		ArrayList<Double> scienceCWOut = new ArrayList<Double>(scienceValueCap);
		ArrayList<Double> scienceTOut = new ArrayList<Double>(scienceValueCap);
		ArrayList<Double> scienceQOut = new ArrayList<Double>(scienceValueCap);
		ArrayList<Double> scienceHWGot = new ArrayList<Double>(scienceValueCap);
		ArrayList<Double> scienceCWGot = new ArrayList<Double>(scienceValueCap);
		ArrayList<Double> scienceTGot = new ArrayList<Double>(scienceValueCap);
		ArrayList<Double> scienceQGot = new ArrayList<Double>(scienceValueCap);

//SCIENCE creating the for loop that assigns the values to the corresponding list array based on the type and out/got 3
		for (int i = 0; i < scienceValueCap; i++) {
			String assignmentType = scienceScoreType.get(i);

			switch (assignmentType) {
			case "quiz":
				scienceQGot.add(scienceScoreGot.get(i));
				scienceQOut.add(scienceScoreOut.get(i));
				break;
			case "test":
				scienceTGot.add(scienceScoreGot.get(i));
				scienceTOut.add(scienceScoreOut.get(i));
				break;
			case "hw":
				scienceHWGot.add(scienceScoreGot.get(i));
				scienceHWOut.add(scienceScoreOut.get(i));
				break;
			case "classwork":
				scienceCWGot.add(scienceScoreGot.get(i));
				scienceCWOut.add(scienceScoreOut.get(i));
				break;

			}
		}

//SPANISH creating the arrays for all the different types of grade data needed
		int spanishValueCap = (spanishScoreType.size() + spanishScoreName.size() + spanishScoreGot.size()
				+ spanishScoreOut.size()) / 4;

		ArrayList<Double> spanishHWOut = new ArrayList<Double>(spanishValueCap);
		ArrayList<Double> spanishCWOut = new ArrayList<Double>(spanishValueCap);
		ArrayList<Double> spanishTOut = new ArrayList<Double>(spanishValueCap);
		ArrayList<Double> spanishQOut = new ArrayList<Double>(spanishValueCap);
		ArrayList<Double> spanishHWGot = new ArrayList<Double>(spanishValueCap);
		ArrayList<Double> spanishCWGot = new ArrayList<Double>(spanishValueCap);
		ArrayList<Double> spanishTGot = new ArrayList<Double>(spanishValueCap);
		ArrayList<Double> spanishQGot = new ArrayList<Double>(spanishValueCap);

//SPANISH creating the for loop that assigns the values to the corresponding list array based on the type and out/got 3
		for (int i = 0; i < spanishValueCap; i++) {
			String assignmentType = spanishScoreType.get(i);

			switch (assignmentType) {
			case "quiz":
				spanishQGot.add(spanishScoreGot.get(i));
				spanishQOut.add(spanishScoreOut.get(i));
				break;
			case "test":
				spanishTGot.add(spanishScoreGot.get(i));
				spanishTOut.add(spanishScoreOut.get(i));
				break;
			case "hw":
				spanishHWGot.add(spanishScoreGot.get(i));
				spanishHWOut.add(spanishScoreOut.get(i));
				break;
			case "classwork":
				spanishCWGot.add(spanishScoreGot.get(i));
				spanishCWOut.add(spanishScoreOut.get(i));
				break;
			}
		}

	}

	class removeActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ae) {
			// TODO Auto-generated method stub

			JButton button = (JButton) ae.getSource();

			dropSelected = studentDrop.getSelectedIndex();
			String queryName = fullNamesArray.get(dropSelected);
			int usernameIndex = dropSelected - 1;
			String queryUser = usernameArray.get(usernameIndex);
			System.out.println(buttonCache);
			System.out.println(button);
			JLabel scoreNameOfLine = buttonCache.get(button);
			String scoreNameOfLineString = scoreNameOfLine.getText();

			BasicDBObject document = new BasicDBObject();
			document.put("scorename", scoreNameOfLineString);
			switch (currentGrades) {
			case "math":
				mathCollection.deleteOne(document);
				gradeCalculatingTeacher(queryName, queryUser);
				studentMath.doClick();
				break;
			case "science":
				scienceCollection.deleteOne(document);
				gradeCalculatingTeacher(queryName, queryUser);
				studentScience.doClick();
				break;
			case "history":
				historyCollection.deleteOne(document);
				gradeCalculatingTeacher(queryName, queryUser);
				studentHistory.doClick();
				break;
			case "english":
				englishCollection.deleteOne(document);
				gradeCalculatingTeacher(queryName, queryUser);
				studentEnglish.doClick();
				break;
			case "spanish":
				spanishCollection.deleteOne(document);
				gradeCalculatingTeacher(queryName, queryUser);
				studentSpanish.doClick();
				break;

			}

			viewTeacher.remove(button);
		}
	}

	void fillGrade(List<Double> gotType, List<Double> outType, List<String> typeType, List<String> nameType) {

		percentLabels.clear();
		typeLabels.clear();
		scoreLabels.clear();
		removeButtons.clear();

		GridBagConstraints gbc = new GridBagConstraints();

		int check = (typeType.size() + gotType.size() + outType.size() + nameType.size()) / 4;
		for (int i = 0; i < check; i++) {

			JLabel typeLabel = new JLabel(nameType.get(i));
			typeLabels.add(typeLabel);
			gbc.gridx = 3;
			gbc.gridy = 2 + i;
			gbc.weightx = 0;
			gbc.weighty = 0;
			viewTeacher.add(typeLabels.get(i), gbc);

			JLabel scoreLabel = new JLabel(gotType.get(i) + "/" + outType.get(i));
			scoreLabels.add(scoreLabel);
			gbc.gridx = 4;
			gbc.gridy = 2 + i;
			gbc.weightx = 0;
			gbc.weighty = 0;
			viewTeacher.add(scoreLabels.get(i), gbc);

			double percentRound = (gotType.get(i) / outType.get(i)) * 100;
			String percentString = round(percentRound, 1) + "%";

			JLabel percentLabel = new JLabel(percentString);
			percentLabels.add(percentLabel);
			gbc.gridx = 5;
			gbc.gridy = 2 + i;
			gbc.weightx = 0;
			gbc.weighty = 0;
			viewTeacher.add(percentLabels.get(i), gbc);

			JButton removeButton = new JButton("Remove");
			removeActionListener removeAction = new removeActionListener();
			removeButton.addActionListener(removeAction);
			removeButtons.add(removeButton);
			gbc.gridx = 6;
			gbc.gridy = 2 + i;
			gbc.weightx = 0;
			gbc.weighty = 0;
			viewTeacher.add(removeButtons.get(i), gbc);

			buttonCache = new HashMap<JButton, JLabel>();
			buttonCache.put(removeButtons.get(i), typeLabels.get(i));
		}
		viewTeacher.validate();
	}

	class math2ActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ae) {
			// TODO Auto-generated method stub
			int labelLengths = (percentLabels.size());
			for (int i = 0; i < labelLengths; i++) {
				viewTeacher.remove(percentLabels.get(i));
				viewTeacher.remove(typeLabels.get(i));
				viewTeacher.remove(scoreLabels.get(i));
				viewTeacher.remove(removeButtons.get(i));
			}
			currentGrades = "math";
			fillGrade(mathScoreGot, mathScoreOut, mathScoreType, mathScoreName);
		}
	}

	class english2ActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ae) {
			// TODO Auto-generated method stub
			int labelLengths = (percentLabels.size());
			for (int i = 0; i < labelLengths; i++) {
				viewTeacher.remove(percentLabels.get(i));
				viewTeacher.remove(typeLabels.get(i));
				viewTeacher.remove(scoreLabels.get(i));
				viewTeacher.remove(removeButtons.get(i));
			}
			currentGrades = "english";
			fillGrade(englishScoreGot, englishScoreOut, englishScoreType, englishScoreName);
		}
	}

	class science2ActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ae) {
			// TODO Auto-generated method stub
			int labelLengths = (percentLabels.size());
			for (int i = 0; i < labelLengths; i++) {
				viewTeacher.remove(percentLabels.get(i));
				viewTeacher.remove(typeLabels.get(i));
				viewTeacher.remove(scoreLabels.get(i));
				viewTeacher.remove(removeButtons.get(i));
			}
			currentGrades = "science";
			fillGrade(scienceScoreGot, scienceScoreOut, scienceScoreType, scienceScoreName);
		}
	}

	class spanish2ActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ae) {
			// TODO Auto-generated method stub
			int labelLengths = (percentLabels.size());
			for (int i = 0; i < labelLengths; i++) {
				viewTeacher.remove(percentLabels.get(i));
				viewTeacher.remove(typeLabels.get(i));
				viewTeacher.remove(scoreLabels.get(i));
				viewTeacher.remove(removeButtons.get(i));
			}
			currentGrades = "spanish";
			fillGrade(spanishScoreGot, spanishScoreOut, spanishScoreType, spanishScoreName);
		}
	}

	class history2ActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ae) {
			// TODO Auto-generated method stub
			int labelLengths = percentLabels.size();
			for (int i = 0; i < labelLengths; i++) {
				viewTeacher.remove(percentLabels.get(i));
				viewTeacher.remove(typeLabels.get(i));
				viewTeacher.remove(scoreLabels.get(i));
				viewTeacher.remove(removeButtons.get(i));
			}
			currentGrades = "history";
			fillGrade(historyScoreGot, historyScoreOut, historyScoreType, historyScoreName);
		}
	}

	class dropboxActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ae) {
			// TODO Auto-generated method stub
			dropSelected = studentDrop.getSelectedIndex();
			String queryName = fullNamesArray.get(dropSelected);
			int usernameIndex = dropSelected - 1;
			String queryUser = usernameArray.get(usernameIndex);
			gradeCalculatingTeacher(queryName, queryUser);

		}
	}

	class addActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ae) {
			// TODO Auto-generated method stub
			collectionAlterations();
			gradeOut.setText("");
			gradeTotal.setText("");
			gradeName.setText("");
			gradeType.setText("");
		}
	}

	class logoutActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ae) {
			// TODO Auto-generated method stub
			teacherViewFrame.dispose();
			// teacherViewFrame.setVisible(false);
			JFrame userFrame = grademain.getUserFrame();
			grademain.operateUserField();
			grademain.operatePassField();
			userFrame.setVisible(true);
		}
	}

//Doesn't clear boxes in gui or show the updated results, basically need to effectively reload the grades
	public void collectionAlterations() {
		assignmentName = gradeName.getText();

		assignmentGot = Double.parseDouble(gradeTotal.getText());
		assignmentOut = Double.parseDouble(gradeOut.getText());

		assignmentType = gradeType.getText();

		Document additionDoc = new Document("scorename", assignmentName).append("scoreout", assignmentOut)
				.append("scoregot", assignmentGot).append("scoretype", assignmentType);

//put inside the switch make function or call the action listener
		dropSelected = studentDrop.getSelectedIndex();
		String queryName = fullNamesArray.get(dropSelected);
		int usernameIndex = dropSelected - 1;
		String queryUser = usernameArray.get(usernameIndex);

		switch (currentGrades) {
		case "math":
			mathCollection.insertOne(additionDoc);
			gradeCalculatingTeacher(queryName, queryUser);
			studentMath.doClick();
			break;

		case "english":
			englishCollection.insertOne(additionDoc);
			gradeCalculatingTeacher(queryName, queryUser);
			studentEnglish.doClick();
			break;

		case "spanish":
			spanishCollection.insertOne(additionDoc);
			gradeCalculatingTeacher(queryName, queryUser);
			studentSpanish.doClick();
			break;

		case "science":
			scienceCollection.insertOne(additionDoc);
			gradeCalculatingTeacher(queryName, queryUser);
			studentScience.doClick();
			break;

		case "history":
			historyCollection.insertOne(additionDoc);
			gradeCalculatingTeacher(queryName, queryUser);
			studentHistory.doClick();
			break;

		}

	}

}
