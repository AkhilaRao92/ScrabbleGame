package scrabble;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ScrabbleGame {

    public static String lcs(String a, String b) {

        int[][] lengths = new int[a.length() + 1][b.length() + 1];

        for (int i = 0; i < a.length(); i++) {
            for (int j = 0; j < b.length(); j++) {
                if (a.charAt(i) == b.charAt(j)) {
                    lengths[i + 1][j + 1] = lengths[i][j] + 1;
                } else {
                    lengths[i + 1][j + 1] = Math.max(lengths[i + 1][j],
                            lengths[i][j + 1]);
                }
            }
        }

        StringBuffer sb = new StringBuffer();

        for (int x = a.length(), y = b.length();
                x != 0 && y != 0;) {

            if (lengths[x][y] == lengths[x - 1][y]) {
                x--;
            } else if (lengths[x][y] == lengths[x][y - 1]) {
                y--;
            } else {
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

    public static boolean rules(String generatedWord, int numLeftSpaces, int numRightSpaces, String constraintLetter, String bconstraintLetter, int betweenSpace) {
        String word = new String(generatedWord);
        int offsetIndex = 0;
        while (word.contains(constraintLetter) && word.length() > constraintLetter.length() + bconstraintLetter.length() + betweenSpace) {
            //System.out.println(word + " " + constraintLetter + " " + betweenSpace + " " + bconstraintLetter);
            if (word.indexOf(constraintLetter) <= numLeftSpaces + offsetIndex) {
                int endLength = bconstraintLetter.length() > 0 ? betweenSpace + 1 : 0;

                if ((word.length() - word.indexOf(constraintLetter) - 1) <= numRightSpaces + endLength) {

                    if (bconstraintLetter.length() > 0) {
                        if ((word.length() - word.indexOf(constraintLetter) - 1) >= betweenSpace + 1) {
                            if (word.charAt(word.indexOf(constraintLetter) + betweenSpace + 1) == bconstraintLetter.charAt(0)) {
                                return true;
                            } else {
                                offsetIndex += word.indexOf(constraintLetter);
                                word = word.substring(word.indexOf(constraintLetter) + 1);
                            }
                        }
                        else {
                            return false;
                        }
                    } else {
                        return true;
                    }
                } else {
                    offsetIndex += word.indexOf(constraintLetter);
                    word = word.substring(word.indexOf(constraintLetter) + 1);
                }
            } else {
                return false;
            }
        }

        return false;
    }

    public static boolean MatchRegExpr(String word, int numLeftSpaces, int numRightSpaces, String constraintLetter, String bconstraintLetter, int betweenSpace) {

        boolean result = false;
        String constraintRegExp = constraintLetter;
        for (int i = 0; i < betweenSpace; i++) {
            constraintRegExp = constraintRegExp + "[a-z]";
        }
        constraintRegExp = constraintRegExp + bconstraintLetter;
        Pattern p = Pattern.compile(constraintRegExp);
        Matcher m = p.matcher(word);
        boolean b = m.matches();

        if (b) {

        }

        return result;
    }

    static String searchInDictionary(String inputString, List<String> dictionary, int numLeftSpaces, int numRightSpaces, String constraintLetter, String bconstraintLetter, int betweenSpace) {
        String maxWord = "";
        int score = 0;
        //System.out.println("inside func:"+numRightSpaces);

        int max = 0;
        //System.out.println(inputString.length());
        for (int i = 0; i < dictionary.size(); i++) {
            String dictionaryWord = dictionary.get(i);

            char letterArray[] = dictionaryWord.toCharArray();
            Arrays.sort(letterArray);
            String sortedDictionaryWord = new String(letterArray);

            int numOfextraLetters = constraintLetter.length() + bconstraintLetter.length();
            if (inputString.length() == 7 + numOfextraLetters) {
                String matchedString = lcs(inputString, sortedDictionaryWord);
                if (matchedString.length() == sortedDictionaryWord.length()) {
                    if (rules(dictionaryWord, numLeftSpaces, numRightSpaces, constraintLetter, bconstraintLetter, betweenSpace)) {
                        score = calculateScore(matchedString);
                    }

                }
            } else {

                String matchedString = lcs(inputString, sortedDictionaryWord);
                if (matchedString.length() == sortedDictionaryWord.length() || matchedString.length() == sortedDictionaryWord.length() - 1) {
                    if (rules(dictionaryWord, numLeftSpaces, numRightSpaces, constraintLetter, bconstraintLetter, betweenSpace)) {
                        score = calculateScore(matchedString);
                    }
                }

            }

            if (score > max) {
                maxWord = dictionaryWord;
                max = score;
            }

        }

        return new String(maxWord + "," + max);
    }

    static int calculateScore(String word) {
        int sum = 0;
        int score[] = {1, 3, 3, 2, 1, 4, 2, 4, 1, 8, 5, 1, 3, 1, 1, 3, 10, 1,
            1, 1, 1, 4, 4, 8, 4, 10};

        for (int i = 0; i < word.length(); i++) {
            sum += score[word.charAt(i) - 'A'];
        }
        return sum;
    }

    public static void printResult(String result) {

        String[] splitString = result.split(",");
        System.out.println(splitString[0] + " " + splitString[1]);

    }

    public static void main(String[] args) throws FileNotFoundException {

        List<String> dictionary = new ArrayList<String>();
        String fileName = "sowpods.txt";
        dictionary = readDictionary(fileName);
        Scanner sc = new Scanner(new FileReader("input.txt"));

        while (sc.hasNextLine()) {
            String constraint = new String("");
            String scrambledWord = new String("");
            int numLeftSpaces;
            int numRightSpaces;
            int betweenSpaces = 0;
            String aconstraintLetter = "";
            String bconstraintLetter = "";

            String[] input = sc.nextLine().split(" ");
            if (input.length > 1) {
                constraint = input[0];
                scrambledWord = input[1];
                String processedString = processConstraintString(constraint);
                String[] splitConstraintString = processedString.split(" ");

                numLeftSpaces = Integer.parseInt(splitConstraintString[0]);
                aconstraintLetter = splitConstraintString[1].toUpperCase();

                if (splitConstraintString.length > 3) {
                    bconstraintLetter = splitConstraintString[3].toUpperCase();
                    betweenSpaces = Integer.parseInt(splitConstraintString[2]);
                    numRightSpaces = Integer.parseInt(splitConstraintString[4]);
                } else {
                    numRightSpaces = Integer.parseInt(splitConstraintString[2]);
                }
            } else {
                scrambledWord = input[0];
                numLeftSpaces = Integer.MAX_VALUE;
                numRightSpaces = Integer.MAX_VALUE;

            }
            //System.out.println(numRightSpaces);
            scrambledWord += constraint.replace("_", "");
            char letterArray[] = scrambledWord.toUpperCase().toCharArray();
            Arrays.sort(letterArray);
            String inputString = new String(letterArray);
            //System.out.println(inputString+" "+aconstraintLetter+" "+bconstraintLetter+" "+numLeftSpaces+ " "+ betweenSpaces + " "+ numRightSpaces);
            String result = searchInDictionary(inputString, dictionary, numLeftSpaces, numRightSpaces, aconstraintLetter, bconstraintLetter, betweenSpaces);
            printResult(result);
        }
    }

    private static String processConstraintString(String constraint) {

        int leftSpace = 0;
        int rightSpace = 0;
        int betweenSpace = 0;
        String aConstraint, bConstraint;
        int i = 0;
        while (i < constraint.length() && constraint.charAt(i) == '_') {
            leftSpace++;
            i++;
        }
        aConstraint = constraint.substring(i, i + 1);
        i++;
        while (i < constraint.length() && constraint.charAt(i) == '_') {
            betweenSpace++;
            i++;
        }
        if (i < constraint.length()) {
            bConstraint = constraint.substring(i, i + 1);
            i++;

            while (i < constraint.length() && constraint.charAt(i) == '_') {
                rightSpace++;
                i++;
            }
        } else {
            rightSpace = betweenSpace;
            String processedConstraint = leftSpace + " " + aConstraint + " " + rightSpace;
            return processedConstraint;
        }

        String processedConstraint = leftSpace + " " + aConstraint + " " + betweenSpace + " " + bConstraint + " " + rightSpace;
        return processedConstraint;
    }

}
