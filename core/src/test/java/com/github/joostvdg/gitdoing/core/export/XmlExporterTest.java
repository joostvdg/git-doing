package com.github.joostvdg.gitdoing.core.export;

import com.github.joostvdg.gitdoing.api.Note;
import com.github.joostvdg.gitdoing.api.NoteItem;
import com.github.joostvdg.gitdoing.api.NoteItemKind;
import com.github.joostvdg.gitdoing.api.exporters.Exporter;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

class XmlExporterTest {
    private final String example1 = """
    <?xml version="1.0" encoding="UTF-8" standalone="yes"?><note><identifier>4c622236-0acc-45b0-ace5-8db4b0fc3e23</identifier><name>Test</name><description>My First Test</description><text>= This Is Now My Text</text><closed>false</closed><created/><labels>study</labels><items><id>68d1ced9-7be6-4ef5-8c6c-d847ceaf8e60</id><link>http://test.that</link><comment>A Comment</comment><kind>OTHER</kind><done>false</done></items><items><id>c7e042cd-58d2-4ca3-83ca-0a90664aed08</id><link>http://test.this</link><comment>No Comment</comment><kind>ARTICLE</kind><done>false</done></items></note>
    """;

    @Test
    public void mustExportValidXml() throws ParserConfigurationException, IOException, SAXException, XPathExpressionException {
        Note note = new Note("Test", "My First Test");
        note.updateText("= This Is Now My Text");
        var noteItem1 = new NoteItem("http://test.this", "No Comment", NoteItemKind.ARTICLE);
        note.addItem(noteItem1);
        var noteItem2 = new NoteItem("http://test.that", "A Comment", NoteItemKind.OTHER);
        note.addItem(noteItem2);
        note.addLabel("study");


        Exporter exporter = new XmlExporter();
        var xmlOutput = exporter.export(note);

        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = builderFactory.newDocumentBuilder();
        InputStream is = new ByteArrayInputStream(xmlOutput.getBytes());
        Document xmlDocument = builder.parse(is);
        XPath xPath = XPathFactory.newInstance().newXPath();
        String nameExpression = "/note/name/text()";
        var nameNode = (Node) xPath.compile(nameExpression).evaluate(xmlDocument, XPathConstants.NODE);
        String itemLinkExpression = "/note/items[1]/link/text()";
        var itemLinkNode = (Node) xPath.compile(itemLinkExpression).evaluate(xmlDocument, XPathConstants.NODE);
        assertEquals("Test", nameNode.getNodeValue());
        assertTrue(itemLinkNode.getNodeValue().equals("http://test.that")
            || itemLinkNode.getNodeValue().equals("http://test.this")  );
    }

}