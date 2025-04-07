package spaceWar;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

public class background {
	private BufferedImage image;

	public background(String imagePath) {
		synchronized (background.class) {
			try {
				URL resource = getClass().getResource(imagePath);
				if (resource == null) {
					System.err.println("Không tìm thấy file hình ảnh tại: " + imagePath);
					return;
				}
				image = ImageIO.read(resource);
			} catch (IOException e) {
				System.err.println("Lỗi khi đọc file hình ảnh: " + e.getMessage());
				e.printStackTrace();
			}
		}
	}

	public void draw(Graphics2D g2, int width, int height) {
		if (image != null) {
			g2.drawImage(image, 0, 0, width, height, null);
		}
	}
}