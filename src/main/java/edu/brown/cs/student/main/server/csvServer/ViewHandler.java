package edu.brown.cs.student.main.server.csvServer;

import com.squareup.moshi.Moshi;
import edu.brown.cs.student.main.csv.csvoperations.ParsedDataPacket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import spark.Request;
import spark.Response;
import spark.Route;

public class ViewHandler implements Route {
  private static Logger LOGGER;
  private final LoadHandler loadHandler;

  public ViewHandler(Logger logger, LoadHandler loadHandler) {
    LOGGER = logger;
    this.loadHandler = loadHandler;
  }

  /**
   * Method when the searchCSV route is called by the server calls a helper method to check if the
   * csv is viewable.
   *
   * @param request the request passed by the frontend user
   * @param response a json containing data about the response.
   * @return a response json containing information about how the viewing process went.
   * @throws RuntimeException if an error occurs while trying to create a Json from the data.
   */
  @Override
  public Object handle(Request request, Response response) throws RuntimeException {
    return this.viewCSV();
  }

  /**
   * Helper class called by the handle method to view the loaded csv.
   * @return a serialized Json containing either the loaded csv data or error data.
   * @throws RuntimeException if an error occurs while trying to create a Json from the data.
   */
  private Object viewCSV() throws RuntimeException {
    Map<String, Object> responseMap = new HashMap<>();

    // This ensures that a csvFile has already been loaded
    if (!this.loadHandler.getIsLoaded()) {
      responseMap.put(
          "Error", "CSV file not loaded. Must call 'loadcsv' prior to calling 'viewcsv'");
      return new CSVViewFailureResponse(responseMap).serialize();
    }

    ParsedDataPacket<List<String>, String> dataPacket = this.loadHandler.getDataPacket();
    if (dataPacket.containsHeader()) {
      responseMap.put("Headers", dataPacket.headers());
    }
    responseMap.put("Data", dataPacket.parsedRows());
    return new CSVViewSuccessResponse(responseMap).serialize();
  }

/**
 * Class used to serialize a success response for a CSV view operation
 * @param responseType a String containing the response type, i.e. "success"
 * @param responseMap a Map of String, Object pairs containing important info about the response
 *                    including the rows of parsed data.
 */
  public record CSVViewSuccessResponse(String responseType, Map<String, Object> responseMap) {
    public CSVViewSuccessResponse(Map<String, Object> responseMap) {
      this("success", responseMap);
    }

    String serialize() {
      Moshi moshi = new Moshi.Builder().build();
      return moshi.adapter(ViewHandler.CSVViewSuccessResponse.class).toJson(this);
    }
  }

  /**
   * Class used to serialize a failure response for a CSV view operation.
   * @param responseType a String containing the response type, i.e. "error"
   * @param responseMap is a Map of String, Object pairs containing pertinent error information.
   */
  public record CSVViewFailureResponse(String responseType, Map<String, Object> responseMap) {
    public CSVViewFailureResponse(Map<String, Object> responseMap) {
      this("error", responseMap);
    }

    String serialize() {
      Moshi moshi = new Moshi.Builder().build();
      return moshi.adapter(ViewHandler.CSVViewFailureResponse.class).toJson(this);
    }
  }
}
