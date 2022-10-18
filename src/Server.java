import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class Server {

    public static void main(String[] args) {
        ArrayList<Socket> user = new ArrayList<>();
        HashMap<Socket, String> userNameList = new HashMap<Socket, String>();
        try (ServerSocket serversocket = new ServerSocket(5000)) {
            System.out.println("O servidor foi iniciado...");
            while (true) {
                Socket socket = serversocket.accept();
                user.add(socket);
                ThreadServer ThreadServer = new ThreadServer(socket, user, userNameList);
                ThreadServer.start();
            }
        } catch (Exception e) {
            System.out.println(e.getStackTrace());
        }
    }
}