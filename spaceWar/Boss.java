package spaceWar;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.ImageIcon;

public class Boss extends Rocket {
	// Danh sách lưu trữ đạn của Boss
	private List<bullet> bossBullets;
	private Random random;
	private int shootInterval = 80; // Khoảng cách giữa các lần bắn (frame)
	private int shootCounter = 0; // Đếm frame để xác định thời điểm bắn
	// Constructor

	public Boss() {
		super(new Heath(100, 100)); // Boss có 100 HP();

		this.setHealth(new Heath(100, 100));
		this.setImageR(new ImageIcon(getClass().getResource("gameImage/poop2.png")).getImage());
		this.setSpeed(0.6f); // Tốc độ nhanh hơn Rocket thông thường
		this.bossBullets = new ArrayList<>();
		this.random = new Random();
	}

	public Boss(Heath h) {
		super(h); // Boss có 100 HP();
		this.setHealth(h);
		this.setImageR(new ImageIcon(getClass().getResource("gameImage/poop2.png")).getImage());
		this.setSpeed(0.6f); // Tốc độ nhanh hơn Rocket thông thường
		this.bossBullets = new ArrayList<>();
		this.random = new Random();
	}

	// Ghi đè phương thức update()
	@Override
	public void update() {
		// Di chuyển ngẫu nhiên
		setAngle(getAngle() + (random.nextFloat() - 0.5f) * 10); // Thay đổi góc ngẫu nhiên trong khoảng [-5, 5]
		if (getAngle() < 0)
			setAngle(getAngle() + 360); // Giữ góc trong khoảng [0, 360)
		if (getAngle() >= 360)
			setAngle(getAngle() - 360);

		// Tính toán vị trí tiếp theo
		// Tính vận tốc theo trục X và Y dựa trên góc và tốc độ
		double velocityX = Math.cos(Math.toRadians(getAngle())) * getSpeed();
		double velocityY = Math.sin(Math.toRadians(getAngle())) * getSpeed();

		// Tính toán vị trí tiếp theo
		double nextX = getX() + velocityX;
		double nextY = getY() + velocityY;

		// Kiểm tra biên và đảo ngược vận tốc nếu chạm biên
		if (nextX < 0) {
			// Chạm biên trái: đảo ngược vận tốc theo trục X
			velocityX = -velocityX;
			nextX = 0; // Đặt lại vị trí vào biên trái
		} else if (nextX > 800 - ROCKET_SIZE) {
			// Chạm biên phải: đảo ngược vận tốc theo trục X
			velocityX = -velocityX;
			nextX = 800 - ROCKET_SIZE; // Đặt lại vị trí vào biên phải
		}

		if (nextY < 0) {
			// Chạm biên trên: đảo ngược vận tốc theo trục Y
			velocityY = -velocityY;
			nextY = 0; // Đặt lại vị trí vào biên trên
		} else if (nextY > 600 - ROCKET_SIZE) {
			// Chạm biên dưới: đảo ngược vận tốc theo trục Y
			velocityY = -velocityY;
			nextY = 600 - ROCKET_SIZE; // Đặt lại vị trí vào biên dưới
		}

		// Cập nhật vị trí của boss
		setX(nextX);
		setY(nextY);
		// Logic bắn đạn
		shootCounter++;
		if (shootCounter >= shootInterval) {
			shootBullet(); // Bắn đạn
			shootCounter = 0; // Reset bộ đếm
		}

		// Cập nhật vị trí của từng đạn
		for (bullet BL : bossBullets) {
			BL.update();
		}
	}

//	 Phương thức bắn đạn
	private void shootBullet() {
		float bulletAngle = random.nextFloat() * 360; // Góc ngẫu nhiên cho đạn
		bullet bullet = new bullet(getX(), getY(), bulletAngle, 10, 2f); // Tạo đạn mới
		bossBullets.add(bullet); // Thêm đạn vào danh sách
	}

	// Ghi đè phương thức draw()
	@Override
	public void draw(Graphics2D g2) {
		super.draw(g2); // Vẽ Boss (kế thừa từ Rocket)
		// Vẽ từng đạn trong danh sách
		for (bullet bullet : bossBullets) {
			bullet.draw(g2);
		}
	}

	public List<bullet> getBossBullets() {
		return bossBullets;
	}
}