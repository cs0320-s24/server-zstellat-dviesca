package edu.brown.cs.student.main.csv.csvoperations.rowoperations;

import edu.brown.cs.student.main.csv.csvoperations.exceptions.FactoryFailureException;
import java.util.List;

/**
 * This interface defines a method that allows your CSV parser to convert each row into an object of
 * some arbitrary passed type. It also defines two methods with slightly different parameters that
 * allow the CSV searcher to search each row of some arbitrary passed type -T- in a way specific to
 * that searchObject of data type -J-. -J- is the type of data that the user is searching to match.
 * To avoid errors, ensure the search function correctly utilizes type -J- (e.g. use .equals for
 * strings and for integers but not when trying to compare an object inside a list to a list. Should
 * search the list by iterating through it and comparing each object). NOTE: if search functionality
 * is not necessary, -J- parameter can be any type (e.g. -Object-)
 */
public interface RowOperator<T, J> {

  T create(List<String> row) throws FactoryFailureException;

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
  T searchRow(T rowToCheck, J searchObject, int searchIndex)
      throws FactoryFailureException, IllegalArgumentException;

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
  T searchRow(T rowToCheck, J searchObject) throws IllegalArgumentException;
}
