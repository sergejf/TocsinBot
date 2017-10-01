package fr.tocsin.stock;

import fr.tocsin.Util;
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

    public double getIndicatorValue(String date, String indicatorFunction, int indicatorPeriod) {
        AwsDynamoDB db = AwsDynamoDB.getDB();
        IndicatorValue indicatorValue;

        indicatorValue = db.getIndicatorValue(date + symbol + indicatorFunction + Integer.toString(indicatorPeriod));
        if (indicatorValue == null) {
            refreshIndicatorValues(Optional.of(indicatorFunction), Optional.of(indicatorPeriod));
            indicatorValue = db.getIndicatorValue(date + symbol + indicatorFunction + Integer.toString(indicatorPeriod));
        }
        return indicatorValue.getIndicatorValue();
    }

    // Return indicator value: if today is a weekday day, return current value, otherwise return value of previous weekday
    public double getLastIndicatorValue(String indicatorFunction, int indicatorPeriod) {
        return getIndicatorValue(Util.lastWeekday(), indicatorFunction, indicatorPeriod);
    }

    // TODO: retrieve close for any day in the past
    // Return bar close: if today is a weekday day, return current close, otherwise return close of previous weekday
    public double getLastBarClose() {
        AwsDynamoDB db = AwsDynamoDB.getDB();
        Bar bar;

        bar = db.getBar(Util.lastWeekday() + symbol);
        if (bar == null) {
            refreshBars();
            bar = db.getBar(Util.lastWeekday() + symbol);
        }
        return bar.getClose();
    }

    public void refreshBars() {

        // Refresh DB if last weekday bar isn't already in DB
        AwsDynamoDB db = AwsDynamoDB.getDB();
        if (db.getBar(Util.lastWeekday() + symbol) == null) {
            // Load bars from data source
            AlphaVantage dataSource = new AlphaVantage();
            this.bars = dataSource.getBars(this.symbol);

            // Persist Bars into DB
            Set set = bars.entrySet();
            Iterator it = set.iterator();
            while (it.hasNext()) {
                Map.Entry me = (Map.Entry) it.next();
                db.setBar((Bar) me.getValue());
            }
        }
    }

    public void refreshIndicatorValues(Optional<String> indicatorFunction, Optional<Integer> indicatorPeriod) {

        AwsDynamoDB db = AwsDynamoDB.getDB();
        AlphaVantage dataSource = new AlphaVantage();

        // Refresh one indicator
        if (indicatorFunction.isPresent() && indicatorPeriod.isPresent()) {
            // Load IndicatorValues from data source for each Indicator Function
            this.indicatorValues = dataSource.getIndicatorValues(symbol, indicatorFunction.get(), indicatorPeriod.get());

            // Persist IndicatorValues into DB
            Set set = indicatorValues.entrySet();
            Iterator it = set.iterator();
            while (it.hasNext()) {
                Map.Entry me = (Map.Entry) it.next();
                db.setIndicatorValue((IndicatorValue) me.getValue());
            }
        } else {
            // Refresh all indicators
            ArrayList<String[]> functions = IndicatorFunctions.getFunctions();
            for (String[] f : functions) {

                // Refresh DB if last weekday IndicatorValue for the symbol/function/period isn't already in DB
                if (db.getIndicatorValue(Util.lastWeekday() + symbol + f[IndicatorFunctions.FUNCTION_NAME] + f[IndicatorFunctions.FUNCTION_PERIOD]) == null) {

                    // Load IndicatorValues from data source for each Indicator Function
                    this.indicatorValues = dataSource.getIndicatorValues(this.symbol, f[IndicatorFunctions.FUNCTION_NAME], Integer.parseInt(f[IndicatorFunctions.FUNCTION_PERIOD]));

                    // Persist IndicatorValues into DB
                    Set set = indicatorValues.entrySet();
                    Iterator it = set.iterator();
                    while (it.hasNext()) {
                        Map.Entry me = (Map.Entry) it.next();
                        db.setIndicatorValue((IndicatorValue) me.getValue());
                    }
                }
            }
        }
    }
}
