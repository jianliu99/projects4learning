import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class CsvUtils {

    private static final char DEFAULT_SEPARATOR = ',';
    private static final char DEFAULT_QUOTE = '"';

    public static void write2CsvFile(String[][] lines, String fname)
    {
    	File file = new File(fname);
		String content = "";
		for (int i = 0; i < lines.length; i++) {
		for (int j = 0; j < lines[0].length; j++) {
			if (lines[i][j].indexOf(',') == -1) {
				content += lines[i][j];
			}
			else {
				content += "\"\"";
				content += lines[i][j];
				content += "\"\""; 
			}
				
			
			content += DEFAULT_SEPARATOR;
			};
			content += "\n";
		}
		
		try (FileOutputStream fop = new FileOutputStream(file)) {

			// if file doesn't exists, then create it
			//if (!file.exists()) {
				file.createNewFile();
			//}

			// get the content in bytes
			byte[] contentInBytes = content.getBytes();

			fop.write(contentInBytes);
			fop.flush();
			fop.close();

			System.out.println("Done");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
    
    public static String[][] convert2Array(List<String[]> arrays)
    {
    	// create a linked list
    	//List<String[]> arrays = new LinkedList<String[]>();

    	// add some trivial test data (note: arrays with different lengths)
    	//arrays.add(new String[]{"a", "b", "c"});
    	//arrays.add(new String[]{"d", "e", "f", "g"});

    	// convert the datastructure to a 2D array
    	String[][] matrix = arrays.toArray(new String[0][]);
    
    	System.out.println("the # of rows: "+matrix.length);
    	if (matrix.length > 0)
    		System.out.println("the # of columns: "+matrix[0].length);
    	// test output of the 2D array
    	//for (String[] s:matrix)
    	//  System.out.println(Arrays.toString(s));
    	/*
    	for (int i = 0; i < matrix.length; i++) {
    		String [] line = matrix[i];
    		//System.out.println("new line");
    		for (int j = 0; j < line.length; j++) {
    			System.out.print(line[j]+ " ");
    		}
    		System.out.println("");
    	}
    	*/
    	
    	return matrix;
    }
    
    public static String[][] parseCsvFile(String fname)
    {
    	Scanner scanner = null;
		try {
			scanner = new Scanner(new File(fname));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//List<List<String>> allLines = new ArrayList<List<String>>();
		List<String[]> allLines = new ArrayList<String[]>();
        
		while (scanner.hasNext()) {
            List<String> line = parseLine(scanner.nextLine());
           // for (int i = 0; i < line.size(); i++)
            //	System.out.println(line.get(i));
            
            allLines.add(line.toArray(new String[line.size()]));
            //System.out.println("Country [id= " + line.get(0) + ", code= " + line.get(1) + " , name=" + line.get(2) + "]");
        }
        
        scanner.close();
        
        return(convert2Array(allLines));
    }
    public static void main(String[] args) throws Exception {

        String OrigCsvFile = "c:\\Users\\Jian\\Desktop\\Book1.csv";
        String TgtCsvFile = "c:\\Users\\Jian\\Desktop\\Book2.csv";
       String[][] data = parseCsvFile(OrigCsvFile);
       write2CsvFile(data, TgtCsvFile);
    }
    
    
    public static List<String> parseLine(String cvsLine) {
        return parseLine(cvsLine, DEFAULT_SEPARATOR, DEFAULT_QUOTE);
    }

    public static List<String> parseLine(String cvsLine, char separators) {
        return parseLine(cvsLine, separators, DEFAULT_QUOTE);
    }

    public static List<String> parseLine(String cvsLine, char separators, char customQuote) {

        List<String> result = new ArrayList<>();

        //if empty, return!
        if (cvsLine == null && cvsLine.isEmpty()) {
            return result;
        }

        if (customQuote == ' ') {
            customQuote = DEFAULT_QUOTE;
        }

        if (separators == ' ') {
            separators = DEFAULT_SEPARATOR;
        }

        StringBuffer curVal = new StringBuffer();
        boolean inQuotes = false;
        boolean startCollectChar = false;
        boolean doubleQuotesInColumn = false;

        char[] chars = cvsLine.toCharArray();

        for (char ch : chars) {

            if (inQuotes) {
                startCollectChar = true;
                if (ch == customQuote) {
                    inQuotes = false;
                    doubleQuotesInColumn = false;
                } else {

                    //Fixed : allow "" in custom quote enclosed
                    if (ch == '\"') {
                        if (!doubleQuotesInColumn) {
                            curVal.append(ch);
                            doubleQuotesInColumn = true;
                        }
                    } else {
                        curVal.append(ch);
                    }

                }
            } else {
                if (ch == customQuote) {

                    inQuotes = true;

                    //Fixed : allow "" in empty quote enclosed
                    if (chars[0] != '"' && customQuote == '\"') {
                        curVal.append('"');
                    }

                    //double quotes in column will hit this!
                    if (startCollectChar) {
                        curVal.append('"');
                    }

                } else if (ch == separators) {

                    result.add(curVal.toString());

                    curVal = new StringBuffer();
                    startCollectChar = false;

                } else if (ch == '\r') {
                    //ignore LF characters
                    continue;
                } else if (ch == '\n') {
                    //the end, break!
                    break;
                } else {
                    curVal.append(ch);
                }
            }

        }

        result.add(curVal.toString());

        return result;
    }

}

