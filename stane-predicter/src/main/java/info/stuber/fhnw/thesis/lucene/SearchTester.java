package info.stuber.fhnw.thesis.lucene;

import java.io.IOException;
import java.nio.file.Paths;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import info.stuber.fhnw.thesis.utils.GetConfigPropertyValues;

public class SearchTester {

	private static final String INDEX_PATH = GetConfigPropertyValues.getProperty("path_index");

	public static void main(String[] args) throws IOException, ParseException {
		// FIND SOMETHING:

		Analyzer analyzer = new StandardAnalyzer();
		Directory index = FSDirectory.open(Paths.get(INDEX_PATH));

		// 2. query
		String querystr = args.length > 0 ? args[0] : "UKIP";

		// the "title" arg specifies the default field to use
		// when no field is explicitly specified in the query.
		Query q = new QueryParser(LuceneConstants.CONTENTS, analyzer).parse(querystr);
		System.out.println(q.toString());

		// 3. search
		int hitsPerPage = LuceneConstants.MAX_SEARCH;
		IndexReader reader = DirectoryReader.open(index);
		IndexSearcher searcher = new IndexSearcher(reader);
		TopDocs docs = searcher.search(q, hitsPerPage);
		ScoreDoc[] hits = docs.scoreDocs;

		System.out.println("maxDocs: " + reader.maxDoc());
		System.out.println("numDocs: " + reader.numDocs());

		// 4. display results
		System.out.println("Found " + hits.length + " hits.");
		for (int i = 0; i < hits.length; ++i) {
			int docId = hits[i].doc;
			Document d = searcher.doc(docId);			
			System.out.println((i + 1) + ". (" + hits[i].score + ")\t" + d.get(LuceneConstants.SOURCE) + "\t" + d.get(LuceneConstants.CONTENTS));
		}

		// reader can only be closed when there
		// is no need to access the documents any more.
		reader.close();
	}
}
