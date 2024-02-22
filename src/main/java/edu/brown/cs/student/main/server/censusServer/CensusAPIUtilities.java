package edu.brown.cs.student.main.server.censusServer;

import com.squareup.moshi.*;
import edu.brown.cs.student.main.csv.csvoperations.ParsedDataPacket;
import edu.brown.cs.student.main.csv.csvoperations.Parser;
import edu.brown.cs.student.main.csv.csvoperations.exceptions.FactoryFailureException;
import edu.brown.cs.student.main.csv.csvoperations.rowoperations.StringRow;

import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CensusAPIUtilities {

  /**
   * HELPFUL INFO: This is a request for county data inside a state with state code (06)
   * 'https://api.census.gov/data/2010/dec/sf1?get=NAME&for=county:*&in=state:06' Returns: [ [
   * "NAME", "state", "county" ], [ "Colusa County, California" , "06", "011" ], ... ] List < List <
   * String > > <--- csv pretty much -- StringList csv containing headers.
   */
  private CensusAPIUtilities() {}

  public static List<CensusData> deserializeCensus(String jsonList)
      throws IOException, JsonDataException {
    // initialize moshi
//    Moshi moshi = new Moshi.Builder().build();
//    // create lisType for adapter
//    Type listType = Types.newParameterizedType(List.class, CensusData.class);
//    // parse into list of CensusData with adapter
//    JsonAdapter<List<CensusData>> adapter = moshi.adapter(listType);
//    System.out.println("Data STUFF: " + adapter.fromJson(jsonList));
//    return adapter.fromJson(jsonList);


    ArrayList<CensusData> censusDataList = new ArrayList<>();
    Parser<List<String>, String> parser = new Parser<>();
    ParsedDataPacket<List<String>, String> packet = null;
    try {
      packet =
              parser.parse(new StringRow(), new StringReader(jsonList), true);
    } catch (Exception e) {
      System.out.println("Fail");
    }
    List<String> header = packet.headers();
    censusDataList.add(new CensusData(header.get(0), header.get(1), header.get(2), header.get(4)));
    for (List<String>  s: packet.parsedRows() ) {
      censusDataList.add(new CensusData(s.get(0), s.get(1), s.get(2), s.get(4)));
    }
    System.out.println(censusDataList);
    return censusDataList;


  }

  // TODO: Should replicate this:
  /**
   * try { // Initializes Moshi Moshi moshi = new Moshi.Builder().build();
   *
   * <p>// Initializes an adapter to an Activity class then uses it to parse the JSON.
   * JsonAdapter<Activity> adapter = moshi.adapter(Activity.class);
   *
   * <p>Activity activity = adapter.fromJson(jsonActivity);
   *
   * <p>return activity; } // Returns an empty activity... Probably not the best handling of this
   * error case... // Notice an alternative error throwing case to the one done in OrderHandler.
   * This catches // the error instead of pushing it up. catch (IOException e) {
   * e.printStackTrace(); return new Activity(); } }
   */
  public static String serializeCensus(List<CensusData> censusData) {
    // initialize moshi
    Moshi moshi = new Moshi.Builder().build();
    // Create type for adapter
    Type listOfCensusDataType = Types.newParameterizedType(List.class, CensusData.class);
    // Parse into a string with adapter
    JsonAdapter<List<CensusData>> adapter = moshi.adapter(listOfCensusDataType);
    return adapter.toJson(censusData);
  }

  public static List<CountyCodeData> deserializeCensusData(String jsonCensusData)
      throws IOException, FactoryFailureException {
    // Moshi moshi = new Moshi.Builder().build();

    //Type listOfCensusData = Types.newParameterizedType(List.class, CountyCodeData.class);
    //JsonAdapter<List<CountyCodeData>> adapter = moshi.adapter(listOfCensusData);
    //System.out.println("CountyCODE STUFF: " + adapter.fromJson(jsonCensusData));
    ArrayList<CountyCodeData> censusDataList = new ArrayList<>();
    Parser<List<String>, String> parser = new Parser<>();
    ParsedDataPacket<List<String>, String> packet = null;
    packet = parser.parse(new StringRow(), new StringReader(jsonCensusData), true);
    ArrayList<String> header = packet.headers();
    CountyCodeData thing = new CountyCodeData(header.get(0), header.get(1), header.get(2));
    censusDataList.add(thing);
    for (List<String>  s: packet.parsedRows() ) {
      censusDataList.add(new CountyCodeData(s.get(0), s.get(1), s.get(2)));
    }
    return censusDataList;
  }


  /**
   * Creates a map from a list of Census data returned by the API.
   *
   * @param countyData List of CensusData
   * @return a map (CountyName --> CountyCode)
   */
  public static Map<String, String> createCountyNameToCodeMap(List<CountyCodeData> countyData) {
    Map<String, String> countyNameToCodeMap = new HashMap<>();

    for (int i = 1; i < countyData.size(); i++) { // TODO: Do we need to start at 1 or 0?
      CountyCodeData censusItem = countyData.get(i);
      countyNameToCodeMap.put(censusItem.NAME(), censusItem.county());
    }
    return countyNameToCodeMap;
  }
}
