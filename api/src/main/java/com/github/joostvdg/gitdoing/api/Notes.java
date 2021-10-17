package com.github.joostvdg.gitdoing.api;

import jakarta.xml.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Notes {

    @XmlElement(name = "note")
    private final List<Note> entries;

    public Notes() {
        this.entries = new ArrayList<>();
    }

    public List<Note> getEntries() {
        return Collections.unmodifiableList(entries);
    }

    public void add(Note note) {
        this.entries.add(note);
    }
}
