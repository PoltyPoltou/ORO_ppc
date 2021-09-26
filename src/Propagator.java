
/*
* Copyright (c) 2021, Nicolas Pierre, Eva Epoy, Jules Nicolas-Thouvenin. All rights reserved.
*
*/
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.List;
import java.util.Stack;

/**
 * Singleton Propagator is the handler of all constraints and variables. Mainly
 * it is used for constraint filtering and reverting it
 */
public class Propagator {
    private static Propagator singleton;

    public static Propagator getPropagator() {
        if (Propagator.singleton == null) {
            Propagator.singleton = new Propagator();
        }
        return Propagator.singleton;
    }

    private HashSet<Constraint> constraints;
    private HashSet<SetVariable> variables;
    private Stack<List<Pair<SetVariable, Integer>>> stack_variables_set_addition;
    private Stack<List<Pair<SetVariable, Integer>>> stack_variables_set_removal;
    private Stack<Map<SetVariable, Pair<Integer, Integer>>> stack_variables_cardinality_changes;

    private Propagator() {
        this.constraints = new HashSet<Constraint>();
        this.variables = new HashSet<SetVariable>();
        this.stack_variables_set_addition = new Stack<List<Pair<SetVariable, Integer>>>();
        this.stack_variables_set_removal = new Stack<List<Pair<SetVariable, Integer>>>();
        this.stack_variables_cardinality_changes = new Stack<Map<SetVariable, Pair<Integer, Integer>>>();
    }

    public void add_constraint(Constraint constraint) {
        constraints.add(constraint);
    }

    public void add_variable(SetVariable variable) {
        variables.add(variable);
    }

    public boolean filter(SetVariable starting_variable) {
        List<Pair<SetVariable, Integer>> list_variables_addition_pair = new LinkedList<Pair<SetVariable, Integer>>();
        List<Pair<SetVariable, Integer>> list_variables_removal_pair = new LinkedList<Pair<SetVariable, Integer>>();
        Map<SetVariable, Pair<Integer, Integer>> map_variables_cardinality_pair = new HashMap<SetVariable, Pair<Integer, Integer>>();
        Set<Constraint> woken_up_constraints = new HashSet<Constraint>();
        List<SetVariable> var_list = new LinkedList<SetVariable>();
        List<Pair<Integer, Integer>> cardinality_list = new LinkedList<Pair<Integer, Integer>>();
        List<List<Integer>> removal_list = new LinkedList<List<Integer>>();
        List<List<Integer>> addition_list = new LinkedList<List<Integer>>();
        boolean must_backtrack = false;

        // getting every constraint where starting variable is involved
        for (Constraint constraint : constraints) {
            if (constraint.get_variables().contains(starting_variable)) {
                woken_up_constraints.add(constraint);
            }
        }
        // looping waiting for a fixed point
        while (!woken_up_constraints.isEmpty()) {
            // geting a constraint in the set and removing it
            Constraint woken_constraint = woken_up_constraints.iterator().next();
            woken_up_constraints.remove(woken_constraint);
            // get filtering informations
            woken_constraint.filter(var_list, cardinality_list, removal_list, addition_list);

            SetVariable filtered_var;
            Pair<Integer, Integer> new_cardinal;
            List<Integer> to_remove_from_upper;
            List<Integer> to_add_to_lower;
            boolean is_updated;
            // iteration on every variable that must be modified
            // with our constraints this loop is only done twices or 3 times
            while (!var_list.isEmpty()) {
                // getting info to update filtered_var bounds
                filtered_var = var_list.get(0);
                new_cardinal = cardinality_list.get(0);
                to_remove_from_upper = removal_list.get(0);
                to_add_to_lower = addition_list.get(0);
                is_updated = false;
                var_list.remove(0);
                cardinality_list.remove(0);
                removal_list.remove(0);
                addition_list.remove(0);

                if (filtered_var.get_min_cardinality() != new_cardinal.first()
                        || filtered_var.get_max_cardinality() != new_cardinal.second()) {
                    filtered_var.decrease_max_cardinality(new_cardinal.second() - filtered_var.get_max_cardinality());
                    filtered_var.increase_min_cardinality(new_cardinal.first() - filtered_var.get_min_cardinality());
                    map_variables_cardinality_pair.put(filtered_var, new_cardinal);
                    is_updated = true;
                }
                for (int to_add : to_add_to_lower) {
                    if (filtered_var.add_to_lower(to_add)) {
                        list_variables_addition_pair.add(new Pair<SetVariable, Integer>(filtered_var, to_add));
                        is_updated = true;
                    }
                }
                for (int to_remove : to_remove_from_upper) {
                    if (filtered_var.remove_from_upper(to_remove)) {
                        list_variables_removal_pair.add(new Pair<SetVariable, Integer>(filtered_var, to_remove));
                        is_updated = true;
                    }
                }
                // if the variable has been changed we add every constraint that contains
                // filtered_var
                // However woken_up_constraints is a set, so we don't have duplicates
                if (is_updated) {
                    for (Constraint constraint : constraints) {
                        if (constraint.get_variables().contains(filtered_var)) {
                            woken_up_constraints.add(constraint);
                        }
                    }
                }
                if (!filtered_var.is_valid()) {
                    // We clear these two containers, so that we exit immediately the loops
                    // and set the backtrack flag to true
                    var_list.clear();
                    woken_up_constraints.clear();
                    must_backtrack = true;
                }
            }
        }
        stack_variables_set_addition.push(list_variables_addition_pair);
        stack_variables_set_removal.push(list_variables_removal_pair);
        stack_variables_cardinality_changes.push(map_variables_cardinality_pair);
        return must_backtrack;
    }

    public void backtrack() {
    }
}
