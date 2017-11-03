package search_engine;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;

public class ICTCLASAnalyzer extends Analyzer 
{

@Override
protected TokenStreamComponents createComponents(String fieldname) {
	// TODO Auto-generated method stub
	try 
	{
		Tokenizer source = new CHNTokenizer();
		TokenFilter filter;
		return new TokenStreamComponents(source);
	} 
	catch (UnsupportedEncodingException e) 
	{
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return null;
}

}

class CHNTokenizer extends Tokenizer
{
	public CHNTokenizer() throws UnsupportedEncodingException
	{
		super();
		parsetool=new ParseTool();
		
	}
	private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);
	private final OffsetAttribute offsetAtt = addAttribute(OffsetAttribute.class);
	private final PositionIncrementAttribute posIncrAtt = addAttribute(PositionIncrementAttribute.class);
	ParseTool parsetool;
	@Override
	public boolean incrementToken() throws IOException {
		clearAttributes();
		parsetool.setInput(input);
		if(parsetool.getNextToken())
		{
			termAtt.copyBuffer(parsetool.getCurrText().toCharArray(), 0, parsetool.getCurrLen());
			posIncrAtt.setPositionIncrement(1);
			offsetAtt.setOffset(parsetool.getCurrOffset(), parsetool.getCurrOffset()+parsetool.getCurrLen());
			return true;
		}
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public void reset() throws IOException
	{
		super.reset();
		parsetool.reset();
	}
	
}

class CHNTokenFilter extends TokenFilter
{

	protected CHNTokenFilter(TokenStream input) {
		super(input);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean incrementToken() throws IOException {
		// TODO Auto-generated method stub
		return false;
	}
	;
}
