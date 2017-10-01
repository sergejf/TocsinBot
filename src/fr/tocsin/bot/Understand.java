package fr.tocsin.bot;

import fr.tocsin.Util;
import fr.tocsin.datasource.AwsDynamoDB;
import fr.tocsin.identity.User;
import fr.tocsin.stock.Portfolio;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StreamTokenizer;
import java.util.ArrayList;

public class Understand {

    private static final String BOT_OBJ_PORTFOLIO = "PORTFOLIO";
    private static final String BOT_OBJ_SCREEN = "SCREEN";
    private static final String BOT_OBJ_HELP = "HELP";

    private static final String BOT_ACT_NEW = "NEW";
    private static final String BOT_ACT_PRINT = "PRINT";
    private static final String BOT_ACT_SET = "SET";
    private static final String BOT_ACT_SCREEN = "SCREEN";

    private static final String BOT_TIM_NOW = "NOW";
    private static final String BOT_TIM_DAILY = "DAILY";
    private static final String BOT_TIM_NEVER = "NEVER";

    private static final String BOT_RES_HELP = "Here's what you can ask me (quotes, if specified, are required):\n" +
            "portfolio new \"id\" -> new portfolio\n" +
            "portfolio \"id\" print -> print portfolio positions\n" +
            "portfolio \"id\" set \"symbol\" \"quantity\" \"price\" -> set long position in portfolio\n" +
            "portfolio \"id\" screen \"id\" now -> screen portfolio now\n" +
            "portfolio \"id\" screen \"id\" daily -> screen portfolio daily\n" +
            "portfolio \"id\" screen \"id\" never -> stop daily portfolio screen\n" +
            "screen new \"id\" set \"expression\" -> new screen and set expression\n" +
            "screen \"id\" print -> print expression for that screen";

    private static final String BOT_RES_OK_PORTFOLIO_NEW = "New portfolio successfully created.";

    private static final String BOT_RES_NOMATCH = "Sorry, I don't understand. Please type \"help\" (no quotes) for a list of valid inputs.";

    private static final int TT_QUOTE = 34;

    public String parseAndRespond(User user, String input) {
        AwsDynamoDB db = AwsDynamoDB.getDB();

        ArrayList<String> texts = new ArrayList<>();
        ArrayList<Integer> types = new ArrayList<>();

        // Tokenize input
        InputStream stream = new ByteArrayInputStream(input.getBytes());
        StreamTokenizer tokens = new StreamTokenizer(stream);
        try {
            while (tokens.nextToken() != StreamTokenizer.TT_EOF) {
                texts.add(tokens.sval.toUpperCase());
                types.add(tokens.ttype);
            }
        } catch (IOException e) {
            System.err.println("parseAndRespond failed. Input: " + input);
            System.err.println(e.getMessage());
            e.printStackTrace();
        }

        // Parse input
        if (types.get(0) == StreamTokenizer.TT_WORD) {
            // HELP
            if (texts.get(0).equals(BOT_OBJ_HELP)) {
                return BOT_RES_HELP;
                // PORTFOLIO
            } else if (texts.get(0).equals(BOT_OBJ_PORTFOLIO)) {
                if (types.get(1) == StreamTokenizer.TT_WORD || types.get(1) == TT_QUOTE) {
                    // PORTFOLIO NEW
                    if (texts.get(1).equals(BOT_ACT_NEW)) {
                        // PORTFOLIO NEW "ID"
                        if (types.get(2) == TT_QUOTE) {
                            // Action: create new portfolio for that user
                            Portfolio portfolio = new Portfolio();
                            portfolio.setKey(Util.stripQuotes(texts.get(2)) + user.getKey());
                            portfolio.setId(Util.stripQuotes(texts.get(2)));
                            portfolio.setUserId(user.getKey());
                            db.setPortfolio(portfolio);
                            return BOT_RES_OK_PORTFOLIO_NEW;
                        } else {
                            return BOT_RES_NOMATCH;
                        }
                    } else {
                        // PORTFOLIO "ID"
                        // TODO: complete
                    }
                } else {
                    return BOT_RES_NOMATCH;
                }
                // SCREEN
            } else if (texts.get(0).equals(BOT_OBJ_SCREEN)) {
                // TODO: complete
            } else {
                return BOT_RES_NOMATCH;
            }
        } else {
            return BOT_RES_NOMATCH;
        }
        // FIXME: is it needed?
        return BOT_RES_NOMATCH;
    }
}
