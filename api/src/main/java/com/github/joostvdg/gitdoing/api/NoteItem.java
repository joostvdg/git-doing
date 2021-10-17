package com.github.joostvdg.gitdoing.api;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.time.LocalDateTime;
import java.util.UUID;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "item")
public class NoteItem implements Comparable {

    // TODO: should this be an URI?
    private final String id;
    private final String link;
    private final String comment;
    private final NoteItemKind kind;
    private Boolean done;
    private LocalDateTime reminder;

    public NoteItem(String link, String comment, NoteItemKind kind) {
        this.id = UUID.randomUUID().toString();
        this.link = link;
        this.comment = comment;
        this.kind = kind;
        done = false;
        reminder = null;
    }

    NoteItem() {
        id = null;
        link = null;
        comment = null;
        kind = null;
    }

    public String getLink() {
        return link;
    }

    public String getId(){
        return this.id;
    }

    public String getComment() {
        return comment;
    }

    public NoteItemKind getKind() {
        return kind;
    }

    public Boolean isDone() {
        return done;
    }

    public void setDone(Boolean done) {
        this.done = done;
    }

    public LocalDateTime getReminder() {
        return reminder;
    }

    public void setReminder(LocalDateTime reminder) {
        this.reminder = reminder;
    }

    @Override
    public int compareTo(Object o) {
        if (o instanceof NoteItem noteItem) {
            return id.compareTo(noteItem.getId());
        }
        return -1;
    }
}
