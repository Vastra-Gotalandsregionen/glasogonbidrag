package se.vgregion.service.glasogonbidrag.types.migration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Martin Lind
 */
public class IdentificationStore {
    private Map<String, List<Integer>> rowMap;
    private Map<String, String> nameMap;

    public IdentificationStore() {
        rowMap = new HashMap<>();
        nameMap = new HashMap<>();
    }

    public void addRow(String identification, int row) {
        if (identification.isEmpty()) {
            return;
        }

        if (!rowMap.containsKey(identification)) {
            rowMap.put(identification, new ArrayList<Integer>());
        }

        List<Integer> rows = rowMap.get(identification);
        if (rows == null) {
            rowMap.remove(identification);
            rowMap.put(identification, new ArrayList<Integer>());
        }

        rows = rowMap.get(identification);
        rows.add(row);
    }

    public void addName(String identification, String name) {
        if (nameMap.containsKey(identification)) {
            nameMap.remove(identification);
        }

        nameMap.put(identification, name);
    }

    public List<String> getIdentifications() {
        return new ArrayList<>(rowMap.keySet());
    }

    public List<Integer> getRows(String identification) {
        if (!rowMap.containsKey(identification)) {
            return new ArrayList<>();
        }

        return rowMap.get(identification);
    }

    public String getName(String identification) {
        if (!nameMap.containsKey(identification)) {
            return "";
        }

        return nameMap.get(identification);
    }
}
