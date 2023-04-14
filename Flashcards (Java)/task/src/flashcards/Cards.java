package flashcards;

import java.io.*;
import java.util.*;

public class Cards {

    Scanner scanner = new Scanner(System.in);
    Map<String, String> flashCardsMap = new HashMap<>();
    Map<String, Integer> flashCardDifficulties = new HashMap<>();
    List<String> flashCardList = new ArrayList<>();
    List<String> logMsgs = new ArrayList<>();
    List<String> hardestCards = new ArrayList<>();
    String importCLIFile, exportCLIFile;
    boolean withImport = false; boolean withExport = false;
    boolean sessionOver = false;

    public void run() {
        while (!sessionOver) {
            actionMenu();
        }
    }

    public void resetStats() {
        flashCardDifficulties.replaceAll((k,v) -> 0);
        hardestCards.clear();
        System.out.println("Card statistics have been reset.");
        logMsgs.add("Card statistics have been reset.\n");
    }

    public void hardestCard() {
        try {
            Integer maxCount = Collections.max(flashCardDifficulties.values()); //
            for (var entry : flashCardDifficulties.entrySet()) {
                if (entry.getValue() != 0 && entry.getValue().equals(maxCount) && !hardestCards.contains(entry.getKey())) {
                    hardestCards.add(entry.getKey());
                }
            }
            if (hardestCards.size() == 1) {
                System.out.printf("The hardest card is \"%s\". You have %d errors answering it.\n"
                        , hardestCards.get(0), maxCount);
                logMsgs.add(String.format("The hardest card is \"%s\". You have %d errors answering it.\n"
                        , hardestCards.get(0), maxCount));
            } else if (hardestCards.size() > 1) {
                System.out.printf("The hardest cards are \"%s\"", hardestCards.get(0));
                logMsgs.add(String.format("The hardest cards are \"%s\"", hardestCards.get(0)));
                for (int i = 1; i < hardestCards.size(); i++) {
                    System.out.print(", \"" + hardestCards.get(i) + "\"");
                    logMsgs.add(", \"" + hardestCards.get(i) + "\"\n");
                }
                System.out.println(". You have " + maxCount + " errors answering them.");
                logMsgs.add(". You have " + maxCount + " errors answering them.\n");
            } else {
                System.out.println("There are no cards with errors.");
                logMsgs.add("There are no cards with errors.\n");
            }
        } catch (Exception e) {
            System.out.println("There are no cards with errors.");
            logMsgs.add("There are no cards with errors.\n");
        }
    }

    public void saveLog() {
        try (FileWriter writer = new FileWriter(filer())) {
            for (var entry : logMsgs) {
                writer.write(entry);
            }
            System.out.println("The log has been saved.");
            logMsgs.add("The log has been saved.\n");
        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        }
    }

    public void askFlashcard() {
        System.out.println("How many times to ask?");
        logMsgs.add("How many times to ask?\n");
        int timesToAsk = scanner.nextInt();
        logMsgs.add("> " + timesToAsk);
        scanner.nextLine();
        for (int cardNum = 0; cardNum < timesToAsk; cardNum++) {
            String currentCard = flashCardList.get(cardNum);
            System.out.printf("Print the definition of \"%s\":\n", currentCard);
            logMsgs.add(String.format("Print the definition of \"%s\":\n", currentCard));
            String answer = scanner.nextLine();
            logMsgs.add("> " + answer);
            String key = null;
            for (var entry : flashCardsMap.entrySet()) {
                if (entry.getValue().equals(answer)) {
                    key = entry.getKey();
                    break;
                }
            }
            if (answer.equalsIgnoreCase(flashCardsMap.get(currentCard))) {
                System.out.println("Correct!");
                logMsgs.add("Correct!\n");
            } else {
                int hardCount;
                System.out.printf("Wrong. The right answer is \"%s", flashCardsMap.get(currentCard)
                        + (flashCardsMap.containsValue(answer) ?
                        ("\", but your definition is correct for \"" + key + "\".\n") : "\".\n"));
                logMsgs.add(String.format("Wrong. The right answer is \"%s", flashCardsMap.get(currentCard)
                        + (flashCardsMap.containsValue(answer) ?
                        ("\", but your definition is correct for \"" + key + "\".\n") : "\".\n")));
                hardCount = flashCardDifficulties.get(currentCard) + 1;
                flashCardDifficulties.put(currentCard, hardCount);
            }
        }
    }

    public void toSaveOrNotToSave() {
        if (withExport) {
            exportFlashcard(exportCLIFile);
        }
        sessionOver = true;
        System.out.println("Bye bye!");
    }
    public void exportFlashcard(String filename) {
        File exportFile = new File(filename); //currentDir + filename?
        try (FileWriter writer = new FileWriter(exportFile)) {
            for (var entry : flashCardsMap.entrySet()) {
                writer.write(entry.getKey() + ":" + entry.getValue()
                        + ":" + flashCardDifficulties.get(entry.getKey()) + "\n");
            }
            System.out.printf("%d cards have been saved.\n", flashCardsMap.size());
            logMsgs.add(String.format("%d cards have been saved.\n", flashCardsMap.size()));
        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        }
    }
    public void exportFlashcard() {
        try (FileWriter writer = new FileWriter(filer())) {
            for (var entry : flashCardsMap.entrySet()) {
                writer.write(entry.getKey() + ":" + entry.getValue()
                        + ":" + flashCardDifficulties.get(entry.getKey()) + "\n");
            }
            System.out.printf("%d cards have been saved.\n", flashCardsMap.size());
            logMsgs.add(String.format("%d cards have been saved.\n", flashCardsMap.size()));
        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        }
    }

