import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {
    private static Set<Socket> clientSockets = new HashSet<>();

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(1234);
        System.out.println("🚀 Server started on port 1234...");

        while (true) {
            Socket clientSocket = serverSocket.accept();
            clientSockets.add(clientSocket);
            System.out.println("✅ New client connected");

            // Create a new thread for each client
            new Thread(() -> handleClient(clientSocket)).start();
        }
    }

    private static void handleClient(Socket socket) {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String message;
            while ((message = in.readLine()) != null) {
                System.out.println("📩 Received: " + message);
                broadcast(message, socket);
            }

        } catch (IOException e) {
            System.out.println("❌ Client disconnected");
        } finally {
            try {
                clientSockets.remove(socket);
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Send message to all clients except the sender
    private static void broadcast(String message, Socket sender) throws IOException {
        for (Socket socket : clientSockets) {
            if (socket != sender) {
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                out.println(message);
            }
        }
    }
}
