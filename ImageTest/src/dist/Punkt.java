package dist;

public class Punkt {
    int x, y;
    public Punkt (int xkoord, int ykoord) {
        this.x = xkoord;
        this.y = ykoord;
    }
   
	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
}