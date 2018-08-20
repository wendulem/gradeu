package gradebook;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

class MongoSetup {
   private static List<String> user_names;
   private static List<String> pass_words;
   private static List<String> first_names;
   private static List<String> last_names;
   private static List<String> full_names;
   private static List<String> user_roles;
   private static List<String> subject_list;
   private static List<Integer> science_weights;
   private static List<Integer> math_weights;
   private static List<Integer> english_weights;
   private static List<Integer> history_weights;
   private static List<Integer> spanish_weights;
   private static List<List<Integer>> weight_list;

   static {
      user_names = new ArrayList<String>();
      Collections.addAll(user_names, "admin", "steven403", "yasa781");
      pass_words = new ArrayList<String>();
      Collections.addAll(pass_words, "admin", "CIT403", "DUKE781");
      first_names = new ArrayList<String>();
      Collections.addAll(first_names, "Connor", "Steven", "Yasa");
      last_names = new ArrayList<String>();
      Collections.addAll(last_names, "Johnson", "Reiss", "Baig");
      full_names = new ArrayList<String>();
      Collections.addAll(full_names, "Connor Johnson", "Steven Reiss",
            "Yasa Baig");
      user_roles = new ArrayList<String>();
      Collections.addAll(user_roles, "admin", "student", "student");
      subject_list = new ArrayList<String>();
      Collections.addAll(subject_list, "spanish", "english", "science",
            "history", "math");
      spanish_weights = new ArrayList<Integer>();
      Collections.addAll(spanish_weights, 10, 20, 30, 40);
      weight_list.add(spanish_weights);
      english_weights = new ArrayList<Integer>();
      Collections.addAll(english_weights, 10, 20, 30, 40);
      weight_list.add(english_weights);
      science_weights = new ArrayList<Integer>();
      Collections.addAll(science_weights, 10, 20, 30, 40);
      weight_list.add(science_weights);
      history_weights = new ArrayList<Integer>();
      Collections.addAll(history_weights, 10, 20, 30, 40);
      weight_list.add(history_weights);
      math_weights = new ArrayList<Integer>();
      Collections.addAll(math_weights, 10, 20, 30, 40);
      weight_list.add(math_weights);
   }

   static void main(String[] args) {
      // TODO Auto-generated method stub
      @SuppressWarnings("resource")
      MongoClient client = new MongoClient("localhost", 27017);
      MongoDatabase db = client.getDatabase("gradeu");
      db.drop();
      db = client.getDatabase("gradeu");
      db.createCollection("profiles");
      MongoCollection<Document> profilecollection = db
            .getCollection("profiles");
      for (int i = 0; i < user_names.size(); i++) {
         Document document = new Document();
         document.put("username", user_names.get(i));
         document.put("password", pass_words.get(i));
         document.put("firstname", first_names.get(i));
         document.put("lastname", last_names.get(i));
         document.put("fullname", full_names.get(i));
         document.put("role", user_roles.get(i));
         profilecollection.insertOne(document);
      }
      for (int i = 0; i < user_names.size(); i++) {
         for (int g = 0; g < subject_list.size(); g++) {
            String collectionstring = user_names.get(i + 1)
                  + subject_list.get(g);
            db.createCollection(collectionstring);
         }
      }
      for (int i = 0; i < subject_list.size(); i++) {
         db.createCollection(subject_list.get(i));
         String currentsubject = subject_list.get(i);
         MongoCollection<Document> subjectcollection = db
               .getCollection(currentsubject);
         Document document = new Document();
         document.put("hwweight", weight_list.get(i).get(0));
         document.put("classworkweight", weight_list.get(i).get(1));
         document.put("quizweight", weight_list.get(i).get(2));
         document.put("testweight", weight_list.get(i).get(3));
         subjectcollection.insertOne(document);

      }
   }
}
