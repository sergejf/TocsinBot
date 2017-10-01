package fr.tocsin.stock;

import java.util.ArrayList;
import java.util.Optional;
import java.util.TreeMap;

public class Market {

    private static Market m = null;
    private TreeMap<String, TimeSeries> historicalData = new TreeMap<>();
    private static ArrayList<String> symbols = new ArrayList<>();

    public static Market getMarket() {

        if (m == null) {
            m = new Market();
            m.addSymbol("MORL");
            m.addSymbol("SDOG");

                        /*
            m.addSymbol("BNDX");
            m.addSymbol("DGS");
            m.addSymbol("RWX");
            m.addSymbol("DFE");
            m.addSymbol("FYT");
            m.addSymbol("FV");
            m.addSymbol("AMU");
            */
        }
        return m;
    }

    public void addSymbol(String symbol) {

        // If symbol not already loaded in memory, then try to load it
        if (!this.hasSymbol(symbol)) {
            TimeSeries t = new TimeSeries(symbol);
            t.refreshBars();
            t.refreshIndicatorValues(Optional.empty(), Optional.empty());
            this.historicalData.put(symbol, t);
            this.symbols.add(symbol);
        }
    }

    public boolean hasSymbol(String s) {

        return this.historicalData.containsKey(s);
    }

    public double getIndicatorValue(String symbol, String date, String indicatorFunction, int indicatorPeriod) {
        if (this.historicalData.containsKey(symbol)) {
            return this.historicalData.get(symbol).getIndicatorValue(date, indicatorFunction, indicatorPeriod);
        }
        return -1.0;
    }

    public double getLastIndicatorValue(String symbol, String indicatorFunction, int indicatorPeriod) {
        if (this.historicalData.containsKey(symbol)) {
            return this.historicalData.get(symbol).getLastIndicatorValue(indicatorFunction, indicatorPeriod);
        }
        return -1.0;
    }

    public double getLastBarClose(String s) {
        if (this.historicalData.containsKey(s)) {
            return this.historicalData.get(s).getLastBarClose();
        }
        return -1.0;
    }

    public int indexOfSymbol(String s) {
        return symbols.indexOf(s);
    }

    public String getSymbol(int i) {
        return symbols.get(i);
    }
}
