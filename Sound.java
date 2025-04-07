package spaceWar;

import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Sound {
	private final URL shoot;
	private final URL hit;
	private final URL destroy;
	private final URL die;
	private final URL backgroundMusic; // URL cho nhạc nền
	private Clip backgroundClip; // Clip để quản lý nhạc nền
	private float currentVolume = 0.0f; // Biến lưu trữ mức âm lượng hiện tại (mặc định là 0.0f)

	public Sound() {
		this.die = this.getClass().getResource("/spaceWar/gameImage/fart.wav");
		this.shoot = this.getClass().getResource("/spaceWar/gameImage/shutter.wav");
		this.hit = this.getClass().getResource("/spaceWar/gameImage/hit.wav");
		this.destroy = this.getClass().getResource("/spaceWar/gameImage/destroy.wav");
		this.backgroundMusic = this.getClass().getResource("/spaceWar/gameImage/PinkPantherThemeSong.wav"); // Đường dẫn
																											// tới file
																											// nhạc nền
		initBackgroundMusic(); // Khởi tạo nhạc nền
		decreaseBackgroundVolume(5.0f);
	}

	// Khởi tạo Clip cho nhạc nền
	private void initBackgroundMusic() {
		try {
			if (backgroundMusic == null) {
				System.err.println("URL nhạc nền là null, không thể khởi tạo nhạc nền.");
				return;
			}
			AudioInputStream audioIn = AudioSystem.getAudioInputStream(backgroundMusic);
			backgroundClip = AudioSystem.getClip();
			backgroundClip.open(audioIn);
			audioIn.close();
		} catch (IOException | LineUnavailableException | UnsupportedAudioFileException e) {
			System.err.println("Lỗi khi khởi tạo nhạc nền: " + e.getMessage());
		}
	}

	private void play(URL url) {

		try {
			if (url == null) {
				System.err.println("URL âm thanh là null, không thể phát âm thanh.");
				return;
			}
			AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
			Clip clip = AudioSystem.getClip();
			clip.open(audioIn);
			clip.addLineListener(new LineListener() {
				@Override
				public void update(LineEvent event) {
					if (event.getType() == LineEvent.Type.STOP) {
						clip.close();

					}
				}

			});
			audioIn.close();
			clip.start();
		} catch (IOException | LineUnavailableException | UnsupportedAudioFileException e) {
			System.err.println(e);
		}

	}

	public void SoundShoot() {
		play(shoot);

	}

	public void SoundHit() {
		play(hit);

	}

	public void SoundDestroy() {
		play(destroy);

	}

	public void SoundDie() {
		play(die);

	}

	// Phương thức phát nhạc nền lặp lại liên tục
	public void playBackgroundMusic() {
		if (backgroundClip != null && !backgroundClip.isRunning()) {
			backgroundClip.loop(Clip.LOOP_CONTINUOUSLY); // Lặp lại vô hạn
		}
	}

	// Phương thức dừng nhạc nền
	public void stopBackgroundMusic() {
		if (backgroundClip != null && backgroundClip.isRunning()) {
			backgroundClip.stop();
		}
	}

	// Phương thức điều chỉnh âm lượng nhạc nền
	public void setBackgroundVolume(float volume) {
		if (backgroundClip != null) {
			try {
				FloatControl gainControl = (FloatControl) backgroundClip.getControl(FloatControl.Type.MASTER_GAIN);
				gainControl.setValue(volume);
				currentVolume = volume; // Cập nhật mức âm lượng hiện tại
			} catch (IllegalArgumentException e) {
				System.err.println("Không thể điều chỉnh âm lượng: " + e.getMessage());
			}
		}
	}

	// Phương thức giảm âm lượng nhạc nền
	public void decreaseBackgroundVolume(float decrement) {
		if (backgroundClip != null) {
			float newVolume = currentVolume - decrement;
			// Đảm bảo âm lượng không nhỏ hơn mức tối thiểu (-80.0f)
			if (newVolume < -80.0f) {
				newVolume = -80.0f;
			}
			setBackgroundVolume(newVolume);
		}
	}
}
