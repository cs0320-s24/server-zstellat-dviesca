package edu.brown.cs.student.main.server.csvServer;

import edu.brown.cs.student.main.csv.csvoperations.ParsedDataPacket;
import edu.brown.cs.student.main.csv.csvoperations.Parser;
import edu.brown.cs.student.main.csv.csvoperations.exceptions.FactoryFailureException;
import edu.brown.cs.student.main.csv.csvoperations.rowoperations.StringRow;
import org.slf4j.Logger;
import spark.Request;
import spark.Response;
import spark.Route;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
     * @param logger the log files
     */
    public LoadHandler(Logger logger) {
        LOGGER = logger;
        this.isLoaded = false;
        this.relativePath = "No File Path Specified";
    }

    /**
     * Getter method for the boolean is loaded. Used to check if a file is loaded yet.
     * @return true if the file has been successfully loaded, and false otherwise;
     */
    public boolean getIsLoaded() { return this.isLoaded; }

    /**
     * Getter method for the data packet parsed by
     * @return the data packet object contained in the loader class
     */
    public ParsedDataPacket<List<String>, String> getDataPacket() { return this.dataPacket; }


    //TODO
    /**
     * @param request the request passed by the frontend user
     * @param response a json containing data about the response.
     * @return
     * @throws Exception
     */
    @Override
    public Object handle(Request request, Response response) throws Exception {
        String route = request.queryParams("route");
        String hasHeaderString = request.queryParams("hasHeader");

        // This checks the header string
        boolean hasHeader = false;
        if (hasHeaderString.equalsIgnoreCase("true")) {
            hasHeader = true;
        } else if (!hasHeaderString.equalsIgnoreCase("false")) { // If not false and it already didn't say true, then do error.
            // If the string isn't "true" or "false", return an error
            //TODO: error message -->
            // ["Error: hasHeader argument (" + hasHeaderString + ") is invalid. Accepted values are: \"false\" or \"true.\""]
            System.out.println("ERROR BRUDDDAh");
        }




        try {
            this.loadCSV(route, hasHeader);
        } catch (RuntimeException e ) {
            // TODO: Figure out how to send errors
            //return new LoadFailureResponse(e).serialize();
        }

      return null;
    }
    private void loadCSV(String relativeFilePath, boolean containsHeaders) throws RuntimeException {
        this.relativePath = relativeFilePath;
        //to protect data on the computer the reader will look only within defined csvFilePath package
        String dataRootPath  = "/Users/zach.stellato/Documents/Code/cs0320/server-zstellat-dviesca/data/";
        String csvFilePath = dataRootPath + this.relativePath;

        try {
            Reader csvReader = new FileReader(csvFilePath);
            this.dataPacket = new Parser<List<String>,String>().parse(new StringRow(), csvReader, containsHeaders);
            this.isLoaded = true; // Set to true if it gets here without throwing an exception
        }
        // TODO: LOG THESE ERRORS
        catch (FileNotFoundException e) {
            throw new RuntimeException("Error: CSV file not found");
            //LOGGER.error //TODO ???
        } catch (IOException e) {
            throw new RuntimeException( "Error: An IO error occurred while trying to read the CSV file by line");
        } catch (FactoryFailureException e) {
            throw new RuntimeException(e.getMessage());
        }

        // TODO: +++++++ FOR TESTING +++++++++
        System.out.println("Just tried to load csv File path: (" + csvFilePath + ")");
        // TODO: +++++++ FOR TESTING +++++++++


    }

//    public record CSVNotFoundFailureResponse(String responseType) {
//        public CSVNotFoundFailureResponse() {
//            // TODO: Figure out how to pass [this.relativePath] into here
//            this("CSV file (" + this.relativePath + ")");
//        }
//
//        String Serialize() {
//            return null;
//        }
//    }

}
