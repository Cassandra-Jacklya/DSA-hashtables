import java.util.*;

/**
** Author: Cassandra Jacklya
** Purpose: to implement a hash table similar to the built-in hashmap class
** Last modified on: 23rd September 2020
**/

public class DSAHashTable {
	DSAHashEntry[] hashArray;
	int count = 0;
	
	//private inner class 'DSAHashEntry'
	private class DSAHashEntry {
		String key;
		Object value;
		int state;
		
		//default constructor for DSAHashEntry
		public DSAHashEntry() {
			this.key = "";
			this.value = null;
			this.state = 0;	//0 = never used, 1 = used, -1 = formerly-used
		}
		
		//alternate constructor (for insertion)
		public DSAHashEntry(String inKey, Object inValue) {
			key = inKey;
			value = inValue;
			state = 1;
		}
		
		//returns the value of the index in the hash table
		public Object getValue() {
			return this.value;
		}
		
		//returns the key of the index in the hash table
		public String getKey() {
			return this.key;
		}
		
		//returns either 0, 1 or -1
		public int getState() {
			return this.state;
		}
		
		//sets the state to -1 to indicate it is formerly-used
		public void clear() {
			this.key = "";
			this.value = null;
			this.state = -1;
		}
	} 
	
	//------------------DSAHashTable methods---------------------------------
	//constructor method for DSAHashTable
	public DSAHashTable(int size) {
		int actualSize = nextPrime(size);
		hashArray = new DSAHashEntry[actualSize];
		for (int i = 0; i < actualSize; i++) {
			hashArray[i] = new DSAHashEntry();
		}
	}

	//returns the number of values in the hash table
	public int getCount() {
		return this.count;
	}
	
