package edu.brown.cs.student.server;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.brown.cs.student.main.csv.csvoperations.ParsedDataPacket;
import edu.brown.cs.student.main.csv.csvoperations.Parser;
import edu.brown.cs.student.main.csv.csvoperations.exceptions.FactoryFailureException;
import edu.brown.cs.student.main.csv.csvoperations.rowoperations.StringRow;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import org.junit.jupiter.api.Test;

public class StateCodesTest {

  // TODO: Change the parser so that it doesn't include the " " on either side of things,
  //  --see ==> asserts in this test class (line 60-62)
  @Test
  public void stateCodeHashCheck() throws IOException, FactoryFailureException {
    FileReader stateFile = new FileReader("data/stateCodes.csv");

    Parser<List<String>, String> parser = new Parser<>();

    ParsedDataPacket<List<String>, String> stateCodes =
        parser.parse(new StringRow(), stateFile, true);

    // Don't have to worry about headers because the parser stores them separately
    List<List<String>> parsedStates = stateCodes.parsedRows();

    HashMap<String, String> stateToNumberMap = new HashMap<>();
    HashMap<String, String> numberToStateMap = new HashMap<>();

    for (List<String> state : parsedStates) {
      stateToNumberMap.put(state.get(0), state.get(1));
      numberToStateMap.put(state.get(1), state.get(0));
    }

    // These all include the quotes but they shouldn't
    assertEquals("11", stateToNumberMap.get("District of Columbia"));
    assertEquals("12", stateToNumberMap.get("Florida"));
    assertEquals("Hawaii", numberToStateMap.get("15"));
  }
}
