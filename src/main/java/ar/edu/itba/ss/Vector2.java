package ar.edu.itba.ss;

public class Vector2 {
    private float x,y;

    public Vector2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float innerProduct(Vector2 v) {
        return this.x * v.getX() + this.y * v.getY();
    }

    public Vector2 substract(Vector2 v) {
        return new Vector2(v.getX() - getX(), v.getY() - getY());
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }
}
