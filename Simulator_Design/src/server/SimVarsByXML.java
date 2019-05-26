package server;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class SimVarsByXML implements SimulatorVariables {

	String fileName;
	
	public SimVarsByXML(String fileName) {
		this.fileName = fileName;
	}
	
	@Override
	public String[] getVariables() {
		String[] tmpDataList = null;
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(fileName);
			NodeList nodeList = doc.getElementsByTagName("node");
			
			tmpDataList = new String[nodeList.getLength()];
			for(int j = 0; j < nodeList.getLength(); j++) {
				Element p = (Element)nodeList.item(j);
				
				// And we also keep an array with the same data ( It's not big array so it's okay )
				// so we could track the order of the data inserted.
				tmpDataList[j] = p.getTextContent();
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		return tmpDataList;
	}

}
