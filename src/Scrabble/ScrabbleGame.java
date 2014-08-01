package Scrabble;

import java.awt.AlphaComposite;
import java.io.*;
import java.util.*;





public class ScrabbleGame {

	
	private static HashMap<String,Integer> dictMap = new HashMap<String,Integer>(); 
	
	public static int sequenceMatching(String a, String b) {
	    int[][] lengths = new int[a.length()+1][b.length()+1];
	 
	 for (int i = 0; i < a.length(); i++)
	        for (int j = 0; j < b.length(); j++)
	            if (a.charAt(i) == b.charAt(j))
	                lengths[i+1][j+1] = lengths[i][j] + 1;
	            else
	                lengths[i+1][j+1] =
	                    Math.max(lengths[i+1][j], lengths[i][j+1]);
	     return lengths[a.length()-1][b.length()-1];

	}
	

	public static int calculateScore(String word){
		
		 String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		 int[] scoresOfLetters = {1,3,3,2,1,4,2,4,1,8,9,1,3,1,1,3,10,1,1,1,1,4,4,8,4,10};
		
		int score = 0;
		for(int i=0; i< word.length(); i++){
			int index = alphabet.indexOf(word.charAt(i));
			score = score + scoresOfLetters[index];
			
		}
		
		return score;
	}
	
	public static void loadSowpods(String inputChar) throws IOException{
		
		
		FileInputStream fis = new FileInputStream("sowpods.txt");
		DataInputStream dis =  new DataInputStream(fis);
		
		while(dis.available() != 0){
			String wordInSowpods = dis.readLine(); 
			int lengthOfMatchedSeq = sequenceMatching(sort(wordInSowpods),inputChar);
			
			if(lengthOfMatchedSeq == wordInSowpods.length()){
				
				int scoreOfWords  = calculateScore(wordInSowpods);
				dictMap.put(wordInSowpods, scoreOfWords);
				System.out.println(wordInSowpods);
			}
			
			
			
		}
		
		
	}
	
	public static String sort(String input){
		char[] sorted = input.toCharArray();
		Arrays.sort(sorted);
		
		return new String(sorted);
	}

	public static String getWordwithMaximumScore(){
		
		int maxScore = 0;
		String maxScoreWord = "";
		Set keySet = dictMap.keySet();
		Iterator<String> iter = keySet.iterator();
		
		while(iter.hasNext()){
			String key = iter.next();
			if(maxScore < dictMap.get(key)){
				maxScore = dictMap.get(key);
				maxScoreWord = key;
			}
			
		}
		
		return maxScoreWord;
	}
	
	public static void main(String[] args) throws IOException {
		
		String lettersOnRack = "AAHINGT";
		String sortedInput = sort(lettersOnRack);
		loadSowpods(sortedInput);
		String word = getWordwithMaximumScore();
		System.out.println(word);
		
		
		
		
		
		
	}

}
