package se.vgregion.glasogonbidrag.util;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TabUtil {
    private Integer activeIndex;
    private List<String> tabs;

    public TabUtil(List<String> tabs, Integer index) {
        this.tabs = tabs;
        this.activeIndex = index;
    }

    public Integer getActiveIndex() {
        return activeIndex;
    }

    public List<Map.Entry<Integer, String>> getTabs() {
        List<Map.Entry<Integer, String>> tabEntries = new ArrayList<>();

        if (tabs != null) {
            for (int i = 0; i < tabs.size(); i++) {
                String tabName = tabs.get(i);
                tabEntries.add(new AbstractMap.SimpleEntry<Integer, String>(i, tabName));

            }
        }

        return tabEntries;
    }

    public void changeTabIndexListener(Integer index) {
        activeIndex = index;
    }
}
