package com.github.joostvdg.gitdoing.core.export;

import com.github.joostvdg.gitdoing.api.Note;
import com.github.joostvdg.gitdoing.api.exporters.Exporter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.ByteArrayOutputStream;

public class XmlExporter implements Exporter {

    private static final String NAME = "XML_PARSER";
    private JAXBContext context;


    public XmlExporter(){
        try {
            init();
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    public void init() throws JAXBException {
        this.context = JAXBContext.newInstance(Note.class);
    }

    @Override
    public String export(Note note) {
        String output = "";
        try {
            Marshaller marshaller = this.context.createMarshaller();
            var rawOutput = new ByteArrayOutputStream();
            marshaller.marshal(note, rawOutput);
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
