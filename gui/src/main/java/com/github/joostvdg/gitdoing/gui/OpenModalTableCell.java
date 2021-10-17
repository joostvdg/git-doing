package com.github.joostvdg.gitdoing.gui;

import com.github.joostvdg.gitdoing.api.Note;
import com.github.joostvdg.gitdoing.gui.controller.ViewNoteController;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.util.function.Function;

public class OpenModalTableCell<S> extends TableCell<S, Button> {

    private final Button actionButton;

    public OpenModalTableCell(String label, Function< S, S> function) {
        this.getStyleClass().add("action-button-table-cell");

        this.actionButton = new Button(label);
        this.actionButton.setOnAction((ActionEvent e) -> {
            System.out.println("Action event happened: " + e.toString());
            clickShow(e);
        });
        this.actionButton.setMaxWidth(Double.MAX_VALUE);
    }

    public static <S> Callback<TableColumn<S, Button>, TableCell<S, Button>> forTableColumn(String label, Function< S, S> function) {
        return param -> new OpenModalTableCell<>(label, function);
    }

    private void clickShow(ActionEvent event) {
        System.out.println("Opening modal panel");

        var cellObject = getTableView().getItems().get(getIndex());;
        System.out.println("Cell Object class: " + cellObject.getClass().getSimpleName());

        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/ViewNote.fxml"));
        String noteInfoLine = "";
        if (cellObject instanceof Note n) {
            var controller = new ViewNoteController(n);
            loader.setController(controller);
            noteInfoLine = "" + n.getName() + " (" + n.getIdentifier() + ")";
            System.out.println(noteInfoLine);
        }

        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setScene(new Scene(loader.getRoot()));
        stage.setTitle("Note - " + noteInfoLine);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(
                ((Node)event.getSource()).getScene().getWindow() );
        stage.show();
    }

    @Override
    public void updateItem(Button item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
            setGraphic(null);
        } else {
            setGraphic(actionButton);
        }
    }
}
