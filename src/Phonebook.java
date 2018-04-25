import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Phonebook extends Application {
// Written by: Mike Baldwin
// Project 8 - JavaFX Phonebook
// This program allows the saving, loading, addition, and editing of an entry into a phonebook using a GUI.

private List<Entry> entries = new ArrayList<>();

public static void main(String[] args) {
    launch(args);
}

@Override
public void start(Stage primaryStage) throws Exception {
    loadPhonebook();

    MenuBar menuBar = setupMenuBar();
    ScrollPane scrollPane = listAllInPane();

    Scene scene = new Scene(new VBox(), 768, 512, Color.GRAY);
    ((VBox) scene.getRoot()).getChildren().addAll(menuBar, scrollPane);
    primaryStage.setTitle("PhoneBook");
    primaryStage.setScene(scene);
    primaryStage.show();
}

private MenuBar setupMenuBar()
{
    Menu fileMenu = new Menu("File");
    Menu editMenu = new Menu("Edit");

    MenuItem saveMenuItem = new MenuItem("Save");
    saveMenuItem.setOnAction(event -> savePhonebook());
    fileMenu.getItems().add(saveMenuItem);

    MenuItem loadMenuItem = new MenuItem("Load");
    loadMenuItem.setOnAction(event -> loadPhonebook());
    fileMenu.getItems().add(loadMenuItem);

    MenuItem exitMenuItem = new MenuItem("Exit");
    exitMenuItem.setOnAction(event -> System.exit(1));
    fileMenu.getItems().add(exitMenuItem);

    return new MenuBar(fileMenu, editMenu);
}

private ScrollPane listAllInPane()
{
    ScrollPane scrollPane = new ScrollPane();
    GridPane mainPane = new GridPane();

    for(int i = 0; i < entries.size(); i++)
    {
        GridPane entryPane = new GridPane();
        entryPane.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        entryPane.setAlignment(Pos.TOP_LEFT);
        entryPane.setHgap(10);
        entryPane.setVgap(10);
        entryPane.setPadding(new Insets(25, 25, 25, 25));

        TextField nameTextField = new TextField(entries.get(i).name);
        TextField numberTextField = new TextField(entries.get(i).number);
        TextField notesTextField = new TextField(entries.get(i).notes);

        nameTextField.setPromptText("name");
        numberTextField.setPromptText("number");
        notesTextField.setPromptText("notes");

        nameTextField.setUserData(entries.get(i));
        numberTextField.setUserData(entries.get(i));
        notesTextField.setUserData(entries.get(i));

        nameTextField.setOnAction(event -> ((Entry)nameTextField.getUserData()).name = nameTextField.getText());
        numberTextField.setOnAction(event -> ((Entry)numberTextField.getUserData()).number = numberTextField.getText());
        notesTextField.setOnAction(event -> ((Entry)notesTextField.getUserData()).notes = notesTextField.getText());

        entryPane.add(nameTextField, 0, 0);
        entryPane.add(numberTextField, 0, 1);
        entryPane.add(notesTextField, 0, 2);

        mainPane.add(entryPane, 0, i);
    }

    scrollPane.setContent(mainPane);
    return scrollPane;
}

private void savePhonebook() {
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

private void loadPhonebook() {
    String codeName, number, notes;
    File file = new File(System.getProperty("user.dir") + "/Phonebook.dir");

    if (!file.exists())
        return;

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
