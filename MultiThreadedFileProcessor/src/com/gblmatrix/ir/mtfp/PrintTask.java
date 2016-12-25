package com.gblmatrix.ir.mtfp;

import java.io.*;

public class PrintTask implements Runnable
{
    private File toBePrinted;
    
    /**
     * Very standard and basic constructor.
     */
    public PrintTask(File tbp)
    {
        toBePrinted = tbp;
    }
    
    /**
     * Self-explanatory: this is very much a helper task, implemented for the sole reason of ease of
     * addition to the queue.
     */
    public void run()
    {
    	try
    	{
    	    Processor.process(toBePrinted); 
    	}
    	catch(Exception e) {Processor.logger.warn(e,e);}
    }
}