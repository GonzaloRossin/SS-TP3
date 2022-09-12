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
        this.aCollisions = a.getCollisions();
        this.bCollisions = b.getCollisions();
        this.b = b;
        this.eventType = eventType;
    }

    public Event(float t, Particle a, EventType eventType) {
        this.t = t;
        this.a = a;
        this.eventType = eventType;
        this.aCollisions = a.getCollisions();
    }

    public boolean isValidEvent() {
        if (Float.isInfinite(t)) {
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

    public EventType getEventType() {
        return eventType;
    }
}
