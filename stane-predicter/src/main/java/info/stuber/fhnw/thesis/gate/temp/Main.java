/**
 * 
 */
package info.stuber.fhnw.thesis.gate.temp;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import gate.creole.SerialAnalyserController;
import gate.util.GateException;

/**
 * @author stalin
 *
 */
public class Main {
		
	/**
	 * @param args
	 * @throws MalformedURLException 
	 * @throws GateException 
	 * @throws IOException 
	 */

	public static void main(String[] args) throws GateException, IOException {
		// TODO Auto-generated method stub
		
		long startTime = System.currentTimeMillis();
		
		// load the processing resources and get the application ready, here Serial Analyser Controller
		SerialAnalyserController sac = LoadPR.getProcessingResources();

		// get location of directory containing all files and obtain path of all files
		File loc = new File("src/data/testdata11043");
		File[] allFiles = loc.listFiles();
		System.out.println(allFiles.length);

		// 2D arraylist to contain arraylist for each column
		ArrayList<ArrayList<String>> rows = new ArrayList<ArrayList<String>>();
		// map to check if column is already present
		Map<String, Integer> checkColumn = new HashMap<String, Integer>();

		// call processor and analyse
		DocumentProcessorClass.processDocuments(sac, allFiles, rows, checkColumn, 500);
		
		// call file writer to write the CSV file
		WriteXLS.csvWriter(allFiles.length, rows);
		
		long endTime = System.currentTimeMillis();
		long totalTime = endTime-startTime;
		System.out.println("Total time taken to process all "+ allFiles.length +" docs is: "+totalTime+"ms");
	}
}
