package spaceWar;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JComponent;

public class panelGame extends JComponent {

	private int witdh, height;
	// game fps
	private final int FPS = 60;
	private final int TARGET_TIME = 1000000000 / FPS; // 16,666,666.666 nanoSec
	private Thread thread;
	private boolean start = true;
	private int score = 0;
	private int maxScore = 0;
	private boolean mus = true;
	private final int BOSS_SCORE_INTERVAL = 1; // Khoảng cách điểm để boss xuất hiện
	private int lastBossScore = 0; // Điểm số tại lần cuối boss xuất hiện

	private int shotTime = 0;
	private int maxShot = 3; // Giới hạn số lượng đạn tối đa
	private int currentShot = 0; // Số lượng đạn hiện tại

	// game object
	private Player player;
	private List<bullet> bullets;
	private List<Rocket> rockets;
	private List<Effect> boomEffects;

	private Sound sound;
	private background Background;

	// key
	private key_controller key;

	private Graphics2D g2;
	private BufferedImage image;

	private boolean firstRun = true;
	// Thêm biến để theo dõi trạng thái mute
	final boolean[] isMuted = { false }; // Sử dụng mảng để thay đổi giá trị trong lambda
	final float defaultVolume = -5.0f; // Âm lượng mặc định khi unmute (có thể điều chỉnh)

	public void start() {
		witdh = this.getWidth();
		height = this.getHeight();

		image = new BufferedImage(witdh, height, BufferedImage.TYPE_INT_ARGB);
		g2 = image.createGraphics();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);// khu rang cua, ...
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

		thread = new Thread(new Runnable() {

			@Override
			public void run() {
				while (start) {
					long startTime = System.nanoTime();
					drawBackGround();
					drawGame();
					render();
					long time = System.nanoTime() - startTime;
					if (time < TARGET_TIME) { // delay the loop
						long sleep = (TARGET_TIME - time) / 1000000;// nano -> mili sec
						sleep(sleep);
//						System.out.println(sleep);
					}
//					sleep(TARGET_TIME);// test
				}

			}
		});

		initObject();
		initKeyboard();
		initBullet();

