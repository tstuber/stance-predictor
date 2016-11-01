package info.stuber.fhnw.thesis.lucene;


import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

public class HelloLuceneSentences {
    public static void main(String[] args) throws IOException, ParseException {
        // 0. Specify the analyzer for tokenizing text.
        //    The same analyzer should be used for indexing and searching
        StandardAnalyzer analyzer = new StandardAnalyzer();

        // 1. create the index
        Directory index = new RAMDirectory();

        IndexWriterConfig config = new IndexWriterConfig(analyzer);

        IndexWriter w = new IndexWriter(index, config);
        
        String url = "https://www.theguardian.com/us-news/2016/oct/19/donald-trump-presidential-debate-election-result"; 
        
        addDoc(w, "Donald Trump says he'll keep country 'in suspense' on accepting election result", url);
        addDoc(w, "Hillary Clinton calls Republican nominee’s unprecedented refusal ‘horrifying’ in debate that saw heated clashes on abortion, immigration and gun rights", url);
        addDoc(w, "Donald Trump used the final presidential debate with Hillary Clinton to declare he would keep the country “in suspense” over whether he would accept the outcome of November’s election, stoking conspiracies over the legitimacy of the democratic process.", url);
        addDoc(w, "The Republican nominee’s refusal to endorse the results of the forthcoming election, unheard of in modern American history, capped a fractious debate in which he clashed with Clinton over abortion, gun rights, immigration and foreign policy.", url);
        w.close();

        // 2. query
        String querystr = args.length > 0 ? args[0] : "Hillary and Trump are dump.";

        // the "title" arg specifies the default field to use
        // when no field is explicitly specified in the query.
        Query q = new QueryParser(LuceneConstants.CONTENTS, analyzer).parse(querystr);
        System.out.println(q.toString());

        // 3. search
        int hitsPerPage = 10;
        IndexReader reader = DirectoryReader.open(index);
        IndexSearcher searcher = new IndexSearcher(reader);
        TopDocs docs = searcher.search(q, hitsPerPage);
        ScoreDoc[] hits = docs.scoreDocs;
        
		System.out.println("maxDocs: " + reader.maxDoc());
		System.out.println("numDocs: " + reader.numDocs());

        // 4. display results
        System.out.println("Found " + hits.length + " hits.");
        for(int i=0;i<hits.length;++i) {
            int docId = hits[i].doc;
            Document d = searcher.doc(docId);
            System.out.println((i + 1) + ". (" + hits[i].score + ")\t" + d.get(LuceneConstants.SOURCE) + "\t" + d.get(LuceneConstants.CONTENTS));
        }

        // reader can only be closed when there
        // is no need to access the documents any more.
        reader.close();
    }

    private static void addDoc(IndexWriter w, String sentence, String url) throws IOException {
        Document doc = new Document();
        doc.add(new TextField(LuceneConstants.CONTENTS, sentence, Field.Store.YES));

        // use a string field for isbn because we don't want it tokenized
        doc.add(new StringField(LuceneConstants.SOURCE, url, Field.Store.YES));
        w.addDocument(doc);
    }

}