package edu.brown.cs.student.main.server.censusServer;

import edu.brown.cs.student.main.csv.csvoperations.ParsedDataPacket;
import edu.brown.cs.student.main.csv.csvoperations.Parser;
import edu.brown.cs.student.main.csv.csvoperations.exceptions.FactoryFailureException;
import edu.brown.cs.student.main.csv.csvoperations.rowoperations.StringRow;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;

public class BroadBandHandler implements Route {

  private HashMap<String, String> stateToNumberMap;
  private HashMap<String, String> numberToStateMap;

  /**
   * Constructor for the broadBandHandler class. Sets up maps that will be used for the state to
   * state ID conversions.
   */
  public BroadBandHandler() {
    this.createStateNumberMaps();
  }


  // Example request to API
  // https://api.census.gov/data/2021/acs/acs1/subject/variables?get=NAME,S2802_C03_022E&for=county:*&in=state:06
  /**
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  @Override
  public Object handle(Request request, Response response) throws Exception {
    String state = request.queryParams("state");
    String county = request.queryParams("county");
    String stateCode = this.stateToNumberMap.get(state);


    // TODO: Before making a query, check if it already exists in the cache



    Map<String, Object> responseMap = new HashMap<>();
    //    try{
    //      String censusDataJSon = this.sendBroadbandRequest(state, county);
    //    }

    return null;
  }

    private String sendBroadbandRequest(String state, String county) throws URISyntaxException {
      HttpRequest censusAPIRequest = HttpRequest.newBuilder().uri(new URI(


   "https://api.census.gov/data/2021/acs/acs1/subject/variables?get=NAME,S2802_C03_022E&for=county:*&in=state:06")).GET().build();

      return state;
    }

    private String sendCountryRequest(String stateCode, String county) throws URISyntaxException {
      HttpRequest censusAPIRequest = HttpRequest.newBuilder().uri(new URI(
          "https://api.census.gov/data/2010/dec/sf1?get=NAME&for=county:*&in=state:" +
              stateCode)).GET().build();

      return stateCode;
    }

    /**
   * This method instantiates the stateToNumber and numberToState maps by parsing through the saved
   * stateCodes.
   */
  private void createStateNumberMaps() {
      this.stateToNumberMap = new HashMap<>();
      this.numberToStateMap = new HashMap<>();

      try {
        // Read and parse the stateCodes CSV
        FileReader stateCodesFile = new FileReader("data/stateCodes.csv");
        Parser<List<String>, String> parser = new Parser<>();
        ParsedDataPacket<List<String>, String> stateCodes = parser.parse(new StringRow(), stateCodesFile,
            true);

        // Don't have to worry about headers because the parser stores them separately
        List<List<String>> parsedStates = stateCodes.parsedRows();
        for (List<String> state : parsedStates) {
          this.stateToNumberMap.put(state.get(0), state.get(1));
          this.numberToStateMap.put(state.get(1), state.get(0));
        }
      } catch (IOException | FactoryFailureException e) {
        System.err.println("Error: Could not read or parse data/stateCodes.csv File necessary for "
            + "converting state ID's to names in BroadBandHandler class.");
      }
  }
}
