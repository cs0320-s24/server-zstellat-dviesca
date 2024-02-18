package edu.brown.cs.student.main.csv.csvoperations.rowoperations;

import java.util.List;

public class StringRow implements RowOperator<List<String>, String> {

  /**
   * Constructor for a row of Strings. This is so you can pass what object type is desired into the
   * parser.
   */
  public StringRow() {}

  @Override
  public List<String> create(List<String> row) {
    return row;
  }

  /**
   * This method is called when a row of type -T- is searched. This version of the search method
   * contains a searchIndex parameter. It is split like this to better accommodate two different
   * search types.
   *
   * @param rowToCheck A generic object of type -T-. This is the form in which the rows were stored
   *     when parsed.
   * @param searchObject A generic object of type -J-, must be handled differently by each
   *     implementation of search.
   * @param searchIndex the index of the column to search through, as passed by the caller of the
   *     Search method and matched to a certain header.
   * @return rowToCheck if the row matches/contains the searchObject and null if it doesn't.
   * @throws IllegalArgumentException if the passed types -T- or -J- are not compatible with the
   *     searchRow functionality
   * @throws IndexOutOfBoundsException if the searchIndex is out of bounds.
   */
  @Override
  public List<String> searchRow(List<String> rowToCheck, String searchObject, int searchIndex)
      throws IllegalArgumentException, IndexOutOfBoundsException {
    if (rowToCheck.get(searchIndex).equals(searchObject)) {
      return rowToCheck;
    } else {
      return null;
    }
  }

  /**
   * This method is called when a row of type -T- is searched. This version of the search method
   * does not contain a searchIndex parameter. It is split like this to better accommodate two
   * different search types.
   *
   * @param rowToCheck A generic of type -T-. This is the form in which the rows were stored when
   *     parsed.
   * @param searchObject A generic of type -J-, must be handled differently by each implementation
   *     of search.
   * @return @rowToCheck if the row matches/contains the searchObject and null if it doesn't.
   * @throws IllegalArgumentException if the passed types -T- or -J- are not compatible with the
   *     search method.
   */
  @Override
  public List<String> searchRow(List<String> rowToCheck, String searchObject)
      throws IllegalArgumentException {
    try {
      if (rowToCheck.contains(searchObject)) {
        return rowToCheck;
      } else {
        return null;
      }
    } catch (ClassCastException e) {
      throw new IllegalArgumentException(
          "Error: type does not allow for .contains() method " + "to function");
    }
  }
}
