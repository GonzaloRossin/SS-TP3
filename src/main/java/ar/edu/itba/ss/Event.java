package ar.edu.itba.ss;

import java.util.Objects;

public class Event implements Comparable<Event> {
    private float t;
    private Particle a;
    private int aCollisions;
    private Particle b;
    private int bCollisions;
    private EventType eventType;

    public Event(float t, Particle a, Particle b, EventType eventType) {
        this.t = t;
        this.a = a;
        this.b = b;
        this.eventType = eventType;
    }

    public Event(float t, Particle a, EventType eventType) {
        this.t = t;
        this.a = a;
        this.eventType = eventType;
    }

    public boolean isValidEvent() {
        switch (eventType) {
            case PARTICLES: {
                return a.getCollisions() == aCollisions && b.getCollisions() == bCollisions;
            }
            case HORIZONTAL_WALL: {
                return a.getCollisions() == aCollisions;
            }
            case VERTICAL_WALL: {
                return a.getCollisions() == bCollisions;
            }
            default:
                return false;
        }
    }

    public void bounce() {
        switch (eventType) {
            case PARTICLES: {
                a.bounce(b);
                break;
            }
            case HORIZONTAL_WALL: {
                a.bounceX();
                break;
            }
            case VERTICAL_WALL: {
                a.bounceY();
                break;
            }
        }
    }

    public float getT() {
        return t;
    }

    public void setT(float t) {
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
        return Float.compare(t, o.getT());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Float.compare(event.t, t) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(t);
    }
}
