package info.stuber.fhnw.thesis.gate;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import info.stuber.fhnw.thesis.collector.Coding;
import info.stuber.fhnw.thesis.lucene.LuceneConstants;

/**
 * Index all text files under a directory.
 * <p>
 * This is a command-line application demonstrating simple Lucene indexing. Run
 * it with no command-line arguments for usage information.
 */
public class IndexFiles {

	// private static final String DOC_PATH =
	// GetConfigPropertyValues.getProperty("path_documents");
	private boolean CREATE = false;
	private String indexPath;

	public IndexFiles(String indexPath) {
		this.indexPath = indexPath;
	}

	public void indexSentences(List<String> sentenceList, Coding coding) {

		if (sentenceList == null || coding == null)
			return;

		Date start = new Date();
		try {
			Directory indexDirectory = FSDirectory.open(Paths.get(this.indexPath));

			if (DirectoryReader.indexExists(indexDirectory)) {
				CREATE = false;
			} else {
				CREATE = true;
			}

			Analyzer analyzer = new StandardAnalyzer();
			IndexWriterConfig iwc = new IndexWriterConfig(analyzer);

			if (CREATE) {
				// Create a new index in the directory, removing any
				// previously indexed documents:
				iwc.setOpenMode(OpenMode.CREATE);
			} else {
				// Add new documents to an existing index:
				iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);
			}

			// Optional: for better indexing performance, if you
			// are indexing many documents, increase the RAM
			// buffer. But if you do this, increase the max heap
			// size to the JVM (eg add -Xmx512m or -Xmx1g):
			//
			// iwc.setRAMBufferSizeMB(256.0);

			IndexWriter writer = new IndexWriter(indexDirectory, iwc);

			indexDoc(writer, sentenceList, coding);

			System.out.println("[INFO] Nr of Docs: " + writer.numDocs());

			writer.commit();
			if (writer.isOpen())
				writer.close();

			Date end = new Date();
			System.out.println("[INFO] DONE " + (end.getTime() - start.getTime()) + " total milliseconds");

		} catch (IOException e) {
			System.out.println(" caught a " + e.getClass() + "\n with message: " + e.getMessage());
		}
	}

	void indexDoc(IndexWriter writer, List<String> sentenceList, Coding coding) {

		for (String sentence : sentenceList) {

			try {
				Document doc = new Document();

				// Add content.
				doc.add(new TextField(LuceneConstants.CONTENTS, sentence, Field.Store.YES));

				// Add features as StringFields since they are not tokenized.
				doc.add(new StringField(LuceneConstants.SOURCE, coding.getSourceUrl().toString(), Field.Store.YES));

				for (int party : coding.getParty()) {
					doc.add(new StringField(LuceneConstants.PARTY, Integer.toString(party), Field.Store.YES));
				}
				for (int question : coding.getQuestion()) {
					doc.add(new StringField(LuceneConstants.QUESTION, Integer.toString(question), Field.Store.YES));
				}


				writer.addDocument(doc);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
