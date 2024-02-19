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

  @Override
  public Object handle(Request request, Response response) throws Exception {
    return this.viewCSV();
  }

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
