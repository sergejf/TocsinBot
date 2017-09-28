package fr.tocsin.stock;

public class Bar {
    private double open;
    private double high;
    private double low;
    private double close;
    private double volume;

    public Bar(double o, double h, double l, double c, double v) {
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
