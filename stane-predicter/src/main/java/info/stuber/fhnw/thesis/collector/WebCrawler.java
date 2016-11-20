package info.stuber.fhnw.thesis.collector;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
	private static final String WEB_ARCHIVE_URL = "https://web.archive.org/web/20140925/";
	private static final int TIMEOUT = 10 * 1000;
	private List<Coding> codings;

	public WebCrawler() {

		SourceLoader loader = new SourceLoader();
		codings = loader.getCodings();
	}

	public static void main(String[] args) throws MalformedURLException {
		WebCrawler crawler = new WebCrawler();
		crawler.scrapSites();
		crawler.makeReport();
	}

	public void makeReport() {

		int total = this.codings.size();
		int successful, aborted = 0;

		System.out.println("*ERRORS*");
		for (Coding coding : codings) {
			Exception ex = coding.getException();
			if (ex != null) {
				aborted++;
				coding.printDebug();
				System.out.println(ex.getClass() + " : " + ex.getMessage());
			}
		}

		successful = total - aborted;
		System.out.println(" ----- SUMMARY -----");
		System.out.println("Total Codings: " + total);
		System.out.println("Total Successful: " + successful);
		System.out.println("Total Aborted: " + aborted);

	}

	/*
	 * Fehlerquellen: MalformedURLException SocketTimeoutException
	 */
	public void scrapSites() {

		for (Coding coding : codings) {

			Document doc = null;
			String url = coding.getSourceUrl().toString();
			String pdfCachePath = PdfCache.getPdfCacheFilepath(url);

			if (pdfCachePath == null) {
				// Normal Processing - get source from Web.
				try {
					doc = Jsoup.connect(url).followRedirects(true).userAgent(USER_AGENT).timeout(TIMEOUT).get();
				} catch (IOException ex) {
					if (ex.toString().contains("404"))
						try {
							doc = Jsoup.connect(WEB_ARCHIVE_URL + url).followRedirects(true).userAgent(USER_AGENT)
									.timeout(TIMEOUT).get();
						} catch (Exception e) {
							coding.printDebug();
							e.printStackTrace();
							coding.setException(e);
						}
					else {
						coding.printDebug();
						ex.printStackTrace();
						coding.setException(ex);
					}
				}

				if (doc != null) {
					coding.setContent(getPlainText(doc));
				}
			} else {
				// PDF Content is load from Cache.
				String pdfContent = null;
				try {
					pdfContent = PdfCache.readFile(url);
				} catch (Exception e) {
					coding.printDebug();
					e.printStackTrace();
					coding.setException(e);
				}
				coding.setContent(pdfContent);
			}

			Serializer.serializeCoding(coding);
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
