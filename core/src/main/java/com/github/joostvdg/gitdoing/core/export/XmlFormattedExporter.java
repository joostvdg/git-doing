package com.github.joostvdg.gitdoing.core.export;

import com.github.joostvdg.gitdoing.api.Note;
import com.github.joostvdg.gitdoing.api.Notes;
import com.github.joostvdg.gitdoing.api.exporters.Exporter;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

public class XmlFormattedExporter implements Exporter {
    @Override
    public String export(Note note) {
        return null;
    }

    @Override
    public String exportAll(Notes notes) {
        // process with a DOM and format the output/pretty print

        JAXBContext jaxbContext = null;
        try {
            jaxbContext = JAXBContext.newInstance(Notes.class);
        } catch (JAXBException e) {
            System.out.println("Could not create JAXB Context");
            e.printStackTrace(); // TODO do proper logging/error handling
        }

        Marshaller jaxbMarshaller = null;
        try {
            jaxbMarshaller = jaxbContext.createMarshaller();
        } catch (JAXBException e) {
            System.out.println("Could not create (jaxb) marshaller");
            e.printStackTrace(); // TODO do proper logging/error handling
        }
        Writer stringExportWriter = new StringWriter();
        try {
            jaxbMarshaller.marshal(notes, stringExportWriter);
        } catch (JAXBException e) {
            System.out.println("Could not marshal notes");
            e.printStackTrace();
        }

        Transformer transformer = null;
        try {
            transformer = TransformerFactory.newInstance().newTransformer();
        } catch (TransformerConfigurationException e) {
            System.out.println("Could not create transformer");
            e.printStackTrace();
        }
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");

        String xml = stringExportWriter.toString();
        Document document = null;
        try {
            document = DocumentBuilderFactory.newInstance()
                .newDocumentBuilder()
                .parse(new InputSource(new ByteArrayInputStream(xml.getBytes("utf-8"))));
        } catch (SAXException | IOException | ParserConfigurationException e) {
            System.out.println("Cannot parse XML output into Document");
            e.printStackTrace();
        }
        StreamResult result = new StreamResult(new StringWriter());
        DOMSource source = new DOMSource(document);
        try {
            transformer.transform(source, result);
        } catch (TransformerException e) {
            System.out.println("Could not transform document to formatted string");
            e.printStackTrace();
        }
        return result.getWriter().toString();
    }

    @Override
    public String name() {
        return null;
    }
}
