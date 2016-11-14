package info.stuber.fhnw.thesis.collector;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Set;

public class DownloadPreparer {

	private SourceLoader loader;
	private Set<Coding> codings;
	private static final String webArchiveUrl = "https://web.archive.org/web/20160331/";

	public static void main(String[] args) {

		DownloadPreparer testee = new DownloadPreparer();
		testee.prepareUrls();
	}

	public DownloadPreparer() {
		loader = new SourceLoader();
		codings = loader.getCodings();
	}

	public Set<Coding> prepareUrls() {

		int count = 0;
		for (Coding coding : codings) {

//			count++;
//			if (count > 5)
//				break;

			HttpURLConnection connection;
			int httpStatus = 0;
			String contentType;
			String mimeType = "";
			String encoding = "";
			URL targetUrl = null;

			try {

				targetUrl = new URL(coding.getSource());
				connection = (HttpURLConnection) targetUrl.openConnection();
				connection.setRequestMethod("HEAD");
				httpStatus = connection.getResponseCode();
				contentType = connection.getContentType();
				// System.out.println(contentType);
				if (contentType != null) {
					if (contentType.contains(";")) {
						mimeType = contentType.split(";")[0].trim();
						encoding = contentType.split(";")[1].trim();
					} else if (contentType.contains("/")) {
						mimeType = contentType.trim();
					} else if (contentType.contains("=")) {
						encoding = contentType.trim();
					} else {
						// nothing
					}
				}
				// System.out.println("First: " + httpStatus);

				// Second try from WebArchive
				if (httpStatus != 200) {
					targetUrl = new URL(webArchiveUrl + coding.getSource());
					connection = (HttpURLConnection) targetUrl.openConnection();
					connection.setRequestMethod("HEAD");
					httpStatus = connection.getResponseCode();
					contentType = connection.getContentType();
					if (contentType != null) {
						if (contentType.contains(";")) {
							mimeType = contentType.split(";")[0].trim();
							encoding = contentType.split(";")[1].trim();
						} else if (contentType.contains("/")) {
							mimeType = contentType.trim();
						} else if (contentType.contains("charset=")) {
							encoding = contentType.trim();
						} else {
							// nothing
						}
					}

					// System.out.println("Second: " + httpStatus);
				}

			} catch (MalformedURLException malEx) {
				System.out.println("URL Malformed: " + targetUrl + " Party: " + coding.getParty() + " Question: "
						+ coding.getQuestion());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			coding.setEncoding(encoding);
			coding.setHttpStatus(httpStatus);
			coding.setTargetUrl(targetUrl);
			coding.setMimeType(mimeType);

			System.out.println("[PREPARE] " + coding.toString());

		}
 
		System.out.println("");
		return codings;
	}

}
