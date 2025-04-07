package spaceWar;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;

public class bullet {
	private double x, y;
	private final Shape shape;
	private final Color color = new Color(255, 255, 255);
	private final float angle;
	private double size;
	private float speed = 0.5f;

	public bullet(double x, double y, float angle, double size, float speed) {

		x += Player.PLAYER_SIZE / 1.5 - size / 2;
		y += Player.PLAYER_SIZE / 1.5 - size / 2;
		this.x = x;
		this.y = y;
		this.angle = angle;
		this.size = size;
		this.speed = speed;
		shape = new Ellipse2D.Double(0, 0, size, size);
	}

	public void draw(Graphics2D g2) {
		AffineTransform oldTranform = g2.getTransform();
		g2.setColor(color);
		g2.translate(x, y);
		g2.fill(shape);
		g2.setTransform(oldTranform);

	}

	public double getSize() {
		return size;
	}

	public void setSize(double size) {
		this.size = size;
	}

	public double getX() {
		return x;
	}

	public double getCenterX() {
		return x + size / 2;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public double getCenterY() {
		return y + size / 2;
	}

	public void setY(double y) {
		this.y = y;
	}

	public boolean check(int witdh, int height) {
		if (x <= size || y <= size || x > witdh || y > height) {
			return false;// bay ra khoi man hinh;
		} else {
			return true;
		}
	}

	public void update() {
		x += Math.cos(Math.toRadians(angle)) * speed;
		y += Math.sin(Math.toRadians(angle)) * speed;

	}

	public Shape getShape() {
		return new Area(new Ellipse2D.Double(x, y, size, size));
	}

}
