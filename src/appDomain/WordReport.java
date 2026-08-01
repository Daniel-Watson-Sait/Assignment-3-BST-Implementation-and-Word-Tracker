package appDomain;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Map;

import utilities.BSTreeADT;
import utilities.Iterator;

/**
 * Creates WordTracker reports.
 */
public class WordReport{
	private static final String PRINT_FILES="-pf";
	private static final String PRINT_LINES="-pl";
	private static final String PRINT_OCCURRENCES="-po";

	/**
	 * Builds report and prints to console or file.
	 *
	 * @param tree word tree to report
	 * @param reportFlag report type
	 * @param outputFile output file or null for console
	 * @throws FileNotFoundException when output file cannot be created
	 * @throws IllegalArgumentException when tree or report flag is invalid
	 */
	public static void generateReport(BSTreeADT<WordRecord> tree,String reportFlag,String outputFile)throws FileNotFoundException{
		String report=buildReport(tree,reportFlag);
		if(outputFile==null||outputFile.trim().isEmpty()){
			System.out.print(report);
			return;
		}
		try(PrintWriter writer=new PrintWriter(outputFile.trim())){
			writer.print(report);
		}
	}

	/**
	 * Builds report in alphabetical order.
	 *
	 * @param tree word tree to report
	 * @param reportFlag report type
	 * @return report text
	 * @throws IllegalArgumentException when tree or report flag is invalid
	 */
	public static String buildReport(BSTreeADT<WordRecord> tree,String reportFlag){
		if(tree==null){
			throw new IllegalArgumentException();
		}
		if(!PRINT_FILES.equals(reportFlag)&&!PRINT_LINES.equals(reportFlag)&&!PRINT_OCCURRENCES.equals(reportFlag)){
			throw new IllegalArgumentException();
		}
		StringBuilder report=new StringBuilder();
		report.append("Displaying ").append(reportFlag).append(" format").append(System.lineSeparator());
		Iterator<WordRecord> iterator=tree.inorderIterator();
		while(iterator.hasNext()){
			WordRecord record=iterator.next();
			report.append("Key : ===").append(record.getWord()).append("===");
			if(PRINT_OCCURRENCES.equals(reportFlag)){
				report.append(" number of entries: ").append(record.getFrequency());
			}
			int fileNo=0;
			for(Map.Entry<String,ArrayList<Integer>> entry:record.getOccurrences().entrySet()){
				if(fileNo>0){
					report.append(",");
				}
				report.append(" found in file: ").append(entry.getKey());
				if(PRINT_LINES.equals(reportFlag)||PRINT_OCCURRENCES.equals(reportFlag)){
					report.append(" on lines: ");
					ArrayList<Integer> lines=entry.getValue();
					for(int i=0;i<lines.size();i++){
						if(i>0){
							report.append(",");
						}
						report.append(lines.get(i));
					}
				}
				fileNo++;
			}
			report.append(System.lineSeparator());
		}
		return report.toString();
	}
}
