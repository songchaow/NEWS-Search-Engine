package search_engine;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.jsoup.Jsoup;
import org.apache.lucene.index.IndexWriterConfig;


public class IndexTools {
	static int i=0;
	static final String index_path = "index";
	static final String doc_path = "E:\\projects\\search_engine\\data";
	static String removeRubbish(String content)
	{
		content.replaceAll("<strong>", "");
		content.replaceAll("</strong>","");
		content.replaceAll("&nbsp", "");
		content.replaceAll("</a.*?>", "");
		content.replaceAll("<a.*?>", "");
		content.replaceAll("<span.*?>", "");
		return content;
	}
	static void makeIndex() throws IOException
	{
		Path doc_dir = Paths.get(doc_path);
		Directory index_dir = FSDirectory.open(Paths.get(index_path));
		Analyzer analyzer = new ICTCLASAnalyzer();
		IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
		iwc.setOpenMode(OpenMode.CREATE);
		IndexWriter writer = new IndexWriter(index_dir,iwc);
		// loop: open each file in doc_dir
		Files.walkFileTree(doc_dir, new SimpleFileVisitor<Path>() {
	        @Override
	        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
	          try (BufferedReader reader = Files.newBufferedReader(file,StandardCharsets.UTF_8))
	          {
	        	  System.out.println("----------------------------------------------");
	        	  i = 0;
	        	  String raw_doc="",line=null;
	        	  while((line=reader.readLine())!=null)
	        	  {
	        		  if(line.equals("<doc>"))
	        			  raw_doc = "";
	        		  raw_doc += line+"\r\n";
	        		  if(line.equals("</doc>"))
	        			  indexDocument(writer, raw_doc);
	        	  }
	          } catch (IOException ignore) {
	            // don't index files that can't be read.
	          }
	          return FileVisitResult.CONTINUE;
	        }
	      });
		writer.close();
	}
	static void indexDocument(IndexWriter writer, String raw_doc) throws IOException
	{
		Document doc = new Document();
		org.jsoup.nodes.Document soupdoc = Jsoup.parse(raw_doc);
		String title = soupdoc.getElementsByTag("title").text();
		String url = soupdoc.getElementsByTag("url").text();
		String description = soupdoc.getElementsByAttributeValue("name","description").attr("content");
		String[] publishid = soupdoc.getElementsByAttributeValue("name","publishid").attr("content").split(",");
		//combine the 3 number in publish_id into a long number.
		long publishid_num = 0;
		if(publishid.length>=3)
			publishid_num = Long.parseLong(publishid[0])*100000000000L+Long.parseLong(publishid[1])*100000000L+Long.parseLong(publishid[2]);
		String rawsubjectid = soupdoc.getElementsByAttributeValue("name","subjectid").attr("content");
		// there are multiple subjects
		rawsubjectid = rawsubjectid.replaceAll("&pid.*", "");
		String[] subjectids = rawsubjectid.split(";");
		for(String subjectid_str : subjectids)
		{
			String[] subjectid = subjectid_str.split(",");
			long subjectid_num=0;
			try
			{
				if(subjectid.length>=3) subjectid_num = Long.parseLong(subjectid[0])*100000000000L+Long.parseLong(subjectid[1])*100000000L+Long.parseLong(subjectid[2]);
				if(subjectid_num>0) doc.add(new LongPoint("subjectid",subjectid_num));
			}
			catch (NumberFormatException e)
			{
				System.err.println("the subjectid of the article titled:"+title+"was not parsed");
				
			}
		}
		
		
		String keywords = soupdoc.getElementsByAttributeValue("name","keywords").attr("content");

		String content = soupdoc.text();
		doc.add(new TextField("title",title,Field.Store.YES));
		doc.add(new StringField("url",url,Field.Store.YES));
		doc.add(new TextField("description",description,Field.Store.YES));
		doc.add(new LongPoint("publishid",publishid_num));
		doc.add(new TextField("keywords",keywords,Field.Store.YES));
		// the content will not be stored in INDEX to save space
		doc.add(new TextField("content",content,Field.Store.NO));
		writer.addDocument(doc);
		System.out.println("document "+ i++ + " added.");
	}
}
