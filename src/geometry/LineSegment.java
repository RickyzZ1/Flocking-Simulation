package geometry;

public class LineSegment {
	private CartesianCoordinate startPoint, endPoint;

	public LineSegment(CartesianCoordinate startPoint, CartesianCoordinate endPoint) {
		this.startPoint = startPoint;
		this.endPoint = endPoint;
	}

	public CartesianCoordinate getStartPoint() {
		return startPoint;
	}

	public CartesianCoordinate getEndPoint() {
		return endPoint;
	}

	public double length() {
		double detltaX = endPoint.getX() - startPoint.getX();
		double detltaY = endPoint.getY() - startPoint.getY();

		return Math.sqrt(detltaX * detltaX + detltaY * detltaY);
	}

	public String toString() {
		return super.toString() + startPoint + "->" + endPoint;

	}
}
