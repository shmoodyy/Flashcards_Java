import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        char[] word1 = scanner.next().toLowerCase().toCharArray();
        char[] word2 = scanner.next().toLowerCase().toCharArray();
        Map<Character, Integer> anagram1 = new HashMap<>();
        Map<Character, Integer> anagram2 = new HashMap<>();

        for (char c1 : word1) {
            int count1 = 0;
            if (anagram1.containsKey(c1)) {
                count1 = anagram1.get(c1); //captures the current count from the key value and saves it
            }
            anagram1.put(c1, ++count1); //updates the saved count by incrementing and adds that new value to the key
        }

        for (char c2 : word2) {
            int count2 = 0;
            if (anagram2.containsKey(c2)) {
                count2 = anagram2.get(c2);
            }
            anagram2.put(c2, ++count2);
        }
        System.out.println(anagram1.equals(anagram2) ? "yes" : "no");
    }
}