package fr.tocsin.channel;

public interface MessengerCallback {
    void receive(String from, String to, String body);
}
