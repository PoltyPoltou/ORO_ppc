import java.util.LinkedList;

/*
 * Copyright (c) 2021, Nicolas Pierre, Eva Epoy, Jules Nicolas-Thouvenin. All rights reserved.
 *
 */

/**
 * Application
 */
public class App {

	public static void main(String[] args) {
		test_inter();
	}
	
	public static void demo_1() {
		System.out.println("IntervalSet mySet = new IntervalSet(new Interval(5,10));");
		IntervalSet mySet = new IntervalSet(new Interval(5,10));
	
		// mySet.enableVerbose(); // For debug only
		mySet.printIntervals();
		
		System.out.println("mySet.remove(7);");
		mySet.remove(7);
		mySet.printIntervals();
		
		System.out.println("mySet.add(12);");
		mySet.add(12);
		mySet.printIntervals();
		
		System.out.println("mySet.add(11);");
		mySet.add(11);
		mySet.printIntervals();
		
		System.out.println("mySet.add(4);");
		mySet.add(4);
		mySet.printIntervals();
		
		System.out.println("mySet.remove(11);");
		mySet.remove(11);
		mySet.printIntervals();
	}
	
	public static void demo_2() {
		System.out.println("IntervalSet mySet = new IntervalSet(new Interval(5,10));");
		IntervalSet mySet = new IntervalSet(new Interval(5,10));
	
		// mySet.enableVerbose(); // For debug only
		mySet.printIntervals();
		
		System.out.println("mySet.remove(7);");
		mySet.remove(7);
		mySet.printIntervals();
		
		System.out.println("mySet.add(12);");
		mySet.add(12);
		mySet.printIntervals();
		
		System.out.println("mySet.add(11);");
		mySet.add(11);
		mySet.printIntervals();
		
		System.out.println("mySet.add(4);");
		mySet.add(4);
		mySet.printIntervals();
		
		System.out.println("mySet.remove(11);");
		mySet.remove(11);
		mySet.printIntervals();
		
		LinkedList<Interval> intervals = new LinkedList<Interval>();
		intervals.add(new Interval(4,6));
		intervals.add(new Interval(8,10));
		intervals.add(new Interval(12,12));
		IntervalSet E = new IntervalSet(intervals);
		
		System.out.println("\nE.printIntervals();");
		E.printIntervals();
		System.out.println("mySet.equals(E) = "+mySet.equals(E));
		System.out.println("E.is_empty() = "+E.is_empty());
		System.out.println("E.clear();");
		E.clear();
		E.printIntervals();
		System.out.println("E.is_empty() = "+E.is_empty());
	}
	
	public static void demo_3() {
		LinkedList<Interval> intervals = new LinkedList<Interval>();
		intervals.add(new Interval(4,5));
		intervals.add(new Interval(6,9));
		intervals.add(new Interval(13,15));
		IntervalSet S = new IntervalSet(intervals);
		
		intervals = new LinkedList<Interval>();
		intervals.add(new Interval(1,4));
		intervals.add(new Interval(5,6));
		intervals.add(new Interval(9,13));
		IntervalSet E = new IntervalSet(intervals);
		
		System.out.println("\nE.printIntervals();");
		E.printIntervals();
		System.out.println("\nS.printIntervals();");
		S.printIntervals();
		System.out.println("--------------------------");
		
		S.enableVerbose();
		E.enableVerbose();
		
		S.inter(E);
		
		S.printIntervals();
	}
	
	public static void test_inter() {
		
		int size_S = 5+(int) (Math.random()*6);
		int size_E = 5+(int) (Math.random()*6);
		int random_buffer_1;
		int random_buffer_2;
		
		LinkedList<Interval> intervals = new LinkedList<Interval>();
		
		int last_value = 0;
		
		for (int iter = 0; iter < size_S; iter ++) {
			random_buffer_1 = 2 + (int) (Math.random()*10);
			random_buffer_2 = (int) (Math.random()*10);
			intervals.add(new Interval(last_value+random_buffer_1,last_value+random_buffer_1+random_buffer_2));
			last_value = last_value+random_buffer_1+random_buffer_2;
		}
		
		IntervalSet S = new IntervalSet(intervals);
		
		S.printIntervals();
		
		intervals = new LinkedList<Interval>();
		
		last_value = 0;
		
		for (int iter = 0; iter < size_E; iter ++) {
			random_buffer_1 = 2 + (int) (Math.random()*10);
			random_buffer_2 = (int) (Math.random()*10);
			intervals.add(new Interval(last_value+random_buffer_1,last_value+random_buffer_1+random_buffer_2));
			last_value = last_value+random_buffer_1+random_buffer_2;
		}
		
		IntervalSet E = new IntervalSet(intervals);
		
		E.printIntervals();
		
		S.inter(E);
		
		S.printIntervals();
	}

}
