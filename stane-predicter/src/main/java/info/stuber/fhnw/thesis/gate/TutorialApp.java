package info.stuber.fhnw.thesis.gate;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import gate.*;
import gate.creole.SerialAnalyserController;
import gate.util.GateException;
import info.stuber.fhnw.thesis.collector.Coding;
import info.stuber.fhnw.thesis.collector.DownloadPreparer;
import info.stuber.fhnw.thesis.collector.SourceLoader;
import info.stuber.fhnw.thesis.lucene.IndexFiles;

/*
 * Reads a website and extracts passages from it. The passages are then stored within a lucene index.
 */
public class TutorialApp {

	private static final int WINDOW_SIZE = 4;
	private static Set<Coding> errorCodings;

	/*
	 * Finally:
	 * https://gate.ac.uk/wiki/TrainingCourseJune2012/track-2/module-5-slides.
	 * pdf
	 * 
	 */
	static DownloadPreparer preparer = null;

	public static void main(String[] args)
			throws GateException, InvocationTargetException, InterruptedException, IOException {

		IndexFiles indexer = new IndexFiles();
		errorCodings = new HashSet<Coding>();

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

		// Load all URLs
		preparer = new DownloadPreparer();
		Set<Coding> codingSet = null;
		codingSet = (Set<Coding>) preparer.prepareUrls();

		int tmpCount = 0;
		for (Coding coding : codingSet) {

			if(coding.getTargetUrl() == null) {
				System.out.println("[SKIP3] " + coding.toString());
				errorCodings.add(coding);
				continue;
			}
			String targetUrl = coding.getTargetUrl().toString();
			if (coding.getHttpStatus() != 200) {
				System.out.println("[SKIP] " + coding.toString());
				errorCodings.add(coding);
				continue;
			}

			FeatureMap params = Factory.newFeatureMap();
			params.put(Document.DOCUMENT_URL_PARAMETER_NAME, targetUrl);
			if (coding.getEncoding() != null)
				params.put(Document.DOCUMENT_ENCODING_PARAMETER_NAME, coding.getEncoding());
			else
				params.put(Document.DOCUMENT_ENCODING_PARAMETER_NAME, "UTF-8");
			FeatureMap feats = Factory.newFeatureMap();
			feats.put("Date", new Date());

			Document doc = null;
			try {
				doc = (Document) Factory.createResource("gate.corpora.DocumentImpl", params, feats,
						"Document_" + tmpCount);
				corpus.add(doc);
			} catch (Exception ex) {
				System.out.println("[SKIP2]: " + coding.toString());
				errorCodings.add(coding);
				continue;
				// skip the current processing
			}

			ProcessingResource token = (ProcessingResource) Factory
					.createResource("gate.creole.tokeniser.DefaultTokeniser", Factory.newFeatureMap());
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

			int max = sentenceSet.size();
			int current = 0;

			// System.out.println("*** max: " + max + "; WindowSize: " +
			// WINDOW_SIZE + "***");

			Iterator<Annotation> iterator = sentenceSet.get("Sentence").iterator();
			ArrayList<String> sentenceList = new ArrayList<String>();

			while (iterator.hasNext()) {
				long start = 0;
				long end = 0;

				Annotation firstAnnotation = iterator.next();
				start = firstAnnotation.getStartNode().getOffset();

				if (WINDOW_SIZE == 1) {
					end = firstAnnotation.getEndNode().getOffset();
				} else {
					// före spuuhlen
					Iterator<Annotation> iteratorWindowSize = sentenceSet.get("Sentence").iterator();

					System.out.println("Current: " + current + " of " + (max));

					for (int i = 0; i <= (current + WINDOW_SIZE - 1); i++) {

						if (i >= max)
							break;

						Annotation lastAnnotation = iteratorWindowSize.next();
						end = lastAnnotation.getEndNode().getOffset();
					}
				}
				current++;

				DocumentContent content = doc.getContent();

				if (start > end) {
					// System.out.println(coding.getSource());
					System.out.println("[ERROR] Offset detected: Start: " + start + ", end: " + end);
					break;
				}

				String sentence = content.toString().substring((int) start, (int) end);
				sentenceList.add(sentence);
				System.out.println(current + ": " + sentence.trim());
			}

			corpus.remove(doc);

			System.out.println("[INDEX]: " + coding.toString());
			
			// Store to Lucene
			indexer.indexSentences(sentenceList, coding);
		}


	}

}
