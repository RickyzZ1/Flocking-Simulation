package geometry;

public class CartesianCoordinate {
	private double xPosition;
	private double yPosition;
/**
 * Cartesian Coordinate to identify location
 * @param xPosition
 * @param yPosition
 */
	public CartesianCoordinate(double xPosition, double yPosition) {
		super();
		this.xPosition = xPosition;
		this.yPosition = yPosition;
	}
/*
 * Getters and Setters
 */
	public double getX() {
		return xPosition;
	}

	public double getY() {
		return yPosition;
	}

	public void setX(double xPosition) {
		this.xPosition = xPosition;

	}

	public void setY(double yPosition) {
		this.yPosition = yPosition;

	}

	/* 
	 * toString method allow human read the information 
	 */
	public String toString() {
		return super.toString() + ": (" + xPosition + ", " + yPosition + ")";

	}

}
