import java.util.*;

/**
** Author: Cassandra Jacklya 
** Purpose: serves as a test harness to test the DSAHashTable class
** Last modified on: 23rd September 2020
**/

public class TestDSAHashTable {
	public static void main(String[] args) {
		int size;
		
		Scanner sc = new Scanner(System.in);
		
		FileIO iFile = new FileIO();
		System.out.println("\nReading csv file:");
		DSAHashTable hash = iFile.readFile("RandomNames7000.csv");
		
		System.out.println("\nWriting into file: ");
		iFile.writeFile("hello.csv",hash.export());
		
		System.out.println("\nSearches for specific keys: ");
		System.out.println("Should be found");
		hash.get("14575130");
		hash.get("14906720");
		hash.get("14644633");
		
		System.out.println("\nShould not be found");
		hash.get("14088485");
		hash.get("14675843");
		
		System.out.println("\nTesting remove function");
		hash.remove("14906720");
		hash.remove("14786148");
		hash.remove("14929292");
		hash.remove("14650254");
		hash.remove("14476919");
		
		System.out.println("\nHash table after removed");
		iFile.writeFile("removal.csv",hash.export());
	}
}