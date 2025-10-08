package com.dmitry.document.rules;

import com.dmitry.document.engine.RulesConfig;
import com.dmitry.document.model.DocumentModel;
import com.dmitry.document.model.Location;
import com.dmitry.document.rules.Severity;
import com.dmitry.document.rules.ValidationIssue;

import org.apache.poi.xwpf.usermodel.*;

import java.util.ArrayList;
import java.util.List;

public class TypographyRule implements Rule {
    @Override public String id() { return "typography"; }

    @Override
    public List<ValidationIssue> validate(DocumentModel dm, RulesConfig cfg) {
        var issues = new ArrayList<ValidationIssue>();
        var doc = dm.raw();
        var expectedFont = cfg.fontFamily();
        var expectedSize = cfg.fontSizePt();
        var expectedAlign = ParagraphAlignment.BOTH; // justify
        var expectedLine = cfg.lineSpacing();
        var expectedFirstIndent = (int)Math.round(cfg.firstLineIndentCm() * 567); // 1 cm = 567 twips

        int pIndex = 0;
        for (XWPFParagraph p : doc.getParagraphs()) {
            pIndex++;

            if (p.getAlignment() != expectedAlign) {
                issues.add(new ValidationIssue(id(), Severity.WARNING, Location.para(pIndex),
                        "Неверное выравнивание абзаца", "justify", String.valueOf(p.getAlignment()), true));
            }

            double spacing = p.getSpacingBetween();
            if (Double.compare(spacing, expectedLine) != 0) {
                issues.add(new ValidationIssue(id(), Severity.WARNING, Location.para(pIndex),
                        "Межстрочный интервал отличается", String.valueOf(expectedLine), String.valueOf(spacing), true));
            }

            if (p.getIndentationFirstLine() != expectedFirstIndent) {
                issues.add(new ValidationIssue(id(), Severity.INFO, Location.para(pIndex),
                        "Неверный отступ первой строки", expectedFirstIndent+" twips",
                        p.getIndentationFirstLine()+" twips", true));
            }

            for (XWPFRun run : p.getRuns()) {
                String f = run.getFontFamily();
                int s = run.getFontSize();

                if (f == null || !f.equals(expectedFont)) {
                    issues.add(new ValidationIssue(id(), Severity.ERROR, Location.para(pIndex),
                            "Неверный шрифт", expectedFont, String.valueOf(f), true));
                }
                if (s <= 0 || s != expectedSize) {
                    issues.add(new ValidationIssue(id(), Severity.ERROR, Location.para(pIndex),
                            "Неверный кегль", String.valueOf(expectedSize), String.valueOf(s), true));
                }
            }
        }
        return issues;
    }

    @Override
    public int applyFix(DocumentModel dm, RulesConfig cfg) {
        var doc = dm.raw();
        int fixes = 0;
        int expectedFirstIndent = (int)Math.round(cfg.firstLineIndentCm() * 567);

        for (XWPFParagraph p : doc.getParagraphs()) {
            if (p.getAlignment() != ParagraphAlignment.BOTH) { p.setAlignment(ParagraphAlignment.BOTH); fixes++; }
            if (Double.compare(p.getSpacingBetween(), cfg.lineSpacing()) != 0) { p.setSpacingBetween(cfg.lineSpacing()); fixes++; }
            if (p.getIndentationFirstLine() != expectedFirstIndent) { p.setIndentationFirstLine(expectedFirstIndent); fixes++; }

            for (XWPFRun run : p.getRuns()) {
                if (run.getFontFamily() == null || !run.getFontFamily().equals(cfg.fontFamily())) { run.setFontFamily(cfg.fontFamily()); fixes++; }
                if (run.getFontSize() <= 0 || run.getFontSize() != cfg.fontSizePt()) { run.setFontSize(cfg.fontSizePt()); fixes++; }
            }
        }
        return fixes;
    }
}
