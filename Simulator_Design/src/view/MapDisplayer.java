package view;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Random;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class MapDisplayer extends Canvas {
	
	double[][] coordinates;
	int x, y;
	double distance;
	double width; 
	double height;
	double widthBlock;
	double heightBlock;
	double max;
	int destX, destY; // position of plane dest
	int planeX, planeY; // position of plane.
	
	boolean isMarkedOnMap;
	
	public MapDisplayer(){
		isMarkedOnMap = false;
		//redraw();
	}
	
	public void setMapData(double[][] coordinates, double max, int x, int y, double distance) {
		this.coordinates = coordinates;
		this.planeX = x;
		this.planeY = y;
		this.distance = distance;
		this.max = max;
		
		//redraw(max);
		//movePlane(x, y); // movePlane also redraw
		System.out.println("we entered setMapData()");
	}
	
	public void redraw(double max) {
		if(coordinates != null) {
			double red = 0,green = 0;
			width = (double)(this.getWidth()) ;
			height = (double)(this.getHeight()) ;
			widthBlock = width / coordinates[0].length;
			heightBlock = height / coordinates.length;
			
		
			GraphicsContext gc = getGraphicsContext2D();
			
			for(int i = 0; i < coordinates.length;i++) {
				for(int j = 0; j<coordinates[0].length; j++) {
					if(coordinates[i][j] <= max * 0.5)
					{
						red = 255;
						green = coordinates[i][j] * (255 / max) * 2;
					}
					else
					{
						red = Math.abs(255 - ((coordinates[i][j] - (max/2)) * (255 / max) * 2));
						green = 255;
					}
					
					gc.setFill(new Color(red/255, green/255, 0.286, 1));
					gc.fillRect(j*widthBlock, i*heightBlock, widthBlock, heightBlock);					
					
					gc.setFill(Color.BLACK);
					gc.fillText((int)coordinates[i][j] + "", j*widthBlock + 4, i*heightBlock + heightBlock - 4);
				}
				
			}
		}
	}
	
	public void movePlane(double posX, double posY) {
		int corX = (int)(posX / widthBlock);
		int corY = (int)(posY / heightBlock);
		planeX = corX;
		planeY = corY;
		
		try {
			Image img = new Image(new FileInputStream("./resources/plane.png"));
			GraphicsContext gc = getGraphicsContext2D();
			//redraw(max); // redraw the map
			gc.drawImage(img, corX*widthBlock, corY*heightBlock); // draw plane
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void markDest(double posX, double posY) {
		isMarkedOnMap = true;
		int corX = (int)(posX / widthBlock);
		int corY = (int)(posY / heightBlock);
		
		// It's opposite from corX and corY because the (0,0) is top left
		// and the width means columns and height means rows.
		destX = corY;
		destY = corX;
		
		try {
			Image img = new Image(new FileInputStream("./resources/close.png"));
			GraphicsContext gc = getGraphicsContext2D();
			//redraw(max); // redraw the map
			movePlane(planeX, planeY); // redraw the plane location
			gc.drawImage(img, corX*widthBlock, corY*heightBlock); // draw the dest
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void putMarkOnMap(MouseEvent e) {
		System.out.println("test");
	}
}
