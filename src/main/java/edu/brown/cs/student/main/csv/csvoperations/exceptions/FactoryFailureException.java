package edu.brown.cs.student.main.csv.csvoperations.exceptions;

/**
 * This is an error provided to catch any error that may occur when you create an object from a row.
 * This is also called when the search can not be completed due to the potential lack of column
 * search functionality for some RowOperator implementations.
 */
public class FactoryFailureException extends Exception {

  public FactoryFailureException(String message) {
    super(message);
  }
}
