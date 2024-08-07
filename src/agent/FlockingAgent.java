package agent;

import java.util.List;

import drawing.Canvas;

public class FlockingAgent extends DynamicAgent {
	private int directionChangeCounter;
	private static final int MAX_COUNTER_VALUE = 100;
	private static final int MIN_COUNTER_VALUE = 50;
	private static final int MAX_DIRECTION_CHANGE = 180;
	private boolean isEscaping = false; // Flag to determine agents are escaping from predator
	private int escapeVelocity = 100;
	private double radiusToFlock = 150;
	private double radiusToSep = 50;
	private int Kc = 200; // Cohesion parameter
	private int Ka = 800; // Alignment parameter
	private int Ks = 500; // Separation parameter
	private double alignAngle = 0;
	private double coheAngle = 0;
	private double sepraAngle = 0;

	/**
	 * Constructor
	 */
	public FlockingAgent(Canvas myCanvas) {
		super(myCanvas);
		directionChangeCounter = (int) (Math.random() * (MAX_COUNTER_VALUE - MIN_COUNTER_VALUE)) + MIN_COUNTER_VALUE;
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
	public FlockingAgent(Canvas myCanvas, int xPosition, int yPosition, int direction) {
		super(myCanvas, xPosition, yPosition, direction);
		directionChangeCounter = (int) (Math.random() * (MAX_COUNTER_VALUE - MIN_COUNTER_VALUE)) + MIN_COUNTER_VALUE;
	}

	/**
	 * Combines all the flocking rules, but run away from predators first ignore the
	 * flocking rules, if a predator is close
	 */
	public void flock(List<FlockingAgent> agents, List<Predator> predators) {
		Predator target = detectPredator(predators);
		if (target != null) {
			// Separates away from predator
			sepraAngle = this.avoidPredator(agents, target);
			this.setAngualrVelocity(sepraAngle * this.getAvoidAngularVelCoef() * 3);
			isEscaping = true;
			if (isEscaping) {
				// Accelerate to run away from predator
				this.setVelocity(this.getMaxVelocity() + escapeVelocity);
			}

		} else {
			if (isEscaping) {
				// When escaped, set back to original velocity
				this.setVelocity(this.getMaxVelocity());
				isEscaping = false;
			}
			alignAngle = this.align(agents);
			coheAngle = this.cohe(agents);
			sepraAngle = this.sepration(agents);
			double angleDiff = coheAngle + sepraAngle;

			// Avoid unnecessary small angle change causing wibbling
			if (angleDiff < 20 && angleDiff > -20) {
				coheAngle = 0;
				sepraAngle = 0;
			}

			// Calculation of the total angle adjustment by three flocking rules, and varies
			// each weight in calculation by using parameters
			double flockAngle = (alignAngle * (Ka / 100)) + (coheAngle * (Kc / 100)) + (sepraAngle * (Ks / 100));

			// If no flocking behaviour give a random angle to agent to look for other
			// agents base on the counters, if it has flocking angle, this will not be
			// executed
			if (flockAngle == 0) {
				directionChangeCounter--;
				if (directionChangeCounter <= 0 && flockAngle == 0) {
					int randomChange = (int) ((Math.random() * (2 * MAX_DIRECTION_CHANGE + 1)) - MAX_DIRECTION_CHANGE);
					this.setAngualrVelocity(randomChange);
					directionChangeCounter = (int) (Math.random() * (MAX_COUNTER_VALUE - MIN_COUNTER_VALUE))
							+ MIN_COUNTER_VALUE;
				}
			} else {
				// Limit angle between -180 to 180, make sure there is not any over rotate
				flockAngle = clamp(flockAngle, -180, 180);
				this.setAngualrVelocity(flockAngle);
			}

		}

	}

	/**
	 * Alignment rule, adds up angle of all agents within a distance range, returns
	 * a average angle
	 * 
	 * @param agents
	 * @return angle to alignment
	 */
	public double align(List<FlockingAgent> agents) {
		double xComponentSum = 0; // Sum x component of the angle
		double yComponentSum = 0; // Sum y component of the angle
		int count = 0;

		// For every agents pass in, add up angle components if within the distance
		// range, break in angle component to avoid bug like adding -120 and 120 is 0
		for (FlockingAgent agent : agents) {
			if (agent != this && this.distanceTo(agent.getPos()) <= radiusToFlock) {
				xComponentSum += Math.cos(Math.toRadians(agent.getAngle()));
				yComponentSum += Math.sin(Math.toRadians(agent.getAngle()));
				count++;
			}
		}

		if (count > 0) {
			return calculateSteeringAngle(xComponentSum, yComponentSum, count);
		}
		return 0;
	}

	/**
	 * Cohesion rule, calculates local centre within the distance range, then steer
	 * agents angle to local centre position
	 * 
	 * @param agents
	 * @return angle to cohesion
	 */
	public double cohe(List<FlockingAgent> agents) {
		double distance = 0;
		int count = 0;
		int sumPosX = 0; // Local centre x component
		int sumPosY = 0; // Local centre y component
		// For every agents pass in, add up position if within the distance range
		for (FlockingAgent agent : agents) {
			if (agent != this) {
				distance = this.distanceTo(agent.getPos());
				if (distance <= radiusToFlock && distance >= 40) {
					sumPosX += agent.getPositionX();
					sumPosY += agent.getPositionY();
					count++;
				}
			}
		}
		if (count > 0) {
			// Multiply by count due to I divide count in calculateSteeringAngle Method
			// where the logic for this should be get the local centre location first then
			// take away this agent location;
			sumPosX += this.getPositionX();
			sumPosY += this.getPositionY();
			count++;
			double XDiff = sumPosX - count * this.getPositionX();
			double YDiff = sumPosY - count * this.getPositionY();
			return calculateSteeringAngle(XDiff, YDiff, count);

		}

		return 0;
	}

	/**
	 * Separation rule, calculates angle to avoid collide with other agent base on
	 * the distance to the other agent, with in the detection radius
	 * 
	 * @param agents
	 * @return angle to separation
	 */
	public double sepration(List<FlockingAgent> agents) {
		double distance = 0;
		int count = 0;
		double sumPosX = 0;
		double sumPosY = 0;
		for (FlockingAgent agent : agents) {
			if (agent != this) {
				double dx = this.getPositionX() - agent.getPositionX();
				double dy = this.getPositionY() - agent.getPositionY();
				distance = this.distanceTo(agent.getPos());

				// normalize the directional components (dx and dy). By dividing each component
				// by the distance, the closer it is the bigger directional components it is
				// which means the closer it is the angle to steer away is bigger
				if (distance > 0 && distance <= radiusToSep) {
					sumPosX += dx / Math.pow(distance, 2);
					sumPosY += dy / Math.pow(distance, 2);
					count++;

				}
			}

		}
		if (count > 0) {
			return calculateSteeringAngle(sumPosX, sumPosY, count);
		}

		return 0;
	}

	/**
	 * Limits the angle between min and max
	 * 
	 * Return min if value is small than min,return max if value is bigger than
	 * max,return value if it is in between
	 * 
	 * @param value to limit
	 * @param min   value
	 * @param max   value
	 * @return limited value
	 */
	private double clamp(double value, double min, double max) {
		return Math.max(min, Math.min(max, value));
	}

	/**
	 * Calculates the steering angle for three flocking rule, by pass in location or
	 * angle component and count of agents
	 *
	 * @param xComponent
	 * @param yComponent
	 * @param count agents in range
	 * @return angle to steer
	 */
	private double calculateSteeringAngle(double xComponent, double yComponent, int count) {
		if (count > 0) {
			double avgX = xComponent / count;
			double avgY = yComponent / count;
			double targetAngle = Math.toDegrees(Math.atan2(avgY, avgX));
			double currentAngle = this.getAngle();
			return normaliseAngle((int) (targetAngle - currentAngle));
		}
		return 0;
	}

	/**
	 * Return the predator if it is closer than the specified range
	 *
	 * @param predators to detect
	 * @return predator to detected
	 */
	private Predator detectPredator(List<Predator> predators) {
		for (Predator predator : predators) {
			double distance = distanceTo(predator.getPos());
			if (distance <= 100) {
				return predator; // Return the first detected predator

			}
		}
		return null;
	}

	/**
	 * Avoid the predator using the same mathematical logic of separation rule, by
	 * using predator location
	 * 
	 * @param agents need avoid predator
	 * @param predator to avoid
	 * @return angle to steer away from predator
	 */
	private double avoidPredator(List<FlockingAgent> agents, Predator predator) {
		// Get the relative position of the predator from the agent's position
		double dx = this.getPositionX() - predator.getPositionX();
		double dy = this.getPositionY() - predator.getPositionY();
		double targetAngle = Math.toDegrees(Math.atan2(dy, dx));
		double currentAngle = this.getAngle();

		// Calculate the angle difference needed to turn away from the predator
		double angleToPos = normaliseAngle((int) (targetAngle - currentAngle));

		return angleToPos;
	}

	/*
	 * Setters For Flocking parameters
	 */
	public void setCohesion(int value) {
		this.Kc = value;
	}

	public void setAlignment(int value) {
		this.Ka = value;
	}

	public void setSepration(int value) {
		this.Ks = value;
	}
}
