package com.github.joostvdg.gitdoing.gui;

import com.github.joostvdg.gitdoing.api.Note;
import com.github.joostvdg.gitdoing.api.NoteItem;
import com.github.joostvdg.gitdoing.api.NoteItemKind;
import com.github.joostvdg.gitdoing.api.exporters.Exporter;

import java.util.List;
import java.util.ServiceLoader;
import java.util.stream.Collectors;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        printVersion();
//        System.out.println("All modules in Boot Layer:");
//        ModuleLayer.boot().modules().forEach(System.out::println);
        List<Module> myModules = ModuleLayer.boot().modules()
                .stream()
                .filter(module -> module.getName().startsWith("com.github.joostvdg"))
                .collect(Collectors.toList());
        myModules.forEach(module -> printModuleInfo(module));

        Note note = new Note("Test", "My First Test");
        note.updateText("= This Is Now My Text");
        var noteItem1 = new NoteItem("http://test.this", "No Comment", NoteItemKind.ARTICLE);
        note.addItem(noteItem1);
        var noteItem2 = new NoteItem("http://test.that", "A Comment", NoteItemKind.OTHER);
        note.addItem(noteItem2);
        note.addLabel("study");


//        Iterable<Exporter> exporters = Exporter.getExporters();
//        for (Exporter exporter : exporters) {
//            String noteExport = exporter.export(note);
//        }

        ServiceLoader<Exporter> exporters = (ServiceLoader<Exporter>)Exporter.loadExporters();
        if (exporters.stream().count() < 1 ) {
            System.out.println("Could not find Exporters");
        }
        System.out.println();
        exporters.stream()
                .map(ServiceLoader.Provider::get)
                .forEach(exporter -> System.out.println( exporter.name() + ": " + exporter.export(note)));

        System.out.println("------------------------------");


    }

    private static void printModuleInfo(Module module) {
        StringBuilder moduleInfo = new StringBuilder();
        moduleInfo.append("----------------\n");
        moduleInfo.append("Module: ");
        moduleInfo.append(module.getName());
        moduleInfo.append("\nPackages: ");
        module.getPackages().forEach(s -> moduleInfo.append(packageInfo(module,s)));
        moduleInfo.append("\n----------------");
        System.out.println(moduleInfo.toString());
    }

    public static void printVersion() {
        Runtime.Version version = Runtime.version();

        System.out.println("Version Feature:" + version.feature());
        System.out.println("Version Interim:" + version.interim());
        System.out.println("Version Update:" + version.update());
        System.out.println("Version Patch:" + version.patch());
    }

    private static String packageInfo(Module module, String packageName) {
        if (module.getName().startsWith("java.")) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        builder.append("\n Package: ");
        builder.append(packageName);
        if (module.isOpen(packageName)) {
            builder.append(", open");
        }
        if (module.isExported(packageName)) {
            builder.append(", exported");
        }

        return builder.toString();
    }
}
