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

    private String relativePath;

    private String absolutePath;


    /**
     * Constructor for the
     * @param logger the log files
     */
    public LoadHandler(Logger logger) {
        LOGGER = logger;
        this.isLoaded = false;
    }
"/Users/domingojr/IdeaProjects/server-zstellat-dviesca/data/RI City & Town Income from American Community Survey 5-Year Estimates Source_ US Census Bureau, 2017-2021 American Community Survey 5-Year Estimates 2017-2021 - Sheet1.csv"
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
        String hasHeaderString = request.queryParams("hasHeader"); //TODO do we expect a number(1/0) or string?
        //TODO assumes it is "true" or "false", PENDING: error checking
        Boolean hasHeader = Boolean.parseBoolean(hasHeaderString);

        try {
            this.loadCSV(route, hasHeader);
        } catch (RuntimeException e ) {
            return new LoadFailureResponse(e).serialize();
        }


    }
    private void loadCSV(String relativeFilePath, boolean containsHeaders) throws RuntimeException {
        //to protect data on the computer the reader will look only within defined csvFilePath package
        String dataRootPath  = "/Users/zach.stellato/Documents/Code/cs0320/server-zstellat-dviesca/data/";
        String csvFilePath = dataRootPath + relativeFilePath;

        try {
            Reader csvReader = new FileReader(csvFilePath);
            this.dataPacket = new Parser<List<String>,String>().parse(new StringRow(), csvReader, containsHeaders);
            this.isLoaded = true;
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

    public record CSVNotFoundFailureResponse(String responseType) {
        public CSVNotFoundFailureResponse() {
            this("CSV file (" + this.relativePath + );
        }

        String Serialize() {

        }
    }





}
