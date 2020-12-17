package com.github.joostvdg.gitdoing.core.export;

import com.github.joostvdg.gitdoing.api.Note;
import com.github.joostvdg.gitdoing.api.exporters.Exporter;

public class OutputTest implements Exporter {


    public OutputTest() {
    }

    @Override
    public String export(Note note) {
        return note.toString();
    }

    @Override
    public String name() {
        return "Test";
    }
}
