package com.ms;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class InputReader {
    static final String INPUT_FILENAME = "input19.txt";
    static String inputBytes;
    static int NumberOfQueries; // number of queries
    static int NumberOfClauses; // number of clauses
    private String[] queries;
    private String[] clauses;

    public String[] getQueries() {
        return queries;
    }

    public String[] getClauses() {
        return clauses;
    }

    public void readInput() throws IOException {
        inputBytes =  new String(Files.readAllBytes(Paths.get(INPUT_FILENAME)));
        String[] inputArray = inputBytes.split("\n");
        NumberOfQueries = Integer.parseInt(inputArray[0].trim());

        queries = new String[NumberOfQueries];
        for(int i=1, j=0; i<=NumberOfQueries; i++, j++) {
            queries[j] = inputArray[i];
        }

        NumberOfClauses = Integer.parseInt(inputArray[NumberOfQueries+1].trim());
        clauses = new String[NumberOfClauses];
        for(int i=NumberOfQueries+2, j=0; j<NumberOfClauses; i++, j++) {
            clauses[j] = inputArray[i];
        }
    }
}
