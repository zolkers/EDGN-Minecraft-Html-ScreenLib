package com.edgn.edml.component.edss;

import java.util.Map;

public record EdssRule(String selector, Map<String, String> declarations) {}
