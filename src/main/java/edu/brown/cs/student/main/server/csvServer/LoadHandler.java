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
    private ParsedDataPacket<List<String>, String> dataPacket; //TODO why the comma?

    public LoadHandler(Logger logger, ParsedDataPacket dataDependency) {
        LOGGER = logger;
    }

    //TODO
    /**
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Override
    public Object handle(Request request, Response response) throws Exception {
        Set<String> params = request.queryParams();
        String  = request.queryParams(); //TODO I dont know what this actually is
        // Creates a hashmap to store the results of the request
        Map<String, Object> responseMap = new HashMap<>();


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
            LOGGER.error //TODO ???
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

}
