package com.github.joostvdg.gitdoing.gui.controller;

import com.github.joostvdg.gitdoing.api.Notes;
import com.github.joostvdg.gitdoing.api.exporters.Exporter;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

import java.util.ServiceLoader;

public class NotesExportController {

    @FXML
    private VBox dataContainer;

    private final Notes notes;

    public NotesExportController(Notes notes) {
        this.notes = notes;
    }

    @FXML
    private void initialize() {
        ServiceLoader<Exporter> exporters = (ServiceLoader<Exporter>)Exporter.loadExporters();
        if (exporters.stream().count() < 1 ) {
            System.out.println("Could not find Exporters");
        }
        System.out.println();
        Exporter xmlExporter = null;
        var exporterIterator = exporters.iterator();
        while (exporterIterator.hasNext()) {
            var exporter = exporterIterator.next();
            if ("XML".equals(exporter.name())) {
                xmlExporter = exporter;
            }
        }
        if ( xmlExporter != null) {
            processExport(xmlExporter);
        }
    }

    private void processExport(Exporter xmlExporter) {
        String rawNotesExport = xmlExporter.exportAll(notes);
        TextArea exportText = new TextArea();
        exportText.setWrapText(true);
        exportText.setMinSize(600, 800);
        exportText.setEditable(false);
        String notesExport = formatXmlData(rawNotesExport);
        exportText.setText(rawNotesExport);
        dataContainer.getChildren().add(exportText);
    }

    private String formatXmlData(String rawNotesExport) {
        StringBuilder formattedText = new StringBuilder();
        // step one: add line endings

        rawNotesExport = rawNotesExport.replace("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>", "");
        formattedText.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>");
        formattedText.append("\n");

        int cursor = 0;
        int nextStart = rawNotesExport.indexOf("<", cursor);
        int nextEnd = rawNotesExport.indexOf(">", cursor);
        int nextClosingStart = 0;
        System.out.println("Raw Notes Export: " + rawNotesExport);
        int lineCount = 0;

        // <notes> <notes> <id>bababa</id>

        // find < and matching >
            // find nextStart: <
            // find nextEnd: >
            // find nextNextStart: <
            // find nextNextClosingStart: </
            // if nextNextStart != nextClosingStart -> chop at nextStart till nextNextStart
            // if nextNextStart == nextClosingStart -> chop at nextStart till nextNextEnd (>)


        int openTagCount = 0;
        int spacesPerIndent = 4;
        while(nextStart >= 0) {
            if (lineCount > 100) {
                break;
            }
            if (cursor > rawNotesExport.length()) {
                break;
            }
            // find < and matching >
            // find nextStart: <
            // find nextEnd: >
            // find nextNextStart: <
            // find nextNextClosingStart: </
            // if nextNextStart != nextClosingStart -> chop at nextStart till nextNextStart (cursor = nextNextStart)
            // if nextNextStart == nextClosingStart -> chop at nextStart till nextNextEnd (>) (cursor = nextNextEnd)
            // else -> chop at nextStart till nextNextStart

            nextStart = rawNotesExport.indexOf("<", cursor);

            if (nextStart == -1) {
                break;
            }

            nextEnd = rawNotesExport.indexOf(">", cursor);
            nextClosingStart = rawNotesExport.indexOf("</", cursor);
            var nextNextStart = rawNotesExport.indexOf("<", nextEnd);
            var nextNextClosingStart = rawNotesExport.indexOf("</", nextEnd);
            var nextNextEnd = rawNotesExport.indexOf(">", nextNextStart);
            var nextNextNextClosingStart = rawNotesExport.indexOf("</", nextNextClosingStart);

            String substring = "";
            if (nextNextStart >=0 && nextNextStart == nextNextClosingStart) {
                substring = rawNotesExport.substring(nextStart, nextNextEnd +1 );
                cursor = nextNextEnd;
            } else {
                substring = rawNotesExport.substring(nextStart, nextNextStart );
                cursor = nextNextStart;
            }

            // Indenting: if we get < and > before a </ its open
            // if there's </ is the first <, close one

            // 1 = nextStart
            // 2 = nextEnd
            // 3 = nextNextStart
            // 4 = nextNextEnd
            // 5 = nextClosingStart
            // <(1)  >(2) <(3)  >(4) -> no new open tag
            // <(1)  >(2) </(5) >(4) -> no new open tag
            // <(1)  >(2) </(5)  >(4) </(5) -> close a tag
            //   = if nextStart = nextClosingStart && nextNextStart == nextNextClosing Start tag--
            //   = elseif nextStart < nextClosingStart && nextNextStart < nextNextClosingStart tag++

            if (!substring.isEmpty()) {
                if (nextNextStart < nextClosingStart && nextStart < nextClosingStart) {
                    openTagCount++;
                }

                if (nextStart == nextClosingStart && nextNextStart == nextNextNextClosingStart) {
                    openTagCount--;
                }
                substring = substring.indent(openTagCount * spacesPerIndent);
                System.out.println("Appending: " + substring);
                formattedText.append(substring);
                formattedText.append("\n");
            }


            lineCount++;
        }

        return formattedText.toString();
    }
}
