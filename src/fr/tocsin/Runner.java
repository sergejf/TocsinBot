package fr.tocsin;

import fr.tocsin.identity.User;
import fr.tocsin.stock.*;

import java.util.ArrayList;

public class Runner {

    public static void main(String[] args) {

        Market m = Market.getMarket();

        Portfolio pf = new Portfolio("Test", new User());

        Position po1 = new Position("MORL", 157, 14.88);
        Position po2 = new Position("SDOG", 102, 35.23);
        Position po3 = new Position("BNDX", 36, 53.54);
        Position po4 = new Position("DGS", 97, 40.06);
        Position po5 = new Position("RWX", 59, 40.20);
        Position po6 = new Position("DFE", 56, 52.66);
        Position po7 = new Position("FYT", 78, 29.51);
        Position po8 = new Position("FV", 75, 22.91);
        Position po9 = new Position("AMU", 115, 19.34);

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
    }
}
