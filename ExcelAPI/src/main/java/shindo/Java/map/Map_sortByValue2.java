package shindo.Java.map;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Map_sortByValue2 {
    public static void main(String[] args) {

        Map<String, Long> map = new TreeMap<String, Long>();

        map.put("KFC", (long) 5);
        map.put("WNBA", (long) 8);
        map.put("NBA", (long) 1);
        map.put("CBA", (long) 6);

//        Map<String, String> resultMap = sortMapByKey(map); // 按Key进行排序
        Map<String, Long> resultMap = sortMapByValue(map); // 按Value进行排序

        for (Map.Entry<String, Long> entry : resultMap.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }
    }

    /**
     * 使用 Map按value进行排序
     * @param map
     * @return
     */
    public static Map<String, Long> sortMapByValue(Map<String, Long> oriMap) {
        if (oriMap == null || oriMap.isEmpty()) {
            return null;
        }
        Map<String, Long> sortedMap = new LinkedHashMap<String, Long>();
        List<Map.Entry<String, Long>> entryList = new ArrayList<Map.Entry<String, Long>>(oriMap.entrySet());
        Collections.sort(entryList, new Map_sortByValue2_Comparator2());

        Iterator<Map.Entry<String, Long>> iter = entryList.iterator();
        Map.Entry<String, Long> tmpEntry = null;
        while (iter.hasNext()) {
            tmpEntry = iter.next();
            sortedMap.put(tmpEntry.getKey(), tmpEntry.getValue());
        }
        return sortedMap;
    }
}
