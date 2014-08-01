package com.scrabble;

import java.io.*;
import java.util.*;

public class ScrabbleGame {

	private static String constraintLetter;

	public static String lcs(String a, String b) {

		int[][] lengths = new int[a.length() + 1][b.length() + 1];

		for (int i = 0; i < a.length(); i++)
			for (int j = 0; j < b.length(); j++)
				if (a.charAt(i) == b.charAt(j))
					lengths[i + 1][j + 1] = lengths[i][j] + 1;
				else
					lengths[i + 1][j + 1] = Math.max(lengths[i + 1][j],
							lengths[i][j + 1]);

		StringBuffer sb = new StringBuffer();

		for (int x = a.length(), y = b.length();

		x != 0 && y != 0;) {

			if (lengths[x][y] == lengths[x - 1][y])

				x--;
			else if (lengths[x][y] == lengths[x][y - 1])

				y--;
			else {
				assert a.charAt(x - 1) == b.charAt(y - 1);

				sb.append(a.charAt(x - 1));

				x--;
				y--;
			}
		}

		return sb.reverse().toString();

	}

	static List<String> readDictionary(String fileName)
			throws FileNotFoundException {
		Scanner sc = new Scanner(new FileReader(fileName));
		List<String> dictionary = new ArrayList<String>();

		while (sc.hasNextLine()) {
			String word = sc.nextLine();
			dictionary.add(word);
		}
		return dictionary;
	}
	
	public  static  boolean  rules(String word, int numLeftSpaces, int numRightSpaces){
		
		boolean result = false;
		if(word.contains(constraintLetter))
			if(word.indexOf(constraintLetter) <= numLeftSpaces )
				if((word.length() - word.indexOf(constraintLetter) -1) <= numRightSpaces)
					result = true;
		return  result;
	}

	static String searchInDictionary(String inputString, List<String> dictionary, int numLeftSpaces, int numRightSpaces) {
		String maxWord = "";
		int score = 0;
		String dictionaryWord, prev = "";
		//System.out.println("inside func:"+numRightSpaces);

		int max = Integer.MIN_VALUE;
		//System.out.println(inputString.length());
		for (int i = 0; i < dictionary.size(); i++) {
			dictionaryWord = dictionary.get(i);

			char letterArray[] = dictionaryWord.toCharArray();
			Arrays.sort(letterArray);
			String newDictionaryWord = new String(letterArray);

			if (inputString.length() == 8 || inputString.length() == 7 ) {
				  String matchedString = lcs(inputString, newDictionaryWord);
                  if (matchedString.length() == newDictionaryWord.length()) {
                	  if(rules(dictionaryWord,numLeftSpaces,numRightSpaces))
                		  score = calculateScore(matchedString);
                      
                  }
			} else {

				String matchedString = lcs(inputString, newDictionaryWord);
				if (matchedString.length() == newDictionaryWord.length()|| matchedString.length() == newDictionaryWord.length() - 1) {
					if(rules(dictionaryWord,numLeftSpaces,numRightSpaces))
						score = calculateScore(matchedString);
				}

			}

			if (score > max) {
				maxWord = dictionaryWord;
				max = score;
				prev = dictionaryWord;
			}

		}
	
		
		return new String(prev+","+max);
	}

	static int calculateScore(String word) {
		int sum = 0;
		int score[] = { 1, 3, 3, 2, 1, 4, 2, 4, 1, 8, 5, 1, 3, 1, 1, 3, 10, 1,
				1, 1, 1, 4, 4, 8, 4, 10 };

		for (int i = 0; i < word.length(); i++) {
			sum += score[word.charAt(i) - 'A'];
		}
		return sum;
	}

	public static void printResult(String input, String result){
		
		String[] splitString = result.split(",");
		System.out.println(input+" "+splitString[0]+" "+splitString[1] );
		
		
	}
	public static void main(String[] args) throws FileNotFoundException {

		List<String> dictionary = new ArrayList<String>();
		String fileName = "sowpods.txt";
		dictionary = readDictionary(fileName);
		Scanner sc = new Scanner(new FileReader("input.txt"));
		String constraint = new String("");
		String scrambledWord = new String("");
		int numLeftSpaces;
		int numRightSpaces;
		while (sc.hasNextLine()) {
			constraint = new String("");
			String[] input = sc.nextLine().split(" ");
			if( input.length > 1){
				constraint = input[0];
				scrambledWord = input[1];
				numLeftSpaces = getNumLeftSpaces(constraint);
				numRightSpaces = getNumRightSpaces(constraint);
			}
			else{
				scrambledWord = input[0];
				numLeftSpaces = Integer.MAX_VALUE;
				numRightSpaces = Integer.MAX_VALUE;
				
			}
			//System.out.println(numRightSpaces);
			scrambledWord += constraint.replace("_", "");
			constraintLetter = constraint.replace("_", "");
			char letterArray[] = scrambledWord.toUpperCase().toCharArray();
			Arrays.sort(letterArray);
			String inputString = new String(letterArray);
			String result = searchInDictionary(inputString, dictionary,numLeftSpaces,numRightSpaces);
			printResult(scrambledWord,result);
		}
	}

	private static int getNumRightSpaces(String constraint) {
		String constraintLetter = constraint.replace("_", "");
		return constraint.length() - constraint.indexOf(constraintLetter) -1;
	}

	private static int getNumLeftSpaces(String constraint) {
		String constraintLetter = constraint.replace("_", "");
		return constraint.indexOf(constraintLetter);
		 
	}
}
