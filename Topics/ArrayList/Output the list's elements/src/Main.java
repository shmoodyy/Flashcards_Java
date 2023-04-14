import java.util.*;

public class Main {
    public static void main(String[] args) {
        List<String> nameList = new ArrayList<>(Arrays.asList("Mr.Green", "Mr.Yellow", "Mr.Red"));
        nameList.forEach(System.out::println);
    }
}