package info.stuber.fhnw.thesis.lucene;

import java.io.IOException;
import java.nio.file.Paths;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;

import info.stuber.fhnw.thesis.utils.GetConfigPropertyValues;

public class DirtyLuceneWriter {

	private static final String INDEX_PATH = GetConfigPropertyValues.getProperty("path_index_test");
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		StandardAnalyzer analyzer = new StandardAnalyzer();

        // 1. create the index
        Directory index = FSDirectory.open(Paths.get(INDEX_PATH));

        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        config.setOpenMode(OpenMode.CREATE);

        IndexWriter w = new IndexWriter(index, config);
        
        String url = "https://www.theguardian.com/us-news/2016/oct/19/donald-trump-presidential-debate-election-result"; 
        
        addDoc(w, "Donald Trump says he'll keep country 'in suspense' on accepting election result", url);
        addDoc(w, "Hillary Clinton calls Republican nominee’s unprecedented refusal ‘horrifying’ in debate that saw heated clashes on abortion, immigration and gun rights", url);
        addDoc(w, "Donald Trump used the final presidential debate with Hillary Clinton to declare he would keep the country “in suspense” over whether he would accept the outcome of November’s election, stoking conspiracies over the legitimacy of the democratic process.", url);
        addDoc(w, "The Republican nominee’s refusal to endorse the results of the forthcoming election, unheard of in modern American history, capped a fractious debate in which he clashed with Clinton over abortion, gun rights, immigration and foreign policy.", url);
        w.close();
	}
	
    private static void addDoc(IndexWriter w, String sentence, String url) throws IOException {
        Document doc = new Document();
        
        TextField textField = new TextField(LuceneConstants.CONTENTS, sentence, Field.Store.YES);
        doc.add(textField);
        
//        FieldType type = new FieldType();
//        type.setIndexOptions(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS_AND_OFFSETS);
//        type.setStored(true);
//        type.setTokenized(true);
//        
//        Field contentField = new Field(LuceneConstants.CONTENTS, sentence, type);
//        doc.add(contentField);

        // use a string field for isbn because we don't want it tokenized
        doc.add(new StringField(LuceneConstants.SOURCE, url, Field.Store.YES));
        doc.add(new StringField(LuceneConstants.QUESTION, "1", Field.Store.YES));
        doc.add(new StringField(LuceneConstants.PARTY, "4", Field.Store.YES));
        doc.add(new StringField(LuceneConstants.PARTY, "3", Field.Store.YES));
        w.addDocument(doc);
    }

}
