package edu.brown.cs.student.csvOperations.RowOperatorTypes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import edu.brown.cs.student.main.csvOperations.Exceptions.FactoryFailureException;
import edu.brown.cs.student.main.csvOperations.ParsedDataPacket;
import edu.brown.cs.student.main.csvOperations.Parser;
import edu.brown.cs.student.main.csvOperations.RowOperatorTypes.IntegerRow;
import edu.brown.cs.student.main.csvOperations.Searcher;
import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

public class IntegerRowTest {

  // Test basic parse functionality
  @Test
  void testParse() throws FactoryFailureException, IOException {
    String csvContent = "5,25,2\n1, 30, 3";
    Parser<List<Integer>, Integer> parser = new Parser<>();

    ParsedDataPacket<List<Integer>, Integer> result =
        parser.parse(new IntegerRow(), new StringReader(csvContent), false);

    assertEquals(
        Arrays.asList(Arrays.asList(5, 25, 2), (Arrays.asList(1, 30, 3))), result.parsedRows());
  }

  // Test basic search functionality
  @Test
  void testSearch() throws FactoryFailureException, IOException {
    String csvContent = "Short,Long,LessLong\n5,25,2\n1, 30, 3";
    Parser<List<Integer>, Integer> parser = new Parser<>();
    Searcher<List<Integer>, Integer> searcher = new Searcher<>();

    ParsedDataPacket<List<Integer>, Integer> packet =
        parser.parse(new IntegerRow(), new StringReader(csvContent), true);

    List<List<Integer>> resultColumnTrue = searcher.search(packet, 25, "Long");
    List<List<Integer>> resultColumnFalse = searcher.search(packet, 25, "Short");

    assertEquals("[[5, 25, 2]]", resultColumnTrue.toString());
    assertEquals("[]", resultColumnFalse.toString());
  }
  // Test if it works given a string that cannot be turned into an integer
  @Test
  void testIncompatibleCSV() throws FactoryFailureException, IOException {
    String csvContent = "5,25,2\n1, 30, NotNumberAtAll";
    Parser<List<Integer>, Integer> parser = new Parser<>();

    Exception exception =
        assertThrows(
            FactoryFailureException.class,
            () -> {
              ParsedDataPacket<List<Integer>, Integer> result =
                  parser.parse(new IntegerRow(), new StringReader(csvContent), false);
            });

    String expectedMessage =
        ("Error: RowOperator failed to parse this row: "
            + Arrays.asList("1", "30", "NotNumberAtAll"));
    String actualMessage = exception.getMessage();

    assertEquals(expectedMessage, actualMessage);
  }
}
