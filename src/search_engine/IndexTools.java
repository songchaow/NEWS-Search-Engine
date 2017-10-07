package search_engine;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.jsoup.Jsoup;
import org.apache.lucene.index.IndexWriterConfig;


public class IndexTools {
	static final String index_path = "index";
	static final String doc_path = "E:\\projects\\search_engine\\data";
	static void makeIndex() throws IOException
	{
		Directory doc_dir = FSDirectory.open(Paths.get(doc_path));
		Directory index_dir = FSDirectory.open(Paths.get(index_path));
		Analyzer analyzer = new StandardAnalyzer();
		IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
		iwc.setOpenMode(OpenMode.CREATE);
		IndexWriter writer = new IndexWriter(index_dir,iwc);
		
	}
	static void indexDocument(IndexWriter writer, String raw_doc) throws IOException
	{
		Document doc = new Document();
		org.jsoup.nodes.Document soupdoc = Jsoup.parse(raw_doc);
		String title = soupdoc.getElementById("title").text();
		String url = soupdoc.getElementById("url").text();
		String description = soupdoc.getElementsByAttributeValue("name","description").attr("content");
		String publishid = soupdoc.getElementsByAttributeValue("name","publishid").attr("content");
		String subjectid = soupdoc.getElementsByAttributeValue("name","subjectid").attr("content");
		String keywords_raw = soupdoc.getElementByAttributeValue("name","keywords").attr("content");
		String[] keywords = keywords_raw.split(",");
		// the first item of keywords is just the title, so we skip it.
		String content = soupdoc.text();
		doc.add(new TextField("title",title,Field.Store.YES));
	}
}
