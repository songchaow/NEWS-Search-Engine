package search_engine;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.queryparser.classic.ParseException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class Test {

	public static void main(String[] args) throws IOException, ParseException {
		// IndexTools.makeIndex();
		// test searcher
		Searcher searcher = new Searcher();
		searcher.search("联合国对极端武装在叙利亚", 10);
		
	}

}

class GlobalTest
{
	public static void test_jsoup(String[] args) throws IOException
	{
		// TODO test the behavior of jsoup
				File test_input = new File(IndexTools.doc_path+"\\test.txt");
				Document soupdoc = Jsoup.parse(test_input,"UTF-8");
				System.out.print(soupdoc.text());
				String title = soupdoc.getElementsByTag("title").text();
				String url = soupdoc.getElementsByTag("url").text();
				String description = soupdoc.getElementsByAttributeValue("name","description").attr("content");
				String[] publishid = soupdoc.getElementsByAttributeValue("name","publishid").attr("content").split(",");
				//combine the 3 number in publish_id into a long number.
				long publishid_num = Long.parseLong(publishid[0])*100000000000L+Long.parseLong(publishid[1])*100000000L+Long.parseLong(publishid[2]);
				String[] subjectid = soupdoc.getElementsByAttributeValue("name","subjectid").attr("content").split(",");
				long subjectid_num=0;
				if(subjectid.length>=3) subjectid_num = Long.parseLong(subjectid[0])*100000000000L+Long.parseLong(subjectid[1])*100000000L+Long.parseLong(subjectid[2]);
				String keywords_raw = soupdoc.getElementsByAttributeValue("name","keywords").attr("content");
				String[] keywords = keywords_raw.split(",");
				// the first item of keywords is just the title, so we skip it.
				String content = soupdoc.text();
	}
}
