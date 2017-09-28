package fr.tocsin;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Util {

    public static Date parseDate(String d) {
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
        Date t;
        try {
            t = ft.parse(d);
        } catch (ParseException e) {
            System.out.println("Unparseable using " + ft);
            t = null;
        }
        return t;
    }

    public static String todayDate() {
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
        Date t = new Date();
        return ft.format(t);
    }
}
