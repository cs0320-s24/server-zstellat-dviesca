package edu.brown.cs.student.main.server.csvServer;

import edu.brown.cs.student.main.csv.csvoperations.ParsedDataPacket;
import org.slf4j.Logger;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.List;

public class SearchHandler implements Route {
    private static Logger LOGGER;
    private ParsedDataPacket<List<String>, String> dataPacket;

    public SearchHandler(Logger logger, LoadHandler loadHandler) {
        LOGGER = logger;
    }

    //TODO
    @Override
    public Object handle(Request request, Response response) throws Exception {
        return null;
    }



}
