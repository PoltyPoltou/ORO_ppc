/*
 * Copyright (c) 2021, Nicolas Pierre, Eva Epoy, Jules Nicolas-Thouvenin. All rights reserved.
 *
 */

/**
 * Application
 */
public class App {

	public static void main(String[] args) {
		
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

}
