import java.util.*;

public class Main {

    static void changeList(List<String> list) {
        int length = 0;
        String longest = null;
        for (String str : list) {
            if (str.length() > length) {
                length = str.length();
                longest = str;
            }
        }
        String finalLongest = longest;
        list.replaceAll(e -> finalLongest);

        // Another solution 1
        // Collections.fill(list, longestString);

        // Another solution 2
        // Collections.fill(list, Collections.max(list, Comparator.comparingInt(String::length)));
    }

    /* Do not change code below */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String s = scanner.nextLine();
        List<String> lst = Arrays.asList(s.split(" "));
        changeList(lst);
        lst.forEach(e -> System.out.print(e + " "));
    }
}