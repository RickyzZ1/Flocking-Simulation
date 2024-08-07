package agent;

import drawing.Canvas;
import shape.Rectangle;

import java.util.List;

public class Predator extends DynamicAgent {
	private int directionChangeCounter;
	private static final int MAX_COUNTER_VALUE = 100;
	private static final int MIN_COUNTER_VALUE = 50;
	private static final int MAX_DIRECTION_CHANGE = 90;
	private int preyDetectRadius = 160;
	private boolean hunger = true;
	private int updateCount = 0;
	private int chaseVelocity = 200;

	/**
	 * Constructor
	 * 
	 * @param myCanvas
	 */
	public Predator(Canvas myCanvas) {
		super(myCanvas);
		directionChangeCounter = (int) (Math.random() * (MAX_COUNTER_VALUE - MIN_COUNTER_VALUE)) + MIN_COUNTER_VALUE;
		draw();
	}

	/**
	 * Overload Constructor for specific predator location and direction
	 * 
	 * @param myCanvas
	 * @param xPosition
	 * @param yPosition
	 * @param direction
	 */
	public Predator(Canvas myCanvas, int xPosition, int yPosition, int direction) {
		super(myCanvas, xPosition, yPosition, direction);
		directionChangeCounter = (int) (Math.random() * (MAX_COUNTER_VALUE - MIN_COUNTER_VALUE)) + MIN_COUNTER_VALUE;
	}

	/**
	 * Hunt Method Chase and eat when an agent has been detected or close enough to
	 * eat keep changing direction if no agent has been detect
	 *
	 * @param agents
	 * @param predators
	 */
	public void hunt(List<FlockingAgent> agents, List<Predator> predators) {

		// set back to normal speed(after finish chase a target)
		this.setVelocity(this.getMaxVelocity());

		if (updateCount > 200) {
			updateCount = 0;
			hunger = true;
		}
		updateCount++;

		if (hunger) {
			FlockingAgent target = detectPrey(agents);
			if (target != null) {
				chase(target);
				if (distanceTo(target.getPos()) < 20) { // Check if prey is close enough to eat
					agents.remove(target);
					hunger = false;
					this.setAngualrVelocity(0);
				}
			} else if (target == null) {
				directionChangeCounter--;
				if (directionChangeCounter <= 0) {
					int randomChange = (int) ((Math.random() * (2 * MAX_DIRECTION_CHANGE + 1)) - MAX_DIRECTION_CHANGE);
					this.setAngualrVelocity(randomChange);
					directionChangeCounter = (int) (Math.random() * (MAX_COUNTER_VALUE - MIN_COUNTER_VALUE))
							+ MIN_COUNTER_VALUE;
				}

			}

		}
	}

	/**
	 * Detect if there is any agent near by, then return the specific agent
	 *
	 * @param agents to pass in to detect
	 * @return agents is close enough
	 */
	private FlockingAgent detectPrey(List<FlockingAgent> agents) {
		for (FlockingAgent prey : agents) {
			double distance = distanceTo(prey.getPos());
			if (distance <= preyDetectRadius) {
				return prey; // Return the first detected prey

			}
		}
		return null;
	}

	/**
	 * Chase the specific agent passed in to the method
	 * 
	 * @param prey to chase
	 */
	private void chase(FlockingAgent prey) {
		boolean accelerate = false;
		double targetAngle = calculateAngleTo(prey);
		double currentAngle = this.getAngle();
		double angleDifference = normaliseAngle((int) (targetAngle - currentAngle));
		this.setAngualrVelocity(angleDifference * 2.5);
		// accelerate if there only small angle adjustment
		if (angleDifference <= 45 && angleDifference >= -45) {
			accelerate = true;
			this.setVelocity(this.getMaxVelocity() + chaseVelocity);
			// set velocity back when the if requires large angle adjust
		} else {
			if (accelerate) {
				accelerate = false;
				this.setVelocity(this.getMaxVelocity());

			}
		}
	}

	/**
	 * Calculates angle to chase prey
	 * 
	 * @param prey to chase
	 * @return angle to steer to prey location
	 */
	private double calculateAngleTo(FlockingAgent prey) {
		// Calculate angle to prey for directional change
		return Math.toDegrees(
				Math.atan2(prey.getPositionY() - this.getPositionY(), prey.getPositionX() - this.getPositionX()));

	}

	@Override
	public void draw() {
		putPenUp();
		turn(90);
		move(7);
		putPenDown();
		for (int i = 0; i < 2; i++) {
			turn(240);
			move(7);
			move(7);
		}
		putPenUp();
		turn(240);
		move(7);
		turn(270);

	}

	@Override
	public int normaliseAngle(int angle) {
		angle = angle % 360;
		if (angle > 250) {
			angle -= 360;
		} else if (angle < -250) {
			angle += 360;
		}
		return angle;
	}

	@Override
	public void update(int deltaTime, List<Rectangle> obstcales) {
		Rectangle obstacle = detectObstcale(obstcales);
		if (obstacle != null) {
			int sepraAngle = this.avoidObstcal(obstacle);
			angularVelocity = sepraAngle * this.getAvoidAngularVelCoef() * 2.5;
		}
		float distance = (float) (velocity * deltaTime / MILLISECONDS_PER_SECOND);
		float angle = (float) (angularVelocity * 1.5 * deltaTime / MILLISECONDS_PER_SECOND);
		this.move((int) distance);
		this.turn((int) angle);
		draw();

	}

}
