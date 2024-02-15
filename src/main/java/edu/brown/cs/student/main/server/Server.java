package edu.brown.cs.student.main.server;

import static spark.Spark.after;

import edu.brown.cs.student.main.census.CSVHandler;
import edu.brown.cs.student.main.census.CensusAPIUtilities;
import edu.brown.cs.student.main.census.CensusData;
import edu.brown.cs.student.main.census.CensusHandler;
import spark.Spark;
import java.util.ArrayList;
import java.util.List;


public class Server {

  public static void main(String[] args) {
    new Server(args).run();
  }

  private Server(String[] args) {}

  private void run() {
    int port = 3232;
    Spark.port(port);

    // allow for any http requests to access the method
    after(
        (request, response) -> {
          response.header("Access-Control-Allow-Origin", "*");
          response.header("Access-Control-Allow-Methods", "*");
        });

    // Reading the JSON
    // TODO: modify from String menuAsJson = SoupAPIUtilities.readInJson("data/menu.json");
    // to include where the census data is coming from
    String filepath = "TODO"; // TODO: find a way so local and api gotten data can be hosted
    String dataAsJson = CensusAPIUtilities.readInJson(filepath); //TODO: find how to manage the
    List<CensusData> censusDataList =  new ArrayList<>();
    //deserializing
    try{
      censusDataList = CensusAPIUtilities.deserializeCensus(dataAsJson);
    } catch (Exception e){ //TODO manage the error more satisfactory as the handout/gearup says or as a log
      e.printStackTrace();
      System.err.println("Errored while deserializing the census data"); //TODO this is wrong
    }

    //setup the handlers for the GET of TODO might have to chane the name
    Spark.get("csvOperations", new CSVHandler());// TODO for the csvoperations case
    Spark.get("censusOperations", new CensusHandler());// TODO for the censusoperations case
    Spark.init();
    Spark.awaitInitialization();

    System.out.println("Server started at http://localhost:" + port);
  }
}
