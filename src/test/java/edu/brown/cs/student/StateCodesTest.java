package edu.brown.cs.student;


import static java.lang.Integer.parseInt;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.brown.cs.student.main.csv.csvoperations.ParsedDataPacket;
import edu.brown.cs.student.main.csv.csvoperations.Parser;
import edu.brown.cs.student.main.csv.csvoperations.rowoperations.RowOperator;
import edu.brown.cs.student.main.csv.csvoperations.rowoperations.StringRow;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;
import org.junit.jupiter.api.Test;
import java.util.HashMap;

import edu.brown.cs.student.main.csv.csvoperations.ParsedDataPacket;
import edu.brown.cs.student.main.csv.csvoperations.Parser;
import edu.brown.cs.student.main.csv.csvoperations.Searcher;
import edu.brown.cs.student.main.csv.csvoperations.exceptions.FactoryFailureException;
import edu.brown.cs.student.main.csv.csvoperations.rowoperations.IntegerRow;
import edu.brown.cs.student.main.csv.csvoperations.rowoperations.StringRow;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class StateCodesTest {


  // TODO: Change the parser so that it doesn't include the " " on either side of things,
  //  --see ==> asserts in this test class (line 60-62)
  @Test
  public void stateCodeHashCheck() throws IOException, FactoryFailureException {
//    FileReader stateFile = new FileReader("/Users/zach.stellato/Documents/Code/"
//        + "cs0320/server-zstellat-dviesca/data/stateCodes.csv");
    FileReader stateFile = new FileReader("/Users/domingojr/IdeaProjects/server-zstellat-dviesca/data/stateCodes.csv");

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
