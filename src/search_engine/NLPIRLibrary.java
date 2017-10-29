package search_engine;

import com.sun.jna.Native;

public interface NLPIRLibrary {
	NLPIRLibrary Instance = (NLPIRLibrary) Native.loadLibrary("NLPIR", NLPIRLibrary.class);
	public int NLPIR_Init(byte[] sDataPath, int encoding, byte[] sLicenceCode);
	public String NLPIR_ParagraphProcess(String sSrc, int bPOSTagged);
	public String NLPIR_GetKeyWords(String sLine, int nMaxKeyLimit, boolean bWeightOut);
	public String NLPIR_GetLastErrorMsg();
	public void NLPIR_Exit();
}
