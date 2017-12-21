package com.ms;

import java.util.*;
import java.util.regex.Pattern;

public class Clause {

    private static Pattern constantSymbolPattern = Pattern.compile("[a-z]");
    private static Pattern variableSymbolPattern = Pattern.compile("[A-Z].*");


    private List<Literal> literals;

    public Clause(List<Literal> literals) {
        this.literals = literals;
    }

    public boolean isSingular() {
        return this.literals.size() == 1;
    }

    public static Clause negate(Clause clause) {
        if(clause.isSingular())
            return new Clause(Arrays.asList(Literal.negateLiteral(clause.literals.get(0))));
        throw new IllegalArgumentException("Expected unit literal clause");
    }

    @Override
    public String toString() {
        return "Clause{" +
                "literals=" + literals +
                '}';
    }

    public Boolean isContradiction() {
        return literals.isEmpty();
    }

    public Boolean isClauseResolutionCompatible(Clause clause) {
        for(int i = 0; i < this.literals.size(); i++ ) {
            for(int j = 0; j < clause.literals.size(); j++) {
                if(this.literals.get(i).isResolutionCompatible(clause.literals.get(j)))
                    return true;
            }
        }
        return false;
    }

    public Clause resolveClause1(Clause clause) {
        Clause resolvedClause = new Clause(new LinkedList<>());
        Clause matchingClause = new Clause(new LinkedList<>());
        boolean matchFound = false;
        for(int i = 0; i < this.literals.size(); i++ ) {
            for(int j = 0; j < clause.literals.size(); j++) {
                if(this.literals.get(i).isResolutionCompatible(clause.literals.get(j))) {
                    matchingClause = new Clause(new LinkedList<>());
                    matchingClause.literals.add(this.literals.get(i));
                    matchingClause.literals.add(clause.literals.get(j));
                    matchFound = true;
                    //break;
                }
            }
            //if(matchFound) break;
        }
        for(int i = 0; i < this.literals.size(); i++ ) {
            if(!matchingClause.literals.contains(this.literals.get(i))) {
                resolvedClause.literals.add(this.literals.get(i));
            }
        }

        for(int i = 0; i < clause.literals.size(); i++ ) {
            if(!matchingClause.literals.contains(clause.literals.get(i))) {
                resolvedClause.literals.add(clause.literals.get(i));
            }
        }
        return resolvedClause;
    }

    public Clause resolveClause(Clause clause) {
        Clause resolvedClause = new Clause(new LinkedList<>());
        Map<String, Symbol> hashTable = new Hashtable<>();
        boolean isMatch = false;
        for(int i = 0; i < this.literals.size(); i++ ) {
            for(int j = 0; j < clause.literals.size(); j++) {
                if(this.literals.get(i).isResolutionCompatible(clause.literals.get(j))) {
                    hashTable = unification(this.literals.get(i), clause.literals.get(j));
                    isMatch = true;
                    //break;
                }
            }
            //if(isMatch) break;
        }
        substitution(clause, hashTable);
        substitution(this, hashTable);

        return this.resolveClause1(clause);
    }

//    private void substitute(Clause clause, Map<String, String> hashTable) {
//        for (Literal literal:clause.literals) {
//            for (Symbol s:literal.parameters) {
//                if(hashTable.containsKey(s.faceValue)) {
//
//                    s.faceValue = hashTable.get(s.faceValue);
//                    s = new VariableSymbol(s.id, s.faceValue);
//                }
//            }
//        }
//    }

