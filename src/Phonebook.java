import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Phonebook {
// Written by: Mike Baldwin
// Project 8 - JavaFX Phonebook
// This program allows the saving, loading, addition, and editing of an entry into a phonebook using a GUI.

public List<Entry> entries = new ArrayList<>();

public void mergeAllSimilar() {
    for (int i = 0; i < entries.size(); i++) {
        for (int j = 0; j < entries.size(); j++) {
            Entry iEntry1 = entries.get(i);
            Entry iEntry2 = entries.get(j);

            if (iEntry1 != null && iEntry2 != null && iEntry1.name.equals(iEntry2.name) && iEntry1 != iEntry2) {
                iEntry1.notes += "\nSecondary Number[" + iEntry2.number + ']';
                entries.remove(j);
            }
        }
    }
}

public void savePhonebook() {
    File file = new File(System.getProperty("user.dir") + "/Phonebook.dir");

    if (!file.exists()) {
        try {
            if (!file.createNewFile())
                System.out.println("File already exists!");
        } catch (IOException exc) {
            System.out.println("Failed to create a new phone book file.");
            return;
        }
    }

    try (PrintStream stream = new PrintStream(file)) {
        for (Entry iEntry : entries)
            if (iEntry != null) {
                stream.println(iEntry.name);
                stream.println(iEntry.number);
                stream.println(iEntry.notes);
            }
    } catch (FileNotFoundException exc) {
        System.out.println("Failed to find the file.");
    }
}

public void loadPhonebook() {
    String codeName, number, notes;
    File file = new File(System.getProperty("user.dir") + "/Phonebook.dir");

    if (!file.exists())
        return;

    entries.clear();

    try (Scanner input = new Scanner(file)) {
        while (input.hasNext()) {
            codeName = input.nextLine();
            number = input.nextLine();
            notes = input.nextLine();
            entries.add(new Entry(codeName, number, notes));
        }
    } catch (FileNotFoundException exc) {
        System.out.println("Failed to find the file.");
    }
}
}
