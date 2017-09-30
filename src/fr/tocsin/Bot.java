package fr.tocsin;


import fr.tocsin.channel.Messenger;
import fr.tocsin.channel.MessengerCallback;
import fr.tocsin.datasource.AwsDynamoDB;
import fr.tocsin.identity.User;
import fr.tocsin.stock.Market;
import fr.tocsin.stock.Portfolio;
import fr.tocsin.stock.Position;
import fr.tocsin.stock.Screener;

import java.util.ArrayList;

public class Bot implements MessengerCallback {

    private Messenger facebook;

    public void input(String s) {
        System.out.println(s);
        facebook.output("", "Thanks!");
    }

    public Bot() {
        // Instantiate a Facebook Messenger channel
        facebook = new Messenger(this);
    }

    public static void main(String[] args) {

        Bot b = new Bot();

        AwsDynamoDB db = AwsDynamoDB.getDB();
        AwsDynamoDB.createTable("IndicatorValues");


        Market m = Market.getMarket();

        AwsDynamoDB.getIndicatorValue("IndicatorValues", "2017-09-29MORLWILLR14");

        /*
        Portfolio pf = new Portfolio("Test", new User());

        Position po1 = new Position("MORL", 1, 14.88);
        Position po2 = new Position("SDOG", 2, 35.23);
        Position po3 = new Position("BNDX", 3, 53.54);
        Position po4 = new Position("DGS", 4, 40.06);
        Position po5 = new Position("RWX", 5, 40.20);
        Position po6 = new Position("DFE", 6, 52.66);
        Position po7 = new Position("FYT", 7, 29.51);
        Position po8 = new Position("FV", 8, 22.91);
        Position po9 = new Position("AMU", 9, 19.34);

        pf.addPosition(po1);
        pf.addPosition(po2);
        pf.addPosition(po3);
        pf.addPosition(po4);
        pf.addPosition(po5);
        pf.addPosition(po6);
        pf.addPosition(po7);
        pf.addPosition(po8);
        pf.addPosition(po9);

        Screener s = new Screener("WILLR(14, S) < -60.0");
        ArrayList<String> matches = s.screen(pf);
        */
    }
}

