package com.github.joostvdg.gitdoing.core.export;

import com.github.joostvdg.gitdoing.api.Note;
import com.github.joostvdg.gitdoing.api.Notes;
import com.github.joostvdg.gitdoing.api.exporters.Exporter;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import java.io.ByteArrayOutputStream;

public class XmlStreamExporter implements Exporter {

    private static final String NAME = "XML_STREAM";
    private JAXBContext noteContext;
    private JAXBContext notesContext;


    public XmlStreamExporter(){
        try {
            init();
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    public void init() throws JAXBException {
        this.noteContext = JAXBContext.newInstance(Note.class);
        this.notesContext = JAXBContext.newInstance(Notes.class);
    }

    @Override
    public String export(Note note) {
        String output = "";
        try {
            Marshaller marshaller = this.noteContext.createMarshaller();
            var rawOutput = new ByteArrayOutputStream();
            marshaller.marshal(note, rawOutput);
            output = rawOutput.toString();
        } catch (JAXBException e) {
            e.printStackTrace();
        }

        return output;
    }

    @Override
    public String exportAll(Notes notes) {
        String output = "";
        try {
            Marshaller marshaller = this.notesContext.createMarshaller();
            var rawOutput = new ByteArrayOutputStream();
            marshaller.marshal(notes, rawOutput);
            output = rawOutput.toString();
        } catch (JAXBException e) {
            e.printStackTrace();
        }

        return output;
    }

    @Override
    public String name() {
        return NAME;
    }
}
