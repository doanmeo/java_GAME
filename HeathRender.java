package spaceWar;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;

public class HeathRender {
	protected final Heath hp;

	public HeathRender(Heath hp) {
		this.hp = hp;
	}

	protected void hpRender(Graphics2D g2, Shape shape, double y) {
		if (this.hp.getCur_hp() != hp.getMAX_HP()) {
			double hpY = shape.getBounds().getY() - y - 10;
			g2.setColor(new Color(70, 70, 70));
			g2.fill(new Rectangle2D.Double(0, hpY, Player.PLAYER_SIZE, 2));

			g2.setColor(new Color(253, 91, 91));
			double hpSize = hp.getCur_hp() / hp.getMAX_HP() * Player.PLAYER_SIZE;
			g2.fill(new Rectangle2D.Double(0, hpY, hpSize, 2));
		}
	}

	public boolean updateHP(double cutHP) {
		hp.setCur_hp(hp.getCur_hp() - cutHP);
		return hp.getCur_hp() > 0;
	}

	public double getHP() {
		return hp.getCur_hp();
	}

	public void resetHP() {
		hp.setCur_hp(hp.MAX_HP);
	}
}
