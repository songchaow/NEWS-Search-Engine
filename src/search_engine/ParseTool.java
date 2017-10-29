package search_engine;

import java.io.IOException;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import search_engine.NlpirTest.NLPIRLibrary;

public class ParseTool {
	public ParseTool(Reader reader) throws UnsupportedEncodingException
	{
		input = reader;
		// setup the JNA and call `init`
		System.setProperty("jna.library.path", NLPIR_Library_basePath+"\\lib\\win64");
		int init_flag = NLPIRLibrary.Instance.NLPIR_Init(NLPIR_Library_basePath.getBytes("UTF-8"), 1, "0".getBytes("UTF-8"));
		if(0 == init_flag)
		{
			String nativeBytes = NLPIRLibrary.Instance.NLPIR_GetLastErrorMsg();
			System.err.println("初始化失败！fail reason is "+nativeBytes);
			System.err.println("Error initializing NLPIR.");
		}
		
		
		
	}
	static final String NLPIR_Library_basePath = "D:\\Codes\\NLPIR-ICTCLAS";
	private int curr_index=0;
	private Reader input;
	String nativeBytes = null;
	ArrayList<String> wordlists;
	
	
	private Pattern word_pattern;
	private Matcher m;
	
	// results:
	private String curr_text;
	private int curr_offset=0;
	private int curr_len=0;
	
	public String getCurrText()
	{
		return curr_text;
	}
	public int getCurrOffset()
	{
		return curr_offset;
	}
	public int getCurrLen()
	{
		return curr_len;
	}
	
	public boolean getNextToken() throws IOException
	{
		if(nativeBytes == null)
			init();
		
		if(m.find())
		{
			curr_text = m.group(1);
			curr_offset+=curr_len; // offset uses the last len's value, not current len's.
			curr_len = curr_text.length();
			return true;
		}
		else
		{
			curr_text = null;
			curr_len = 0;
			return false;
		}
			
	}
	public void init() throws IOException
	{
		// get text from reader, and parse it.
		// use string builder to avoid unnecessary string creation.
		StringBuilder builder = new StringBuilder();
		int charsRead = -1;
		char[] chars = new char[100];
		do{
		    charsRead = input.read(chars,0,chars.length);
		    // if we have valid chars, append them to end of string.
		    if(charsRead>0)
		        builder.append(chars,0,charsRead);
		}while(charsRead>0);
		String text = builder.toString();
		// now analyze the text use NLPIR
		nativeBytes = NLPIRLibrary.Instance.NLPIR_ParagraphProcess(text, 1);
		word_pattern = Pattern.compile("(\\w+?)/[a-z]+");
		m = word_pattern.matcher(nativeBytes);
	}
		

}
