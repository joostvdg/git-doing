package com.github.joostvdg.gitdoing.api;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.time.LocalDateTime;
import java.util.UUID;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class NoteItem implements Comparable{

    // TODO: should this be an URI?
    private final String id;
    private final String link;
    private final String comment;
    private final NoteItemKind kind;
    private boolean done;
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

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
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
        if (o instanceof NoteItem) {
            return id.compareTo(((NoteItem) o).getId());
        }
        return -1;
    }
}
