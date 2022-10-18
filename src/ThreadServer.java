import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Thread for Server
 */
public class ThreadServer extends Thread {

    private Socket socket;
    private ArrayList<Socket> user;
    private HashMap<Socket, String> userNameList;

    public ThreadServer(Socket socket, ArrayList<Socket> clients, HashMap<Socket, String> clientNameList) {
        this.socket = socket;
        this.user = clients;
        this.userNameList = clientNameList;
    }

    @Override
    public void run() {
        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            while (true) {
                String outputString = input.readLine();
                if (outputString.equals("logout")) {
                    throw new SocketException();
                }
                if (!userNameList.containsKey(socket)) {
                    String[] messageString = outputString.split(":", 2);
                    userNameList.put(socket, messageString[0]);
                    System.out.println(messageString[0] + messageString[1]);
                    showMessageToAllClients(socket, messageString[0] + messageString[1]);
                } else {
                    System.out.println(outputString);
                    showMessageToAllClients(socket, outputString);
                }
            }
        } catch (SocketException e) {
            String printMessage = userNameList.get(socket) + " saiu do bate-papo";
            System.out.println(printMessage);
            showMessageToAllClients(socket, printMessage);
            user.remove(socket);
            userNameList.remove(socket);
        } catch (Exception e) {
            System.out.println(e.getStackTrace());
        }
    }

    private void showMessageToAllClients(Socket sender, String outputString) {
        Socket socket;
        PrintWriter printWriter;
        int i = 0;
        while (i < user.size()) {
            socket = user.get(i);
            i++;
            try {
                if (socket != sender) {
                    printWriter = new PrintWriter(socket.getOutputStream(), true);
                    printWriter.println(outputString);
                }
            } catch (IOException ex) {
                System.out.println(ex);
            }
        }
    }
}