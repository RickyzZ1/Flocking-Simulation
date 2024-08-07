package shape;

import agent.Agent;
import drawing.Canvas;
import geometry.CartesianCoordinate;

public abstract class Shape {
	protected static final int DEFAULT_SIZE = 100;
	protected static final double DEFAULT_ANGLE = 0.0;
	protected Agent agent;
	protected double size;
	protected double angle;
	protected CartesianCoordinate startPosition;

	public Shape(Canvas canvas, CartesianCoordinate position) {
		agent = new Agent(canvas);
		agent.putPenUp();
		agent.move((int) position.getX());
		agent.turn(90);
		agent.move((int) position.getY());
		agent.turn(-90);
		startPosition = new CartesianCoordinate((int) position.getX(), (int) position.getY());
		size = DEFAULT_SIZE;
		angle = DEFAULT_ANGLE;

	}

	public CartesianCoordinate getPosition() {
		return startPosition;
	}

	public void setPosition(CartesianCoordinate position) {
		agent.move((int) (position.getX() - startPosition.getX()));
		agent.turn(90);
		agent.move((int) (position.getY() - startPosition.getY()));
		agent.turn(-90);
		startPosition = new CartesianCoordinate((int) position.getX(), (int) position.getY());
	}

	public double getSize() {
		return size;
	}

	public void setSize(double size) {
		this.size = size;
	}

	public double getAngle() {
		return angle;
	}

	public void setAngle(double angle) {
		this.angle = angle;
	}

	public abstract void draw();

}
