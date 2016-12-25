package com.gblmatrix.ir.mtfp;

import java.io.*;
import java.nio.channels.FileChannel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.ArrayList;
import java.util.Scanner;

import com.gblmatrix.utils.session.SessionUtils;
import org.apache.log4j.*;
import com.globalmatrix.IRLoader.ReadSabre;

public class Processor
{
    // determines what type of processing should be done
    public static enum ProcessorType {SABRE, APOLLO, AMADEUS, ALL, NOT_RECOGNIZED};
    public static ProcessorType thisInstanceIs;
	
    // configuration file (location will be dependent on the ProcessorType, see initialize() method for the implementation)
    static File CONFIG; 
    
    // all threads will draw tasks from this queue
    static LinkedBlockingQueue<Runnable> queue = new LinkedBlockingQueue<Runnable>();
    
    public static ArrayList<File> sourceDirectories = new ArrayList<File>();
    public static File targetDirectory;
    public static int numberOfThreads;
    public static File logDirectory;
    public static File exceptionLogDirectory;
    public static int filesProcessed = 0;
    public static long totalRunTime = 0;
    public static long processTime = 0;
    
    public static Logger logger = Logger.getLogger("com.gblmatrix.ir.mtfp");
    
    public static String sessionIDServer;
    public static String sessionIDPort;
    
    // various formats for parsing/formatting dates to/from Strings
    public static final SimpleDateFormat LOG4J_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    public static final SimpleDateFormat BASE_FORMAT = new SimpleDateFormat("EEE MMM dd HH:mm:ss:SSS yyyy");
    public static final SimpleDateFormat INPUT_FORMAT = new SimpleDateFormat("yyyy MM dd HH:mm:ss");
    public static final SimpleDateFormat METRICS_FORMAT = new SimpleDateFormat("ddMMMyyyy HH-mm-ss-SSS");
    public static final SimpleDateFormat LOG_HELPER_FORMAT = new SimpleDateFormat("yyyy MM dd");
    
    public static ArrayList<File> allInputFiles = new ArrayList<File>();
    
    public static ProcessorThread[] workers;
    
    // control flow
    public static Scanner scan = new Scanner(System.in);
   
