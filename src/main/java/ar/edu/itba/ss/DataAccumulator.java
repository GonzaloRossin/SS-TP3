package ar.edu.itba.ss;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class DataAccumulator {
    private List<Double> PList;
    private List<Double> TList;
    private int N;

    public DataAccumulator(int N) {
        this.PList = new ArrayList<>();
        this.TList = new ArrayList<>();
        this.N = N;
    }

    public void addP(Double i) {
        PList.add(i);
    }

    public void addT(Double i) {
        TList.add(i);
    }

    public double getPressureProm() {
        double accum = 0;
        double count = 0;
        for (Double i : PList) {
            accum += i;
            count++;
        }
        return accum / count;
    }

    public Double getPressureError() {
        return PList.stream().max(Comparator.naturalOrder()).get() - PList.stream().min(Comparator.naturalOrder()).get();
    }

    public double getTimeToEqProm() {
        double accum = 0;
        double count = 0;
        for (Double i : TList) {
            accum += i;
            count++;
        }
        return accum / count;
    }


    public Double getTimeToEqError() {
        return TList.stream().max(Comparator.naturalOrder()).get() - TList.stream().min(Comparator.naturalOrder()).get();
    }

    public List<Double> getPList() {
        return PList;
    }

    public void setPList(List<Double> PList) {
        this.PList = PList;
    }

    public List<Double> getTList() {
        return TList;
    }

    public void setTList(List<Double> TList) {
        this.TList = TList;
    }

    public int getN() {
        return N;
    }

    public void setN(int n) {
        N = n;
    }
}
