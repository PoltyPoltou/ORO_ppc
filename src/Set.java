
/*
 * Copyright (c) 2021, Nicolas Pierre, Eva Epoy, Jules Nicolas-Thouvenin. All rights reserved.
 *
 */

import java.util.LinkedList;

/**
 * Interface for all set Sets
 */
public interface Set {

    /**
     * @return True if the given integer has been added to the set,
     *         false otherwise
     */
    public boolean add(int value);

    /**
     * @return True if the given integer has been removed to the set,
     *         false otherwise
     */
    public boolean remove(int value);
    
    /**
     * Return the complementary set to @this
     */
    public IntervalSet get_complementary();

    /**
     * @return True if the given integer is contained by the set,
     *         false otherwise
     */
    public boolean contains(int value);

    /**
     * @return True if @this is the same as the given set,
     *         false otherwise
     */
    public boolean equals(Set set);

    /**
     * @return True if @this has no elements,
     *         false otherwise
     */
    public boolean is_empty();

    /**
     * Remove all elements in the set
     */
    public void clear();

    /**
     * Remove from @this the elements not contained by the given set
     */
    public boolean inter(Set set);

    /**
     * Add to @this the elements contained by the given set
     */
    public void union(Set set);

    /**
     * Remove from @this the elements contained by the given set
     */
    public void deprived_of(Set set);
    
    /**
     * Return the number of values in the set
     */
    public int size();

    /**
     * @return Returns the list of intervals of the set (for intervalSets only)
     */
    public LinkedList<Interval> get_intervals();

    /**
     * @return Returns the list of values in the set
     */
    public LinkedList<Integer> get_values();

}
