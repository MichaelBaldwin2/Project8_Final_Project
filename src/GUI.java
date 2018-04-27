import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.util.Collections;

public class GUI extends Application {
// Written by: Mike Baldwin
// Project 8 - JavaFX Phonebook
// This program allows the saving, loading, addition, and editing of an entry into a phonebook using a GUI.

private Phonebook phonebook;
private TextField searchBar;
private ScrollPane scrollPane;

public static void main(String[] args) {
    launch(args);
}

@Override
public void start(Stage primaryStage) {
    MenuBar menuBar;
    phonebook = new Phonebook();
    searchBar = new TextField();
    scrollPane = new ScrollPane();
    phonebook.loadPhonebook();

    //Setup Menu Bar
    Menu fileMenu = new Menu("File");
    Menu editMenu = new Menu("Edit");
    MenuItem saveMenuItem = new MenuItem("Save");
    MenuItem loadMenuItem = new MenuItem("Load");
    MenuItem exitMenuItem = new MenuItem("Exit");
    MenuItem addMenuItem = new MenuItem("Add");
    MenuItem mergeMenuItem = new MenuItem("Merge");
    MenuItem sortMenuItem = new MenuItem("Sort");

    saveMenuItem.setOnAction(event -> phonebook.savePhonebook());
    loadMenuItem.setOnAction(event -> {
        phonebook.loadPhonebook();
        listAll();
    });
    exitMenuItem.setOnAction(event -> System.exit(0));
    addMenuItem.setOnAction(event -> addNewEntryPopup());
    mergeMenuItem.setOnAction(event -> {
        phonebook.mergeAllSimilar();
        listAll();
    });
    sortMenuItem.setOnAction(event -> {
        Collections.sort(phonebook.entries);
        listAll();
    });

    fileMenu.getItems().add(saveMenuItem);
    fileMenu.getItems().add(loadMenuItem);
    fileMenu.getItems().add(exitMenuItem);
    editMenu.getItems().add(addMenuItem);
    editMenu.getItems().add(mergeMenuItem);
    editMenu.getItems().add(sortMenuItem);
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

    //Setup Scroll Pane
    scrollPane = new ScrollPane();
    listAll();

    //Main GridPane
    VBox mainPane = new VBox();
    mainPane.getChildren().addAll(menuBar, searchBar, scrollPane);

    //Primary Scene
    Scene scene = new Scene(mainPane, 512, 512, Color.GRAY);
    primaryStage.setTitle("PhoneBook");
    primaryStage.setScene(scene);
    primaryStage.show();
}

private void addNewEntryPopup() {
    // variable declarations
    Stage stage;
    Scene scene;
    VBox vBox;
    Label titleLabel;
    TextField nameTextField, numberTextField, notesTextField;
    Button saveButton, cancelButton;

    // constructors
    stage = new Stage();
    vBox = new VBox();
    scene = new Scene(vBox, 256, 256, Color.GRAY);
    titleLabel = new Label("ADD NEW ENTRY...");
    nameTextField = new TextField();
    numberTextField = new TextField();
    notesTextField = new TextField();
    saveButton = new Button("save");
    cancelButton = new Button("cancel");

    // setups
    vBox.setAlignment(Pos.TOP_LEFT);
    vBox.setSpacing(15);
    vBox.setPadding(new Insets(25, 25, 25, 25));
    titleLabel.setFont(new Font(18));
    nameTextField.setPromptText("name...");
    numberTextField.setPromptText("number...");
    notesTextField.setPromptText("notes...");
    vBox.getChildren().addAll(titleLabel, nameTextField, numberTextField, notesTextField, saveButton, cancelButton);
    stage.setTitle("Add New");
    stage.setScene(scene);

    // events
    saveButton.setOnAction(event -> {
        phonebook.entries.add(new Entry(nameTextField.getText(), numberTextField.getText(), notesTextField.getText()));
        stage.close();
    });
    cancelButton.setOnAction(event -> stage.close());

    stage.show();
}

private void showDetailsPopup(Entry entry) {
    // variable declarations
    Stage stage;
    Scene scene;
    GridPane pane;
    Label titleLabel, nameLabel, numberLabel, notesLabel, instanceNameLabel, instanceNumberLabel, instanceNotesLabel;
    Button editButton, deleteButton;

    // constructors
    stage = new Stage();
    pane = new GridPane();
    scene = new Scene(pane, 384, 256, Color.GRAY);
    titleLabel = new Label("DETAILS...");
    nameLabel = new Label("Name:");
    numberLabel = new Label("Number:");
    notesLabel = new Label("Notes:");
    instanceNameLabel = new Label(entry.name);
    instanceNumberLabel = new Label(entry.number);
    instanceNotesLabel = new Label(entry.notes);
    editButton = new Button("edit");
    deleteButton = new Button("delete");

    // setups
    pane.setAlignment(Pos.TOP_LEFT);
    pane.setVgap(10);
    pane.setHgap(15);
    pane.setPadding(new Insets(25, 25, 25, 25));
    titleLabel.setFont(new Font(18));
    nameLabel.setFont(new Font(14));
    numberLabel.setFont(new Font(14));
    notesLabel.setFont(new Font(14));
    stage.setTitle("Details");
    stage.setScene(scene);
    pane.add(titleLabel, 0, 0);
    pane.add(nameLabel, 0, 1);
    pane.add(numberLabel, 0, 2);
    pane.add(notesLabel, 0, 3);
    pane.add(instanceNameLabel, 1, 1);
    pane.add(instanceNumberLabel, 1, 2);
    pane.add(instanceNotesLabel, 1, 3);
    pane.add(editButton, 0, 4);
    pane.add(deleteButton, 1, 4);

    // events
    editButton.setOnAction(event -> {
        showEditEntryPopup(entry);
        stage.close();
    });
    deleteButton.setOnAction(event -> {
        showDeleteConfirmationPopup(entry);
        stage.close();
    });

    stage.show();
}

private void showEditEntryPopup(Entry entry) {
    // variable declarations
    Stage stage;
    Scene scene;
    VBox vBox;
    Label titleLabel;
    TextField nameTextField, numberTextField, notesTextField;
    Button saveButton, cancelButton;

    // constructors
    stage = new Stage();
    vBox = new VBox();
    scene = new Scene(vBox, 256, 256, Color.GRAY);
    titleLabel = new Label("EDIT ENTRY...");
    nameTextField = new TextField(entry.name);
    numberTextField = new TextField(entry.number);
    notesTextField = new TextField(entry.notes);
    saveButton = new Button("save");
    cancelButton = new Button("cancel");

    // setups
    vBox.setAlignment(Pos.TOP_LEFT);
    vBox.setSpacing(15);
    vBox.setPadding(new Insets(25, 25, 25, 25));
    titleLabel.setFont(new Font(18));
    nameTextField.setPromptText("name...");
    numberTextField.setPromptText("number...");
    notesTextField.setPromptText("notes...");
    vBox.getChildren().addAll(titleLabel, nameTextField, numberTextField, notesTextField, saveButton, cancelButton);
    stage.setTitle("Edit");
    stage.setScene(scene);

    // events
    saveButton.setOnAction(event -> {
        entry.name = nameTextField.getText();
        entry.number = numberTextField.getText();
        entry.notes = notesTextField.getText();
        stage.close();
        showDetailsPopup(entry);
    });
    cancelButton.setOnAction(event -> {
        stage.close();
        showDetailsPopup(entry);
    });

    stage.show();
}

private void showDeleteConfirmationPopup(Entry entry) {
    // variable declarations
    Stage stage;
    Scene scene;
    VBox pane;
    Label titleLabel, questionLabel;
    Button yesButton, noButton;

    // constructors
    stage = new Stage();
    pane = new VBox();
    scene = new Scene(pane, 384, 256, Color.GRAY);
    titleLabel = new Label("DELETE CONFIRMATION...");
    questionLabel = new Label("Are you sure you want to delete\n" + entry.name + "?");
    yesButton = new Button("yes");
    noButton = new Button("no");

    // setups
    pane.setAlignment(Pos.TOP_LEFT);
    pane.setSpacing(15);
    pane.setPadding(new Insets(25, 25, 25, 25));
    titleLabel.setFont(new Font(18));
    questionLabel.setTextAlignment(TextAlignment.CENTER);
    stage.setTitle("Delete Confirmation");
    stage.setScene(scene);
    pane.getChildren().addAll(titleLabel, questionLabel, yesButton, noButton);

    // events
    yesButton.setOnAction(event -> {
        phonebook.entries.remove(entry);
        stage.close();
    });
    noButton.setOnAction(event -> {
        stage.close();
        showDetailsPopup(entry);
    });

    stage.show();
}

private void listAll() {
    VBox contentPane;
    contentPane = new VBox();

    for (Entry iEntry : phonebook.entries) {
        // variable declarations
        GridPane entryPane;
        Label nameLabel, numberLabel, instanceNameLabel, instanceNumberLabel;
        Button detailsButton;

        // constructors
        entryPane = new GridPane();
        nameLabel = new Label("Name:");
        numberLabel = new Label("Number:");
        instanceNameLabel = new Label(iEntry.name);
        instanceNumberLabel = new Label(iEntry.number);
        detailsButton = new Button("Details");

        // setups
        entryPane.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        entryPane.setAlignment(Pos.TOP_LEFT);
        entryPane.setVgap(10);
        entryPane.setHgap(15);
        entryPane.setPadding(new Insets(25, 25, 25, 25));
        nameLabel.setFont(new Font(14));
        numberLabel.setFont(new Font(14));
        instanceNameLabel.setUserData(iEntry);
        instanceNumberLabel.setUserData(iEntry);
        detailsButton.setUserData(iEntry);
        entryPane.add(nameLabel, 0, 0);
        entryPane.add(numberLabel, 0, 1);
        entryPane.add(instanceNameLabel, 1, 0);
        entryPane.add(instanceNumberLabel, 1, 1);
        entryPane.add(detailsButton, 1, 2);
        contentPane.getChildren().add(entryPane);

        // events
        detailsButton.setOnAction(event -> showDetailsPopup((Entry) detailsButton.getUserData()));

    }

    scrollPane.setContent(contentPane);
    searchBar.setText("");
}

private void find(String name) {
    VBox contentPane;
    contentPane = new VBox();

    for (Entry iEntry : phonebook.entries) {
        if (!iEntry.name.toLowerCase().contains(name.toLowerCase()))
            continue;

        // variable declarations
        GridPane entryPane;
        Label nameLabel, numberLabel, instanceNameLabel, instanceNumberLabel;
        Button detailsButton;

        // constructors
        entryPane = new GridPane();
        nameLabel = new Label("Name:");
        numberLabel = new Label("Number:");
        instanceNameLabel = new Label(iEntry.name);
        instanceNumberLabel = new Label(iEntry.number);
        detailsButton = new Button("Details");

        // setups
        entryPane.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        entryPane.setAlignment(Pos.TOP_LEFT);
        entryPane.setVgap(10);
        entryPane.setHgap(15);
        entryPane.setPadding(new Insets(25, 25, 25, 25));
        nameLabel.setFont(new Font(14));
        numberLabel.setFont(new Font(14));
        instanceNameLabel.setUserData(iEntry);
        instanceNumberLabel.setUserData(iEntry);
        detailsButton.setUserData(iEntry);
        entryPane.add(nameLabel, 0, 0);
        entryPane.add(numberLabel, 0, 1);
        entryPane.add(instanceNameLabel, 1, 0);
        entryPane.add(instanceNumberLabel, 1, 1);
        entryPane.add(detailsButton, 1, 2);
        contentPane.getChildren().add(entryPane);

        // events
        detailsButton.setOnAction(event -> showDetailsPopup((Entry) detailsButton.getUserData()));
    }

    scrollPane.setContent(contentPane);
}
}
