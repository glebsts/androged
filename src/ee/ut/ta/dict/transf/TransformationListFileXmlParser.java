package ee.ut.ta.dict.transf;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

import android.util.Log;

public class TransformationListFileXmlParser {
	static final String TAG = "ged.transformationfilexmlparser"; // tag for LogCat
	
	String convertstreamtostring(InputStream is){
		java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
	    return s.hasNext() ? s.next() : "";
	}
	
	public List<ITransformationFile> parseFile(InputStream fileStream) {
		List<ITransformationFile> result = new ArrayList<ITransformationFile>();
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setIgnoringElementContentWhitespace(true);
		dbf.setIgnoringComments(true);
		try {
			DocumentBuilder builder = dbf.newDocumentBuilder();

			Document document = builder.parse(fileStream);
			document.getDocumentElement().normalize();
			Element root = document.getDocumentElement();
			NodeList nodeList = root.getChildNodes();
			for (int i = 0; i < nodeList.getLength(); i++) {
				NamedNodeMap attrs = nodeList.item(i).getAttributes();
				ITransformationFile file = new AssetTransformationFile();
				file.setId(Integer.parseInt(attrs.getNamedItem("id")
						.getNodeValue()));
				file.setName(attrs.getNamedItem("name")
						.getNodeValue());
				file.setFileName(attrs.getNamedItem("fileName")
						.getNodeValue());
				
				result.add(file);
				Log.d(TAG, "Added "+i+". transformation file "+file.getName());
			}

		} catch (Exception e) {
			e.printStackTrace();
			Log.e(TAG, "Error on parsing: "+e.getMessage());
		}

		return result;
	}

}
