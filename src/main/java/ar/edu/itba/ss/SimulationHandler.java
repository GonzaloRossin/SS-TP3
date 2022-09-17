package ar.edu.itba.ss;

import java.util.*;

public class SimulationHandler {
    private int N;
    private double rc;
    private double pRadius;
    private double pVModule;
    private double pMass;

    private double Lx;
    private double Ly;
    private int Mx;
    private int My;

    private double ranY;
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
//        generateDummyParticles();
        calculateM();
    }

    public void generateParticles() {
        Random r = new Random(1);
        for (int i = 0; i < N; i++) {
            double rx = pRadius + r.nextDouble() * (Lx - 2 * pRadius);
            double ry = pRadius + r.nextDouble() * (Ly - 2 * pRadius);
            double ang = 0 + r.nextDouble() * 2 * Math.PI;
            double vx = (Math.cos(ang) * getPVModule());
            double vy = (Math.sin(ang) * getPVModule());
            particlesList.add(new Particle(rc, getPRadius(), rx, ry, particleCount++, vx, vy, getPMass()));
        }
    }

    public void generateDummyParticles() {
        particlesList.add(new Particle(rc, getPRadius(), getLx()/2 - 0.01f, getLy()/2, particleCount++, getPVModule(), 0, getPMass()));
        particlesList.add(new Particle(rc, getPRadius(), getLx()/2 + 0.01f, getLy()/2, particleCount++, -getPVModule(), 0, getPMass()));
    }

    public String printParticles() {
        StringBuilder sb = new StringBuilder();
        for (Particle particle : particlesList) {
            sb.append(String.format("%f %f\n", particle.getR().getX(), particle.getR().getY(), 0.0f));
        }
        return sb.toString();
    }

    public void calculateM() {
        double maxRadius = 0.0f;
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
    
    public List<List<Particle>> cellIndexUpdate() {
        List<List<Particle>> cells = getCells();
        for (int i = 0; i < getMx() * getMy(); i++) {
            cells.get(i).clear();
        }
        for (Particle particle: getParticlesList()) {
            // Calculates cell coordinates and stores them in particle
            particle.setCellCoords(getMx(), getMy(), getLx(), getLy());

            // Adds the particle to de corresponding cell
            int index = particle.getCellX() + particle.getCellY() * getMx();
            cells.get(index).add(particle);
        }
        return cells;
    }

    public void cellIndexMethod() {
        List<List<Particle>> cells = cellIndexMethodSetup();
        for (Particle p : getParticlesList()) {
            calculateNeighbours(p);
        }
    }

    public void calculateNeighbours(Particle p) {
        // Sets an object with the corresponding xy indexes
        NeighbourCells nc = new NeighbourCells(p, Mx, My);
        p.emptyNeighbours();

        for (int i = nc.xStart; i < nc.xEnd; i++) {
            for (int j = nc.yStart; j < nc.yEnd; j++) {
                p.checkNeighbours(cells.get(i + j * Mx));
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
            double curT = event.getT();
            for (Event e : events) {
                e.setT(e.getT() - curT);
            }
            // Update involved particles velocities
            event.bounce();

            // Add events for involved particles
            cellIndexUpdate();
            if (event.getEventType() == EventType.PARTICLES) {
                calculateNeighbours(event.getB());
                addEvents(event.getB());
            }
            calculateNeighbours(event.getA());
            addEvents(event.getA());
            t++;
        }
        events.remove(event);
        removeNotValidEvents();
        return isValid;
    }

    public void removeNotValidEvents() {
        events.removeIf(e -> !e.isValidEvent());
    }

    public void eventSetup() {
        for (Particle p : getParticlesList()) {
            addEvents(p);
        }
    }

    public void addEvents(Particle p) {
        events.add(new Event(p.collidesY(getLy()), p, EventType.HORIZONTAL_WALL));
        events.add(new Event(p.collidesX(getLx()), p, EventType.VERTICAL_WALL));

        for (Particle neigh : p.getNeighbours()) {
            events.add(new Event(p.collides(neigh), p, neigh, EventType.PARTICLES));
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

    public double getRc() {
        return rc;
    }

    public void setRc(double rc) {
        this.rc = rc;
    }

    public int getN() {
        return N;
    }

    public void setN(int n) {
        N = n;
    }

    public double getLx() {
        return Lx;
    }

    public void setLx(double lx) {
        Lx = lx;
    }

    public double getLy() {
        return Ly;
    }

    public void setLy(double ly) {
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

    public double getRanY() {
        return ranY;
    }

    public void setRanY(double ranY) {
        this.ranY = ranY;
    }

    public double getPMass() {
        return pMass;
    }

    public void setPMass(double pMass) {
        this.pMass = pMass;
    }

    public double getPVModule() {
        return pVModule;
    }

    public void setPVModule(double pVModule) {
        this.pVModule = pVModule;
    }

    public double getPRadius() {
        return pRadius;
    }

    public void setPRadius(double pRadius) {
        this.pRadius = pRadius;
    }
}
