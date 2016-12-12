package info.stuber.fhnw.thesis.lucene;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
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
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.search.similarities.MultiSimilarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.aop.aspectj.AspectJAdviceParameterNameDiscoverer.AmbiguousBindingException;
import org.w3c.dom.Element;

import info.stuber.fhnw.thesis.utils.GetConfigPropertyValues;
import info.stuber.fhnw.thesis.utils.Party;
import info.stuber.fhnw.thesis.utils.Question;

public class SearchTester {

	public SearchTester() {

	}

	public static void main(String[] args) throws IOException, ParseException {

		SearchTester tester = new SearchTester();
		List<SearchResult> res = tester.retrieveTopDocs(Party.CON, 14, 2);;
	}

	public List<SearchResult> retrieveTopDocs(Party party, int questionId, int windowSize) {
		List<SearchResult> searchResults = new ArrayList<SearchResult>();

		String indexPath = getPathOfIndex(windowSize);

		try {
			Analyzer analyzer = new StandardAnalyzer();
			Directory index = FSDirectory.open(Paths.get(indexPath));

			// 2. query
			String issueStmt = Question.getQuestionById(questionId);
			int partyId = party.getId();
			String special = issueStmt + " +party:" + partyId;
			Query q = new QueryParser(LuceneConstants.CONTENTS, analyzer).parse(special);

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
			System.out.println("Found " + hits.length + " hits.\n");
			System.out.printf("Top Results for Party:%s and Question:%d\n" , party, questionId);
			
			int docCount = 0;
			for (int i = 0; i < hits.length; ++i) {
				
				// Aborts if already 10 documents are collected. 
				if(docCount >= 10) {
					break;
				}
				
				int docId = hits[i].doc;
				Document d = searcher.doc(docId);
				
				float hitScore = hits[i].score;
				String passage = d.get(LuceneConstants.CONTENTS);
				String source = d.get(LuceneConstants.SOURCE);
				
				SearchResult res = new SearchResult(passage, hitScore, docCount, source);
				
				// Only adds search result if not a duplicate. 
				if(!searchResults.contains(res)) {
					searchResults.add(res);
					docCount++;
//					System.out.println((docCount + 1) + ". (" + hitScore + ") Party:" + party + " Question:" + questionId
//							+ " URL:" + source + "\n" + passage);
					System.out.printf("%2d (%7.4f) %s (%s)\n" , docCount, hitScore, passage, source);
				} 	
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

		return searchResults;
	}

	public String getPathOfIndex(int windowSize) {

		if (windowSize > 0 && windowSize <= 4)
			return GetConfigPropertyValues.getProperty("path_index_ws" + windowSize);
		else
			return null;
	}
}
