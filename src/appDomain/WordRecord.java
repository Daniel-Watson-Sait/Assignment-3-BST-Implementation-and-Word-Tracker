package appDomain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Stores one word with its file and line occurrences.
 */
public class WordRecord implements Comparable<WordRecord>,Serializable{
	private static final long serialVersionUID=1L;
	private final String word;
	private final Map<String,ArrayList<Integer>> locations;

	/**
	 * Creates a record for one word.
	 *
	 * @param word word to store
	 * @throws NullPointerException when word is null
	 * @throws IllegalArgumentException when word is blank
	 */
	public WordRecord(String word){
		if(word==null){
			throw new NullPointerException();
		}
		if(word.trim().isEmpty()){
			throw new IllegalArgumentException();
		}
		this.word=word.trim();
		locations=new LinkedHashMap<String,ArrayList<Integer>>();
	}

	/**
	 * Returns stored word.
	 *
	 * @return stored word
	 */
	public String getWord(){
		return word;
	}

	/**
	 * Adds file and line occurrence.
	 *
	 * @param fileName file containing word
	 * @param lineNo line containing word
	 * @throws NullPointerException when file name is null
	 * @throws IllegalArgumentException when file name is blank or line no. is less than 1
	 */
	public void addOccurrence(String fileName,int lineNo){
		if(fileName==null){
			throw new NullPointerException();
		}
		if(fileName.trim().isEmpty()||lineNo<1){
			throw new IllegalArgumentException();
		}
		String file=fileName.trim();
		if(!locations.containsKey(file)){
			locations.put(file,new ArrayList<Integer>());
		}
		locations.get(file).add(lineNo);
	}

	/**
	 * Returns file and line occurrences.
	 *
	 * @return map of file names and line numbers
	 */
	public Map<String,ArrayList<Integer>> getOccurrences(){
		return locations;
	}

	/**
	 * Returns total no. of occurrences.
	 *
	 * @return total frequency
	 */
	public int getFrequency(){
		int count=0;
		for(ArrayList<Integer> lines:locations.values()){
			count+=lines.size();
		}
		return count;
	}

	/**
	 * Compares words without letter case.
	 *
	 * @param other word record to compare
	 * @return negative, zero or positive based on alphabetical order
	 * @throws NullPointerException when other record is null
	 */
	@Override
	public int compareTo(WordRecord other){
		if(other==null){
			throw new NullPointerException();
		}
		return word.compareToIgnoreCase(other.word);
	}

	/**
	 * Returns stored word.
	 *
	 * @return stored word
	 */
	@Override
	public String toString(){
		return word;
	}
}
