package edu.brown.cs.student.main.server.censusServer;

/**
 * This is a class that models a CensusData received from the CensusPlace. It doesn't have a lot but
 * there are a few fields that you could filter on if you wanted!
 */
public class CensusData {
  private String NAME;
  private String state;
  private String county;

  public CensusData() {}

  // TODO: Make this actually mean something, this is just testing rn
  @Override
  public String toString() {
    return this.NAME + " has StateCode: " + this.state + " and CountyCode: " + this.county;
  }
}
