package vishalCode;

import java.util.*;
import java.lang.*;
import java.io.*;

public class Scrabble {

	public static void main(String[] args) throws IOException{
		int[] scores = new int[]{1,3,3,2,1,4,2,4,1,8,5,1,3,1,1,3,10,1,1,1,1,4,4,8,4,10};
		String input = "LCASMER".toUpperCase();
		ArrayList< String > listOfWords = readFile();
		String maxScoreWord = getWordWithMaxScore(listOfWords, scores, input);
		System.out.println(maxScoreWord);
	}
	
	public static ArrayList< String > readFile() throws IOException{
		ArrayList< String > listOfWords = new ArrayList< String >();
        BufferedReader br = new BufferedReader(new FileReader("sowpods.txt"));
        String token = "";   
        while((token = br.readLine()) != null){
			listOfWords.add(token);
		}
		return listOfWords;
	}
	
	public static String getWordWithMaxScore(ArrayList< String > listOfWords, int[] scores, String input){
		String output = "";
		int maxScore = 0;
		
		for(int i = 0; i < listOfWords.size(); i++){
			int score = getScore(listOfWords.get(i), fillMap(input), scores);
			if( score > maxScore){
				maxScore = score;
				output = listOfWords.get(i);
			}
		}
		
		return output;
	}
	
	public static int getScore(String word, HashMap<String, Integer> map, int[] scores){
		int score = 0;
		
		for(int i = 0; i < word.length(); i++){
			if(map.containsKey(""+word.charAt(i))){
				if(map.get(""+word.charAt(i)) == 0){
					return 0;
				}
				else{
					int value = map.get(""+word.charAt(i));
					value--;
					map.put(""+word.charAt(i), value);
					score += scores[word.charAt(i) - 'A'];
				}
			}
			else return 0;
		}
		return score;
	}
	
	public static HashMap<String, Integer> fillMap(String input){
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		
		for(int i = 0; i < input.length(); i++){
			if(map.containsKey("" + input.charAt(i))){
				int value = map.get("" + input.charAt(i));
				value++;
				map.put(""+input.charAt(i), value );
			}
			else{
				map.put(""+input.charAt(i), 1);
			}
		}
		
		return map;
	}
}
