package com.dmitry.document.api;


import com.dmitry.document.engine.RuleEngine;
import com.dmitry.document.service.ValidationService;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class ValidationController {
    private final ValidationService service;
    public ValidationController(ValidationService service) { this.service = service; }

    @PostMapping(value = "/validate", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> validate(@RequestPart("file") MultipartFile file,
                                      @RequestParam(defaultValue = "false") boolean fix,
                                      @RequestParam(required = false, defaultValue = "json") String format) throws Exception {
        var name = file.getOriginalFilename() != null ? file.getOriginalFilename().toLowerCase() : "";
        if (!name.endsWith(".docx")) {
            return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body("Требуется файл .docx");
        }

        if (fix && "docx".equalsIgnoreCase(format)) {
            var baos = new java.io.ByteArrayOutputStream();
            RuleEngine.Report rep = service.validate(file.getBytes(), true, baos);
            var bytes = baos.toByteArray();

            var headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.wordprocessingml.document"));
            headers.setContentDisposition(ContentDisposition.attachment().filename("fixed.docx").build());
            headers.add("X-Report-Fixes", String.valueOf(rep.fixesApplied()));

            return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
        } else {
            RuleEngine.Report rep = service.validate(file.getBytes(), fix, null);
            return ResponseEntity.ok(rep);
        }
    }
}
