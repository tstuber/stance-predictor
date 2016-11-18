package info.stuber.fhnw.thesis.collector;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.StringUtil;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.jsoup.select.NodeTraversor;
import org.jsoup.select.NodeVisitor;

import info.stuber.fhnw.thesis.utils.GetConfigPropertyValues;

public class WebCrawler {

	private static final String USER_AGENT = "Mozilla/5.0 (jsoup)";
	private static final String WEB_ARCHIVE_URL = "https://web.archive.org/web/20160331/";
	private static final int TIMEOUT = 10 * 1000;
	private static final String PATH_DOCUMENT = GetConfigPropertyValues.getProperty("path_documents");

	private Set<String> sources = null;
	private ArrayList<CrawableDocument> urlList = null;

	public WebCrawler(Set<String> sources) {

		this.sources = sources;

		this.urlList = new ArrayList<CrawableDocument>();
		int index = 1;
		for (String url : sources) {
			CrawableDocument doc = new CrawableDocument(index, url);
			urlList.add(doc);
			index++;
		}

	}

	public WebCrawler() {

	}

	public static void main(String[] args) throws MalformedURLException {
		WebCrawler crawler = new WebCrawler();
		crawler.scrapSites();
	}

	/*
	 * Fehlerquellen: MalformedURLException SocketTimeoutException
	 */
	public void scrapSites() {

		// TODO: Validate all URLs

		String url = "http://www.scotsman.com/news/politics/top-stories/scottish-independence-backed-by-plaid-cymru-leader-1-3484494";
		Coding coding = new Coding(7, 35, url);

		Document doc = null;
		HtmlToPlainText formatter = new HtmlToPlainText();

		try {
			doc = Jsoup.connect(coding.getSource()).followRedirects(true).userAgent(USER_AGENT).timeout(TIMEOUT).get();
		} catch (org.jsoup.HttpStatusException httpex) {
			httpex.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		coding.setContent(formatter.getPlainText(doc));

		
		Serializer.serializeCoding(coding);

	}

	public void crawlSites() {

		int counter = 1;
		boolean success;

		for (CrawableDocument entity : urlList) {

			success = false;
			String url = entity.getUrl();
			String webArchiveUrl = WEB_ARCHIVE_URL + url;

			System.out.println("Crawling Source " + counter + " from " + urlList.size());
			System.out.println("URL: " + entity.getUrl());

			String content = "";

			try {

				// lookup selector

				Document doc = null;
				try {
					// fetch the specified URL and parse to a HTML DOM
					doc = Jsoup.connect(webArchiveUrl).userAgent(USER_AGENT).timeout(TIMEOUT).get();
				} catch (org.jsoup.HttpStatusException httpex) {
					// fetch the specified URL and parse to a HTML DOM
					if (httpex.toString().contains("404"))
						try {
							doc = Jsoup.connect(url).userAgent(USER_AGENT).timeout(TIMEOUT).get();
						} catch (Exception ex) {
							System.out.println(ex);
							entity.addException(ex);
						}
					else {
						entity.addException(httpex);
					}

				}

				HtmlToPlainText formatter = new HtmlToPlainText();

				// String selector = CrawlerUtils.getSelector(url);
				String selector = null;

				if (selector != null) {

					Elements elements = doc.getElementsByTag("p");

					// Elements elements = doc.select(selector); // get each
					// element that matches the CSS selector
					// Elements elements = doc.getElementsByClass(selector); //
					// get
					// each
					// element
					// that
					// matches
					// the
					// CSS
					// selector
					for (Element element : elements) {
						entity.setContent(formatter.getPlainText(element)); // format
						// that
						// element
						// to
						// plain
						// text
					}
				} else { // format the whole doc
					entity.setContent(formatter.getPlainText(doc));
				}
			} catch (Exception ex) {
				System.out.println(ex);
				entity.addException(ex);
			}

			// Try to write File to disk
			File dir = new File(PATH_DOCUMENT);
			if (!dir.exists())
				dir.mkdir();

			String filename = PATH_DOCUMENT + entity.getFilename();
			System.out.println("Target: " + filename);
			writeToTextFile(filename, entity.getContent());

			if (entity.hasError())
				writeErrorToTextFile(entity.getErrorForSummary());
			else
				success = true;

			counter++;

			if (success)
				System.out.println("[DONE]");
			else
				System.out.println("[FAIL] " + entity.getErrorForConsle());
		}

	}

	public void writeErrorToTextFile(String content) {

		String filename = PATH_DOCUMENT + "/errorSummary.txt";

		try {
			Files.write(Paths.get(filename), content.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
		} catch (IOException ex) {
			System.out.println(ex);
		}
	}

	public static void writeToTextFile(String fileName, String content) {
		try {
			Files.write(Paths.get(fileName), content.getBytes(), StandardOpenOption.CREATE);
		} catch (IOException ex) {
			System.out.println(ex);
		}
	}

	/**
	 * Format an Element to plain-text
	 * 
	 * @param element
	 *            the root element to format
	 * @return formatted text
	 */
	public String getPlainText(Element element) {
		FormattingVisitor formatter = new FormattingVisitor();
		NodeTraversor traversor = new NodeTraversor(formatter);
		traversor.traverse(element); // walk the DOM, and call .head() and
										// .tail() for each node

		return formatter.toString();
	}

	// the formatting rules, implemented in a breadth-first DOM traverse
	private class FormattingVisitor implements NodeVisitor {
		private static final int maxWidth = 80;
		private int width = 0;
		private StringBuilder accum = new StringBuilder(); // holds the
															// accumulated text

		// hit when the node is first seen
		public void head(Node node, int depth) {
			String name = node.nodeName();
			if (node instanceof TextNode)
				append(((TextNode) node).text()); // TextNodes carry all
													// user-readable text in the
													// DOM.
			else if (name.equals("li"))
				append("\n * ");
			else if (name.equals("dt"))
				append("  ");
			else if (StringUtil.in(name, "p", "h1", "h2", "h3", "h4", "h5", "tr"))
				append("\n");
		}

		// hit when all of the node's children (if any) have been visited
		public void tail(Node node, int depth) {
			String name = node.nodeName();
			if (StringUtil.in(name, "br", "dd", "dt", "p", "h1", "h2", "h3", "h4", "h5"))
				append("\n");
			else if (name.equals("a"))
				append(String.format(" <%s>", node.absUrl("href")));
		}

		// appends text to the string builder with a simple word wrap method
		private void append(String text) {
			if (text.startsWith("\n"))
				width = 0; // reset counter if starts with a newline. only from
							// formats above, not in natural text
			if (text.equals(" ")
					&& (accum.length() == 0 || StringUtil.in(accum.substring(accum.length() - 1), " ", "\n")))
				return; // don't accumulate long runs of empty spaces

			if (text.length() + width > maxWidth) { // won't fit, needs to wrap
				String words[] = text.split("\\s+");
				for (int i = 0; i < words.length; i++) {
					String word = words[i];
					boolean last = i == words.length - 1;
					if (!last) // insert a space if not the last word
						word = word + " ";
					if (word.length() + width > maxWidth) { // wrap and reset
															// counter
						accum.append("\n").append(word);
						width = word.length();
					} else {
						accum.append(word);
						width += word.length();
					}
				}
			} else { // fits as is, without need to wrap text
				accum.append(text);
				width += text.length();
			}
		}

		@Override
		public String toString() {
			return accum.toString();
		}
	}

}
