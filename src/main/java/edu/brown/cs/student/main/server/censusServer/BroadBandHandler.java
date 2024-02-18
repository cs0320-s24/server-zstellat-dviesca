package edu.brown.cs.student.main.server.censusServer;

import edu.brown.cs.student.main.csv.csvoperations.ParsedDataPacket;
import edu.brown.cs.student.main.csv.csvoperations.Parser;
import edu.brown.cs.student.main.csv.csvoperations.rowoperations.StringRow;
import spark.Request;
import spark.Response;
import spark.Route;

import java.io.FileReader;
import java.net.URI;
import java.net.http.HttpRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BroadBandHandler implements Route {

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
    String stateCode = getCodeFromState(state);

    Map<String, Object> responseMap = new HashMap<>();
    try{
      String censusDataJSon = this.sendBroadbandRequest(state, county);
    }

    return null;
  }


  private String sendBroadbandRequest(String state, String county){
    HttpRequest censusAPIRequest = HttpRequest.newBuilder().uri(new URI(
            "https://api.census.gov/data/2021/acs/acs1/subject/variables?get=NAME,S2802_C03_022E&for=county:*&in=state:06")).GET().build();

  }

  private String sendCountryRequest(String stateCode, String county){
    HttpRequest censusAPIRequest = HttpRequest.newBuilder().uri(new URI(
            "https://api.census.gov/data/2010/dec/sf1?get=NAME&for=county:*&in=state:" + stateCode)).GET().build();

  }

  private String getCodeFromState(String stateName){
    FileReader stateFile = new FileReader("data/stateCodes.csv");

    Parser<List<String>, String> parser = new Parser<>();

    ParsedDataPacket<List<String>, String> stateCodes = parser.parse(new StringRow(), stateFile, true);

    // Don't have to worry about headers because the parser stores them separately
    List<List<String>> parsedStates = stateCodes.parsedRows();

    HashMap<String, String> stateToNumberMap = new HashMap<>();
    HashMap<String, String> numberToStateMap = new HashMap<>();

    for (List<String> state : parsedStates) {
      stateToNumberMap.put(state.get(0), state.get(1));
    }
    return stateToNumberMap.get(stateName);
  }
}
