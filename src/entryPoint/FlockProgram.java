//Y3921824
package entryPoint;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import agent.FastFlockingAgent;
import agent.FlockingAgent;
import agent.Predator;
import drawing.Canvas;
import geometry.CartesianCoordinate;
import shape.Rectangle;
import tools.Utils;

public class FlockProgram {
	private JFrame frame;
	private final int WINDOW_X_SIZE = 1800;
	private final int WINDOW_Y_SIZE = 1000;
	private Canvas canvas;
	private JPanel lowerPanel;
	private JPanel higherPanel;
	private List<FlockingAgent> agents;
	private List<Predator> predators;
	private List<Rectangle> obstcals;
	private JLabel flockingAgentCount;
	private JLabel predatorCount;

	public FlockProgram() {
		super();
		setupFrame();
		setupGUI();
		setupAgents();
		setupObstacles();
	}

	/**
	 * Initialise JFrame
	 */
	private void setupFrame() {
		frame = new JFrame();
		frame.setTitle("Flocking Simulation");
		frame.setSize(WINDOW_X_SIZE, WINDOW_Y_SIZE);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	/**
	 * Initialise GUI
	 */
	private void setupGUI() {
		lowerPanel = new JPanel();
		higherPanel = new JPanel();
		canvas = new Canvas();

		frame.add(canvas, BorderLayout.CENTER);
		frame.add(lowerPanel, BorderLayout.SOUTH);
		frame.add(higherPanel, BorderLayout.NORTH);

		JButton addAgentButton = new JButton("Add Agent");
		JButton addFastAgentButton = new JButton("Add Fast Agent");
		JButton addPredatorButton = new JButton("Add Predator");
		JButton removeAgentButton = new JButton("Remove Agent");
		JButton removeFastAgentButton = new JButton("Remove Fast Agent");
		JButton removePredatorButton = new JButton("Remove Predator");
		JButton clear = new JButton("Clear");
		JSlider speed = new JSlider(0, 500, 100);
		JSlider cohesion = new JSlider(0, 1000, 200);
		JSlider alignment = new JSlider(0, 1000, 800);
		JSlider sepration = new JSlider(0, 1000, 500);

		JLabel speedLabel = new JLabel("Speed:");
		JLabel cohesionLabel = new JLabel("Cohesion");
		JLabel alignmentLabel = new JLabel("Alignment");
		JLabel seperationLabel = new JLabel("Speration");
		flockingAgentCount = new JLabel("FlockingAgent Count: ");
		predatorCount = new JLabel("Predator Count: ");

		lowerPanel.add(addAgentButton);
		lowerPanel.add(removeAgentButton);

		lowerPanel.add(speedLabel);
		lowerPanel.add(speed);

		lowerPanel.add(addFastAgentButton);
		lowerPanel.add(removeFastAgentButton);

		higherPanel.add(addPredatorButton);
		higherPanel.add(removePredatorButton);

		higherPanel.add(cohesionLabel);
		higherPanel.add(cohesion);

		higherPanel.add(alignmentLabel);
		higherPanel.add(alignment);

		higherPanel.add(seperationLabel);
		higherPanel.add(sepration);

		lowerPanel.add(flockingAgentCount);
		lowerPanel.add(predatorCount);

		higherPanel.add(clear);

		speed.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				synchronized (agents) {
					for (FlockingAgent agent : agents) {
						agent.setVelocity(speed.getValue());
						agent.setMaxVelocity(speed.getValue());
						agent.setAvoidAngularVelCoef(speed.getValue() / 100);
					}
				}
				synchronized (predators) {
					for (Predator predator : predators) {
						predator.setVelocity(speed.getValue());
						predator.setMaxVelocity(speed.getValue());
					}
				}
			}

		});

		cohesion.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				synchronized (agents) {
					for (FlockingAgent agent : agents) {
						agent.setCohesion(cohesion.getValue());
					}
				}

			}
		});

		alignment.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				synchronized (agents) {
					for (FlockingAgent agent : agents) {
						agent.setAlignment(alignment.getValue());
					}
				}

			}
		});

		sepration.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				synchronized (agents) {
					for (FlockingAgent agent : agents) {
						agent.setSepration(sepration.getValue());
					}
				}

			}
		});

		addAgentButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Agent Added!");
				int x = (int) (Math.random() * WINDOW_X_SIZE);
				int y = (int) (Math.random() * WINDOW_Y_SIZE);
				int z = (int) (Math.random() * 360);

				// If random location in obstacles range, regenerate random location
				while ((x > 80 && x < 240 && y < 200 && y > 80) || (x > 330 && x < 450 && y < 360 && y > 190)
						|| (x > 530 && x < 720 && y < 440 && y > 280)) {
					x = (int) (Math.random() * WINDOW_X_SIZE);
					y = (int) (Math.random() * WINDOW_Y_SIZE);
				}

				FlockingAgent newAgent = new FlockingAgent(canvas, x, y, z);
				newAgent.setVelocity(speed.getValue());
				newAgent.setAlignment(alignment.getValue());
				newAgent.setCohesion(cohesion.getValue());
				newAgent.setSepration(sepration.getValue());
				newAgent.setMaxVelocity(speed.getValue());
				agents.add(newAgent);
			}
		});

		addFastAgentButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Fast Agent Added!");
				int x = (int) (Math.random() * WINDOW_X_SIZE);
				int y = (int) (Math.random() * WINDOW_Y_SIZE);
				int z = (int) (Math.random() * 360);

				// If random location in obstacles range, regenerate random location
				while ((x > 80 && x < 240 && y < 200 && y > 80) || (x > 330 && x < 450 && y < 360 && y > 190)
						|| (x > 530 && x < 720 && y < 440 && y > 280)) {
					x = (int) (Math.random() * WINDOW_X_SIZE);
					y = (int) (Math.random() * WINDOW_Y_SIZE);
				}

				FastFlockingAgent newAgent = new FastFlockingAgent(canvas, x, y, z);
				newAgent.setVelocity(speed.getValue());
				newAgent.setMaxVelocity(speed.getValue());
				newAgent.setAlignment(alignment.getValue());
				newAgent.setCohesion(cohesion.getValue());
				newAgent.setSepration(sepration.getValue());
				agents.add(newAgent);
			}
		});

		addPredatorButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Predator Added!");
				int x = (int) (Math.random() * WINDOW_X_SIZE);
				int y = (int) (Math.random() * WINDOW_Y_SIZE);
				int z = (int) (Math.random() * 360);

				// If random location in obstacles range, regenerate random location
				while ((x > 80 && x < 240 && y < 200 && y > 80) || (x > 330 && x < 450 && y < 360 && y > 190)
						|| (x > 530 && x < 720 && y < 440 && y > 280)) {
					x = (int) (Math.random() * WINDOW_X_SIZE);
					y = (int) (Math.random() * WINDOW_Y_SIZE);
				}

				Predator newPredator = new Predator(canvas, x, y, z);
				newPredator.setVelocity(speed.getValue());
				newPredator.setMaxVelocity(speed.getValue());
				predators.add(newPredator);
			}
		});

		removeAgentButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (agents.size() <= 0) {
					System.out.println("No More Agent to Remove");
				} else {
					System.out.println("Agent Removed");
					synchronized (agents) {
						int index = agents.size() - 1;
						FlockingAgent agent = agents.get(index);
						agent.undraw();
						agents.remove(index);
					}

				}
			}
		});

		removeFastAgentButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean removed = false;
				int FastFlockingAgentCount = 0;
				int i = 1;
				if (agents.size() <= 0) {
					System.out.println("No More Fast Agent to Remove");
				} else {
					for (FlockingAgent agent : agents) {
						// Used Polymorphism,check if it is FastFlockingAgent from the array
						if (agent instanceof FastFlockingAgent) {
							FastFlockingAgentCount++;

						}

					}
					if (FastFlockingAgentCount <= 0) {
						System.out.println("No More Fast Agent to Remove");

					} else {
						System.out.println("Fast Agent Removed");
						while (!removed) {
							synchronized (agents) {
								// Get the index of the FastFlocking agent to remove
								int index = agents.size() - i;
								FlockingAgent agent = agents.get(index);
								if (agent instanceof FastFlockingAgent) {
									agent.undraw();
									agents.remove(index);
									removed = true;
								} else {
									i++;
								}
							}
						}

					}
				}
			}
		});

		removePredatorButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (predators.size() <= 0) {
					System.out.println("No More Predator to Remove");
				} else {
					System.out.println("Predator Removed");
					synchronized (predators) {
						// Get the index of the Predator to remove
						int index = predators.size() - 1;
						Predator predator = predators.get(index);
						predator.undraw();
						predators.remove(index);
					}

				}
			}
		});

		clear.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (agents.size() <= 0 && predators.size() <= 0) {
					System.out.println("The frame is already clear");
				} else {
					synchronized (agents) {
						for (FlockingAgent agent : agents) {
							agent.undraw();

						}
						agents.clear();
					}
					synchronized (predators) {
						for (Predator predator : predators) {
							predator.undraw();

						}
						predators.clear();
					}
					System.out.println("Clear Successful");

				}
			}
		});

		frame.validate();
	}

	/**
	 * Initialise agents
	 */
	private void setupAgents() {
		agents = Collections.synchronizedList(new ArrayList<>());
		for (int i = 0; i < 30; i++) {
			int x = (int) (Math.random() * WINDOW_X_SIZE);
			int y = (int) (Math.random() * WINDOW_Y_SIZE);
			int z = (int) (Math.random() * 360);

			// If random location in obstacles range, regenerate random location
			while ((x > 80 && x < 240 && y < 200 && y > 80) || (x > 330 && x < 450 && y < 360 && y > 190)
					|| (x > 530 && x < 720 && y < 440 && y > 280)) {
				x = (int) (Math.random() * WINDOW_X_SIZE);
				y = (int) (Math.random() * WINDOW_Y_SIZE);
			}

			agents.add(new FlockingAgent(canvas, x, y, z));
		}
		predators = Collections.synchronizedList(new ArrayList<>());
		for (int i = 0; i < 0; i++) {
			int x = (int) (Math.random() * WINDOW_X_SIZE);
			int y = (int) (Math.random() * WINDOW_Y_SIZE);
			int z = (int) (Math.random() * 360);

			// If random location in obstacles range, regenerate random location
			while ((x > 80 && x < 240 && y < 200 && y > 80) || (x > 330 && x < 450 && y < 360 && y > 190)
					|| (x > 530 && x < 720 && y < 440 && y > 280)) {
				x = (int) (Math.random() * WINDOW_X_SIZE);
				y = (int) (Math.random() * WINDOW_Y_SIZE);
			}

			predators.add(new Predator(canvas, x, y, z));
		}

	}

	/**
	 * Initialise obstacles
	 */
	private void setupObstacles() {
		obstcals = Collections.synchronizedList(new ArrayList<>());
		CartesianCoordinate obstcaleStartpoint1 = new CartesianCoordinate(100, 100);
		CartesianCoordinate obstcaleStartpoint2 = new CartesianCoordinate(350, 200);
		CartesianCoordinate obstcaleStartpoint3 = new CartesianCoordinate(550, 300);
		obstcals.add(new Rectangle(canvas, obstcaleStartpoint1, 120, 80));
		obstcals.add(new Rectangle(canvas, obstcaleStartpoint2, 80, 150));
		obstcals.add(new Rectangle(canvas, obstcaleStartpoint3, 150, 120));
	}

	/**
	 * Gameloop
	 */
	private void gameLoop() {
		int deltaTime = 10;
		boolean continueRunning = true;
		// Game loop.
		while (continueRunning) {
			synchronized (obstcals) {
				for (Rectangle obstcal : obstcals) {
					obstcal.draw();
				}
			}
			synchronized (predators) {
				for (Predator predator : predators) {
					predator.update(deltaTime, obstcals);
					predator.hunt(agents, predators);
					predator.wrapPosition(canvas.getWidth(), canvas.getHeight());
				}
			}
			synchronized (agents) {
				for (FlockingAgent agent : agents) {
					agent.update(deltaTime, obstcals);
					agent.flock(agents, predators);
					agent.wrapPosition(canvas.getWidth(), canvas.getHeight());
				}
			}
			Utils.pause(deltaTime);

			// Update count information
			predatorCount.setText(" Predator Count:  " + predators.size());
			flockingAgentCount.setText(" FlockingAgent Count: " + agents.size());

			// Performance improvement instead use undraw for each agent
			canvas.clear();

		}

	}

	/*
	 * Main Method
	 */
	public static void main(String[] args) {
		System.out.println("Running FlockProgram...");
		FlockProgram flockProgram = new FlockProgram();
		flockProgram.gameLoop();

	}

}
