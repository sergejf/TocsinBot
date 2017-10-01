/*
    Bot commands: must start with an object

        help                                -> display list of commands
        portfolio new "id"                  -> new portfolio
        portfolio "id" print                -> print portfolio positions
        portfolio "id" set "symbol" "quantity" "price" -> set long position in portfolio
        portfolio "id" screen "id" now      -> screen portfolio now
        portfolio "id" screen "id" daily    -> screen portfolio daily
        portfolio "id" screen "id" never    -> stop daily portfolio screen
        screen new "id" set "expression"    -> new screen and assign expression
        screen "id" print                   -> print expression for that screen
 */

/*  To run this you need:

    1) Inspect requests: http://localhost:4040/inspect/http
    2) Chat with bot: https://www.facebook.com/TocsinBot-491275257908762/inbox
 */

// FIXME: when adding delete, ensure referential integrity
// FIXME: use separators between concatenated key parts
// FIXME: delete local DB to erase old keys, etc. and refresh data
// TODO: add Junit to Intellij https://www.jetbrains.com/help/idea/configuring-testing-libraries.html
// TODO: add unit tests, http://www.vogella.com/tutorials/JUnit/article.html
// TODO: package Tocsin as jar file, https://www.jetbrains.com/help/idea/packaging-a-module-into-a-jar-file.html
// TODO: deploy jar to AWS using Elastic Beanstalk, http://docs.aws.amazon.com/elasticbeanstalk/latest/dg/java-se-platform.html
// TODO: long term, replace AlphaVantage with native indicator functions to handle LSE symbols, http://www.fmlabs.com/reference/

package fr.tocsin.bot;

import fr.tocsin.channel.Messenger;
import fr.tocsin.channel.MessengerCallback;
import fr.tocsin.datasource.AwsDynamoDB;
import fr.tocsin.identity.User;
import fr.tocsin.stock.Market;

public class Execute implements MessengerCallback {

    private Messenger facebook;

    public void receive(String from, String to, String body) {
        AwsDynamoDB db = AwsDynamoDB.getDB();
        User user;
        String input, output;

        // If user doesn't exist, create a new User
        user = db.getUser(from);
        if (user == null) {
            user = new User();
            user.setKey(from);
            user.setChannelId(from);
            db.setUser(user);
        }

        input = body;
        Understand bot = new Understand();
        output = bot.parseAndRespond(user, input);

        // Respond to user
        facebook.send(user.getChannelId(), output);
    }

    public Execute() {
        // Instantiate a Facebook Messenger channel
        facebook = new Messenger(this);
    }

    public static void main(String[] args) {

        // Initialize Messenger Bot
        Execute bot = new Execute();

        // Initialize DB
        AwsDynamoDB db = AwsDynamoDB.getDB();
        db.createTable("IndicatorValues");
        db.createTable("Bars");
        db.createTable("Users");
        db.createTable("Portfolios");
        db.createTable("Positions");

        // Initialize Market
        Market m = Market.getMarket();

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

        Screen s = new Screen("WILLR(14, S) < -60.0");
        ArrayList<String> matches = s.apply(pf);
        */
    }
}
