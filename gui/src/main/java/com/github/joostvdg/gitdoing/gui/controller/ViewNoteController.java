package com.github.joostvdg.gitdoing.gui.controller;

import com.github.joostvdg.gitdoing.api.Note;
import com.github.joostvdg.gitdoing.api.NoteItem;
import com.github.joostvdg.gitdoing.gui.DemoPopulator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.web.WebView;

import java.util.Objects;

public class ViewNoteController {

    @FXML
    private VBox dataContainer;

    private final Note note;

    public ViewNoteController(Note note) {
        this.note = note;
    }

    public Note getNote(){
        return note;
    }

    // https://stackpath.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css


    @FXML
    public void initialize() {
        System.out.println("Note (modal controller): " + note.getIdentifier() + " - " + note.getName());

        var nameHeaderFont = new Font("Arial", 32);

        HBox nameHeader = new HBox();
        Text nameHeaderValue = new Text(note.getName());
        nameHeaderValue.setFont(nameHeaderFont);
        nameHeaderValue.setTextAlignment(TextAlignment.CENTER);
        nameHeader.setMinWidth(200);
        nameHeader.getChildren().add(nameHeaderValue);

        HBox identifier = createTextLine("Identifier", note.getIdentifier());
        HBox description = createTextLine("Description", note.getDescription());
        HBox closed = createTextLine("Closed", ""+note.isClosed());
        HBox created = createTextLine("Created", note.getCreated().toString());
        String lastEditText = "N/A";
        if (note.getLastEdit() != null) {
            lastEditText = note.getLastEdit().toString();
        }
        HBox lastEdit = createTextLine("Last Edit", lastEditText);
        String lastOpenedText = "N/A";
        if (note.getLastOpened() != null) {
            lastOpenedText = note.getLastOpened().toString();
        }
        HBox lastOpened = createTextLine("Last Opened", lastOpenedText);

        StringBuilder labels = new StringBuilder();
        note.getLabels().forEach( label -> labels.append(label +" "));
        HBox label = createTextLine("Labels", labels.toString());

        ObservableList<NoteItem> masterData = FXCollections.observableArrayList();
        var demoData = new DemoPopulator();
        masterData.addAll(note.getItems());

        var noteItems = new TableView<>(FXCollections.observableList(masterData));
        TableColumn kind = new TableColumn("Kind");
        kind.setCellValueFactory(new PropertyValueFactory("kind"));
        TableColumn link = new TableColumn("Link");
        link.setCellValueFactory(new PropertyValueFactory("link"));
        TableColumn comment = new TableColumn("Comment");
        comment.setCellValueFactory(new PropertyValueFactory("comment"));

        noteItems.getColumns().addAll(kind, comment, link);
        noteItems.setMaxHeight(200);
        noteItems.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/bootstrap3.css")).toExternalForm());

        WebView webView = new WebView();
        // TODO: do a markdown translation!
        String content = note.getText();
        content = "<div styleClass=\"jumbotron\"><h1>" + content + "</h1></div>";
        webView.getEngine().loadContent(content, "text/html");
        VBox noteText = new VBox(webView);

        dataContainer.getChildren().addAll(nameHeader, identifier, description, closed, created, lastEdit, lastOpened, label, noteItems, noteText);
    }

    private HBox createTextLine(String fieldLabel, String fieldValue) {
        var labelFont = new Font("Arial", 18);

        HBox line = new HBox();
        Label label = new Label(fieldLabel + ":");
        label.setMinWidth(150);
        label.setFont(labelFont);
        Text value = new Text(fieldValue);
        value.setFont(labelFont);
        line.getChildren().addAll(label, value);
        return line;
    }
}
