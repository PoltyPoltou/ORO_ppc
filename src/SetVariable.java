/*
 * Copyright (c) 2020, Nicolas Pierre, Eva Epoy, Jules Nicolas-Thouvenin. All rights reserved.
 *
 */

/**
 * Note (jules) : just a first draft.
 *
 * We have to discuss several points :
 * > what structure to store lower and upper bounds (ArrayList, LinkedList, Arrays...)
 * TreeSet ? log(n) access but always sorted
 * > do we keep the lower and upper bound sorted, even if they are sets ?
 * > other small things, variables names, types of exceptions...
 *
 * The functions increase and decrease cardinality are not finished. They maybe need to check
 * more preconditions (min <= max, ...)
 */

import java.util.TreeSet;

/**
 * The object representing a set variable.
 */
public class SetVariable {

	/**
	 * The list of integers that <em>must</em> be in the set variable.
	 */
	private TreeSet<Integer> lower_bound;

	/**
	 * The list of integers that <em>might</em> be in the set variable.
	 */
	private TreeSet<Integer> upper_bound;

	/**
	 * The minimum number of integers in the set variable.
	 */
	private int min_cardinality;

	/**
	 * The maximum number of integers in the set variable.
	 */
	private int max_cardinality;

	/**
	 * The initial list of integers that <em>must</em> be in the set variable.
	 */
	private TreeSet<Integer> default_lower_bound;

	/**
	 * The initial list of integers that <em>might</em> be in the set variable.
	 */
	private TreeSet<Integer> default_upper_bound;

	/**
	 * The initial minimum number of integers in the set variable.
	 */
	private int default_min_cardinality;

	/**
	 * The initial maximum number of integers in the set variable.
	 */
	private int default_max_cardinality;

	/**
	 * Creates a new {@code SetVariable} instance associated with the given default
	 * lower and upper bounds.
	 *
	 * @param default_lower_bound     The TreeSet of minimum integers that must be
	 *                                in the set variable
	 * @param default_upper_bound     The TreeSet of maximum integers that might be
	 *                                in the set variable
	 * @param default_min_cardinality The minimum number of integers that must be in
	 *                                the set variable
	 * @param default_max_cardinality The maximum number of integers that might be
	 *                                in the set variable
	 */
	public SetVariable(TreeSet<Integer> default_lower_bound, TreeSet<Integer> default_upper_bound,
			int default_min_cardinality, int default_max_cardinality) {

		this.lower_bound = new TreeSet<Integer>(default_lower_bound);
		this.upper_bound = new TreeSet<Integer>(default_upper_bound);

		this.min_cardinality = default_min_cardinality;
		this.max_cardinality = default_max_cardinality;

		this.default_upper_bound = default_upper_bound;
		this.default_upper_bound = default_upper_bound;

		this.default_min_cardinality = default_min_cardinality;
		this.default_max_cardinality = default_max_cardinality;
	}

	/**
	 * Try removing the given integer from the lower bound and return whether or not
	 * the integer has been removed.
	 *
	 * @param variable The integer variable to remove from the lower bound
	 * @return True if the given integer has been removed from the lower bound,
	 *         false otherwise
	 */
	public boolean remove_from_lower(int variable) {
		return this.lower_bound.remove(variable);
	}

	/**
	 * Try removing the given integer from the upper bound and return whether or not
	 * the integer has been removed.
	 *
	 * @param variable The integer variable to remove from the upper bound
	 * @return True if the given integer has been removed from the upper bound,
	 *         false otherwise
	 */
	public boolean remove_from_upper(int variable) {
		return this.upper_bound.remove(variable);
	}

	/**
	 * Try adding the given integer to the lower bound and return whether or not the
	 * integer has been added.
	 *
	 * @param variable The integer variable to add to the lower bound
	 * @return True if the given integer has been added to the lower bound, false
	 *         otherwise
	 */
	public boolean add_to_lower(int variable) {
		return this.lower_bound.add(variable);
	}

	/**
	 * Try adding the given integer to the upper bound and return whether or not the
	 * integer has been added.
	 *
	 * @param variable The integer variable to add to the upper bound
	 * @return True if the given integer has been added to the upper bound, false
	 *         otherwise
	 */
	public boolean add_to_upper(int variable) {
		return this.upper_bound.add(variable);
	}

	/**
	 * Increase the minimum number of integers in the set variable by delta.
	 *
	 * <p>
	 * Assertion : delta >= 0
	 * </p>
	 *
	 * @param delta The non-negative quantity to add to min_cardinality
	 * @return True if the min_cardinality has been increased, false otherwise
	 */
	public boolean increase_min_cardinality(int delta) {
		// Check assertions
		if (delta < 0) {
			throw new IllegalArgumentException("\n$ delta = " + delta
					+ ". Parameter 'delta' must be non-negative because min_cardinality can only be increased.");
		}
		// Body
		else {
			min_cardinality += delta;
			return true;
		}
	}

	/**
	 * Decrease the maximum number of integers in the set variable by -delta.
	 *
	 * <p>
	 * Assertion : delta <= 0
	 * </p>
	 *
	 * @param delta The negative or null quantity to add to max_cardinality
	 * @return True if the max_cardinality has been decreased, false otherwise
	 */
	public boolean decrease_max_cardinality(int delta) {
		// Check assertions
		if (delta > 0) {
			throw new IllegalArgumentException("\n$ delta = " + delta
					+ ". Parameter 'delta' must be negative or null because max_cardinality can only be decreased.");
		}
		// Body
		else {
			max_cardinality += delta;
			return true;
		}
	}

	public boolean is_valid() {
		if (this.max_cardinality < this.lower_bound.size()) {
			return false;
		}
		if (this.min_cardinality < this.upper_bound.size()) {
			return false;
		}
		if (this.max_cardinality < this.min_cardinality) {
			return false;
		}
		// Costly operation O(n*ln(n))
		if (this.upper_bound.containsAll(this.lower_bound)) {
			return false;
		}
		return true;
	}

}
