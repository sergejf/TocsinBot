package fr.tocsin.channel;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import fr.tocsin.Properties;

import static spark.Spark.port;
import static spark.Spark.post;

public class Messenger {

    private MessengerCallback callback;
    private String myChannelID;

    public Messenger(MessengerCallback cb) {
        this.callback = cb;

        Properties cfg = new Properties();
        port(Integer.parseInt(cfg.getProperty("messenger.port")));

        post("/input", (request, response) -> {

            // Get request parameters
            String from, to, body;
            from = request.queryParams("From");
            to = request.queryParams("To");
            body = request.queryParams("Body");

            // Submit request parameters to callback
            callback.receive(from, to, body);

            // Respond with http status 200
            response.status(200);
            return response;
        });

        post("/status", (request, response) -> {
            response.status(200);
            return response;
        });
    }

    public String send(String to, String body) {
        Properties cfg = new Properties();
        HttpResponse<String> res;
        String data;
        String req = "https://api.twilio.com/2010-04-01/Accounts/" + cfg.getProperty("twilio.sid") + "/Messages.json";

        try {
            String userChannelID = myChannelID = cfg.getProperty("tmp.user.id"); // FIXME needs DB integration
            myChannelID = cfg.getProperty("messenger.id");
            res = Unirest.post(req)
                    .header("content-type", "application/x-www-form-urlencoded")
                    .header("authorization", "Basic " + cfg.getProperty("twilio.api"))
                    .header("cache-control", "no-cache")
                    .body("To=" + userChannelID + "&From=" + myChannelID + "&Body=" + body)
                    .asString();
            data = res.getBody();
        } catch (UnirestException e) {
            System.err.println("output failed. To: " + to + "Body: " + body);
            System.err.println(e.getMessage());
            e.printStackTrace();
            data = null;
        }
        return data;
    }

}
