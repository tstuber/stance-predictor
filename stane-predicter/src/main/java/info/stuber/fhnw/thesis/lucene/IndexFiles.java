package info.stuber.fhnw.thesis.lucene;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Date;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import info.stuber.fhnw.thesis.collector.Coding;
import info.stuber.fhnw.thesis.utils.GetConfigPropertyValues;

/**
 * Index all text files under a directory.
 * <p>
 * This is a command-line application demonstrating simple Lucene indexing. Run
 * it with no command-line arguments for usage information.
 */
public class IndexFiles {

	private static final String INDEX_PATH = GetConfigPropertyValues.getProperty("path_index");
	// private static final String DOC_PATH =
	// GetConfigPropertyValues.getProperty("path_documents");
	private boolean CREATE = false;

	public IndexFiles() {

	}

	public void indexSentences(ArrayList<String> sentenceList, Coding coding) {

		if (sentenceList == null)
			return;

		Date start = new Date();
		try {
			Directory indexDirectory = FSDirectory.open(Paths.get(INDEX_PATH));

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

			// NOTE: if you want to maximize search performance,
			// you can optionally call forceMerge here. This can be
			// a terribly costly operation, so generally it's only
			// worth it when your index is relatively static (ie
			// you're done adding documents to it):
			//
			// writer.forceMerge(1);

			System.out.println("[INFO] Nr of Docs: " + writer.numDocs());

			writer.commit();

			if (writer.isOpen())
				writer.close();

			Date end = new Date();
			System.out.println("[INFO] DONE" + (end.getTime() - start.getTime()) + " total milliseconds");

		} catch (IOException e) {
			System.out.println(" caught a " + e.getClass() + "\n with message: " + e.getMessage());
		}
	}

	void indexDoc(IndexWriter writer, ArrayList<String> sentenceList, Coding coding) {

		for (String sentence : sentenceList) {

			try {
				Document doc = new Document();

				TextField textField = new TextField(LuceneConstants.CONTENTS, sentence, Field.Store.YES);
				doc.add(textField);

				// use a string field for isbn because we don't want it
				// tokenized
				Field urlField = new StringField(LuceneConstants.SOURCE, coding.getSource(), Field.Store.YES);
				Field partyField = new StringField(LuceneConstants.PARTY, Integer.toString(coding.getParty()),
						Field.Store.YES);
				Field questionField = new StringField(LuceneConstants.QUESTION, Integer.toString(coding.getQuestion()),
						Field.Store.YES);
				doc.add(urlField);
				doc.add(partyField);
				doc.add(questionField);

				if (writer.getConfig().getOpenMode() == OpenMode.CREATE) {
					// New index, so we just add the document (no old document
					// can
					// be there):
					writer.addDocument(doc);
				} else {
					// Existing index (an old copy of this document may have
					// been
					// indexed) so
					// we use updateDocument instead to replace the old one
					// matching
					// the exact
					// path, if present:
					// writer.updateDocument(new Term(LuceneConstants.SOURCE,
					// coding.getSource()), doc);
					writer.addDocument(doc);

				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
