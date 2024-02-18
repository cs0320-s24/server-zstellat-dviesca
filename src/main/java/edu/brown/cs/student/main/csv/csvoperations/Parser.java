package edu.brown.cs.student.main.csv.csvoperations;

import edu.brown.cs.student.main.csv.csvoperations.exceptions.FactoryFailureException;
import edu.brown.cs.student.main.csv.csvoperations.rowoperations.RowOperator;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @param <T> is the type that the user wants to convert their rows into using an implementation of
 *     RowOperator.
 * @param <J> the type that the user will search by if using search functionality. NOTE: if search
 *     functionality is not necessary, any type can be inputted (e.g. -Object-)
 */
public class Parser<T, J> {

  // A simple compiler given by the cs320 gods, removes commas but not if they're in quotes
  static final Pattern regexSplitCSVRow =
      Pattern.compile("\\s*,\\s*(?=([^\"]*\"[^\"]*\")*(?![^\"]*\"))");

  /**
   * This method takes a CSV file in the reader form and converts it into a list of objects of type
   * -T-. It also splits any headers from the document. It then stores all these data in a
   * ParsedDataPacket.java Record and returns the Record.
   *
   * @param rowType is an object of an implementation of the RowOperator interface
   * @param reader an object of type Reader. Convert the CSV file into a Reader type before passing
   *     it into the parser.
   * @param containsHeaders bool indicating whether or not the file contains headers.
   * @return a data packet object of ParsedDataPacket Record containing the parsed rows and headers
   * @throws FactoryFailureException if errors occur when using RowOperator interface to convert
   *     strings to an object of type -T-.
   * @throws IOException if an IO error occurs when trying to read the buffReader with readLine()
   *     method.
   */
  public ParsedDataPacket<T, J> parse(
      RowOperator<T, J> rowType, Reader reader, boolean containsHeaders)
      throws FactoryFailureException, IOException {

    // Creates variables to hold data about the parsed csv
    List<T> parsedRows = new ArrayList<>();
    ArrayList<String> headers = new ArrayList<>();
    String line;

    // Turns abstract reader into a form that can be read through line by line with readLine()
    BufferedReader buffReader = new BufferedReader(reader);

    // Separates the headers from the rest of the csv data if they are present by reading
    // the first line
    if (containsHeaders) {
      // Ensures that the first line isn't worked on if it's empty/null
      if ((line = buffReader.readLine()) != null) {
        headers = new ArrayList<>(Arrays.asList(regexSplitCSVRow.split(line)));
      }
    }

    // reads each line and converts them into rowOperator types until no rows are left
    while ((line = buffReader.readLine()) != null) {
      // Splits the string of each row, removes spaces, and splits it by ","
      List<String> row = Arrays.asList(regexSplitCSVRow.split(line));

      // Loops through each row and gets rid of unnecessary quotations if they exist
      for(int i = 0; i < row.size(); i++) {
        String ogString = row.get(i);
        if (ogString.length()>1 && ogString.startsWith("\"") && ogString.endsWith("\"")) {
          String newString = ogString.substring(1, ogString.length() - 1);
          row.set(i, newString);
        }
      }

      // Adds the converted row object to the list of parsed rows
      parsedRows.add(rowType.create(row));
    }
    // Creates a data packet and returns it to the caller. Note: headers may be empty
    return new ParsedDataPacket<>(rowType, parsedRows, containsHeaders, headers);
  }
}
