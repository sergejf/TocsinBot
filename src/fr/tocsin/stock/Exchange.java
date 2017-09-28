package fr.tocsin.stock;

import java.util.TreeMap;

public class Exchange {

    private String name;
    private TreeMap<String, TimeSeries> history;

    public Exchange(String name) {
        this.name = name;
        this.history = new TreeMap<>();
    }

    public void addSymbol(String s) {
        if (!this.history.containsKey(s)) {
            TimeSeries t = new TimeSeries(s);
            t.refreshBars();
            t.refreshIndicators();
            this.history.put(s, t);
        }
    }

    public boolean hasSymbol(String s) {
        return this.history.containsKey(s);
    }

    public double getIndicatorValue(String s, String d) {
        if (this.history.containsKey(s)) {
            return this.history.get(s).getIndicatorValue(d);
        }
        return 0.0;
    }

    public double getLastIndicatorValue(String s) {
        if (this.history.containsKey(s)) {
            return this.history.get(s).getLastIndicatorValue();
        }
        return 0.0;
    }

    public double getLastBarClose(String s) {
        if (this.history.containsKey(s)) {
            return this.history.get(s).getLastBarClose();
        }
        return 0.0;
    }

}
