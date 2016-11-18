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

import info.stuber.fhnw.thesis.lucene.SearchTester;
import info.stuber.fhnw.thesis.utils.Question;
import info.stuber.fhnw.thesis.utils.Party;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class DocumentSentimentAnalyzer {

	private static final int TIMEOUT_BEFORE_GETTING_RESPONSE = 2000; // in

	private Configuration m_config = null;
	private static ISerializer serializer = new JsonSerializer();
	private String version = "4.2";
	static String API_KEY = "06758519-fdf0-4a36-a405-ff680b98db53";
	static String API_SECRET = "8496b5de-1a16-4842-8c57-1c01efe57854";

	public DocumentSentimentAnalyzer() {

	}

	public static void main(String[] args) throws InterruptedException {

		Party party = Party.UKIP;
		int question = 5;

		DocumentSentimentAnalyzer analyzer = new DocumentSentimentAnalyzer();
		analyzer.calculateSentiment(party, question);
	}

	public PredictedResult calculateSentiment(Party party, int questionId) {
		PredictedResult predictionResult = new PredictedResult(party, questionId);
		SearchTester searcher = new SearchTester();
		HashMap<Integer, String> res = searcher.getBestHits(party, questionId);

		String configId = null;
		Session session = Session.createSession(API_KEY, API_SECRET);
		session.registerSerializer(serializer);
		session.setCallbackHandler(new CallbackHandler());

		List<Document> tasks = new ArrayList<Document>();
		for (int i = 0; i < 10; i++) {
			tasks.add(new Document("BATCH_" + i, res.get(i)));
		}
		// tasks.add(new Document("QUESTION", Question.getQuestionById(questionId)));

		int status = session.QueueBatchOfDocuments(tasks, configId);
		if (status == 202)
			System.out.println("Document queued successfully!");

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

			// Quercheck, kann irgendwann gelöscht werden!
			List<Float> sentimentScores = new ArrayList<Float>();

			for (DocAnalyticData docAnalyticData : data) {
				if (docAnalyticData.getId().startsWith("BATCH_") || docAnalyticData.getId().equals("QUESTION")) {
					doc = docAnalyticData;
					System.out.println(doc.getId() + " SentiScore: " + doc.getSentimentScore() + "\tDocPolarity: "
							+ doc.getSentimentPolarity() + "\t Text: " + doc.getSourceText());
					sentimentScores.add(doc.getSentimentScore());
					predictionResult.addItem(new PredictedResultItem(party.getId(), questionId, doc.getSentimentScore(),
							doc.getSentimentPolarity()));
				}
			}

			// Quercheck: Calculate Mean!
			float sum = 0.0f;
			for (Float value : sentimentScores) {
				sum += value;
			}
			float result = sum / sentimentScores.size();
			System.out.println("Overall SentimentScore: " + result);

			// Verify matching data lists sizes!
			if (predictionResult.size() != tasks.size())
				System.out.println("WARNUNG! Task-Liste (" + tasks.size() + ") und Result-Liste ("
						+ predictionResult.size() + "nicht gleich gross!");
		}

		return predictionResult;
	}

}
