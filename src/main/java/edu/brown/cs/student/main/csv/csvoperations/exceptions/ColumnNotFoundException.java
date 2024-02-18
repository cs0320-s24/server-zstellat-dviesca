package edu.brown.cs.student.main.csv.csvoperations.exceptions;

/**
 * This is an error that is thrown when the user tries to search by a column name but the column name isn't found
 * in the headers for the csv.
 */
public class ColumnNotFoundException extends Exception {

    public ColumnNotFoundException(String message) {
        super(message);
    }
}
