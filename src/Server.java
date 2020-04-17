import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;

public class Server {
    private static int port;
    private static DatagramSocket socket;
    private static boolean isRunnig;
    private static int clientId;
    private static ArrayList<ClientInform> clientInforms = new ArrayList<ClientInform>();

    public static void start(int port) {
        try {
            socket = new DatagramSocket(port);
            //this.port = port;
            System.out.println("Server started at port: " + port);
            isRunnig = true;
            listener();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void broadcastReciever(String message) {
        for (ClientInform info : clientInforms) {
            send(message, info.getAddress(), info.getPort());
        }
    }

    private static void send(String message, InetAddress address, int port) {
        try {
            message += "\\n";
            byte[] data = message.getBytes();
            DatagramPacket packet = new DatagramPacket(data,data.length);
            socket.send(packet);
            System.out.println("Send message to" + address.getHostAddress() + ":" + port);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void listener() {
        Thread listenerThread = new Thread("Chat listener") {
            public void run() {
                try {
                    while (isRunnig) {
                        byte[] data = new byte[1024];
                        DatagramPacket packet = new DatagramPacket(data, data.length);
                        socket.receive(packet);

                        String message = new String(data);
                        message = message.substring(0, message.indexOf("\\n"));

                        if (!isCommand(message, packet)) {
                            broadcastReciever(message);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        listenerThread.run();
    }

    /**
     * "\\con:[name]" - connection user to server
     * "\\dis:[id]" - disstonection user from server
     */

    public static boolean isCommand(String message, DatagramPacket packet) {
        if (message.startsWith("\\con:")) {
            String name = message.substring(message.indexOf(":")+1);
            clientInforms.add(new ClientInform(name, clientId++, packet.getAddress(), packet.getPort()));
            broadcastReciever("User " + name + " joined to server.");
            return true;
        }

        return false;
    }

    public static void close() {
        isRunnig = false;
    }
}
