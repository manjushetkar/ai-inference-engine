package com.ms;

public class VariableSymbol extends Symbol {

    public VariableSymbol(int id, String faceValue) {
        this.id = id;
        this.faceValue = faceValue;
    }

    @Override
    public String toString() {
        return "VariableSymbol{" +
                "id=" + id +
                ", faceValue='" + faceValue + '\'' +
                '}';
    }

}
