package search_engine;
import java.io.IOException;
import java.nio.file.Paths;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
// missing?
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.jsoup.select.Evaluator.Matches;

public class Searcher {
	static final String index_path = "D:\\CLASSPATH\\search_engine\\index";
	private Analyzer my_analyzer = new ICTCLASAnalyzer();
	private QueryParser title_parser = new QueryParser("title",my_analyzer);
	private QueryParser content_parser = new QueryParser("content",my_analyzer);
	private String[] fields = {"title","content"};
	private QueryParser parser; 
	private IndexReader reader;
	private IndexSearcher searcher;
	
	private BooleanClause.Occur[] flags = {BooleanClause.Occur.SHOULD,BooleanClause.Occur.SHOULD};
	private ResultItem[] items;
    
	public Searcher() throws IOException
	{
		super();
		reader = DirectoryReader.open(FSDirectory.open(Paths.get(index_path)));
		searcher = new IndexSearcher(reader);
		parser = new QueryParser("content", my_analyzer);
		
	}
	
	public ResultItem[] getItems()
	{
		return items;
	}
	
	public int search(String query_str,int hitsPerPage)
	{
		Query query = null;
		if(! query_str.contains(":"))
		{
			MultiFieldQueryParser multiparser = new MultiFieldQueryParser(fields,my_analyzer);
			try {
				query = multiparser.parse(query_str);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("parse error");
			}
		} else
			try {
				query = parser.parse(query_str);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("parse error");
			}
		TopDocs results = null;
		try {
			results = searcher.search(query, 5 * hitsPerPage);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int result_n = Math.toIntExact(results.totalHits);
		// fill in result items
		ScoreDoc[] hits = results.scoreDocs;
		int len = Math.min(hitsPerPage, result_n);
		items = new ResultItem[len];
		for(int i = 0;i<=items.length-1;i++)
		{
			Document doc = null;
			try {
				doc = searcher.doc(hits[i].doc);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("doc exception");
			}
			if(doc == null) continue;
			String text = doc.get("title");
			items[i] = new ResultItem();
			items[i].title = doc.get("title");
			items[i].description = doc.get("description");
			items[i].url = doc.get("url");
			String publishid= doc.get("publishid");
			if(publishid!=null) items[i].publishid_num = Long.parseLong(publishid);
			String subjectid= doc.get("subjectid");
			if(subjectid!=null) items[i].subjectid_num = Long.parseLong(subjectid);
		}
		return result_n;
	}

	
}


