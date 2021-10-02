import java.util.LinkedList;

/*
 * Copyright (c) 2021, Nicolas Pierre, Eva Epoy, Jules Nicolas-Thouvenin. All rights reserved.
 *
 */

/**
 * Set made of integer intervals
 */
public class IntervalSet implements Set {

    /**
     * Sorted list of integers, representing intervals
     */
    private LinkedList<Interval> intervals = new LinkedList<Interval>();

    /**
     * Constructor
     */
    public IntervalSet(Interval interval) {
        this.intervals.add(interval);
    }

    /**************
     * ELEMENTARY *
     **************/

    /**
     * Remove the interval at the given index from the list intervals
     */
    private void remove_interval(int index_interval) { // O(list.remove(i))
        intervals.remove(index_interval);
    }

    /**
     * Add the given interval at the specific index.
     * An index equal to size() + 1 will result in adding at the end of the list.
     * The interval must fit perfectly into the sorted list. Otherwise an exception is thrown.
     */
    private void add_interval(int index_interval, Interval interval) { // O(list.add(i,e))

        int nb_intervals = intervals.size();

        // assertions
        if (index_interval < 0 || index_interval > nb_intervals + 1) {
            throw new IndexOutOfBoundsException("\n$ Index out of bound.");
        } else if (index_interval == 0 && interval.top() >= intervals.get(0).bottom()) {
            throw new IllegalArgumentException("\n$ The new interval is covering an existing interval.");
        } else if (index_interval == nb_intervals+1 && interval.bottom() <= intervals.get(nb_intervals).top()) {
            throw new IllegalArgumentException("\n$ The new interval is covering an existing interval.");
        } else if (index_interval > 0 && index_interval <= nb_intervals) {
            if (interval.bottom() <= intervals.get(index_interval-1).top()) {
                throw new IllegalArgumentException("\n$ The new interval is covering an existing interval.");
            } else if (interval.top() >= intervals.get(index_interval).bottom()) {
                throw new IllegalArgumentException("\n$ The new interval is covering an existing interval.");
            }
        }
            
        // code
        if (index_interval >= 0 && index_interval <= nb_intervals) {
            intervals.add(index_interval,interval);
        } else {
            intervals.add(interval);
        }
    }

    /**
     * Merge the two intervals into one.
     * The interval on the left is expanded, the one on the right is deleted.
     * Return true if the intervals have been merged, false otherwise.
     */
    private boolean merge_intervals(int index_inter_left, int index_inter_right) { // O(list.remove(i))

        if (index_inter_left + 1 != index_inter_right) {
            throw new IllegalArgumentException("\n$ The two intervals have to be consecutive.");
        }

        Interval inter_left = intervals.get(index_inter_left);
        Interval inter_right = intervals.get(index_inter_right);

        if (inter_left.top() > inter_right.top() || inter_left.bottom() > inter_right.bottom()) {
            throw new IllegalArgumentException("\n$ The left interval should be on the left wrt the interval on the right.");
        } else if (inter_left.top() < inter_right.bottom()) { // the intervals don't have to be merged
            return false;
        } else { // the merge can be done
            inter_left.shift_top(inter_right.top()-inter_left.top()); // expand left interval
            remove_interval(index_inter_right);
        }
        return true;
    }

    /**
     * Reduce the given interval, from the bottom or the top (depending of the given boolean parameter)
     */
    private void reduce_interval(int index_interval, boolean at_bottom) { // O(list.remove(i))
        Interval interval = intervals.get(index_interval);

        if (interval.bottom() == interval.top()) { // the resulting interval is empty
            remove_interval(index_interval);
        } else if (at_bottom) { // we reduce from the left
            intervals.get(index_interval).shift_bottom(1);
        } else { // we reduce from the right
            intervals.get(index_interval).shift_top(-1);
        }
    }

