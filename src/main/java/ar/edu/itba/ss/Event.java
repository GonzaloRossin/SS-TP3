package ar.edu.itba.ss;

public class Event implements Comparable<Event> {
    private float t;
    private Particle a;
    private Particle b;

    public Event(float t, Particle a, Particle b) {
        this.t = t;
        this.a = a;
        this.b = b;
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
}
