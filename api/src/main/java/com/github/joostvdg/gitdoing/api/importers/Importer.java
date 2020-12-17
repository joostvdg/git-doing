package com.github.joostvdg.gitdoing.api.importers;


import com.github.joostvdg.gitdoing.api.Note;

import java.util.ServiceLoader;

public interface Importer {
    Note importNote(String note);
    String name();

    public static Iterable<Importer> loadExporters() {
        return ServiceLoader.load(Importer.class);
    }
}
