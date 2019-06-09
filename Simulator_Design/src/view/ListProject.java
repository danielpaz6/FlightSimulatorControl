package view;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;

public class ListProject extends AnchorPane {
	
	String xmlFileName;
	public StringProperty scriptFileName;
	
	public ListProject() {
		//System.out.println("draw projects!");
		//drawProjects("./resources/projects.xml");
		scriptFileName = new SimpleStringProperty();
	}
	
	public void setXMLDirectory(String fileName) {
		this.xmlFileName = fileName;
	}
	
	public void drawProjects() {
		String[] tmpDataList = null;
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(xmlFileName);
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
				// <Pane layoutX="-8.0" prefHeight="35.0" prefWidth="350.0">
				Pane p = new Pane();
				p.setLayoutX(-8.0);
				p.setLayoutY(35.0 * j);
				p.setMinHeight(35.0);
				p.setMinWidth(350.0);
				//p.setStyle("-fx-background-color: GREY");
				
				MaterialDesignIconView m = new MaterialDesignIconView();
				m.setFill(Color.web("#074495"));
				m.setGlyphName("GOOGLE_CIRCLES_EXTENDED");
				m.setLayoutX(8.0);
				m.setLayoutY(23.0);
				m.setSize("15");
				
				Font f = new Font("System Bold", 11.0);
				Label l = new Label();
				// <Label layoutX="32.0" layoutY="10.0" text="Take off" textFill="#074495">
				
				l.setLayoutX(32.0);
				l.setLayoutY(10.0);
				l.setText(name.getTextContent());
				l.setTextFill(Color.web("#074495"));
				l.setFont(f);
				
				
				Label l2 = new Label();
				//  <Label layoutX="149.0" layoutY="10.0" text="Cessna C172p">
				
				l2.setLayoutX(149.0);
				l2.setLayoutY(10.0);
				l2.setText(type.getTextContent());
				l2.setFont(f);
				
				
				// <Label layoutX="285.0" layoutY="10.0" text="Daniel Paz" textFill="#074495">
				Label l3 = new Label();
				
				l3.setLayoutX(285.0);
				l3.setLayoutY(10.0);
				l3.setText(owner.getTextContent());
				l3.setTextFill(Color.web("#074495"));
				l3.setFont(f);
				
				// <MaterialDesignIconView glyphName="ACCOUNT_CHECK" layoutX="263.0" layoutY="23.0" size="15" />
				MaterialDesignIconView m2 = new MaterialDesignIconView();
				m2.setGlyphName("ACCOUNT_CHECK");
				m2.setLayoutX(263.0);
				m2.setLayoutY(23.0);
				m2.setSize("15");
				
				p.getChildren().add(m);
				p.getChildren().add(l);
				p.getChildren().add(l2);
				p.getChildren().add(l3);
				p.getChildren().add(m2);
				
				p.setOnMouseEntered(e -> {
					p.setStyle("-fx-background-color: #F0F0F0");
				});
				
				p.setOnMouseExited(e -> {
					p.setStyle("-fx-background-color: WHITE");
				});
				
				p.setOnMousePressed(e -> {
					System.out.println("file: " + dir.getTextContent());
					scriptFileName.set(dir.getTextContent());
				});
				
				this.getChildren().add(p);
				
				
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
