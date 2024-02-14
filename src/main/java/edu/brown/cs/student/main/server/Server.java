package edu.brown.cs.student.main.server;

import spark.Spark;
import static spark.Spark.after;
import java.util.ArrayList;
import java.util.List;
import spark.Spark;

public class Server {


  public static void main(String[] args) {
    int port = 3232;
    Spark.port(port);
//
//    // allow for any http requests to access the method
//    after(
//        (request, response) -> {
//          response.header("Access-Control-Allow-Origin", "*");
//          response.header("Access-Control-Allow-Methods", "*");
//        });
//
//    // Reading the JSON
//    // TODO: modify from String menuAsJson = SoupAPIUtilities.readInJson("data/menu.json");
//    String dataAsJson =
//    //deserializing
//    try{
//      //TODO create a deserialoize X DATA method
//
//      dataAsJson =
//    }


  }
}
