import java.net.InetAddress;

public class ClientInform {
    private InetAddress address;
    private int port;
    private int id;
    private String name;

    public ClientInform(String name, int id, InetAddress address, int port) {
        this.name = name;
        this.address = address;
        this.port = port;
        this.id = id;
    }

    public InetAddress getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
