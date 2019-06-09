package view;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

public class ListProject extends AnchorPane {
	
	public ListProject() {
		
	}
	
	private void drawProjects(String fileName) {
		String[] tmpDataList = null;
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(fileName);
			NodeList nameList = doc.getElementsByTagName("name");
			NodeList typeList = doc.getElementsByTagName("type");
			NodeList ownerList = doc.getElementsByTagName("owner");
			NodeList directionList = doc.getElementsByTagName("direction");
 
			
			tmpDataList = new String[nameList.getLength()];
			for(int j = 0; j < nameList.getLength(); j++) {
				Element name = (Element)nameList.item(j);
				Element type = (Element)typeList.item(j);
				Element owner = (Element)ownerList.item(j);
				Element dir = (Element)directionList.item(j);
				
				//GraphicsContext gc = this.getGraphicsContext2D();
				Pane p = new Pane();
				p.setLayoutX(23.0);
				p.setLayoutY(124.0);
				p.prefHeight(35.0);
				p.prefWidth(364.0);
				
				MaterialDesignIconView m = new MaterialDesignIconView();
				
				this.getChildren().add(p);
				
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}
