package edu.brown.cs.student.main.csv.csvoperations.rowoperations;

import static java.lang.Integer.parseInt;

import edu.brown.cs.student.main.csv.csvoperations.exceptions.FactoryFailureException;
import java.util.ArrayList;
import java.util.List;

/** This is a implementation of RowOperator that creates a list of integers in each row */
public class IntegerRow implements RowOperator<List<Integer>, Integer> {

  /**
   * Constructor for a row of Strings. This is so you can pass what object type is desired into the
   * parser.
   */
  public IntegerRow() {}

  @Override
  public List<Integer> create(List<String> row) throws FactoryFailureException {
    List<Integer> intList = new ArrayList<>();
    // TODO: ++++++++++++++ TESTING +++++++++++++++++
    System.out.println("Row to check: (" + row + ")");
    try {
      for (String s : row) {
        // TODO: ++++++++++++++ TESTING +++++++++++++++++
        System.out.println("String: (" + s + ")");
        intList.add(parseInt(s));
      }
    } catch (NumberFormatException e) {
      throw new FactoryFailureException("Error: RowOperator failed to parse this row: " + row);
    }
    return intList;
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
   */
  @Override
  public List<Integer> searchRow(List<Integer> rowToCheck, Integer searchObject, int searchIndex) {
    if (searchObject.equals(rowToCheck.get(searchIndex))) {
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
   *     searchRow functionality
   */
  @Override
  public List<Integer> searchRow(List<Integer> rowToCheck, Integer searchObject) {

    for (int i : rowToCheck) {
      if (searchObject.equals(i)) {
        return rowToCheck;
      }
    }
    return null;
  }
}
