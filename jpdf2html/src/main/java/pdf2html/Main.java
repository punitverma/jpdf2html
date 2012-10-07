package pdf2html;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import pdf2html.service.intf.PDF2HTMLService;
import pdf2html.util.PDF2HTMLException;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		ApplicationContext appContext = new ClassPathXmlApplicationContext(
				"applicationContext.xml");

		PDF2HTMLService service = appContext.getBean(PDF2HTMLService.class);

		try {
			InputStream inputStream = new FileInputStream("test1.pdf");
			String output = service.convertPage(inputStream, 5);
			FileUtils.writeStringToFile(new File("generated.html"), output);
		} catch (PDF2HTMLException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
