package edu.brown.cs.student.main.server.censusServer;

import com.squareup.moshi.Json;

/**
 * This is a class that models a CensusData received from the 201 census. It doesn't have a lot but
 * there are a few fields that you could filter on if you wanted!
 */
public record CountyCodeData(String NAME, String state, String county) {}
//  @Json(name = "\"NAME\"")
//  private String NAME;
//
//  @Json(name = "\"state\"")
//  private String state;
//
//  @Json(name = "\"county\"")
//  private String county;

 // public record CountyCodeData(String NAME, String state, String county) {
//    this.NAME = NAME;
//    this.state = state;
//    this.county = county;
//   }
//  @Override
//  public String toString() {
//    return this.NAME + " has StateCode: " + this.state + " and CountyCode: " + this.county;
//  }
//
//  public String getNAME() {
//    return this.NAME;
//  }
//
//  public String getState() {
//    return this.state;
//  }
//
//  public String getCounty() {
//    return this.county;
//  }

