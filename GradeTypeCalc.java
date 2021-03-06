/**
 * 
 * Does the calculations needed to find the specific weights for each type and fnds what the weighted supervalues are
 * 
 **/

package gradebook;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;

import com.mongodb.client.MongoCollection;

class GradeTypeCalc {
   private List<String> score_type;
   private MongoCollection<Document> call_collection;
   private List<Integer> valid_parts;
   private int sub_value;
   private List<String> type_strings;
   private Map<String, Integer> type_map;
   private MongoOperations score_doubles;

   GradeTypeCalc(List<String> scoretype,
         MongoCollection<Document> collection, List<Integer> validparts,
         List<String> typestrings) {
      call_collection = collection;
      score_type = scoretype;
      score_doubles = new MongoOperations();
      valid_parts = validparts;
      sub_value = 0;
      type_strings = typestrings;
      type_map = new HashMap<>();
      // This puts the various types of grades with their specific
      // weights for each
      // subject from the DB
      for (int t = 0; t < valid_parts.size(); t++) {
         type_map.put(type_strings.get(t), valid_parts.get(t));
      }
   }

   // The below method gets the various gotten scores and what they were
   // out of for
   // the type that's an arguement
   double processCalc(String type) {
      int itlength = score_type.size();
      List<Double> scoregot = score_doubles
            .calcScoreDoubles(call_collection, "scoregot");
      List<Double> scoreout = score_doubles
            .calcScoreDoubles(call_collection, "scoreout");
      double currentscore = 0;
      double currenttotal = 0;
      for (int i = 0; i < itlength; i++) {
         String scoretype = score_type.get(i);
         if (scoretype.equals(type)) {
            currentscore += scoregot.get(i);
            currenttotal += scoreout.get(i);
         }
      }
      // Below the total score and what it's out of are found and
      // divided to create
      // the unweighted super

      double currentsupernw = (currentscore / currenttotal);

      // This cumulatively finds the valid parts of the weight as this
      // iterates for
      // each type used
      if (Double.isNaN(currentsupernw)) {
         sub_value += type_map.get(type);
         // int removeindex = Integer.valueOf(type_map.get(type));
         type_map.remove(type);
      }
      return currentsupernw;
   }

   // This is used once this is iterated enough to return all the values
   // that
   // change across each type that is used
   int getSubValue() {
      return sub_value;
   }

   Map<String, Integer> getTypeMap() {
      return type_map;
   }
} // End of class
