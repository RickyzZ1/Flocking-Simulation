package agent;

import drawing.Canvas;
import geometry.CartesianCoordinate;

public class Agent {
	private Canvas myCanvas;
	private int x, y;
	private CartesianCoordinate currentPosition;
	private int angle; // Current direction of the agent in degrees
	private boolean penDown; // Flag to indicate whether the pen is down or up

	/**
	 * Constructor
	 */
	public Agent(Canvas myCanvas) {
		this.myCanvas = myCanvas;
		currentPosition = new CartesianCoordinate(0, 0);
		this.x = 0;
		this.y = 0;
		this.angle = 0;
		this.penDown = true;
	}

	/**
	 * The agent is moved in its current direction for the given number of pixels.
	 * If the pen is down when the robot moves, a line will be drawn on the floor.
	 * 
	 * @param i The number of pixels to move.
	 */
	public void move(int i) {
		double newX = currentPosition.getX() + i * Math.cos(Math.toRadians(angle));
		double newY = currentPosition.getY() + i * Math.sin(Math.toRadians(angle));

		if (penDown) {
			myCanvas.drawLineBetweenPoints(currentPosition, new CartesianCoordinate(newX, newY));
		}
		currentPosition = new CartesianCoordinate(newX, newY);
	}

	/**
	 * Rotates the agent clockwise by the specified angle in degrees. The angle has
	 * been normalised to -180 to 180 degrees
	 * 
	 * @param i the number of degrees to turn.
	 */
	public void turn(int i) {
		angle += i;
		angle = normaliseAngle(angle);
	}

	/**
	 * Moves the pen off the canvas so that the agent's route isn't drawn for any
	 * subsequent movements.
	 */
	public void putPenUp() {
		penDown = false;
	}

	/**
	 * Lowers the pen onto the canvas so that the turtle's route is drawn.
	 */
	public void putPenDown() {
		penDown = true;
	}

	/**
	 * Draw the shape of agent to make it visualize
	 */
	public void draw() {
		putPenDown();
		turn(90);
		move(4);
		for (int i = 0; i < 2; i++) {
			turn(240);
			move(8);
		}
		turn(240);
		move(4);
		turn(270);
		putPenUp();

	}

	/**
	 * Undraw the shape of turtle to make it not visualize
	 * 
	 * Use for Dynamic to wipe away drawing on previous position
	 */
	public void undraw() {
		myCanvas.removeMostRecentLine();
		myCanvas.removeMostRecentLine();
		myCanvas.removeMostRecentLine();
		myCanvas.removeMostRecentLine();
		myCanvas.repaint();

	}

	/**
	 * Wrap the position of agents when its position is bigger or smaller than
	 * canvas limits.
	 * 
	 * @param jPanelXLim xLimit of panel
	 * @param jPanelYLim yLimit of panel
	 */
	public void wrapPosition(int jPanelXLim, int jPanelYLim) {
		if ((int) currentPosition.getX() < 0) {
			currentPosition.setX(jPanelXLim);
		} else if ((int) currentPosition.getY() < 0) {
			currentPosition.setY(jPanelYLim);
		} else if ((int) currentPosition.getY() > jPanelYLim) {
			currentPosition.setY(0);
		} else if ((int) currentPosition.getX() > jPanelXLim) {
			currentPosition.setX(0);
		} else if ((int) currentPosition.getY() < 0) {
			currentPosition.setY(jPanelYLim);
		}
	}

	/**
	 * Normalise all the angle pass in into -180 to 180, that makes sure that the
	 * agent will not over rotate
	 * 
	 * Initially the agent is facing East as 0 degree;
	 * 
	 * @param angle
	 * @return normalised angle
	 */
	public int normaliseAngle(int angle) {
		angle %= 360;
		if (angle >= 180) {
			angle -= 360;
		} else if (angle < -180) {
			angle += 360;
		}
		return angle;
	}

	/**
	 * Calculates distance to the point pass in the method
	 * 
	 * @param position
	 * @return distance to that position
	 */
	public double distanceTo(CartesianCoordinate position) {
		return Math.sqrt(Math.pow(position.getX() - this.getPositionX(), 2)
				+ Math.pow(position.getY() - this.getPositionY(), 2));
	}

	/*
	 * Getters and Setters
	 */
	public int getPositionX() {
		return (int) currentPosition.getX();
	}

	public int getPositionY() {
		return (int) currentPosition.getY();
	}

	public CartesianCoordinate getPos() {
		return currentPosition;

	}

	public int getAngle() {
		return angle;
	}

	@Override
	public String toString() {
		return String.format("Turtle: [Position: (%.2f, %.2f), Angle: %.2f degrees, Pen: %s]", x, y, angle,
				penDown ? "Down" : "Up");
	};
}
