package ar.edu.itba.ss;

import javax.naming.NameNotFoundException;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class Particle {
    private float rc;
    private float radius;
    private Vector2 r;
    private Vector2 v;
    private float mass;
    private int id;
    private int cellX;
    private int cellY;

    private Set<Particle> neighbours;

    public Particle(float rc, float radius, float x, float y, int id) {
        this.rc = rc;
        this.radius = radius;
        this.r = new Vector2(x, y);
        this.id = id;
        neighbours = new HashSet<>();
    }

    public Particle(float rc, float radius, float rx, float ry, int id, float vx, float vy, float mass) {
        this.rc = rc;
        this.radius = radius;
        this.r = new Vector2(rx, ry);
        this.v = new Vector2(vx, vy);
        this.id = id;
        this.mass = mass;
        neighbours = new HashSet<>();
    }

    Particle(float x, float y) {
        this.r = new Vector2(x, y);
    }

    public float collidesY(float Ly) {
        if (v.getY() > 0) {
            return (Ly - radius - r.getY()) / v.getY();
        }
        return (radius - r.getY()) / v.getY();
    }

    public float collidesX(float Lx) {
        if (v.getX() > 0) {
            return (Lx - radius - r.getX()) / v.getX();
        }
        return (radius - r.getX()) / v.getX();
    }

    public float collides(Particle neigh) {
        Vector2 dR = getR().substract(neigh.getR());
        Vector2 dV = getV().substract(neigh.getV());
        float dVdR = dV.getX() * dR.getX() + dV.getY() * dR.getY();
        if (dVdR >= 0) {
            return Float.NaN;
        }
        float dRdR = dR.innerProduct(dR);
        float dVdV = dV.innerProduct(dV);
        float sigma = getRadius() + neigh.getRadius();
        float d = (float) Math.pow(dVdR, 2) - (dVdV) * (dRdR - (float) Math.pow(sigma, 2));

        if (d < 0 ) {
            return Float.NaN;
        }
        return (-dVdR + (float) Math.sqrt(d)) / dVdV;
    }

    public void setCellCoords(int Mx, int My, float Lx, float Ly) {
        int xOffset = (int)Math.floor((getR().getX() * Mx) / Lx);
        int yOffset = (int)Math.floor((getR().getY() * My) / Ly);

        setCellX(xOffset);
        setCellY(yOffset);
    }

    public void checkNeighbours(List<Particle> particles) {
        // Calculate distance for each neighbor and add to set, check if not already calculated distance
        for (Particle p : particles) {
            if (!p.equals(this)) {
                if (!neighbours.contains(p) && isInRange(p)) {
                    neighbours.add(p);
                    p.getNeighbours().add(this);
                }
            }
        }
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

    public float getRc() {
        return rc;
    }

    public void setRc(float rc) {
        this.rc = rc;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
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

}
