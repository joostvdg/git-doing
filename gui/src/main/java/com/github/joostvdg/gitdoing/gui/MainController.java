package com.github.joostvdg.gitdoing.gui;

import com.github.joostvdg.gitdoing.api.Note;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.time.LocalDateTime;

public class MainController {


    @FXML
    private VBox dataContainer;
    @FXML
    private TableView tableView;

    @FXML
    private void initialize() {
        initTable();
    }

    private void initTable() {
        ObservableList<Note> masterData = FXCollections.observableArrayList();
        var demoData = new DemoPopulator();
        masterData.addAll(demoData.getNotes());

        tableView = new TableView<>(FXCollections.observableList(masterData));
        TableColumn id = new TableColumn("ID");
        id.setCellValueFactory(new PropertyValueFactory("identifier"));
        TableColumn name = new TableColumn("NAME");
        name.setCellValueFactory(new PropertyValueFactory("name"));
        TableColumn description = new TableColumn("DESCRIPTION");
        description.setCellValueFactory(new PropertyValueFactory("description"));
        TableColumn<TableView, LocalDateTime> created = new TableColumn("DATE CREATED");
        created.setCellValueFactory(new PropertyValueFactory("created"));
        TableColumn closed = new TableColumn("CLOSED");
        closed.setCellValueFactory(new PropertyValueFactory("closed"));

        TableColumn removeCol = new TableColumn("Remove");
        removeCol.setCellFactory(ActionButtonTableCell.<Note>forTableColumn("Remove", (Note n) -> {
            System.out.println("Removing (not really) note: " + n);
            tableView.getItems().remove(n);
            return n;
        }));
        TableColumn hideCol = new TableColumn("Hide");
        hideCol.setCellFactory(ActionButtonTableCell.<Note>forTableColumn("Hide", (Note n) -> {
            System.out.println("Hiding (temporarily) note: " + n);
            tableView.getItems().remove(n);
            return n;
        }));

        TableColumn openCol = new TableColumn("Open");
        openCol.setCellFactory(OpenModalTableCell.<Note>forTableColumn("Open", (Note n) -> {
            // how do we trigger a new window?
            System.out.println("Opening a new modal?!");
            System.out.println("N = " + n.getClass().getSimpleName());
            return n;
        }));

        tableView.getColumns().addAll(id, name, description, created, closed, removeCol, hideCol, openCol);
        dataContainer.getChildren().add(tableView);
    }

}