    /**
     * Basic outline of algorithm:
     * 
     * Constantly check the target directory for new folders and files.
     * 
     * Whenever new files appear within an existing folder, create a PrintTask for each file and add the task to the queue.
     * 
     * The ProcessThreads wait until tasks appear in the queue, and then run them by calling the process() method.
     */
    public static void main(String[] args)
    {
    	// the menu for configuration options will only be displayed once
    	boolean firstPass = true;
    	
        try
        {
            if(!(initialize(args)))
            {
                System.out.println("Initialization failed, press enter to continue.");
                scan.nextLine();
                System.exit(0);
            }
            
            // from somewhere in this loop System.exit(0) will be called, so the loop can go on forever.
            while(true)
            {
            	// command line arguments used to determine what mode the program will be run in
                if(args[0].equals("measure performance"))
                {
                    System.out.println("From what start time?");
                    Date startDate = promptTime();
                    Date startDateForLogs = LOG_HELPER_FORMAT.parse(LOG_HELPER_FORMAT.format(startDate));
                    System.out.println("To what end time?");
                    Date endDate = promptTime();
                    Date endDateForLogs = LOG_HELPER_FORMAT.parse(LOG_HELPER_FORMAT.format(endDate));
                		
                    File[] allLogs = logDirectory.listFiles();
                		
                    // the relevant log files will accumulate here
                    ArrayList<File> rightLogs = new ArrayList<File>();
                		
                    boolean measuringAll = false;
                		
                    if(thisInstanceIs.toString().equals("ALL"))
                	measuringAll = true;
                    
            	    for(File f : allLogs)
            	    {
            		// will only collect the log files of the appropriate type
            		if(f != null && (!f.isDirectory()) && (measuringAll || f.getName().indexOf(thisInstanceIs.toString()) != -1))
            		{
            		    String logName = f.getName();
            		    
            		    if(logName.indexOf('-') != -1)
            		    {
            			String $logDate = logName.substring(logName.indexOf("log.log.") + 8);
            		    	Date logDate = LOG4J_FORMAT.parse($logDate);
            		    	
            		    	if((logDate.before(endDateForLogs) && logDate.after(startDateForLogs)) || logDate.equals(startDateForLogs) || logDate.equals(endDateForLogs))
            		    	    rightLogs.add(f);
            		    }
            		    else
            			rightLogs.add(f);
            		}
            	    }
            		
            	    System.out.println("Metric?");
                	
            	    while(true)
            	    {
	                String metric = scan.nextLine();
	                	
	                if(metric.equalsIgnoreCase("number of files"))
	                {
	                    metricNumFiles(rightLogs, startDate, endDate);
	                		
	                    System.exit(0);
	                }
	                else if(metric.equalsIgnoreCase("list files"))
	                {
	               	    metricListFiles(rightLogs, startDate, endDate);
	                		
	               	    System.exit(0);
	                }
	                else if(metric.equalsIgnoreCase("average processing time"))
	                {
	               	    metricAPT(rightLogs, startDate, endDate);
	                		
	               	    System.exit(0);
	                }
	                else if(metric.equalsIgnoreCase("all"))
	                {
        	             metricNumFiles(rightLogs, startDate, endDate);
        	             metricListFiles(rightLogs, startDate, endDate);
        	             metricAPT(rightLogs, startDate, endDate);
        	                		
        	             System.exit(0);
	                }
	                else if(metric.equalsIgnoreCase("cancel"))
	                {
	                     System.exit(0);
	                }
            	    }
                }
                else if(args[0].equals("start"))
                {
                    // programmatically configuring log4j (because the log directory cannot be known beforehand, and I want to keep this control with the config file)
                    DailyRollingFileAppender regularLog = new DailyRollingFileAppender();
                    regularLog.setFile(logDirectory.getPath() + "/" + thisInstanceIs + "log.log");
                    regularLog.setAppend(true);
                    regularLog.setThreshold(Level.DEBUG);
                    regularLog.setLayout(new PatternLayout("%m%n"));
                    regularLog.setDatePattern("'.'yyyy-MM-dd");
                    regularLog.activateOptions();
                    
                    DailyRollingFileAppender exceptionLog = new DailyRollingFileAppender();
                    exceptionLog.setFile(exceptionLogDirectory.getPath() + "/" + thisInstanceIs + "exlog.log");
                    exceptionLog.setAppend(true);
                    exceptionLog.setThreshold(Level.WARN);
                    exceptionLog.setLayout(new PatternLayout("%d{HH:mm:ss} Thread: %t %m%n"));
                    exceptionLog.setDatePattern("'.'yyyy-MM-dd"); 
                    exceptionLog.activateOptions();
                	 
                    logger.addAppender(regularLog);
                    logger.addAppender(exceptionLog);
                    
                    logger.warn("Program successfully initialized at " + new Date());
                    
                    totalRunTime = totalRunTime - System.currentTimeMillis();
                    
                    // to ensure that when the process is killed, the log file still updates properly
                    Runtime.getRuntime().addShutdownHook(new TerminationThread());
                    
                    body();
                }
                else if(args[0].equals("modify settings"))
                {
                	if(firstPass)
                	{
                	    System.out.println("Valid commands: clean sources, clean target, set threads, add source [directory], set target [directory], set log [directory], set exception log [directory], get threads, get sources, get target, get log, get exception log. Type done to exit.");
                	    firstPass = !firstPass;
                	}
                	
                	String command = scan.nextLine();
                	
	                if(command.equalsIgnoreCase("clean sources"))
	                {
	                    System.out.println("Are you sure? (Y/N)");
	                    
	                    if(scan.nextLine().equals("Y"))
	                    {
	                        for(File f : sourceDirectories)
	                            for(File g : f.listFiles())
	                                fullClean(g);
	                                
	                        System.out.println("All source folders cleaned.");
	                    }
	                    else
	                        System.out.println("Cleaning aborted.");
	                }
	                else if(command.equalsIgnoreCase("clean target"))
	                {
	                    System.out.println("Are you sure? (Y/N)");
	                    
	                    if(scan.nextLine().equals("Y"))
	                    {
	                        for(File f : targetDirectory.listFiles())
	                            fullClean(f);
	                        
	                        System.out.println("Target folder cleaned.");
	                    }
	                    else
	                        System.out.println("Cleaning aborted.");
	                }
	                else if(command.length() > 10 && command.substring(0, 10).equalsIgnoreCase("add source"))
	                {
	            	    sourceDirectories.add(new File(command.substring(11)));
	            	    System.out.println("Added new source folder.");
	                }
	            	else if(command.length() > 10 && command.substring(0, 10).equalsIgnoreCase("set target"))
	            	{
	            	    targetDirectory = new File(command.substring(11));
	            	    System.out.println("Target folder changed.");
	                }
	            	else if(command.length() > 7 && command.substring(0, 7).equalsIgnoreCase("set log"))
	            	{
	            	    logDirectory = new File(command.substring(8));
	            	    System.out.println("Log folder changed.");
	                }
	            	else if(command.length() > 17 && command.substring(0, 17).equalsIgnoreCase("set exception log"))
	            	{
	            	    exceptionLogDirectory = new File(command.substring(18));
	            	    System.out.println("Exception log folder changed.");
	                }
	            	else if(command.equalsIgnoreCase("get sources"))
	                {
	            	    for(File f : sourceDirectories)
	            		System.out.println(f);
	                }
	            	else if(command.equalsIgnoreCase("get target"))
	                {
	            	    System.out.println(targetDirectory);
	                }
	            	else if(command.equalsIgnoreCase("get log"))
	                {
	            	    System.out.println(logDirectory);
	                }
	            	else if(command.equalsIgnoreCase("get exception log"))
	                {            		
	            	    System.out.println(exceptionLogDirectory);
	                }
	            	else if(command.equalsIgnoreCase("get threads"))
	                {
	            	    System.out.println(numberOfThreads);
	                }
	            	else if(command.length() >= 11 && command.substring(0, 11).equalsIgnoreCase("set threads"))
	            	{
	            	    try
	            	    {
	            		int newThreads = Integer.parseInt(command.substring(12));
	            			
	            		if(newThreads > 0)
	            		{
	            		    numberOfThreads = newThreads;
	            		    workers = new ProcessorThread[numberOfThreads];
	            		    System.out.println("Number of threads changed.");
	            		}
	            		else
	            		    throw new NumberFormatException();
	            	    }
	            	    catch(NumberFormatException e) {System.out.println("That is not a valid number of threads.");}
	            	}
	            	else if(command.equalsIgnoreCase("done"))
	            	{
	            	    // update the config file without creating any new log file
	            	    terminate(false);
	            	    System.exit(0);
	            	}
	            	else
	            	{
	            	    System.out.println("Not a valid command.");
	            	}
               }
               else
               {
            	   System.exit(0);
               }   	
	    }
        }
        catch(Exception e) 
        {
            e.printStackTrace(System.err);
            terminate(true);
        }
    }
    
