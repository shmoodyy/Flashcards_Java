import java.util.*;

class MapUtils {

    public static SortedMap<String, Integer> wordCount(String[] strings) {

        SortedMap<String, Integer> wordCountMap = new TreeMap<>();
        for (String word : strings) {
            int count = 0;
            if (wordCountMap.containsKey(word)) {
                count = wordCountMap.get(word);
            }
            wordCountMap.put(word, ++count);
        }
        return wordCountMap;
    }

    public static void printMap(Map<String, Integer> map) {
        map.forEach((word, count) -> System.out.println(word + " : " + count));
    }
}

/* Do not change code below */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String[] words = scanner.nextLine().split(" ");
        MapUtils.printMap(MapUtils.wordCount(words));
    }
}