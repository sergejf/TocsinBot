package fr.tocsin.stock;

public class Bar {
    private String symbol;
    private double open;
    private double high;
    private double low;
    private double close;
    private double volume;

    public Bar(String s, double o, double h, double l, double c, double v) {
        this.symbol = s;
        this.open = o;
        this.high = h;
        this.low = l;
        this.close = c;
        this.volume = v;
    }

    public double getClose() {
        return this.close;
    }
}
