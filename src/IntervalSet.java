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
     * True if the functions print messages in the shell for debugging, false otherwise.
     */
    private boolean verbose = false;

    /**
     * Constructor
     */
    public IntervalSet(Interval interval) {
        this.intervals.add(interval);
    }
    
    public IntervalSet(LinkedList<Interval> intervals) {
        this.intervals = intervals;
    }
    
    public void enableVerbose() {
    	verbose = true;
    }
    
    public void disableVerbose() {
    	verbose = false;
    }
    
    public void printIntervals() {
    	if (!this.is_empty()) {
    		System.out.print("(");
        	for(int i = first(); i < last(); i++) {
        		System.out.print(intervals.get(i).toString()+" U ");
        	}
        	System.out.println(intervals.get(last()).toString()+")");
    	} else {
    		System.out.println("()");
    	}
    }

    /**************
     * ELEMENTARY *
     **************/
    
    /**
     * Return first index of the list of intervals
     */
    private int first() {
    	return 0;
    }
    
    /**
     * Return last index of the list of intervals
     */
    private int last() {
    	return intervals.size()-1;
    }
    
    /**
     * Value symbolizing inifinity. In short, this is a value that can't be inside a IntervalSet.
     */
    private int inf() {
    	return Integer.MAX_VALUE;
    }

    /**
     * Remove the interval at the given index from the list intervals
     */
    private void remove_interval(int index_interval) { // O(list.remove(i))
    	
    	if (verbose) {
    		System.out.println("remove_interval("+index_interval+")");
    	}
    	
        intervals.remove(index_interval);
    }

    /**
     * Add the given interval at the specific index.
     * An index equal to size() will result in adding at the end of the list.
     * The interval must fit perfectly into the sorted list. Otherwise an exception is thrown.
     */
    private void add_interval(int index_insertion, Interval interval) { // O(list.add(i,e))
    	
    	if (verbose) {
        	System.out.println("add_interval("+index_insertion+","+interval.toString()+")");
    	}

        // assertions
        if (index_insertion < first() || index_insertion > last()+1) {
            throw new IndexOutOfBoundsException("\n$ Index out of bound.");
        } else if (index_insertion == first() && interval.top() >= intervals.get(first()).bottom()) {
            throw new IllegalArgumentException("\n$ The new interval is covering an existing interval.");
        } else if (index_insertion == last()+1 && interval.bottom() <= intervals.get(last()).top()) {
            throw new IllegalArgumentException("\n$ The new interval is covering an existing interval.");
        } else if (index_insertion > first() && index_insertion <= last()) {
            if (interval.bottom() <= intervals.get(index_insertion-1).top()) {
                throw new IllegalArgumentException("\n$ The new interval is covering an existing interval.");
            } else if (interval.top() >= intervals.get(index_insertion).bottom()) {
                throw new IllegalArgumentException("\n$ The new interval is covering an existing interval.");
            }
        }
            
        // code
        if (index_insertion >= first() && index_insertion <= last()) {
            intervals.add(index_insertion,interval);
        } else {
            intervals.add(interval);
        }
    }

    /**
     * Merge the two intervals into one.
     * The interval on the left is expanded, the one on the right is deleted.
     * Return true if the intervals have been merged, false otherwise.
     */
    private void merge_intervals(int index_inter_left, int index_inter_right) { // O(list.remove(i))
    	
    	if (verbose) {
    		System.out.println("merge_intervals("+index_inter_left+","+index_inter_right+")");
    	}
    	
        if (index_inter_left + 1 != index_inter_right) {
            throw new IllegalArgumentException("\n$ The two intervals have to be consecutive.");
        } else if (index_inter_left < first() || index_inter_right > last()) {
        	throw new IndexOutOfBoundsException("\n$ Indicies out of bound.");
        }

        Interval inter_left = intervals.get(index_inter_left);
        Interval inter_right = intervals.get(index_inter_right);

        if (inter_left.top() > inter_right.top() || inter_left.bottom() > inter_right.bottom()) { // this condition can be removed if we are sure that the list of intervals is sorted
            throw new IllegalArgumentException("\n$ The left interval should be on the left wrt the interval on the right.");
        } else { // the merge can be done
            inter_left.shift_top(inter_right.top()-inter_left.top()); // expand left interval
            remove_interval(index_inter_right);
        }
    }

    /**
     * Reduce the given interval, from the bottom or the top (depending of the given boolean parameter)
     */
    private void reduce_interval(int index_interval, boolean at_bottom) { // O(list.remove(i))
    	
    	if (verbose) {
    		System.out.println("reduce_interval("+index_interval+","+at_bottom+")");
    	}
    	
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
    	
    	if (verbose) {
    		System.out.println("expand_interval("+index_interval+","+at_bottom+")");
    	}
    		
        Interval interval = intervals.get(index_interval);

        if (at_bottom && index_interval > first() && intervals.get(index_interval-1).top() + 1 == interval.bottom()) {
            merge_intervals(index_interval-1,index_interval);
        } else if (!at_bottom && index_interval < last() && intervals.get(index_interval+1).bottom() - 1 == interval.top()) {
        	merge_intervals(index_interval,index_interval+1);
        } else if (at_bottom) {
            intervals.get(index_interval).shift_bottom(-1);
        } else {
            intervals.get(index_interval).shift_top(1);
        }
    }

    /**
     * Split the given interval to remove the given value from the set
     * The split can only be done if the value is stricly inside the interval
     */
    private void split_interval(int index_interval, int deleted_value) { // O(list.add(i,e))
    	
    	if (verbose) {
    		System.out.println("split_interval("+index_interval+","+deleted_value+")");
    	}
    	
        Interval interval = intervals.get(index_interval);
        
        if (deleted_value <= interval.bottom() || deleted_value >= interval.top()) {
            throw new IllegalArgumentException("\n$ The deleted value doesn't fit the given interval. The update can't be done.");
        } else {
            Interval right_inter = new Interval(deleted_value+1,interval.top()); // create the interval on the right
            interval.shift_top(deleted_value - interval.top() - 1); // ajust the interval one the left
            add_interval(index_interval+1,right_inter);
        }
    }

    /**
     * Update the list of intervals to remove a given value
     */
    private void update_interval(int index_interval, int deleted_value) { // max[ O(list.add(i,e)), O(list.remove(i)) ]
    	
    	if (verbose) {
    		System.out.println("update_interval("+index_interval+","+deleted_value+")");
    	}
    	
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
     * True if the given value is contained by the given interval,
     * false otherwise
     */
    private boolean value_is_in_interval(int value, Interval interval){ // O(1)
    	
    	if (verbose) {
    		System.out.println("value_is_in_interval("+value+","+interval.toString()+")");
    	}
    	
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
     * if a == first()-2 and b == first() -1 then value is smaller than all the intervals in the set
     * else if a == first()-4 and b == first()-3 then value is greater than all the intervals in the set
     * else if a+1 == b then value is between intervals of index a and b, therefore not in the set
     * else (a == b) then value is in the interval of index a, therefore in the set
     * 
     * WARNING : the result of this function shouldn't be used as it is.
     * The functions below parses the result of the dichotomy and extract precise information.
     * > value_below_intervals (indicator)
     * > value_above_intervals (indicator)
     * > value_between_intervals (indicator)
     * > value_inside_interval (indicator)
     * > index_interval_containing_value (indicator)
     * > indicies_intervals_around_value (indicator)
     */
    private Interval indicies_intervals_containing(int value){ // O(log(nb_intervals))
    	
    	if (verbose) {
    		System.out.println("indicies_intervals_containing("+value+")");
    	}
    		
        // Check extreme cases
        if (value < intervals.get(first()).bottom()) { // value is inferior to the smallest interval
        	return new Interval(first()-2,first()-1);
        } else if (value > intervals.get(last()).top()) { // value is superior to the greatest interval
        	return new Interval(first()-4,first()-3);
        // Dichotomic search
        } else {
            int start = first();
            int end = last();
            int middle;

            while (start <= end) {
                middle = (int) Math.floor( (double)(end+start) / 2 );
                if (value_is_in_interval(value, intervals.get(middle))) { // value is in the middle interval
                    return new Interval(middle,middle);
                } else if (middle > first() && value <= intervals.get(middle-1).top()) { // value is maybe in an interval on the left
                    end = middle-1;
                } else if (middle < last() && value >= intervals.get(middle+1).bottom()) { // value is maybe in an interval on the right
                    start = middle+1;
                } else if (value < intervals.get(middle).bottom()) { // value is in no interval, but below middle.
                    return new Interval(middle-1,middle); // middle - 1 >= 0 because if not we would have stop in an earlier condition
                } else {
                    return new Interval(middle,middle+1); // same idea but for middle + 1
                }
            }
            throw new RuntimeException("\n$ The dichotomy method is broken.");
        }
    }
    
    /*****************************************************************
     * INTERFACE FUNCTIONS FOR EXTRACTING INFO FROM DICHOTOMY RESULT *
     *****************************************************************/
    
    /**
     * Return true is value is smaller than all values in the set
     */
    private boolean value_below_intervals(Interval indicator) {
    	return indicator.bottom() == first() - 2;
    }
    
    /**
     * Return true is value is greater than all values in the set
     */
    private boolean value_above_intervals(Interval indicator) {
    	return indicator.bottom() == first() - 4;
    }
    
    /**
     * Return true is value is between two intervals of the set
     */
    private boolean value_between_intervals(Interval indicator) {
    	return indicator.bottom() + 1 == indicator.top();
    }
    
    /**
     * Return true is value is contained by an interval in the set
     */
    private boolean value_inside_interval(Interval indicator) {
    	return indicator.bottom() == indicator.top();
    }
    
    /**
     * Return the index of the interval containing value
     */
    private int index_interval_containing_value(Interval indicator) {
    	if (!value_inside_interval(indicator)) {
    		throw new IllegalArgumentException("\n$ The value isn't contained by an interval.");
    	} else {
    		return indicator.bottom();
    	}
    }
    
    /**
     * Return true is value is smaller than all values in the set
     */
    private Interval indicies_intervals_around_value(Interval indicator) {
    	if (!value_between_intervals(indicator)) {
    		throw new IllegalArgumentException("\n$ The value isn't between two intervals.");
    	} else {
    		return new Interval(indicator.bottom(),indicator.top());
    	}
    }
    
    /**************************************
     * PRIVATE FUNCTIONS FOR INTERSECTION *
     **************************************/
    
    /**
     * Remove the interval interval_to_remove from the interval of index intex_interval in the set
     * Return true if the list of intervals has been modified
     */
    private boolean remove_interval_from(Interval interval_to_remove, int index_interval) {
    	
    	if (verbose) {
    		System.out.println("remove_interval_from("+interval_to_remove.toString()+", "+index_interval+") from "+intervals.get(index_interval).toString());
    	}
    	
    	if (index_interval < first() || index_interval > last()) {
    		throw new IndexOutOfBoundsException("\n$ Index out of bound. index = "+index_interval+", first() = "+first()+", last() = "+last());
    	}
    	
    	Interval interval = intervals.get(index_interval);
    	
    	if (interval.top() < interval_to_remove.bottom() || interval.bottom() > interval_to_remove.top()) { // no removal to do
    		return false;
    	} else if (interval.bottom() >= interval_to_remove.bottom() && interval.top() <= interval_to_remove.top()) { // interval is inside the interval to remove
    		remove_interval(index_interval);
    	} else if (interval.bottom() < interval_to_remove.bottom() && interval.top() > interval_to_remove.top()) { // interval to remove is stricly inside the interval
    		Interval right_interval = new Interval(interval_to_remove.top()+1,interval.top());
    		interval.shift_top(interval_to_remove.bottom() - interval.top() - 1);
    		add_interval(index_interval+1,right_interval);
    	} else if (interval.bottom() < interval_to_remove.bottom()) { // the top of the interval is over the interval to remove
    		interval.shift_top(interval_to_remove.bottom() - interval.top() - 1);
    	} else if (interval.top() > interval_to_remove.top()) { // the bottom of the interval is over the interval to remove
    		interval.shift_bottom(interval_to_remove.top() - interval.bottom() + 1);
    	}
    	
    	return true;
    }

    /********************
     * PUBLIC FUNCTIONS *
     ********************/
    
    /**
     * Remove the given value from the set
     * Return true if the list of intervals has been modified, false otherwise
     */
    public boolean remove(int value){ // max[ O(log(n)), O(list.add(i,e)), O(list.remove(i)) ]
    	
    	if (verbose) {
    		System.out.println("remove("+value+")");
    	}
    		
    	Interval indicator = indicies_intervals_containing(value); // O(log(nb_intervals))
        
        if (!value_inside_interval(indicator)) { // value isn't contained by the set
            return false;
        } else { // value is in the set
            update_interval(index_interval_containing_value(indicator), value); // O(1)
            return true;
        }
    }

    /**
     * Add the given value to the set
     * Return true if the list of intervals has been modified, false otherwise
     */
    public boolean add(int value){ // max[ O(log(n)), O(list.add(i,e)), O(list.remove(i)) ]
    	
    	if (verbose) {
    		System.out.println("add("+value+")");
    	}

		Interval indicator = indicies_intervals_containing(value); // O(log(nb_intervals))
        
        if (value_inside_interval(indicator)) { // value is already in an interval
            return false;
            
        } else if (value_below_intervals(indicator)) { // value is smaller than all elements in the set
            if (value + 1 == intervals.get(first()).bottom()) { // the first interval will be expanded from the bottom
                expand_interval(first(), true);
            } else {
                Interval new_interval = new Interval(value,value); // we have to create a new interval
                add_interval(first(), new_interval);
            }
            
        } else if (value_above_intervals(indicator)) { // value is greater than all elements in the set
        	if (value - 1 == intervals.get(last()).top()) { // the last interval will be expanded from to top
                expand_interval(last(), false);
            } else {
                Interval new_interval = new Interval(value,value);
                add_interval(last()+1, new_interval);
            }
        	
        } else if (value_between_intervals(indicator)){ // value is between two intervals
        	
        	Interval indicies = indicies_intervals_around_value(indicator);
        	
        	if (value - 1 == intervals.get(indicies.bottom()).top() && value + 1 == intervals.get(indicies.top()).bottom()) { // two intervals have to be merged
        		merge_intervals(indicies.bottom(),indicies.top());
        	} else if (value - 1 == intervals.get(indicies.bottom()).top()) { // left interval will be expanded
                expand_interval(indicies.bottom(), false);
            } else if (value + 1 == intervals.get(indicies.top()).bottom()) { // right interval will be expanded
                expand_interval(indicies.top(), true);
            } else { // a new interval has to be created
                Interval new_interval = new Interval(value,value);
                add_interval(indicies.top(),new_interval);
            }
        	
        } else {
        	throw new IllegalArgumentException("\n$ The indicator is broken.");
        }
        
        return true;
    }
    
    /**
     * Return the complementary set to @this
     */
    public IntervalSet complementary() {
    	
    	if (verbose) {
    		System.out.println("complementary()");
    	}
    	
    	if (is_empty()) {
    		return new IntervalSet(new Interval(-inf(),inf()));
    	}
    	
    	IntervalSet complem = new IntervalSet(new Interval(-inf(),intervals.get(first()).bottom()-1));
    	
    	for (int iter = first(); iter < last(); iter ++) {
    		if (intervals.get(iter).top()+1 <= intervals.get(iter+1).bottom()-1) {
    			complem.add_interval(complem.last()+1, new Interval(intervals.get(iter).top()+1,intervals.get(iter+1).bottom()-1));
    		}
    	}
    	
    	complem.add_interval(complem.last()+1, new Interval(intervals.get(last()).top()+1,inf()));
    	
    	return complem;
    }
    
    /**
     * @return True if the given integer is contained by the set,
     *         false otherwise
     */
    public boolean contains(int value) {
    	
    	Interval indicator = indicies_intervals_containing(value);

        return value_inside_interval(indicator);
    }
    
    /**
     * @return True if @this is the same as the given set,
     *         false otherwise
     */
    public boolean equals(Set set) {
    	
    	return intervals.equals(set.get_intervals());
    }
    
    /**
     * @return True if @this has no elements,
     *         false otherwise
     */
    public boolean is_empty() {
    	
    	return intervals.isEmpty();
    }

    /**
     * Remove all elements in the set
     */
    public void clear() {
    	
    	intervals.clear();
    }

    /**
     * Remove from @this the elements not contained by the given set
     */
    public boolean inter(Set set) {
    	
    	IntervalSet complem = set.complementary();
    	LinkedList<Interval> complem_intervals = complem.get_intervals();
    	
    	// extreme cases
    	if (is_empty() || complem.is_empty()) {
    		clear();
    	}
    	
    	int iter = first();
    	int iter_complem = complem.first();
    	
    	boolean stop = false; // termination
    	boolean result = false; // weither or not the list of intervals has been modified
    	boolean removed = false; // for updating result
    	int nb_intervals; // buffer for the number of intervals
    	boolean smaller_list = false; // weither the iteration (remove_interval_from) has reduced the number of intervals
    	
    	while (!stop) {
    		// initialization
    		smaller_list = false;
    		nb_intervals = last();
    		
    		// termination criteria
    		if (iter > last() || iter_complem > complem.last()) {
    			stop = true;
    		
    		// try removing
    		} else {
    			// filter
    			removed = remove_interval_from(complem_intervals.get(iter_complem),iter);
    			// if the list of intervals is smaller
    			if (last() < nb_intervals) {
    				smaller_list = true;
    			}
    			// update the result
        		if (removed) {
        			result = true;
        		}
        		// while we can reduce the number of intervals
        		while (smaller_list && iter <= last()) {
        			// init
        			smaller_list = false;
        			nb_intervals = last();
        			// filter
        			removed = remove_interval_from(complem_intervals.get(iter_complem),iter);
        			if (last() < nb_intervals) {
        				smaller_list = true;
        			}
        		}
        		
    			// update interators
    			if ((iter_complem < complem.last() && iter < last())) { // we can move the two iterators
    				if (intervals.get(iter+1).bottom() <= complem_intervals.get(iter_complem+1).bottom()) {
						iter += 1;
    				} else {
    					iter_complem += 1;
    				}
    			} else if (iter_complem < complem.last()) {
    				iter_complem += 1;
    			} else if (iter < last()) {
					iter += 1;
    			} else {
    				stop = true;
    			}
    		}
    	}
    	return result;
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
