package ee.ut.ta.dict;

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

public class DictionaryListFileXmlParser {
	static final String TAG = "ged.filexmlparser"; // tag for LogCat
	
	String convertstreamtostring(InputStream is){
		java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
	    return s.hasNext() ? s.next() : "";
	}
	
	public List<IDictionary> parseFile(InputStream fileStream) {
		List<IDictionary> result = new ArrayList<IDictionary>();
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
				IDictionary dict = new AssetDictionary();
				dict.setId(Integer.parseInt(attrs.getNamedItem("id")
						.getNodeValue()));
				dict.setName(attrs.getNamedItem("name")
						.getNodeValue());
				dict.setDescription(attrs.getNamedItem("description")
						.getNodeValue());
				dict.setFileName(attrs.getNamedItem("fileName")
						.getNodeValue());
				dict.setLetterFileName(attrs
						.getNamedItem("letterFileName").getNodeValue());
				dict.setTransformationFileId(Integer.parseInt(attrs.getNamedItem(
						"transformationFileId").getNodeValue()));
				
				result.add(dict);
				Log.d(TAG, "Added "+i+". dictionary "+dict.getName());
			}

		} catch (Exception e) {
			e.printStackTrace();
			Log.e(TAG, "Error on parsing: "+e.getMessage());
		}

		return result;
	}

}
