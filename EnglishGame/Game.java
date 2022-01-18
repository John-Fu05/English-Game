package englishGame;

//Tianlin Fu
//This class contains methods that are used in a simple English spelling game
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Game {
	Random rand = new Random();

	public void startGame() { // method that starts the game
		welcomeMessage();// display messages to user
		boolean play = true;
		while (play) {
			System.out.println("Input a sentence.");
			ArrayList<String> sentence = getSentence();
			ArrayList<String> misspell = new ArrayList<>();
//			System.out.println(test(misspell, sentence));
//			System.out.println(test("possibility"));
			misspell.addAll(sentence);
			modify(misspell);
			String result = misspell.toString();
			System.out.print("Here is the sentence with typos: ");
			System.out.println(result.replaceAll(",", "").replace("[", "").replace("]", ""));
			System.out.println("Input the correct spelling of the sentence.");
			checkAnswer(sentence);
			System.out.println("You have entered the correct spelling of the sentence!");
			System.out.println("Would you like to continue playing (yes or no)?");
			Scanner scan = new Scanner(System.in);
			String status = "";
			do {
				String game = scan.nextLine();
				status = game;
				if (status.equalsIgnoreCase("no")) {
					System.out.println("Goodbye!");
					play = false;
				}
				if (!(status.equalsIgnoreCase("no") || status.equalsIgnoreCase("yes"))) {
					System.out.println("Please input yes or no.");
				}
			} while (!(status.equalsIgnoreCase("no") || status.equalsIgnoreCase("yes")));
		}
	}

	private void welcomeMessage() {// prints a series of welcome messages to the user
		System.out.println("Welcome to the English learning game!");
		System.out.println("Please input a sentence, the game will return a new sentence with misspellings.");
		System.out.println("Spot the error and input the correct sentence again.");
		System.out.println("Be aware of punctuation when inputing the correct sentence.");
		System.out.println("Good Luck!");
	}

	private boolean test(ArrayList<String> a, ArrayList<String> b) {// test method for testing code
		for (int i = 0; i < 1000000; i++) {
			a.addAll(b);
			modify(a);
			if (equals(a, b)) {
				return false;
			}
			a.clear();
		}
		return true;
	}

	private boolean test(String word) {// test method to see if individual methods are working
		for (int i = 0; i < 1000000; i++) {
			if (swapChar(word).equals(word)) {
				return false;
			}
		}
		return true;
	}

	private ArrayList<String> getAttempt() {// gets user's new sentence that contains no typos
		ArrayList<String> sentence = new ArrayList<String>();
		Scanner input = new Scanner(System.in);
		Scanner scan = new Scanner(input.nextLine());
		while (scan.hasNext()) {
			sentence.add(scan.next());
		}
		return sentence;
	}

	private ArrayList<String> getSentence() { // gets a sentence from the user that has appropriate sized words
		ArrayList<String> sentence = new ArrayList<String>();
		// asks the user for a sentence until it has long enough words
		do {
			sentence.clear();
			Scanner input = new Scanner(System.in);
			Scanner scan = new Scanner(input.nextLine());
			while (scan.hasNext()) {
				sentence.add(scan.next());
			}
		} while (!checkSentenceSize(sentence));
		return sentence;
	}

	private ArrayList<String> modify(ArrayList<String> sentence) { // modifies the arraylist values and return a
																	// arraylist with typos in words
		ArrayList<Integer> used = new ArrayList<>();
		int count = 0;
		int num = rand.nextInt(bigWords(sentence)) + 1;
		while (count < num) {
			int x = rand.nextInt(sentence.size());
			if (used.contains(x)) {
				x = rand.nextInt(sentence.size());
			} else {
				if (sentence.get(x).length() > 2) {
					sentence.set(x, addTypo(sentence.get(x)));
					count++;
				}
				used.add(x);
			}
		}
		return sentence;
	}

	private String addTypo(String word) { // decides which misspelling method to use
		if (duplicateCharIndex(word) != 0) {
			return removeChar(word);
		} else if (duplicateCharIndex(word) == 0 && addCharIndex(word) != 0) {
			return addChar(word);
		} else {
			return swapChar(word);
		}
	}

	private String swapChar(String word) {// swaps the place of two characters in a word to produce a typo
		int i = rand.nextInt(word.length() - 2) + 1;
		StringBuilder builder = new StringBuilder(word);
		char temp = word.charAt(i);
		builder.setCharAt(i, word.charAt(i + 1));
		builder.setCharAt(i + 1, temp);
		word = builder.toString();
		return word;
	}

	private String removeChar(String word) { // removes a char if there are two identical chars next to each other in
												// the word
		char[] letters = word.toCharArray();
		int length = letters.length;
		char[] copy = new char[length - 1];
		for (int i = 0; i < copy.length; i++) {
			if (i >= duplicateCharIndex(word)) {
				copy[i] = letters[i + 1];
			} else {
				copy[i] = letters[i];
			}
		}
		word = new String(copy);
		return word;
	}

	private String addChar(String word) {// adds a char to the word using a common extra-letter in typos
		char[] letters = word.toCharArray();
		int length = letters.length;
		char[] copy = new char[length + 1];
		int x = addCharIndex(word);
		for (int i = 0; i < copy.length; i++) {
			if (i > x) {
				copy[i] = letters[i - 1];
			} else {
				copy[i] = letters[i];
			}
		}
		word = new String(copy);
		return word;
	}

	private int duplicateCharIndex(String word) {// return index of duplicate char that is next to each other, return 0
													// if not found
		char[] letters = word.toCharArray();
		for (int i = 0; i < letters.length - 1; i++) {
			if (letters[i] == letters[i + 1]) {
				return i;
			}
		}
		return 0;
	}

	private int addCharIndex(String word) {// find the index to which addChar will add a char to
		char[] letters = word.toCharArray();
		for (int i = 1; i < letters.length; i++) {
			// common letters in typos
			if ((letters[i] == 's') || (letters[i] == 'c') || (letters[i] == 'l') || (letters[i] == 'r')) {
				return i;
			}
		}
		return 0;
	}

	private boolean checkSentenceSize(ArrayList<String> sentence) {// checks the size of words in a sentence to make
																	// sure they are suitable for modifying
		for (int i = 0; i < sentence.size(); i++) {
			if (sentence.get(i).length() > 2) {
				return true;
			}
		}
		System.out.println("Your sentence is too short. Try again.");
		return false;
	}

	private void checkAnswer(ArrayList<String> sentence) {// check if the user's input is the correct spelling
		boolean same = false;
		while (!same) {
			ArrayList<String> attempt = getAttempt();
			if (equals(attempt, sentence)) {
				same = true;
				break;
			}
			System.out.println("Your sentence contains typos. Try again.");
		}
	}

	private int bigWords(ArrayList<String> sentence) {// find the amount of words that are suitable for modifying in the
														// sentence
		int count = 0;
		for (int i = 0; i < sentence.size(); i++) {
			if (sentence.get(i).length() > 2) {
				count++;
			}
		}
		return count;
	}

	private boolean equals(ArrayList<String> a, ArrayList<String> b) {// compares two arraylists to see if they are
																		// equal, disregards capitalization
		for (int i = 0; i < a.size(); i++) {
			if (!a.get(i).equalsIgnoreCase(b.get(i))) {
				return false;
			}
		}
		return true;
	}
}
