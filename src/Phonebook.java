import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;

public class Phonebook extends Application {
// Written by: Mike Baldwin
// Project 7 - Phonebook
// This program allows the saving, loading, addition, and editing of an entry into a phonebook.

private static Scanner scanner = new Scanner(System.in);
private static Entry[] entries;
private static int entryIndex;

public static void main(String[] args) {
	launch(args);
}

@Override
public void start(Stage primaryStage) throws Exception {
	Button fileButton = new Button("File");
	Button editButton = new Button("Edit");
	Button optionsButton = new Button("Options");
	Button helpButton = new Button("Help");
	GridPane menuBarPane = new GridPane();
	//menuBarPane.setPrefSize(512, 32);
	menuBarPane.add(fileButton, 0, 0);
	menuBarPane.add(editButton, 1, 0);
	menuBarPane.add(optionsButton, 2, 0);
	menuBarPane.add(helpButton, 3, 0);

	//Button button = new Button("Add New Entry");
	Scene scene = new Scene(menuBarPane, 512, 768, Color.GRAY);
	primaryStage.setTitle("PhoneBook");
	primaryStage.setScene(scene);
	primaryStage.show();
}

public static void update() {
    String commandString;

    entries = new Entry[200];
    entryIndex = 0;
    commandString = "";

    try {
        readPhoneBook();
    } catch (IOException exc) {
        System.out.println("Failed to read phone book.\n" + exc);
        System.exit(1);
    }

    System.out.println("Codes are entered as 1 to 8 characters.");
    System.out.println("Use \"e\" for enter, \"f\" for find, \"l\" for list, \"q\" for quit.");

    while (!commandString.equals("q")) {
        System.out.print("Command: ");
        commandString = scanner.nextLine().toLowerCase();

        if (commandString.isEmpty())
            continue;
        if (commandString.length() > 8) {
            System.out.println("Codes have to be between 1 and 8 characters in length.");
            System.out.println("Use \"e\" for enter, \"f\" for find, \"l\" for list," +
                    "\"q\" for quit.");
            continue;
        }

        switch (commandString.charAt(0)) {
            case 'e':
                addNewEntry(commandString.substring(2));
                break;
            case 'f':
                findEntry(commandString.substring(2));
                break;
            case 'l':
                listAllEntries();
                break;
            case 'q':
                //Do nothing, the while loop will quit
                break;
            default:
                System.out.println("Input was not recognized. Please try again.");
                System.out.println("Use \"e\" for enter, \"f\" for find, \"l\" for list," +
                        "\"q\" for quit.");
                break;
        }
    }

    try {
        storePhoneBook();
    } catch (IOException exc) {
        System.out.println("Failed to store phone book.\n" + exc);
        System.exit(2);
    }
}

private static void addNewEntry(String codeName) {
    String number, notes;

    for (Entry iEntry : entries)
        if (iEntry != null && iEntry.codeName.equals(codeName)) {
            //We are going to update the entry
            System.out.print("Enter number: ");
            number = scanner.nextLine();
            System.out.print("Enter notes: ");
            notes = scanner.nextLine();
            iEntry.number = number;
            iEntry.notes = notes;
            System.out.println();
            return;
        }

    if (entryIndex > 200) {
        System.out.println("Can't add any more entries due to the 200 limit " +
                "project constraints!");
        return;
    }

    System.out.print("Enter number: ");
    number = scanner.nextLine();
    System.out.print("Enter notes: ");
    notes = scanner.nextLine();
    entries[entryIndex++] = new Entry(codeName, number, notes);
    System.out.println();
}

private static void findEntry(String codeName) {
    for (Entry iEntry : entries)
        if (iEntry != null && iEntry.codeName.equals(codeName)) {
            System.out.println(iEntry);
            return;
        }

    System.out.println("** No entry with code " + codeName);
}

private static void listAllEntries() {
    for (Entry iEntry : entries)
        if (iEntry != null) {
            System.out.println(iEntry);
            System.out.println();
        }
}

private static void storePhoneBook() throws IOException {
    File file = new File(System.getProperty("user.dir") + "/PhoneBook.dir");

    if (!file.exists()) {
        try {
            if (!file.createNewFile())
                System.out.println("File already exists!");
        } catch (IOException exc) {
            System.out.println("Failed to create a new phone book file.");
            throw exc;
        }
    }

    try (PrintStream stream = new PrintStream(file)) {
        for (Entry iEntry : entries)
            if (iEntry != null)
                stream.println(iEntry);
    } catch (FileNotFoundException exc) {
        System.out.println("Failed to find the file.");
        throw exc;
    }
}

private static void readPhoneBook() throws IOException {
    String codeName, number, notes;
    File file = new File(System.getProperty("user.dir") + "/PhoneBook.dir");

    if (!file.exists())
        return;

    try (Scanner input = new Scanner(file)) {
        while (input.hasNext()) {
            if (entryIndex > 200) {
                System.out.println("Ignoring the rest of the file due to the 200 limit project constraints!");
                break;
            }

            codeName = input.nextLine().substring(3);
            number = input.nextLine().substring(3);
            notes = input.nextLine().substring(3);
            entries[entryIndex++] = new Entry(codeName, number, notes);
        }
    } catch (FileNotFoundException exc) {
        System.out.println("Failed to find the file.");
        throw exc;
    }
}
}
