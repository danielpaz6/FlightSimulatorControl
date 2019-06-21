package view;

import javafx.scene.effect.DropShadow;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;

public class Joystick extends AnchorPane {
	private final double SMALL_RADIUS = 38.0;
	private final double LARGE_RADIUS = 108.0;
	Circle outerCircle, innerCircle;
	public double radius;
	double centerX, centerY;
	double initializedCenterX, initializedCenterY;
	public double aileronVal, elevatorVal;
	
	public Joystick() {
		outerCircle = new Circle();
		innerCircle = new Circle();
		
//<Circle fx:id="btn_joystick" fill="#0e59c3" layoutX="108.0" layoutY="108.0" onMouseDragged="#dragable" onMousePressed="#dragable" onMouseReleased="#dragable_exit" radius="38.0" stroke="#0747a6" strokeType="INSIDE">
		outerCircle.setFill(Color.WHITE);
		outerCircle.setLayoutX(124.0);
		outerCircle.setLayoutY(108.0);
		outerCircle.setRadius(LARGE_RADIUS);
		outerCircle.setStroke(Color.web("#0747a6"));
		outerCircle.setStrokeType(StrokeType.INSIDE);
		outerCircle.setStrokeWidth(2.0);
		
		innerCircle.setFill(Color.web("#0e59c3"));
		innerCircle.setLayoutX(108.0);
		innerCircle.setLayoutY(108.0);
		innerCircle.setRadius(SMALL_RADIUS);
		innerCircle.setStroke(Color.web("#0747a6"));
		innerCircle.setStrokeType(StrokeType.INSIDE);
		
		DropShadow ds = new DropShadow();
		ds.setColor(Color.web("#cfcfcf"));
		ds.setHeight(5.0);
		ds.setOffsetX(3.0);
		ds.setOffsetY(3.0);
		ds.setRadius(2.0);
		ds.setSpread(0.28);
		ds.setWidth(5.0);
		
		innerCircle.setEffect(ds);
		
		this.getChildren().add(outerCircle);
		this.getChildren().add(innerCircle);
		
		AnchorPane.setTopAnchor(outerCircle, 0.0);
		AnchorPane.setBottomAnchor(outerCircle, 0.0);
		AnchorPane.setRightAnchor(outerCircle, 0.0);
		AnchorPane.setLeftAnchor(outerCircle, 0.0);
		
		initializedCenterX = innerCircle.getLayoutX();
		initializedCenterY = innerCircle.getLayoutY();
	}
	
	public void moveJoystick(double getSceneX, double getSceneY) {
		if(radius == 0) {
			radius = outerCircle.getRadius();
			centerX = (innerCircle.localToScene(innerCircle.getBoundsInLocal()).getMinX() + innerCircle.localToScene(innerCircle.getBoundsInLocal()).getMaxX())/2;
			centerY = (innerCircle.localToScene(innerCircle.getBoundsInLocal()).getMinY() + innerCircle.localToScene(innerCircle.getBoundsInLocal()).getMaxY())/2;		
		}
		
		double x1 = getSceneX;
		double y1 = getSceneY;
		double x2, y2;
		
		double distance = dist(x1, y1, centerX, centerY);
		if(distance <= radius) {
			innerCircle.setLayoutX(initializedCenterX + x1 - centerX);
			innerCircle.setLayoutY(initializedCenterY + y1 - centerY);
			
			x2 = x1;
			y2 = y1;
		}
		else
		{
			if(x1 > centerX) {
				double alfa = Math.atan((y1-centerY)/(x1-centerX));
				double w = radius * Math.cos(alfa);
				double z = radius * Math.sin(alfa);
				
				x2 = centerX + w;
				y2 = centerY + z;
				
				
				innerCircle.setLayoutX(initializedCenterX + x2 - centerX);
				innerCircle.setLayoutY(initializedCenterY + y2 - centerY);
			}
			else
			{
				double alfa = Math.atan((centerY - y1) / (centerX - x1));
				double w = radius * Math.cos(alfa);
				double z = radius * Math.sin(alfa);
				
				x2 = centerX - w;
				y2 = centerY - z;
			}
			
			innerCircle.setLayoutX(initializedCenterX + x2 - centerX);
			innerCircle.setLayoutY(initializedCenterY + y2 - centerY);
		}
		
		aileronVal = (x2 - centerX) / radius;
		elevatorVal = (centerY - y2) / radius;
	}
	
	public void resetJoystick() {
		innerCircle.setLayoutX(initializedCenterX);
		innerCircle.setLayoutY(initializedCenterY);
		
		aileronVal = 0;
		elevatorVal = 0;
	}
	
	private double dist(double x1, double y1, double x2, double y2) {
		return Math.sqrt((x1-x2) * (x1-x2) + (y1-y2) * (y1-y2));
	}
}
