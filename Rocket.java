package spaceWar;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Path2D;

import javax.swing.ImageIcon;

public class Rocket extends HeathRender {

	public static final double ROCKET_SIZE = 50;
	private double x, y;
	private float speed = 0.4f;
	private float angle = 0;
	private Image imageR;

	private final Area rocketShap;

	private float rotationAngle = 0; // Góc xoay hiện tại (bắt đầu từ 0 độ)
	private final float rotationSpeed = 0.1f; // Tốc độ xoay (độ mỗi frame)

	public Rocket() {
		super(new Heath(20, 20));
		this.imageR = new ImageIcon(getClass().getResource("gameImage/poop1.png")).getImage();
		Path2D p2 = new Path2D.Double();
		p2.moveTo(0, ROCKET_SIZE);
		p2.lineTo(25, 0); // Điểm 1: Đỉnh trên
		p2.lineTo(50, 50); // Điểm 3: Góc dưới phải
		p2.closePath();
		rocketShap = new Area(p2);

	}

	public Rocket(Heath health) {
		super(health);
		this.imageR = new ImageIcon(getClass().getResource("gameImage/poop1.png")).getImage();
		Path2D p2 = new Path2D.Double();
		p2.moveTo(0, ROCKET_SIZE);
		p2.lineTo(25, 0); // Điểm 1: Đỉnh trên
		p2.lineTo(50, 50); // Điểm 3: Góc dưới phải
		p2.closePath();
		rocketShap = new Area(p2);

	}

	public void setImageR(Image imageR) {
		this.imageR = imageR;
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

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
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

	public void setAngle(float angle) {
		this.angle = angle;
	}

	public void update() {
		x += Math.cos(Math.toRadians(angle)) * speed;
		y += Math.sin(Math.toRadians(angle)) * speed;

		rotationAngle += rotationSpeed; // Tăng góc xoay
		if (rotationAngle >= 359) {
			rotationAngle -= 359; // Đặt lại góc về 0 nếu vượt quá 360 độ
		}

	}

	public void draw(Graphics2D g2) {
		AffineTransform oldTranform = g2.getTransform();// Lưu trạng thái ban đầu
		g2.translate(x, y);// Di chuyển đến vị trí của rocket

		// Tạo phép xoay quanh tâm hình ảnh
		AffineTransform tran = new AffineTransform();
//		tran.rotate(Math.toRadians(angle + 0), ROCKET_SIZE / 2, ROCKET_SIZE / 2);// xuay quanh tam
		tran.rotate(Math.toRadians(rotationAngle + 0), ROCKET_SIZE / 2, ROCKET_SIZE / 2);// xuay quanh tam

		g2.drawImage(getImageR(), tran, null);

		Shape shap = getShape();

		this.hpRender(g2, shap, y);

		g2.setTransform(oldTranform);

		// test
//		g2.setColor(Color.red);
//		g2.draw(shap);
//		g2.draw(shap.getBounds2D());

	}

	public Area getShape() {
		AffineTransform afx = new AffineTransform();
		afx.translate(x, y);
		afx.rotate(Math.toRadians(angle), ROCKET_SIZE / 2, ROCKET_SIZE / 2);
		return new Area(afx.createTransformedShape(rocketShap));
	}

	public boolean check(int witdh, int height) {
		Rectangle size = getShape().getBounds();
		if (x <= -size.getWidth() || y <= -size.getHeight() || x > witdh || y > height) {
			return false;
		} else
			return true;

	}

	public Image getImageR() {
		return imageR;
	}

	public void setHealth(Heath health) {

	}

}
