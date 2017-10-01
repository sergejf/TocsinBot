package fr.tocsin.stock;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import fr.tocsin.identity.User;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

@DynamoDBTable(tableName = "Portfolios")
public class Portfolio {

    private String key; // Portfolio.id + User.key
    private String id;
    private String userId;

    @DynamoDBHashKey(attributeName = "Key")
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @DynamoDBAttribute(attributeName = "Id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @DynamoDBAttribute(attributeName = "UserId")
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    // FIXME: rewrite to use DB of positions belonging to a portfolio
    /*
    public void addPosition(Position p) {
        this.positions.put(p.getSymbol(), p);
    }
    */

    // FIXME: ObjectMapper takes this as another DB item
    public ArrayList<String> getSymbols() {

        // FIXME: scan all portfolio positions to find symbols in it

        ArrayList<String> symbols = new ArrayList<>();

        /*
        for (Map.Entry me : this.positions.entrySet()) {
            symbols.add((String) me.getKey());
        }

        */

        return symbols;
    }
}
