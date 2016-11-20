package info.stuber.fhnw.thesis.collector;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import org.apache.commons.io.IOUtils;

public class PdfCache {

	private static HashMap<String, String> pdfCache;

	public static String getPdfCacheFilepath(String url) {

		if (pdfCache == null)
			initializeCache();

		return pdfCache.get(url);
	}

	public static String readFile(String url) throws IOException {
		if (pdfCache == null)
			initializeCache();

		String pdfContent = null;
		InputStream inputStream = null;
		String filename = getPdfCacheFilepath(url);
		String path = "pdf/" + filename;
		try {
			inputStream = PdfCache.class.getClassLoader().getResourceAsStream(path);
 
			if (inputStream != null) {
				pdfContent = IOUtils.toString(inputStream, StandardCharsets.UTF_8); 
			} else {
				throw new FileNotFoundException("property file '" + path + "' not found in the classpath");
			}
 
		} catch (Exception e) {
			System.out.println("Exception: " + e);
		} finally {
			inputStream.close();
		}
		
		return pdfContent;
	}
	
	private static void initializeCache() {
		pdfCache = new HashMap<String, String>();

		pdfCache.put("http://www.stuber.info", "test.txt");
		
		pdfCache.put("https://www.conservatives.com/~/media/Files/Downloadable%20Files/Railreview.ashx?dl=true","Railreview.txt");
		pdfCache.put("http://www.parliament.uk/briefing-papers/SN06252.pdf","SN06252.txt");
		pdfCache.put("https://www.conservatives.com/europe/~/media/Files/Downloadable%20Files/MANIFESTO%202014/Euro%20Manifesto%20English.ashx","Euro Manifesto English.txt");
		pdfCache.put("https://www.conservatives.com/europe/~/media/Files/Downloadable%20Files/MANIFESTO%202014/Large%20Print%20Euro%20Manifesto_English.ashx","Large Print Euro Manifesto_English.txt");
		pdfCache.put("https://www.conservatives.com/~/media/files/downloadable%20Files/human_rights.pdf","HUMAN_RIGHTS.txt");
		pdfCache.put("https://www.gov.uk/government/uploads/system/uploads/attachment_data/file/363236/Command_paper.pdf","Command_paper.txt");
		pdfCache.put("http://b.3cdn.net/labouruk/89012f856521e93a4d_phm6bflfq.pdf","89012f856521e93a4d_phm6bflfq.txt");
		pdfCache.put("http://www.parliament.uk/briefing-papers/sn01747.pdf","SN01747.txt");
		pdfCache.put("https://d3n8a8pro7vhmx.cloudfront.net/libdems/pages/6272/attachments/original/1409941645/Pre-Manifesto_3_Sep_2014.pdf?1409941645","Pre-Manifesto_3_Sep_2014.txt");
		pdfCache.put("http://d3n8a8pro7vhmx.cloudfront.net/libdems/pages/4138/attachments/original/1392840151/116_-_Making_Migration_Work_for_Britain.pdf?1392840151","116_-_Making_Migration_Work_for_Britain.txt");
		pdfCache.put("http://www.ukipmeps.org/uploads/file/energy-policy-2014-f-20-09-2013.pdf","energy-policy-2014-f-20-09-2013.txt");
		pdfCache.put("http://www.politicsresources.net/area/uk/ge10/man/parties/UKIPManifesto2010.pdf","UKIPManifesto2010.txt");
		pdfCache.put("https://www.greenparty.org.uk/assets/files/Wealth%20Tax%20briefing%20July%202014.pdf","Wealth Tax briefing July 2014.txt");
		pdfCache.put("http://greenparty.org.uk/assets/files/European%20Manifesto%202014.pdf","European Manifesto 2014.txt");
		pdfCache.put("http://greenparty.org.uk/assets/files/PolicyPointers/Women_pointer.pdf","Women_pointer.txt");
		pdfCache.put("https://www.greenparty.org.uk/assets/files/democracy_for_everyone.pdf","democracy_for_everyone.txt");
		pdfCache.put("http://greenparty.org.uk/assets/files/reports/R&Rsubmission_sept2009.pdf","R&Rsubmission_sept2009.txt");
		// pdfCache.put("http://www.scottishgreens.org.uk/wp-content/uploads/downloads/2014/10/SGP-submission-to-Smith-Commission.pdf","xxx"); // NOT FOUND
		pdfCache.put("http://www.partyof.wales/uploads/1000_doctors_PDF_Eng_.pdf","1000_doctors_PDF_Eng_.txt");
		pdfCache.put("http://www.plaid.cymru/g_downloads/2011-06-13-29-2-safer-communities.pdf","2011-06-13-29-2-safer-communities.txt");
		pdfCache.put("http://www.politicsresources.net/area/uk/ge10/man/parties/plaid.pdf","plaid.txt");
		pdfCache.put("http://www.partyof.wales/uploads/downloads/Equal_Marriage_Consultation_Plaid_Cymru_Response_13-6.pdf","Equal_Marriage_Consultation_Plaid_Cymru_Response_13-6.txt");
		pdfCache.put("http://partyof.wales/uploads/Ewrop_2014_/EU_Manifesto_English.pdf","EU_Manifesto_English.txt");
		pdfCache.put("http://www.english.plaidcymru.org/uploads/Manifesto_2011/Main_maniffesto_English_SP.pdf","Main_maniffesto_English_SP.txt");
		pdfCache.put("http://www.partyof.wales/uploads/Higher_Education_Discussion_Document.pdf","Higher_Education_Discussion_Document.txt");
		pdfCache.put("http://www.partyof.wales/uploads/policies/Migration_and_Wales_final_copy.pdf","Migration_and_Wales_final_copy.txt");
	}
}
