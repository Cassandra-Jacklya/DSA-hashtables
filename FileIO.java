import java.util.*;
import java.io.*;

/**
** Author: Cassandra Jacklya 
** Purpose: to handle the file operations
** Last modified on: 23rd September 2020
**/

//obtained from my own previous code from PDI unit with a few modifications
//... to fit the practical needs

public class FileIO {
	private DSAHashTable hash;
	
	/********************************************************************
	** Submodule name: readFile
	** Import: fileName (String)
	** Export: hash (DSAHashTable)
	** Purpose: to read in the edges from the file and add into the hash table
	**********************************************************************/
	public DSAHashTable readFile(String fileName) {
	    String word = "";
		hash = new DSAHashTable(countLines(fileName));
	    FileInputStream fileStream = null;
	    InputStreamReader reader;
	    BufferedReader buffer;
	    String line;
	    try {
	        fileStream = new FileInputStream(fileName);
    		reader = new InputStreamReader(fileStream);
			buffer = new BufferedReader(reader);
			
			//reads the first line in the file
			line = buffer.readLine();
			while (line != null) {	
			    
				//if it is a non-empty line, file continues to be read
				if (line.length() == 0) {
		    	   line = buffer.readLine();	//just to make sure that "\n" lines are not considered as EOF
				}
				else {
					word = processLine(line);	
				}
				//continues to read the next non-empty line in the file
		        line = buffer.readLine();
			}
			fileStream.close();
			System.out.println("File '" + fileName + "' has been read and saved to a hash table");
		}
	    catch(IOException e) {
			if (fileStream != null) {
				try {
					fileStream.close();
				}
				catch(IOException e2) { 		     	 				
					System.out.println("Error: File not found or file data is invalid");
				}
			}
	    }
	    return hash;
	}    	
	
	/**************************************************************************
	** Submodule name: processLine
	** Import: line (String)
	** Export: word (String)
	** Purpose: to separate certain parts of the string to add into the hash table
	****************************************************************************/
	
	private String processLine(String line) {
	    String word = "";
	    String[] splitLine;
	    splitLine = line.split(",");	
	    for (int count = 1; count < splitLine.length; count++) {
	        word = word + splitLine[count] + " ";
	    }
		hash.put(splitLine[0], word);		//splitLine[0] represents the key (student ID)
		return word;
	}
	
	/**********************************************************************
	** Submodule name: writeFile
	** Import: fileName (String), writeArray (String)
	** Export: none
	** Purpose: writes the key + value of the hash table into a csv file
	**********************************************************************/
	public void writeFile(String fileName, String writeArray) {
	    String word = "";
	    String[] change = writeArray.split("\n");	//uses the "\n" as a delimiter to separate the different keys in the hash table
	    FileOutputStream fileStream = null;
	    PrintWriter printLine; 
	    try {
			fileStream = new FileOutputStream(fileName);
			printLine = new PrintWriter(fileStream);
			for (int count = 0; count < change.length; count++) {
				word = toFileString(change[count]);	//converts the string to a csv file string format
				printLine.println(word);	//enters the data into the file
			}
			printLine.close();
			System.out.println("Data has been written to file '" + fileName + "'");
	    }
	    catch(IOException e) {
			if (fileStream != null) {
				try {
					fileStream.close();
				} 
				catch (IOException e2) {  }
			}
			System.out.println("Error in writing into file. Sorry!");
	    } 
	}
	
	/***************************************************************************
	** Submodule name: toFileString
	** Import: line (String)
	** Export: newLine (String)
	** Purpose: converts the string of the key + value of hash table to csv file format
	****************************************************************************/
	private String toFileString(String line) {
	    String[] splitLine;
	    String newLine = "";
	    splitLine = line.split(",");	//makes "," as a delimiter as the output from hash table consists of a comma
	    for (int count = 0; count < splitLine.length; count++) {
		    if (count == (splitLine.length-1)) {
				newLine = newLine + splitLine[count];	//checks if it is the last string in the line
		    }
		    else {
				newLine = newLine + splitLine[count] + ",";		//else a comma is added
		    }
		}
		return newLine;
	}
	
	/***************************************************************************
	** Submodule name: countLines
	** Import: fileName (String)
	** Export: lineNum (int)
	** Purpose: to count the number of non-empty lines in the file
	****************************************************************************/
	private static int countLines(String fileName) {
		FileInputStream fileStream = null;
		InputStreamReader reader;
		BufferedReader buffer;
	    int lineNum = 0;
	    String line;
	    try {
			fileStream = new FileInputStream(fileName);
    		reader = new InputStreamReader(fileStream);
			buffer = new BufferedReader(reader);
			line = buffer.readLine();
			while (line != null) {	//if line is not empty
				lineNum = lineNum + 1;	//increase the count
				line = buffer.readLine();	//read the next line
			}
			
			//close the file once finished
			fileStream.close();
		}
	    catch(IOException e) {
	    	System.out.println("Something went wrong: " + e.getMessage());
	    }
	    return lineNum;
	}
}
