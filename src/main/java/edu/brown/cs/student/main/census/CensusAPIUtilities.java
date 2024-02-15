package edu.brown.cs.student.main.census;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonDataException;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class CensusAPIUtilities {

  private CensusAPIUtilities() {}

  public static List<CensusData> deserializeCensus(String jsonList) throws IOException {
    List<CensusData> dataPieces = new ArrayList<>();
    try {
      // initialize moshi
      Moshi moshi = new Moshi.Builder().build();
      // create lisType for adapter
      Type listType = Types.newParameterizedType(List.class, CensusData.class);
      // parse into list of CensusData with adapter
      JsonAdapter<List<CensusData>> adapter = moshi.adapter(listType);

      List<CensusData> deserializedData = adapter.fromJson(jsonList);
      return deserializedData;
    }
    // catch the moshi IO exception
    catch (IOException e) {
      System.err.println("DataHandler: string wasn't valid JSON.");
      throw e;
    } catch (JsonDataException e) {
      System.err.println("DataHandler: JSON wasn't in the right format.");
      throw e;
    }
  }

  public static String serializeCensus(List<CensusData> censusData) {
    // initialize moshi
    Moshi moshi = new Moshi.Builder().build();
    // Create type for adapter
    Type listOfCensusDataType = Types.newParameterizedType(List.class, CensusData.class);
    // Parse into a string with adapter
    JsonAdapter<List<CensusData>> adapter = moshi.adapter(listOfCensusDataType);
    return adapter.toJson(censusData);
  }

  // TODO do the serialize and deserialize equivalents for soup if CensusData was a menu

  // TODO for sprint2 do the readInJson method for the non http request case
  public static String readInJson(String filepath) {
    try {
      return new String(Files.readAllBytes(Paths.get(filepath)));
    } catch (IOException e) {
      return "Error in reading JSON";
    }
  }
}
