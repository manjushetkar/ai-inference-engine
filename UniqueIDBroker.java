package com.ms;

public class UniqueIDBroker {

    static Integer counter = 0;

    public static Integer getID() {
        counter += 1;
        return counter;
    }

}
