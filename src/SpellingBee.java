import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Spelling Bee
 *
 * This program accepts an input of letters. It prints to an output file
 * all English words that can be generated from those letters.
 *
 * For example: if the user inputs the letters "doggo" the program will generate:
 * do
 * dog
 * doggo
 * go
 * god
 * gog
 * gogo
 * goo
 * good
 *
 * It utilizes recursion to generate the strings, mergesort to sort them, and
 * binary search to find them in a dictionary.
 *
 * @author Zach Blick, [ADD YOUR NAME HERE]
 *
 * Written on March 5, 2023 for CS2 @ Menlo School
 *
 * DO NOT MODIFY MAIN OR ANY OF THE METHOD HEADERS.
 */
public class SpellingBee {

    private String letters;
    private ArrayList<String> words;
    public static final int DICTIONARY_SIZE = 143091;
    public static final String[] DICTIONARY = new String[DICTIONARY_SIZE];

    public SpellingBee(String letters) {
        this.letters = letters;
        words = new ArrayList<String>();
    }

    // TODO: generate all possible substrings and permutations of the letters.
    //  Store them all in the ArrayList words. Do this by calling ANOTHER method
    //  that will find the substrings recursively.
    // generate function generates all permutations of the string letters which is read in from the console
    public void generate() {
        // calls the generatePermuations helper function which adds all permutations to the words arraylist
        generatePermutations("", letters);
    }

    // generatePermutations is a recursive helper function that takes in a strings beginning and end then returns all possible combinations of it
    public void generatePermutations(String beginning, String end) {
        // Stats by checking how many characters are left in end
        int n = end.length();
        // adds a new string no matter what because its not a requirement to use all the letters, just every single combination
        words.add(beginning);
        // base case built in as if n == 0 then this wont run
        for (int i = 0; i < n; i++) {
            // recursively calls itself with a new arrangement of ending letters each time, this is how it generates its permutations
            generatePermutations(beginning + end.substring(i, i+1), end.substring(0, i) + end.substring(i + 1));
        }
    }

    // TODO: Apply mergesort to sort all words. Do this by calling ANOTHER method
    //  that will find the substrings recursively.
    // sort method calls a helper method that applies a mergesort algorithm on words
    public void sort() {
        words = mergeSort(words);
    }

    // mergeSort takes in an array then recursively sorts it and merges it
    public ArrayList<String> mergeSort(ArrayList<String> arr) {
        // base case that checks when you have a single or "sorted" mini-array
        if (arr.size() == 1) {
            return arr;
        }
        // calculates mid point of the array
        int mid = arr.size()/2;
        // Creates both an array of stuff on the left of the middle and stuff on the right of the middle
        ArrayList<String> left = new ArrayList<String>();
        ArrayList<String> right = new ArrayList<String>();
        for (int i = 0; i < arr.size(); i++) {
            // adds to left if < mid otherwise adds to right arraylist
            if (i < mid) {
                left.add(arr.get(i));
            } else {
                right.add(arr.get(i));
            }
        }
        // mergeSorts both smaller arrays until they get to size one
        left = mergeSort(left);
        right = mergeSort(right);
        // Then everything is merged back together to sort the arraylist
        return merge(left, right);
    }

    // helper function merge, merges 2 arraylists together in order
    public ArrayList<String> merge (ArrayList<String> arrayLeft, ArrayList<String> arrayRight) {
        // creates a new arraylist that will serve as the merged version
        ArrayList<String> merged = new ArrayList<String>();
        // creates separate indicies of both arrays to keep track of where you are in each
        int indexLeft = 0, indexRight = 0;
        // loops through both while there are still indicies available
        while (indexLeft < arrayLeft.size() && indexRight < arrayRight.size()) {
            // compares the values of the strings to determine which array to increment
            if (arrayLeft.get(indexLeft).compareTo(arrayRight.get(indexRight)) < 0) {
                merged.add(arrayLeft.get(indexLeft++));
            } else {
                merged.add(arrayRight.get(indexRight++));
            }
        }
        // case where you have run out of elements in right array so you add rest of left array to the merged
        while (indexLeft < arrayLeft.size()) {
            merged.add(arrayLeft.get(indexLeft++));
        }
        // case where you have run out of elements in left array so you add rest of right array to the merged
        while (indexRight < arrayRight.size()) {
            merged.add(arrayRight.get(indexRight++));
        }
        // returns the final sorted and merged arraylist
        return merged;
    }

    // Removes duplicates from the sorted list.
    public void removeDuplicates() {
        int i = 0;
        while (i < words.size() - 1) {
            String word = words.get(i);
            if (word.equals(words.get(i + 1)))
                words.remove(i + 1);
            else
                i++;
        }
    }

    // TODO: For each word in words, use binary search to see if it is in the dictionary.
    //  If it is not in the dictionary, remove it from words.
    // checkWords is a method that verifies each word in words to make sure it is in the dictionary
    public void checkWords() {
        // loops through every word in words
        for (int i = 0; i < words.size(); i++) {
            // checks if its in the dictionary
            if (!searchForWord(words.get(i))) {
                // if not, it removes it
                words.remove(i--);
            }
        }
    }

    // helper function searchForWord applies a binary search algorithm too help find the word in the dictionary
    public boolean searchForWord(String word) {
        // utilizes a start and end indicies to know where it is searching in the larger array
        int start = 0, end = DICTIONARY_SIZE - 1, mid;
        // will stop once you have reached a single index, in other words, start == end
        while (start <= end) {
            // calculates a midpoint dependant on where you are in the array
            mid = start + (end-start)/2;
            // checks if the thing where you are at currently is a valid string
            if (DICTIONARY[mid].equals(word)) {
                // returns true if it is
                return true;
            } else if (DICTIONARY[mid].compareTo(word) < 0) {
                // otherwise it compares the word to know which part of the array to search through next
                start = mid + 1;
            } else {
                // otherwise it compares the word to know which part of the array to search through next
                end = mid - 1;
            }
        }
        // returns false when no instance of the string is found in the dictionary
        return false;
    }

    // Prints all valid words to wordList.txt
    public void printWords() throws IOException {
        File wordFile = new File("Resources/wordList.txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(wordFile, false));
        for (String word : words) {
            writer.append(word);
            writer.newLine();
        }
        writer.close();
    }

    public ArrayList<String> getWords() {
        return words;
    }

    public void setWords(ArrayList<String> words) {
        this.words = words;
    }

    public SpellingBee getBee() {
        return this;
    }

    public static void loadDictionary() {
        Scanner s;
        File dictionaryFile = new File("Resources/dictionary.txt");
        try {
            s = new Scanner(dictionaryFile);
        } catch (FileNotFoundException e) {
            System.out.println("Could not open dictionary file.");
            return;
        }
        int i = 0;
        while(s.hasNextLine()) {
            DICTIONARY[i++] = s.nextLine();
        }
    }

    public static void main(String[] args) {

        // Prompt for letters until given only letters.
        Scanner s = new Scanner(System.in);
        String letters;
        do {
            System.out.print("Enter your letters: ");
            letters = s.nextLine();
        }
        while (!letters.matches("[a-zA-Z]+"));

        // Load the dictionary
        SpellingBee.loadDictionary();

        // Generate and prabcint all valid words from those letters.
        SpellingBee sb = new SpellingBee(letters);
        sb.generate();
        sb.sort();
        sb.removeDuplicates();
        sb.checkWords();
        try {
            sb.printWords();
        } catch (IOException e) {
            System.out.println("Could not write to output file.");
        }
        s.close();
    }
}
