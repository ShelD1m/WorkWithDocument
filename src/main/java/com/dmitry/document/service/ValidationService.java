package com.dmitry.document.service;


import com.dmitry.document.engine.RuleEngine;
import com.dmitry.document.engine.RulesConfig;
import com.dmitry.document.model.DocumentModel;
import com.dmitry.document.model.DocxLoader;
import org.springframework.stereotype.Service;

import java.io.OutputStream;
import java.nio.file.*;

@Service
public class ValidationService {
    private final RulesConfig rules;
    public ValidationService(RulesConfig rules) { this.rules = rules; }

    public RuleEngine.Report validate(byte[] docxBytes, boolean fix, OutputStream fixedOut) throws Exception {
        Path tmp = Files.createTempFile("in", ".docx");
        Files.write(tmp, docxBytes);
        try (DocumentModel dm = DocxLoader.load(tmp)) {
            var engine = new RuleEngine(rules);
            var report = engine.run(dm, fix);
            if (fix && fixedOut != null) {
                try (var os = fixedOut) { dm.raw().write(os); }
            }
            return report;
        } finally {
            Files.deleteIfExists(tmp);
        }
    }
}