    /**
     * Displays and writes to file the average processing time of a file within the given date bounds.
     */
    public static void metricAPT(ArrayList<File> rightLogs, Date startDate, Date endDate) throws Exception
    {
    	int totalFilesProcessed = 0;
	long totalProcessingTime = 0;
		
	for(File f : rightLogs)
	{
	    BufferedReader br = new BufferedReader(new FileReader(f));
	    String nextLine = br.readLine();
			
	    while(nextLine != null)
	    {
    		if(nextLine.substring(0, 8).equals("Finished"))
    		{
    		    Date logDate = BASE_FORMAT.parse(nextLine.substring(nextLine.lastIndexOf("at") + 3));
    				
    	            if(logDate.before(endDate) && logDate.after(startDate))
    		    {
    		        totalFilesProcessed++;
    		        totalProcessingTime = totalProcessingTime + Integer.parseInt(nextLine.substring(nextLine.lastIndexOf("e: ") + 3));
    		    }
    		    else if(logDate.after(endDate))
    			break;
    		}
    			
    		nextLine = br.readLine();
	    }
			
	    br.close();
	}
		
	System.out.println("Average processing time within that time period for " + thisInstanceIs + ": " + ((double)totalProcessingTime / totalFilesProcessed) + " milliseconds per file.");
		
	// the PrintWriter is started in append mode: stuff will accumulate within this file
	File avgProcTimeResults = new File(logDirectory + "/Metrics/AvgProcTimeResults.txt");
	PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(avgProcTimeResults, true)));
		
	pw.println("The average file processed between " + METRICS_FORMAT.format(startDate) + " and " + METRICS_FORMAT.format(endDate) + " took " + ((double)totalProcessingTime / totalFilesProcessed) + " milliseconds for " + thisInstanceIs + ".");
	pw.close();
    }
    
    /**
     * Writes to file every filename/path processed within the given date bounds.
     */
    public static void metricListFiles(ArrayList<File> rightLogs, Date startDate, Date endDate) throws Exception
    {
    	File fileList = new File(logDirectory + "/Metrics/List of Processed Files/" + thisInstanceIs + " Files Processed from " + startDate.toString().replace(':', '-') + " to " + endDate.toString().replace(':', '-') + ".txt");
		
	if(!fileList.getParentFile().exists())
	    fileList.getParentFile().mkdirs();
		
	fileList.setWritable(true);
	PrintWriter pw = new PrintWriter(fileList);
		
	for(File logFile : rightLogs)
	{
	    BufferedReader br = new BufferedReader(new FileReader(logFile));
	    String nextLine = br.readLine();
			
	    while(nextLine != null)
	    {
    		if(nextLine.substring(0, 8).equals("Finished"))
    		{
    		    Date logDate = BASE_FORMAT.parse(nextLine.substring(nextLine.lastIndexOf("at") + 3));
    				
    		    if(logDate.before(endDate) && logDate.after(startDate))
    			pw.println(nextLine.substring(25, nextLine.lastIndexOf("at") - 1));
    		    else if(logDate.after(endDate))
    			break;
    		}
    			
    		nextLine = br.readLine();
	    }
	    br.close();
	}
	pw.close();
    }
    
    /**
     * Displays and writes to file the number of files processed within the given date bounds.
     */
    public static void metricNumFiles(ArrayList<File> rightLogs, Date startDate, Date endDate) throws Exception
    {
    	int totalFilesProcessed = 0;
		
	for(File logFile : rightLogs)
	{
	    BufferedReader br = new BufferedReader(new FileReader(logFile));
	    String nextLine = br.readLine();
			
	    while(nextLine != null)
	    {
    		if(nextLine.substring(0, 8).equals("Finished"))
    		{
    		    Date logDate = BASE_FORMAT.parse(nextLine.substring(nextLine.lastIndexOf("at") + 3));
    				
    		    if(logDate.before(endDate) && logDate.after(startDate))
    			totalFilesProcessed++;
    		    else if(logDate.after(endDate))
    			break;
    		}
    			
    		nextLine = br.readLine();
	    }
	    br.close();
	}
		
	System.out.println("A total of " + totalFilesProcessed + " files were processed in the indicated period of time by " + thisInstanceIs + ".");
		
	File numFilesResults = new File(logDirectory + "/Metrics/NumFilesResults.txt");
	PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(numFilesResults, true)));
		
	pw.println(totalFilesProcessed + " files were processed from " + METRICS_FORMAT.format(startDate) + " to " + METRICS_FORMAT.format(endDate) + " by " + thisInstanceIs + ".");
	pw.close();
    }
    
    /**
     * Prompts user for a time, returns a Date object.
     */
    public static Date promptTime() 
    {
    	while(true)
    	{
    	    try
    	    {
	    	System.out.print("Enter a date and time in the format yyyy MM dd HH:mm:ss, or type 'now' with no quotes for the current time, or type '-x hours' for a time x hours ago: ");
	    		
	    	String input = scan.nextLine();
	    		
	    	if(input.equalsIgnoreCase("now"))
	    	   return new Date();
	    	else if(input.charAt(0) == '-')
	    	{
	    	    try
	    	    {
	    		double hours = Double.parseDouble(input.substring(1, input.indexOf(" hours")));
	    				
	    		if(hours > 0)
	    		{
	    		    return new Date(System.currentTimeMillis() - (long)(3600000 * hours));
	    		}
	    	    }
	    	    catch(Exception e){}
	    	}
	    	else
	    	{
	    	    Date inputDate = INPUT_FORMAT.parse(input);
	    		
	    	    if(inputDate != null)
	    		return inputDate;
	    	}
    	    }
    	    catch(ParseException pe) {}
    	}
    }
    
    /**
     * The main body/processing state of the application. Will never terminate on its own (needs to be killed).
     */
    public static void body() throws Exception
    {
    	// create all ProcessorThreads: their constructor automatically starts them
        for(int i = 0; i < workers.length; i++)
            workers[i] = new ProcessorThread();
        
        // this will keep running until a termination signal is received from the outside
        while(true)
        {
            Thread.yield();

            for(File sourceDirectory : sourceDirectories)
        	getAllFiles(sourceDirectory, allInputFiles);

            for(int counter = 0; counter < allInputFiles.size(); counter++)
            {
        	if(! (allInputFiles.get(counter).getPath().endsWith(".input") || allInputFiles.get(counter).getPath().endsWith(".in_progress")))
        	{
        	    File dotInput = new File(allInputFiles.get(counter).getPath() + ".input");

        	    int safetyValve = 0;
        			
        	    // sometimes there are initial issues getting access to a file when it is first copied
        	    // these usually resolve within 10ms or so, but if they don't resolve after 100ms the program will move on
        	    while(! allInputFiles.get(counter).renameTo(dotInput) && safetyValve < 10)
        	    {
        		safetyValve++;
        		try
        		{
        		    Thread.sleep(10);
        		}
        		catch(InterruptedException e) {logger.warn(e,e);}
        	    }

        	    queue.add(new PrintTask(dotInput));
        	}
            }

            allInputFiles.clear();
            System.gc();
        }
    }
    
    /**
     * Deletes every file and folder within the target directory.
     */
    public static void fullClean(File f) throws Exception
    {
        if(f.isFile())
            f.delete();
        else
        {
            File[] fList = f.listFiles();
            
            if(! (fList == null))
            {
                for(File g : fList)
                {
                    fullClean(g);
                }
            }            
            f.delete();
        }
    }
    
    /**
     * Adds every file (even those buried in subdirectories) that is in the directory f to the ArrayList alf.
     */
    public static void getAllFiles(File f, ArrayList<File> listOfFiles) throws Exception
    {
        if(f.isFile())
            listOfFiles.add(f);
        else
        {
            File[] fList = f.listFiles();
            
            if(! (fList == null))
            {
                for(File file : fList) 
                {
                    if(file.isFile()) 
                    {
                        listOfFiles.add(file);
                    } 
                    else if(file.isDirectory()) 
                    {
                        getAllFiles(new File(file.getAbsolutePath()), listOfFiles);
                    }
                }
            }
        }
    }    
    
    /**
     * Performs basic clean-up functions such as closing the log, killing all active threads, and updating the config file.
     */
    public static void terminate(boolean full) 
    {
    	// ends all ProcessorThreads
        ProcessorThread.killAll();
        
        PrintWriter pw = null;
        
        // updates config file if any changes have been made
        try 
        {
            pw = new PrintWriter(CONFIG);
            pw.println("CONFIG");
            pw.println("Source Directories: " + sourceDirectories.get(0));
            
            for(int i = 1; i < sourceDirectories.size(); i++)
                pw.println("                    " + sourceDirectories.get(i));
            
            pw.println("Target Directory: " + targetDirectory);
            pw.println("Log Directory: " + logDirectory);
            pw.println("Exception Log Directory: " + exceptionLogDirectory);
            pw.println("Number of Threads: " + numberOfThreads);
            pw.println("Session ID Server: " + sessionIDServer);
            pw.println("Session ID Port: " + sessionIDPort);
            
            pw.close();
        } 
        catch (FileNotFoundException e) 
        {
            System.err.println("Error re-initializing config file.");
            logger.warn(e,e);
        }
        finally
        {
            if(pw != null)
                pw.close();
        }
        
        // a full terminate is only called when the program was run with the "start" commandline argument
        // it finishes dealing with the log files that were created
        if(full)
        {
            logger.warn("Program terminated at " + new Date());
            logger.debug("Processed " + filesProcessed + " files.");
            logger.debug("Active run-time " + totalRunTime + " milliseconds.");
            logger.debug("Active processing time " + processTime + " milliseconds.");
            logger.debug("Processing time per file: " + (double)processTime / filesProcessed + " milliseconds.");   
        }
    }
    
    /**
     * To be filled with the appropriate business logic.
     */
    public static File processLogic(File toBeProcessed) throws Exception
    {
    	switch(thisInstanceIs)
    	{
    	    case SABRE:
    		ReadSabre.test(toBeProcessed, SessionUtils.getSessionId(sessionIDServer, sessionIDPort, null));
    		return toBeProcessed;
    	    case APOLLO:
    		return toBeProcessed;
    	    case AMADEUS:
    		return toBeProcessed;
    	    default:
    		return toBeProcessed;
    	}
    }
    
    /**
     * Prints the contents of the File parameter to an equivalent file in the target directory.
     * Also records this copy in the config-specified log file.
     */
    public static void process(File f) throws Exception
    {
    	long startProcessTime = System.currentTimeMillis();
    	
        File source = new File(f.getPath().substring(0, f.getPath().indexOf(".input")) + ".in_progress");
        f.renameTo(source);
        f = null;
        
        File target = new File(targetDirectory + "/" + source.getName().substring(0, source.getName().lastIndexOf('.')) + ".processed");

        if(target.exists())
        {
            source.delete();
            return;
        }
        
        logger.debug("Starting to process file " + source.getPath() + " at " + BASE_FORMAT.format(startProcessTime));
        
        boolean logicSuccessful = true;
        
        try
        {
            source = processLogic(source);
        }
        catch(Exception e)
        {
            logicSuccessful = false;
            logger.warn(e,e);
        }
        
        int copyTries = 0;
        FileChannel inputChannel = null;
        FileChannel outputChannel = null;
        FileInputStream fis = null;
        FileOutputStream fos = null;
        
        while(true)
        {
            try
            {
            	if(!logicSuccessful)
            	    throw new IOException("There was an error with the processLogic method: check the parsing classes.");
            	
            	fis = new FileInputStream(source);
            	fos = new FileOutputStream(target);
            	
            	inputChannel = fis.getChannel();
            	outputChannel = fos.getChannel();
            	
            	long bytesTransferred = 1;
            	
            	if(source.length() > 0)
            	    bytesTransferred = 0;
            	
            	// make sure that an empty file isn't copied by accident (this happened during development occasionally)
            	while(bytesTransferred == 0)
            	    bytesTransferred = outputChannel.transferFrom(inputChannel, 0, inputChannel.size());

                target.setLastModified(System.currentTimeMillis());
                
                long finishProcessTime = System.currentTimeMillis();
                long thisProcessTime = finishProcessTime - startProcessTime;
                processTime = processTime + thisProcessTime;
                System.out.println("Finished processing file " + source.getPath() + " at " + BASE_FORMAT.format(finishProcessTime) + ". Process time: " + thisProcessTime);
                logger.debug("Finished processing file " + source.getPath() + " at " + BASE_FORMAT.format(finishProcessTime) + ". Process time: " + thisProcessTime);
                filesProcessed++;
                
                return;
            }
            catch(IOException e)
            {
                // if the file isn't immediately accessible, wait for a short time and try again
                // eventually this thread will acquire the lock on it, and the program can proceed
                
                // I'm not entirely sure what necessitates this: it might be that the file is in use by the system when it's being
                // copied into the source directory, and so one quick execution of this catch block suffices
                
                // if after several tries the file still can't be copied, move on and print an error message to the log
                
                // NOTE: 06JUN16 --> testing suggests that the Thread.sleep is no longer necessary, for an unknown reason 
                // I'm leaving the code as is because it does provide a useful few chances to catch any errors
                
                copyTries++;
                
                if(copyTries <= 5)
                {
                    try
                    {
                        Thread.sleep(10);
                    }
                    catch(InterruptedException ex) {}
                }
                else
                {
                    logger.debug("Processing of file " + source.getPath() + " failed at " + new Date());
                    source.renameTo(new File(source.getPath().substring(0, source.getPath().indexOf(".in_progress")) + ".failed"));
                    logger.warn(e,e);
                    return;
                }
            }
            finally
            {
            	if(inputChannel != null)
            	    inputChannel.close();
            	
            	if(outputChannel != null)
            	    outputChannel.close();
            	
            	if(fis != null)
            	    fis.close();
            	
            	if(fos != null)
            	    fos.close();
            	
            	// if processing was successful, delete the file from the source directory
            	if(source.getPath().indexOf(".failed") == -1)
            	    source.delete();
            }
        }
    }
    
    /**
     * Reads the source directories, the target directory etc. from the specified config file.
     * Returns true if the config file is properly formatted, and false otherwise.
     */
    public static boolean initialize(String[] args)
    {
    	if(args[1].equalsIgnoreCase("SABRE"))
    	    thisInstanceIs = ProcessorType.SABRE;
    	else if(args[1].equalsIgnoreCase("APOLLO"))
    	    thisInstanceIs = ProcessorType.APOLLO;
    	else if(args[1].equalsIgnoreCase("AMADEUS"))
    	    thisInstanceIs = ProcessorType.AMADEUS;
    	else if(args[1].equalsIgnoreCase("ALL"))
    	    thisInstanceIs = ProcessorType.ALL;
    	else
    	    thisInstanceIs = ProcessorType.NOT_RECOGNIZED;
    	
    	// sets up the config file based on what the ProcessorType is 
    	switch(thisInstanceIs)
    	{
    	    case SABRE:
    		CONFIG = new File(System.getProperty("user.dir") + "/Config/SABREconfig.txt");
    		break;
    	    case APOLLO:
    		CONFIG = new File(System.getProperty("user.dir") + "/Config/APOLLOconfig.txt");
    		break;
    	    case AMADEUS:
    		CONFIG = new File(System.getProperty("user.dir") + "/Config/AMADEUSconfig.txt");
    		break;
    	    case ALL:
    		CONFIG = new File(System.getProperty("user.dir") + "/Config/SABREconfig.txt");
    		break;
    	    default:
    		System.err.println("Processor type (e.g. SABRE/APOLLO/AMADEUS) not recognized.");
    		break;
    	}
    	
        BufferedReader br;
        
        try
        {
            br = new BufferedReader(new FileReader(CONFIG));
        }
        catch(FileNotFoundException e)
        {
            System.err.println("Config file missing.");
            return false;
        }
        
        try
        {
            if(!br.readLine().equals("CONFIG"))
            {
               System.err.println("Config file missing header.");
               br.close();
               return false;
            }
            
            String nextLine = br.readLine().trim();
            sourceDirectories.add(new File(nextLine.substring(20)));
            
            nextLine = br.readLine().trim();
            
            while(! nextLine.startsWith("Target Directory: "))
            {
                sourceDirectories.add(new File(nextLine));
                nextLine = br.readLine().trim();
            }
            
            targetDirectory = new File(nextLine.substring(18));
            nextLine = null;
            
            logDirectory = new File(br.readLine().trim().substring(15));
            exceptionLogDirectory = new File(br.readLine().trim().substring(25));
            numberOfThreads = Integer.parseInt(br.readLine().trim().substring(19));
            sessionIDServer = br.readLine().trim().substring(19);
            sessionIDPort = br.readLine().trim().substring(17);
            
            workers = new ProcessorThread[numberOfThreads];
            
            br.close();
        }
        catch(Exception e)
        {
            System.err.println("Config file not properly formatted.");
            return false;
        }
        
        return true;
    }
}