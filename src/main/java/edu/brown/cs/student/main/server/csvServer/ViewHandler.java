package edu.brown.cs.student.main.server.csvServer;

import edu.brown.cs.student.main.csv.csvoperations.ParsedDataPacket;
import org.slf4j.Logger;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.List;

public class ViewHandler implements Route {
    private static Logger LOGGER;
    private final LoadHandler loadHandler;

    public ViewHandler(Logger logger, LoadHandler loadHandler) {
        LOGGER = logger;
        this.loadHandler = loadHandler;
    }

    //TODO
    @Override
    public Object handle(Request request, Response response) throws Exception {


        return null;
    }

    private void viewCSV() throws RuntimeException {
        // This ensures that a csvFile has already been loaded
        if (!this.loadHandler.getIsLoaded()) {
            // TODO: Figure out logging errors. This is copied from the missive -->
            //  USER STORY 1: using `viewcsv` or `searchcsv` CSV queries without a CSV
            //  loaded must produce an error API response, but not halt the server. (See the API
            //  specification your server must follow below.)
            throw new RuntimeException("Error encountered while viewing CSV file: "
                + "CSV file not loaded. Must call 'loadcsv' prior to calling 'viewcsv'");
        }



    }

}





