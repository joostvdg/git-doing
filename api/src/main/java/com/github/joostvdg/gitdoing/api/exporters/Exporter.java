package com.github.joostvdg.gitdoing.api.exporters;

import com.github.joostvdg.gitdoing.api.Note;
import com.github.joostvdg.gitdoing.api.Notes;

import java.util.ServiceLoader;

public interface Exporter {
    String export(Note note);

    String exportAll(Notes notes);

    String name();

    public static Iterable<Exporter> loadExporters() {
        return ServiceLoader.load(Exporter.class);
    }
}
