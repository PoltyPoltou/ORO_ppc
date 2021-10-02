/*
 * Copyright (c) 2021, Nicolas Pierre, Eva Epoy, Jules Nicolas-Thouvenin. All rights reserved.
 *
 */

/**
 * Interval on integers
 */
public class Interval {

    /**
     * Lower and upper bounds of the interval
     */
    private int bottom;
    private int top;

    /**
     * Constructor
     */
    public Interval(int bottom, int top) {
        if (bottom > top) {
            throw IllegalArgumentException("\n$ bottom can't be strictly greater than top.");
        }
        this.bottom = bottom;
        this.top = top;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
 
        if (!(o instanceof Interval)) {
            return false;
        }
         
        Interval interval = (Interval) o;
         
        return (bottom == interval.bottom() && top == interval.top());
    }

    /**
     * Shift the bottom frontier of the interval from delta
     */
    public void shift_bottom(int delta) {
        if (bottom + delta > top) {
            throw new IllegalArgumentException("\n$ delta = " + delta
					+ ". Parameter 'delta' is too big, the bottom cannot be greater than the top of the interval.");
        } else {
            bottom += delta;
        }
    }

    /**
     * Shift the top frontier of the interval from delta
     */
    public void shift_top(int delta) {
        if (bottom > top + delta) {
            throw new IllegalArgumentException("\n$ delta = " + delta
					+ ". Parameter 'delta' is too big, the bottom cannot be greater than the top of the interval.");
        } else {
            top += delta;
        }
    }

    public int bottom() {
        return bottom;
    }

    public int top() {
        return top;
    }

}
