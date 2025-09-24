package com.edgn.prog.parser.units;

public enum Unit {
    PIXEL("px"),
    PERCENTAGE("%"),
    VIEWPORT_WIDTH("vw"),
    VIEWPORT_HEIGHT("vh");

    private final String symbol;
    Unit(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }
}
