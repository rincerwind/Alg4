import java.util.*;
import SuffixTreePackage.*;

/**
 * Main class - for accessing suffix tree applications
 * David Manlove, Jan 03.  Modified by David Manlove, Jan 07 and Jan 09.
 */

public class Main {

	/**
	 * The main method.
	 * @param args the arguments
	 */
	public static void main(String args[]) {

		boolean debug = true;
		SuffixTree t;
		SuffixTreeAppl a;
		
		String fileName1;
		String fileName2;
		String searchStr;
		
		byte[] sWordBytes;
		byte[] fileContents;
		
		FileInput f;
		Scanner standardInput = new Scanner(System.in);
		
		do {   
			// display prompt for user
			System.out.println();
			System.out.print("Enter the number of the task or type 'q' to quit: ");

			// read in a line from standard input
			String line = standardInput.nextLine();
			System.out.println();

			try {
				// try to extract an integer from line if possible
				int numTask = Integer.parseInt(line);

				switch (numTask) {
					case 1:
						System.out.print("Enter the name of the text file: ");
						fileName1 = standardInput.nextLine();
						
						System.out.print("Enter the string to search for: ");
						searchStr = standardInput.nextLine();
						sWordBytes = searchStr.getBytes();
						
						f = new FileInput(fileName1);
						fileContents = f.readFile();
						
						t = new SuffixTree(fileContents);
						a = new SuffixTreeAppl(t);
						Task1Info t1Result = a.searchSuffixTree(sWordBytes);
						
						if( t1Result.getPos() < 0 )
							System.out.printf("Search string \"%s\" not found in %s\n", 
									searchStr, fileName1);
						else
							System.out.printf("Search string \"%s\" occurs at position %d of %s\n",
									searchStr, t1Result.getPos(), fileName1);
						
						if( debug ){
							System.out.println();
							System.out.println( fileName1 );
							System.out.println( searchStr );
							printByteArray(sWordBytes, 0, sWordBytes.length);
							System.out.println();
						}
						break;
						
					case 2:
						System.out.print("Enter the name of the text file: ");
						fileName1 = standardInput.nextLine();
						
						System.out.print("Enter the string to search for: ");
						searchStr = standardInput.nextLine();
						sWordBytes = searchStr.getBytes();
						
						f = new FileInput(fileName1);
						fileContents = f.readFile();
						
						t = new SuffixTree(fileContents);
						a = new SuffixTreeAppl(t);
						Task2Info t2Result = a.allOccurrences(sWordBytes);
						LinkedList<Integer> l = t2Result.getPositions();
						
						if( l.size() < 1 )
							System.out.printf("Search string \"%s\" does not occur in %s\n",
									searchStr, fileName1);
						else{
							System.out.printf("The string \"%s\" occurs in %s at positions:\n",
									searchStr, fileName1);
							
							Iterator<Integer> i = l.iterator();
							while( i.hasNext() )
								System.out.println( (int)i.next() );
							System.out.printf("The total number of occurrances is %d\n", l.size());
						}
						
						if( debug ){
							System.out.println( fileName1 );
							System.out.println( searchStr );
						}
						break;
						
					case 3:
						System.out.print("Enter the name of the text file: ");
						fileName1 = standardInput.nextLine();
						
						f = new FileInput(fileName1);
						fileContents = f.readFile();
						
						t = new SuffixTree(fileContents);
						a = new SuffixTreeAppl(t);
						Task3Info t3Result = a.traverseForLrs();
						byte[] text = t.getString();
						
						if( t3Result.getLen() < 1 )
							System.out.printf("No LRS was found in %s\n", fileName1);
						else{
							System.out.printf("An LRS in %s is \"", fileName1);
							printByteArray(text, t3Result.getPos1(), t3Result.getLen());
							System.out.println("\"");
							System.out.printf("Its length is %d\n", t3Result.getLen());
							System.out.printf("Starting position of one occurrence is %d\n", t3Result.getPos1());
							System.out.printf("Starting position of another occurrence is %d\n", t3Result.getPos2());
						}
						
						if( debug ){
							System.out.println( fileName1 );
						}
						break;
					case 4: System.out.println("You entered '4'"); break;
					/* replace the above four lines with code to display relevant
					 * output for each task    
	                 *
					 * in the case of Tasks 1, 2 and 3, get the name of a text file
					 * from standard input; in the case of Task 4, get the names of
					 * two text files from standard input
	
					 * then, in all cases, read the data from the text file(s) using 
					 * the FileInput class and build the relevant suffix tree
	
					 * in the case of Tasks 1 and 2, get a string from standard input
					 * and convert the string to bytes, with the relevant information
					 * stored in the array of bytes from positions 0 onwards
	
					 * then call the relevant method from above to process the
					 * information, and display the output using System.out.print
					 * and System.out.println */
	
					default: throw new NumberFormatException();
				}
			}
			catch (NumberFormatException e) {
				if (line.length()==0 || line.charAt(0)!='q')
					System.out.println("You must enter either '1', '2', '3', '4' or 'q'.");
				else
					break;
			}
		} while (true);
		standardInput.close();
	}
	
	static void printByteArray(byte[] x, int startPos, int x_len){
		for(int i = startPos; i < startPos + x_len; i++)
			System.out.print( (char)x[i] );
	}
}