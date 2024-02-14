package edu.brown.cs.student.main.csvUtilities.csvOperations.RowOperatorTypes;

import edu.brown.cs.student.main.csvUtilities.csvOperations.Exceptions.FactoryFailureException;
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
   * @throws FactoryFailureException Implement this method by throwing FactoryFailureException if
   *     the arbitrary type -T- is not compatible with searching by index (e.g. only one object per
   *     row).
   * @throws IllegalArgumentException if the passed types -T- or -J- are not compatible with the
   *     searchRow functionality
   */
  @Override
  public List<String> searchRow(List<String> rowToCheck, String searchObject, int searchIndex)
      throws FactoryFailureException, IllegalArgumentException {
    try {
      if (rowToCheck.get(searchIndex).equals(searchObject)) {
        return rowToCheck;
      } else {
        return null;
      }
    } catch (IndexOutOfBoundsException e) {
      throw new FactoryFailureException(
          "Error: SearchObject type not compatible "
              + "with searching by column or request index not available in this row: "
              + rowToCheck);
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
