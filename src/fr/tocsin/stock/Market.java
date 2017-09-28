package fr.tocsin.stock;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class Market {

    private static Market m = null;
    private static TreeMap<String, Exchange> exchanges = new TreeMap<>();
    private static ArrayList<String> symbols = new ArrayList<>();

    public static Market getMarket() {

        if (m == null) {
            m = new Market();
            exchanges.put("NYSE", new Exchange("NYSE"));
            m.addSymbol("NYSE", "MORL");
            m.addSymbol("NYSE", "SDOG");
            m.addSymbol("NYSE", "BNDX");
            m.addSymbol("NYSE", "DGS");
            m.addSymbol("NYSE", "RWX");
            m.addSymbol("NYSE", "DFE");
            m.addSymbol("NYSE", "FYT");
            m.addSymbol("NYSE", "FV");
            m.addSymbol("NYSE", "AMU");
        }
        return m;
    }

    public void addSymbol(String e, String s) {
        TimeSeries ts = new TimeSeries(s);
        ts.refreshBars();
        ts.refreshIndicators();
        exchanges.get(e).addSymbol(s);
        symbols.add(s);
    }

    public boolean hasSymbol(String s) {

        // FIXME: should add symbols ArrayList check
        for (Map.Entry me : exchanges.entrySet()) {
            Exchange e = (Exchange) me.getValue();
            if (e.hasSymbol(s)) {
                return true;
            }
        }
        return false;
    }

    public double getIndicatorValue(String s, String d) {
        for (Map.Entry me : exchanges.entrySet()) {
            Exchange e = (Exchange) me.getValue();
            if (e.hasSymbol(s)) {
                return e.getIndicatorValue(s, d);
            }
        }
        return 0.0;
    }

    public double getLastIndicatorValue(String s) {
        for (Map.Entry me : exchanges.entrySet()) {
            Exchange e = (Exchange) me.getValue();
            if (e.hasSymbol(s)) {
                return e.getLastIndicatorValue(s);
            }
        }
        return 0.0;
    }

    public double getLastBarClose(String s) {
        for (Map.Entry me : exchanges.entrySet()) {
            Exchange e = (Exchange) me.getValue();
            if (e.hasSymbol(s)) {
                return e.getLastBarClose(s);
            }
        }
        return 0.0;
    }


    public int indexOfSymbol(String s) {
        return symbols.indexOf(s);
    }

    public String getSymbol(int i) {
        return symbols.get(i);
    }
}
