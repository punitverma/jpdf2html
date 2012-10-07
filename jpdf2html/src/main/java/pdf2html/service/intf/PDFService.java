package pdf2html.service.intf;

import java.io.InputStream;

import org.apache.pdfbox.pdmodel.PDDocument;

import pdf2html.util.PDF2HTMLException;

public interface PDFService {

	public PDDocument load(InputStream input, String password)
			throws PDF2HTMLException;

}
