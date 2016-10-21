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

import info.stuber.fhnw.thesis.utils.GetConfigPropertyValues;

/**
 * Index all text files under a directory.
 * <p>
 * This is a command-line application demonstrating simple Lucene indexing. Run
 * it with no command-line arguments for usage information.
 */
public class IndexFiles {

	private static final String INDEX_PATH = GetConfigPropertyValues.getProperty("path_index");
	// private static final String DOC_PATH = GetConfigPropertyValues.getProperty("path_documents");
	private boolean CREATE = false;

	public IndexFiles() {
		
	}
	
	public void indexSentences(ArrayList<String> sentenceList, String sourceUrl) {

		if (sentenceList == null)
			return;

		Date start = new Date();
		try {
			System.out.println("Indexing to directory '" + INDEX_PATH + "'...");

			Directory indexDirectory = FSDirectory.open(Paths.get(INDEX_PATH));
			
			
			if (DirectoryReader.indexExists(indexDirectory))
			{
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
			
			indexDoc(writer, sentenceList, sourceUrl);

			// NOTE: if you want to maximize search performance,
			// you can optionally call forceMerge here. This can be
			// a terribly costly operation, so generally it's only
			// worth it when your index is relatively static (ie
			// you're done adding documents to it):
			//
			// writer.forceMerge(1);

			System.out.println("Nr of Docs: " + writer.numDocs());

			// writer.commit();

			if (writer.isOpen())
				writer.close();

			Date end = new Date();
			System.out.println(end.getTime() - start.getTime() + " total milliseconds");

		} catch (IOException e) {
			System.out.println(" caught a " + e.getClass() + "\n with message: " + e.getMessage());
		}

		// FIND SOMETHING:

		// try {
		// String querystr = "boris";
		//
		// // the "title" arg specifies the default field to use
		// // when no field is explicitly specified in the query.
		// Directory dir = FSDirectory.open(Paths.get(INDEX_PATH));
		// Analyzer analyzer = new StandardAnalyzer();
		// Query q = new QueryParser("source", analyzer).parse(querystr);
		//
		// System.out.println(q.toString());
		//
		// // 3. search
		// int hitsPerPage = 10;
		// IndexReader reader;
		//
		// reader = DirectoryReader.open(dir);
		//
		// for (int i=0; i<reader.maxDoc(); i++) {
		//
		// Document doc = reader.document(i);
		// System.out.println(doc.toString());
		//
		// // do something with docId here...
		// }
		//
		//
		//
		//
		// System.out.println("total docs: " + reader.numDocs());
		// IndexSearcher searcher = new IndexSearcher(reader);
		// TopDocs docs = searcher.search(q, hitsPerPage);
		// ScoreDoc[] hits = docs.scoreDocs;
		//
		// // 4. display results
		// System.out.println("Found " + hits.length + " hits.");
		// for (int i = 0; i < hits.length; ++i) {
		// int docId = hits[i].doc;
		// Document d = searcher.doc(docId);
		// System.out.println((i + 1) + ". " + d.get("source") + "\t" +
		// d.get("content"));
		// }
		//
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (ParseException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

	}

	void indexDoc(IndexWriter writer, ArrayList<String> sentenceList, String sourceUrl) {

		for (String sentence : sentenceList) {

			try {
				Document doc = new Document();
				

		        TextField textField = new TextField(LuceneConstants.CONTENTS, sentence, Field.Store.YES);
		        doc.add(textField);			

				// use a string field for isbn because we don't want it
				// tokenized
				Field urlField = new StringField(LuceneConstants.SOURCE, sourceUrl, Field.Store.YES);
				doc.add(urlField);

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
					writer.updateDocument(new Term(LuceneConstants.SOURCE, sourceUrl), doc);

				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/** Indexes a single document */
	static void indexDoc(IndexWriter writer, Path file, long lastModified) throws IOException {
		try (InputStream stream = Files.newInputStream(file)) {
			// make a new, empty document
			Document doc = new Document();

			// Add the path of the file as a field named "path". Use a
			// field that is indexed (i.e. searchable), but don't tokenize
			// the field into separate words and don't index term frequency
			// or positional information:
			Field pathField = new StringField("path", file.toString(), Field.Store.YES);
			doc.add(pathField);

			// Add the last modified date of the file a field named "modified".
			// Use a LongPoint that is indexed (i.e. efficiently filterable with
			// PointRangeQuery). This indexes to milli-second resolution, which
			// is often too fine. You could instead create a number based on
			// year/month/day/hour/minutes/seconds, down the resolution you
			// require.
			// For example the long value 2011021714 would mean
			// February 17, 2011, 2-3 PM.
			doc.add(new LongPoint("modified", lastModified));

			// Add the contents of the file to a field named "contents". Specify
			// a Reader,
			// so that the text of the file is tokenized and indexed, but not
			// stored.
			// Note that FileReader expects the file to be in UTF-8 encoding.
			// If that's not the case searching for special characters will
			// fail.
			doc.add(new TextField("contents",
					new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))));

			if (writer.getConfig().getOpenMode() == OpenMode.CREATE) {
				// New index, so we just add the document (no old document can
				// be there):
				System.out.println("adding " + file);
				writer.addDocument(doc);
			} else {
				// Existing index (an old copy of this document may have been
				// indexed) so
				// we use updateDocument instead to replace the old one matching
				// the exact
				// path, if present:
				System.out.println("updating " + file);
				writer.updateDocument(new Term("path", file.toString()), doc);
			}
		}
	}
}
