package pdf2html.service.impl;

import java.io.IOException;
import java.io.InputStream;

import org.apache.pdfbox.exceptions.CryptographyException;
import org.apache.pdfbox.exceptions.InvalidPasswordException;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.stereotype.Service;

import pdf2html.service.intf.PDFService;
import pdf2html.util.PDF2HTMLException;

@Service
public class PDFServiceImpl implements PDFService {

	@Override
	public PDDocument load(InputStream input, String password)
			throws PDF2HTMLException {
		PDDocument document;
		try {
			PDFParser parser = new PDFParser(input);
			parser.parse();
			document = parser.getPDDocument();
			if (document.isEncrypted()) {
				document.decrypt(password);
			}
		} catch (InvalidPasswordException e) {
			throw new PDF2HTMLException(e);
		} catch (IOException e) {
			throw new PDF2HTMLException(e);
		} catch (CryptographyException e) {
			throw new PDF2HTMLException(e);
		}
		return document;
	}

}
