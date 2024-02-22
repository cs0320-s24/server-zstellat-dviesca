package edu.brown.cs.student.main.server.censusServer;

import com.squareup.moshi.Json;

/**
 * This is a class that models a CensusData received from the 201 census. It doesn't have a lot but
 * there are a few fields that you could filter on if you wanted!
 */
public record CensusData(String NAME, String S2802_C03_022E, String state, String county) { }
//  @Json(name = "NAME")
//  private String NAME;
//
//  @Json(name = "state")
//  private String state;
//
//  @Json(name = "county")
//  private String county;
//
//  @Json(name = "S2802_C03_022E")
//  private String S2802_C03_022E;

 // public CensusData(String NAME, String S2802_C03_022E, String state, String county) {
 // }

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
//
//  public String getCoverage() {
//    return this.S2802_C03_022E;
//  }

