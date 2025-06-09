import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ChatClient {

    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 1234); // Connect to server
            System.out.println("📶 Connected to chat server");

            // Read messages from server in new thread
            new Thread(() -> {
                try {
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String serverMsg;
                    while ((serverMsg = in.readLine()) != null) {
                        System.out.println("🔔 " + serverMsg);
                    }
                } catch (IOException e) {
                    System.out.println("❌ Disconnected from server");
                }
            }).start();

            // Send user input to server
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            Scanner sc = new Scanner(System.in);
            while (true) {
                String message = sc.nextLine();
                out.println(message);
            }

        } catch (IOException e) {
            System.out.println("❌ Cannot connect to server");
        }
    }
}
