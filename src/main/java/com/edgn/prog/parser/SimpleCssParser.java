package com.edgn.prog.parser;

import com.edgn.prog.component.css.CssRule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class SimpleCssParser implements CssParser {

    @Override
    public List<CssRule> parse(String cssContent) {
        if (cssContent == null || cssContent.trim().isEmpty()) {
            return List.of();
        }

        List<CssRule> rules = new ArrayList<>();
        String[] blocks = cssContent.split("\\}");

        for (String block : blocks) {
            block = block.trim();
            if (block.isEmpty()) continue;

            int braceIndex = block.indexOf('{');
            if (braceIndex == -1) continue;

            String selector = block.substring(0, braceIndex).trim();
            String declarations = block.substring(braceIndex + 1).trim();

            Map<String, String> declarationMap = parseDeclarations(declarations);
            if (!declarationMap.isEmpty()) {
                rules.add(new CssRule(selector, declarationMap));
            }
        }

        return rules;
    }

    private Map<String, String> parseDeclarations(String declarations) {
        Map<String, String> map = new HashMap<>();
        String[] decls = declarations.split(";");

        for (String decl : decls) {
            decl = decl.trim();
            if (decl.isEmpty()) continue;

            int colonIndex = decl.indexOf(':');
            if (colonIndex == -1) continue;

            String property = decl.substring(0, colonIndex).trim();
            String value = decl.substring(colonIndex + 1).trim();

            if (!property.isEmpty() && !value.isEmpty()) {
                map.put(property, value);
            }
        }

        return map;
    }
}