package edu.brown.cs.student.main;

import edu.brown.cs.student.main.csvOperations.Exceptions.FactoryFailureException;
import edu.brown.cs.student.main.csvOperations.Exceptions.RestartLoopException;
import edu.brown.cs.student.main.csvOperations.ParsedDataPacket;
import edu.brown.cs.student.main.csvOperations.Parser;
import edu.brown.cs.student.main.csvOperations.RowOperatorTypes.StringRow;
import edu.brown.cs.student.main.csvOperations.Searcher;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

/**
 * A helper utility for searching a csv with Strings and creating rows of List(String)
 *
 * @param <T> The type of object that is turned into rows. In this case, it should be List(String)
 */
public class csvFileUtility<T> {

  private String csvName;
  private boolean containsHeaders;
  private String searchValue;
  private String searchColumn;
  private ParsedDataPacket<List<String>, String> dataPacket;

  /**
   * This method helps to parse and search through csv files and prints a string with the rows that
   * contain a matching string in the indicated column to the one being searched for. This version
   * of the csvFileUtility is called when a search column is entered as an argument. NOTE: Only the
   * first column found with a matching header will be searched through. If duplicate headers exist,
   * it will only search through the first.
   *
   * @param csvName a String containing the name of the CSV file to read
   * @param containsHeaders true if the file contains headers and false if not
   * @param searchValue a String that this method is searching for within the CSV
   * @param searchColumn a String containing the name of the header of the column that should be
   *     searched through.
   * @throws RestartLoopException if any part of the program creates an error, this is sent after
   *     the error is described, and it contains the error message. Restarting the loop allows the
   *     user to re-input new arguments.
   */
  public csvFileUtility(
      String csvName, boolean containsHeaders, String searchValue, String searchColumn)
      throws RestartLoopException {
    this.csvName = csvName;
    this.containsHeaders = containsHeaders;
    this.searchValue = searchValue;
    this.searchColumn = searchColumn;

    this.ParseHelper();
    this.SearchHelper(true); // True, because there is a search column argument
  }

  /**
   * This method helps to parse and search through csv files and prints a string with the rows that
   * contain a matching string to the one being searched for. This version of the csvFileUtility is
   * called when a search column isn't entered as an argument.
   *
   * @param csvName a String containing the name of the CSV file to read
   * @param containsHeaders true if the file contains headers and false if not
   * @param searchValue a String that this method is searching for within the CSV
   * @throws RestartLoopException if any part of the program creates an error, this is sent after
   *     the error is described, and it contains the error message. Restarting the loop allows the
   *     user to re-input new arguments.
   */
  public csvFileUtility(String csvName, boolean containsHeaders, String searchValue)
      throws RestartLoopException {
    this.csvName = csvName;
    this.containsHeaders = containsHeaders;
    this.searchValue = searchValue;

    this.ParseHelper();
    this.SearchHelper(false); // False, because there isn't a search column argument
  }

  private void ParseHelper() throws RestartLoopException {
    try {
      FileReader reader = new FileReader(this.csvName);
      Parser<List<String>, String> parser = new Parser<>();
      this.dataPacket = parser.parse(new StringRow(), reader, this.containsHeaders);

    } catch (FileNotFoundException e) {
      throw new RestartLoopException("Error: CSV file not found");
    } catch (IOException e) {
      throw new RestartLoopException(
          "Error: An IO error occurred while trying to read the " + "CSV file by line");
    } catch (FactoryFailureException e) {
      throw new RestartLoopException(e.getMessage());
    }
  }

  /**
   * The helper method calls the search functionality from the search class and prints out the
   * result.
   *
   * @param hasSearchColumn true if a valid searchColumn was passed and false otherwise
   */
  private void SearchHelper(boolean hasSearchColumn) throws RestartLoopException {
    Searcher<List<String>, String> searcher = new Searcher<>();
    List<List<String>> result;

    try {
      if (hasSearchColumn) {
        result = searcher.search(this.dataPacket, this.searchValue, this.searchColumn);
      } else {
        result = searcher.search(this.dataPacket, this.searchValue);
      }
    } catch (IllegalArgumentException | FactoryFailureException e) {
      throw new RestartLoopException(e.getMessage());
    }

    // Print out search results
    if (result.isEmpty()) {
      System.out.println("The search returned no matches");
    } else {
      System.out.println("The search returned the following results: ");
      for (List<String> matchingRow : result) {
        System.out.println(matchingRow);
      }
    }
  }
}
