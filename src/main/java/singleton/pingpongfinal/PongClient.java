package singleton.pingpongfinal;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class PongClient {
    private static final String SERVER_ADDRESS = "localhost"; // Cambia a la IP del servidor
    private static final int SERVER_PORT = 5555;

    private static PongController controller;

    public static void main(String[] args) {
        controller = new PongController();

        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            new Thread(() -> {
                String message;
                try {
                    while ((message = in.readLine()) != null) {
                        controller.handleServerMessage(message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
            String userInput;
            while ((userInput = stdIn.readLine()) != null) {
                out.println(userInput);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void sendMessage(String leftUp) {
    }
}



