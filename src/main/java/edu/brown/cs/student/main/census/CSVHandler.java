package edu.brown.cs.student.main.census;

import edu.brown.cs.student.main.csv.csvoperations.ParsedDataPacket;
import edu.brown.cs.student.main.csv.csvoperations.Parser;
import edu.brown.cs.student.main.csv.csvoperations.exceptions.FactoryFailureException;
import edu.brown.cs.student.main.csv.csvoperations.rowoperations.StringRow;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;
import spark.Route;

public class CSVHandler implements Route {

  private static Logger LOGGER;
  private ParsedDataPacket<List<String>, String> dataPacket;

  public CSVHandler(Logger logger) {
    LOGGER = logger;
  }

  /**
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  @Override
  public Object handle(Request request, Response response) throws Exception {
    return null;
  }


  private void loadCSV(String relativeFilePath, boolean containsHeaders) throws RuntimeException {
    //to protect data on the computer the reader will look only within defined csvFilePath
    String dataRootPath  = "/Users/zach.stellato/Documents/Code/cs0320/server-zstellat-dviesca/data";
    String csvFilePath = dataRootPath + relativeFilePath;
    try {
      Reader csvReader = new FileReader(csvFilePath);
      this.dataPacket = new Parser<List<String>,String>().parse(new StringRow(), csvReader, containsHeaders);
    }
    // TODO: LOG THESE ERRORS
    catch (FileNotFoundException e) {
      throw new RuntimeException("Error: CSV file not found");
      LOGGER.error
    } catch (IOException e) {
      throw new RuntimeException(
          "Error: An IO error occurred while trying to read the" + "CSV file by line");
    } catch (FactoryFailureException e) {
      throw new RuntimeException(e.getMessage());
    }

    // TODO: +++++++ FOR TESTING +++++++++
    System.out.println("Just tried to load csv File path: (" + csvFilePath + ")");
    // TODO: +++++++ FOR TESTING +++++++++
  }

  // TODO: make sure load csv is called or this.
  private void viewCSV() {
    // This ensures that a csvFile has already been loaded
    if (this.dataPacket == null) {
      throw new RuntimeException("Error encountered while viewing CSV file: CSV file not loaded. "
          + "Must call 'loadcsv' prior to calling 'viewcsv'");
    }






  }
}



