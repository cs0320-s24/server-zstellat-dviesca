package edu.brown.cs.student.main.server.csvServer;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import edu.brown.cs.student.main.csv.csvoperations.ParsedDataPacket;
import edu.brown.cs.student.main.csv.csvoperations.Parser;
import edu.brown.cs.student.main.csv.csvoperations.exceptions.FactoryFailureException;
import edu.brown.cs.student.main.csv.csvoperations.rowoperations.StringRow;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import spark.Request;
import spark.Response;
import spark.Route;

public class LoadHandler implements Route {
  private static Logger LOGGER;
  private ParsedDataPacket<List<String>, String> dataPacket;
  private boolean isLoaded;

  // TODO: this isn't really helpful because the only time we need it is when we are calling it
  //  in the Failure Response class, but that isn't part of the LoadHandlerClass and so it isnt
  //  accessible.
  private String relativePath;

  /**
   * Constructor for the
   *
   * @param logger the log files
   */
  public LoadHandler(Logger logger) {
    LOGGER = logger;
    this.isLoaded = false;
    this.relativePath = "No File Path Specified";
  }

  /**
   * Getter method for the boolean is loaded. Used to check if a file is loaded yet.
   *
   * @return true if the file has been successfully loaded, and false otherwise;
   */
  public boolean getIsLoaded() {
    return this.isLoaded;
  }

  /**
   * Getter method for the data packet parsed by
   *
   * @return the data packet object contained in the loader class
   */
  public ParsedDataPacket<List<String>, String> getDataPacket() {
    return this.dataPacket;
  }

  // TODO
  /**
   * @param request the request passed by the frontend user
   * @param response a json containing data about the response.
   * @return
   * @throws Exception
   */
  @Override
  public Object handle(Request request, Response response) {
    String route = request.queryParams("route");
    String hasHeaderString = request.queryParams("hasHeader");

    return this.loadCSV(route, hasHeaderString);
  }

  private Object loadCSV(String relativeFilePath, String hasHeaderString) throws RuntimeException {
    this.relativePath = relativeFilePath;
    // to protect data on the computer the reader will look only within defined csvFilePath package
    String dataRootPath = "data/";
    String csvFilePath = dataRootPath + this.relativePath;
    Map<String, Object> responseMap = new HashMap<>();

    // This checks the header string
    boolean hasHeader = false;
    if (hasHeaderString.equalsIgnoreCase("true")) {
      hasHeader = true;
      // If not false and it already didn't say true, then do error.
    } else if (!hasHeaderString.equalsIgnoreCase("false")) {
      // If the string isn't "true" or "false", return an error
      responseMap.put(
          "Error", "Invalid hasHeader argument. Accepted values are 'false' and 'true'");
      responseMap.put("hasHeader argument", hasHeaderString);
      return new CSVFailureResponse(responseMap).serialize();
    }

    try {
      Reader csvReader = new FileReader(csvFilePath);
      this.dataPacket =
          new Parser<List<String>, String>().parse(new StringRow(), csvReader, hasHeader);
      this.isLoaded = true; // Set to true if it gets here without throwing an exception
      return new CSVLoadedSuccessResponse(responseMap).serialize();
    }
    // TODO: LOG THESE ERRORS
    catch (FileNotFoundException e) {
      responseMap.put("Error", "CSV file not found");
      responseMap.put("Route", this.relativePath);
      return new CSVFailureResponse(responseMap).serialize();
    } catch (IOException e) {
      responseMap.put("Error", "An IO error occurred while trying to read the CSV file by line");
      responseMap.put("Route", this.relativePath);
      return new CSVFailureResponse(responseMap).serialize();
    } catch (FactoryFailureException e) {
      responseMap.put("Error", e.getMessage());
      responseMap.put("Route", this.relativePath);
      return new CSVFailureResponse(responseMap).serialize();
    }
  }

  public record CSVLoadedSuccessResponse(String responseType, Map<String, Object> responseMap) {
    public CSVLoadedSuccessResponse(Map<String, Object> responseMap) {
      this("success", responseMap);
    }

    // TODO conceptualize maybe change (catch json issues)
    String serialize() {
      Moshi moshi = new Moshi.Builder().build();
      JsonAdapter<CSVLoadedSuccessResponse> adapter = moshi.adapter(CSVLoadedSuccessResponse.class);
      return adapter.toJson(this);
    }
  }

  public record CSVFailureResponse(String responseType, Map<String, Object> responseMap) {
    public CSVFailureResponse(Map<String, Object> responseMap) {
      this("error", responseMap);
    }

    String serialize() {
      Moshi moshi = new Moshi.Builder().build();
      return moshi.adapter(CSVFailureResponse.class).toJson(this);
    }
  }
}
