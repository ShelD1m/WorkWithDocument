package com.dmitry.document.model;

public record Location(Integer page, Integer paragraph, String note) {
    public static Location para(Integer p) { return new Location(null, p, null); }
    public static Location note(String n) { return new Location(null, null, n); }
}
