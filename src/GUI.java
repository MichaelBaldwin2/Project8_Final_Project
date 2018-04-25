import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class GUI extends Application {
// Written by: Mike Baldwin
// Project 8 - JavaFX Phonebook
// This program allows the saving, loading, addition, and editing of an entry into a phonebook using a GUI.

private Phonebook phonebook;
private MenuBar menuBar;
private TextField searchBar;
private ScrollPane scrollPane;
private GridPane detailsPane;

public static void main(String[] args) {
    launch(args);
}

@Override
public void start(Stage primaryStage) throws Exception {
    phonebook = new Phonebook();
    menuBar = new MenuBar();
    searchBar = new TextField();
    scrollPane = new ScrollPane();
    detailsPane = new GridPane();

    //Setup Menu Bar
    Menu fileMenu = new Menu("File");
    Menu editMenu = new Menu("Edit");
    MenuItem saveMenuItem = new MenuItem("Save");
    saveMenuItem.setOnAction(event -> phonebook.savePhonebook());
    fileMenu.getItems().add(saveMenuItem);
    MenuItem loadMenuItem = new MenuItem("Load");
    loadMenuItem.setOnAction(event -> {
        phonebook.loadPhonebook();
        listAll();
    });
    fileMenu.getItems().add(loadMenuItem);
    MenuItem exitMenuItem = new MenuItem("Exit");
    exitMenuItem.setOnAction(event -> System.exit(1));
    fileMenu.getItems().add(exitMenuItem);
    menuBar = new MenuBar(fileMenu, editMenu);

    //Setup Search Bar
    searchBar = new TextField();
    searchBar.setPromptText("search for contacts here...");
    searchBar.setOnAction(event -> {
        if (searchBar.getText().isEmpty()) {
            listAll();
        } else
            find(searchBar.getText());
    });

    //Setup Details Pane
    detailsPane.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
    detailsPane.setAlignment(Pos.CENTER);
    detailsPane.setHgap(10);
    detailsPane.setVgap(10);
    detailsPane.setPadding(new Insets(25, 25, 25, 25));

    //Setup Scroll Pane
    scrollPane = new ScrollPane();

    //ScrollPane with names
    listAll();

    //Main GridPane
    BorderPane mainGridPane = new BorderPane();
    mainGridPane.setTop(searchBar);
    mainGridPane.setLeft(scrollPane);
    mainGridPane.setCenter(detailsPane);
    // mainGridPane.add(searchBar, 0, 0);
    // mainGridPane.add(scrollPane, 0, 1);
    //mainGridPane.add(detailsPane, 1, 1);

    //Primary Scene
    Scene scene = new Scene(new VBox(), 512, 512, Color.GRAY);
    ((VBox) scene.getRoot()).getChildren().addAll(menuBar, mainGridPane);
    primaryStage.setTitle("PhoneBook");
    primaryStage.setScene(scene);
    primaryStage.show();
}

private void showDetails(Entry entry) {
    detailsPane.getChildren().clear();

    Button editButton = new Button("edit");
    editButton.setUserData(entry);
    editButton.setOnAction(event -> editEntry((Entry) editButton.getUserData()));

    Label nameLabel = new Label(entry.name);
    Label numberLabel = new Label(entry.number);
    nameLabel.setUserData(entry);
    numberLabel.setUserData(entry);

    detailsPane.add(editButton, 0, 0);
    detailsPane.add(nameLabel, 0, 1);
    detailsPane.add(numberLabel, 0, 2);
}

private void editEntry(Entry entry) {
    detailsPane.getChildren().clear();

    TextField nameTextField = new TextField(entry.name);
    TextField numberTextField = new TextField(entry.number);
    TextField notesTextField = new TextField(entry.notes);

    Button saveButton = new Button("save");
    Button cancelButton = new Button("cancel");
    saveButton.setUserData(entry);
    cancelButton.setUserData(entry);

    saveButton.setOnAction(event -> {
        ((Entry) saveButton.getUserData()).name = nameTextField.getText();
        ((Entry) saveButton.getUserData()).number = numberTextField.getText();
        ((Entry) saveButton.getUserData()).notes = notesTextField.getText();
    });

    cancelButton.setOnAction(event -> showDetails((Entry) cancelButton.getUserData()));

    detailsPane.add(saveButton, 0, 0);
    detailsPane.add(cancelButton, 1, 0);
    detailsPane.add(nameTextField, 0, 1);
    detailsPane.add(numberTextField, 0, 2);
    detailsPane.add(notesTextField, 0, 3);
}

private void listAll() {
    GridPane scrollContentPane = new GridPane();

    for (int i = 0; i < phonebook.entries.size(); i++) {
        GridPane entryPane = new GridPane();
        entryPane.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        entryPane.setAlignment(Pos.TOP_LEFT);
        entryPane.setHgap(10);
        entryPane.setVgap(10);
        entryPane.setPadding(new Insets(25, 25, 25, 25));

        //We also get a small button called details for viewing notes
        Button detailsButton = new Button("Details");
        detailsButton.setUserData(phonebook.entries.get(i));
        detailsButton.setOnAction(event -> showDetails((Entry) detailsButton.getUserData()));

        Label nameLabel = new Label(phonebook.entries.get(i).name);
        Label numberLabel = new Label(phonebook.entries.get(i).number);

        nameLabel.setUserData(phonebook.entries.get(i));
        numberLabel.setUserData(phonebook.entries.get(i));

        entryPane.add(nameLabel, 0, 0);
        entryPane.add(numberLabel, 0, 1);
        entryPane.add(detailsButton, 1, 0);

        scrollContentPane.add(entryPane, 0, i);
    }

    scrollPane.setContent(scrollContentPane);
}

private void find(String name) {
    GridPane scrollContentPane = new GridPane();

    for (int i = 0, pos = 0; i < phonebook.entries.size(); i++) {
        if (!phonebook.entries.get(i).name.contains(name))
            continue;

        GridPane entryPane = new GridPane();
        entryPane.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        entryPane.setAlignment(Pos.TOP_LEFT);
        entryPane.setHgap(10);
        entryPane.setVgap(10);
        entryPane.setPadding(new Insets(25, 25, 25, 25));

        Button detailsButton = new Button("Details");
        detailsButton.setUserData(phonebook.entries.get(i));
        detailsButton.setOnAction(event -> showDetails((Entry) detailsButton.getUserData()));

        Label nameLabel = new Label(phonebook.entries.get(i).name);
        Label numberLabel = new Label(phonebook.entries.get(i).number);

        entryPane.add(nameLabel, 0, 0);
        entryPane.add(numberLabel, 0, 1);
        entryPane.add(detailsButton, 1, 0);

        scrollContentPane.add(entryPane, 0, pos++);
    }

    scrollPane.setContent(scrollContentPane);
}
}
