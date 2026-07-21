package it.globalx.spigot.socket;

import java.io.*;
import java.net.Socket;
import java.util.function.Consumer;

public class     GlobalXSocket {

    private final Socket socket;
    private final BufferedReader in;
    private Consumer<String> onMessageReceived;

    public GlobalXSocket(String socketHost, int socketPort, Consumer<String> onMessageReceived) throws IOException {
        this.socket = new Socket(socketHost, socketPort);
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.onMessageReceived = onMessageReceived;

        new Thread(this::listenForMessages).start();
    }

    private void listenForMessages() {
        String incomingMessage;

        try {
            while ((incomingMessage = in.readLine()) != null) {
                if (onMessageReceived != null) {
                    onMessageReceived.accept(incomingMessage);
                }
            }
        } catch (IOException e) {
            System.err.println("Errore durante la lettura dei messaggi dal server: " + e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                System.err.println("Errore durante la chiusura del socket: " + e.getMessage());
            }
        }
    }

    public void setOnMessageReceived(Consumer<String> onMessageReceived) {
        this.onMessageReceived = onMessageReceived;
    }
}
