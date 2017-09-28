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

    public String loadBarData(String s) {
        Properties cfg = new Properties();
        HttpResponse<String> res;
        String data;
        String req = "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=" + s + "&outputsize=compact&apikey=" + cfg.getProperty("alphaVantage.api");

        try {
            res = Unirest.get(req).header("cache-control", "no-cache").asString();
            data = res.getBody();
        } catch (UnirestException e) {
            e.printStackTrace();
            data = "";
        }
        return data;
    }

    public TreeMap<String, Bar> parseBarData(String d) {
        TreeMap<String, Bar> bars = new TreeMap<>();
        JSONObject json = new JSONObject(d);
        JSONObject timeSeries = (JSONObject) json.get("Time Series (Daily)");
        Iterator<String> keyItr = timeSeries.keys();
        while (keyItr.hasNext()) {
            String date = keyItr.next();
            JSONObject item = (JSONObject) timeSeries.get(date);
            Bar bar = new Bar(item.getDouble("1. open"), item.getDouble("2. high"), item.getDouble("3. low"), item.getDouble("4. close"), item.getDouble("5. volume"));
            bars.put(date, bar);
        }
        return bars;
    }

    public String loadIndicatorData(String s, String i, int p) {
        Properties cfg = new Properties();
        HttpResponse<String> res;
        String data;
        String req = "https://www.alphavantage.co/query?function=" + i + "&symbol=" + s + "&interval=daily&time_period=" + Integer.toString(p) + "&apikey=" + cfg.getProperty("alphaVantage.api");

        try {
            res = Unirest.get(req).header("cache-control", "no-cache").asString();
            data = res.getBody();
        } catch (UnirestException e) {
            e.printStackTrace();
            data = "";
        }
        return data;
    }

    public TreeMap<String, IndicatorValue> parseIndicatorData(String d, String i, int p) {
        TreeMap<String, IndicatorValue> indicators = new TreeMap<>();
        JSONObject json = new JSONObject(d);
        JSONObject timeSeries = (JSONObject) json.get("Technical Analysis: " + i);
        Iterator<String> keyItr = timeSeries.keys();
        while (keyItr.hasNext()) {
            String date = keyItr.next();
            JSONObject item = (JSONObject) timeSeries.get(date);
            IndicatorValue indicator = new IndicatorValue(i, p, item.getDouble(i));
            indicators.put(date, indicator);
        }
        return indicators;
    }

}
