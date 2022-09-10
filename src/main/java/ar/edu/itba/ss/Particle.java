package ar.edu.itba.ss;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class Particle {
    private float rc;
    private float radius;
    private float rx;
    private float ry;
    private float vx;
    private float vy;
    private float mass;
    private int id;
    private int cellX;
    private int cellY;

    private Set<Particle> neighbours;

    public Particle(float rc, float radius, float x, float y, int id) {
        this.rc = rc;
        this.radius = radius;
        this.rx = x;
        this.ry = y;
        this.id = id;
        neighbours = new HashSet<>();
    }

    public Particle(float rc, float radius, float rx, float ry, int id, float vx, float vy, float mass) {
        this.rc = rc;
        this.radius = radius;
        this.rx = rx;
        this.ry = ry;
        this.vx = vx;
        this.vy = vy;
        this.id = id;
        this.mass = mass;
        neighbours = new HashSet<>();
    }

    Particle(float x, float y) {
        this.rx = x;
        this.ry = y;
    }

    public float collidesY(float Ly) {
        if (vy > 0) {
            return (Ly - radius - ry) / vy;
        }
        return (radius - ry) / vy;
    }

    public float collidesX(float Lx) {
        if (vx > 0) {
            return (Lx - radius - rx) / vx;
        }
        return (radius - rx) / vx;
    }

    public void setCellCoords(int Mx, int My, float Lx, float Ly) {
        int xOffset = (int)Math.floor((getRx() * Mx) / Lx);
        int yOffset = (int)Math.floor((getRy() * My) / Ly);

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
        return Math.sqrt(Math.pow(p.getRx() - getRx(), 2) + Math.pow(p.getRy() - getRy(), 2)) < getRc() + getRadius() + p.getRadius();
    }

    @Override
    public String toString() {
        return String.format("Id %d pos[%.2f, %.2f] rc %.2f cellX %d cellY %d", id, rx, ry, rc, cellX, cellY);
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

    public float getRx() {
        return rx;
    }

    public void setRx(float rx) {
        this.rx = rx;
    }

    public float getRy() {
        return ry;
    }

    public void setRy(float ry) {
        this.ry = ry;
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

    public float getVx() {
        return vx;
    }

    public void setVx(float vx) {
        this.vx = vx;
    }

    public float getVy() {
        return vy;
    }

    public void setVy(float vy) {
        this.vy = vy;
    }
}
