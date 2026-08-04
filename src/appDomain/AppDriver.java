package appDomain;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import implementations.BSTree;
import implementations.BSTreeNode;
import utilities.BSTreeADT;

/**
 * Provides the main entry point for the Word Tracker application.
 * The application reads words from a text file, stores each unique word
 * in a binary search tree, records the file name and line number of each
 * occurrence, saves the tree to a serialized repository, and generates
 * a report based on the command-line arguments.
 *
 * @author Habin Park
 */
public class AppDriver {

	/**
	 * The name of the serialized repository file used to store the binary
	 * search tree between program executions.
	 */
	private static final String REPO_FILE = "repository.ser";

	/**
	 * Runs the Word Tracker application.
	 * <p>
	 * The expected command-line format is:
	 * </p>
	 *
	 * <pre>
	 * java -jar WordTracker.jar &lt;input.txt&gt; -pf/-pl/-po [-f&lt;output.txt&gt;]
	 * </pre>
	 *
	 * @param args command-line arguments containing the input file path,
	 *             report flag, and optional output file
	 */
	public static void main(String[] args) {

		if (args.length < 2) {
			System.err.println(
					"Usage: java -jar WordTracker.jar <input.txt> -pf/-pl/-po [-f<output.txt>]");
			return;
		}

		String inputFilePath = args[0];
		String reportFlag = args[1];
		String outputFile = null;

		if (args.length >= 3) {
			String arg2 = args[2];

			if (arg2.startsWith("-f")) {
				outputFile = arg2.substring(2);

				if (outputFile.isEmpty() && args.length >= 4) {
					outputFile = args[3];
				}
			}
		}

		BSTreeADT<WordRecord> tree = loadRepository();

		File inputFile = new File(inputFilePath);

		if (!inputFile.exists()) {
			System.err.println("Error: Input file not found: " + inputFilePath);
			return;
		}

		processFile(inputFile, tree);
		saveRepository(tree);

		try {
			WordReport.generateReport(tree, reportFlag, outputFile);
		} catch (Exception e) {
			System.err.println("Error generating report: " + e.getMessage());
		}
	}

	/**
	 * Loads a previously serialized binary search tree from the repository
	 * file.
	 * <p>
	 * If the repository file does not exist or cannot be read, a new empty
	 * binary search tree is returned.
	 * </p>
	 *
	 * @return the restored binary search tree, or a new empty tree if the
	 *         repository cannot be loaded
	 */
	@SuppressWarnings("unchecked")
	private static BSTreeADT<WordRecord> loadRepository() {
		File repo = new File(REPO_FILE);

		if (repo.exists()) {
			try (ObjectInputStream ois =
					new ObjectInputStream(new FileInputStream(repo))) {

				return (BSTreeADT<WordRecord>) ois.readObject();

			} catch (Exception e) {
				System.out.println(
						"Could not load repository.ser. Creating a new tree");
			}
		}

		return new BSTree<>();
	}

	/**
	 * Serializes and saves the supplied binary search tree to the repository
	 * file.
	 *
	 * @param tree the binary search tree to save
	 */
	private static void saveRepository(BSTreeADT<WordRecord> tree) {
		try (ObjectOutputStream oos =
				new ObjectOutputStream(new FileOutputStream(REPO_FILE))) {

			oos.writeObject(tree);

		} catch (IOException e) {
			System.err.println(
					"Error saving tree to " + REPO_FILE + " " + e.getMessage());
		}
	}

	/**
	 * Reads the supplied text file line by line and processes every word found.
	 * Each unique word is stored in the binary search tree as a
	 * {@link WordRecord}. Existing records are updated with the file name and
	 * line number of each additional occurrence.
	 *
	 * @param file the text file to process
	 * @param tree the binary search tree used to store the word records
	 */
	private static void processFile(File file, BSTreeADT<WordRecord> tree) {
		String fileName = file.getName();

		try (BufferedReader reader =
				new BufferedReader(new FileReader(file))) {

			String line;
			int lineNumber = 1;

			while ((line = reader.readLine()) != null) {
				String[] words = line.split("[^a-zA-Z0-9]+");

				for (String w : words) {
					w = w.trim();

					if (w.isEmpty()) {
						continue;
					}

					WordRecord searchKey = new WordRecord(w);
					BSTreeNode<WordRecord> node = tree.search(searchKey);

					if (node != null) {
						node.getElement().addOccurrence(fileName, lineNumber);
					} else {
						WordRecord newRecord = new WordRecord(w);
						newRecord.addOccurrence(fileName, lineNumber);
						tree.add(newRecord);
					}
				}

				lineNumber++;
			}

		} catch (IOException e) {
			System.err.println(
					"Error reading input file: " + e.getMessage());
		}
	}
}