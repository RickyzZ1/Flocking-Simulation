package agent;

import java.util.List;

import drawing.Canvas;
import shape.Rectangle;

public class DynamicAgent extends Agent {
	protected static final int MILLISECONDS_PER_SECOND = 1000;
	protected int velocity = 100;
	protected double angularVelocity = 0;
	private int maxVelocity = 100;
	private int avoidAngularVelCoef = 1; // AngularVelocity Coefficient depend on the speed of the simulation

	/**
	 * Constructor
	 * 
	 * @param myCanvas
	 */
	public DynamicAgent(Canvas myCanvas) {
		super(myCanvas);
		draw();
	}

	/**
	 * Overload Constructor for specific agent location and direction
	 * 
	 * @param myCanvas
	 * @param xPosition of agent
	 * @param yPosition of agent
	 * @param direction of agent
	 */
	public DynamicAgent(Canvas myCanvas, int xPosition, int yPosition, int direction) {
		super(myCanvas);
		putPenUp();
		move(xPosition);
		turn(90);
		move(yPosition);
		turn(270);
		putPenDown();
		turn(direction);
		draw();
	}

	/**
	 * Calculates the distance and angle, moves the agent to new position then draw
	 * the agent, for all dynamic agents it should avoid the obstacles passed in the
	 * method as it's a part of the base specification
	 * 
	 * Note undraw is not called in the method, it will be called in gameLoop to
	 * clear all to improve the performance
	 * 
	 * @param deltaTime
	 * @param obstacles to avoid
	 */
	public void update(int deltaTime, List<Rectangle> obstcales) {
		Rectangle obstacle = detectObstcale(obstcales);
		if (obstacle != null) {
			int sepraAngle = this.avoidObstcal(obstacle);
			angularVelocity = sepraAngle * avoidAngularVelCoef * 1.9;
		}
		float distance = (float) (velocity * deltaTime / MILLISECONDS_PER_SECOND);
		float angle = (float) (angularVelocity * deltaTime / MILLISECONDS_PER_SECOND);
		this.move((int) distance);
		this.turn((int) angle);
		draw();
	}

	/**
	 * Check which position is agent relative to the obstacle,detects the obstacles
	 * and the return the specific obstacle if the distance is close enough
	 * 
	 * @param obstacles
	 * @return obstacle to avoid
	 */
	public Rectangle detectObstcale(List<Rectangle> obstcales) {
		double distance = 100;
		for (Rectangle obstcale : obstcales) {

			// In X range of rectangle
			if (this.getPositionX() >= obstcale.getLowerBondX() && this.getPositionX() <= obstcale.getUpperBondX()) {

				// Below Lower Bond of Y
				if (this.getPositionY() < obstcale.getLowerBondY()) {
					distance = Math.abs(obstcale.getLowerBondY() - this.getPositionY());
				}
				// Above Upper Bond of Y
				else if (this.getPositionY() > obstcale.getUpperBondY()) {
					distance = Math.abs(obstcale.getUpperBondY() - this.getPositionY());
				}
			}

			// In Y range of rectangle
			else if (this.getPositionY() >= obstcale.getLowerBondY()
					&& this.getPositionY() <= obstcale.getUpperBondY()) {

				// Below Lower Bond of X
				if (this.getPositionX() < obstcale.getLowerBondX()) {
					distance = Math.abs(obstcale.getLowerBondX() - this.getPositionX());
				}

				// Above Upper Bond of X
				else if (this.getPositionX() > obstcale.getUpperBondX()) {
					distance = Math.abs(obstcale.getUpperBondX() - this.getPositionX());
				}
			}

			// Not in X or Y range of rectangle
			else if (this.getPositionX() < obstcale.getLowerBondX() && this.getPositionY() < obstcale.getLowerBondY()) {
				distance = this.distanceTo(obstcale.getTopLeftVertex());
			} else if (this.getPositionX() > obstcale.getUpperBondX()
					&& this.getPositionY() < obstcale.getLowerBondY()) {
				distance = this.distanceTo(obstcale.getTopRightVertex());
			} else if (this.getPositionX() < obstcale.getLowerBondX()
					&& this.getPositionY() > obstcale.getUpperBondY()) {
				distance = this.distanceTo(obstcale.getBottomLeftVertex());
			} else if (this.getPositionX() > obstcale.getUpperBondX()
					&& this.getPositionY() > obstcale.getUpperBondY()) {
				distance = this.distanceTo(obstcale.getBottomRightVertex());
			}
			if (distance <= 70) {
				return obstcale;
			}
		}
		return null;
	}

	/**
	 * Returns the angle to move away from the obstacle, using the center position
	 * of the obstacles
	 * 
	 * @param obstacle to avoid
	 * @return angle to avoid obstacle
	 */
	public int avoidObstcal(Rectangle obstcale) {
		// Get the relative position of the obstacle from the agent's position
		double dx = this.getPositionX() - obstcale.getMiddlePos().getX();
		double dy = this.getPositionY() - obstcale.getMiddlePos().getY();
		double distance = this.distanceTo(obstcale.getMiddlePos());
		dy /= Math.pow(distance, 2);
		dx /= Math.pow(distance, 2);

		// Calculate the angle to move directly away from the obstacle
		double targetAngle = Math.toDegrees(Math.atan2(dy, dx));

		// Get the current angle of the agent
		double currentAngle = this.getAngle();

		// Calculate the angle difference needed to turn away from the obstacle
		double angleToPos = normaliseAngle((int) (targetAngle - currentAngle));

		return (int) angleToPos;
	}

	/*
	 * Getters and Setters
	 */
	public int getVelocity() {
		return velocity;
	}

	public void setVelocity(int velocity) {
		this.velocity = velocity;
	}

	public int getMaxVelocity() {
		return maxVelocity;
	}

	public void setMaxVelocity(int maxVelocity) {
		this.maxVelocity = maxVelocity;
	}

	public double getAngularVelocity() {
		return angularVelocity;
	}

	public void setAngualrVelocity(double angularVelocity) {
		this.angularVelocity = angularVelocity;
	}

	public int getAvoidAngularVelCoef() {
		return avoidAngularVelCoef;
	}

	public void setAvoidAngularVelCoef(int value) {
		this.avoidAngularVelCoef = value;
	}
}
