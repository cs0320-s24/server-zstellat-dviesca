package edu.brown.cs.student.main.csv.csvoperations.exceptions;

/**
 * This is an error intended to restart the loop in the main class so the user can be queried again.
 */
public class RestartLoopException extends Exception {

  public RestartLoopException(String message) {
    super(message);
  }
}
