package fr.tocsin.stock;

import fr.tocsin.identity.User;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class Portfolio {

    private String key; // Portfolio.id + User.key
    private String id;
    private String userId;

    private TreeMap<String, Position> positions;
    private User user;

    public Portfolio(String id, User user) {
        this.id = id;
        this.positions = new TreeMap<>();
        this.user = user;
    }

    public void addPosition(Position p) {
        this.positions.put(p.getSymbol(), p);
    }

    public ArrayList<String> getSymbols() {
        ArrayList<String> symbols = new ArrayList<>();

        for (Map.Entry me : this.positions.entrySet()) {
            symbols.add((String) me.getKey());
        }
        return symbols;
    }
}
