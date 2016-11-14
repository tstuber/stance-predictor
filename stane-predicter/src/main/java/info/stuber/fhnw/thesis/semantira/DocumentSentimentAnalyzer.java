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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DocumentSentimentAnalyzer {

	private static final int TIMEOUT_BEFORE_GETTING_RESPONSE = 2000; // in
		
	private Configuration m_config = null;
	private static ISerializer serializer = new JsonSerializer();
    private String version = "4.2";

	public static void main(String[] args) throws InterruptedException {
		// Replace with your API key and secret
		String API_KEY = "06758519-fdf0-4a36-a405-ff680b98db53";
		String API_SECRET = "8496b5de-1a16-4842-8c57-1c01efe57854";

		String configId = null;
		String jobId = "Stuber1234";
		String id = "TEST_DOCUMENT_ID";

		Session session = Session.createSession(API_KEY, API_SECRET);
        session.registerSerializer(serializer);
        
        session.setCallbackHandler(new CallbackHandler());
	    
        Document doc = new Document();
        String text = "UKIP has no idea. This is really far from reality. This is crazy and a big risk!";
        
		doc.setId(id);
		doc.setJobId(jobId);
		doc.setTag("UKIP Votes");
		doc.setText(text);
	

		int status = session.queueDocument(doc, configId);
		if (status == 202) 
			System.out.println("Document queued successfully!");
		
		List<DocAnalyticData> result = null;

		for ( int i = 0; i <10; i++ )
		{
			result = session.getProcessedDocumentsByJobId(jobId);
			if (result != null && !result.isEmpty())
			{
				break;
			}

			try
			{
				Thread.sleep(5000L);
			} catch (InterruptedException e)
			{
				e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
			}
		}
		
		System.out.println(result.size());
		DocAnalyticData document = null;
		if( result != null && result.isEmpty() == false )
		{
			for (DocAnalyticData colAnalyticData : result)
			{
				if( colAnalyticData.getId().equals(id) )
				{
					document = colAnalyticData;
				}
			}
		}
		

		System.out.println(document.getId() + " Sentiment Score: " + document.getSentimentScore() + " DocumentPolarity: " + document.getSentimentPolarity());

		

	}

}
