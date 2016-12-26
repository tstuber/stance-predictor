package info.stuber.fhnw.thesis.semantira;

import com.semantria.CallbackHandler;
import com.semantria.Session;
import com.semantria.auth.CredentialException;
import com.semantria.interfaces.ISerializer;
import com.semantria.mapping.Collection;
import com.semantria.mapping.Document;
import com.semantria.mapping.configuration.*;
import com.semantria.mapping.output.*;
import com.semantria.serializer.JsonSerializer;

import info.stuber.fhnw.thesis.lucene.SearchResult;
import info.stuber.fhnw.thesis.lucene.LuceneSearcher;
import info.stuber.fhnw.thesis.utils.GetConfigPropertyValues;
import info.stuber.fhnw.thesis.utils.Party;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class DocumentSentimentAnalyzer {
	private static final boolean ONLY_PASSAGES_WITH_SENTIMENT = Boolean.parseBoolean(GetConfigPropertyValues.getProperty("only_sentimental"));
	private static final boolean ONLY_FIRST_HIT = Boolean.parseBoolean(GetConfigPropertyValues.getProperty("first_hit"));

	private static ISerializer serializer = new JsonSerializer();

	
	static String API_KEY = "42885975-07fe-4fc2-b904-6ed743a441ef";
	static String API_SECRET = "d715adb6-d3fe-4966-8d94-eb5a26963858";

	public DocumentSentimentAnalyzer() {

	}

	public static void main(String[] args) throws InterruptedException {

		Party party = Party.UKIP;
		int question = 1;
		int windowSize = 1;

		DocumentSentimentAnalyzer analyzer = new DocumentSentimentAnalyzer();
		PredictedResult res = analyzer.returningHitScoreToCalculateToWeightScore(party, question, windowSize);
		System.out.println(res.getMean());
	}

	public PredictedResult returningHitScoreToCalculateToWeightScore(Party party, int questionId, int windowSize) {

		PredictedResult predictionResult = new PredictedResult(party, questionId);
		LuceneSearcher searcher = new LuceneSearcher();
		List<SearchResult> searchResults = searcher.retrieveTopDocs(party, questionId, windowSize);

		// Prepare documents to process.
		String configId = null;
		Session session = Session.createSession(API_KEY, API_SECRET);
		session.registerSerializer(serializer);
		session.setCallbackHandler(new CallbackHandler());
		
		//System.out.println("LEFT: "+session.getSubscription().getBillingSettings().getDocsBalance());
		//System.out.println("STATUS: "+session.getSubscription().getStatus());

		HashMap<String, String> sourceLookup = new HashMap<String, String>();
		List<Document> tasks = new ArrayList<Document>();
		for (SearchResult res : searchResults) {
			tasks.add(new Document("BATCH_" + res.getRanking(), res.getPassage()));
			sourceLookup.put("BATCH_" + res.getRanking(), res.source());
		}

		// VERIFY
		int status = session.QueueBatchOfDocuments(tasks, configId);
		if (status == 202)
			System.out.println("Document queued successfully!");

		if(status == 402)
			return null;
		
		List<DocAnalyticData> data = null;
		for (int i = 0; i < 5; i++) {
			data = session.getProcessedDocuments(configId);
			if (data != null && !data.isEmpty()) {
				break;
			}

			try {
				Thread.sleep(5000L);
			} catch (InterruptedException e) {
				e.printStackTrace(); // To change body of catch statement use
										// File | Settings | File Templates.
			}
		}

		DocAnalyticData doc = null;
		if (data != null && data.isEmpty() == false) {

			Collections.sort(data, new DocAnalyticDataComparator());
			StringBuilder sb = new StringBuilder();

			for (DocAnalyticData docAnalyticData : data) {
				if (docAnalyticData.getId().startsWith("BATCH_") || docAnalyticData.getId().equals("QUESTION")) {
					doc = docAnalyticData;

					int ranking = Integer.parseInt(doc.getId().split("_")[1]);
					float hitScore = searchResults.get(ranking).getHitScore();

					// Skip neutral results if only sentimented passages are used. 
					if(ONLY_PASSAGES_WITH_SENTIMENT && doc.getSentimentPolarity().equals("neutral"))
					{
						continue;
					}
					
					PredictedResultItem item = new PredictedResultItem(party.getId(), questionId, hitScore,
							doc.getSentimentScore(), doc.getSentimentPolarity(), doc.getSourceText());
					
					String source = sourceLookup.get(docAnalyticData.getId());
					item.setSource(source);
					
					predictionResult.addItem(item);

					String s = String.format("%s (%7.4f) Polarity:%s\tSentiScore:%.3f\tText:%s\n", doc.getId(),
							hitScore, doc.getSentimentPolarity(), doc.getSentimentScore(), doc.getSourceText());
					sb.append(s);

					// Skip any further results if only first hit is activated. 
					if(ONLY_FIRST_HIT)
						break;
				}
			}
			System.out.println("ELEMENTS: " + predictionResult.size());
			System.out.println(sb.toString());
		}
		return predictionResult;
	}
}
