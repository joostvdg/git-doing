module com.github.joostvdg.gitdoing.core {

    requires java.xml.bind;

    requires com.github.joostvdg.gitdoing.api;

    exports com.github.joostvdg.gitdoing.core.export;

    provides com.github.joostvdg.gitdoing.api.exporters.Exporter with
            com.github.joostvdg.gitdoing.core.export.OutputTest,
            com.github.joostvdg.gitdoing.core.export.XmlExporter;

    // Required for Surefire?
    opens com.github.joostvdg.gitdoing.core.export;
}