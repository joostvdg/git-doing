package com.github.joostvdg.gitdoing.gui;

import com.github.joostvdg.gitdoing.api.Note;
import com.github.joostvdg.gitdoing.api.NoteItem;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.time.LocalDateTime;
import java.util.Set;

public class WindowTestController {

    @FXML
    private VBox dataContainer;

    private final Note note;

    public WindowTestController(Note note) {
        this.note = note;
    }

    public Note getNote(){
        return note;
    }

    @FXML
    public void initialize() {
        System.out.println("Note (modal controller): " + note.getIdentifier() + " - " + note.getName());
        var labelFont = new Font("Arial", 24);
        var spacing = 10;

        HBox identifier = createTextLine("Identifier", note.getIdentifier());
        HBox name = createTextLine("Name", note.getName());
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

//        private String text;
//        private final Set<NoteItem> items;

        dataContainer.getChildren().addAll(identifier, name, description, closed, created, lastEdit, lastOpened, label);
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
