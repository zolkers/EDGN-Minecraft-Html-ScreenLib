package com.edgn.edml.dom.styling;

import java.util.Map;

public record EdssRule(String selector, Map<String, String> declarations) {}
