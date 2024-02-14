package edu.brown.cs.student.main.server;

import spark.Spark;

public class Server {


  public static void main(String[] args) {
    int port = 3232;
    Spark.port(port);

  }
}
