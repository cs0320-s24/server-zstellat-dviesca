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

public class SearchHandler implements Route {
  private static Logger LOGGER;
  private LoadHandler loadHandler;

  public SearchHandler(Logger logger, LoadHandler loadHandler) {
    LOGGER = logger;
    this.loadHandler = loadHandler;
  }

  // TODO
  @Override
  public Object handle(Request request, Response response) throws Exception {
    return this.searchCSV();
  }

  public Object searchCSV() {
    Map<String, Object> responseMap = new HashMap<>();

    // This ensures that a csvFile has already been loaded
    if (!this.loadHandler.getIsLoaded()) {
      responseMap.put(
          "Error", "CSV file not loaded. Must call 'loadcsv' prior to calling 'viewcsv'");
      return new CSVSearchSuccessResponse(responseMap).serialize();
    }

    ParsedDataPacket<List<String>, String> dataPacket = this.loadHandler.getDataPacket();
    if (dataPacket.containsHeader()) {
      responseMap.put("Headers", dataPacket.headers());
    }
    responseMap.put("Data", dataPacket.parsedRows());
    return new CSVSearchSuccessResponse(responseMap).serialize();
  }

  public record CSVSearchSuccessResponse(String responseType, Map<String, Object> responseMap) {
    public CSVSearchSuccessResponse(Map<String, Object> responseMap) {
      this("success", responseMap);
    }

    String serialize() {
      Moshi moshi = new Moshi.Builder().build();
      return moshi.adapter(SearchHandler.CSVSearchSuccessResponse.class).toJson(this);
    }
  }

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
