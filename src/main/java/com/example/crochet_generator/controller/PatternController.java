package com.example.crochet_generator.controller;

import com.example.crochet_generator.model.PatternResponse;
import com.example.crochet_generator.model.ScaleMode;
import com.example.crochet_generator.service.CrochetPatternService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/patterns")
public class PatternController {

    private final CrochetPatternService patternService;

    public PatternController(CrochetPatternService patternService) {
        this.patternService = patternService;
    }

    @PostMapping("/generate")
    public ResponseEntity<?> generatePattern(
            @RequestParam("file") MultipartFile file,
            @RequestParam(defaultValue = "40") int width,
            @RequestParam(defaultValue = "SMOOTH") ScaleMode mode,
            @RequestParam(defaultValue = "128") int threshold) {
        try {
            PatternResponse response = patternService.generateFromImage(file.getInputStream(), width, mode, threshold);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Failed to process image.");
        }
    }
}
