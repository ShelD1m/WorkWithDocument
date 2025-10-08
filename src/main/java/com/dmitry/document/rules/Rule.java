package com.dmitry.document.rules;

import com.dmitry.document.engine.RulesConfig;
import com.dmitry.document.model.DocumentModel;
import com.dmitry.document.rules.ValidationIssue;

import java.util.List;

public interface Rule {
    String id();
    List<ValidationIssue> validate(DocumentModel doc, RulesConfig cfg);
    /** Возвращает число применённых исправлений. */
    default int applyFix(DocumentModel doc, RulesConfig cfg) { return 0; }
}
