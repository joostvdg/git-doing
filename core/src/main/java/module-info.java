import com.github.joostvdg.gitdoing.core.export.XmlStreamExporter;

module com.github.joostvdg.gitdoing.core {

    requires jakarta.xml.bind;

    requires com.github.joostvdg.gitdoing.api;

    exports com.github.joostvdg.gitdoing.core.export;

    provides com.github.joostvdg.gitdoing.api.exporters.Exporter with
            com.github.joostvdg.gitdoing.core.export.OutputTest,
            XmlStreamExporter;

    // Required for Surefire?
    opens com.github.joostvdg.gitdoing.core.export;
}