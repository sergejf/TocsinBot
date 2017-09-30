package fr.tocsin.datasource;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import fr.tocsin.Properties;
import fr.tocsin.stock.Bar;
import fr.tocsin.stock.IndicatorValue;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.TreeMap;

public class AlphaVantage {

    private String loadBarData(String symbol) {
        Properties cfg = new Properties();
        HttpResponse<String> res;
        String data;
        String req = "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=" + symbol + "&outputsize=compact&apikey=" + cfg.getProperty("alphaVantage.api");

        try {
            res = Unirest.get(req).header("cache-control", "no-cache").asString();
            data = res.getBody();
        } catch (UnirestException e) {
            e.printStackTrace();
            data = "";
        }
        return data;
    }

    private TreeMap<String, Bar> parseBarData(String symbol, String data) {
        TreeMap<String, Bar> bars = new TreeMap<>();
        JSONObject json = new JSONObject(data);
        JSONObject timeSeries = (JSONObject) json.get("Time Series (Daily)");
        Iterator<String> keyItr = timeSeries.keys();
        while (keyItr.hasNext()) {
            String date = keyItr.next();
            JSONObject item = (JSONObject) timeSeries.get(date);
            Bar bar = new Bar(symbol, item.getDouble("1. open"), item.getDouble("2. high"), item.getDouble("3. low"), item.getDouble("4. close"), item.getDouble("5. volume"));
            bars.put(date, bar);
        }
        return bars;
    }

    public TreeMap<String, Bar> getBars(String symbol) {
        String data = this.loadBarData(symbol);
        return this.parseBarData(symbol, data);
    }

    private String loadIndicatorData(String symbol, String indicatorFunction, int period) {
        Properties cfg = new Properties();
        HttpResponse<String> res;
        String data;
        String req = "https://www.alphavantage.co/query?function=" + indicatorFunction + "&symbol=" + symbol + "&interval=daily&time_period=" + Integer.toString(period) + "&apikey=" + cfg.getProperty("alphaVantage.api");

        try {
            res = Unirest.get(req).header("cache-control", "no-cache").asString();
            data = res.getBody();
        } catch (UnirestException e) {
            e.printStackTrace();
            data = "";
        }
        return data;
    }

    private TreeMap<String, IndicatorValue> parseIndicatorData(String symbol, String data, String indicatorFunction, int indicatorPeriod) {
        TreeMap<String, IndicatorValue> indicators = new TreeMap<>();
        JSONObject json = new JSONObject(data);
        JSONObject timeSeries = (JSONObject) json.get("Technical Analysis: " + indicatorFunction);
        Iterator<String> keyItr = timeSeries.keys();
        while (keyItr.hasNext()) {
            String date = keyItr.next();
            JSONObject item = (JSONObject) timeSeries.get(date);

            // Create new IndicatorValue
            IndicatorValue indicator = new IndicatorValue();
            indicator.setKey(date + symbol + indicatorFunction + Integer.toString(indicatorPeriod));
            indicator.setDate(date);
            indicator.setSymbol(symbol);
            indicator.setIndicatorName(indicatorFunction);
            indicator.setIndicatorPeriod(indicatorPeriod);
            indicator.setIndicatorValue(item.getDouble(indicatorFunction));

            // Add new IndicatorValue to map
            indicators.put(date, indicator);
        }
        return indicators;
    }

    public TreeMap<String, IndicatorValue> getIndicatorValues(String symbol, String indicatorFunction, int indicatorPeriod) {
        String data = this.loadIndicatorData(symbol, indicatorFunction, indicatorPeriod);
        return this.parseIndicatorData(symbol, data, indicatorFunction, indicatorPeriod);
    }

}
