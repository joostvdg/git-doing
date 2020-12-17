package com.github.joostvdg.gitdoing.api.exporters;

import java.util.Optional;
import java.util.ServiceLoader;

public class ExporterProvider {

    public Optional<Exporter> loadPaymentService() {
        return ServiceLoader
                .load(Exporter.class)
                .findFirst();
    }
}
