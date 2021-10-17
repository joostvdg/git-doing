package com.github.joostvdg.gitdoing.gui.controller;

import com.github.joostvdg.gitdoing.api.Note;
import com.github.joostvdg.gitdoing.gui.ActionButtonTableCell;
import com.github.joostvdg.gitdoing.gui.DemoPopulator;
import com.github.joostvdg.gitdoing.gui.OpenModalTableCell;
import com.github.joostvdg.gitdoing.gui.config.GitSource;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;

public class MainController {


    @FXML
    private VBox dataContainer;
    @FXML
    private TableView tableView;

    @FXML
    private Menu fileMenu;

    private final GitSource gitSourceConfig;

    private DemoPopulator demoData;

    public MainController(){
        gitSourceConfig = new GitSource();
    }

    @FXML
    private void initialize() {
        initData();
        initMenu();
        initTable();
    }

    private void initData(){
        demoData = new DemoPopulator();
    }

    private void initMenu(){
        MenuItem menuItem1 = new MenuItem("Export Current Data");
        fileMenu.getItems().add(menuItem1);
        menuItem1.setOnAction((ActionEvent e) -> {
            System.out.println("Action event happened: " + e.toString());
            System.out.println("Attempt To Export Data");
            openExportWindow(e);
        });
        System.out.println("Menu Initialized");
    }

    private void initTable() {
        ObservableList<Note> masterData = FXCollections.observableArrayList();
        masterData.addAll(demoData.getEntries().getEntries());

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
        System.out.println("Data Table Initialized");
    }

    public void openExportWindow(ActionEvent event) {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/NoteExport.fxml"));

        NotesExportController notesExportController = new NotesExportController(demoData.getEntries());
        try {
            loader.setController(notesExportController);
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setScene(new Scene(loader.getRoot()));
        stage.setTitle("Notes Export");
        stage.initModality(Modality.WINDOW_MODAL);
//        stage.initOwner(
//                ((MenuItem)event.getSource()).get );
        stage.show();
    }
}
