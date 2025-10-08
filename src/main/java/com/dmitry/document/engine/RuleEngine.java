package com.dmitry.document.engine;


import com.dmitry.document.model.DocumentModel;
import com.dmitry.document.rules.MarginsRule;
import com.dmitry.document.rules.Rule;
import com.dmitry.document.rules.TypographyRule;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

public class RuleEngine {
    private final List<Rule> rules = List.of(
            new TypographyRule(),
            new MarginsRule()
    );
    private final RulesConfig cfg;

    public RuleEngine(RulesConfig cfg) { this.cfg = cfg; }

    public record ReportItem(String ruleId, String severity, String location, String message,
                             String expected, String actual, boolean autofixable) {}
    public record Report(List<ReportItem> items, int fixesApplied) {}

    public Report run(DocumentModel doc, boolean fix) {
        var items = new ArrayList<ReportItem>();
        int totalFixes = 0;

        for (Rule r : rules) {
            var issues = r.validate(doc, cfg);
            for (var is : issues) {
                items.add(new ReportItem(
                        is.ruleId(), is.severity().name(),
                        is.location()==null ? "" : String.valueOf(is.location()),
                        is.message(), is.expected(), is.actual(), is.autofixable()
                ));
            }
            if (fix) totalFixes += r.applyFix(doc, cfg);
        }
        return new Report(items, totalFixes);
    }

    public static void writeJson(Report rep, Path out) throws IOException {
        var mapper = new ObjectMapper().writerWithDefaultPrettyPrinter();
        if (out.getParent() != null) Files.createDirectories(out.getParent());
        mapper.writeValue(out.toFile(), rep);
    }
}
