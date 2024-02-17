package edu.brown.cs.student.main.server.csvServer;

import edu.brown.cs.student.main.csv.csvoperations.ParsedDataPacket;
import org.slf4j.Logger;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.List;

public class ViewHandler implements Route {
    private static Logger LOGGER;
    private ParsedDataPacket<List<String>, String> dataPacket;

    public ViewHandler(Logger logger, LoadHandler loadHandler) {
        LOGGER = logger;
    }

    //TODO
    @Override
    public Object handle(Request request, Response response) throws Exception {
        return null;
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




