package fr.tocsin.stock;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "IndicatorValues")
public class IndicatorValue {

    private String key;
    private String date;
    private String symbol;
    private String indicatorName;
    private int indicatorPeriod;
    private double indicatorValue;

    @DynamoDBHashKey(attributeName = "Key")
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @DynamoDBAttribute(attributeName = "Value")
    public double getIndicatorValue() {
        return indicatorValue;
    }

    public void setIndicatorValue(double indicatorValue) {
        this.indicatorValue = indicatorValue;
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

    public String getIndicatorName() {
        return indicatorName;
    }

    public void setIndicatorName(String indicatorName) {
        this.indicatorName = indicatorName;
    }

    public int getIndicatorPeriod() {
        return indicatorPeriod;
    }

    public void setIndicatorPeriod(int indicatorPeriod) {
        this.indicatorPeriod = indicatorPeriod;
    }
}
