import java.awt.*;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class Main {
    public static void main(String[] args) {
        Frame frame = new Frame("HTTP server", new Dimension(700, 500));
        try {
            ServerSocket serverSocket = new ServerSocket(8080, 0, InetAddress.getByName("localhost"));
            while (true){
                Socket clientSocket = serverSocket.accept();
                new HTTPServer(clientSocket, frame);
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
