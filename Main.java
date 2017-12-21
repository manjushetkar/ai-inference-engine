package com.ms;

import java.io.IOException;

public class Main {



    public static void main(String[] args) {
        Parser parser = new Parser();
        Agent agent = new Agent();
        InputReader inputReader = new InputReader();
        try {
            inputReader.readInput();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error reading input file");
        }

        for(String query: inputReader.getQueries()) {
            Clause clause = parser.parse(query, UniqueIDBroker.getID());
            agent.queriesList.add(clause);
            System.out.println(clause);
        }

        for (String clauseString: inputReader.getClauses()) {
            Clause clause =  parser.parse(clauseString, UniqueIDBroker.getID());
            agent.tell(clause);
            System.out.println(clause);
        }

        for (Clause clause: agent.queriesList) {
            Boolean answer = agent.ask(clause);
            if(answer) {
                System.out.println("TRUE");
            } else {
                System.out.println("FALSE");
            }
        }

    }

}
