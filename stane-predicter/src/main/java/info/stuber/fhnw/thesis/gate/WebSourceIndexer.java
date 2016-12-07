package info.stuber.fhnw.thesis.gate;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import gate.*;
import gate.creole.SerialAnalyserController;
import gate.util.GateException;
import info.stuber.fhnw.thesis.collector.Coding;
import info.stuber.fhnw.thesis.utils.GetConfigPropertyValues;

/*
 * Reads a website and extracts passages from it. The passages are then stored within a lucene index.
 */
public class WebSourceIndexer {

	private int windowSize = 0;
	private static final int WINDOW_SIZE = Integer.parseInt(GetConfigPropertyValues.getProperty("window_size_1"));
	private static final String INDEX_PATH = GetConfigPropertyValues.getProperty("path_index_ws1");
	private static final int MIN_SENTENCE_LENGTH = 5;
	private IndexFiles indexer = null;

	private ProcessingResource token = null;
	private ProcessingResource sspliter = null;
	private SerialAnalyserController pipeline = null;
	private Corpus corpus = null;

	/*
	 * Finally:
	 * https://gate.ac.uk/wiki/TrainingCourseJune2012/track-2/module-5-slides.
	 * pdf
	 * 
	 */

	public WebSourceIndexer() throws GateException, MalformedURLException {
		indexer = new IndexFiles(INDEX_PATH);
		Gate.init();

		File pluginsDir = Gate.getPluginsHome();
		File aPluginDir = new File(pluginsDir, "ANNIE");
		Gate.getCreoleRegister().registerDirectories(aPluginDir.toURI().toURL());

		this.token = (ProcessingResource) Factory.createResource("gate.creole.tokeniser.DefaultTokeniser",
				Factory.newFeatureMap());
		this.sspliter = (ProcessingResource) Factory.createResource("gate.creole.splitter.SentenceSplitter",
				Factory.newFeatureMap());

		this.windowSize = WINDOW_SIZE;

		// MainFrame.getInstance().setVisible(true);
		// SwingUtilities.invokeAndWait(new Runnable() {
		// public void run() {
		// MainFrame.getInstance().setVisible(true);
		// }
		// });
	}

	public static void main(String[] args)
			throws InvocationTargetException, GateException, InterruptedException, IOException {
		// load serialized files

		long start = System.currentTimeMillis();

		List<Coding> codings = Deserializer.deserializeAllCoding();

		IndexFiles index = new IndexFiles(INDEX_PATH);
		WebSourceIndexer indexer = new WebSourceIndexer();
		for (Coding coding : codings) {
			coding.printDebug();
			List<String> sentenceList = indexer.splitSentence(coding, WINDOW_SIZE);
			index.indexSentences(sentenceList, coding);
		}

		long end = System.currentTimeMillis();
		System.out.println("Time needed: " + (end - start));
		System.out.println("WindowSize: " + WINDOW_SIZE);
		System.out.println("Index: " + INDEX_PATH);
	}

	public List<String> splitSentence(Coding coding, int windowSize)
			throws InvocationTargetException, GateException, InterruptedException, IOException {
		this.windowSize = windowSize;
		return splitSentence(coding);
	}

	public List<String> splitSentence(Coding coding)
			throws GateException, InvocationTargetException, InterruptedException, IOException {

		Document doc = null;
		ArrayList<String> sentenceList = new ArrayList<String>();

		this.pipeline = (SerialAnalyserController) Factory.createResource("gate.creole.SerialAnalyserController",
				Factory.newFeatureMap(), Factory.newFeatureMap(), "ANNIE");

		this.corpus = (Corpus) Factory.createResource("gate.corpora.CorpusImpl");

		FeatureMap params = Factory.newFeatureMap();
		params.put(Document.DOCUMENT_STRING_CONTENT_PARAMETER_NAME, coding.getContent());
		params.put(Document.DOCUMENT_ENCODING_PARAMETER_NAME, "UTF-8");

		FeatureMap feats = Factory.newFeatureMap();
		feats.put("Date", new Date());

		try {
			doc = (Document) Factory.createResource("gate.corpora.DocumentImpl", params, feats);
			corpus.add(doc);
		} catch (Exception ex) {
			ex.printStackTrace();
			coding.printDebug();
			return null;
		}

		this.pipeline.setCorpus(corpus);
		this.pipeline.add(token);
		this.pipeline.add(sspliter);
		this.pipeline.execute();

		// Split sentences
		List<Annotation> annotationList = doc.getAnnotations().get("Sentence").inDocumentOrder();
		DocumentContent content = doc.getContent();

		int maxAnnotations = annotationList.size();
		int index = 0;

		for (Annotation annotation : annotationList) {
			long start = 0;
			long end = 0;

			start = annotation.getStartNode().getOffset();

			if (this.windowSize == 1) {
				end = annotation.getEndNode().getOffset();
			} else {
				// get later annotation, based on index access.
				int endIndex = index + this.windowSize - 1;
				if (endIndex >= maxAnnotations) {
					endIndex = maxAnnotations - 1;
				}
				end = annotationList.get(endIndex).getEndNode().getOffset();
			}
			index++;

			String sentence = content.toString().substring((int) start, (int) end);
			sentence = sentence.replace("\n", "").replace("\r", "");

			if (sentence.length() < MIN_SENTENCE_LENGTH * this.windowSize) {
				// do not add, has not enough characters. 
			} else if (!sentence.contains(" ")) {
				// Do not add, does not contain a space!
			} else {
				sentenceList.add(sentence);
			}
		}

		corpus.remove(doc);
		return sentenceList;
	}
}
