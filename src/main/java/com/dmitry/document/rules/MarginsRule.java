package com.dmitry.document.rules;

import com.dmitry.document.engine.RulesConfig;
import com.dmitry.document.model.DocumentModel;
import com.dmitry.document.model.Location;
import com.dmitry.document.rules.ValidationIssue;

import com.dmitry.document.util.Units;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class MarginsRule implements Rule {
    @Override public String id() { return "margins"; }

    @Override
    public List<ValidationIssue> validate(DocumentModel dm, RulesConfig cfg) {
        var issues = new ArrayList<ValidationIssue>();
        XWPFDocument doc = dm.raw();
        CTSectPr sectPr = doc.getDocument().getBody().getSectPr();
        if (sectPr == null || !sectPr.isSetPgMar()) {
            issues.add(new ValidationIssue(id(), Severity.ERROR, Location.note("section"),
                    "Не заданы поля страницы", "L=30,R=15,T=20,B=20 (мм)", "—", true));
            return issues;
        }

        CTPageMar m = sectPr.getPgMar();

        // ожидаемые значения (twips)
        BigInteger expL = BigInteger.valueOf(Units.mmToTwips(cfg.marginLeftMm()));
        BigInteger expR = BigInteger.valueOf(Units.mmToTwips(cfg.marginRightMm()));
        BigInteger expT = BigInteger.valueOf(Units.mmToTwips(cfg.marginTopMm()));
        BigInteger expB = BigInteger.valueOf(Units.mmToTwips(cfg.marginBottomMm()));

        // фактические (могут быть null в некоторых документах)
        BigInteger left   = (BigInteger) m.getLeft();
        BigInteger right  = (BigInteger) m.getRight();
        BigInteger top    = (BigInteger) m.getTop();
        BigInteger bottom = (BigInteger) m.getBottom();

        if (left == null || left.compareTo(expL) != 0) {
            issues.add(new ValidationIssue(id(), Severity.ERROR, Location.note("left"),
                    "Левое поле 30 мм", expL.toString(), String.valueOf(left), true));
        }
        if (right == null || right.compareTo(expR) != 0) {
            issues.add(new ValidationIssue(id(), Severity.ERROR, Location.note("right"),
                    "Правое поле 15 мм", expR.toString(), String.valueOf(right), true));
        }
        if (top == null || top.compareTo(expT) != 0) {
            issues.add(new ValidationIssue(id(), Severity.ERROR, Location.note("top"),
                    "Верхнее поле 20 мм", expT.toString(), String.valueOf(top), true));
        }
        if (bottom == null || bottom.compareTo(expB) != 0) {
            issues.add(new ValidationIssue(id(), Severity.ERROR, Location.note("bottom"),
                    "Нижнее поле 20 мм", expB.toString(), String.valueOf(bottom), true));
        }

        return issues;
    }

    @Override
    public int applyFix(DocumentModel dm, RulesConfig cfg) {
        XWPFDocument doc = dm.raw();
        CTSectPr sectPr = doc.getDocument().getBody().isSetSectPr()
                ? doc.getDocument().getBody().getSectPr()
                : doc.getDocument().getBody().addNewSectPr();

        CTPageMar m = sectPr.isSetPgMar() ? sectPr.getPgMar() : sectPr.addNewPgMar();

        m.setLeft  (BigInteger.valueOf(Units.mmToTwips(cfg.marginLeftMm())));
        m.setRight (BigInteger.valueOf(Units.mmToTwips(cfg.marginRightMm())));
        m.setTop   (BigInteger.valueOf(Units.mmToTwips(cfg.marginTopMm())));
        m.setBottom(BigInteger.valueOf(Units.mmToTwips(cfg.marginBottomMm())));

        return 4;
    }
}
