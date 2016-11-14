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

public class CollectionSentimentAnalyzer {

	private static final int TIMEOUT_BEFORE_GETTING_RESPONSE = 2000; // in
		
	private Configuration m_config = null;
	private static ISerializer serializer = new JsonSerializer();
    private String version = "4.2";

	public static void main(String[] args) throws InterruptedException {
		// Replace with your API key and secret
		String API_KEY = "06758519-fdf0-4a36-a405-ff680b98db53";
		String API_SECRET = "8496b5de-1a16-4842-8c57-1c01efe57854";

		String configId = null;

		Session session = Session.createSession(API_KEY, API_SECRET);
        session.registerSerializer(serializer);
        
        session.setCallbackHandler(new CallbackHandler());
	    
        Collection coll = new Collection();
		coll.setId("TEST_COLLECTION_ID");
		List<String> documents = new ArrayList<String>();
		documents.add("This is fucking bad! Horror.");
		documents.add("UKIP has no idea. This is really far from reality.");
		documents.add("Amazing idea. We should really do it that way. ");
		coll.setDocuments(documents);

		int status = session.queueCollection(coll, configId);
		if (status == 202) 
			System.out.println("Document queued successfully!");


		List<CollAnalyticData> data = null;
		for ( int i = 0; i <10; i++ )
		{
			data = session.getProcessedCollections(configId);
			if (data != null && !data.isEmpty())
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
		
		
		CollAnalyticData col = null;
		if( data != null && data.isEmpty() == false )
		{
			for (CollAnalyticData colAnalyticData : data)
			{
				if( colAnalyticData.getId().equals("TEST_COLLECTION_ID") )
				{
					col = colAnalyticData;
				}
			}
		}
		
		
	
		System.out.println(col.toString());

		

	}

}
