module com.github.joostvdg.gitdoing.gui {

    requires com.github.joostvdg.gitdoing.api;
    requires com.github.joostvdg.gitdoing.core;
    requires javafx.controls;
    requires javafx.fxml;

    opens com.github.joostvdg.gitdoing.gui to javafx.fxml;
    exports com.github.joostvdg.gitdoing.gui to javafx.graphics;


    uses com.github.joostvdg.gitdoing.api.exporters.Exporter;
}