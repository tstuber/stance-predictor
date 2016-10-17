package info.stuber.fhnw.thesis.gate;

import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.SwingUtilities;

import gate.*;
import gate.creole.ResourceInstantiationException;
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

		// MainFrame.getInstance().setVisible(true);
		// SwingUtilities.invokeAndWait(new Runnable() {
		// public void run() {
		// MainFrame.getInstance().setVisible(true);
		// }
		// });

		// Factory.newDocument("This is a document");

		FeatureMap params = Factory.newFeatureMap();
		params.put(Document.DOCUMENT_STRING_CONTENT_PARAMETER_NAME, "This is a document!");
		FeatureMap feats = Factory.newFeatureMap();
		feats.put("createdBy", "me!");
		Factory.createResource("gate.corpora.DocumentImpl", params, feats, "My first document");

		params = Factory.newFeatureMap();
		params.put(Document.DOCUMENT_STRING_CONTENT_PARAMETER_NAME, "This is home");
		params.put(Document.DOCUMENT_URL_PARAMETER_NAME,
				"https://www.theguardian.com/commentisfree/2016/oct/17/3-million-citizens-uk-brexit-vote-theresa-may");
		params.put(Document.DOCUMENT_ENCODING_PARAMETER_NAME, "UTF-8");
		feats = Factory.newFeatureMap();
		feats.put("Date", "17.10.2016");
		Document res = (Document) Factory.createResource("gate.corpora.DocumentImpl", params, feats,
				"My second document");

		Set<Annotation> annotationSet = res.getAnnotations("Original markups");

		System.out.println(annotationSet.size() + " to Check!");

		int count = 0;

		for (Annotation a : annotationSet) {

			String type = a.getType();

			if (type.equals("a")) {
				
				System.out.println("Found a #" + count);
				FeatureMap map = a.getFeatures();
				
				Iterator entries = map.entrySet().iterator();
				
				while (entries.hasNext()) {
				  Entry thisEntry = (Entry) entries.next();
				  
				  if(thisEntry.getKey().equals("href"))
					  System.out.println(thisEntry.getValue());

				}
				

				//System.out.println("good");
				count++;
			}


			if (count > 10)
				break;
		}

		System.out.println("staring windows");

	}
}
