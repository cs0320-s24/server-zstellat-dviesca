package edu.brown.cs.student.main.server.csvServer;

import com.squareup.moshi.Moshi;
import edu.brown.cs.student.main.csv.csvoperations.ParsedDataPacket;
import edu.brown.cs.student.main.csv.csvoperations.Searcher;
import edu.brown.cs.student.main.csv.csvoperations.exceptions.FactoryFailureException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import spark.Request;
import spark.Response;
import spark.Route;

public class SearchHandler implements Route {
  private static Logger LOGGER;
  private LoadHandler loadHandler;

  /**
   * Constructor for SearchHandler. Initializes a new SearchHandler instance with specified Logger
   * and LoadHandler. ChatGPT was utilized to help create simple javadocs.
   *
   * @param logger The logger used for logging information and errors.
   * @param loadHandler The LoadHandler instance used for handling CSV file loading operations.
   */
  public SearchHandler(Logger logger, LoadHandler loadHandler) {
    LOGGER = logger;
    this.loadHandler = loadHandler;
  }

  /**
   * Method when the searchCSV route is called by the server, it checks the query parameters and
   * then calls a helper method to perform the searching.
   *
   * @param request the request passed by the frontend user
   * @param response a json containing data about the response.
   * @return a response json containing information about how the searching process went.
   * @throws RuntimeException if an error occurs while trying to create a Json from the data.
   */
  @Override
  public Object handle(Request request, Response response) throws RuntimeException {
    String searchTerm = request.queryParams("searchTerm");
    String columnIdentifier = request.queryParams("columnIdentifier");

    return this.searchCSV(searchTerm, columnIdentifier);
  }

  /**
   * Helper class called by the handle method to search the loaded csv.
   * @param searchTerm a String containing the user's inputted data 'searchTerm'
   * @param columnIdentifier An optional string containing the user's inputted data about
   *                        'columnIdentifier'
   * @return a serialized Json containing response info about output, error or any valid matches.
   * @throws RuntimeException if an error occurs while trying to create a Json from the data.
   */
  public Object searchCSV(String searchTerm, String columnIdentifier) throws RuntimeException {
    Map<String, Object> responseMap = new HashMap<>();

    // This ensures that a csvFile has already been loaded
    if (!this.loadHandler.getIsLoaded()) {
      responseMap.put(
          "Error", "CSV file not loaded. Must call 'loadcsv' prior to calling 'viewcsv'");
      return new CSVSearchSuccessResponse(responseMap).serialize();
    }

    ParsedDataPacket<List<String>, String> dataPacket = this.loadHandler.getDataPacket();
    Searcher<List<String>, String> searcher = new Searcher<>();
    List<List<String>> result;
    try {
      // Calls search differently if a columnIdentifier was passed or not
      if (columnIdentifier == null || columnIdentifier.isEmpty()) {
        result = searcher.search(dataPacket, searchTerm);
      } else {
        result = searcher.search(dataPacket, searchTerm, columnIdentifier);
      }

      // Adds results to responseMap
      responseMap.put("success", "CSV was searched");
      if (dataPacket.containsHeader()) {
        responseMap.put("Column headers", dataPacket.headers());
      }
      responseMap.put("Query results", result);
      return new CSVSearchSuccessResponse(responseMap).serialize();

    } catch (FactoryFailureException e) {
      responseMap = e.getResponseMap();
      return new CSVSearchFailureResponse(responseMap);
    }
  }

  /**
   * Class used to serialize a success response for a CSV search operation
   * @param responseType a String containing the response type, i.e. "success"
   * @param responseMap a Map of String, Object pairs containing important info about the
   *                    process and the results.
   */
  public record CSVSearchSuccessResponse(String responseType, Map<String, Object> responseMap) {
    public CSVSearchSuccessResponse(Map<String, Object> responseMap) {
      this("success", responseMap);
    }

    String serialize() {
      Moshi moshi = new Moshi.Builder().build();
      return moshi.adapter(SearchHandler.CSVSearchSuccessResponse.class).toJson(this);
    }
  }

  /**
   * Class used to serialize a failure response for a CSV search operation.
   * @param responseType a String containing the response type, i.e. "error"
   * @param responseMap is a Map of String, Object pairs containing pertinent error information.
   */
  public record CSVSearchFailureResponse(String responseType, Map<String, Object> responseMap) {
    public CSVSearchFailureResponse(Map<String, Object> responseMap) {
      this("error", responseMap);
    }

    String serialize() {
      Moshi moshi = new Moshi.Builder().build();
      return moshi.adapter(SearchHandler.CSVSearchFailureResponse.class).toJson(this);
    }
  }
}