    /**
     * Expand the given interval, from the bottom or the top (depending of the given boolean parameter)
     */
    private void expand_interval(int index_interval, boolean at_bottom) { // O(list.remove(i))
        Interval interval = intervals.get(index_interval);

        if (at_bottom && intervals.get(index_interval-1).top() + 1 == interval.bottom()) {
            merge_intervals(index_interval-1,index_interval);
        } else if (at_bottom) {
            intervals.get(index_interval).shift_bottom(-1);
        } else {
            intervals.get(index_interval).shift_top(1);
        }
    }

    /**
     * Split the given interval to remove the given value from the set
     */
    private void split_interval(int index_interval, int deleted_value) { // O(list.add(i,e))
        Interval interval = intervals.get(index_interval);
        
        if (deleted_value <= interval.bottom() || deleted_value >= interval.top()) {
            throw new IllegalArgumentException("\n$ The deleted value doesn't fit the given interval. The update can't be done.");
        } else {
            Interval right_inter = new Interval(deleted_value+1,interval.top()); // create the interval on the right
            interval.shift_top(deleted_value-1); // ajust the interval one the left
            intervals.add(index_interval+1,right_inter);
        }
    }

    /**
     * Update the list of intervals to remove a given value
     */
    private void update_interval(int index_interval, int deleted_value) { // max[ O(list.add(i,e)), O(list.remove(i)) ]
        Interval interval = intervals.get(index_interval);

        if (!value_is_in_interval(deleted_value, interval)) {
            throw new IllegalArgumentException("\n$ The deleted value doesn't fit the given interval. The update can't be done.");
        } else if (deleted_value == interval.bottom()) {
            reduce_interval(index_interval, true);
        } else if (deleted_value == interval.top()) {
            reduce_interval(index_interval, false);
        } else {
            split_interval(index_interval, deleted_value);
        }
    }

    /**
     * True if the given value isn't contained by neither of the given intervals and is between them,
     * false otherwise
     * 
     * (CURRENTLY NOT USED)
     */
    private boolean value_is_between_intervals(int value, Interval interval_left, Interval interval_right) { // O(1)
        if (value > interval_left.top() && value < interval_right.bottom()) { // O(1)
            return true;
        } else {
            return false;
        }
    }

    /**
     * True if the given value is contained by the given interval,
     * false otherwise
     */
    private boolean value_is_in_interval(int value, Interval interval){ // O(1)
        if (value >= interval.bottom() && value <= interval.top()){
            return true;
        } else {
            return false;
        }
    }

    /*********
     * ALGOS *
     *********/

    /**
     * Dichotomic search to find the index of the interval containing the given value
     * Return a tuple (a,b) :
     * if a == -2 and b == -1 then value is smaller than all the intervals in the set
     * else if a == nb_int + 1 and b == nb_intervals + 2 then value is greater than all the intervals in the set
     * else if a+1 == b then value is between intervals of index a and b, therefore not in the set
     * else (a == b) then value is in the interval of index a, therefore in the set
     */
    private Interval indicies_intervals_containing(int value){ // O(log(nb_intervals))

        int nb_intervals = intervals.size();

        // Check extreme cases
        if (value < intervals.get(0).bottom()) { // value is inferior to the smallest interval
            return new Interval(-2,-1);
        } else if (value > intervals.get(nb_intervals).top()) { // value is superior to the greatest interval
            return new Interval(nb_intervals+1,nb_intervals+2);
        // Dichotomic search
        } else {
            int start = 1;
            int end = nb_intervals;
            int middle;

            while (start <= end) {
                middle = (int) Math.floor( (double)(end+start) / 2 );
                if (value_is_in_interval(value, intervals.get(middle))) { // value is in the middle interval
                    return new Interval(middle,middle);
                } else if (middle > 1 && value <= intervals.get(middle-1).top()) { // value is maybe in an interval on the left
                    end = middle-1;
                } else if (middle < nb_intervals && value >= intervals.get(middle+1).bottom()) { // value is maybe in an interval on the right
                    start = middle+1;
                } else if (value < intervals.get(middle).bottom()) { // value is in no interval, but below middle.
                    return new Interval(middle-1,middle); // middle - 1 >= 0 because if not we would have stop in an earlier condition
                } else {
                    return new Interval(middle,middle+1); // same idea but for middle + 1
                }
            }
        }
        return new Interval(-2,-1);
    }

