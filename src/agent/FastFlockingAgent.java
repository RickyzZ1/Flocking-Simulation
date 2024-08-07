package agent;

import java.util.List;

import drawing.Canvas;
import shape.Rectangle;

public class FastFlockingAgent extends FlockingAgent {

	/*
	 * Constructors
	 */
	public FastFlockingAgent(Canvas myCanvas) {
		super(myCanvas);
	}

	public FastFlockingAgent(Canvas myCanvas, int xPosition, int yPosition, int direction) {
		super(myCanvas, xPosition, yPosition, direction);
	}

	@Override
	public void draw() {
		putPenDown();
		turn(90);
		move(5);
		for (int i = 0; i < 2; i++) {
			turn(240);
			move(10);
		}
		turn(240);
		move(5);
		turn(270);
		putPenUp();
	}

	@Override
	public void update(int deltaTime, List<Rectangle> obstcales) {
		Rectangle obstacle = detectObstcale(obstcales);
		if (obstacle != null) {
			int sepraAngle = this.avoidObstcal(obstacle);
			angularVelocity = sepraAngle * this.getAvoidAngularVelCoef() * 4.5;
		}
		float distance = (float) (velocity * 1.5 * deltaTime / MILLISECONDS_PER_SECOND);
		float angle = (float) (angularVelocity * 1.1 * deltaTime / MILLISECONDS_PER_SECOND);
		this.move((int) distance);
		this.turn((int) angle);
		draw();
	}

	@Override
	public void setVelocity(int velocity) {
		this.velocity = (int) (velocity * 2);
	}

	@Override
	public void setMaxVelocity(int maxVelocity) {
		this.velocity = maxVelocity * 2;
	}

	@Override
	public void setAngualrVelocity(double angularVelocity) {
		this.angularVelocity = angularVelocity * 1.1;
	}

}