	//this method inserts the key and value into the hash table
	public void put(String inKey, Object inValue) {
		int hashIndex, h1, h2;
		hashIndex = hash(inKey);
		h2 = stepHash(inKey);	//second hash
		try {
			if (isDuplicate(inKey)) {
				throw new IllegalArgumentException("Key '" + inKey + "' already exists. Cannot be added to hash table");
			}
			//finds an empty space in the hash table
			while (hashArray[hashIndex].getState() == 1) {
				
				/**********************************************
				** https://www.youtube.com/watch?v=xVJ4Bu5wwVU
				** Concept obtained from Mr Abdul Bari
				***********************************************/
				hashIndex = hashIndex + h2;
				hashIndex = hashIndex % hashArray.length;
			}
			hashArray[hashIndex] = new DSAHashEntry(inKey, inValue);
			count++;
			if (getLoadFactor() >= 0.75) {	//checks if the table is 0.75 or > full
				resize(hashArray.length * 2);	//if yes, then dynamically resize the table
			}
			
		}
		catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
		}	
	}
	
	//find the targeted key and returns the equivalent value to the main
	public Object get(String inKey) {
		int h1, h2, hashIndex;
		hashIndex = hash(inKey);
		h2 = stepHash(inKey);
		Object outValue = null;
		boolean found = true;
		try {
			while (!(hashArray[hashIndex].getKey().equals(inKey))) {	
				if (hashArray[hashIndex].getState() == 0) {		//key does not exist
					throw new IllegalArgumentException("'" + inKey + "'" + " cannot be found");
				}
				hashIndex += h2;	//does double hashing if key not found
				hashIndex %= hashArray.length;
			}
			outValue = hashArray[hashIndex].getValue();
			System.out.println("Found! Key: " + inKey + ", Value: " + outValue);
		}
		catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
		}
		return outValue;
	}
	
	//removes the targeted key and its value
	public Object remove(String inKey) {	//actually similar to get method, but this time
		int h2, hashIndex;					//...the DSAHashEntry will be cleared
		Object tempValue = null;
		try {
			hashIndex = hash(inKey);
			h2 = stepHash(inKey);
			while (!(hashArray[hashIndex].getKey()).equals(inKey)) {	//if targeted key does not match the current key visited
				if (hashArray[hashIndex].getState() == 0) {
					throw new NullPointerException("Key " + inKey + " does not exist");
				}
				hashIndex += h2;
				hashIndex %= hashArray.length;
				tempValue = hashArray[hashIndex].getValue();
			}
			System.out.println(hashArray[hashIndex].getKey() + " is removed");
			hashArray[hashIndex].clear();	//clears the data in the respective index
			count--;	//decreases the count value
			
			//checks whether the elements in the table is too little (<=0.25%)
			if (getLoadFactor() <= 0.25) {		
				resize(hashArray.length / 2);	//if yes, then resize to a smaller array
			}
		}
		catch (NullPointerException e) {
			System.out.println(e.getMessage());
		}
		return tempValue;
	}
	
	//calculates how full the table is 
	public double getLoadFactor() {
		double loadFactor = count/(double)hashArray.length;
		return loadFactor;
	}
	
	//toString method to print out to the main in a nice display
	//...example: 1) Key: 12345678, Value: Cassandra
	public String toString() {
		String word = "";
		for (int i = 0; i < hashArray.length; i++) {
			if (hashArray[i].getValue() != null) {
				word = word + i + ") Key: " + hashArray[i].getKey() + ", Value: " + hashArray[i].getValue() + "\n";
			}
		}
		return word;
	}
	
	//this method is for exporting the string to a csv format for writing into file
	public String export() {
		String word = "";
		for (int i = 0; i < hashArray.length; i++) {
			if (hashArray[i].getValue() != null) {
				word = word + hashArray[i].getKey() + "," + hashArray[i].getValue() + "\n";
			}
		}
		return word;
	}
		
	//as the name suggests, it resizes the array to be smaller/bigger
	private void resize(int size) {
		//default of load factor in hashmap is 0.75f
		//...https://www.javatpoint.com/load-factor-in-hashmap
		int newSize = nextPrime(size);
		DSAHashEntry[] oldTable = hashArray;
		hashArray = new DSAHashEntry[nextPrime(newSize)];	//creates a new array
		for (int i = 0; i < hashArray.length; i++) {	//re-initialize the array to default
			hashArray[i] = new DSAHashEntry();
		}
		count = 0;	//resets the count
		for (int i = 0; i <oldTable.length; i++) {
			if (oldTable[i].getState() == 1) {
				put(oldTable[i].getKey(), oldTable[i].getValue());	//places the elements into the new array
			}
		}
	}
	
	//first hash function
	private int hash(String inKey) {
		int hashIndex = 0;
		try {
			if (inKey.equals("")) {		//keys must not be null
				throw new NullPointerException("Key cannot be null");
			}
			else {
				for (int ii = 0; ii < inKey.length(); ii++) {
					hashIndex = (33 * hashIndex) + inKey.charAt(ii);
				}
			}
		}
		catch (NullPointerException e) {
			System.out.println(e.getMessage());
		}
		return (hashIndex % hashArray.length);
	}
	
	//second hash function (double hashing)
	//...calculates the stepsize for the next hash index
	private int stepHash(String inKey) {
		int stepsize = nextPrime(inKey.length());	//length of String is used as it never changes 
		int h2 = stepsize - (inKey.length() % stepsize);	
		return h2;
	}
	
	//aids in finding the next closest prime number 
	private int nextPrime(int inNum) {
		int primeValue;
		if (inNum % 2 == 0) {
			primeValue = inNum-1;	//even numbers are never prime so make it odd
		}
		else {
			primeValue = inNum;
		}
		boolean isPrime = false;
		do {
			isPrime = isPrime(primeValue);
			primeValue = primeValue + 2;	//finds the next candidate
		} while (!isPrime);
		return primeValue;
	}
	
	//checks whether the passed value is a prime number
	private boolean isPrime(int prime) {
		boolean flag = true;
        for(int i = 2; i <= prime/2; ++i)
        {
            // condition for nonprime number
            if(prime % i == 0)
            {
                flag = false;
            }
        }
		return flag;
	}
	
	//ensures no duplicate keys exist in the table
	private boolean isDuplicate(String inKey) {
		boolean duplicate = false;
		for (int i = 0; i < hashArray.length; i++) {
			if (hashArray[i].getKey().equals(inKey)) {
				duplicate = true;
			}
		}
		return duplicate;
	}
	
}
	
	