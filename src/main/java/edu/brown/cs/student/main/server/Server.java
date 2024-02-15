package edu.brown.cs.student.main.server;

import static spark.Spark.after;

import spark.Spark;

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

    //    // Reading the JSON
    //    // TODO: modify from String menuAsJson = SoupAPIUtilities.readInJson("data/menu.json");
    //    // to include where the census data is coming from
    //    String filepath =
    //    String dataAsJson = CensusAPIUtilities.readInJson(); //TODO: find how to manage the
    // filepath
    //    //deserializing
    //    try{
    //      //TODO create a deserialoize X DATA method
    //
    //      String dataAsJson = CensusAPIUtilities.readInJson(())
    //    }

  }
}
