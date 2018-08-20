/**
 * 
 *Establishes constant variables and a constant method for repeated use throughout the project 
 * 
 **/

package gradebook;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

public interface ConstantInterface {
   // Establishes mongoclient for local use

   /*
    * MongoClient MONGOCLIENT = new MongoClient(new MongoClientURI(
    * "mongodb+srv://wendulem:9og8op33ttkLkpYj@cluster0-ugj9q.mongodb.net/test?retryWrites=true"
    * ));
    */
   MongoClient MONGOCLIENT = new MongoClient("localhost", 27017);

   // Establishes the database used throughout the project based on the
   // above mongo
   // connection
   MongoDatabase DATABASE = MONGOCLIENT.getDatabase("gradeu");
   // Establishes subject list for each changes later on
   String[] SUBJECT_ARRAY = { "spanish", "english", "history",
         "science", "math" };

   // Static rounding method to be used mostly when creating most #
   // oriented GUI
   // elements
   static double roundValue(double value, int precision) {
      int scale = (int) Math.pow(10, precision);
      return (double) Math.round(value * scale) / scale;
   }
} // End of interface
