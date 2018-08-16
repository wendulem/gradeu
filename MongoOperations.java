package gradebook;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Projections.include;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.Block;
import com.mongodb.DBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Projections;

/*
 * 
 * Does various MongoOperations in one unified class 
 * 
 */

class MongoOperations {

	MongoOperations() {

	}
	
	//Queries the database based on user input and returns additional user data based on the input
	Document loginDetails(MongoCollection<Document> collection, DBObject obj) {
		Document userdocument = collection.find((Bson) obj)
				.projection(
						Projections.fields(Projections.include("role", "firstname", "fullname"), Projections.excludeId()))
				.first();
		return userdocument;
	}

	//Gets the subject weights from the database based on the subject
	List<Integer> getSubjectWeights(MongoCollection<Document> collection) {
		// TRY AND MAKE THIS SO IT GETS ALL VALUES OF THIS TYPE WITHOUT SPECIFICATION
		List<Integer> subjectweights = new ArrayList<>();
		FindIterable<Document> iterable = collection.find();
		for (Document doc : iterable) {
			subjectweights.add(doc.getInteger("hwweight"));
			subjectweights.add(doc.getInteger("classworkweight"));
			subjectweights.add(doc.getInteger("quizweight"));
			subjectweights.add(doc.getInteger("testweight"));
		}
		return subjectweights;
	}

	//Gets the scoregot or scoreout depending on which is specified and what collection it is
	List<Double> calcScoreDoubles(MongoCollection<Document> collection, String querytype) {
		List<Double> doubledata = new ArrayList<>();
		collection.find().projection(Projections.include(querytype)).forEach((Block<Document>) d -> {
			doubledata.add(d.getDouble(querytype));
		});
		return doubledata;
	}

	//Does the same as the one above but for scorename and scoretype
	List<String> calcScoreStrings(MongoCollection<Document> collection, String querytype) {
		List<String> stringdata = new ArrayList<>();
		collection.find().projection(Projections.include(querytype)).forEach((Block<Document>) d -> {
			stringdata.add(d.getString(querytype));
		});
		return stringdata;
	}

	//Gets the fullnames and usernames of every student in the database
	FindIterable<Document> studentFullUser(MongoCollection<Document> profilecollection) {
		FindIterable<Document> studentnames = profilecollection.find(eq("role", "student"))
				.projection(include("fullname", "username"));
		return studentnames;

	}

} //End of class
