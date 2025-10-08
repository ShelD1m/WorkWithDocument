package com.dmitry.document.engine;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.*;
import java.nio.file.*;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RulesConfig {

    public record Typography(String font_family, int font_size_pt, double line_spacing, double first_line_indent_cm) {}
    public record Margins(double left, double right, double top, double bottom) {}

    public record Root(Meta meta, Document document) {}
    public record Meta(String standard, String locale) {}
    public record Document(Typography typography, Margins margins_cm) {}

    private final Root root;
    private RulesConfig(Root root) { this.root = root; }

    public static RulesConfig fromYaml(Path path) throws IOException {
        try (Reader reader = Files.newBufferedReader(path)) {
            return fromYaml(reader);
        }
    }

    public static RulesConfig fromYaml(InputStream in) throws IOException {
        return fromYaml(new InputStreamReader(in));
    }

    public static RulesConfig fromYaml(Reader reader) throws IOException {
        var mapper = new ObjectMapper(new YAMLFactory());
        Root r = mapper.readValue(reader, Root.class);
        return new RulesConfig(r);
    }

    public String fontFamily() { return root.document.typography.font_family(); }
    public int fontSizePt() { return root.document.typography.font_size_pt(); }
    public double lineSpacing() { return root.document.typography.line_spacing(); }
    public double firstLineIndentCm() { return root.document.typography.first_line_indent_cm(); }
    public double marginLeftMm() { return root.document.margins_cm.left(); }
    public double marginRightMm() { return root.document.margins_cm.right(); }
    public double marginTopMm() { return root.document.margins_cm.top(); }
    public double marginBottomMm() { return root.document.margins_cm.bottom(); }
}
