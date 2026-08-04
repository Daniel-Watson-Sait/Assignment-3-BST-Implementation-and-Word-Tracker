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

public class AppDriver {

	private static final String REPO_FILE = "repository.ser";
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		if (args.length < 2) {
			System.err.println("Usage: java -jar WordTracker.jar <input.txt> -pf/-pl/-po [-f<output.txt>]");
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

	@SuppressWarnings("unchecked")
	private static BSTreeADT<WordRecord> loadRepository() {
		File repo = new File(REPO_FILE);
		if (repo.exists()) {
			try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(repo))) {
				return (BSTreeADT<WordRecord>) ois.readObject();
			} catch (Exception e) {
				System.out.println("Could not load repository.ser. Creating a new tree");
			}
		}
		return new BSTree<>();
	}
	
	private static void saveRepository(BSTreeADT<WordRecord> tree) {
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(REPO_FILE))) {
			oos.writeObject(tree);
		} 
		catch (IOException e) {
			System.err.println("Error saving tree to " + REPO_FILE + " " + e.getMessage());
		}
	}
	
	private static void processFile(File file, BSTreeADT<WordRecord> tree) {
		String fileName = file.getName();
		
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
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
					}
					else {
						WordRecord newRecord = new WordRecord(w);
						newRecord.addOccurrence(fileName, lineNumber);
						tree.add(newRecord);
					}
				}
				lineNumber++;
			}
		} catch (IOException e) {
			System.err.println("Error reading input file: " + e.getMessage());
		}
	}
}
