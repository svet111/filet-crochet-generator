package com.example.crochet_generator.model;

import java.util.List;

public record PatternResponse(List<String> instructions, boolean[][] grid) {}