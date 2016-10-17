
package info.stuber.fhnw.thesis.gate;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import gate.Annotation;
import gate.AnnotationSet;
import gate.Corpus;
import gate.Document;
import gate.Factory;
import gate.FeatureMap;
import gate.Resource;
import gate.creole.ExecutionException;
import gate.creole.ResourceInstantiationException;
import gate.creole.SerialAnalyserController;

public class DocumentProcessorClass {

	public static void processDocuments(SerialAnalyserController sac, File[] allFiles, 
			ArrayList<ArrayList<String>> rows, Map<String, Integer> checkColumn, int fileClusterLength) 
					throws ResourceInstantiationException, ExecutionException, MalformedURLException{
		
		// temp
		//Map<String, Integer> countLastMod = new HashMap<String, Integer>();
		
		// create a corpus
		Corpus corpus = Factory.newCorpus("Test Data Corpus");
		// arraylist to store document resources
		ArrayList<Document> documentResList=new ArrayList<Document>();

		int i=0;	// variable to name each doc uniquely
		Integer uniqueColKey = 0;	// unique column key for each arraylist of each column
		int rowCount=0;
		
		// processing of docs starts
		long docsStartTime=System.currentTimeMillis();
		int countFiles=0;	// keep counting till max cluster size	
		int totalFilesProcessed=0;
		for(File f : allFiles){	// for each file
			URL sourceUrl = f.toURI().toURL();
			countFiles++; // increment for each file
			totalFilesProcessed++;

			// feature map for creating documents
			FeatureMap params = Factory.newFeatureMap();
			params.put(Document.DOCUMENT_URL_PARAMETER_NAME, sourceUrl);
			params.put(Document.DOCUMENT_ENCODING_PARAMETER_NAME, "UTF-8");

			FeatureMap features = Factory.newFeatureMap();
			features.put("createdOn", new Date());			
			i++;	// increment i to name each doc and corpus uniquely	
			// create document with specified params, features and unique name
			Document doc=(Document) Factory.createResource("gate.corpora.DocumentImpl", params, features, f.getName()+" TestDoc"+i);
			// add document in the corpus
			corpus.add(doc);
			// also maintain a list of these documents
			documentResList.add(doc);
			// if file cluster is full, process the corpus and analyze the documents inside corpus
			if(countFiles==fileClusterLength||totalFilesProcessed==allFiles.length){
				long docsEndTime=System.currentTimeMillis();
				long fileTime = docsEndTime-docsStartTime;
				System.out.println("Time to retrieve "+ fileClusterLength +" files: " + fileTime+"ms");

				// add the corpus to sac
				sac.setCorpus(corpus);
				// execute the sac on the corpus
				System.out.println("Executing SAC on corpus..");
				long sacStartTime = System.currentTimeMillis();
				sac.execute();
				long sacEndTime = System.currentTimeMillis();
				long sacTime = sacEndTime-sacStartTime;
				System.out.println("Time to execute SAC on a corpus of "+fileClusterLength+" files: "+sacTime+"ms");

				System.out.println("Analysing processed docs.."); 
				//int currentDoc=1;
				long docsProcessStartTime = System.currentTimeMillis();
				// start analyzing each data from the corpus				
				
				for(Iterator<Document> corpIterator = corpus.iterator(); corpIterator.hasNext();){
					rowCount++;	// increment the rowCount since doc row added; represents current document number
					// get the document from the corpus
					Document corpDoc = corpIterator.next(); 
					// getting default set of annotations 
					AnnotationSet defaultSet = corpDoc.getAnnotations();
					// get annotations of type COL_NAME and VALUES from default annotation set
					AnnotationSet colTypeAnnotations = defaultSet.get("ColumnName");
					
					// arraylist to check if a column name appears again in this doc
					ArrayList<String> docColumnCheck = new ArrayList<String>();
					boolean present = false;

					// iterate over these collected COL_NAME type annotations and get the value of 'name' feature
					for(Annotation colAnnotation : colTypeAnnotations){
						FeatureMap colFeatureMap=colAnnotation.getFeatures();
						String colNamesString = colFeatureMap.get("name").toString();	// get column name
						colNamesString.trim();	// trim() to remove extra-space at beg of end of string
						colNamesString=colNamesString.toUpperCase();	// convert to upper case to maintain uniqueness
						colNamesString=colNamesString.replaceAll("<", "");
						colNamesString=colNamesString.replaceAll(">", "");
						//int firstUnderscore=colNamesString.indexOf('_');
						//int lastUnderscore=colNamesString.lastIndexOf('_');
						
						// temp starts
						/*if(colNamesString.equals("<F_LASTMOD_DISP>")){
							String docName = corpDoc.getName();
							if(countLastMod.containsKey(docName)){
								countLastMod.put(docName, countLastMod.get(docName)+1);
							} 
							else{
								countLastMod.put(docName, 1);
							}
						}*/
						// temp ends
						
						if(docColumnCheck.contains(colNamesString)){
							present = true;
						}
						else {
							docColumnCheck.add(colNamesString);
							present=false;
						}
						
						String valueString="";	// default value and it indicates that column present is present in this doc but has not value
						//System.out.println();
						//System.out.print(colNamesString+" has a value: ");

						if(colFeatureMap.containsKey("value")){
							valueString = colFeatureMap.get("value").toString();	// get value of column
							valueString.trim();	// trim() to remove extra-space at beg of end of string
							//valueString = valueString.replaceAll(",", "");
							
							// url decoding
							String decodedValueString=valueString;
							try {
								// decode the value
								decodedValueString = URLDecoder.decode(valueString,  "UTF-8");
								// final value
								valueString = decodedValueString;
							} catch (UnsupportedEncodingException e) {
								// TODO Auto-generated catch block							
							} catch (IllegalArgumentException iae){
								System.out.println(valueString);
							}
						}

						if(checkColumn.containsKey(colNamesString)){	// check if column is already present
							int getColKey = checkColumn.get(colNamesString);	// if present then get the index of this column in the arraylist
							// add COLUMN_NOT_PRESENT value for previous files
							if(rows.get(getColKey).size()<rowCount){
								while(rows.get(getColKey).size()!=rowCount){
									rows.get(getColKey).add("");	// this column was not present in previous documents
								}
							}
							//check if this column appears again in this same document
							if(present==false){								
								rows.get(getColKey).add(valueString);	// get arraylist of this column from 2D arraylist and add its value
							}
							else{
								int size = rows.get(getColKey).size();	
								String prev = rows.get(getColKey).get(size-1); // get last value added from the same doc
								String concat;
								if(prev.equals("")){	// is previous value empty?
									if(valueString.equals("")){	// is new value also empty

									}
									else{
										concat=valueString;	// prev value is empty, so replace it with new value
										rows.get(getColKey).remove(size-1);	// remove the last value
										rows.get(getColKey).add(concat);	// add the new value										
									}
								}
								else{
									if(valueString.equals("")){	// nothing to do it new value is empty
										
									}
									else{
										concat = rows.get(getColKey).get(size-1) + " ~~ " + valueString;	// concatenate with new value separating with a " ~~ "
										rows.get(getColKey).remove(size-1);	// remove the last value
										rows.get(getColKey).add(concat);	// add the new value
									}
								}																
							}
						} 
						else{	
							// else create a new arraylist for this column; first element of arraylist is columnname itself
							ArrayList<String> column = new ArrayList<String>(allFiles.length+1);
							column.add(colNamesString);
							while(column.size()!=rowCount){
								column.add("");
							}
							column.add(valueString);
							//System.out.println("VALUE - "+ valueString+" added");

							// add the array list for column to global arraylist with a unique key
							rows.add(uniqueColKey, column);
							// add the arraylist for each column in the hashmap with its uniqueColKey
							checkColumn.put(colNamesString, uniqueColKey);

							uniqueColKey++;
						}	 	
					}
					//System.out.print("Done doc-"+currentDoc+"	");
					//currentDoc++;	
					docColumnCheck.clear();
				}	

				System.out.println(fileClusterLength+" processed docs analysed!");
				long docsProcessEndTime = System.currentTimeMillis();
				long docsProcessTime = docsProcessEndTime-docsProcessStartTime;
				System.out.println("Time to analyse processed docs: "+docsProcessTime+"ms");
				// docs in the cluster processed

				// reset counter to zero
				countFiles=0;
				// clear the corpus
				corpus.clear();
				// delete each document resource
				for(Iterator<Document> docResIterator = documentResList.iterator(); docResIterator.hasNext();){
					Factory.deleteResource((Resource) docResIterator.next());
				}
				System.out.println("All docs removed from LR and corpus cleared!");

				//clear list of document resources
				documentResList.clear();
				System.out.println("Document resource list cleared!");
				System.gc();
				
				docsStartTime=System.currentTimeMillis();				
			}						
		}
		
		/*File csvFile = new File("src/output/output1043/countLastMod.csv");
		PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(csvFile)));
		
		int totalCount =0;
		for(Iterator<String> countLastModIterator = countLastMod.keySet().iterator(); countLastModIterator.hasNext();){
			String fileName = countLastModIterator.next();
			int count = countLastMod.get(fileName);
			totalCount+=count;
			
			pw.println(fileName+", "+ count);
		}
		pw.println("Total= "+totalCount);
		pw.close();*/
	}
}
