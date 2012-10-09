package pdf2html.util;


public class FontMap {

	private static String[] REMOVE_PREFIXES = {".A{5}\\+"};
	
	private static String[] REMOVE_SUFFIXES = {"MT", "-.*", "PS"};
	
	public static String getFont(String pdfFontName) {
		//TODO: Gain performance with StringBuilder
		for (String prefix : REMOVE_PREFIXES) {
			pdfFontName = pdfFontName.replaceFirst(prefix, "");
		}
		
		for (String suffix : REMOVE_SUFFIXES) {
			pdfFontName = pdfFontName.replaceFirst(suffix, "");
		}
		
		return pdfFontName;
	}
	
}
