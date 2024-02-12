package edu.brown.cs.student.csvOperations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import edu.brown.cs.student.main.csvOperations.Exceptions.FactoryFailureException;
import edu.brown.cs.student.main.csvOperations.ParsedDataPacket;
import edu.brown.cs.student.main.csvOperations.Parser;
import edu.brown.cs.student.main.csvOperations.RowOperatorTypes.RowOperator;
import edu.brown.cs.student.main.csvOperations.RowOperatorTypes.StringRow;
import edu.brown.cs.student.main.csvOperations.Searcher;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SearcherTest {

  private String csvContentHeader;
  private String csvContentNoHeader;

  @BeforeEach
  void setUp() {
    this.csvContentHeader = "Name,Age,Location\nJohn,25,New York\nJane,30,San Francisco";
    this.csvContentNoHeader = "John,25,New York\nJane,30,San Francisco";
  }

  // Tests if it searches the data correctly with headers and by column
  @Test
  void testParseWithHeaders() throws FactoryFailureException, IOException {
    Parser<List<String>, String> parser = new Parser<>();
    Searcher<List<String>, String> searcher = new Searcher<>();

    ParsedDataPacket<List<String>, String> packet =
        parser.parse(new StringRow(), new StringReader(this.csvContentHeader), true);

    List<List<String>> resultFound = searcher.search(packet, "New York", "Location");

    List<List<String>> resultNotFound = searcher.search(packet, "New York", "Age");

    assertEquals("[[John, 25, New York]]", resultFound.toString());
    assertEquals("[]", resultNotFound.toString());
  }

  // if CSV is empty
  @Test
  void testSearchWithEmptyCSV() throws FactoryFailureException, IOException {
    Parser<List<String>, String> parser = new Parser<>();
    Searcher<List<String>, String> searcher = new Searcher<>();

    ParsedDataPacket<List<String>, String> packet =
        parser.parse(new StringRow(), new StringReader(""), true);

    List<List<String>> result = searcher.search(packet, "New York", "Location");

    assertEquals("[]", result.toString());
  }

  // Tests if there is nothing at the specified column for that case
  @Test
  void testMissingDataInRow() throws FactoryFailureException, IOException {
    String csvContent = "Name,Age,Location\nJohn,25,New York\nJane,30";
    Searcher<List<String>, String> searcher = new Searcher<>();

    Parser<List<String>, String> parser = new Parser<>();
    ParsedDataPacket<List<String>, String> packet =
        parser.parse(new StringRow(), new StringReader(csvContent), true);
    Exception exception =
        assertThrows(
            FactoryFailureException.class,
            () -> {
              searcher.search(packet, "San Francisco", "Location");
            });

    String expectedMessage = ("Error: generic type <T> does not support search by column");
    String actualMessage = exception.getMessage();

    assertEquals(expectedMessage, actualMessage);
  }

  // Sample RowOperator for testing purposes
  private static class SampleRowOperatorNoColumn implements RowOperator<String, String> {

    @Override
    public String create(List<String> row) throws FactoryFailureException {
      // For simplicity, just concatenate the row values
      return String.join(",", row);
    }

    // This one tests if no index is accepted
    // TODO: +
    @Override
    public String searchRow(String rowToCheck, String searchObject, int searchIndex)
        throws FactoryFailureException, IllegalArgumentException {
      throw new FactoryFailureException(
          "Error: RowType doesn't have search by column "
              + "functionality. Row at failure: "
              + rowToCheck);
    }

    @Override
    public String searchRow(String rowToCheck, String searchObject)
        throws IllegalArgumentException {
      return null;
    }
  }

  // Test if it respects a RowOperator that doesn't allow search by column
  @Test
  void testSearchByColumnIncompatible() throws FactoryFailureException, IOException {
    Parser<String, String> parser = new Parser<>();
    Searcher<String, String> searcher = new Searcher<>();

    ParsedDataPacket<String, String> result =
        parser.parse(
            new SampleRowOperatorNoColumn(), new StringReader(this.csvContentHeader), true);

    Exception exception =
        assertThrows(
            FactoryFailureException.class,
            () -> {
              searcher.search(result, "Twenty", "Age");
            });

    String expectedMessage = ("Error: generic type <T> does not support search by column");
    String actualMessage = exception.getMessage();

    assertEquals(expectedMessage, actualMessage);
  }
}
