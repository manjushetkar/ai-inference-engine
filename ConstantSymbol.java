package com.ms;

public class ConstantSymbol extends Symbol {

    public ConstantSymbol(int id, String faceValue) {
        this.id = id;
        this.faceValue = faceValue;
    }

    @Override
    public String toString() {
        return "ConstantSymbol{" +
                "id=" + id +
                ", faceValue='" + faceValue + '\'' +
                '}';
    }



}