    /********************
     * PUBLIC FUNCTIONS *
     ********************/
    
    /**
     * Remove the given value from the set
     * Return true if the list of intervals has been modified, false otherwise
     */
    public boolean remove(int value){ // max[ O(log(n)), O(list.add(i,e)), O(list.remove(i)) ]
    	Interval indicies = indicies_intervals_containing(value); // O(log(nb_intervals))
        int nb_intervals = intervals.size();
        
        if (indicies.bottom() == -1 || (int) indicies.bottom() == nb_intervals+1 || indicies.bottom() != indicies.top()) { // value isn't contained by the set
            return false;
        } else { // value is in the set
            update_interval((int) indicies.bottom(), value); // O(1)
            return true;
        }
    }

    /**
     * Add the given value to the set
     * Return true if the list of intervals has been modified, false otherwise
     */
    public boolean add(int value){ // max[ O(log(n)), O(list.add(i,e)), O(list.remove(i)) ]
        Interval indicies = indicies_intervals_containing(value); // O(log(nb_intervals))
        int nb_intervals = intervals.size();
        
        if (indicies.bottom() == indicies.top()) { // value is already in an interval
            return false;
        } else if (indicies.bottom() == -2) { // value is smaller than all elements in the set
            if (value + 1 == intervals.get(0).bottom()) { // the first interval will be expanded from the bottom
                expand_interval(0, true);
            } else {
                Interval new_interval = new Interval(value,value); // we have to create a new interval
                add_interval(0, new_interval);
            }
        } else if (indicies.bottom() == nb_intervals + 1) { // value is greater than all elements in the set
            if (value - 1 == intervals.get(nb_intervals).top()) { // the last interval will be expanded from to top
                expand_interval(nb_intervals, false);
            } else {
                Interval new_interval = new Interval(value,value);
                add_interval(nb_intervals+1, new_interval);
            }
        } else { // value is between two intervals
            if (value - 1 == intervals.get(indicies.bottom()).top()) { // left interval will be expanded
                expand_interval(indicies.bottom(), false);
            } else if (value + 1 == intervals.get(indicies.top()).bottom()) { // right interval will be expanded
                expand_interval(indicies.top(), true);
            } else { // a new interval has to be created
                Interval new_interval = new Interval(value,value);
                add_interval(indicies.top(),new_interval);
            }
        }
        return true;
    }
    
    /**
     * @return True if the given integer is contained by the set,
     *         false otherwise
     */
    public boolean contains(int value) {
    	// TODO
    	return true;
    }
    
    /**
     * @return True if @this is the same as the given set,
     *         false otherwise
     */
    public boolean equals(Set set) {
    	// TODO
    	return true;
    }
    
    /**
     * @return True if @this has no elements,
     *         false otherwise
     */
    public boolean is_empty() {
    	// TODO
    	return true;
    }

    /**
     * Remove all elements in the set
     */
    public void clear() {
    	// TODO
    }

    /**
     * Remove from @this the elements not contained by the given set
     */
    public void inter(Set set) {
    	// TODO
    }

    /**
     * Add to @this the elements contained by the given set
     */
    public void union(Set set) {
    	// TODO
    }

    /**
     * Remove from @this the elements contained by the given set
     */
    public void deprived_of(Set set) {
    	// TODO
    }

    /**
     * @return Returns the list of intervals of the set (for intervalSets only)
     */
    public LinkedList<Interval> get_intervals() {
    	return intervals;
    }

    /**
     * @return Returns the list of values in the set
     */
    public LinkedList<Integer> get_values() {
    	// TODO
    	return new LinkedList<Integer>();
    }

}