    private void substitution(Clause clause, Map<String, Symbol> hashTable) {
        for (Literal literal: clause.literals) {
            for(int i = 0; i < literal.parameters.size(); i++) {
                if(hashTable.containsKey(literal.parameters.get(i).faceValue)) {
                    String faceValue = literal.parameters.get(i).faceValue;
                    literal.parameters.remove(i);
                    literal.parameters.add(i, hashTable.get(faceValue));
                }
            }

        }
    }

//    private Map<String, Symbol> unify(Literal literal1, Literal literal2) {
//        Map<String, Symbol> res = new Hashtable<>();
//        for(int i = 0; i < literal1.parameters.size(); i++) {
//            if(literal1.parameters.get(i) instanceof ConstantSymbol && literal2.parameters.get(i) instanceof VariableSymbol) {
//                hashing(literal1, literal2, res, i);
//            }
//            if(literal1.parameters.get(i) instanceof VariableSymbol && literal2.parameters.get(i) instanceof ConstantSymbol) {
//                hashing(literal1, literal2, res, i);
//            }
//            if(literal1.parameters.get(i) instanceof VariableSymbol && literal2.parameters.get(i) instanceof VariableSymbol) {
//                hashing(literal1, literal2, res, i);
//            }
//            if(literal1.parameters.get(i) instanceof ConstantSymbol && literal2.parameters.get(i) instanceof ConstantSymbol) {
//                hashing(literal1, literal2, res, i);
//            }
//
//        }
//        return res;
//    }
//
//    private void hashing(Literal literal1, Literal literal2, Map<String, Symbol> res, int i) {
//        Symbol symbol = literal1.parameters.get(i);
//        Symbol symbol1 = null;
//        if (symbol instanceof ConstantSymbol) {
//            symbol1 = new ConstantSymbol(symbol.id, symbol.faceValue);
//        }
//        res.put(literal2.parameters.get(i).faceValue, symbol1);
//    }

    private Map<String, Symbol> unification(Literal literal1, Literal literal2) {
        Map<String, Symbol> res = new Hashtable<>();
        for(int i = 0; i < literal1.parameters.size(); i++) {
            if(literal1.parameters.get(i) instanceof ConstantSymbol && literal2.parameters.get(i) instanceof VariableSymbol) {
                res.put(literal2.parameters.get(i).faceValue, literal1.parameters.get(i));
            }
            if(literal1.parameters.get(i) instanceof VariableSymbol && literal2.parameters.get(i) instanceof ConstantSymbol) {
                res.put(literal1.parameters.get(i).faceValue, literal2.parameters.get(i));
            }
            if(literal1.parameters.get(i) instanceof VariableSymbol && literal2.parameters.get(i) instanceof VariableSymbol) {
                res.put(literal1.parameters.get(i).faceValue, literal2.parameters.get(i));
            }
            if(literal1.parameters.get(i) instanceof ConstantSymbol && literal2.parameters.get(i) instanceof ConstantSymbol) {
                res.put(literal1.parameters.get(i).faceValue, literal2.parameters.get(i));
            }

        }
        return res;
    }

//    public Clause clone1() {
//        List<Literal> literals = new LinkedList<>();
//        List<Symbol> params = new LinkedList<>();
//        for (Literal l: this.literals) {
//            params.clear();
//            for (Symbol s: l.parameters) {
//                Symbol s1 = s instanceof VariableSymbol ? new VariableSymbol(s.id, s.faceValue) :
//                        new ConstantSymbol(s.id, s.faceValue);
//                params.add(s1);
//            }
//            Literal a = new Literal(l.literalName, l.isNegated, params);
//            literals.add(a);
//        }
//        return new Clause(literals);
//    }

    public Clause clone() {
        List<Literal> literals = new LinkedList<>();
        for(int i = 0; i < this.literals.size(); i++) {
            Literal literal = this.literals.get(i);
            List<Symbol> params = new LinkedList<>();
            for(int j = 0; j < literal.parameters.size(); j++) {
                Symbol symbol = literal.parameters.get(j);
                Symbol tempSymbol = symbol instanceof VariableSymbol ? new VariableSymbol(symbol.id, symbol.faceValue) :
                        new ConstantSymbol(symbol.id, symbol.faceValue);
                params.add(tempSymbol);
            }
            Literal tempLiteral = new Literal(literal.literalName, literal.isNegated, params);
            literals.add(tempLiteral);
        }
        return new Clause(literals);
    }

//    @Override
//    public boolean equals(Object o) {
//        if(o == this) return true;
//        if(!(o instanceof Clause)) return false;
//        Clause other = (Clause) o;
//        boolean flag = true;
//        for(int i = 0; i < this.literals.size(); i++) {
//            if(!this.literals.get(i).equals(other.literals.get(i))) {
//                flag = false;
//                break;
//            }
//        }
//        return flag;
//    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Clause)) return false;

        Clause clause = (Clause) o;

        return literals != null ? literals.equals(clause.literals) : clause.literals == null;
    }

    @Override
    public int hashCode() {
        return literals != null ? literals.hashCode() : 0;
    }
}
