package view;


import java.util.Random;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
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
	
	public MapDisplayer(){
		
		//redraw();
	}
	
	public void setMapData(double[][] coordinates, double max, int x, int y, double distance) {
		this.coordinates = coordinates;
		this.x = x;
		this.y = y;
		this.distance = distance;
		this.max = max;
		
		redraw(max);
	}
	
	public void redraw(double max) {
		if(coordinates != null) {
			System.out.println("max:" + max);
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
	public void markDest(double posX, double posY) {
		int corX = (int)(posX / widthBlock);
		int corY = (int)(posY / heightBlock);
		
		redraw(max);
		
		GraphicsContext gc = getGraphicsContext2D();
	}
}
