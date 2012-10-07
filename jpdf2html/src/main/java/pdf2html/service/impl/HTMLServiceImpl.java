package pdf2html.service.impl;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import pdf2html.service.intf.HTMLService;
import pdf2html.util.HTMLConstants;
import pdf2html.util.PDF2HTMLException;

@Service
public class HTMLServiceImpl implements HTMLService {

	private Element pageElement;

	@Override
	public Document createDocument(String id, int width, int height)
			throws PDF2HTMLException {
		Document document = Jsoup.parse(HTMLConstants.EMPTY_HTML);

		Map<String, String> attributes = new HashMap<String, String>();
		attributes.put(HTMLConstants.ID, id);
		attributes
				.put("style",
						"width: "
								+ width
								+ "px; height: "
								+ height
								+ "px;"
								+ "border:2px solid; border-radius:5px; -moz-border-radius:5px; /* Firefox 3.6 and earlier */"
								+ "box-shadow: 10px 10px 5px #888888;");
		pageElement = addElement(document.body(), HTMLConstants.DIV, attributes);

		return document;
	}

	@Override
	public Element addText(Document document, String text, String id,
			int width, int height, float direction, float fontSize, float x,
			float y, String fontFamily, int fontWeight, boolean italic,
			int foreRed, int foreGreen, int foreBlue, int backRed,
			int backGreen, int backBlue) throws PDF2HTMLException {

		String fontStyle;
		if (italic) {
			fontStyle = "italic";
		} else {
			fontStyle = "normal";
		}

		direction -= 360;
		if (direction < 0) {
			direction *= -1;
		}

		Map<String, String> attributes = new HashMap<String, String>();
		attributes.put(HTMLConstants.ID, id);
		attributes.put("style", "position:absolute; left:" + x + "px; top:" + y
				+ "px; width: " + width + "px; height: " + height + "px;"
				+ "font-family: " + fontFamily + ";" + "font-size: " + fontSize
				+ "px;" + "font-style: " + fontStyle + ";" + "font-weight: "
				+ fontWeight + ";"
				+ "writing-mode:tb-rl;-webkit-transform:rotate(" + direction
				+ "deg); -moz-transform:rotate(" + direction
				+ "deg);	-o-transform: rotate(" + direction + "deg);"
				+ "color:rgb(" + foreRed + "," + foreGreen + "," + foreBlue
				+ ")");
		// + "background-color:rgb(" + backRed + "," + backGreen + "," +
		// backBlue + ");"
		Element element = addElement(pageElement, HTMLConstants.DIV, attributes);
		element.text(text);
		return element;
	}

	@Override
	public Element addImage(Document document, BufferedImage image, String id,
			int width, int height, int x, int y) throws PDF2HTMLException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			// TODO: possibly can skip this if able to retrieve image bytes +
			// mime type
			ImageIO.write(image, "png", bos);
		} catch (IOException e) {
			e.printStackTrace();
		}
		byte[] imageBytes = bos.toByteArray();
		String src = "data:image/png;base64,"
				+ Base64.encodeBase64String(imageBytes);
		String alt = "Meta7_1_Image8_1";
		// String style = "display: none";
		String style = "position:absolute; left:" + x + "px; top:" + y + "px;";

		Map<String, String> attributes = new HashMap<String, String>();
		attributes.put(HTMLConstants.ID, id);
		attributes.put(HTMLConstants.SRC, src);
		attributes.put(HTMLConstants.ALT, alt);
		attributes.put(HTMLConstants.WIDTH, String.valueOf(width));
		attributes.put(HTMLConstants.HEIGHT, String.valueOf(height));
		attributes.put(HTMLConstants.STYLE, style);

		Element element = addElement(pageElement, HTMLConstants.IMAGE,
				attributes);
		return element;
	}

	private Element addElement(Element parent, String name,
			Map<String, String> attributes) throws PDF2HTMLException {
		Element element = parent.appendElement(name);

		if (attributes != null) {
			for (Map.Entry<String, String> entry : attributes.entrySet()) {
				element.attr(entry.getKey(), entry.getValue());
			}
		}

		return element;
	}

}
