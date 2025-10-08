package com.dmitry.document.rules;


import com.dmitry.document.model.Location;

public class ValidationIssue {
    private final String ruleId;
    private final Severity severity;
    private final Location location;
    private final String message;
    private final String expected;
    private final String actual;
    private final boolean autofixable;

    public ValidationIssue(String ruleId, Severity severity, Location location, String message,
                           String expected, String actual, boolean autofixable) {
        this.ruleId = ruleId; this.severity = severity; this.location = location;
        this.message = message; this.expected = expected; this.actual = actual;
        this.autofixable = autofixable;
    }

    public String ruleId() { return ruleId; }
    public Severity severity() { return severity; }
    public Location location() { return location; }
    public String message() { return message; }
    public String expected() { return expected; }
    public String actual() { return actual; }
    public boolean autofixable() { return autofixable; }
}
