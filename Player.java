package spaceWar;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Path2D;

import javax.swing.ImageIcon;

public class Player extends HeathRender {
	public static final double PLAYER_SIZE = 64;
	private double x, y;
	private float angle = 0;
	private final Image image;
	private final Image image_speed;
	private final float MAX_SPEED = 1f;
	private float speed = 0f;
	private boolean speed_up;

	private final Area playerShap;
	private boolean alive = true;

	public Player() {
		super(new Heath(50, 50));
		this.image = new ImageIcon(getClass().getResource("gameImage/grayFly.png")).getImage();
		this.image_speed = new ImageIcon(getClass().getResource("gameImage/yellowFly.png")).getImage();
		Path2D p = new Path2D.Double();
		p.moveTo(PLAYER_SIZE / 3, PLAYER_SIZE); // Điểm dưới bên trái
		p.lineTo(62, PLAYER_SIZE); // Điểm dưới bên phải
		p.lineTo(PLAYER_SIZE / 1.1, 0); // Điểm trên bên phải
		p.lineTo(26, 0); // Điểm trên bên trái
		p.lineTo(PLAYER_SIZE / 3, PLAYER_SIZE); // Quay lại điểm đầu
		playerShap = new Area(p);

	}

	public void changeAngle(float ang) {
		if (ang < 0) {
			ang = 359;
		} else if (ang > 359) {
			ang = 0;
		}
		this.angle = ang;
	}

	public void changeLocation(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public float getAngle() {
		return angle;
	}

	public void draw(Graphics2D g2) {
		AffineTransform oldTranform = g2.getTransform();
		g2.translate(x, y);
//		g2.drawImage(image, 0, 0, null);

		AffineTransform Tran = new AffineTransform();
		Tran.rotate(Math.toRadians(angle + 45), PLAYER_SIZE / 1.5, PLAYER_SIZE / 1.5);// xuay quanh tam
		g2.drawImage(speed_up ? image_speed : image, Tran, null);

		hpRender(g2, getShape(), y);

		g2.setTransform(oldTranform);
		// tét
//		g2.setColor(Color.red);
//		g2.draw(getShape());
//		g2.draw(getShape().getBounds());

	}

	public void speedUp() {
		speed_up = true;
		if (speed > MAX_SPEED) {
			speed = MAX_SPEED;
		} else {
			speed += 0.01f;
		}

	}

	public void speedDown() {
		speed_up = false;
		if (speed <= 0) {
			speed = 0;
		} else {
			speed -= 0.004f;
		}
	}

	public void update() {
		x += Math.cos(Math.toRadians(angle)) * speed;
		y += Math.sin(Math.toRadians(angle)) * speed;

	}

	public Area getShape() {
		AffineTransform afx = new AffineTransform();
		afx.translate(x, y);
		afx.rotate(Math.toRadians(angle), PLAYER_SIZE / 2, PLAYER_SIZE / 2);
		return new Area(afx.createTransformedShape(playerShap));
	}

	public boolean isAlive() {
		return alive;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	public void resetLive() {
		alive = true;
		resetHP();
		angle = 0;
		speed = 0;
	}

}
