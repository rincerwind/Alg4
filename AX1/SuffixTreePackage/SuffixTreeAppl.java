package SuffixTreePackage;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Class with methods for carrying out applications of suffix trees
 * David Manlove, Jan 03.  Modified by David Manlove, Jan 07 and Jan 09.
 */

public class SuffixTreeAppl {

	/** The suffix tree */
	private SuffixTree t;
	private boolean debug = false;

	/**
	 * Default constructor.
	 */
	public SuffixTreeAppl () {
		t = null;
	}
	
	/**
	 * Constructor with parameter.
	 * 
	 * @param tree the suffix tree
	 */
	public SuffixTreeAppl (SuffixTree tree) {
		t = tree;
	}
	
	/**
	 * Search the suffix tree t representing string s for a target x.
	 * Stores -1 in Task1Info.pos if x is not a substring of s,
	 * otherwise stores p in Task1Info.pos such that x occurs in s
	 * starting at s[p] (p counts from 0)
	 * - assumes that characters of s and x occupy positions 0 onwards
	 * 
	 * @param x the target string to search for
	 * 
	 * @return a Task1Info object
	 */
	public Task1Info searchSuffixTree(byte[] x) {
		Task1Info t1Info = new Task1Info();
		
		int pos = 0, i, j;
		byte[] text = t.getString();
		
		SuffixTreeNode current, next;
		current = t.getRoot();

		while (true) {
			// search for child of current with left label equal to x[pos]
			next = t.searchList(current.getChild(), x[pos]);

			if (next == null) 
				break;
			
			// try to match s[node.getLeftLabel()+1..node.getRightLabel()] with 
			// segment of x starting at position pos+1
			j = next.getLeftLabel() + 1;
			i = pos + 1;

			while ( i < x.length && j <= next.getRightLabel() ){
				if( x[i] != text[j] )
					break;
				if( debug )
					System.out.println( "" + (char)x[i] + "==" + (char)text[j] );
				i++;
				j++;
			}
			
			if( i >= x.length ){
				// succeeded in matching whole search word, so break;
				if( debug )
					System.out.println("" + j + "," + x.length);
				t1Info.setPos( j - x.length );
				t1Info.setMatchNode(next);
				break;
			}
			if (j > next.getRightLabel()) {
				// succeeded in matching whole segment, so go further down tree
				pos = i;
				current = next;
			}
			else
				break;
		}// end of while-loop
		
		return t1Info; // replace with your code!
	}

	/**
	 * Search suffix tree t representing string s for all occurrences of target x.
	 * Stores in Task2Info.positions a linked list of all such occurrences.
	 * Each occurrence is specified by a starting position index in s
	 * (as in searchSuffixTree above).  The linked list is empty if there
	 * are no occurrences of x in s.
	 * - assumes that characters of s and x occupy positions 0 onwards
	 * 
	 * @param x the target string to search for
	 * 
	 * @return a Task2Info object
	 */
	public Task2Info allOccurrences(byte[] x) {
		Task1Info t1Info = searchSuffixTree(x);
		Task2Info t2Info = new Task2Info();
		Queue<SuffixTreeNode> q = new LinkedList<SuffixTreeNode>();
		SuffixTreeNode current, child, sibling;
		
		q.add( t1Info.getMatchNode().getChild() );
		
		while( !q.isEmpty() ){
			current = q.poll();
			child = current.getChild();
			sibling = current.getSibling();
			
			if( child == null )
				t2Info.addEntry( current.getSuffix() );
			else
				q.add(child);
			
			if( sibling != null )
				q.add(sibling);
		}
		return t2Info; // replace with your code!
	}

	/**
	 * Traverses suffix tree t representing string s and stores ln, p1 and
	 * p2 in Task3Info.len, Task3Info.pos1 and Task3Info.pos2 respectively,
	 * so that s[p1..p1+ln-1] = s[p2..p2+ln-1], with ln maximal;
	 * i.e., finds two embeddings of a longest repeated substring of s
	 * - assumes that characters of s occupy positions 0 onwards
	 * so that p1 and p2 count from 0
	 * 
	 * @return a Task3Info object
	 */
	public Task3Info traverseForLrs () {
		Task3Info t3Info = new Task3Info();
		Queue<SuffixTreeNode> q = new LinkedList<SuffixTreeNode>();
		SuffixTreeNode current, child, sibling;
		
		q.add( t.getRoot() );
		
		while( !q.isEmpty() ){
			current = q.poll();
			child = current.getChild();
			sibling = current.getSibling();
			
			if( child == null ){
				int prefix_len = current.getLeftLabel() - current.getSuffix();
				if( sibling != null && prefix_len > t3Info.getLen() ){
					t3Info.setLen( prefix_len );
					t3Info.setPos1( current.getSuffix() );
					t3Info.setPos2( sibling.getSuffix() );
				}
			}
			else
				q.add(child);
			
			if( sibling != null )
				q.add(sibling);
		}
		return t3Info;
	}

	/**
	 * Traverse generalised suffix tree t representing strings s1 (of length
	 * s1Length), and s2, and store ln, p1 and p2 in Task4Info.len,
	 * Task4Info.pos1 and Task4Info.pos2 respectively, so that
	 * s1[p1..p1+ln-1] = s2[p2..p2+ln-1], with len maximal;
	 * i.e., finds embeddings in s1 and s2 of a longest common substring 
         * of s1 and s2
	 * - assumes that characters of s1 and s2 occupy positions 0 onwards
	 * so that p1 and p2 count from 0
	 * 
	 * @param s1Length the length of s1
	 * 
	 * @return a Task4Info object
	 */
	public Task4Info traverseForLcs (int s1Length) {
		Task4Info t4Result = new Task4Info();
		
		getLcs( t4Result, t.getRoot(), s1Length, 0 );
		return t4Result;
	}// end of traverseForLcs
	
	private void getLcs( Task4Info t4Result, SuffixTreeNode current,  
			int s1Length, int currLen){
		
		SuffixTreeNode child = current.getChild();
		SuffixTreeNode curr_child;
		
		if( current.getLeafNodeString1() && current.getLeafNodeString2() ){
			System.out.println(currLen);
			int prefix_len = current.getLeftLabel() - current.getLeafNodeNumString1() + 1;
			if( currLen > t4Result.getLen() ){
				t4Result.setLen( currLen );
				t4Result.setPos1( current.getLeafNodeNumString1() );
				t4Result.setPos2( current.getLeafNodeNumString2() - (s1Length + 1) );
			}
		}
		
		while( child != null ){
			currLen += child.getRightLabel() - child.getLeftLabel() + 1;
			getLcs( t4Result, child, s1Length, currLen );
			currLen -= child.getRightLabel() - child.getLeftLabel() + 1;
			
			child = child.getSibling();
		}
	}// end of getLcs
}
