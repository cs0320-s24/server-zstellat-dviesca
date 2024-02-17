package edu.brown.cs.student.main.csv.csvoperations;

import edu.brown.cs.student.main.csv.csvoperations.exceptions.FactoryFailureException;
import java.util.ArrayList;
import java.util.List;

/**
 * I build the class with two constructors that each call the same helper method. I did this so the
 * user could input or not input an argument for the searchColumn and the searcher will accept it
 * either way. It turns the presence of the boolean into an argument that can be passed into a
 * private helper method that can actually do the search. I know this is slightly convoluted, but I
 * wanted to maximize the functionality of the code.
 *
 * @param <T> is a generic type parameter
 */
public class Searcher<T, J> {

  private J searchObject;
  private ParsedDataPacket<T, J> dataPacket;
  private String columnIdentifier;
  private boolean isIndex;
  private Integer indexIdentifier;

  /**
   * This method is called if a searchColumn argument is passed.This method calls the SearchHelper
   * method with a true hasColumnArgument only if the searchColumn is contained within the headers()
   * ArrayList within the dataPacket.
   *
   * @param dataPacket a Record of multiple data types returned by the Parser class
   * @param searchObject is a generic Object type in order to accommodate multiple types of values
   *                     that are searchable (e.g. by Star, by StringRow).
   * @param columnIdentifier is a String argument passed by the user indicating a specific column to
   *                         search through. Searches by index number if the String is parseable as
   *                         an integer. NOTE: only the first matching column header found is used.
   * @return an ArrayList of generic type -T- containing the rows that match the search parameters.
   *     If no matches are found, returns an empty ArrayList of type -T-
   */
  public List<T> search(ParsedDataPacket<T, J> dataPacket, J searchObject, String columnIdentifier)
      throws FactoryFailureException {
    this.dataPacket = dataPacket;
    this.searchObject = searchObject;
    this.columnIdentifier = columnIdentifier;
    this.isIndex = false;

    // Only calls the SearchHelper method with a columnArgument if the column identifier is a
    // parseable integer or there is a header with a matching string as the identifier.
    try {
      this.indexIdentifier = Integer.parseInt(this.columnIdentifier);
      this.isIndex = true; // Lets the helper know that the identifier is an int.
      return this.SearchHelper(true); // true b/c valid searchColumn exists
    } catch (NumberFormatException e) {
      if (this.dataPacket.headers().contains(this.columnIdentifier)) {
        return this.SearchHelper(true); // true b/c valid searchColumn exists
      } else {
        // TODO: ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        //  Maybe we should return an error rather than just searching all the columns if the input
        //  was bad.
        //  +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        System.err.println("Search by column failed. Searching all columns...");
        return this.SearchHelper(false); // false b/c NO valid searchColumn exists
      }
    }
  }

  /**
   * This method is called if no columnIdentifier argument is passed. This method calls the
   * SearchHelper method with a false hasColumnArgument.
   *
   * @param dataPacket a Record of multiple data types returned by the Parser class
   * @param searchObject is a generic Object type in order to accommodate multiple types of values
   *     that are searchable (e.g. by Star, by StringRow).
   * @return an ArrayList of generic type -T- containing the rows that match the search parameters.
   *     If no matches are found, returns an empty ArrayList of type -T-.
   */
  public List<T> search(ParsedDataPacket<T, J> dataPacket, J searchObject)
      throws FactoryFailureException {
    this.dataPacket = dataPacket;
    this.searchObject = searchObject;

    // False because a parameter for the searchColumn wasn't passed
    return SearchHelper(false);

  }

  /**
   * This method is called by the search method. It calls the search method from the RowOperator
   * implementation. If a row is returned, meaning there is a matching object, it adds that row to a
   * list which returns at the end.
   *
   * @param hasColumnArgument true if a valid column argument was passed into the search method, and
   *                          false otherwise.
   * @return the List of objects of RowOperator type that match the search parameters.
   * @throws FactoryFailureException if an error is encountered in the Row Operator implementation
   * of searchRow.
   */
  private List<T> SearchHelper(boolean hasColumnArgument) throws FactoryFailureException {

    // Note: Only the first occurrence of a matching column is used
    int columnIndex = -1;
    if (hasColumnArgument) {
      if (this.isIndex) { // If an index was given as an identifier, sets the column index to that
        columnIndex = this.indexIdentifier;
      } else { // If an integer index wasn't given, column index is set to index of matching header
        columnIndex = this.dataPacket.headers().indexOf(this.columnIdentifier);
      }
    }

    List<T> matchingRows = new ArrayList<>();
    T response;
    T currentRow = null;
    try {
      // Calls the search method from RowOperator. Returns the row object if it contains a match
      // and null if it doesn't.
      for (T row : this.dataPacket.parsedRows()) {
        currentRow = row; // Set so it can be outputted in error message
        // Only checks by column if a valid column index was found
        if (columnIndex != -1) {
          response = this.dataPacket.rowType().searchRow(row, this.searchObject, columnIndex);
        } else {
          response = this.dataPacket.rowType().searchRow(row, this.searchObject);
        }
        if (response != null) {
          matchingRows.add(response);
        }
      }
    } catch (FactoryFailureException e) {
      throw new FactoryFailureException(
          "Error: Generic type <T> does not support search by column");
    } catch (IllegalArgumentException e) {
      throw new FactoryFailureException(
          "Error: Type arguments for <T> and <J> are not compatible with the searchRow "
              + "functionality implemented by the RowOperator class");
    } catch (IndexOutOfBoundsException e) {
      throw new FactoryFailureException(
          "Error: SearchObject type not compatible with searching by column or request index: [" +
              columnIndex + "] not available in this row: " + currentRow);
    }
    return matchingRows;
  }
}
