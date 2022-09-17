package ar.edu.itba.ss;

import java.util.Objects;

public class Event implements Comparable<Event> {
    private double t;
    private Particle a;
    private int aCollisions;
    private Particle b;
    private int bCollisions;
    private EventType eventType;
    private static int id;

    public Event(double t, Particle a, Particle b, EventType eventType) {
        this.t = t;
        this.a = a;
        this.aCollisions = a.getCollisions();
        this.bCollisions = b.getCollisions();
        this.b = b;
        this.eventType = eventType;
        this.id = id++;
    }

    public Event(double t, Particle a, EventType eventType) {
        this.t = t;
        this.a = a;
        this.eventType = eventType;
        this.aCollisions = a.getCollisions();
    }

    public boolean isValidEvent() {
        if (Double.isInfinite(t)) {
            return false;
        }
        if (eventType == EventType.PARTICLES) {
            return a.getCollisions() == aCollisions && b.getCollisions() == bCollisions;
        } else {
            return a.getCollisions() == aCollisions;
        }
    }

    public void bounce() {
        switch (eventType) {
            case PARTICLES: {
                a.bounce(b);
                break;
            }
            case HORIZONTAL_WALL: {
                a.bounceY();
                break;
            }
            case VERTICAL_WALL: {
                a.bounceX();
                break;
            }
        }
    }

    public double getT() {
        return t;
    }

    public void setT(double t) {
        this.t = t;
    }

    public Particle getA() {
        return a;
    }

    public void setA(Particle a) {
        this.a = a;
    }

    public Particle getB() {
        return b;
    }

    public void setB(Particle b) {
        this.b = b;
    }

    @Override
    public int compareTo(Event o) {
        if (Double.compare(t, o.getT()) != 0) {
            return Double.compare(t, o.getT());
        }
        if (aCollisions != o.a.getCollisions()) {
            return Integer.compare(aCollisions, o.a.getCollisions());
        }
        if (eventType == EventType.PARTICLES) {
            if (bCollisions != o.b.getCollisions()) {
                return Integer.compare(aCollisions, o.b.getCollisions());
            }
        } else {
            return Integer.compare(getA().getId(), o.getA().getId());
        }
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Double.compare(event.t, t) == 0 && a.equals(event.a) && eventType == event.eventType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(t, a, eventType);
    }

    public EventType getEventType() {
        return eventType;
    }
}
