package fr.tocsin.stock;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "Bars")
public class Bar {
    private String key;
    private String date;
    private String symbol;
    private double open;
    private double high;
    private double low;
    private double close;
    private double volume;

    @DynamoDBHashKey(attributeName = "Key")
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    @DynamoDBAttribute(attributeName = "Open")
    public double getOpen() {
        return open;
    }

    public void setOpen(double open) {
        this.open = open;
    }

    @DynamoDBAttribute(attributeName = "High")
    public double getHigh() {
        return high;
    }

    public void setHigh(double high) {
        this.high = high;
    }

    @DynamoDBAttribute(attributeName = "Low")
    public double getLow() {
        return low;
    }

    public void setLow(double low) {
        this.low = low;
    }

    @DynamoDBAttribute(attributeName = "Close")
    public double getClose() {
        return close;
    }

    public void setClose(double close) {
        this.close = close;
    }

    @DynamoDBAttribute(attributeName = "Volume")
    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }
}
