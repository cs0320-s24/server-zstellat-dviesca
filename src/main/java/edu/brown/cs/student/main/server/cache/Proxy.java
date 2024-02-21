package edu.brown.cs.student.main.server.cache;

import edu.brown.cs.student.main.csv.csvoperations.rowoperations.StringRow;
import java.util.HashMap;
import java.util.Map;

public class Proxy {
  private Map<String, Map<String, String>> stateToCountyCodeMap;

  public Proxy() {
    this.stateToCountyCodeMap = new HashMap<>();

  }

  public Map<String, Map<String, String>> getStateToCountyCodeMap() {
    return stateToCountyCodeMap;
  }

  public void addStateToCountyCodes(String stateCode, Map<String, String> countyToCodeMap) {
    this.stateToCountyCodeMap.put(stateCode, countyToCodeMap);
  }

  //  LoadingCache<Key, Graph> graphs = CacheBuilder.newBuilder()
  //      .maximumSize(1000)
  //      .build(
  //          new CacheLoader<Key, Graph>() {
  //            public Graph load(Key key) throws AnyException {
  //              return createExpensiveGraph(key);
  //            }
  //          });
  //
  // ...
  //    try {
  //    return graphs.get(key);
  //  } catch (ExecutionException e) {
  //    throw new OtherException(e.getCause());
  //  }
}
