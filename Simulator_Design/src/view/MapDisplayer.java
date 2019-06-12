package view;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Arrays;
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
	public int planeX, planeY; // position of plane.
	double initPlaneX = -5509036.243, initPlaneY = -2232151.782;
	String path;
	int initMapPlaneX, initMapPlaneY;
	
	public boolean isMarkedOnMap; // == isDestOnMap
	public boolean isMapLoaded;
	public boolean isPathExists;
	public boolean isPlaneMoved;
	
	public MapDisplayer(){
		isMarkedOnMap = false;
		isMapLoaded = false;
		isPathExists = false;
		isPlaneMoved = true;
		//redraw();
	}
	
	public void setMapData(double[][] coordinates, double max, int x, int y, double distance) {
		this.coordinates = coordinates;
		this.planeX = x;
		this.planeY = y;
		this.initMapPlaneX = x;
		this.initMapPlaneY = y;
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
					
					//if(green > 255) green = 255;
					//if(red > 255) red = 255;
					
					gc.setFill(new Color(red/255, green/255, 0.286, 1));
					gc.fillRect(j*widthBlock, i*heightBlock, widthBlock, heightBlock);					
					
					gc.setFill(Color.BLACK);
					gc.fillText((int)coordinates[i][j] + "", j*widthBlock + 4, i*heightBlock + heightBlock - 4);
				}
				
			}
		}
	}
	public void setPath(String path) {
		isPathExists = true;
		this.path = path;
	}
	
	public void updateIsPlaneMoved(double simPlaneX, double simPlaneY) {
		int planePosX = ((int)((simPlaneX - initPlaneX) / distance)) + initMapPlaneX;
		int planePosY = -1 * ((int)((simPlaneY - initPlaneY) / distance)) + initMapPlaneY;
		
		/*System.out.println("initPlaneX: " + initPlaneX);
		System.out.println("initPlaneY: " + initPlaneY);
		System.out.println("simPlaneX: " + simPlaneX);
		System.out.println("simPlaneY: " + simPlaneY);
		System.out.println("distance: " + distance);
		System.out.println("planePosX: " + planePosX);
		System.out.println("planePosY: " + planePosY);*/
		
		// the position of the plane before finish loading
		if(simPlaneX == -5506399.0 && simPlaneY == -2231083.25)
			return;
		
		if(simPlaneX == 0 && simPlaneY == 0)
			return;
		
		if(planePosX < 0 || planePosY < 0)
		{
			System.out.println("simPlaneX < 0 || simPlaneY < 0");
			return;
		}
		
		if(planePosX != planeX || planePosY != planeY)
		{
			isPlaneMoved = true;
			planeX = planePosX;
			planeY = planePosY;
		}
		else
			isPlaneMoved = false;
	}
	
	public void setPlaneOnMap(double simPlaneX, double simPlaneY)
	{
		
		int planePosX = ((int)((simPlaneX - initPlaneX) / distance)) + initMapPlaneX;
		int planePosY = -1 * ((int)((simPlaneY - initPlaneY) / distance)) + initMapPlaneY;
		
		/*System.out.println("initPlaneX: " + initPlaneX);
		System.out.println("initPlaneY: " + initPlaneY);
		System.out.println("simPlaneX: " + simPlaneX);
		System.out.println("simPlaneY: " + simPlaneY);
		System.out.println("distance: " + distance);
		System.out.println("planePosX: " + planePosX);
		System.out.println("planePosY: " + planePosY);*/
		
		// the position of the plane before finish loading
		if(simPlaneX == -5506399.0 && simPlaneY == -2231083.25)
			return;
		
		if(simPlaneX == 0 && simPlaneY == 0)
			return;
		
		if(planePosX < 0 || planePosY < 0)
		{
			System.out.println("simPlaneX < 0 || simPlaneY < 0");
			return;
		}
		
		redraw(max);
		movePlaneByPosition(planePosX, planePosY);
		
		if(isMarkedOnMap == true)
			markDestByPosition(destX, destY);
		
		//if(isPathExists == true)
			//drawPath(path);
	}
	
	public void movePlaneByPosition(int posX, int posY) {
		
		if(posX != planeX || posY != planeY)
		{
			isPlaneMoved = true;
			planeX = posX;
			planeY = posY;
		}
		else
			isPlaneMoved = false;
		
		try {
			Image img = new Image(new FileInputStream("./resources/plane.png"));
			GraphicsContext gc = getGraphicsContext2D();
			//redraw(max); // redraw the map
			gc.drawImage(img, posX*widthBlock, posY*heightBlock); // draw plane
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
	
	public void markDestByMouse(double posX, double posY) {
		isMarkedOnMap = true;
		int corX = (int)(posX / widthBlock);
		int corY = (int)(posY / heightBlock);
		
		// It's opposite from corX and corY because the (0,0) is top left
		// and the width means columns and height means rows.
		destX = corX;
		destY = corY;
		
		try {
			Image img = new Image(new FileInputStream("./resources/close.png"));
			GraphicsContext gc = getGraphicsContext2D();
			//redraw(max); // redraw the map
			//movePlane(planeX, planeY); // redraw the plane location
			gc.drawImage(img, corX*widthBlock, corY*heightBlock); // draw the dest
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void markDestByPosition(int posX, int posY) {
		isMarkedOnMap = true;
		int corX = posX;
		int corY = posY;
		
		// It's opposite from corX and corY because the (0,0) is top left
		// and the width means columns and height means rows.
		destX = corX;
		destY = corY;
		
		try {
			Image img = new Image(new FileInputStream("./resources/close.png"));
			GraphicsContext gc = getGraphicsContext2D();
			//redraw(max); // redraw the map
			//movePlane(planeX, planeY); // redraw the plane location
			gc.drawImage(img, corX*widthBlock, corY*heightBlock); // draw the dest
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void putMarkOnMap(MouseEvent e) {
		System.out.println("test");
	}
	
	public void drawPath(String path) {
		String[] mapPath = path.split(",");
		GraphicsContext gc = getGraphicsContext2D();
		gc.setFill(Color.BLACK);
		
		int posX = planeX, posY = planeY;
		System.out.println("posx + posy:");
		System.out.println(posX + "," + posY);
		
		/*System.out.println(planeX + "," + planeY);
		System.out.println(widthBlock);
		System.out.println(heightBlock);*/
		
		int len = mapPath.length - 1;
		for(int i = 0; i < len; i++) {
			if(mapPath[i].equals("Right")) {
				++posX;
				gc.fillRect(posX * widthBlock, posY * heightBlock + heightBlock/3, widthBlock/2, heightBlock/6);
			}
			else if(mapPath[i].equals("Down")) {
				++posY;
				gc.fillRect(posX * widthBlock + widthBlock/3, posY * heightBlock, widthBlock/6, heightBlock/2);
			}
			else if(mapPath[i].equals("Left")) {
				--posX;
				gc.fillRect(posX * widthBlock, posY * heightBlock + heightBlock/3, widthBlock/2, heightBlock/6);
			}
			else if(mapPath[i].equals("Up")) {
				--posY;
				gc.fillRect(posX * widthBlock + widthBlock/3, posY * heightBlock, widthBlock/6, heightBlock/2);

			}
			
		}
	}
}