		thread.start();

	}

	private void addRocket() {
		Random ran = new Random();
		int locationY = ran.nextInt(height - 50) + 25;
		Rocket rocket = new Rocket();
		rocket.changeLocation(0, locationY);
		rocket.changeAngle(0);
		rockets.add(rocket);
		int locationY2 = ran.nextInt(height - 50) + 25;
		Rocket rocket2 = new Rocket();
		rocket2.changeLocation(witdh, locationY2);
		rocket2.changeAngle(180);
		rockets.add(rocket2);
		if (score % BOSS_SCORE_INTERVAL == 0 && score > lastBossScore) {
			Boss boss = new Boss(
					new Heath(100 + 5 * (score / BOSS_SCORE_INTERVAL), 100 + 5 * (score / BOSS_SCORE_INTERVAL)));
			boss.changeLocation(0, locationY);
			boss.changeAngle(0);
			rockets.add(boss);
			lastBossScore = score; // Cập nhật điểm số cuối cùng mà boss xuất hiện
		}

	}

	private void initObject() {

		sound = new Sound();
		sound.playBackgroundMusic();
		// Khởi tạo background một lần duy nhất
		Background = new background("gameImage/summerlandscape.png");

		player = new Player();
		player.changeLocation(150, 150);
		rockets = new ArrayList<>();
		boomEffects = new ArrayList<>();
		new Thread(new Runnable() {

			@Override
			public void run() {
				while (start) {
					addRocket();
					sleep(3000);
				}

			}
		}).start();
	}

	private void initBullet() {
		bullets = new ArrayList<>();
		new Thread(new Runnable() {

			@Override
			public void run() {
				while (start) {
					for (int i = 0; i < bullets.size(); i++) {
						bullet BL = bullets.get(i);
						if (BL != null) {
							BL.update();
							checkBullet(BL);
							if (!BL.check(witdh, height)) {
								bullets.remove(BL);
								currentShot--;
							}
						} else {
							bullets.remove(BL);
						}
					}
					// effect
					for (int i = 0; i < boomEffects.size(); i++) {
						Effect boomeffect = boomEffects.get(i);
						if (boomeffect != null) {
							boomeffect.update();
							if (!boomeffect.check()) {
								boomEffects.remove(boomeffect);

							}
						} else {
							boomEffects.remove(boomeffect);
						}
					}
					sleep(5);
				}

			}
		}).start();
	}

	private void initKeyboard() {
		key = new key_controller();
		requestFocus();
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_A) {
					key.setKey_left(true);
				} else if (e.getKeyCode() == KeyEvent.VK_D) {
					key.setKey_right(true);
				} else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
					key.setKey_space(true);
				} else if (e.getKeyCode() == KeyEvent.VK_J) {
					key.setKey_j(true);

				} else if (e.getKeyCode() == KeyEvent.VK_K) {
					key.setKey_k(true);

				} else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					key.setKey_enter(true);
					if (firstRun)
						firstRun = false;
				} else if (e.getKeyCode() == KeyEvent.VK_M) {
					key.setKey_m(true);
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {

				if (e.getKeyCode() == KeyEvent.VK_A) {
					key.setKey_left(false);
				} else if (e.getKeyCode() == KeyEvent.VK_D) {
					key.setKey_right(false);
				} else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
					key.setKey_space(false);
				} else if (e.getKeyCode() == KeyEvent.VK_J) {
					key.setKey_j(false);
				} else if (e.getKeyCode() == KeyEvent.VK_K) {
					key.setKey_k(false);
				} else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					key.setKey_enter(false);
				} else if (e.getKeyCode() == KeyEvent.VK_M) {
					key.setKey_m(false);
				}

			}
		});

		new Thread(new Runnable() {

			@Override
			public void run() {
				float s = 0.5f;
				while (start) {
					if (key.isKey_m()) {
						if (!isMuted[0]) {
							// Mute: Đặt âm lượng về -80.0f (tắt tiếng)
							sound.setBackgroundVolume(-80.0f);
							isMuted[0] = true;
						} else {
							// Unmute: Đặt lại âm lượng mặc định
							sound.setBackgroundVolume(defaultVolume);
							isMuted[0] = false;
						}
					}

					if (player.isAlive()) {
						float ang = player.getAngle();

						if (key.isKey_left()) {
							ang -= s;
						}
						if (key.isKey_right()) {
							ang += s;
						}
						if (key.isKey_space()) {
							player.speedUp();

						} else {
							player.speedDown();
						}
						player.update();
						if (key.isKey_j() || key.isKey_k()) {
							if (shotTime == 0 && currentShot < maxShot) {// Kiểm tra giới hạn đạn
								if (key.isKey_j()) {
									bullets.add(0, new bullet(player.getX(), player.getY(), player.getAngle(), 5, 2f));
								} else {
									bullets.add(0,
											new bullet(player.getX(), player.getY(), player.getAngle(), 15, 1.5f));
								}
								sound.SoundShoot();
								currentShot++;
							}
							shotTime++;

							if (shotTime >= 15) {
								shotTime = 0;// Reset để cho phép bắn tiếp
							}
						} else {
							shotTime = 0;// Reset khi không nhấn phím
						}
						player.changeAngle(ang);
					} else {
						if (key.isKey_enter()) {
							resetGame();
						}
					}

					for (int i = 0; i < rockets.size(); i++) {
						Rocket rocket = rockets.get(i);
						if (rocket != null) {
							rocket.update();
							if (!rocket.check(witdh, height)) {
								rockets.remove(rocket);

							} else {
								if (player.isAlive()) {
									checkPlayer(rocket);
								}
							}
						}
					}

					sleep(5);
				}
			}
		}).start();

	}

	private void drawBackGround() {
//		g2.setBackground(new Color(30, 30, 30));
//		g2.setColor(new Color(30, 30, 30));
//
//		g2.fillRect(0, 0, witdh, height);
		if (Background != null) {
//		Background = new background("gameImage/summerlandscape.png");
			Background.draw(g2, 800, 600);
		}

	}

	private void drawGame() {

		if (player.isAlive()) {
			player.draw(g2);

			// bullet
			for (int i = 0; i < bullets.size(); i++) {
				bullet BL = bullets.get(i);
				if (BL != null) {
					BL.draw(g2);
//					BL.update();
				}
			}
		}

		// enemy
		for (int i = 0; i < rockets.size(); i++) {
			Rocket rocket = rockets.get(i);
			if (rocket != null) {
				rocket.draw(g2);
			}
		}
		// effect
		for (int i = 0; i < boomEffects.size(); i++) {
			Effect boom = boomEffects.get(i);
			if (boom != null) {
				boom.draw(g2);
			}
		}

		g2.setColor(Color.white);
		g2.setFont(getFont().deriveFont(Font.BOLD, 15f));
		g2.drawString("Score : " + score, 10, 20);
		g2.drawString("max_Score : " + maxScore, 90, 20);
		GAMEhint(g2);

		if (!player.isAlive()) {
			String text = "GAME OVER";
			String textKey = "Press key enter to Continue...";
			g2.setFont(getFont().deriveFont(Font.BOLD, 50f));
			FontMetrics fm = g2.getFontMetrics();
			Rectangle2D r2 = fm.getStringBounds(text, g2);
			double textWidth = r2.getWidth();
			double textHeight = r2.getHeight();
			double x = (witdh - textWidth) / 2;
			double y = (height - textHeight) / 2;
			g2.drawString(text, (int) x, (int) y + fm.getAscent());
			g2.setFont(getFont().deriveFont(Font.BOLD, 15f));
			fm = g2.getFontMetrics();
			r2 = fm.getStringBounds(textKey, g2);
			textWidth = r2.getWidth();
			textHeight = r2.getHeight();
			x = (witdh - textWidth) / 2;
			y = (height - textHeight) / 2;
			g2.drawString(textKey, (int) x, (int) y + fm.getAscent() + 50);

		}
		checkBossBullets();

	}

	private void render() {
		Graphics g = getGraphics();
		g.drawImage(image, 0, 0, null);
		g.dispose();
	}

	private void sleep(long speed) {
		try {
			Thread.sleep(speed);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void checkBullet(bullet BL) {
		for (int i = 0; i < rockets.size(); i++) {
			Rocket rocket = rockets.get(i);
			if (rocket != null) {
				Area area = new Area(BL.getShape());
				area.intersect(rocket.getShape());

				if (!area.isEmpty()) {
					boomEffects.add(
							new Effect(BL.getCenterX(), BL.getCenterY(), 3, 5, 60, 0.5f, new Color(250, 207, 105)));
					if (!rocket.updateHP(BL.getSize())) {

						score++;
						updateScore();

						rockets.remove(rocket);
						sound.SoundDestroy();
						double x = rocket.getX() + rocket.ROCKET_SIZE / 2;
						double y = rocket.getY() + rocket.ROCKET_SIZE / 2;
						boomEffects.add(new Effect(x, y, 5, 5, 75, 0.3f, new Color(32, 170, 169)));
						boomEffects.add(new Effect(x, y, 7, 7, 85, 0.25f, new Color(30, 150, 139)));
						boomEffects.add(new Effect(x, y, 5, 5, 75, 0.2f, new Color(240, 107, 159)));

					} else {
						sound.SoundHit();
					}
					bullets.remove(BL);
					currentShot--;
				}
			}
		}

	}

	private void checkBossBullets() {
		for (int i = 0; i < rockets.size(); i++) {
			Rocket rocket = rockets.get(i);
			if (rocket instanceof Boss) { // Kiểm tra nếu là Boss
				Boss boss = (Boss) rocket;
				List<bullet> bossBullets = boss.getBossBullets();
				for (int j = 0; j < bossBullets.size(); j++) {
					bullet bullet = bossBullets.get(j);
					if (bullet != null) {
						// Kiểm tra va chạm với Player
						Area area = new Area(bullet.getShape());
						area.intersect(player.getShape());
						if (!area.isEmpty()) { // Nếu có va chạm
//							player.updateHP(bullet.getSize());// Giảm máu Player
							if (!player.updateHP(bullet.getSize())) {
								sound.SoundDie();
								player.setAlive(false);
								double x = player.getX() + player.PLAYER_SIZE / 2;
								double y = player.getY() + player.PLAYER_SIZE / 2;
								boomEffects.add(new Effect(x, y, 5, 5, 75, 0.3f, new Color(32, 170, 169)));
								boomEffects.add(new Effect(x, y, 7, 7, 85, 0.25f, new Color(30, 150, 139)));
								boomEffects.add(new Effect(x, y, 5, 5, 75, 0.2f, new Color(240, 107, 159)));
							}
							bossBullets.remove(j); // Xóa đạn
							j--; // Giảm chỉ số để không bỏ sót phần tử

						}
//						else if (!bullet.check(WIDTH, HEIGHT)) { // Giả sử check() kiểm tra biên
//							bossBullets.remove(j);
//							j--;
//						}
					}
				}
			}
		}
	}

	private void checkPlayer(Rocket rocket) {

		if (rocket != null) {
			Area area = new Area(player.getShape());
			area.intersect(rocket.getShape());
			if (!area.isEmpty()) {
				double rocketHP = rocket.getHP();
				if (!rocket.updateHP(player.getHP())) {
					rockets.remove(rocket);
					sound.SoundDestroy();
					double x = rocket.getX() + rocket.ROCKET_SIZE / 2;
					double y = rocket.getY() + rocket.ROCKET_SIZE / 2;
					boomEffects.add(new Effect(x, y, 5, 5, 75, 0.3f, new Color(32, 170, 169)));
					boomEffects.add(new Effect(x, y, 7, 7, 85, 0.25f, new Color(30, 150, 139)));
					boomEffects.add(new Effect(x, y, 5, 5, 75, 0.2f, new Color(240, 107, 159)));
				}
				if (!player.updateHP(rocketHP)) {
					sound.SoundDie();
					player.setAlive(false);
					double x = player.getX() + player.PLAYER_SIZE / 2;
					double y = player.getY() + player.PLAYER_SIZE / 2;
					boomEffects.add(new Effect(x, y, 5, 5, 75, 0.3f, new Color(32, 170, 169)));
					boomEffects.add(new Effect(x, y, 7, 7, 85, 0.25f, new Color(30, 150, 139)));
					boomEffects.add(new Effect(x, y, 5, 5, 75, 0.2f, new Color(240, 107, 159)));
				}
			}
		}

	}

	private void updateScore() {
		if (score > maxScore) {
			maxScore = score;
		}
	}

	private void GAMEhint(Graphics2D g2) {
		if (firstRun) {
			g2.setColor(Color.WHITE);
			g2.setFont(new Font("Arial", Font.BOLD, 20));
			g2.drawString("Hướng dẫn:", 800 / 2 - 50, 600 / 2 - 100);
			g2.drawString("A/D: Xoay tàu", 800 / 2 - 50, 600 / 2 - 50);
			g2.drawString("Space: Tăng tốc độ", 800 / 2 - 50, 600 / 2);
			g2.drawString("J/K: Bắn đạn", 800 / 2 - 50, 600 / 2 + 50);
			g2.drawString("Nhấn Enter để bắt đầu", 800 / 2 - 50, 600 / 2 + 100);
		}

	}

	private void resetGame() {

		score = 0;
		rockets.clear();
		bullets.clear();
		player.changeLocation(150, 150);
		player.resetLive();
		lastBossScore = 0;
	}
}
