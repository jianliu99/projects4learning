package com.gblmatrix.ir.mtfp;

public class ProcessorThread extends Thread
{
    public static volatile boolean running;
    public int id;
    
    /**
     * The thread is started immediately.
     */
    public ProcessorThread()
    {
        running = true;
        start();
    }
    
    /**
     * For testing only.
     */
    public ProcessorThread(int i)
    {
    	id = i;
    	start();
    }
    
    /**
     * Terminates all ProcessorThreads.
     */
    public static void killAll()
    {
        running = false;
    }  
    /**
     * Repeatedly takes tasks from the queue and runs them.
     */
    public void run()
    {
        while(running)
        {
            try
            {
                Runnable task = Processor.queue.take();
	            task.run();
            }
            catch(InterruptedException e) {e.printStackTrace();}
        }
    }
}