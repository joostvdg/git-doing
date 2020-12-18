package com.github.joostvdg.gitdoing.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Hello world!
 *
 */
public class App extends Application  {

    @Override
    public void start(Stage stage) throws Exception {
        System.out.println("Starting GUI!");
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/main.fxml"));
        stage.setTitle("GitDoing v0.0.1");
        stage.setScene(new Scene(root, 800, 500));
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        System.out.println("Shutting Down!");
    }
}
