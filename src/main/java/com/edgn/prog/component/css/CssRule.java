package com.edgn.prog.component.css;

import java.util.Map;

public record CssRule(String selector, Map<String, String> declarations) {}
