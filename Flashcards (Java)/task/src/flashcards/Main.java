package flashcards;

public class Main {
    public static void main(String[] args) {
        Cards study = new Cards();
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-import")) study.importFlashcard(args[i + 1]);
            else if (args[i].equals("-export")) {
                study.withExport = true;
                study.exportCLIFile = args[i + 1];
            }
        }
        study.run();
    }
}