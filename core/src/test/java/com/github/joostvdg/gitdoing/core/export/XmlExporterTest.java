package com.github.joostvdg.gitdoing.core.export;

import com.github.joostvdg.gitdoing.api.Note;
import com.github.joostvdg.gitdoing.api.NoteItem;
import com.github.joostvdg.gitdoing.api.NoteItemKind;
import com.github.joostvdg.gitdoing.api.Notes;
import com.github.joostvdg.gitdoing.api.exporters.Exporter;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.diff.Diff;

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

    private final String formattedExample1 = """
<notes>
  <note>
    <identifier>XXNOTEIDXX</identifier>
    <name>Test</name>
    <description>My First Test</description>
    <text>= This Is Now My Text</text>
    <closed>false</closed>
    <created/>
    <labels>
      <label>or-not-to-study</label>
      <label>study</label>
    </labels>
    <items>
      <item>
        <id>XXITEM1IDXX</id>
        <link>http://test.this</link>
        <comment>No Comment</comment>
        <kind>ARTICLE</kind>
        <done>false</done>
      </item>
    </items>
  </note>
  <note>
    <identifier>XXNOTE2IDXX</identifier>
    <name>Test</name>
    <description>My First Test</description>
    <text>= This Is Now My Text</text>
    <closed>false</closed>
    <created/>
    <labels/>
    <items>
      <item>
        <id>XXITEM2IDXX</id>
        <link>http://test.that</link>
        <comment>A Comment</comment>
        <kind>OTHER</kind>
        <done>false</done>
      </item>
    </items>
  </note>
</notes>
""";

    @Test
    public void mustExportValidXml() throws ParserConfigurationException, IOException, SAXException, XPathExpressionException {
        Note note = new Note("Test", "My First Test");
        note.updateText("= This Is Now My Text");
        var noteItem1 = new NoteItem("http://test.this", "No Comment", NoteItemKind.ARTICLE);
        note.addItem(noteItem1);
        note.addLabel("study");

        Exporter exporter = new XmlStreamExporter();
        var xmlOutput = exporter.export(note);

        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = builderFactory.newDocumentBuilder();
        InputStream is = new ByteArrayInputStream(xmlOutput.getBytes());
        Document xmlDocument = builder.parse(is);
        XPath xPath = XPathFactory.newInstance().newXPath();
        String nameExpression = "//note[1]/name/text()";
        var nameNode = (Node) xPath.compile(nameExpression).evaluate(xmlDocument, XPathConstants.NODE);
        String itemLinkExpression = "//note[1]/items//item[1]/link/text()";
        var itemLinkNode = (Node) xPath.compile(itemLinkExpression).evaluate(xmlDocument, XPathConstants.NODE);
        assertEquals("Test", nameNode.getNodeValue());
        assertEquals("http://test.this", itemLinkNode.getNodeValue());
    }

    @Test
    public void mustExportValidAndFormattedXML(){
        Note note = new Note("Test", "My First Test");
        note.updateText("= This Is Now My Text");
        var noteItem1 = new NoteItem("http://test.this", "No Comment", NoteItemKind.ARTICLE);
        note.addItem(noteItem1);
        note.addLabel("study");
        note.addLabel("or-not-to-study");
        Notes notes = new Notes();
        notes.add(note);

        Note note2 = new Note("Test", "My First Test");
        var noteItem2 = new NoteItem("http://test.that", "A Comment", NoteItemKind.OTHER);
        note2.updateText("= This Is Now My Text");
        note2.addItem(noteItem2);
        notes.add(note2);

        Exporter exporter = new XmlFormattedExporter();
        var xmlOutput = exporter.exportAll(notes);
        System.out.println(xmlOutput);
        var expectedOutput = formattedExample1.replace("XXNOTEIDXX", note.getIdentifier());
        expectedOutput = expectedOutput.replace("XXNOTE2IDXX", note2.getIdentifier());
        expectedOutput = expectedOutput.replace("XXITEM1IDXX", noteItem1.getId());
        expectedOutput = expectedOutput.replace("XXITEM2IDXX", noteItem2.getId());
        expectedOutput = expectedOutput.replaceAll("\\r\\n", "\n");
        expectedOutput = expectedOutput.replaceAll("\\r", "\n");

        xmlOutput = xmlOutput.replaceAll("\\r\\n", "\n");
        xmlOutput = xmlOutput.replaceAll("\\r", "\n");
        assertEquals(expectedOutput, xmlOutput);
        final Diff documentDiff = DiffBuilder
                .compare(xmlOutput)
                .withTest(expectedOutput)
                .withNodeFilter(node -> !node.getNodeName().equals("id"))
                .withNodeFilter(node -> !node.getNodeName().equals("identifier"))
                .build();
        assertFalse(documentDiff.hasDifferences());
    }

}