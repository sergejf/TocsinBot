package fr.tocsin.bot;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StreamTokenizer;

public class Understand {

    public static final int TT_QUOTE = 34;

    public String parseAndRespond(String input) {
        String output = new String("Thanks");
        InputStream stream = new ByteArrayInputStream(input.getBytes());
        StreamTokenizer tokens = new StreamTokenizer(stream);
        try {
            while (tokens.nextToken() != StreamTokenizer.TT_EOF) {
                if (tokens.ttype == StreamTokenizer.TT_NUMBER) {
                    System.out.println("Number: " + tokens.nval);
                } else if (tokens.ttype == StreamTokenizer.TT_WORD) {
                    System.out.println("Word: " + tokens.sval);
                } else if (tokens.ttype == StreamTokenizer.TT_EOL) {
                    System.out.println("--End of Line--");
                } else if (tokens.ttype == TT_QUOTE) {
                    System.out.println("Quote: " + tokens.sval);
                } else {
                    System.out.println("Other: " + tokens.sval + " type " + Integer.toString(tokens.ttype));
                }
            }
        } catch (IOException e) {
            System.err.println("parseAndRespond failed. Input: " + input);
            System.err.println(e.getMessage());
            e.printStackTrace();
        }

        return output;
    }
}
