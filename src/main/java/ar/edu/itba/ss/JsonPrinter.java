package ar.edu.itba.ss;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class JsonPrinter {
    JSONArray fpArray;
    JSONArray pVsTArray;

    public JsonPrinter() {
        this.fpArray = new JSONArray();
        this.pVsTArray = new JSONArray();
    }

    public void addFpVsT(double t, double fp) {
        JSONObject fpVsT = new JSONObject();
        fpVsT.put("t", t);
        fpVsT.put("fp", fp);
        fpArray.add(fpVsT);
    }

    public void addPVsT(double P, double T, double error) {
        JSONObject pVsT = new JSONObject();
        pVsT.put("P", P);
        pVsT.put("T", T);
        pVsT.put("error", error);
        pVsTArray.add(pVsT);
    }

    public JSONArray getFpArray() {
        return fpArray;
    }

    public JSONArray getpVsTArray() {
        return pVsTArray;
    }
}
