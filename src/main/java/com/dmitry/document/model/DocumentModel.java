package com.dmitry.document.model;

import org.apache.poi.xwpf.usermodel.XWPFDocument;

public class DocumentModel implements AutoCloseable {
    private final XWPFDocument doc;
    public DocumentModel(XWPFDocument doc) { this.doc = doc; }
    public XWPFDocument raw() { return doc; }
    @Override public void close() throws Exception { doc.close(); }
}
