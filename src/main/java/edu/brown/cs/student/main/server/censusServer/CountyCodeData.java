package edu.brown.cs.student.main.server.censusServer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CountyCodeData {

    public Map<String, String> CountyCodeMap(List<CensusData> countyData) {
        Map<String, String> countyNameToCodeMap = new HashMap<>();

        // Adds ba
        for (int i = 1; i < countyData.size(); i++) {
            CensusData censusItem = countyData.get(i);
            countyNameToCodeMap.put(censusItem.getNAME(), censusItem.getCounty());
        }


        return countyNameToCodeMap;
    }

    public String countyNameFor
}
