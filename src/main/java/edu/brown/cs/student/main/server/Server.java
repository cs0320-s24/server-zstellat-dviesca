package edu.brown.cs.student.main.server;

import static spark.Spark.after;

import edu.brown.cs.student.main.server.censusServer.BroadbandHandler;
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

    // TODO I THINK THIS IS ALL NOW IRRELEVANT WITH DEPENDENCY INJECTION AND THE NEW HANDLERS
    //    Reading the JSON
    //    ODO: modify from String menuAsJson = SoupAPIUtilities.readInJson("data/menu.json"); //TODO
    // this will be the census
    //    to include where the census data is coming from
    //    String filepath = ; // ODO: find a way so local and api gotten data can be hosted
    //    String dataAsJson = CensusAPIUtilities.readInJson(filepath); // ODO: find how to manage
    // the
    //    List<CensusData> censusDataList = new ArrayList<>();
    //    // deserializing
    //    try {
    //      censusDataList = CensusAPIUtilities.deserializeCensus(dataAsJson);
    //    } catch (
    //        Exception
    //            e) { // ODO manage the error more satisfactory as the handout/gearup says or as a
    // log
    //      e.printStackTrace();
    //      System.err.println("Errored while deserializing the census data"); // ODO this is wrong
    //    }

    // TODO is starting as null ok? could have a constructor that makes it just have a boolean as
    // false
    LoadHandler loadHandler = new LoadHandler(LOGGER);

    // setup the handlers for the GET of TODO might have to chane the name
    Spark.get("loadcsv", loadHandler);
    Spark.get("viewcsv", new ViewHandler(LOGGER, loadHandler));
    Spark.get("searchcsv", new SearchHandler(LOGGER, loadHandler));
    Spark.get("broadband", new BroadbandHandler()); // TODO for the censusoperations case

    Spark.init();
    Spark.awaitInitialization();
    System.out.println(
        "edu.brown.cs.student.main.server.Server started at http://localhost:" + port);
  }
}
