package spaceWar;

public class modelBoom {
	double size;
	float angle;

	public double getSize() {
		return size;
	}

	public void setSize(double size) {
		this.size = size;
	}

	public float getAngle() {
		return angle;
	}

	public void setAngle(float angle) {
		this.angle = angle;
	}

	public modelBoom(double size, float angle) {
		super();
		this.size = size;
		this.angle = angle;
	}

}
