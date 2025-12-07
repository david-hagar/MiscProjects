package com.david_hagar.bui;

import java.util.Objects;

class Symbol {
    public final String value;
    public int count;

    public Symbol(String value, int count) {
        this.value = value;
        this.count = count;
    }

    @Override
    public String toString() {
        return "Symbol{" +
                "value='" + value + '\'' +
                ", count=" + count +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Symbol)) return false;
        Symbol symbol = (Symbol) o;
        return count == symbol.count && value.equals(symbol.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, count);
    }
}
