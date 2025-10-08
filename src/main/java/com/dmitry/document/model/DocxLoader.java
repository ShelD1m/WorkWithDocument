package com.dmitry.document.model;

import com.dmitry.document.model.DocumentModel;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.IOException;
import java.nio.file.*;

public final class DocxLoader {
    private DocxLoader() {}
    public static DocumentModel load(Path path) throws IOException {
        try (var in = Files.newInputStream(path)) {
            return new DocumentModel(new XWPFDocument(in));
        }
    }
}
