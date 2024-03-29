package edu.brown.cs.student.main.server.csvServer;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import edu.brown.cs.student.main.csv.csvoperations.ParsedDataPacket;
import edu.brown.cs.student.main.csv.csvoperations.Parser;
import edu.brown.cs.student.main.csv.csvoperations.exceptions.FactoryFailureException;
import edu.brown.cs.student.main.csv.csvoperations.rowoperations.StringRow;
import java.io.*;
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

  private String relativePath;

  /**
   * Constructor for the LoadHandler class. Sets up important variables and instantiates the logger.
   * TODO: Check TODO in loadCSV method and alter abs path
   *
   * @param logger the log object used to log actions
   */
  public LoadHandler(Logger logger) {
    LOGGER = logger;
    this.isLoaded = false;
    this.relativePath = "No File Path Specified"; // Temporary until overwritten by handle call.
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

  /**
   * Method takes in requests from the server and calls helper method, loadCSV with queryparams.
   *
   * @param request the request passed by the frontend user
   * @param response a json containing data about the response.
   * @return a response json containing information about how the loading process went.
   * @throws RuntimeException if an error occurs while trying to create a Json from the data.
   */
  @Override
  public Object handle(Request request, Response response) throws RuntimeException {
    String route = request.queryParams("filepath");
    String hasHeaderString = request.queryParams("hasHeader");

    return this.loadCSV(route, hasHeaderString);
  }

  // http://localhost:3232/loadcsv?filepath=RI_Town_Income.csv&hasHeader=true
  /**
   * Helper class called by the handle method to parse the given csv.
   *
   * @param relativeFilePath a String containing the user's inputted file path for 'filepath'
   * @param hasHeaderString A string containing the user's inputted information about 'hasHeader'
   * @return a serialized Json containing response info about the process.
   * @throws RuntimeException if an error occurs while trying to create a Json from the data.
   */
  private Object loadCSV(String relativeFilePath, String hasHeaderString) throws RuntimeException {
    this.relativePath = relativeFilePath;
    // to protect data on the computer the reader will look only within defined csvFilePath package
    String dataRootPath = "data/";
    String csvFilePath = dataRootPath + this.relativePath;
    Map<String, Object> responseMap = new HashMap<>();

    // To prevent empty queries
    if (this.relativePath.isEmpty()) {
      responseMap.put("Error", "Invalid file path specified");
      responseMap.put("FilePath", this.relativePath);
      return new CSVFailureResponse(responseMap).serialize();
    }

    // To further prevent from traversal attacks sample opens the file to analyze its path without
    // directly opening it.
    File file = new File(csvFilePath);
    String canonicalPath = "";
    try {
      canonicalPath = file.getCanonicalPath();
    } catch (IOException e) {
      // Handle IOException if nonexistent file
      responseMap.put("Error", "Failed to open the desired file IO");
      responseMap.put("Route", this.relativePath);
      return new CSVFailureResponse(responseMap).serialize();
    }

    // TODO: Must be changed for each user running the code
    String absPath = "/Users/zach.stellato/Documents/Code/cs0320/server-zstellat-dviesca/data/";
    // if the given absolute path is not contained some traversal has taken place
    if (!canonicalPath.contains(absPath)) {
      responseMap.put("Error", "Malicious traversal of directory");
      responseMap.put("Route", this.relativePath);
      return new CSVFailureResponse(responseMap).serialize();
    }

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

    // This reads and parses the file
    try {
      Reader csvReader = new FileReader(csvFilePath);
      this.dataPacket =
          new Parser<List<String>, String>().parse(new StringRow(), csvReader, hasHeader);
      this.isLoaded = true; // Set to true if it gets here without throwing an exception
      responseMap.put("Success", "CSV File was parsed correctly");
      responseMap.put("File Path", this.relativePath);
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

  /**
   * Class used to serialize a success response for a CSV load operation
   *
   * @param responseType a String containing the response type, i.e. "success"
   * @param responseMap a Map of String, Object pairs containing important info about the process.
   */
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

  /**
   * Class used to serialize a failure response for a CSV load operation.
   *
   * @param responseType a String containing the response type, i.e. "error"
   * @param responseMap is a Map of String, Object pairs containing pertinent error information.
   */
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
