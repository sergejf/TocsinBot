package fr.tocsin.stock;

import fr.tocsin.datasource.AlphaVantage;

import java.util.ArrayList;
import java.util.TreeMap;

public class TimeSeries {

    private String symbol;
    private TreeMap<String, Bar> bars = new TreeMap<>();
    private TreeMap<String, IndicatorValue> indicatorValues = new TreeMap<>();

    public TimeSeries(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return this.symbol;
    }

    public double getIndicatorValue(String date) {
        return this.indicatorValues.get(date).getValue();
    }

    // Return indicator value: if today is a trading day, return current value, otherwise return value of previous close.
    public double getLastIndicatorValue() {
        return this.indicatorValues.get(this.indicatorValues.lastKey()).getValue();
    }

    public double getLastBarClose() {
        return this.bars.get(this.bars.lastKey()).getClose();
    }

    public void refreshBars() {
        AlphaVantage dataSource = new AlphaVantage();
        String data = dataSource.loadBarData(this.symbol);
        this.bars = dataSource.parseBarData(data);
    }

    public void refreshIndicators() {
        AlphaVantage dataSource = new AlphaVantage();
        ArrayList<String[]> functions = IndicatorFunctions.getFunctions();
        for (String[] f : functions) {
            String data = dataSource.loadIndicatorData(this.symbol, f[0], Integer.parseInt(f[1]));
            this.indicatorValues = dataSource.parseIndicatorData(data, f[0], Integer.parseInt(f[1]));
        }
    }
}
