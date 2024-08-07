package shape;

import drawing.Canvas;
import geometry.CartesianCoordinate;

public class Rectangle extends Shape {
	private double sizeX;
	private double sizeY;
	private double xUpperBond;
	private double yUpperBond;
	private double xLowerBond;
	private double yLowerBond;
	private CartesianCoordinate topLeftVertex;
	private CartesianCoordinate topRightVertex;
	private CartesianCoordinate bottomLeftVertex;
	private CartesianCoordinate bottomRightVertex;

	/**
	 * Constructor
	 * 
	 * @param canvas
	 * @param position
	 */
	public Rectangle(Canvas canvas, CartesianCoordinate position) {
		super(canvas, position);
		sizeX = DEFAULT_SIZE;
		sizeY = DEFAULT_SIZE;
		xUpperBond = position.getX() + sizeX;
		xLowerBond = position.getX();
		yUpperBond = position.getY() + sizeY;
		yLowerBond = position.getY();
		topLeftVertex = position;
		topRightVertex = new CartesianCoordinate(xUpperBond, yLowerBond);
		bottomLeftVertex = new CartesianCoordinate(xLowerBond, yUpperBond);
		bottomRightVertex = new CartesianCoordinate(xUpperBond, yUpperBond);

	}

	/**
	 * Overload Constructor
	 * 
	 * @param canvas
	 * @param position
	 * @param sizeX  length of rectangle
	 * @param sizeY  width of rectangle
	 */
	public Rectangle(Canvas canvas, CartesianCoordinate position, double sizeX, double sizeY) {
		super(canvas, position);
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		xUpperBond = position.getX() + sizeX;
		xLowerBond = position.getX();
		yUpperBond = position.getY() + sizeY;
		yLowerBond = position.getY();
		topLeftVertex = position;
		topRightVertex = new CartesianCoordinate(xUpperBond, yLowerBond);
		bottomLeftVertex = new CartesianCoordinate(xLowerBond, yUpperBond);
		bottomRightVertex = new CartesianCoordinate(xUpperBond, yUpperBond);
	}

	/*
	 * Getters and Setters
	 */
	
	/**
	 * @return middle position of the rectangle
	 */
	public CartesianCoordinate getMiddlePos() {
		CartesianCoordinate midPos = new CartesianCoordinate(startPosition.getX() + (sizeX / 2),
				startPosition.getY() + (sizeY / 2));
		return midPos;
	}

	public CartesianCoordinate getTopLeftVertex() {
		return topLeftVertex;
	}

	public CartesianCoordinate getTopRightVertex() {
		return topRightVertex;
	}

	public CartesianCoordinate getBottomLeftVertex() {
		return bottomLeftVertex;
	}

	public CartesianCoordinate getBottomRightVertex() {
		return bottomRightVertex;
	}

	public double getUpperBondX() {
		return xUpperBond;

	}

	public double getUpperBondY() {
		return yUpperBond;

	}

	public double getLowerBondX() {
		return xLowerBond;

	}

	public double getLowerBondY() {
		return yLowerBond;

	}

	public double getSizeX() {
		return sizeX;
	}

	public double getSizeY() {
		return sizeY;
	}

	public void setSizeX(double sizeX) {
		this.sizeX = sizeX;
	}

	public void setSizeY(double sizeY) {
		this.sizeY = sizeY;
	}

	@Override
	public void draw() {
		agent.putPenDown(); // Pen down to draw
		agent.turn((int) getAngle());
		for (int i = 0; i < 2; i++) {
			agent.move((int) getSizeX());
			agent.turn(90);
			agent.move((int) getSizeY());
			agent.turn(90);
		}
		agent.putPenUp(); // Pen up when done
	}

}
