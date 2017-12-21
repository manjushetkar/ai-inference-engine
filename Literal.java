package com.ms;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class Literal {

    String literalName;
    Boolean isNegated;
    List<Symbol> parameters;

    public Literal(String literalName, Boolean isNegated, List<Symbol> parameters) {
        this.literalName = literalName;
        this.isNegated = isNegated;
        this.parameters = parameters;
    }

    public static Literal negateLiteral(Literal literal) {
        return new Literal(literal.literalName, !literal.isNegated, literal.parameters);
    }

    @Override
    public String toString() {
        return "Literal{" +
                "literalName='" + literalName + '\'' +
                ", isNegated=" + isNegated +
                ", parameters=" + parameters +
                '}';
    }

    public Boolean isResolutionCompatible(Literal l) {
        return this.literalName.equals(l.literalName) && this.isNegated == !l.isNegated && this.isParameterCompatible(l);
    }

    private Boolean isParameterCompatible(Literal literal) {
        for(int i = 0; i < this.parameters.size(); i++) {
            Symbol s1 = this.parameters.get(i);
            Symbol s2 = literal.parameters.get(i);
            if(s1 instanceof  ConstantSymbol && s2 instanceof ConstantSymbol && !s1.faceValue.equals(s2.faceValue))
                return false;
        }

//        HashSet<String> literalParamSet = new HashSet<>();
//        HashSet<String> thisParamSet = new HashSet<>();
//        Map<String, String> subsMap = new Hashtable<>();
//        for (int i = 0; i < literal.parameters.size(); i++) {
//            String s1 = literal.parameters.get(i).faceValue;
//            String s2 = this.parameters.get(i).faceValue;
//            literalParamSet.add(s1);
//            thisParamSet.add(s2);
//            if(subsMap.containsKey(s1) && !subsMap.get(s1).equals(s2)) return false;
//            subsMap.put(s1, s2);
//        }
//
//        if(thisParamSet.size() != literalParamSet.size()) return false;

        Map<String, String> res = new Hashtable<>();
        Map<String, String> res2 = new Hashtable<>();

         for(int i = 0; i < this.parameters.size(); i++) {
            String s1 = literal.parameters.get(i).faceValue;
            String s2 = this.parameters.get(i).faceValue;
            if(this.parameters.get(i) instanceof ConstantSymbol && this.parameters.get(i) instanceof VariableSymbol) {
                if(res.containsKey(s1) && !res.get(s1).equals(s2)) return false;
                res.put(s1, s2);
            }
            if(this.parameters.get(i) instanceof VariableSymbol && literal.parameters.get(i) instanceof ConstantSymbol) {
                if(res.containsKey(s2) && !res.get(s2).equals(s1)) return false;
                res.put(s2, s1);
            }
            if(this.parameters.get(i) instanceof VariableSymbol && literal.parameters.get(i) instanceof VariableSymbol) {
                if(res.containsKey(s2) && !res.get(s2).equals(s1)) return false;
                res.put(s2, s1);
                if(res2.containsKey(s1) && !res2.get(s1).equals(s2)) return false;
                res2.put(s1, s2);
            }
            if(this.parameters.get(i) instanceof ConstantSymbol && literal.parameters.get(i) instanceof ConstantSymbol) {
                if(res.containsKey(s2) && !res.get(s2).equals(s1)) return false;
                res.put(s2, s1);
            }

        }
        return true;
    }

//    @Override
//    public boolean equals(Object o) {
//        if(this == o) return true;
//        if(!(o instanceof Literal)) return false;
//        Literal other = (Literal) o;
//        boolean flag = true;
//        if(this.parameters.size() != other.parameters.size()) return false;
//        for (int i = 0; i < this.parameters.size(); i++) {
//            if(!this.parameters.get(i).equals(other.parameters.get(i))) {
//                flag = false;
//                break;
//            }
//
//        }
//        return  flag && this.isNegated == this.isNegated && this.literalName.equals(other.literalName);
//    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Literal)) return false;

        Literal literal = (Literal) o;

        if (!literalName.equals(literal.literalName)) return false;
        if (!isNegated.equals(literal.isNegated)) return false;
        return parameters.equals(literal.parameters);
    }

    @Override
    public int hashCode() {
        int result = literalName.hashCode();
        result = 31 * result + isNegated.hashCode();
        result = 31 * result + parameters.hashCode();
        return result;
    }
}
