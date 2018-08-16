package gradebook;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.mongodb.client.MongoCollection;

/*
 * 
 * Populates arrays with user data to be processed into GUI elements 
 * 
 */

class GradeTeacherCalc implements ConstantInterface {
	private String query_user;
	private String current_subject;
	private List<Double> subject_scoregot;
	private List<Double> subject_scoreout;
	private List<String> subject_scoretype;
	private List<String> subject_scorename;
	private MongoCollection<Document> subject_collection;

	GradeTeacherCalc(String queryuser, String subject) {
		query_user = queryuser;
		current_subject = subject;
		gradeCalcTeacher();
	}

	private void gradeCalcTeacher() {

		// Gets the collection to get data from based on user and subject
		String querystring = query_user + current_subject;
		subject_collection = DATABASE.getCollection(querystring);

		// Gets the score data for the subject and user
		MongoOperations subjectdatamongo = new MongoOperations();
		subject_scoretype = subjectdatamongo.calcScoreStrings(subject_collection, "scoretype");
		subject_scorename = subjectdatamongo.calcScoreStrings(subject_collection, "scorename");
		subject_scoregot = subjectdatamongo.calcScoreDoubles(subject_collection, "scoregot");
		subject_scoreout = subjectdatamongo.calcScoreDoubles(subject_collection, "scoreout");

		List<Double> hwout = new ArrayList<>();
		List<Double> cwout = new ArrayList<>();
		List<Double> tout = new ArrayList<>();
		List<Double> qout = new ArrayList<>();
		List<Double> hwgot = new ArrayList<>();
		List<Double> cwgot = new ArrayList<>();
		List<Double> tgot = new ArrayList<>();
		List<Double> qgot = new ArrayList<>();

		int valuecap = (subject_scoretype.size() + subject_scorename.size() + subject_scoregot.size()
				+ subject_scoreout.size()) / 4;

		// Classifies the score data into sub-categories based on the type
		for (int i = 0; i < valuecap; i++) {
			String assignment_type = subject_scoretype.get(i);

			switch (assignment_type) {
			case "quiz":
				qgot.add(subject_scoregot.get(i));
				qout.add(subject_scoreout.get(i));
				break;
			case "test":
				tgot.add(subject_scoregot.get(i));
				tout.add(subject_scoreout.get(i));
				break;
			case "hw":
				hwgot.add(subject_scoregot.get(i));
				hwout.add(subject_scoreout.get(i));
				break;
			case "classwork":
				cwgot.add(subject_scoregot.get(i));
				cwout.add(subject_scoreout.get(i));
				break;

			}
		}
	}

	// Gets for the different data to be used elsewhere without querying again
	List<Double> getScoreGot() {
		return subject_scoregot;
	}

	List<Double> getScoreOut() {
		return subject_scoreout;
	}

	List<String> getScoreType() {
		return subject_scoretype;
	}

	List<String> getScoreName() {
		return subject_scorename;
	}

	public MongoCollection<Document> getCollectionName() {
		return subject_collection;
	}
} // End of class