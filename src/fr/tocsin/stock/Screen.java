package fr.tocsin.stock;

import com.udojava.evalex.Expression;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Screen {

    private String key; // Screen.id + User.key
    private String id;
    private String userId;
    private String screen;

    private Expression expression;

    public Screen(String condition) {
        this.expression = new Expression(condition);

        // http://traderhq.com/ultimate-guide-williams-r-indicator/
        // param 1 = period, param 2 = index of symbol
        Expression.Function willr = this.expression.new Function("WILLR", 2) {
            @Override
            public BigDecimal eval(List<BigDecimal> parameters) {
                Market m = Market.getMarket();
                int period = parameters.get(0).intValue();
                String symbol = m.getSymbol(parameters.get(1).intValue());
                double v = m.getLastIndicatorValue(symbol, "WILLR", period);
                return new BigDecimal(v);
            }
        };
        this.expression.addFunction(willr);
    }

    public ArrayList<String> apply(Portfolio p) {
        Market m;
        BigDecimal res;
        ArrayList<String> matches = new ArrayList<>();
        ArrayList<String> symbols;
        // check that portfolio symbol exists in stock market
        symbols = p.getSymbols();
        m = Market.getMarket();
        for (String s : symbols) {
            if (!m.hasSymbol(s)) {
                m.addSymbol(s);
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
