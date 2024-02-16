package edu.brown.cs.student.main.csv.csvoperations;

import edu.brown.cs.student.main.csv.csvoperations.rowoperations.RowOperator;
import java.util.ArrayList;
import java.util.List;

/**
 * Record that contains a bunch of info outputted after a csv is parsed
 * @param rowType an object of type RowOperator
 * @param parsedRows a List of type -T- that contains all the rows of the CSV file (excluding
 *     headers)
 * @param containsHeader true if user said the csv file has headers, false otherwise
 * @param headers an arraylist of Strings with the headers in it (empty if containsHeader is false)
 * @param <T> The inputted type that each row in the csv is converted into (returned by RowOperator)
 * @param <J> The inputted type that needs to be used to search inside the row objects of type -T-
 */
public record ParsedDataPacket<T, J>(
    RowOperator<T, J> rowType,
    List<T> parsedRows,
    boolean containsHeader,
    ArrayList<String> headers) {}
