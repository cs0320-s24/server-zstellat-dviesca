package edu.brown.cs.student.main.server;

import static spark.Spark.after;

import edu.brown.cs.student.main.csv.csvoperations.ParsedDataPacket;
import edu.brown.cs.student.main.server.censusServer.CensusAPIUtilities;
import edu.brown.cs.student.main.server.censusServer.CensusData;
import edu.brown.cs.student.main.server.censusServer.CensusHandler;
import java.util.ArrayList;
import java.util.List;

import edu.brown.cs.student.main.server.csvServer.LoadHandler;
import edu.brown.cs.student.main.server.csvServer.SearchHandler;
import edu.brown.cs.student.main.server.csvServer.ViewHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Spark;


public class Server {

  public static void main(String[] args) {
    new Server(args).run();
  }

  private Server(String[] args) {}

  private static final Logger LOGGER = LoggerFactory.getLogger(Server.class);

  private void run() {
    int port = 3232;
    Spark.port(port);

    // allow for any http requests to access the method
    after(
        (request, response) -> {
          response.header("Access-Control-Allow-Origin", "*");
          response.header("Access-Control-Allow-Methods", "*");
        });


    //TODO I THINK THIS IS ALL NOW IRRELEVANT WITH DEPENDENCY INJECTION AND THE NEW HANDLERS
//    Reading the JSON
//    ODO: modify from String menuAsJson = SoupAPIUtilities.readInJson("data/menu.json");
//    to include where the census data is coming from
//    String filepath = ; // ODO: find a way so local and api gotten data can be hosted
//    String dataAsJson = CensusAPIUtilities.readInJson(filepath); // ODO: find how to manage the
//    List<CensusData> censusDataList = new ArrayList<>();
//    // deserializing
//    try {
//      censusDataList = CensusAPIUtilities.deserializeCensus(dataAsJson);
//    } catch (
//        Exception
//            e) { // ODO manage the error more satisfactory as the handout/gearup says or as a log
//      e.printStackTrace();
//      System.err.println("Errored while deserializing the census data"); // ODO this is wrong
//    }

    //TODO is starting as null ok? could have a constructor that makes it just have a boolean as false
    LoadHandler loadHandler = new LoadHandler(LOGGER);


    // setup the handlers for the GET of TODO might have to chane the name
    Spark.get("loadcsv", loadHandler); // TODO for the csvoperations case
    Spark.get("viewcsv", new ViewHandler(LOGGER, loadHandler)); // TODO for the csvoperations case
    Spark.get("searchcsv", new SearchHandler(LOGGER, loadHandler)); // TODO for the csvoperations case
    //TODO question: is this all within the same "level" as the csv "streets" or is it in a different "category/route"
    Spark.get("censusOperations", new CensusHandler()); // TODO for the censusoperations case

    Spark.init();
    Spark.awaitInitialization();
    System.out.println("Server started at http://localhost:" + port);

  }
}
