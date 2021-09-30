
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
     * @return True if the given integer is contained by the set,
     *         false otherwise
     */
    public boolean contains(int value);

    /**
     * @return True if @this is the same as the given set,
     *         false otherwise
     */
    public boolean different(Set set);

    /**
     * @return Remove from @this the elements not contained by the given set
     */
    public void inter(Set set);

    /**
     * @return Add to @this the elements contained by the given set
     */
    public void union(Set set);

    /**
     * @return Remove from @this the elements contained by the given set
     */
    public void deprivedOf(Set set);

    /**
     * @return Returns the list of values in the set
     */
    public List<Integer> get_values();
}