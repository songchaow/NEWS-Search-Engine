package search_engine;

import com.sun.jna.Library;
import com.sun.jna.Native;

public class NlpirTest
{
	static final String NLPIR_Library_basePath = "D:\\Codes\\NLPIR-ICTCLAS";
	public interface CLibrary extends Library {
		CLibrary Instance = (CLibrary) Native.loadLibrary("NLPIR", CLibrary.class);
		public int NLPIR_Init(byte[] sDataPath, int encoding, byte[] sLicenceCode);
		public String NLPIR_ParagraphProcess(String sSrc, int bPOSTagged);
		public String NLPIR_GetKeyWords(String sLine, int nMaxKeyLimit, boolean bWeightOut);
		public String NLPIR_GetLastErrorMsg();
		public void NLPIR_Exit();
		
	}
	public static void main(String[] args) throws Exception
	{
		//com.sun.jna.NativeLibrary.addSearchPath("NLPIR", "D:\\Codes\\汉语分词20140928\\bin\\ICTCLAS2015");
		System.setProperty("jna.library.path", NLPIR_Library_basePath+"\\lib\\win64");
		int init_flag = CLibrary.Instance.NLPIR_Init(NLPIR_Library_basePath.getBytes("UTF-8"), 1, "0".getBytes("UTF-8"));
		if(0 == init_flag)
		{
			String nativeBytes = CLibrary.Instance.NLPIR_GetLastErrorMsg();
			System.err.println("初始化失败！fail reason is "+nativeBytes);
			System.err.println("Error initializing NLPIR.");
		}
		String sInput = "本报平壤12月31日电 (记者周之然)据朝鲜中央通讯社12月31日报道，朝鲜劳动党中央委员会和朝鲜劳动党中央军事委员会当天就金日成诞辰100周年发表联合口号，号召全党、全军和全民在金正恩领导下，化悲痛为千百倍的力量和勇气，使2012年成为朝鲜打开强盛国家之大门的昌盛之年。口号号召全党、全军和全民在金正恩领导下，千方百计地加强国家政治军事威力，使革命大高潮的烈火更加熊熊燃烧起来。全党、全军和全民以高度的政治热情和辉煌的劳动成果迎接金日成诞辰100周年和金正日诞辰70周年，把金日成和金正日的遗训作为朝鲜劳动党、军队和人民的斗争纲领紧紧抓住并发扬光大，以生命捍卫以金正恩为首的党中央委员会，全党、全军和全国人民要团结在他的周围。";
		String nativeBytes;
		try
		{
			nativeBytes = CLibrary.Instance.NLPIR_ParagraphProcess(sInput, 1);
			int nCountKey = 0;
			CLibrary.Instance.NLPIR_Exit();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		//
	}
}

