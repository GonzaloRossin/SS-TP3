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

    private double fp = 1;
    private int particleCountLeft = 0;

    private int particleCount;
    private final List<List<Particle>> cells;
    List<Particle> particlesList = new ArrayList<>();
    private int eventId = 0;

    private final SortedSet<Event> events = new TreeSet<>();

    private double globalTime = 0;
    private double timeStep = 0.1;
    private double lastTime = 0;

    private final SortedSet<Event> happened = new TreeSet<>();

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
//        calculateM();
    }

    public void generateParticles() {
        Random r = new Random(1);
        for (int i = 0; i < N;) {
            double rx = pRadius * 2 + r.nextDouble() * ((Lx / 2) - 3 * pRadius);
            double ry = 2 * pRadius + r.nextDouble() * (Ly - 4 * pRadius);
            boolean ok = true;
            for (Particle p : particlesList) {
                if (!(Math.pow(rx - p.getR().getX(), 2) + Math.pow(ry - p.getR().getY(), 2) > 4 * pRadius * pRadius)) {
                    ok = false;
                    break;
                }
            }
            if (!ok) {
                continue;
            }
            double ang = 0 + r.nextDouble() * 2 * Math.PI;
            double vx = (Math.cos(ang) * getPVModule());
            double vy = (Math.sin(ang) * getPVModule());
            particlesList.add(new Particle(rc, getPRadius(), rx, ry, particleCount++, vx, vy, getPMass(), 1));
            i++;
        }

        double topRan = Ly / 2 + ranY / 2 + pRadius - 0.0008;
        double botRan = Ly / 2 - ranY / 2 - pRadius + 0.0008;
        particlesList.add(new Particle(rc, getPRadius(), getLx() / 2, topRan, particleCount++, 0, 0, 100000000, 255));
        particlesList.add(new Particle(rc, getPRadius(), getLx() / 2, botRan, particleCount++, 0, 0, 100000000, 255));
    }

    public void generateDummyParticles() {
        particlesList.add(new Particle(rc, getPRadius(), getLx()/4 - 0.01f, getLy()/2, particleCount++, getPVModule(), 0, getPMass(), 1));
        particlesList.add(new Particle(rc, getPRadius(), getLx()/4 + 0.01f, getLy()/2, particleCount++, -getPVModule(), 0, getPMass(), 1));

        double topRan = Ly / 2 + ranY / 2 + pRadius - 0.0008;
        double botRan = Ly / 2 - ranY / 2 - pRadius + 0.0008;
        particlesList.add(new Particle(rc, getPRadius(), getLx() / 2, topRan, particleCount++, 0, 0, 1000000, 255));
        particlesList.add(new Particle(rc, getPRadius(), getLx() / 2, botRan, particleCount++, 0, 0, 1000000, 255));
    }

    public String printParticles() {
        StringBuilder sb = new StringBuilder();
        for (Particle particle : particlesList) {
            sb.append(String.format("%f %f %d\n", particle.getR().getX(), particle.getR().getY(), particle.getColor()));
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
                double newT = e.getT() - curT;
                e.setT(newT);
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

            globalTime += curT;
            if (globalTime > lastTime + timeStep) {
                lastTime += timeStep;
            }
            happened.add(event);
        }
        events.remove(event);
        removeNotValidEvents();

        calculateFp();
        return isValid;
    }

    public boolean endCondition() {
        return fp <= 0.5;
    }

    public void calculateFp() {
        int count = 0;
        for(Particle p: particlesList) {
            if (p.getR().getX() < getLx() / 2) {
                count++;
            }
        }
        setFp(count / (double) particlesList.size());
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
        double t = p.collidesY(getLy());
        if (t > 0) {
            events.add(new Event(t, p, EventType.HORIZONTAL_WALL, eventId++));
        }
        t = p.collidesX(getLx(), getLx() / 2, getLy(), getRanY());
        if (t > 0) {
            events.add(new Event(t, p, EventType.VERTICAL_WALL, eventId++));
        }
        for (Particle neigh : p.getNeighbours()) {
            t = p.collides(neigh);
            if (t > 0) {
                events.add(new Event(t, p, neigh, EventType.PARTICLES, eventId++));
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

    public double getFp() {
        return fp;
    }

    public void setFp(double fp) {
        this.fp = fp;
    }

    public double getGlobalTime() {
        return globalTime;
    }

    public void setGlobalTime(double globalTime) {
        this.globalTime = globalTime;
    }

    public double getTimeStep() {
        return timeStep;
    }

    public void setTimeStep(double timeStep) {
        this.timeStep = timeStep;
    }

    public double getLastTime() {
        return lastTime;
    }

    public void setLastTime(double lastTime) {
        this.lastTime = lastTime;
    }
}
