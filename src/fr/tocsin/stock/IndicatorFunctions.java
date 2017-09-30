package fr.tocsin.stock;

import java.util.*;

public class IndicatorFunctions {

    public static final int FUNCTION_NAME = 0;
    public static final int FUNCTION_PERIOD = 1;

    private static ArrayList<String[]> functions = new ArrayList<>();
    // http://traderhq.com/ultimate-guide-williams-r-indicator/
    private static String[] willr = {"WILLR", "14"};
    // http://traderhq.com/relative-strength-index-ultimate-guide-rsi/
    private static String[] rsi = {"RSI", "14"};

    static {
        functions = new ArrayList<>();
        functions.add(willr);
        // functions.add(rsi); // FIXME: incompatible AlphaVantage API parameters, review
    }

    public static ArrayList<String[]> getFunctions() {
        return functions;
    }
}
