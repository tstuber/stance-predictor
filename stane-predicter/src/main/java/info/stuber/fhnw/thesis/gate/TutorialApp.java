package info.stuber.fhnw.thesis.gate;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.SwingUtilities;

import gate.*;
import gate.annotation.AnnotationSetImpl;
import gate.creole.ResourceInstantiationException;
import gate.creole.SerialAnalyserController;
import gate.gui.MainFrame;
import gate.util.GateException;
import gate.util.Out;

public class TutorialApp {

	/*
	 * Finally:
	 * https://gate.ac.uk/wiki/TrainingCourseJune2012/track-2/module-5-slides.
	 * pdf
	 * 
	 */

	public static void main(String[] args)
			throws GateException, InvocationTargetException, InterruptedException, MalformedURLException {

		Gate.init();

		File pluginsDir = Gate.getPluginsHome();
		File aPluginDir = new File(pluginsDir, "ANNIE");
		Gate.getCreoleRegister().registerDirectories(aPluginDir.toURI().toURL());

		// MainFrame.getInstance().setVisible(true);
		SwingUtilities.invokeAndWait(new Runnable() {
			public void run() {
				MainFrame.getInstance().setVisible(true);
			}
		});

		gate.Corpus corpus = (Corpus) Factory.createResource("gate.corpora.CorpusImpl");

		FeatureMap params = Factory.newFeatureMap();
		params.put(Document.DOCUMENT_STRING_CONTENT_PARAMETER_NAME, "This is a document!");
		FeatureMap feats = Factory.newFeatureMap();
		feats.put("createdBy", "me!");
		Document doc1 = (Document) Factory.createResource("gate.corpora.DocumentImpl", params, feats,
				"My first document");

		params = Factory.newFeatureMap();
		params.put(Document.DOCUMENT_STRING_CONTENT_PARAMETER_NAME, "This is home");
		params.put(Document.DOCUMENT_URL_PARAMETER_NAME,
				"https://www.theguardian.com/commentisfree/2016/oct/17/3-million-citizens-uk-brexit-vote-theresa-may");
		params.put(Document.DOCUMENT_ENCODING_PARAMETER_NAME, "UTF-8");
		feats = Factory.newFeatureMap();
		feats.put("Date", new Date());
		Document doc2 = (Document) Factory.createResource("gate.corpora.DocumentImpl", params, feats,
				"My second document");

		Document doc3 = Factory.newDocument("Height is 60 in. Weight is 150 lbs pulse rate 90 Pulse rate 90");

		corpus.add(doc1);
		corpus.add(doc2);
		corpus.add(doc3);

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

		AnnotationSetImpl ann = (AnnotationSetImpl) doc1.getAnnotations();
		Iterator<Annotation> i = ann.get("Sentence").iterator();
		Annotation annotation = i.next();
		long start = annotation.getStartNode().getOffset();
		long end = annotation.getEndNode().getOffset();
		System.out.println("OUT: " + doc1.toString().substring((int) start, (int) end));

		// GET ALL ANNOTATIONS
		Map<String, AnnotationSet> namedASes = doc2.getNamedAnnotationSets();
		System.out.println("No. of named Annotation Sets:" + namedASes.size());

		for (String setName : namedASes.keySet()) {
			AnnotationSet aSet = namedASes.get(setName);
			System.out.println("No. of Annotations for " + setName + ":" + aSet.size());

			Set<String> annotTypes = aSet.getAllTypes();
			for (String aType : annotTypes) {
				System.out.println(" " + aType + ": " + aSet.get(aType).size());
			}
		}

		// GET URLS FROM A TAGS
		AnnotationSet origMarkupsSet = doc2.getAnnotations("Original markups");
		System.out.println("MARKUP SIZE (Original Markups): " + origMarkupsSet.size());
		AnnotationSet anchorSet = origMarkupsSet.get("a");
		System.out.println("Count a:" + anchorSet.size());

		for (Annotation anchor : anchorSet) {
			String href = (String) anchor.getFeatures().get("href");
			if (href != null) {
				try {
					// System.out.println(new URL(doc2.getSourceUrl(), href));
				} catch (Exception ex) {
					// System.out.println(ex.toString());
				}
			}
		}
		
		// GET URLS FROM SENTENCE TAGS
		AnnotationSet myMarkupsSet = doc2.getAnnotations("");
		System.out.println("MARKUP SIZE (all Markups): " + myMarkupsSet.size());
		AnnotationSet sentenceSet = myMarkupsSet.get("Sentence");
		System.out.println("Count Sentences: " + sentenceSet.size());
		
		for (Annotation sentence : sentenceSet) {

			long start1 = sentence.getStartNode().getOffset();
			long end2 =  sentence.getEndNode().getOffset();
			System.out.println(doc2.toString().substring((int)start1, (int)end2));
		}
		
	}

}
