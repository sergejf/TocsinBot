package fr.tocsin.stock;

import com.udojava.evalex.Expression;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Screener {

    private Expression expression;

    public Screener(String condition) {
        this.expression = new Expression(condition);

        // http://traderhq.com/ultimate-guide-williams-r-indicator/
        // param 1 = period, param 2 = index of symbol
        Expression.Function willr = this.expression.new Function("WILLR", 2) {
            @Override
            public BigDecimal eval(List<BigDecimal> parameters) {
                Market m = Market.getMarket();
                int period = parameters.get(0).intValue();
                String symbol = m.getSymbol(parameters.get(1).intValue());
                double v = m.getLastIndicatorValue(symbol);
                return new BigDecimal(v);
            }
        };
        this.expression.addFunction(willr);
    }

    public ArrayList<String> screen(Portfolio p) {
        Market m;
        BigDecimal res = null;
        ArrayList<String> matches = new ArrayList<>();
        ArrayList<String> symbols;
        // check that portfolio symbol exists in stock market
        symbols = p.getSymbols();
        m = Market.getMarket();
        for (String s : symbols) {
            if (!m.hasSymbol(s)) {
                m.addSymbol("NYSE", s); // FIXME: symbol must belong to an exchange
            }
            // eval screen
            ArrayList<String[]> functions = IndicatorFunctions.getFunctions();
            BigDecimal si = new BigDecimal(m.indexOfSymbol(s));
            this.expression.with("S", si);
            res = this.expression.eval();
            if (res == BigDecimal.ONE) {
                matches.add(s);
            }
        }
        return matches;
    }

}
