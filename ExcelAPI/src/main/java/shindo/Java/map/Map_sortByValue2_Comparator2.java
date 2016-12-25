package shindo.Java.map;

import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;

class Map_sortByValue2_Comparator2 implements Comparator<Map.Entry<String, Long>> {

    // @Override
    public int compare(Entry<String, Long> me1, Entry<String, Long> me2) {

        return me1.getValue().compareTo(me2.getValue());
    }
}
