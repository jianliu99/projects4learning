package shindo.Java.map;

import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;

class Map_sortByValue_Comparator implements Comparator<Map.Entry<String, String>> {

    // @Override
    public int compare(Entry<String, String> me1, Entry<String, String> me2) {

        return me1.getValue().compareTo(me2.getValue());
    }
}
