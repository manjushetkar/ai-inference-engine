package com.ms;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {

    String orRegexString;
    String negatedFlagString;
    String commaRegexString;

    Pattern literalPattern;
    Pattern constantSymbolPattern;
    Pattern variableSymbolPattern;

    public Parser() {
        orRegexString = "\\|";
        negatedFlagString = "~";
        commaRegexString = ",";

        literalPattern = Pattern.compile("(~?)([A-Z].*)\\((.*)\\)");
        variableSymbolPattern = Pattern.compile("[a-z]");
        constantSymbolPattern = Pattern.compile("[A-Z].*");
    }

    public Clause parse(String clausesString, int id) {
        // Split into literals
        String[] literalStrings = clausesString.split(orRegexString);
        List<Literal> literals = new ArrayList<>();
        for (String literalString : literalStrings) {
            // Parse each literal
            Matcher literalMatcher = literalPattern.matcher(literalString.trim());
            if (literalMatcher.matches()) {
                // Create literalString instance
                Boolean negatedFlag = literalMatcher.group(1).contains(negatedFlagString);
                String literalName = literalMatcher.group(2);
                String[] literalParameters = literalMatcher.group(3).split(commaRegexString);
                List<Symbol> parameters = new LinkedList<>();
                for (String parameterString : literalParameters) {
                    // Parse each parameter
                    Matcher variableSymbolMatcher = variableSymbolPattern.matcher(parameterString.trim());
                    Matcher constantSymbolMatcher = constantSymbolPattern.matcher(parameterString.trim());
                    if (variableSymbolMatcher.matches()) {
                        parameters.add(new VariableSymbol(id, parameterString + id));
                    } else if (constantSymbolMatcher.matches()) {
                        parameters.add(new ConstantSymbol(id, parameterString));
                    } else
                        throw new ParameterParseException("Unable to parse input string. Cannot generate parameter");
                }
                literals.add(new Literal(literalName, negatedFlag, parameters));
            } else
                throw new LiteralParseException("Unable to parse input string. Cannot generate literal");
        }
        return new Clause(literals);
    }
}

class ParseException extends RuntimeException {

    public ParseException(String reason) {
        super(reason);
    }

}

class LiteralParseException extends ParseException {

    public LiteralParseException(String reason) {
        super(reason);
    }

}

class ParameterParseException extends ParseException {

    public ParameterParseException(String reason) {
        super(reason);
    }

}
