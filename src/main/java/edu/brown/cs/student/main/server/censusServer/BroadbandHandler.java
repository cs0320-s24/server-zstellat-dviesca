package edu.brown.cs.student.main.server.censusServer;

import com.squareup.moshi.JsonDataException;
import com.squareup.moshi.Moshi;
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
   * Method takes in requests from the server and calls helper method, loadCSV with queryparams.
   *
   * @param request the request passed by the frontend user
   * @param response a json containing data about the response.
   * @return a response json containing information about how the loading process went.
   * @throws RuntimeException if an error occurs while trying to create a Json from the data.
   */
  @Override
  public Object handle(Request request, Response response) {
    // TODO:  +++++++++++++++++++++++
    System.out.println("INSIDE BROADBAND");
    String state = request.queryParams("state");
    String county = request.queryParams("county");
    String stateCode = this.stateToNumberMap.get(state);
    Map<String, Object> responseMap = new HashMap<>();

    // Switch county to correct county string needed for API: ('CountyName, StateName')
    // String county = shortCounty + ",+" + state;

    // If (state data doesn't exist) --> Send BroadbandRequest to get the data, parse the data, and
    // add the data to the cache
    if (this.proxy.getStateToCountyCodeMap().get(stateCode) == null) {
      try {
        //        System.out.print(
        //            "state: " + state + ". county: " + county + ". Statecode: " + stateCode +
        // "\n");

        // makes a request to the government API for county codes within the state
        String countyResponse = this.sendCountyRequest(stateCode);
        System.out.println("County response: " + countyResponse);

        List<CountyCodeData> countyData = CensusAPIUtilities.deserializeCensusData(countyResponse);
        System.out.println("County data: " + countyData + "\n\n");

        Map<String, String> countyNameToCodeMap =
            CensusAPIUtilities.createCountyNameToCodeMap(countyData);
        System.out.println("createCountyNameToCodeMap");

        this.proxy.addStateToCountyCodes(stateCode, countyNameToCodeMap);
      } catch (IOException | URISyntaxException | InterruptedException e) {
        System.out.println("+++Exception caught");
        responseMap.put(
            "Error",
            "Could not request county code from API. Either State or County name" + " is invalid.");
        responseMap.put("State", state);
        responseMap.put("County", county);
        return new BroadbandFailureResponse(responseMap).serialize();
      } catch (FactoryFailureException e) {
          throw new RuntimeException(e);
      }
    }

    // Get county code and make sure it's not null
    String countyCode = this.proxy.getStateToCountyCodeMap().get(stateCode).get(county);
    if (countyCode == null) {
      // TODO:  +++++++++++++++++++++++
      System.out.println("is null");
      responseMap.put("Error", "County code not found in API State to County Code data");
      responseMap.put("State", state);
      responseMap.put("County", county);
      return new BroadbandFailureResponse(responseMap).serialize();
    }
    // TODO:  +++++++++++++++++++++++
    System.out.println("Past null");


    // Get the data from the county and return a success json containing the relevant info
    try {
      String dataResponse = this.sendBroadbandRequest(stateCode, countyCode);
      List<CensusData> coverageData = CensusAPIUtilities.deserializeCensus(dataResponse);
      return CensusAPIUtilities.serializeCensus(coverageData);
    } catch (IOException | URISyntaxException | InterruptedException e) {
      responseMap.put(
          "Error",
          "Could not request county code from API. Either State or County name" + " is invalid.");
      responseMap.put("State", state);
      responseMap.put("County", county);
      return new BroadbandFailureResponse(responseMap).serialize();
    } catch (JsonDataException e) {
      responseMap.put("Error", "DataHandler: JSON wasn't in the right format.");
      responseMap.put("State", state);
      responseMap.put("County", county);
      return new BroadbandFailureResponse(responseMap).serialize();
    }
  }

  private String sendBroadbandRequest(String stateCode, String countyCode)
      throws URISyntaxException, JsonDataException, IOException, InterruptedException {

    // INITIAL REQUEST THAT
    HttpRequest censusAPIRequest =
        HttpRequest.newBuilder()
            .uri(
                new URI(
                    "https://api.census.gov/data/2021/acs/acs1/subject/variables?get=NAME,S2802_C03_022E&"
                        + "for=county:"
                        + countyCode
                        + "&in=state:"
                        + stateCode))
            .GET()
            .build();

    HttpResponse<String> sentAPIResponse =
        HttpClient.newBuilder()
            .build()
            .send(censusAPIRequest, HttpResponse.BodyHandlers.ofString());
    return sentAPIResponse.body();
  }

  private String sendCountyRequest(String stateCode)
      throws URISyntaxException, IOException, InterruptedException {
    HttpRequest censusAPIRequest =
        HttpRequest.newBuilder()
            .uri(
                new URI(
                    "https://api.census.gov/data/2010/dec/sf1?get=NAME&for=county:*"
                        + "&in=state:"
                        + stateCode))
            .GET()
            .build();

    HttpResponse<String> sentAPIResponse =
        HttpClient.newBuilder()
            .build()
            .send(censusAPIRequest, HttpResponse.BodyHandlers.ofString());

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
      ParsedDataPacket<List<String>, String> stateCodes =
          parser.parse(new StringRow(), stateCodesFile, true);

      // Don't have to worry about headers because the parser stores them separately
      List<List<String>> parsedStates = stateCodes.parsedRows();
      for (List<String> state : parsedStates) {
        this.stateToNumberMap.put(state.get(0), state.get(1));
        this.numberToStateMap.put(state.get(1), state.get(0));
      }
    } catch (IOException | FactoryFailureException e) {
      System.err.println(
          "Error: Could not read or parse data/stateCodes.csv File necessary for "
              + "converting state ID's to names in BroadBandHandler class.");
    }
  }

  /**
   * Class used to serialize a failure response for a broadBandHandler operation.
   *
   * @param responseType a String containing the response type, i.e. "error"
   * @param responseMap is a Map of String, Object pairs containing pertinent error information.
   */
  public record BroadbandFailureResponse(String responseType, Map<String, Object> responseMap) {
    public BroadbandFailureResponse(Map<String, Object> responseMap) {
      this("error", responseMap);
    }

    String serialize() {
      Moshi moshi = new Moshi.Builder().build();
      return moshi.adapter(BroadbandFailureResponse.class).toJson(this);
    }
  }
}
