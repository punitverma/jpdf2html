package pdf2html.service.intf;

import java.io.InputStream;

import pdf2html.util.PDF2HTMLException;

public interface PDF2HTMLService {

	public String convertPage(InputStream document, int page) throws PDF2HTMLException;
	
}
