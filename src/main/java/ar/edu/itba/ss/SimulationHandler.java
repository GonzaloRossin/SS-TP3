package ar.edu.itba.ss;

import java.util.*;

public class SimulationHandler {
    private int N;
    private float rc;
    private float pRadius;
    private float pVModule;
    private float pMass;

    private float Lx;
    private float Ly;
    private int Mx;
    private int My;

    private float ranY;
    private int t;

    private int particleCount;
    private final List<List<Particle>> cells;
    List<Particle> particlesList = new ArrayList<>();

    private final SortedSet<Event> events = new TreeSet<>();

    public SimulationHandler() {
        // Default
        particleCount = 0;
        Mx = 1;
        My = 1;
        cells = new ArrayList<>(Mx * My);
    }

    public void simInit() {
        generateParticles();
        calculateM();
    }
    public void generateParticles() {
        Random r = new Random(0);
        for (int i = 0; i < N; i++) {
            float rx = pRadius + r.nextFloat() * (Lx - 2 * pRadius);
            float ry = pRadius + r.nextFloat() * (Ly - 2 * pRadius);
            double ang = 0 + r.nextFloat() * 2 * Math.PI;
            float vx = (float) (Math.cos(ang) * getPVModule());
            float vy = (float) (Math.sin(ang) * getPVModule());
            particlesList.add(new Particle(rc, getPRadius(), rx, ry, particleCount++, vx, vy, getPMass()));
        }
    }

    public String printParticles() {
        StringBuilder sb = new StringBuilder(particlesList.size() + "\n\n");
        for (Particle particle : particlesList) {
            sb.append(String.format("%f %f %f\n", particle.getR().getX(), particle.getR().getY(), 0.0f));
        }
        return sb.toString();
    }

    public void calculateM() {
        float maxRadius = 0.0f;
        for(Particle p : getParticlesList()) {
            if (p.getRadius() > maxRadius) {
                maxRadius = p.getRadius();
            }
        }
        Mx = (int) Math.floor(Lx / (rc + 2 * maxRadius));
        My = (int) Math.floor(Ly / (rc + 2 * maxRadius));
    }

    public List<Particle> getParticlesList() {
        return particlesList;
    }

    public List<List<Particle>> cellIndexMethodSetup() {
        List<List<Particle>> cells = getCells();
        for (int i = 0; i < getMx() * getMy(); i++) {
            cells.add(new ArrayList<Particle>());
        }
        for (Particle particle: getParticlesList()) {
            // Calculates cell coordinates and stores them in particle
            particle.setCellCoords(getMx(), getMy(), getLx(), getLy());

            // Adds the particle to de corresponding cell
            cells.get(particle.getCellX() + particle.getCellY() * getMx()).add(particle);
        }
        return cells;
    }

    public void cellIndexMethod() {
        List<List<Particle>> cells = cellIndexMethodSetup();
        for (Particle p : getParticlesList()) {
            // Sets an object with the corresponding xy indexes
            NeighbourCells nc = new NeighbourCells(p, Mx, My);

            for (int i = nc.xStart; i < nc.xEnd; i++) {
                for (int j = nc.yStart; j < nc.yEnd; j++) {
                    p.checkNeighbours(cells.get(i + j * Mx));
                }
            }
        }
    }

    public boolean endCondition() {
        return t == 250;
    }

    public boolean iterate() {
        Event event = events.first();
        boolean isValid = event.isValidEvent();
        if (isValid) {
            // Update all particles positions
            for (Particle p : particlesList) {
                p.updateR(event.getT());
            }
            // Update all events timers
            for (Event e : events) {
                e.setT(e.getT() - event.getT());
            }
            // Update involved particles velocities
            event.bounce();
        }
        t++;
        events.remove(event);
        return isValid;
    }

    public void eventSetup() {
        for (Particle p : getParticlesList()) {
            events.add(new Event(p.collidesY(getLy()), p, EventType.HORIZONTAL_WALL));
            events.add(new Event(p.collidesX(getLx()), p, EventType.VERTICAL_WALL));
            for (Particle neigh : p.getNeighbours()) {
                events.add(new Event(p.collides(neigh), p, neigh, EventType.PARTICLES));
            }
        }
    }

    private static class NeighbourCells {
        int xStart, xEnd, yStart, yEnd;

        public NeighbourCells(Particle p, int Mx, int My) {
            // set xStart
            if (p.getCellX() == 0) {
                xStart = 0;
            } else {
                xStart = p.getCellX();
            }
            // Set xEnd
            if (p.getCellX() == Mx - 1) {
                xEnd = Mx;
            } else {
                xEnd = p.getCellX() + 2;
            }
            // Set yStart
            if (p.getCellY() == 0) {
                yStart = 0;
            } else {
                yStart = p.getCellY() - 1;
            }
            // Set yEnd
            if (p.getCellY() == My - 1) {
                yEnd = My;
            } else {
                yEnd = p.getCellY() + 2;
            }
        }
    }

    public float getRc() {
        return rc;
    }

    public void setRc(float rc) {
        this.rc = rc;
    }

    public int getN() {
        return N;
    }

    public void setN(int n) {
        N = n;
    }

    public float getLx() {
        return Lx;
    }

    public void setLx(float lx) {
        Lx = lx;
    }

    public float getLy() {
        return Ly;
    }

    public void setLy(float ly) {
        Ly = ly;
    }

    public int getMx() {
        return Mx;
    }

    public void setMx(int mx) {
        Mx = mx;
    }

    public int getMy() {
        return My;
    }

    public void setMy(int my) {
        My = my;
    }

    public List<List<Particle>> getCells() {
        return cells;
    }

    public float getRanY() {
        return ranY;
    }

    public void setRanY(float ranY) {
        this.ranY = ranY;
    }

    public float getPMass() {
        return pRadius;
    }

    public void setPMass(float pMass) {
        this.pMass = pMass;
    }

    public float getPVModule() {
        return pVModule;
    }

    public void setPVModule(float pVModule) {
        this.pVModule = pVModule;
    }

    public float getPRadius() {
        return pRadius;
    }

    public void setPRadius(float pRadius) {
        this.pRadius = pRadius;
    }
}
