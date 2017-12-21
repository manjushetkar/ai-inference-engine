package com.ms;

import java.util.*;

public class Agent {

    List<Clause> clauseList;
    List<Clause> queriesList;

    public Agent() {
        this.clauseList = new LinkedList<>();
        this.queriesList = new ArrayList<>();
    }

    public void tell(Clause clause) {
        this.clauseList.add(clause);
    }

    public Boolean ask(Clause clause) {
        Clause negated = Clause.negate(clause);
        this.tell(negated);
        Boolean result = this.DFS(negated);
        this.remove(negated);
        return result;
    }

    private Boolean DFS(Clause start) {
        Stack<Clause> stack = new Stack<>();
        stack.push(start);
        List<Clause> visitedClauseList = new LinkedList<>();
        while (!stack.empty()) {
            if(stack.size() > 1000) return false;
            Clause current = stack.pop();
            if(!visitedClauseList.contains(current)) {
                visitedClauseList.add(current);
                if (current.isContradiction())
                    return true;
                List<Clause> tempClauseList = createTempClauseList(clauseList);
                for (Clause clause : tempClauseList) {
                    Clause clause1 = current.clone();
                    Clause clause2 = clause.clone();
                    List<Clause> results = unifyAndResolve(clause1, clause2);
                    if (results != null) {
                        for (Clause child : results)
                            stack.push(child);
                    }
                }
            }
        }
        return false;
    }

    private List<Clause> createTempClauseList(List<Clause> clauseList) {
        List<Clause> tempList = new LinkedList<>();
        for (Clause clause: clauseList) {
            tempList.add(clause.clone());
        }
        return tempList;
    }

    public void remove(Clause clause) {
        this.clauseList.remove(clause);
    }

    private List<Clause> unifyAndResolve(Clause clause1, Clause clause2) {
        List<Clause> results = null;
        if(clause1.isClauseResolutionCompatible(clause2)) {
            results = new ArrayList<>();
            results.add(clause1.resolveClause(clause2));
        }
        return results;
    }

}
