/*
* Copyright (c) 2021, Nicolas Pierre, Eva Epoy, Jules Nicolas-Thouvenin. All rights reserved.
*
*/

public class Pair<T, U> {
    private T first;
    private U second;

    public Pair(T first_elem, U second_elem) {
        first = first_elem;
        second = second_elem;
    }

    public T first() {
        return this.first;
    }

    public U second() {
        return this.second;
    }
}
