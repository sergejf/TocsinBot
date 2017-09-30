package fr.tocsin.stock;

import fr.tocsin.datasource.AlphaVantage;
import fr.tocsin.datasource.AwsDynamoDB;

import java.util.*;

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
        return this.indicatorValues.get(date).getIndicatorValue();
    }

    // Return indicator value: if today is a trading day, return current value, otherwise return value of previous close.
    public double getLastIndicatorValue() {
        return this.indicatorValues.get(this.indicatorValues.lastKey()).getIndicatorValue();
    }

    public double getLastBarClose() {
        return this.bars.get(this.bars.lastKey()).getClose();
    }

    public void refreshBars() {
        AlphaVantage dataSource = new AlphaVantage();
        this.bars = dataSource.getBars(this.symbol);
    }

    public void refreshIndicators() {
        AlphaVantage dataSource = new AlphaVantage();
        ArrayList<String[]> functions = IndicatorFunctions.getFunctions();
        for (String[] f : functions) {
            this.indicatorValues = dataSource.getIndicatorValues(this.symbol, f[IndicatorFunctions.FUNCTION_NAME], Integer.parseInt(f[IndicatorFunctions.FUNCTION_PERIOD]));
        }

        Set set = indicatorValues.entrySet();
        Iterator it = set.iterator();
        while (it.hasNext()) {
            Map.Entry me = (Map.Entry) it.next();
            AwsDynamoDB.setIndicatorValue((IndicatorValue) me.getValue());
        }
    }
}
