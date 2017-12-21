package com.ms;

public abstract class Symbol {

    protected int id;
    protected String faceValue;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Symbol)) return false;

        Symbol symbol = (Symbol) o;

        if (id != symbol.id) return false;
        return faceValue.equals(symbol.faceValue);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + faceValue.hashCode();
        return result;
    }
}
