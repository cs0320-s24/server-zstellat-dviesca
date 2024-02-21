package edu.brown.cs.student.main.server.censusServer;

import edu.brown.cs.student.main.csv.csvoperations.ParsedDataPacket;
import edu.brown.cs.student.main.csv.csvoperations.Parser;
import edu.brown.cs.student.main.csv.csvoperations.exceptions.FactoryFailureException;
import edu.brown.cs.student.main.csv.csvoperations.rowoperations.StringRow;
import edu.brown.cs.student.main.server.cache.Proxy;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;

public class BroadbandHandler implements Route {

  private Proxy proxy;
  private HashMap<String, String> stateToNumberMap;
  private HashMap<String, String> numberToStateMap;

  /**
   * Constructor for the broadBandHandler class. Sets up maps that will be used for the state to
   * state ID conversions.
   */
  public BroadbandHandler(Proxy proxy) {
    this.proxy = proxy;
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
    Map<String, Object> responseMap = new HashMap<>();


    // If (state data doesn't exist) --> Send BroadbandRequest to get the data, parse the data, and
    // add the data to the cache
    if (this.proxy.getStateToCountyCodeMap().get(stateCode) == null) {
      try {
        String countyResponse = this.sendCountyRequest(stateCode, county);
        List<CensusData> countyData = CensusAPIUtilities.deserializeCensusData(countyResponse);
        Map<String, String> countyNameToCodeMap =
            CensusAPIUtilities.createCountyNameToCodeMap(countyData);
        this.proxy.addStateToCountyCodes(stateCode, countyNameToCodeMap);
      } catch (IOException e) {
        responseMap.put("Error", "Could not request county code from API. Either State or County name"
            + " is invalid.");
        responseMap.put("State", state);
        responseMap.put("County", county);
        return new BroadbandFailureResponse(responseMap).serialize();
      }
    }

    String countyCode = this.proxy.getStateToCountyCodeMap().get(stateCode).get(county);
    if (countyCode == null) {
      responseMap.put("Error", "County code not found in API State to County Code data");
      responseMap.put("State", state);
      responseMap.put("County", county);
      return new BroadbandFailureResponse(responseMap).serialize();
    }

// TODO: ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // TODO: --> Search the data with the state code
    //  return --> A success JSON containing that good good  informational
    try {

      String dataResponse = this.sendBroadbandRequest(stateCode, countyCode);



    } catch () {

    }





    return null;
  }

    private String sendBroadbandRequest(String stateCode, String countyCode)
        throws URISyntaxException {
      HttpRequest censusAPIRequest = HttpRequest.newBuilder().uri(new URI(
   "https://api.census.gov/data/2021/acs/acs1/subject/variables?get=NAME,S2802_C03_022E&"
       + "for=county:" + countyCode + "&in=state:" + stateCode))
          .GET()
          .build();

      HttpResponse<String> sentAPIResponse = HttpClient.newBuilder()
              .build().send(censusAPIRequest, HttpResponse.BodyHandlers.ofString());


      return sentAPIResponse.body();
    }

    private String sendCountyRequest(String stateCode, String county) throws URISyntaxException {
      HttpRequest censusAPIRequest = HttpRequest.newBuilder().uri(new URI(
          "https://api.census.gov/data/2010/dec/sf1?get=NAME&for=county:" + county +
                  "&in=state:" + stateCode)).GET().build();

      HttpResponse<String> sentAPIResponse = HttpClient.newBuilder()
              .build().send(censusAPIRequest, HttpResponse.BodyHandlers.ofString());


      return sentAPIResponse.body();
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


  // TODO: Create this class
  private class BroadbandFailureResponse {

    public BroadbandFailureResponse(Map<String, Object> responseMap) {
    }
  }
}
