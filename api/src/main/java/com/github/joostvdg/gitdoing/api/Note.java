package com.github.joostvdg.gitdoing.api;


import java.time.LocalDateTime;
import java.util.*;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Note {

    private final String identifier;
    private final String name;
    private final String description;
    private String text;
    private boolean closed;
    private final LocalDateTime created;
    private LocalDateTime lastEdit;
    private LocalDateTime lastOpened;
    private final Set<String> labels;
    private final Set<NoteItem> items;

    public Note(String name, String description) {
        this.identifier = UUID.randomUUID().toString();
        this.name = name;
        this.description = description;
        created = LocalDateTime.now();
        text = "";
        items = new TreeSet<>();
        labels = new TreeSet<>();
        closed = false;
    }

    // For JAXB
    Note() {
        closed = false;
        identifier = null;
        name = null;
        description = null;
        created = null;
        labels = null;
        items = null;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getText() {
        return text;
    }

    public void updateText(String text) {
        this.text = text;
    }
    public boolean isClosed() {
        return closed;
    }

    public void close() {
        this.closed = true;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public LocalDateTime getLastEdit() {
        return lastEdit;
    }

    public void setLastEdit(LocalDateTime lastEdit) {
        this.lastEdit = lastEdit;
    }

    public LocalDateTime getLastOpened() {
        return lastOpened;
    }

    public void setLastOpened(LocalDateTime lastOpened) {
        this.lastOpened = lastOpened;
    }

    public Set<String> getLabels() {
        return Collections.unmodifiableSet(labels);
    }

    public boolean addLabel(String label) {
        return labels.add(label);
    }

    public Set<NoteItem> getItems() {
        return Collections.unmodifiableSet(items);
    }

    public boolean addItem(NoteItem item) {
        return items.add(item);
    }

    @Override
    public String toString() {
        return "Note{" +
                "identifier='" + identifier + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", text='" + text + '\'' +
                ", created=" + created +
                ", lastEdit=" + lastEdit +
                ", lastOpened=" + lastOpened +
                ", labels=" + labels +
                ", number of items=" + items.size() +
                '}';
    }

}
