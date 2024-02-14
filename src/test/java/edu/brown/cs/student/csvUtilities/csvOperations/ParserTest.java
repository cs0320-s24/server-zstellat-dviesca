package edu.brown.cs.student.csvUtilities.csvOperations;

import static org.junit.jupiter.api.Assertions.*;

import edu.brown.cs.student.main.csvUtilities.csvOperations.Exceptions.FactoryFailureException;
import edu.brown.cs.student.main.csvUtilities.csvOperations.ParsedDataPacket;
import edu.brown.cs.student.main.csvUtilities.csvOperations.Parser;
import edu.brown.cs.student.main.csvUtilities.csvOperations.RowOperatorTypes.IntegerRow;
import edu.brown.cs.student.main.csvUtilities.csvOperations.RowOperatorTypes.StringRow;
import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * This Test class was created by @author with help from chatGPT. I told it the parameters my Parser
 * class and gave examples of the expected outputs and exceptions. I used it's test format but redid
 * most of the tests to fit the actual user cases better.
 */
class ParserTest {
  private String csvContentHeader;
  private String csvContentNoHeader;

  @BeforeEach
  void setUp() {
    this.csvContentHeader = "Name,Age,Location\nJohn,25,New York\nJane,30,San Francisco";
    this.csvContentNoHeader = "John,25,New York\nJane,30,San Francisco";
  }

  // Tests if it parses the data correctly with headers
  @Test
  void testParseWithHeaders() throws FactoryFailureException, IOException {
    Parser<List<String>, String> parser = new Parser<>();
    ParsedDataPacket<List<String>, String> packet =
        parser.parse(new StringRow(), new StringReader(this.csvContentHeader), true);

    assertTrue(packet.containsHeader());
    assertEquals(Arrays.asList("Name", "Age", "Location"), packet.headers());
    assertEquals(2, packet.parsedRows().size());
    assertEquals(Arrays.asList("John", "25", "New York"), packet.parsedRows().get(0));
  }

  // Tests if it parses the data correctly without headers
  @Test
  void testParseWithoutHeaders() throws FactoryFailureException, IOException {
    Parser<List<String>, String> parser = new Parser<>();
    ParsedDataPacket<List<String>, String> packet =
        parser.parse(new StringRow(), new StringReader(this.csvContentNoHeader), false);

    assertTrue(packet.headers().isEmpty());
    assertEquals(2, packet.parsedRows().size());
  }

  // Tests if it parses the data correctly with if given an empty csv
  @Test
  void testParseEmptyCSV() throws FactoryFailureException, IOException {
    String csvContent = "";

    Parser<List<String>, String> parser = new Parser<>();
    ParsedDataPacket<List<String>, String> packet =
        parser.parse(new StringRow(), new StringReader(csvContent), true);

    assertEquals(0, packet.parsedRows().size());
  }

  // Tests if it returns the correct error if the data doesn't work with the RowOperator converter
  @Test
  void testParseIncompatibleRowOperator() throws FactoryFailureException, IOException {
    Parser<List<Integer>, Integer> parser = new Parser<>();

    Exception exception =
        assertThrows(
            FactoryFailureException.class,
            () -> {
              ParsedDataPacket<List<Integer>, Integer> packet =
                  parser.parse(new IntegerRow(), new StringReader(this.csvContentNoHeader), false);
            });

    String expectedMessage =
        ("Error: RowOperator failed to parse this row: " + Arrays.asList("John", "25", "New York"));
    String actualMessage = exception.getMessage();

    assertEquals(expectedMessage, actualMessage);
  }
}
