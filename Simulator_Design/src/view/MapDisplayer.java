package view;


import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class MapDisplayer extends Canvas {
	
	double[][] coordinates;
	
	public MapDisplayer(){
		coordinates = new double[][]{
				{1,2,3,4},
				{4,3,3,4},
				{8,2,3,4}
		};
		
		redraw();
	}
	
	public void setMapData(double[][] coordinates) {
		
		this.coordinates = coordinates;
	}
	
	public void redraw() {
		if(coordinates != null) {
			double width = (double)(this.getWidth()) ;
			double height = (double)(this.getHeight()) ;
			double widthBlock = width / coordinates[0].length;
			double heightBlock = height / coordinates.length;
		
			
			GraphicsContext gc = getGraphicsContext2D();
			
			for(int i = 0; i < coordinates.length;i++) {
				for(int j = 0; j<coordinates[0].length; j++) {
					gc.fillRect(i*widthBlock, j*heightBlock, widthBlock,heightBlock);
					gc.setFill(new Color(0.0,0.0,255.0,1.0));
				}
			}
		}
	}
}
