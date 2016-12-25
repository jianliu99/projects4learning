package com.gblmatrix.ir.mtfp;

public class TerminationThread extends Thread
{
	/**
	 * Executes basic shutdown maneuvers that will make sure the program terminates properly (i.e. closes log files)
	 */
	public void run()
	{
		System.out.println("In shutdown hook.");
		Processor.totalRunTime += System.currentTimeMillis();
		Processor.terminate(true);
	}
}
