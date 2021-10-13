
/*
 * Copyright (c) 2021, Nicolas Pierre, Eva Epoy, Jules Nicolas-Thouvenin. All rights reserved.
 *
 */

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class InterEmptyConstraint implements Constraint {
	
	private SetVariable A;
	private SetVariable B;
	
	public InterEmptyConstraint(SetVariable A, SetVariable B) {
		this.A = A;
		this.B = B;
	}

	@Override
	public boolean entailment() {
		// ENTAILMENT if intersection of upper bounds is empty
		IntervalSet intersection = (IntervalSet) A.get_upper_bound().clone();
		
		intersection.inter(B.get_upper_bound());
		
		return intersection.is_empty();
	}

	@Override
	public boolean failure() {
		// FAILURE if intersection of lower bounds is not empty
		IntervalSet intersection = (IntervalSet) A.get_lower_bound().clone();
		
		intersection.inter(B.get_lower_bound());
		
		return (!intersection.is_empty());
	}

	@Override
	public void filter(List<SetVariable> var_list, List<Pair<Integer, Integer>> cardinality_list,
			List<List<Integer>> removal_list, List<List<Integer>> addition_list) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public Collection<SetVariable> get_variables() {
		LinkedList<SetVariable> variables = new LinkedList<SetVariable>();
		
		variables.add(A);
		variables.add(B);
		
		return variables;
	}

}