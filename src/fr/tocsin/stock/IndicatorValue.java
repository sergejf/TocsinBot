package fr.tocsin.stock;

public class IndicatorValue {
    private String name;
    private int period;
    private double value;

    public double getValue() {
        return value;
    }

    public IndicatorValue(String name, int period, double value) {
        this.name = name;
        this.period = period;
        this.value = value;
    }
}
