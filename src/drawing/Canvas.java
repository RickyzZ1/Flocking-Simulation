package drawing;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Line2D;

import java.util.Collections;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import geometry.CartesianCoordinate;
import geometry.LineSegment;

/**
 * <h2>Canvas</h2> This class represents a canvas object that can be drawn to
 * with various line segments.
 * 
 * <P>
 * The list of LineSegment's is stored in a collection within the implementation
 * of the class. This collection is now synchronised to deal with the issue of
 * concurrent accesses to the collection.
 * 
 * <p>
 * WARNING: This class should NOT be modified in ANY way.
 */
public class Canvas extends JPanel {
	private static final long serialVersionUID = 1L;
	private int xSize, ySize;
	private List<LineSegment> lines;
	private final static int DEFAULT_X = 800;
	private final static int DEFAULT_Y = 600;
	// Added 2023 to allow easy changing of drawn line width - only integer widths allowed.
	public static final int DEFAULT_LINE_WIDTH = 3; 
	private int lineWidth = DEFAULT_LINE_WIDTH;

	/**
	 * Default constructor which produces a canvas of the default size of 800 x 600.
	 */
	public Canvas() {
		this(DEFAULT_X, DEFAULT_Y);
	}

	/**
	 * Constructor which produces a canvas of a specified size.
	 * 
	 * @param x Width of the canvas.
	 * @param y Height of the canvas.
	 */
	public Canvas(int x, int y) {
		xSize = x;
		ySize = y;
		setupCanvas();
		lines = Collections.synchronizedList(new ArrayList<LineSegment>());
	}

	private void setupCanvas() {
		setSize(xSize, ySize);
		setVisible(true);
		repaint();
	}

	/**
	 * <b>NB: You never need to call this method yourself.</b> It handles the
	 * drawing but is called automatically each time a line segment is drawn.
	 */
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // Smoother
																									// lines
		g2.setStroke(new BasicStroke(lineWidth));

		synchronized (lines) {
			for (LineSegment line : lines) {
				g2.draw(new Line2D.Double(line.getStartPoint().getX(), line.getStartPoint().getY(),
						line.getEndPoint().getX(), line.getEndPoint().getY()));
			}
		}
	}

	/**
	 * Draws a line between two CartesianCoordinates to the canvas.
	 * 
	 * @param startPoint Starting coordinate.
	 * @param endPoint   Ending coordinate.
	 */
	public void drawLineBetweenPoints(CartesianCoordinate startPoint, CartesianCoordinate endPoint) {
		synchronized (lines) {
			lines.add(new LineSegment(startPoint, endPoint));
		}
		repaint();
	}

	/**
	 * Draws a line segment to the canvas.
	 * 
	 * @param lineSegment The LineSegment to draw.
	 */
	public void drawLineSegment(LineSegment lineSegment) {
		synchronized (lines) {
			lines.add(lineSegment);
		}
		repaint();
	}

	/**
	 * Draws multiple line segments to the canvas.
	 * 
	 * @param lineSegments An array of LineSegment.
	 */
	public void drawLineSegments(LineSegment[] lineSegments) {
		for (LineSegment thisLineSegment : lineSegments) {
			synchronized (lines) {
				lines.add(thisLineSegment);
			}
		}
		repaint();
	}

	/**
	 * Removes the most recently added line from the drawing.
	 */
	public void removeMostRecentLine() {
		synchronized (lines) {
			lines.remove(lines.size() - 1);
		}
	}

	/**
	 * Clears the canvas of all drawing.
	 */
	public void clear() {
		synchronized (lines) {
			lines.clear();
		}
		repaint();
	}
	
	/**
	 * Get the currently set width of drawn lines.
	 * 
	 * @return the current width.
	 */
	public int getLineWidth() {
		return lineWidth;
	}

	/**
	 * Set the width of all drawn lines.
	 * 
	 * @param lineWidth the width of all drawn lines.
	 */
	public void setLineWidth(int lineWidth) {
		this.lineWidth = lineWidth;
	}


}
