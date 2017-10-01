package fr.tocsin.stock;

import fr.tocsin.Util;

public class Position {

    // position is long only
    private String key; // Portfolio.id +
    private String symbol;
    private double quantity;
    private double cost; // average cost
    private double price;
    private double profit;

    public Position(String symbol, double quantity, double cost) {
        this.symbol = symbol;
        this.quantity = quantity;
        this.cost = cost;
        this.price = Market.getMarket().getLastBarClose(symbol);
        this.profit = this.quantity * this.price - this.quantity * this.cost;
    }

    public String getSymbol() {
        return this.symbol;
    }
}


