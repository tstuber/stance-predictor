package info.stuber.fhnw.thesis.lucene;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.xml.ParserException;
import org.apache.lucene.queryparser.xml.QueryBuilder;
import org.apache.lucene.queryparser.xml.QueryBuilderFactory;
import org.apache.lucene.queryparser.xml.builders.BooleanQueryBuilder;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.aop.aspectj.AspectJAdviceParameterNameDiscoverer.AmbiguousBindingException;
import org.w3c.dom.Element;

import info.stuber.fhnw.thesis.utils.GetConfigPropertyValues;
import info.stuber.fhnw.thesis.utils.Party;
import info.stuber.fhnw.thesis.utils.Question;

public class SearchTester {

	// private static final String INDEX_PATH = GetConfigPropertyValues.getProperty("path_index");
	private static final String INDEX_PATH = GetConfigPropertyValues.getProperty("path_index_test");

	public SearchTester() {

	}

	public static void main(String[] args) throws IOException, ParseException {

		SearchTester tester = new SearchTester();
		tester.getBestHits(Party.UKIP, 1);
	}

	public HashMap<Integer,String> getBestHits(Party party, int questionId) {

		HashMap<Integer,String> result = new HashMap<Integer, String>();
		
		try {

			Analyzer analyzer = new StandardAnalyzer();
			Directory index = FSDirectory.open(Paths.get(INDEX_PATH));

			// 2. query
			// String issueStmt = Question.getQuestionById(questionId);
			String issueStmt = "Donald";
			int partyId = party.getId();
			// String special = issueStmt + " +party:" + partyId;
			String special = issueStmt;
			Query q = new QueryParser(LuceneConstants.CONTENTS, analyzer).parse(special);
			q = new QueryParser("contents", analyzer).parse("Donald +party:3");

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
				String passage = d.get(LuceneConstants.CONTENTS).replace("\n", "").replace("\r", "");
				System.out.println((i + 1) + ". (" + hits[i].score + ") Party:" + d.get(LuceneConstants.PARTY)
						+ " Question:" + d.get(LuceneConstants.QUESTION) + " URL:" + d.get(LuceneConstants.SOURCE)
						+ "\n" + passage);
				System.out.println(d.getFields(LuceneConstants.PARTY).length);
				result.put(i, passage);
			}

			// reader can only be closed when there
			// is no need to access the documents any more.
			reader.close();

		} catch (IOException ex) {
			System.out.println(ex.toString());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}
}
