package edu.brown.cs.student.main.csv.star;

import static java.lang.Integer.parseInt;

import edu.brown.cs.student.main.csv.csvoperations.exceptions.FactoryFailureException;
import edu.brown.cs.student.main.csv.csvoperations.rowoperations.RowOperator;
import java.util.List;

/**
 * This method implements the RowOperator interface for the specification of creating and searching
 * though Star Objects from the stardata.csv file.
 */
public class StarRow implements RowOperator<Star, Star> {

  public StarRow() {}

  @Override
  public Star create(List<String> row) throws FactoryFailureException {
    Star star;
    try {
      star =
          new Star(
              parseInt(row.get(0)),
              row.get(1),
              parseInt(row.get(2)),
              parseInt(row.get(3)),
              parseInt(row.get(4)));
    } catch (NumberFormatException e) {
      throw new FactoryFailureException("Doesnt work");
    }
    return star;
  }

  @Override
  public Star searchRow(Star rowToCheck, Star searchObject, int searchIndex)
      throws FactoryFailureException, IllegalArgumentException {

    throw new FactoryFailureException("Not possible to search by column for star data");
  }

  @Override
  public Star searchRow(Star rowToCheck, Star searchObject) throws IllegalArgumentException {
    if (rowToCheck.equals(searchObject)) {
      return rowToCheck;
    } else {
      return null;
    }
  }
}
