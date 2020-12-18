package com.github.joostvdg.gitdoing.gui;

import com.github.joostvdg.gitdoing.api.Note;
import com.github.joostvdg.gitdoing.api.NoteItem;
import com.github.joostvdg.gitdoing.api.NoteItemKind;
import com.github.joostvdg.gitdoing.api.exporters.Exporter;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

public class DemoPopulator {

    private final List<Note> notes;

    public DemoPopulator() {
        Note note1 = new Note("Test", "My First Test");
        note1.updateText("= This Is Now My Text");
        var noteItem1 = new NoteItem("http://test.this", "No Comment", NoteItemKind.ARTICLE);
        note1.addItem(noteItem1);
        var noteItem2 = new NoteItem("http://test.that", "A Comment", NoteItemKind.OTHER);
        note1.addItem(noteItem2);
        note1.addLabel("study");

        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Note note2 = new Note("Something", "Another One");
        note2.updateText("= More Text");
        note2.addItem(noteItem1);
        note2.addItem(noteItem2);
        note2.addLabel("study");

        notes = new ArrayList<>();
        notes.add(note1);
        notes.add(note2);

        ServiceLoader<Exporter> exporters = (ServiceLoader<Exporter>)Exporter.loadExporters();
        if (exporters.stream().count() < 1 ) {
            System.out.println("Could not find Exporters");
        }
        System.out.println();
        exporters.stream()
                .map(ServiceLoader.Provider::get)
                .forEach(exporter -> System.out.println( exporter.name() + ": " + exporter.export(note1)));
    }

    public List<Note> getNotes(){
        return notes;
    }
}
