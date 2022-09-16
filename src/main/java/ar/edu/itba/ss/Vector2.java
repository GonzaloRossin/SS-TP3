package ar.edu.itba.ss;

public class Vector2 {
    private double x,y;

    public Vector2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double innerProduct(Vector2 v) {
        return this.x * v.getX() + this.y * v.getY();
    }

    public Vector2 substract(Vector2 v) {
        return new Vector2(v.getX() - getX(), v.getY() - getY());
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
}
