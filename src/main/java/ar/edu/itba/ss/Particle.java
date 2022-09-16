package ar.edu.itba.ss;

import javax.naming.NameNotFoundException;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class Particle {
    private double rc;
    private double radius;
    private Vector2 r;
    private Vector2 v;
    private double mass;
    private int id;
    private int cellX;
    private int cellY;
    private int collisions;

    private Set<Particle> neighbours;

    public Particle(double rc, double radius, double x, double y, int id) {
        this.rc = rc;
        this.radius = radius;
        this.r = new Vector2(x, y);
        this.id = id;
        neighbours = new HashSet<>();
    }

    public Particle(double rc, double radius, double rx, double ry, int id, double vx, double vy, double mass) {
        this.rc = rc;
        this.radius = radius;
        this.r = new Vector2(rx, ry);
        this.v = new Vector2(vx, vy);
        this.id = id;
        this.mass = mass;
        neighbours = new HashSet<>();
    }

    Particle(double x, double y) {
        this.r = new Vector2(x, y);
    }

    public double collidesY(double Ly) {
        if (v.getY() > 0) {
            return ((Ly - radius - r.getY()) / v.getY()) * 0.99;
        }
        return ((radius - r.getY()) / v.getY()) * 0.99;
    }

    public double collidesX(double Lx) {
        if (v.getX() > 0) {
            return ((Lx - radius - r.getX()) / v.getX()) * 0.99;
        }
        return ((radius - r.getX()) / v.getX()) * 0.99;
    }

    public double collides(Particle neigh) {
        Vector2 dR = getR().substract(neigh.getR());
        Vector2 dV = getV().substract(neigh.getV());
        double dVdR = dV.getX() * dR.getX() + dV.getY() * dR.getY();
        if (dVdR >= 0) {
            return Double.NaN;
        }
        double dRdR = dR.innerProduct(dR);
        double dVdV = dV.innerProduct(dV);
        double sigma = getRadius() + neigh.getRadius();
        double d = (double) Math.pow(dVdR, 2) - (dVdV) * (dRdR - (double) Math.pow(sigma, 2));

        if (d < 0 ) {
            return Double.NaN;
        }
        return ((-dVdR + Math.sqrt(d)) / dVdV) * 0.99;
    }

    public void bounceX() {
        v.setX(-v.getX());
        incrementCollision();
    }
    public void bounceY() {
        v.setY(-v.getY());
        incrementCollision();
    }

    public double getMass() {
        return mass;
    }

    public void incrementCollision() {
        collisions++;
    }

    public void bounce(Particle b) {
        Vector2 dR = getR().substract(b.getR());
        Vector2 dV = getV().substract(b.getV());
        double dVdR = dV.getX() * dR.getX() + dV.getY() * dR.getY();
        double sigma = getRadius() + b.getRadius();
        double J = (2 * getMass() * b.getMass() * (dVdR)) / sigma * (getMass() * b.getMass());
        double Jx = J * dR.getX() / sigma;
        double Jy = J * dR.getY() / sigma;

        v.setX(v.getX() - Jx / getMass());
        v.setY(v.getY() - Jy / getMass());
        incrementCollision();

        b.getV().setX(b.getV().getX() + Jx / b.getMass());
        b.getV().setY(b.getV().getY() + Jy / b.getMass());
        b.incrementCollision();
    }

    public void setCellCoords(int Mx, int My, double Lx, double Ly) {
        int xOffset = (int)Math.floor((getR().getX() * Mx) / Lx);
        int yOffset = (int)Math.floor((getR().getY() * My) / Ly);

        setCellX(xOffset);
        setCellY(yOffset);
    }

    public void checkNeighbours(List<Particle> particles) {
        // Calculate distance for each neighbor and add to set, check if not already calculated distance
        for (Particle p : particles) {
            if (!p.equals(this)) {
                if (!neighbours.contains(p)) {
                    neighbours.add(p);
                    p.getNeighbours().add(this);
                }
            }
        }
    }

    public void emptyNeighbours() {
        neighbours.clear();
    }

    public boolean isInRange(Particle p) {
        return Math.sqrt(Math.pow(p.getR().getX() - getR().getX(), 2) + Math.pow(p.getR().getY() - getR().getY(), 2)) < getRc() + getRadius() + p.getRadius();
    }

    @Override
    public String toString() {
        return String.format("Id %d pos[%.2f, %.2f] rc %.2f cellX %d cellY %d", id, r.getX(), r.getY(), rc, cellX, cellY);
    }

    public String strNeighbours() {
        StringBuilder toRet = new StringBuilder();
        for (Particle p : neighbours) {
            toRet.append(p.getId()).append(" ");
        }
        return toRet.toString();
    }

    public Set<Particle> getNeighbours() {
        return neighbours;
    }

    public Vector2 getR() {
        return r;
    }

    public Vector2 getV() {
        return v;
    }

    public void setNeighbours(Set<Particle> neighbours) {
        this.neighbours = neighbours;
    }

    public double getRc() {
        return rc;
    }

    public void setRc(double rc) {
        this.rc = rc;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCellX() {
        return cellX;
    }

    public void setCellX(int cellX) {
        this.cellX = cellX;
    }

    public int getCellY() {
        return cellY;
    }

    public void setCellY(int cellY) {
        this.cellY = cellY;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Particle particle = (Particle) o;
        return id == particle.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public int getCollisions() {
        return collisions;
    }

    public void updateR(double t) {
        r.setX(getR().getX() + getV().getX() * t);
        r.setY(getR().getY() + getV().getY() * t);
    }
}
