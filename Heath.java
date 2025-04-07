package spaceWar;

public class Heath {
	double MAX_HP;
	double cur_hp;

	public double getMAX_HP() {
		return MAX_HP;
	}

	public void setMAX_HP(double mAX_HP) {
		MAX_HP = mAX_HP;
	}

	public double getCur_hp() {
		return cur_hp;
	}

	public void setCur_hp(double cur_hp) {
		this.cur_hp = cur_hp;
	}

	public Heath(double mAX_HP, double cur_hp) {
		MAX_HP = mAX_HP;
		this.cur_hp = cur_hp;
	}

}
