package pdf2html.service.impl;

import java.io.InputStream;

import org.apache.pdfbox.examples.util.PrintImageLocations;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.util.PDFTextStripper;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pdf2html.service.intf.HTMLService;
import pdf2html.service.intf.PDF2HTMLService;
import pdf2html.service.intf.PDFService;
import pdf2html.util.PDF2HTMLException;

@Service
public class PDF2HTMLServiceImpl implements PDF2HTMLService {

	@Autowired
	private HTMLService htmlService;

	@Autowired
	private PDFService pdfService;

	@Override
	public String convertPage(InputStream document, int page)
			throws PDF2HTMLException {
		// Parse PDF
		PDDocument pdf = pdfService.load(document, null);
		PDPage pdPage = (PDPage) pdf.getDocumentCatalog().getAllPages()
				.get(page);

		// Create HTML doc
		PDRectangle rect = pdPage.getMediaBox();
		if (rect == null) {
			throw new PDF2HTMLException("Mediabox is null");
		}
		int width = (int) rect.getWidth();
		int height = (int) rect.getHeight();
		Document html = htmlService.createDocument("page-" + page, width, height);

		// Extract Images
		try {
			PrintImageLocations pil = new PrintImageLocations();
			pil.setHtmlService(htmlService);
			pil.setHtmlDocument(html);
			pil.processStream(pdPage, pdPage.findResources(), pdPage
					.getContents().getStream());
		} catch (Exception e) {
			throw new PDF2HTMLException(e);
		}

		// Extract text
		try {
			PDFTextStripper textStripper = new PDFTextStripper();
			textStripper.setHtmlService(htmlService);
			textStripper.setHtmlDocument(html);
			textStripper.setStartPage(page + 1);
			textStripper.setEndPage(page + 1);
			System.out.print(textStripper.getText(pdf));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return html.toString();
	}

}