    public void importFlashcard(String filename) {
        try (Scanner fileScanner = new Scanner(new File(filename))) {
            int filePairsCount = 0;
            while (fileScanner.hasNextLine()) {
                String[] fileCard = fileScanner.nextLine().split(":");
                flashCardList.add(fileCard[0]);
                flashCardsMap.put(fileCard[0], fileCard[1]);
                flashCardDifficulties.put(fileCard[0], Integer.parseInt(fileCard[2]));
                filePairsCount++;
            }
            System.out.printf("%d cards have been loaded.\n", filePairsCount);
            logMsgs.add(String.format("%d cards have been loaded.\n", filePairsCount));
        } catch (FileNotFoundException fNFE) {
            System.out.println("File not found.");
            logMsgs.add("File not found.\n");
        }
    }

    public void importFlashcard() {
        try (Scanner fileScanner = new Scanner(filer())) {
            int filePairsCount = 0;
            while (fileScanner.hasNextLine()) {
                String[] fileCard = fileScanner.nextLine().split(":");
                flashCardList.add(fileCard[0]);
                flashCardsMap.put(fileCard[0], fileCard[1]);
                flashCardDifficulties.put(fileCard[0], Integer.parseInt(fileCard[2]));
                filePairsCount++;
            }
            System.out.printf("%d cards have been loaded.\n", filePairsCount);
            logMsgs.add(String.format("%d cards have been loaded.\n", filePairsCount));
        } catch (FileNotFoundException fNFE) {
            System.out.println("File not found.");
            logMsgs.add("File not found.\n");
        }
    }

    public File filer() {
        System.out.println("File name:");
        logMsgs.add("File name:\n");
        String filePath = scanner.nextLine();
        logMsgs.add("> " + filePath + "\n");
        return new File(filePath);
    }

    public void removeFlashcard() {
        System.out.println("Which card?");
        logMsgs.add("Which card?\n");
        String termToRemove = scanner.nextLine();
        logMsgs.add("> " + termToRemove + "\n");
        if (flashCardsMap.containsKey(termToRemove)) {
            flashCardsMap.remove(termToRemove);
            flashCardList.remove(termToRemove);
            flashCardDifficulties.remove(termToRemove);
            System.out.println("The card has been removed.");
            logMsgs.add("The card has been removed.\n");
        } else {
            System.out.printf("Can't remove \"%s\": there is no such card.\n", termToRemove);
            logMsgs.add(String.format("Can't remove \"%s\": there is no such card.\n", termToRemove));

        }
    }

    public void addFlashcard() {
        System.out.println("The card:");
        logMsgs.add("The card:\n");
        String term = scanner.nextLine();
        logMsgs.add("> " + term + "\n");
        if (flashCardsMap.containsKey(term)) {
            System.out.printf("The card \"%s\" already exists.\n", term);
            logMsgs.add(String.format("The card \"%s\" already exists.\n", term));
        } else {
            flashCardList.add(term);
            flashCardDifficulties.put(term, 0);
            System.out.println("The definition of the card:");
            logMsgs.add("The definition of the card:\n");
            String definition = scanner.nextLine();
            logMsgs.add("> " + definition + "\n");
            if (flashCardsMap.containsValue(definition)) {
                System.out.printf("The definition \"%s\" already exists.\n", definition);
                logMsgs.add(String.format("The definition \"%s\" already exists.\n", definition));
            } else {
                flashCardsMap.put(term, definition);
                System.out.printf("The pair (\"%s\":\"%s\") has been added. \n", term, definition);
                logMsgs.add(String.format("The pair (\"%s\":\"%s\") has been added. \n", term, definition));
            }
        }
    }

    public void actionMenu() {
        System.out.println("Input the action (add, remove, import, export, ask, exit, log, hardest card, reset stats):");
        logMsgs.add("Input the action (add, remove, import, export, ask, exit, log, hardest card, reset stats):\n");
        String action = scanner.nextLine();
        logMsgs.add("> " + action + "\n");
        switch (action.toLowerCase()) {
            case "add"            ->  addFlashcard();
            case "remove"         ->  removeFlashcard();
            case "import"         ->  importFlashcard();
            case "export"         ->  exportFlashcard();
            case "ask"            ->  askFlashcard();
            case "log"            ->  saveLog();
            case "hardest card"   ->  hardestCard();
            case "reset stats"    ->  resetStats();
            case "exit"           ->  toSaveOrNotToSave();
        }
        System.out.println();
        logMsgs.add("\n");
    }
}