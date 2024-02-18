package edu.brown.cs.student.main.csv.csvoperations.exceptions;

import java.util.HashMap;
import java.util.Map;

/**
 * This is an error provided to catch any error that may occur when you create an object from a row.
 * This is also called when the search can not be completed due to the potential lack of column
 * search functionality for some RowOperator implementations.
 */
public class FactoryFailureException extends Exception {

  private final Map<String, Object> responseMap;

  /**
   *  Error provided to catch any error that may occur when you create an object from a row.
   *  * This is also called when the search can not be completed due to the potential lack of column
   *  * search functionality for some RowOperator implementations.
   * @param message A string containing relevant info about error reason
   */
  public FactoryFailureException(String message) {
    super(message); // Exception message
    this.responseMap = new HashMap<>();
    this.responseMap.put("Error", message); // Adds the exception message it isn't already provided

  }

  /**
   * Error provided to catch any error that may occur when you create an object from a row.
   *  This is also called when the search can not be completed due to the potential lack of column
   *  search functionality for some RowOperator implementations.
   * @param responseMap a response map with info about the error and relevant data
   */
  public FactoryFailureException(Map<String, Object> responseMap) {
    this.responseMap = responseMap;
  }

  /**
   * Returns the response map provided (if any) as the root cause of
   * this exception.
   * @return a response map containing important info on the failure
   */
  public Map<String, Object> getResponseMap() { return this.responseMap; }
}
