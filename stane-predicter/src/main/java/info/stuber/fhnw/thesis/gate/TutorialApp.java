package info.stuber.fhnw.thesis.gate;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import gate.*;
import gate.creole.SerialAnalyserController;
import gate.util.GateException;
import info.stuber.fhnw.thesis.collector.SourceLoader;
import info.stuber.fhnw.thesis.lucene.IndexFiles;

public class TutorialApp {

	/*
	 * Finally:
	 * https://gate.ac.uk/wiki/TrainingCourseJune2012/track-2/module-5-slides.
	 * pdf
	 * 
	 */
	static SourceLoader ls = null;

	public static void main(String[] args)
			throws GateException, InvocationTargetException, InterruptedException, IOException {

		IndexFiles indexer = new IndexFiles();

		// Load all URLs
		ls = new SourceLoader();
		Set<String> urlSet = null;
		urlSet = (Set<String>) ls.readSourceFile();

		Gate.init();

		File pluginsDir = Gate.getPluginsHome();
		File aPluginDir = new File(pluginsDir, "ANNIE");
		Gate.getCreoleRegister().registerDirectories(aPluginDir.toURI().toURL());

		// MainFrame.getInstance().setVisible(true);
		// SwingUtilities.invokeAndWait(new Runnable() {
		// public void run() {
		// MainFrame.getInstance().setVisible(true);
		// }
		// });

		gate.Corpus corpus = (Corpus) Factory.createResource("gate.corpora.CorpusImpl");

		// TODO: Test, if Archive-Org URL is existing, else take original one!

		HttpURLConnection connection = (HttpURLConnection) new URL("http://www.exelance.ch/").openConnection();
		connection.setRequestMethod("HEAD");
		int responseCode = connection.getResponseCode();
		if (responseCode != 200) {
			System.out.println("ERRRROR");
		}
		
		 
		int tmpCount = 0;
		
		for(String url : urlSet)
		{
			tmpCount++;
			if(tmpCount > 2)
				break;
			
			System.out.println(url);
			
			FeatureMap params = Factory.newFeatureMap();
			// params.put(Document.DOCUMENT_STRING_CONTENT_PARAMETER_NAME, "This is home");
			params.put(Document.DOCUMENT_URL_PARAMETER_NAME, url);
			params.put(Document.DOCUMENT_ENCODING_PARAMETER_NAME, "UTF-8");
			FeatureMap feats = Factory.newFeatureMap();
			feats.put("Date", new Date());
			Document doc = (Document) Factory.createResource("gate.corpora.DocumentImpl", params, feats, "Document_"+tmpCount);

			corpus.add(doc);

			ProcessingResource token = (ProcessingResource) Factory.createResource("gate.creole.tokeniser.DefaultTokeniser",
					Factory.newFeatureMap());
			ProcessingResource sspliter = (ProcessingResource) Factory
					.createResource("gate.creole.splitter.SentenceSplitter", Factory.newFeatureMap());

			SerialAnalyserController pipeline = (SerialAnalyserController) Factory.createResource(
					"gate.creole.SerialAnalyserController", Factory.newFeatureMap(), Factory.newFeatureMap(), "ANNIE");
			pipeline.setCorpus(corpus);

			pipeline.add(token);
			pipeline.add(sspliter);
			pipeline.execute();

			// GET URLS FROM SENTENCE TAGS
			AnnotationSet myMarkupsSet = doc.getAnnotations();
			AnnotationSet sentenceSet = myMarkupsSet.get("Sentence");

			int windowSize = 4;
			int max = sentenceSet.size();
			int current = 0;

			System.out.println("*** max: " + max + "; WindowSize: " + windowSize + "***");

			Iterator<Annotation> iterator = sentenceSet.get("Sentence").iterator();
			ArrayList<String> sentenceList = new ArrayList<String>();

			while (iterator.hasNext()) {
				long start = 0;
				long end = 0;

				Annotation firstAnnotation = iterator.next();
				start = firstAnnotation.getStartNode().getOffset();

				if (windowSize == 1) {
					end = firstAnnotation.getEndNode().getOffset();
				} else {
					// före spuuhlen
					Iterator<Annotation> iteratorWindowSize = sentenceSet.get("Sentence").iterator();

					// System.out.println("Current: " + current + " of " + (max));

					for (int i = 0; i <= (current + windowSize - 1); i++) {

						if (i >= max)
							break;

						Annotation lastAnnotation = iteratorWindowSize.next();
						end = lastAnnotation.getEndNode().getOffset();
					}
				}
				current++;

				DocumentContent content = doc.getContent();
				
				if(start>end)
				{
					System.out.println(url);
					System.out.println("Start: " + start + ", end: " + end);
					break;
				}
				
				String sentence = content.toString().substring((int) start, (int) end);
				sentenceList.add(sentence);
				// System.out.println(current + ": " + sentence.trim());
			}

			corpus.remove(doc);
			
			System.out.println("Finish");
			// Store to Lucene


			indexer.indexSentences(sentenceList, url);
		}
	
		// AnnotationSetImpl ann = (AnnotationSetImpl) doc1.getAnnotations();
		// Iterator<Annotation> i = ann.get("Sentence").iterator();
		// Annotation annotation = i.next();
		// long start = annotation.getStartNode().getOffset();
		// long end = annotation.getEndNode().getOffset();
		// System.out.println("OUT: " + doc1.toString().substring((int) start,
		// (int) end));

		// // GET ALL ANNOTATIONS
		// Map<String, AnnotationSet> namedASes = doc2.getNamedAnnotationSets();
		// System.out.println("No. of named Annotation Sets:" +
		// namedASes.size());
		//
		// for (String setName : namedASes.keySet()) {
		// AnnotationSet aSet = namedASes.get(setName);
		// System.out.println("No. of Annotations for " + setName + ":" +
		// aSet.size());
		//
		// Set<String> annotTypes = aSet.getAllTypes();
		// for (String aType : annotTypes) {
		// System.out.println(" " + aType + ": " + aSet.get(aType).size());
		// }
		// }

		// GET URLS FROM A TAGS
		// AnnotationSet origMarkupsSet = doc2.getAnnotations("Original
		// markups");
		// System.out.println("MARKUP SIZE (Original Markups): " +
		// origMarkupsSet.size());
		// AnnotationSet anchorSet = origMarkupsSet.get("a");
		// System.out.println("Count a:" + anchorSet.size());
		//
		// for (Annotation anchor : anchorSet) {
		// String href = (String) anchor.getFeatures().get("href");
		// if (href != null) {
		// try {
		// // System.out.println(new URL(doc2.getSourceUrl(), href));
		// } catch (Exception ex) {
		// // System.out.println(ex.toString());
		// }
		// }
		// }

		// for(int count = 0; count>= maxSentence; count++) {
		// long start= 0;
		// long end = 0;
		//
		// Annotation annotation = iterator.next();
		// start = annotation.getStartNode().getOffset();
		//
		//
		//
		//// for(int i = 0; i>=windowsSize; i++) {
		//// Annotation annotation = iterator.next();
		//// }
		// // get start sentence
		//
		// // incrase to windows size
		//
		// // get end offset
		//
		// // cut sentence
		//
		//
		//// AnnotationSetImpl ann = (AnnotationSetImpl) doc1.getAnnotations();
		//// Iterator<Annotation> i = ann.get("Sentence").iterator();
		//// Annotation annotation = i.next();
		//// long start = annotation.getStartNode().getOffset();
		//// long end = annotation.getEndNode().getOffset();
		//
		// }
		//

		// System.out.println("---------------");
		// System.out.println("Elements: " + sentenceSet.size());
		//
		// for (Annotation sentence : sentenceSet) {
		//
		// long start1 = sentence.getStartNode().getOffset();
		// long end2 = sentence.getEndNode().getOffset();
		// System.out.println(doc2.toString().substring((int) start1, (int)
		// end2));
		// }

	}

}
