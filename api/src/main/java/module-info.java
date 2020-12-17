module com.github.joostvdg.gitdoing.api {

    requires java.xml.bind;

    exports com.github.joostvdg.gitdoing.api to com.github.joostvdg.gitdoing.core, com.github.joostvdg.gitdoing.gui;
    exports com.github.joostvdg.gitdoing.api.exporters; // to com.github.joostvdg.gitdoing.core, com.github.joostvdg.gitdoing.gui;

    uses com.github.joostvdg.gitdoing.api.exporters.Exporter;

    opens com.github.joostvdg.gitdoing.api to java.xml.bind;
}