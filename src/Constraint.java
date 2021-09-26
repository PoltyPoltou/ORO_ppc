/*
 * Copyright (c) 2021, Nicolas Pierre, Eva Epoy, Jules Nicolas-Thouvenin. All rights reserved.
 *
 */

import java.util.Collection;
import java.util.List;

/**
 * Interface for all set Constraint
 */
public interface Constraint {
    /**
     * @return true if the constraint is always satisfied
     */
    public boolean entailment();

    public Collection<SetVariable> get_variables();

    /**
     * @return true if the constrant cannot be satisfied
     */
    public boolean failure();

    /**
     * Filtering function, will <b>NOT</b> have any side effects on the variables.
     * These lists are passed as parameters but they are going to be completed with
     * the result of the filtering
     *
     * @param var_list         list for the variables that must be changed
     * @param cardinality_list new cardinal for the variable at the same index in
     *                         var_list
     * @param removal_list     list of integers to remove from the upper bound of
     *                         the variable at the same index in var_list
     * @param addition_list    list of integers to add to the lower bound of the
     *                         variable at the same index in var_list
     */
    public void filter(List<SetVariable> var_list, List<Pair<Integer, Integer>> cardinality_list,
            List<List<Integer>> removal_list, List<List<Integer>> addition_list);
}
